package fpinscala.purestate

trait RNG {

  def nextInt: (Int, RNG)
}

object RNG {
  //type Rand[A] = State[RNG, A]
  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] = { rng =>
    val (a, rng2) = s(rng)
    (f(a), rng2)
  }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val res@(i, next) = rng.nextInt

    if (i == Int.MinValue) (0, next)
    else if (i < 0) (-i, next)
    else res
  }

  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = { rng =>
    val (a, rng2) = f(rng)
    g(a)(rng2)
  }

  def nonNegativeLessThan(n: Int): Rand[Int] = flatMap(nonNegativeInt) { i =>
    val mod = i % n

    if (i + (n - 1) - mod >= 0) { rng =>
      (mod, rng)
    }
    else nonNegativeLessThan(n)
  }
}

case class SimpleRNG(seed: Long) extends RNG {

  def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }
}
