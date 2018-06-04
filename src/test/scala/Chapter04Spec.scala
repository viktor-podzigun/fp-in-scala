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

  "mean" should "return the mean of a Seq" in {
    //when & then
    mean(Seq()) shouldBe None
    mean(Seq(1, 2, 3)) shouldBe Some(2.0d)
  }

  "variance" should "return the variance of a Seq" in {
    //when & then
    variance(Seq()) shouldBe None
    variance(Seq(1, 2, 3)) shouldBe Some(0.6666666666666666d)
  }

  "map2" should "combine two Option values using a binary function" in {
    //when & then
    map2(None: Option[Int], None)(_ + _) shouldBe None
    map2(Some(1), None)(_ + _) shouldBe None
    map2(None: Option[Int], Some(1))(_ + _) shouldBe None
    map2(Some(1), Some(2))(_ + _) shouldBe Some(3)
  }

  "Option.sequence" should "combine a list of Options into one Option" in {
    //when & then
    Option.sequence(List()) shouldBe Some(List())
    Option.sequence(List(None, Some(1))) shouldBe None
    Option.sequence(List(Some(1), None)) shouldBe None
    Option.sequence(List(Some(1), None, Some(2))) shouldBe None
    Option.sequence(List(Some(1), Some(2), None)) shouldBe None
    Option.sequence(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
  }

  "Option.traverse" should "combine a list of Options into one Option" in {
    //when & then
    Option.traverse(List())(_ => None) shouldBe Some(List())
    Option.traverse(List(1))(_ => None) shouldBe None
    Option.traverse(List(1, 2))(_ => None) shouldBe None
    Option.traverse(List(1, 2))(x => if (x == 2) None else Some(x)) shouldBe None
    Option.traverse(List(1, 2, 3))(x => Some(x)) shouldBe Some(List(1, 2, 3))
  }

  "Option.sequence2" should "combine a list of Options into one Option" in {
    //when & then
    Option.sequence2(List()) shouldBe Some(List())
    Option.sequence2(List(None, Some(1))) shouldBe None
    Option.sequence2(List(Some(1), None)) shouldBe None
    Option.sequence2(List(Some(1), None, Some(2))) shouldBe None
    Option.sequence2(List(Some(1), Some(2), None)) shouldBe None
    Option.sequence2(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
  }

  "Either.map" should "apply f if the Either is Right" in {
    //when & then
    Left(1).map(identity) shouldBe Left(1)
    Right(1).map(_ * 2) shouldBe Right(2)
    Right(2).map(_ * 2) shouldBe Right(4)
  }

  "Either.flatMap" should "apply f, which may fail, to the Either if Right" in {
    //when & then
    Left(1).flatMap(identity) shouldBe Left(1)
    Right(1).flatMap(_ => Left(1)) shouldBe Left(1)
    Right(1).flatMap(v => Right(v * 2)) shouldBe Right(2)
    Right(2).flatMap(v => Right(v * 2)) shouldBe Right(4)
  }

  "Either.orElse" should "not evaluate b unless needed" in {
    //when & then
    Left(1).orElse(Left(2)) shouldBe Left(2)
    Left(1).orElse(Right(1)) shouldBe Right(1)
    Right(1).orElse(Right(2)) shouldBe Right(1)
    Right(2).orElse(Right(1)) shouldBe Right(2)
  }

  it should "not evaluate b for Right" in {
    //given
    val b = mockFunction[Either[Int, Int]]

    //then
    b.expects().never()

    //when
    Right(1).orElse(b()) shouldBe Right(1)
  }

  "Either.map2" should "combine two Either values using a binary function" in {
    //when & then
    Left(1).map2(Left(2))((_, b) => b) shouldBe Left(1)
    Left(1).map2(Right(2))((_, b) => b) shouldBe Left(1)
    Right(1).map2(Left(2))(_ + _) shouldBe Left(2)
    Right(1).map2(Right(2))(_ + _) shouldBe Right(3)
  }
}
