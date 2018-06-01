
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

  /**
    * Exercise 4.3
    *
    * Write a generic function `map2` that combines two `Option` values using a binary function.
    * If either `Option` value is None, then the return value is too.
    */
  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a.flatMap { v1 =>
      b.map { v2 =>
        f(v1, v2)
      }
    }
  }

  /**
    * Exercise 4.4
    *
    * Write a function sequence that combines a list of `Option`s into one `Option`
    * containing a `List` of all the `Some` values in the original list.
    * If the original list contains `None` even once, the result of the function
    * should be `None`; otherwise the result should be `Some` with a list of all the values.
    *
    * @see [[Option.sequence]]
    */

  /**
    * Exercise 4.5
    *
    * Implement `traverse` function.
    * It's straightforward to do using `map` and `sequence`, but try for a more efficient implementation
    * that only looks at the list once.
    * In fact, implement `sequence` in terms of `traverse`.
    *
    * @see [[Option.traverse]]
    * @see [[Option.sequence2]]
    */
}
