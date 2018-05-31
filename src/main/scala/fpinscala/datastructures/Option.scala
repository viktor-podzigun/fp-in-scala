package fpinscala.datastructures

sealed trait Option[+A] {

  /** Apply `f` if the `Option` is not `None`.
    */
  def map[B](f: A => B): Option[B] = this match {
    case None => None
    case Some(v) => Some(f(v))
  }

  /** Apply `f`, which may fail, to the `Option` if not `None`.
    */
  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case None => None
    case Some(v) => f(v)
  }

  def getOrElse[B >: A](default: => B): B = this match {
    case None => default
    case Some(v) => v
  }

  /** Don't evaluate `ob` unless needed.
    */
  def orElse[B >: A](ob: => Option[B]): Option[B] = {
    if (this == None) ob
    else this
  }

  /** Convert `Some` to `None` if the value doesn't satisfy `f`.
    */
  def filter(f: A => Boolean): Option[A] = this match {
    case Some(v) if f(v) => this
    case _ => None
  }
}

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]
