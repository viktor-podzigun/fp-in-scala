import fpinscala.purestate.RNG._
import fpinscala.purestate.{RNG, State}

import scala.annotation.tailrec

object Chapter06 {

  /**
    * Exercise 6.1
    *
    * Write a function that uses `RNG.nextInt` to generate a random integer
    * between `0` and `Int.maxValue` (inclusive). Make sure to handle the corner case
    * when `nextInt` returns `Int.MinValue`, which doesn't have a non-negative counterpart.
    * 
    * @see [[RNG.nonNegativeInt]]
    */

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

  /**
    * Exercise 6.3
    *
    * Write functions to generate an `(Int, Double)` pair,
    * a `(Double, Int)` pair, and a `(Double, Double, Double)` 3-tuple.
    * You should be able to reuse the functions you've already written.
    */
  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, rng2) = nonNegativeInt(rng)
    val (d, rng3) = double(rng2)
    ((i, d), rng3)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val (d, rng2) = double(rng)
    val (i, rng3) = nonNegativeInt(rng2)
    ((d, i), rng3)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, next1) = double(rng)
    val (d2, next2) = double(next1)
    val (d3, next3) = double(next2)
    ((d1, d2, d3), next3)
  }

  /**
    * Exercise 6.4
    *
    * Write a function to generate a list of random integers.
    */
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    @tailrec
    def loop(n: Int, rng: RNG, res: List[Int]): (List[Int], RNG) = {
      if (n <= 0) (res, rng)
      else {
        val (i, next) = nonNegativeInt(rng)
        loop(n - 1, next, i :: res)
      }
    }

    loop(count, rng, Nil)
  }

  /**
    * Exercise 6.5
    *
    * Use `map` to reimplement `double` in a more elegant way. See exercise 6.2.
    */
  def doubleUsingMap: Rand[Double] = {
    map(nonNegativeInt) { i =>
      if (i == Int.MaxValue) 0.0
      else i.toDouble/Int.MaxValue
    }
  }

  /**
    * Exercise 6.6
    *
    * Write the implementation of `map2` based on the following signature.
    * This function takes two actions, `ra` and `rb`, and a function `f`
    * for combining their results, and returns a new action that combines them:
    */
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = { rng =>
    val (a, rng2) = ra(rng)
    val (b, rng3) = rb(rng2)
    (f(a, b), rng3)
  }

  /**
    * Exercise 6.7
    *
    * Hard: If you can combine two `RNG` transitions,
    * you should be able to combine a whole list of them.
    * Implement `sequence` for combining a `List` of transitions
    * into a single transition.
    * Use it to reimplement the `ints` function you wrote before.
    * For the latter, you can use the standard library function `List.fill(n)(x)`
    * to make a list with `x` repeated `n` times.
    */
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = { rng =>
    val (list, n) = fs.foldLeft((Nil: List[A], rng)) { case ((res, next), r) =>
      val (i, next2) = r(next)
      (i :: res, next2)
    }

    (list.reverse, n)
  }

  def intsUsingSequence(count: Int)(rng: RNG): (List[Int], RNG) = {
    sequence(List.fill(count)(nonNegativeInt _))(rng)
  }

  /**
    * Exercise 6.8
    *
    * Implement `flatMap`, and then use it to implement `nonNegativeLessThan`.
    * 
    * @see [[RNG.flatMap]]
    * @see [[RNG.nonNegativeLessThan]]
    */

  /**
    * Exercise 6.9
    *
    * Reimplement `map` and `map2` in terms of `flatMap`.
    * The fact that this is possible is what we're referring to
    * when we say that `flatMap` is more powerful than `map` and `map2`.
    */
  def mapUsingFlatMap[A, B](s: Rand[A])(f: A => B): Rand[B] = flatMap(s) { a =>
    rng => (f(a), rng)
  }

  def map2UsingFlatMap[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = flatMap(ra) { a =>
    flatMap(rb) { b =>
      rng => (f(a, b), rng)
    }
  }

  /**
    * Exercise 6.10
    *
    * Generalize the functions `unit`, `map`, `map2`, `flatMap`, and `sequence`.
    * Add them as methods on the `State` case class where possible.
    * Otherwise you should put them in a `State` companion object.
    *
    * @see [[State.unit]]
    * @see [[State.map]]
    * @see [[State.map2]]
    * @see [[State.flatMap]]
    * @see [[State.sequence]]
    */

  /**
    * Exercise 6.11
    *
    * Hard: To gain experience with the use of `State`, implement a finite state automaton
    * that models a simple candy dispenser. The machine has two types of input:
    * you can insert a coin, or you can turn the knob to dispense candy.
    * It can be in one of two states: locked or unlocked.
    * It also tracks how many candies are left and how many coins it contains.
    *
    * The method `simulateMachine` should operate the machine based on the list of `inputs`
    * and return the number of coins and candies left in the machine at the end.
    * For example, if the input `Machine` has `10` coins and `5` candies,
    * and a total of `4` candies are successfully bought, the output should be `(14, 1)`.
    */
  sealed trait Input
  case object Coin extends Input
  case object Turn extends Input

  case class Machine(locked: Boolean, candies: Int, coins: Int)

  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = {

    def operate(input: Input): State[Machine, (Int, Int)] = State { m =>
      val next = input match {
        case Coin if m.locked && m.candies > 0 => m.copy(
          locked = false,
          coins = m.coins + 1
        )
        case Turn if !m.locked && m.candies > 0 => m.copy(
          locked = true,
          candies = m.candies - 1
        )
        case _ => m
      }

      ((next.coins, next.candies), next)
    }

    for {
      list <- State.sequence(inputs.map(operate))
      m <- State.get
    } yield {
      if (list.isEmpty) (m.coins, m.candies)
      else list.last
    }
  }
}
