package fpinscala.parallelism

/**
  * Container type for parallel computation.
  *
  * @tparam A a type of computed value
  */
trait Par[+A]

object Par {

  /**
    * Takes an unevaluated A and returns a computation that might evaluate it
    * in a separate thread.
    *
    * We call it `unit` because in a sense it creates a unit of parallelism
    * that just wraps a single value.
    *
    * @param a an unevaluated value
    * @tparam A the type of the value
    * @return a parallel computation that evaluates the value
    */
  def unit[A](a: => A): Par[A] = ???

  /**
    * Extracts the resulting value from a parallel computation.
    *
    * @param a
    * @tparam A
    * @return
    */
  def get[A](a: Par[A]): A = ???

  /**
    * Combines the result of two parallel computations.
    */
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???
}
