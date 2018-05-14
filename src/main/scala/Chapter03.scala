
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

  /**
    * Exercise 3.2
    *
    * Implement the function tail for removing the first element of a List.
    * Note that the function takes constant time.
    *
    * What are different choices you could make in your implementation if the List is Nil?
    * We'll return to this question in the next chapter.
    */
  def tail[A](xs: List[A]): List[A] = xs match {
    case Nil => Nil
    case Cons(_, t) => t
  }
}
