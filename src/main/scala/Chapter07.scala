import fpinscala.parallelism.Par._
import fpinscala.parallelism._

object Chapter07 {

  /**
    * Exercise 7.1
    *
    * `Par.map2` is a new higher-order function for combining the result
    * of two parallel computations. What is its signature?
    * Give the most general signature possible (don't assume it works only for `Int`).
    *
    * @see [[Par.map2]]
    */
  def sum(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.size <= 1) {
      Par.unit(ints.headOption getOrElse 0)
    }
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(sum(l), sum(r))(_ + _)
    }

  /**
    * Exercise 7.2
    *
    * Before continuing, try to come up with representations for `Par`
    * that make it possible to implement the functions of our API.
    */

  /**
    * Exercise 7.3
    *
    * Hard: Fix the implementation of `map2` so that it respects
    * the contract of timeouts on `Future`.
    *
    * @see [[Par.Map2Future]]
    */

  /**
    * Exercise 7.4
    *
    * This API already enables a rich set of operations.
    * Here's a simple example: using `lazyUnit`, write a function to convert
    * any function `A => B` to one that evaluates its result asynchronously.
    *
    * @see [[Par.asyncF]]
    */

  /**
    * Exercise 7.5
    *
    * Hard: Write this function, called `sequence`.
    * No additional primitives are required. Do not call run.
    *
    * @see [[Par.sequence]]
    */
}
