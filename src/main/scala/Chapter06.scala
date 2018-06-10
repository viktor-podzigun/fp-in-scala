import fpinscala.purestate.RNG

object Chapter06 {

  /**
    * Exercise 6.1
    *
    * Write a function that uses `RNG.nextInt` to generate a random integer
    * between `0` and `Int.maxValue` (inclusive). Make sure to handle the corner case
    * when `nextInt` returns `Int.MinValue`, which doesn't have a non-negative counterpart.
    */
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val res@(i, next) = rng.nextInt

    if (i == Int.MinValue) (0, next)
    else if (i < 0) (-i, next)
    else res
  }

  /**
    * Exercise 6.2
    *
    * Write a function to generate a `Double` between `0` and `1`, not including 1.
    * Note: You can use `Int.MaxValue` to obtain the maximum positive integer value,
    * and you can use `x.toDouble` to convert an `x: Int` to a `Double`.
    */
  def double(rng: RNG): (Double, RNG) = {
    val (i, next) = nonNegativeInt(rng)

    if (i == Int.MaxValue) (0.0, next)
    else (i.toDouble/Int.MaxValue, next)
  }
}
