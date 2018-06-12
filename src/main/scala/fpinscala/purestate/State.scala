package fpinscala.purestate

case class State[S, +A](run: S => (A, S)) {

  def apply(s: S): (A, S) = run(s)

  def map[B](f: A => B): State[S, B] = State { s =>
    val (a, s2) = run(s)
    (f(a), s2)
  }

  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = State { s =>
    val (a, s2) = run(s)
    val (b, s3) = sb(s2)
    (f(a, b), s3)
  }

  def flatMap[B](g: A => State[S, B]): State[S, B] = State { s =>
    val (a, s2) = run(s)
    g(a).run(s2)
  }
}

object State {

  def unit[S, A](a: A): State[S, A] = State { s => (a, s) }

  def sequence[S, A](fs: List[State[S, A]]): State[S, List[A]] = State { s =>
    val (list, n) = fs.foldLeft((Nil: List[A], s)) { case ((res, next), r) =>
      val (i, next2) = r(next)
      (i :: res, next2)
    }

    (list.reverse, n)
  }

  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
}
