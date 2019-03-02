package com.cryptokoans

import us.oyanglul.owlet._
import DOM._
import cats.implicits._
import monix.execution.Scheduler.Implicits.global
import cats._
import cats.implicits._
import monix.reactive.subjects.Var

object Main {
  def main(args: scala.Array[String]): Unit = {
    val baseInput = number("Base", 2.0)
    val exponentInput = number("Exponent", 10.0)
    val pow = (baseInput, exponentInput) parMapN math.pow
    renderOutput(pow, "#app").runSyncStep
  }
}
