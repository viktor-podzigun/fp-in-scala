import Chapter06._
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
    val (result, _) = nonNegativeInt(rng)

    //then
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
}
