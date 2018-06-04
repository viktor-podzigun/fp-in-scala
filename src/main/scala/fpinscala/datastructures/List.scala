package fpinscala.datastructures

import scala.annotation.tailrec

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {

  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x,xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x,xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(ns: List[Int]): Int = foldRight(ns, 0)((x,y) => x + y)

  def product2(ns: List[Double]): Double = foldRight(ns, 1.0)(_ * _)

  def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B = {
    @tailrec
    def loop(xs: List[A], res: B): B = xs match {
      case Nil => res
      case Cons(h, t) => loop(t, f(res, h))
    }

    loop(as, z)
  }

  def reverse[A](list: List[A]): List[A] = list match {
    case Nil => list
    case Cons(_, Nil) => list
    case _ => foldLeft(list, Nil: List[A])((res, x) => Cons(x, res))
  }
}
