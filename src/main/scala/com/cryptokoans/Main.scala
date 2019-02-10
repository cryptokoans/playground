package com.cryptokoans

import us.oyanglul.owlet._
import DOM._
import cats.implicits._
import monix.execution.Scheduler.Implicits.global

object Main {
  def main(args: scala.Array[String]): Unit = {
    val a1 = number("a1", 1)
    val a2 = number("a2", 2)
    val a3 = number("a3", 3)
    val sum = fx[Double, Double](_.sum, List(a1, a2, a3))
    val product = fx[Double, Double](_.product, List(a1, a2, a3, sum))
    render(a1 &> a2 &> a3 &> sum &> product, "#app").runSyncStep
  }
}


/*import outwatch.dom._
import outwatch.dom.dsl._
import monix.execution.Scheduler.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {

  OutWatch.renderInto("#app", h1("Hello World")).unsafeRunSync()
}
}
*/