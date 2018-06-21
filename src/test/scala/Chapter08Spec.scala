
import Chapter08._
import fpinscala.purestate.SimpleRNG
import fpinscala.testing.Prop._
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
  
  "Gen.unit" should "always generate the given value" in {
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
  
  "Gen.boolean" should "generate random boolean value" in {
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

  "Gen.union" should "return Gen combining two generators of the same type" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.union(Gen.unit(true), Gen.unit(false))

    //when & then
    val (result, next) = gen.sample(rng)
    List(result) should contain oneOf (true, false)

    next should not be rng
  }
  
  "Prop.&&" should "return new Prop with current and the given Props combined with &&" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.choose(1, 3)
    val max = 5

    //when & then
    (Prop.forAll(gen)(_ >= 1) &&
      Prop.forAll(gen)(_ < 3)
      ).run(max, 5, rng) shouldBe Prop.Passed

    val Falsified(_, _, tag) = (
      Prop.forAll(gen, "1")(_ >= 3) &&
        Prop.forAll(gen, "2")(_ < 3)
      ).run(max, 5, rng)
    tag shouldBe "1"
  }
  
  "Prop.||" should "return new Prop with current and the given Props combined with ||" in {
    //given
    val rng = SimpleRNG(42)
    val gen = Gen.choose(1, 3)
    val max = 5

    //when & then
    (Prop.forAll(gen)(_ >= 3) ||
      Prop.forAll(gen)(_ < 3)
      ).run(max, 5, rng) shouldBe Prop.Passed
    
    val Falsified(_, _, tag) = (
      Prop.forAll(gen, "1")(_ < 1) ||
        Prop.forAll(gen, "2")(_ >= 3)
      ).run(max, 5, rng)
    tag shouldBe "2"
  }
  
  "Gen.unsized" should "return new unsized generator that always return current gen" in {
    //given
    val gen = Gen.choose(1, 3)
    val sgen = gen.unsized

    //when & then
    sgen.forSize(0) shouldBe gen
    sgen.forSize(1) shouldBe gen
    sgen.forSize(5) shouldBe gen
  }

  "SGen.unit" should "always generate the given value" in {
    //given
    val rng = SimpleRNG(42)
    val sgen = SGen.unit(10)

    //when & then
    val (result, next) = sgen.forSize(5).sample(rng)
    result shouldBe 10
    next shouldBe rng

    val (result2, next2) = sgen.forSize(3).sample(next)
    result2 shouldBe 10
    next2 shouldBe rng
  }

  "SGen.listOf" should "generate lists of the requested size" in {
    //given
    val rng = SimpleRNG(42)
    val sgen = SGen.listOf(Gen.choose(0, 2))
    val gen = sgen.forSize(3)

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
  
  "SGen.listOf1" should "generate nonempty lists" in {
    //given
    val rng = SimpleRNG(42)
    val sgen = SGen.listOf1(Gen.choose(0, 2))

    //when & then
    val (result, next) = sgen.forSize(0).sample(rng)
    result.size shouldBe 1
    result.forall(i => i == 0 || i == 1) shouldBe true
    next should not be rng

    val (result2, next2) = sgen.forSize(3).sample(next)
    result2.size shouldBe 3
    result2.forall(i => i == 0 || i == 1) shouldBe true
    next2 should not be next
  }
  
  "maxProp" should "pass all the test cases" in {
    //given
    val smallInt = Gen.choose(-10, 10)
    val maxProp = forAll(SGen.listOf1(smallInt)) { ns =>
      val max = ns.max
      !ns.exists(_ > max)
    }
    
    //when & then
    Prop.run(maxProp)
  }
}
