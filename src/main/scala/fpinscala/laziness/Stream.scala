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

  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }

  def takeWhileUsingFoldRight(p: A => Boolean): Stream[A] = {
    foldRight(Stream.empty[A]) { (a, b) =>
      if (p(a)) Stream.cons(a, b)
      else Stream.empty
    }
  }

  def headOptionUsingFoldRight: Option[A] = {
    foldRight(None: Option[A]) { (a, _) =>
      Some(a)
    }
  }

  def map[B](f: A => B): Stream[B] = {
    foldRight(Stream.empty[B]) { (a, b) =>
      Stream.cons(f(a), b)
    }
  }

  def filter(f: A => Boolean): Stream[A] = {
    foldRight(Stream.empty[A]) { (a, b) =>
      if (f(a)) Stream.cons(a, b)
      else b
    }
  }

  def append[AA >: A](b: => Stream[AA]): Stream[AA] = {
    foldRight(b) { (a, b) =>
      Stream.cons(a, b)
    }
  }

  def flatMap[B](f: A => Stream[B]): Stream[B] = {
    foldRight(Stream.empty[B]) { (a, b) =>
      f(a).append(b)
    }
  }

  def mapUsingUnfold[B](f: A => B): Stream[B] = Stream.unfold(this) {
    case Empty => None
    case Cons(h, t) => Some((f(h()), t()))
  }

  def takeUsingUnfold(n: Int): Stream[A] = Stream.unfold((this, n)) {
    case (Cons(h, t), i) if i > 0 => Some((h(), (t(), i - 1)))
    case _ => None
  }

  def takeWhileUsingUnfold(p: A => Boolean): Stream[A] = Stream.unfold(this) {
    case Cons(h, t) if p(h()) => Some((h(), t()))
    case _ => None
  }

  def zip[B](s2: Stream[B]): Stream[(A, B)] = zipWithUsingUnfold(s2)((_, _))
  
  def find(p: A => Boolean): Option[A] = {
    @tailrec
    def loop(s: Stream[A], res: Option[A]): Option[A] = s match {
      case Cons(h, _) if p(h()) => Some(h())
      case Cons(_, t) => loop(t(), res)
      case _ => res
    }

    loop(this, None)
  }
  
  def zipWithUsingUnfold[B, C](s2: Stream[B])(f: (A, B) => C): Stream[C] = {
    Stream.unfold((this, s2)) {
      case (Cons(h1, t1), Cons(h2, t2)) => Some((f(h1(), h2()), (t1(), t2())))
      case _ => None
    }
  }

  def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] = {
    Stream.unfold((this, s2)) {
      case (Cons(h1, t1), Cons(h2, t2)) => Some(((Some(h1()), Some(h2())), (t1(), t2())))
      case (Cons(h1, t1), Empty) => Some(((Some(h1()), None), (t1(), Empty)))
      case (Empty, Cons(h2, t2)) => Some(((None, Some(h2())), (Empty, t2())))
      case _ => None
    }
  }

  def startsWith[AA >: A](s: Stream[AA]): Boolean = {
    zipAll(s).takeWhile {
      case (_, Some(_)) => true
      case _ => false
    }.forAll {
      case (a, b) => a == b
    }
  }

  def tails: Stream[Stream[A]] = {
    Stream.unfold((this, true)) {
      case (s@Cons(_, t), _) => Some((s, (t(), true)))
      case (Empty, true) => Some((Empty, (Empty, false)))
      case (Empty, false) => None
    }
  }

  def scanRight[B](z: B)(f: (A, B) => B): Stream[B] = {
    foldRight(Stream.cons(z, Stream.empty)) { (a, bb) =>
      val Cons(b, _) = bb
      Stream.cons(f(a, b()), bb)
    }
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

  def constant[A](a: A): Stream[A] = {
    lazy val res: Stream[A] = cons(a, res)
    res
  }

  def from(n: Int): Stream[Int] = {
    lazy val res: Stream[Int] = cons(n, res.map(_ + 1))
    res
  }

  def fibs(): Stream[Int] = {
    def loop(n1: Int, n2: Int): Stream[Int] = {
      cons(n1, loop(n2, n1 + n2))
    }

    loop(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = {
    def loop(z: S): Stream[A] = f(z) match {
      case None => empty
      case Some((a, s)) => cons(a, loop(s))
    }

    loop(z)
  }

  def onesUsingUnfold: Stream[Int] = unfold(1)(s => Some((1, s)))

  def constantUsingUnfold[A](a: A): Stream[A] = unfold(a)(s => Some((a, s)))

  def fromUsingUnfold(n: Int): Stream[Int] = unfold(n)(s => Some((s, s + 1)))

  def fibsUsingUnfold(): Stream[Int] = {
    unfold((0, 1)) { case (n1, n2) => Some((n1, (n2, n1 + n2))) }
  }
}
