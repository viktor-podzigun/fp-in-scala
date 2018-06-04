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

object Option {

  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    Some(List.foldRight(a, Nil: List[A]) {
      case (None, _) => return None
      case (Some(x), res) => Cons(x, res)
    })
  }

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    Some(List.reverse(List.foldLeft(a, Nil: List[B]) { (res, x) =>
      f(x) match {
        case None => return None
        case Some(b) => Cons(b, res)
      }
    }))
  }

  def sequence2[A](a: List[Option[A]]): Option[List[A]] = traverse(a)(identity)
}

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]
