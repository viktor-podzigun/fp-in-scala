
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
      case Cons(_, t) if n > 0 => loop(t, n - 1)
      case _ => xs
    }

    loop(l, n)
  }

  /**
    * Exercise 3.5
    *
    * Implement `dropWhile`, which removes elements from the `List` prefix as long as they match a predicate.
    */
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = {
    @tailrec
    def loop(xs: List[A]): List[A] = xs match {
      case Cons(h, t) if f(h) => loop(t)
      case _ => xs
    }

    loop(l)
  }

  /**
    * Exercise 3.6
    *
    * Not everything works out so nicely. Implement a function, `init`, that returns a `List`
    * consisting of all but the last element of a `List`.
    * So, given `List(1,2,3,4)`, `init` will return `List(1,2,3)`.
    * Why can't this function be implemented in constant time like `tail`?
    */
  def init[A](l: List[A]): List[A] = {
    def loop(xs: List[A]): List[A] = xs match {
      case Nil => xs
      case Cons(_, Nil) => Nil
      case Cons(h, t) => Cons(h, loop(t))
    }

    loop(l)
  }

  /**
    * Exercise 3.7
    *
    * Can `product`, implemented using `foldRight`, immediately halt the recursion and return `0.0`
    * if it encounters a `0.0`?
    * Why or why not?
    * Consider how any short-circuiting might work if you call `foldRight` with a large list.
    */
  def product(ns: List[Double]): Double = foldRight(ns, 1.0) { (x, y) =>
    println(s"$x * $y")
    if (x == 0.0) {
      return 0.0
    }

    x * y
  }

  /**
    * Exercise 3.8
    *
    * See what happens when you pass `Nil` and `Cons` themselves to `foldRight`, like this:
    * `foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_))`
    *
    * What do you think this says about the relationship between `foldRight`
    * and the data constructors of `List`?
    */
  def foldRightWithNilAndCons: List[Int] =
    foldRight(List(1, 2, 3), Nil: List[Int])(Cons(_, _))

  /**
    * Exercise 3.9
    *
    * Compute the length of a list using `foldRight`.
    */
  def length[A](as: List[A]): Int = foldRight(as, 0) { (_, res) =>
    res + 1
  }

  /**
    * Exercise 3.10
    *
    * Our implementation of `foldRight` is not tail-recursive and will result in a `StackOverflowError`
    * for large lists (we say it's not stack-safe).
    * Convince yourself that this is the case, and then write another general list-recursion function,
    * `foldLeft`, that is tail-recursive, using the techniques we discussed in the previous chapter.
    */
  def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B = {
    @tailrec
    def loop(xs: List[A], res: B): B = xs match {
      case Nil => res
      case Cons(h, t) => loop(t, f(res, h))
    }

    loop(as, z)
  }

  /**
    * Exercise 3.11
    *
    * Write `sum`, `product`, and a function to compute the length of a list using `foldLeft`.
    */
  def sum3(ns: List[Int]): Int = foldLeft(ns, 0)((x, y) => x + y)

  def product3(ns: List[Double]): Double = foldLeft(ns, 1.0)(_ * _)

  def length3[A](as: List[A]): Int = foldLeft(as, 0) { (res, _) =>
    res + 1
  }

  /**
    * Exercise 3.12
    *
    * Write a function that returns the reverse of a list (given `List(1,2,3)` it returns `List(3,2,1)`).
    * See if you can write it using a fold.
    */
  def reverse[A](list: List[A]): List[A] = list match {
    case Nil => list
    case Cons(_, Nil) => list
    case _ => foldLeft(list, Nil: List[A])((res, x) => Cons(x, res))
  }

  /**
    * Exercise 3.13
    *
    * Hard: Can you write `foldLeft` in terms of foldRight? How about the other way around?
    * Implementing `foldRight` via `foldLeft` is useful because it lets us implement `foldRight`
    * tail-recursively, which means it works even for large lists without overflowing the stack.
    */
  def foldRight2[A, B](list: List[A], z: B)(f: (A, B) => B): B = {
    foldLeft(reverse(list), z)((res, x) => f(x, res))
  }

  /**
    * Exercise 3.14
    *
    * Implement `append` in terms of either `foldLeft` or `foldRight`.
    */
  def append[A](list: List[A], x: A): List[A] = {
    foldRight2(list, Cons(x, Nil))((x, res) => Cons(x, res))
  }
}
