import Chapter06._
import fpinscala.purestate.RNG._
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

  "sequence" should "combine a List of transitions into a single transition" in {
    //given
    val rng = SimpleRNG(42)
    val d: Rand[Double] = double

    //when
    val (result, next) = sequence(List.fill(3)(d))(rng)

    //then
    result.size shouldBe 3
    result.foldLeft(-1.0) { case (prev, r) =>
      r should be >= 0.0
      r should be < 1.0
      r should not be prev
      r
    }
    next should not be rng
  }

  "intsUsingSequence" should "return a list of random integers" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = intsUsingSequence(5)(rng)

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
    val (result, next) = intsUsingSequence(0)(rng)

    //then
    result shouldBe Nil
    next shouldBe rng
  }

  "flatMap" should "generate a random A with Rand[A] and choose a Rand[B] based on its value" in {
    //given
    val rng = SimpleRNG(42)
    val d: Rand[Double] = double

    //when
    val (result, next) = flatMap(d)(_ => nonNegativeInt)(rng)

    //then
    next should not be rng
    result should be >= 0
  }

  "nonNegativeLessThan" should "generate an integer between 0 (inclusive) and n (exclusive)" in {
    //given
    val rng = SimpleRNG(42)
    val n = 123

    //when
    val (result, next) = nonNegativeLessThan(n)(rng)

    //then
    next should not be rng
    result should be >= 0
    result should be < n
  }

  "mapUsingFlatMap" should "transform the output of a state action without modifying the state itself" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = mapUsingFlatMap(nonNegativeInt)(- _)(rng)

    //then
    next should not be rng
    result should be <= 0
  }

  "map2UsingFlatMap" should "return an action that combines the results of two other actions" in {
    //given
    val rng = SimpleRNG(42)
    val ra: Rand[Int] = nonNegativeInt
    val rb: Rand[Double] = double

    //when
    val ((res1, res2), next) = map2UsingFlatMap(ra, rb)((_, _))(rng)

    //then
    next should not be rng
    res1 should be >= 0
    res2 should be >= 0.0
    res2 should be < 1.0
  }

  "State.unit" should "pass the state through without using it" in {
    //given
    val rng = SimpleRNG(42)

    //when
    val (result, next) = State.unit(5)(rng)

    //then
    next shouldBe rng
    result shouldBe 5
  }

  "State.map" should "transform the output of a state action without modifying the state itself" in {
    //given
    val rng = SimpleRNG(42)
    val state = State(nonNegativeInt)

    //when
    val (result, next) = state.map(- _)(rng)

    //then
    next should not be rng
    result should be <= 0
  }

  "State.map2" should "return an action that combines the results of two other actions" in {
    //given
    val rng = SimpleRNG(42)
    val state = State(nonNegativeInt)
    val rb = State(double)

    //when
    val ((res1, res2), next) = state.map2(rb)((_, _))(rng)

    //then
    next should not be rng
    res1 should be >= 0
    res2 should be >= 0.0
    res2 should be < 1.0
  }

  "State.flatMap" should "generate a random A with Rand[A] and choose a Rand[B] based on its value" in {
    //given
    val rng = SimpleRNG(42)
    val state = State(double)

    //when
    val (result, next) = state.flatMap(_ => State(nonNegativeInt))(rng)

    //then
    next should not be rng
    result should be >= 0
  }

  "State.sequence" should "combine a List of transitions into a single transition" in {
    //given
    val rng = SimpleRNG(42)
    val d = State(double)

    //when
    val (result, next) = State.sequence(List.fill(3)(d))(rng)

    //then
    result.size shouldBe 3
    result.foldLeft(-1.0) { case (prev, r) =>
      r should be >= 0.0
      r should be < 1.0
      r should not be prev
      r
    }
    next should not be rng
  }

  "modify" should "modify the state" in {
    //when
    val result = State.modify[String](_ + "b")

    //then
    val (_, next) = result("a")
    next shouldBe "ab"

    val (s, _) = State.get("c")
    s shouldBe "c"
  }

  "simulateMachine" should "operate machine based on inputs and return coins and candies left" in {
    //given
    val machine: Machine = Machine(locked = true, coins = 10, candies = 5)
    val inputs = List(
      Coin, Turn,
      Coin, Turn,
      Coin, Turn,
      Coin, Turn
    )

    //when
    val ((coins, candies), next) = simulateMachine(inputs)(machine)

    //then
    coins shouldBe 14
    candies shouldBe 1

    next.locked shouldBe true
    next.coins shouldBe coins
    next.candies shouldBe candies
    next should not be machine
  }

  it should "return original state when list of inputs is empty" in {
    //given
    val machine: Machine = Machine(locked = true, coins = 10, candies = 5)
    val inputs: List[Input] = Nil

    //when
    val ((coins, candies), next) = simulateMachine(inputs)(machine)

    //then
    coins shouldBe machine.coins
    candies shouldBe machine.candies
    next shouldBe machine
  }
}
