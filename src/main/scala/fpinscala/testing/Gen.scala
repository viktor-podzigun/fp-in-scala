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
}
