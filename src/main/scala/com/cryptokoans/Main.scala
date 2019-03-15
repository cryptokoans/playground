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

  def main(args: scala.Array[String]): Unit = {
    val hashedSecret = label(string("hashed secret", "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"),"Hashed Secret (hex encoded)")
    val secret = label(string("secret","abc"),"Secret (string)")
    //val secretText = hashedSecret flatMap(s => text(s))
    val candidateHash = secret.flatMap(s => JsCryptoHasher.Sha256[Owlet](s.getBytes).value).map {
      case Left(e) => e.message
      case Right(h) => ByteVector(h).toHex
    }
    val verified = (hashedSecret,candidateHash) parMapN {
      case (hs,ch) => hs == ch
    }
    render(div(hashedSecret) &> div(secret) &> output(hashedSecret) &> output(candidateHash) &> output(verified), "#app").runSyncStep
  }
}
