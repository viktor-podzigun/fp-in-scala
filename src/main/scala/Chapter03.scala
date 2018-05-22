
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
  def append[A](a1: List[A], a2: List[A]): List[A] = a2 match {
    case Nil => a1
    case _ => foldRight2(a1, a2)((x, res) => Cons(x, res))
  }

  /**
    * Exercise 3.15
    *
    * Hard: Write a function that concatenates a list of lists into a single list.
    * Its runtime should be linear in the total `length` of all lists.
    * Try to use functions we have already defined.
    */
  def concatenate[A](lists: List[List[A]]): List[A] = {
    foldRight2(lists, Nil: List[A])((x, res) => append(x, res))
  }

  /**
    * Exercise 3.16
    *
    * Write a function that transforms a list of integers by adding `1` to each element.
    * (Reminder: this should be a pure function that returns a new `List`!)
    */
  def increment(list: List[Int]): List[Int] = {
    foldRight2(list, Nil:List[Int])((x, res) => Cons(x + 1, res))
  }

  /**
    * Exercise 3.17
    *
    * Write a function that turns each value in a `List[Double]` into a `String`.
    * You can use the expression `d.toString` to convert some `d: Double` to a `String`.
    */
  def toString(list: List[Double]): List[String] = {
    foldRight2(list, Nil:List[String])((x, res) => Cons(x.toString, res))
  }

  /**
    * Exercise 3.18
    *
    * Write a function `map` that generalizes modifying each element in a list
    * while maintaining the structure of the list.
    */
  def map[A, B](as: List[A])(f: A => B): List[B] = {
    reverse(foldLeft(as, Nil:List[B])((res, x) => Cons(f(x), res)))
  }

  /**
    * Exercise 3.19
    *
    * Write a function `filter` that removes elements from a list unless they satisfy a given predicate.
    * Use it to remove all odd numbers from a `List[Int]`.
    */
  def filter[A](as: List[A])(f: A => Boolean): List[A] = {
    reverse(foldLeft(as, Nil:List[A]) { (res, x) =>
      if (f(x)) Cons(x, res)
      else res
    })
  }

  /**
    * Exercise 3.20
    *
    * Write a function flatMap that works like `map` except that the function given will return a list
    * instead of a single result, and that list should be inserted into the final resulting list.
    *
    * For instance, `flatMap(List(1,2,3))(i => List(i,i))` should result in `List(1,1,2,2,3,3)`.
    */
  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = {
    concatenate(reverse(foldLeft(as, Nil:List[List[B]])((res, x) => Cons(f(x), res))))
  }
}
