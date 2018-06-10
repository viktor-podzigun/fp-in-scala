import Chapter06._
import fpinscala.purestate.RNG.Rand
import fpinscala.purestate._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter06Spec extends FlatSpec
  with Matchers
  with MockFactory {

  "nonNegativeInt" should "return random, non-negative Int" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = nonNegativeInt(rng)

    //then
    next should not be rng
    result should be >= 0
    result shouldBe nonNegativeInt(rng)._1
  }

  it should "return 0 when nextInt returns Int.MinValue" in {
    //given
    val rng = mock[RNG]
    (rng.nextInt _).expects().returns((Int.MinValue, rng))

    //when
    val (result, _) = nonNegativeInt(rng)

    //then
    result shouldBe 0
  }

  "double" should "return random Double between 0 and 1, not including 1" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = double(rng)

    //then
    next should not be rng
    result should be >= 0.0
    result should be < 1.0
    result shouldBe double(rng)._1
  }

  it should "return 0.0 when nextInt returns Int.MaxValue" in {
    //given
    val rng = mock[RNG]
    (rng.nextInt _).expects().returns((Int.MaxValue, rng))

    //when
    val (result, _) = double(rng)

    //then
    result shouldBe 0.0
  }

  "intDouble" should "return random (Int, Double)" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val ((res1, res2), next) = intDouble(rng)

    //then
    next should not be rng
    res1 should be >= 0
    res2 should be >= 0.0
    res2 should be < 1.0
  }

  "doubleInt" should "return random (Double, Int)" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val ((res1, res2), next) = doubleInt(rng)

    //then
    next should not be rng
    res1 should be >= 0.0
    res1 should be < 1.0
    res2 should be >= 0
  }

  "double3" should "return random (Double, Double, Double)" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val ((res1, res2, res3), next) = double3(rng)

    //then
    next should not be rng
    res1 should be >= 0.0
    res1 should be < 1.0
    res2 should be >= 0.0
    res2 should be < 1.0
    res3 should be >= 0.0
    res3 should be < 1.0
  }

  "ints" should "return a list of random integers" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = ints(5)(rng)

    //then
    result.size shouldBe 5
    result.foldLeft(-1) { case (prev, r) =>
      r should be >= 0
      r should not be prev
      r
    }
    next should not be rng
  }

  it should "return an empty list if n = 0" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = ints(0)(rng)

    //then
    result shouldBe Nil
    next shouldBe rng
  }

  "doubleUsingMap" should "return random Double between 0 and 1, not including 1" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = doubleUsingMap(rng)

    //then
    next should not be rng
    result should be >= 0.0
    result should be < 1.0
    result shouldBe doubleUsingMap(rng)._1
  }

  it should "return 0.0 when nextInt returns Int.MaxValue" in {
    //given
    val rng = mock[RNG]
    (rng.nextInt _).expects().returns((Int.MaxValue, rng))

    //when
    val (result, _) = doubleUsingMap(rng)

    //then
    result shouldBe 0.0
  }

  "map2" should "return an action that combines the results of two other actions" in {
    //given
    val rng = SimpleRNG(42)
    val ra: Rand[Int] = nonNegativeInt
    val rb: Rand[Double] = double

    //when
    val ((res1, res2), next) = map2(ra, rb)((_, _))(rng)

    //then
    next should not be rng
    res1 should be >= 0
    res2 should be >= 0.0
    res2 should be < 1.0
  }
}
