
import fpinscala.datastructures._

object Chapter04 {

  def mean(xs: Seq[Double]): Option[Double] = {
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)
  }

  /**
    * Exercise 4.1
    *
    * Implement all of the preceding functions on `Option`.
    * As you implement each function, try to think about what it means
    * and in what situations you'd use it.
    *
    * @see [[Option.map]]
    * @see [[Option.flatMap]]
    * @see [[Option.getOrElse]]
    * @see [[Option.orElse]]
    * @see [[Option.filter]]
    */

  /**
    * Exercise 4.2
    *
    * Implement the `variance` function in terms of `flatMap`.
    * If the `mean` of a sequence is `m`, the `variance` is the `mean` of `math.pow(x - m, 2)`
    * for each element `x` in the sequence.
    */
  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs).flatMap { m =>
      mean(xs.map { x =>
        math.pow(x - m, 2)
      })
    }
  }
}
