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
  /*def main(args: scala.Array[String]): Unit = {
    val baseInput = number("Base", 2.0)
    val exponentInput = number("Exponent", 10.0)
    val pow = (baseInput, exponentInput) parMapN math.pow
    renderOutput(pow, "#app").runSyncStep
  }*/
  def main(args: scala.Array[String]): Unit = {
    val label = text("my label")
    val secret = string("secret","secret")
    val hashedSecret = secret.map(s => ByteVector(JsCryptoHasher.Sha256.unsafe(s.getBytes)).toHex)
    renderOutput(hashedSecret,"#app").runSyncStep
  }
}
