package fpinscala.testing

import fpinscala.purestate._

case class Gen[A](sample: State[RNG, A])

object Gen {

  def choose(start: Int, stopExclusive: Int): Gen[Int] = Gen(State {
    RNG.map(RNG.nonNegativeLessThan(stopExclusive)) { i =>
      if (i < start) start
      else i
    }
  })

  /**
    * Always generates the value `a`
    */
  def unit[A](a: => A): Gen[A] = Gen(State(RNG.unit(a)))

  def boolean: Gen[Boolean] = Gen(State {
    RNG.map(RNG.int) { i =>
      if (i % 2 == 0) true
      else false
    }
  })

  /**
    * Generates lists of length `n` using the generator `g`
    */
  def listOfN[A](n: Int, g: Gen[A]): Gen[List[A]] = Gen(State { rng =>
    val (a, rng2) = g.sample(rng)
    (List.fill(n)(a), rng2)
  })
}