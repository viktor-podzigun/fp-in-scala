
import fpinscala.datastructures.List._
import fpinscala.datastructures._

import scala.annotation.tailrec

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

  /**
    * Exercise 3.3
    *
    * Using the same idea, implement the function `setHead` for replacing the first element
    * of a List with a different value.
    */
  def setHead[A](xs: List[A], x: A): List[A] = xs match {
    case Nil => Nil
    case Cons(_, t) => Cons(x, t)
  }

  /**
    * Exercise 3.4
    *
    * Generalize `tail` to the function `drop`, which removes the first `n` elements from a list.
    * Note that this function takes time proportional only to the number of elements being dropped —
    * we don't need to make a copy of the entire `List`.
    */
  def drop[A](l: List[A], n: Int): List[A] = {
    @tailrec
    def loop(xs: List[A], n: Int): List[A] = xs match {
      case _ if n <= 0 => xs
      case Nil => xs
      case Cons(_, t) => loop(t, n - 1)
    }

    loop(l, n)
  }
}
