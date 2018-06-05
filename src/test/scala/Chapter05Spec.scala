import Chapter05._
import fpinscala.laziness._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter05Spec extends FlatSpec
  with Matchers
  with MockFactory {

  "Stream.toList" should "convert a Stream to a scala List" in {
    //when & then
    Stream().toList shouldBe Nil
    Stream(1, 2, 3).toList shouldBe List(1, 2, 3)
  }

  "Stream.take" should "return the first n elements of a Stream" in {
    //when & then
    Stream().take(2).toList shouldBe Nil
    Stream(1, 2, 3).take(-1).toList shouldBe Nil
    Stream(1, 2, 3).take(0).toList shouldBe Nil
    Stream(1, 2, 3).take(1).toList shouldBe List(1)
    Stream(1, 2, 3).take(2).toList shouldBe List(1, 2)
    Stream(1, 2, 3).take(3).toList shouldBe List(1, 2, 3)
    Stream(1, 2, 3).take(4).toList shouldBe List(1, 2, 3)
  }

  "Stream.drop" should "skip the first n elements of a Stream" in {
    //when & then
    Stream().drop(2).toList shouldBe Nil
    Stream(1, 2, 3).drop(-1).toList shouldBe List(1, 2, 3)
    Stream(1, 2, 3).drop(0).toList shouldBe List(1, 2, 3)
    Stream(1, 2, 3).drop(1).toList shouldBe List(2, 3)
    Stream(1, 2, 3).drop(2).toList shouldBe List(3)
    Stream(1, 2, 3).drop(3).toList shouldBe Nil
    Stream(1, 2, 3).drop(4).toList shouldBe Nil
  }
}
