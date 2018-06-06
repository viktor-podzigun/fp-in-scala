package fpinscala.laziness

import scala.annotation.tailrec

sealed trait Stream[+A] {

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }

  def toList: List[A] = {
    @tailrec
    def loop(s: Stream[A], res: List[A]): List[A] = s match {
      case Empty => res
      case Cons(h, t) => loop(t(), h() :: res)
    }

    loop(this, Nil).reverse
  }

  def take(n: Int): Stream[A] = {
    def loop(s: Stream[A], i: Int): Stream[A] = s match {
      case Cons(h, t) if i < n => Stream.cons(h(), loop(t(), i + 1))
      case _ => Stream.empty
    }

    loop(this, 0)
  }

  def drop(n: Int): Stream[A] = {
    @tailrec
    def loop(s: Stream[A], i: Int): Stream[A] = s match {
      case Cons(_, t) if i > 0 => loop(t(), i - 1)
      case _ => s
    }

    loop(this, n)
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    def loop(s: Stream[A]): Stream[A] = s match {
      case Cons(h, t) if p(h()) => Stream.cons(h(), loop(t()))
      case _ => Stream.empty
    }

    loop(this)
  }

  def forAll(p: A => Boolean): Boolean = {
    @tailrec
    def loop(s: Stream[A], res: Boolean): Boolean = s match {
      case Cons(h, _) if !p(h()) => false
      case Cons(_, t) => loop(t(), res)
      case _ => res
    }

    loop(this, res = true)
  }
}

case object Empty extends Stream[Nothing]

/**
  * A nonempty stream consists of a head and a tail, which are both non-strict.
  * Due to technical limitations, these are thunks that must be explicitly forced,
  * rather than by-name parameters.
  */
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {

  /**
    * A smart constructor for creating a nonempty stream.
    */
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  /**
    * A smart constructor for creating an empty stream of a particular type.
    */
  def empty[A]: Stream[A] = Empty

  /**
    * A convenient variable-argument method for constructing a Stream from multiple elements.
    */
  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
}
