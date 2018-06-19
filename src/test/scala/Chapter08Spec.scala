
import Chapter08._
import fpinscala.purestate.SimpleRNG
import fpinscala.testing._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter08Spec extends FlatSpec
  with Matchers
  with MockFactory {

  "Gen.choose" should "generate integers in the range start to stopExclusive" in {
    //given
    val rng = SimpleRNG(42)
    
    //when & then
    val (result, next) = Gen.choose(0, 10).sample(rng)

    result should be >= 0
    result should be < 10

    next should not be rng
  }
}
