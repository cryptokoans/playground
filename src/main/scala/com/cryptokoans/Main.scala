package com.cryptokoans

import us.oyanglul.owlet._
import DOM._
import cats.implicits._
import monix.execution.Scheduler.Implicits.global
import cats._
import cats.implicits._
import org.scalajs.dom.crypto.HashAlgorithm

import scala.scalajs.js.typedarray.Uint8Array
//import monix.reactive.subjects.Var
import fluence.crypto.hash.JsCryptoHasher
import scodec.bits.ByteVector

object Main {

  implicit class LabelSyntaxFromString(s: String) {
    def --->[A](owlet: Owlet[A]): Owlet[A] = label(owlet,s)
  }

  def main(args: scala.Array[String]): Unit = {
    val hashedSecret = "Hashed Secret (hex encoded)" ---> string("hashed secret", "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad")
    val secret = "Secret (string)" ---> string("secret","abc")
    val numBits = "Number of Bits" ---> secret.map(_.getBytes.length * 8)
    //val secretText = hashedSecret flatMap(s => text(s))
    val candidateHash = secret.flatMap(s => JsCryptoHasher.Sha256[Owlet](s.getBytes).value).map {
      case Left(e) => e.message
      case Right(h) => ByteVector(h).toHex
    }
    val verified = (hashedSecret,candidateHash) parMapN {
      case (hs,ch) => hs == ch
    }

    val hashExample = div(h1("hash example") &> div(hashedSecret) &> div(secret) &> div(text("Num of Bits:") &> output(numBits)) &> output(hashedSecret) &> output(candidateHash) &> output(verified))

    val numOfItem = int("noi", 3)
    val items = numOfItem
      .flatMap(
        no => (0 to no).toList.parTraverse(i => string("inner", i.toString))
      )
    val monadExample = div(h1("monad example") &> text("monad example") &> numOfItem &> items &> output(numOfItem &> items))

    val a1 = number("a1",1)
    val onestring: Option[Double] => String = {
      case Some(d) => d.toString
      case None => "whoops!"
    }
    val traversed = fx(onestring,Option(a1))
    val traverseExample = div(h1("traverse example") &> a1 &> output(traversed))


    val linked = numOfItem.flatMap(i => a(text(s"$i items"),i).fold(i)(_ + _))



    val stringOrInt = toggleK(secret,numOfItem)

    val mytextBox = string("mytextbox","mytextbox")
    val mytextBox2 = mytextBox

    render( hashExample &> monadExample &> traverseExample &> div(stringOrInt) &> output(stringOrInt) &> div(mytextBox &> div(mytextBox2)), "#app").runSyncStep
  }
}
