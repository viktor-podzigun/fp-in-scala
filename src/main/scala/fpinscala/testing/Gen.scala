package fpinscala.testing

import fpinscala.purestate._

case class Gen[+A](sample: State[RNG, A]) {

  def flatMap[B](f: A => Gen[B]): Gen[B] = Gen(sample.flatMap { a =>
    f(a).sample
  })

  def listOfN(size: Gen[Int]): Gen[List[A]] = size.flatMap { n =>
    Gen.listOfN(n, this)
  }

  def unsized: SGen[A] = SGen(_ => this)

  def map[B](f: A => B): Gen[B] = Gen(sample.map(f))
  
  def map2[B, C](sb: Gen[B])(f: (A, B) => C): Gen[C] = Gen(sample.map2(sb.sample)(f))

  def **[B](g: Gen[B]): Gen[(A, B)] = (this map2 g) ((_, _))
}

object ** {
  def unapply[A, B](p: (A, B)) = Some(p)
}

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

  def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = Gen.boolean.flatMap {
    case true => g1
    case false => g2
  }
}
