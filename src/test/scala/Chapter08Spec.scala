
import Chapter08._
import fpinscala.purestate.SimpleRNG
import fpinscala.testing._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter08Spec extends FlatSpec
  with Matchers
  with MockFactory {

  "choose" should "generate integers in the range start to stopExclusive" in {
    //given
    val rng = SimpleRNG(42)
    
    //when & then
    val (result, next) = Gen.choose(0, 10).sample(rng)

    result should be >= 0
    result should be < 10

    next should not be rng
  }
  
  "unit" should "always generate the given value" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.unit(10)

    //when & then
    val (result, next) = gen.sample(rng)
    result shouldBe 10
    next shouldBe rng
    
    val (result2, next2) = gen.sample(next)
    result2 shouldBe 10
    next2 shouldBe rng
  }
  
  "boolean" should "generate random boolean value" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.boolean

    //when & then
    val (result, next) = gen.sample(rng)
    List(result) should contain oneOf (true, false)

    next should not be rng
  }
  
  "Gen.listOfN" should "generate random lists of given size" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.listOfN(3, Gen.choose(0, 2))

    //when & then
    val (result, next) = gen.sample(rng)
    result.size shouldBe 3
    result.forall(i => i == 0 || i == 1) shouldBe true
    next should not be rng
    
    val (result2, next2) = gen.sample(next)
    result2.size shouldBe 3
    result2.forall(i => i == 0 || i == 1) shouldBe true
    next2 should not be next
  }
  
  "flatMap" should "return Gen based on current Gen's value" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.choose(0, 2).flatMap(_ => Gen.boolean)

    //when & then
    val (result, next) = gen.sample(rng)
    List(result) should contain oneOf (true, false)

    next should not be rng
  }
  
  "listOfN" should "generate random lists with sizes by the given gen" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.choose(0, 2).listOfN(Gen.choose(3, 6))

    //when & then
    val (result, next) = gen.sample(rng)
    result.size should be >= 3
    result.size should be < 6
    result.forall(i => i == 0 || i == 1) shouldBe true
    next should not be rng
    
    val (result2, next2) = gen.sample(next)
    result2.size should be >= 3
    result2.size should be < 6
    result2.forall(i => i == 0 || i == 1) shouldBe true
    next2 should not be next
  }
}
