package fix

import scala.concurrent.ExecutionContext.global

object Scalafixmyrules {
  implicit val a: Int = 123

  def fun(implicit i : Int) = println(i)

  val hello = fun
  // Add code that needs fixing here.
}
