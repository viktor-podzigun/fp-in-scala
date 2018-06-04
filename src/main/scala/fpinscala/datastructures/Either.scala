package fpinscala.datastructures

sealed trait Either[+E, +A] {

  def map[B](f: A => B): Either[E, B] = this match {
    case Right(v) => Right(f(v))
    case Left(v) => Left(v)
  }

  /**
    * When mapping over the right side, we must promote the left type parameter to some supertype,
    * to satisfy the +E variance annotation.
    */
  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
    case Right(v) => f(v)
    case Left(v) => Left(v)
  }

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
    case Right(_) => this
    case _ => b
  }

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = this match {
    case Right(a) => b match {
      case Right(bb) => Right(f(a, bb))
      case Left(bb) => Left(bb)
    }
    case Left(v) => Left(v)
  }
}

case class Left[+E](value: E) extends Either[E, Nothing]

case class Right[+A](value: A) extends Either[Nothing, A]
