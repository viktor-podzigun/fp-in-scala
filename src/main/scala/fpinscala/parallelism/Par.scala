package fpinscala.parallelism

/**
  * Container type for parallel computation.
  *
  * @tparam A a type of computed value
  */
sealed trait Par[+A]

case class ParUnit[+A](value: A) extends Par[A]

case class ParFork[+A](value: () => Par[A]) extends Par[A]

object Par {

  /**
    * Creates a computation that immediately results in the value a.
    */
  def unit[A](a: A): Par[A] = ParUnit(a)

  /**
    * Combines the results of two parallel computations with a binary function.
    */
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???

  /**
    * Wraps the expression a for concurrent evaluation by `run`.
    */
  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  /**
    * Marks a computation for concurrent evaluation by `run`.
    */
  def fork[A](a: => Par[A]): Par[A] = ParFork(() => a)

  /**
    * Fully evaluates a given Par, spawning parallel computations
    * as requested by `fork` and extracting the resulting value.
    */
  def run[A](a: Par[A]): A = ???
}
