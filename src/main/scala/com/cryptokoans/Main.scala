package com.cryptokoans

import us.oyanglul.owlet._
import DOM._
import cats.implicits._
import monix.execution.Scheduler.Implicits.global
import cats._
import monix.reactive.subjects.Var

object Main {
  def main(args: scala.Array[String]): Unit = {
    val greeting = Map(
      "Chinese" -> "你好",
      "English" -> "Hello",
      "French" -> "Salut"
    )
    val selectBox = label(select("pierer", Var(greeting), "你好"), "Language")
    val hello = for {
      selected <- selectBox
      towho <- if (selected == "你好") string("name", "继超")
      else string("name", "Jichao")
    } yield towho
    renderOutput(selectBox |+| " ".pure[Owlet] |+| hello, "#app").runSyncStep
  }
}
