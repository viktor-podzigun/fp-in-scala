import Chapter04._
import fpinscala.datastructures._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter04Spec extends FlatSpec
  with Matchers
  with MockFactory {

  "Option.map" should "apply f if the Option is not None" in {
    //when & then
    None.map(identity) shouldBe None
    Some(1).map(_ * 2) shouldBe Some(2)
    Some(2).map(_ * 2) shouldBe Some(4)
  }

  "Option.flatMap" should "apply f, which may fail, to the Option if not None" in {
    //when & then
    None.flatMap(identity) shouldBe None
    Some(1).flatMap(_ => None) shouldBe None
    Some(1).flatMap(v => Some(v * 2)) shouldBe Some(2)
    Some(2).flatMap(v => Some(v * 2)) shouldBe Some(4)
  }

  "Option.getOrElse" should "return default value if the Option is None" in {
    //when & then
    None.getOrElse(1) shouldBe 1
    None.getOrElse(2) shouldBe 2
    Some(1).getOrElse(2) shouldBe 1
    Some(2).getOrElse(1) shouldBe 2
  }

  it should "not evaluate default for Some" in {
    //given
    val default = mockFunction[Int]

    //then
    default.expects().never()

    //when
    Some(1).getOrElse(default()) shouldBe 1
  }

  "Option.orElse" should "not evaluate ob unless needed" in {
    //when & then
    None.orElse(None) shouldBe None
    None.orElse(Some(1)) shouldBe Some(1)
    Some(1).orElse(Some(2)) shouldBe Some(1)
    Some(2).orElse(Some(1)) shouldBe Some(2)
  }

  it should "not evaluate ob for Some" in {
    //given
    val ob = mockFunction[Option[Int]]

    //then
    ob.expects().never()

    //when
    Some(1).orElse(ob()) shouldBe Some(1)
  }

  "Option.filter" should "convert Some to None if the value doesn't satisfy f" in {
    //when & then
    None.filter(_ => false) shouldBe None
    None.filter(_ => true) shouldBe None
    Some(1).filter(_ => false) shouldBe None
    Some(2).filter(_ => true) shouldBe Some(2)
  }
}
