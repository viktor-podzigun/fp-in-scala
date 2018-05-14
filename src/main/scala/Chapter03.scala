
import fpinscala.datastructures._
import fpinscala.datastructures.List._

object Chapter03 {

  /**
    * Exercise 3.1
    *
    * What will be the result of the following match expression?
    */
  def matchResult(): Int = {
    List(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + sum(t)
      case _ => 101
    }
  }
}
