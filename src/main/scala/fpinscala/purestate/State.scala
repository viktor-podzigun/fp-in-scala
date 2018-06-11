package fpinscala.purestate

case class State[S, +A](run: State.State[S, A]) {

  def map[B](f: A => B): State.State[S, B] = { s =>
    val (a, s2) = run(s)
    (f(a), s2)
  }

  def map2[B, C](sb: State.State[S, B])(f: (A, B) => C): State.State[S, C] = { s =>
    val (a, s2) = run(s)
    val (b, s3) = sb(s2)
    (f(a, b), s3)
  }

  def flatMap[B](g: A => State.State[S, B]): State.State[S, B] = { s =>
    val (a, s2) = run(s)
    g(a)(s2)
  }
}

object State {

  type State[S, +A] = S => (A, S)

  def unit[S, A](a: A): State[S, A] = s => (a, s)

  def sequence[S, A](fs: List[State.State[S, A]]): State.State[S, List[A]] = { s =>
    val (list, n) = fs.foldLeft((Nil: List[A], s)) { case ((res, next), r) =>
      val (i, next2) = r(next)
      (i :: res, next2)
    }

    (list.reverse, n)
  }
}
