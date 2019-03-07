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
    val candidate = secret map hashString
    val verified = (hashedSecret,secret) parMapN hashverify
    val another = JsCryptoHasher.Sha256[Owlet]("abc".getBytes).map(ByteVector(_).toHex).value.flatMap{
      case Left(e) => text(e.message)
      case Right(v) => text(v)
    }
    render(div(hashedSecret) &> div(secret) &> output(hashedSecret) &> output(candidate) &> output(verified) &> another, "#app").runSyncStep
  }

  def hashString(input: String): String = ByteVector(JsCryptoHasher.Sha256.unsafe(input.getBytes)).toHex
  def hashverify(hashed: String, secret: String): Boolean = ByteVector(JsCryptoHasher.Sha256.unsafe(secret.getBytes)).toHex == hashed
}
