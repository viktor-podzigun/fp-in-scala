import Chapter05._
import fpinscala.laziness._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter05Spec extends FlatSpec
  with Matchers
  with MockFactory {

  "Stream.toList" should "convert a Stream to a scala List" in {
    //when & then
    Stream().toList should be theSameInstanceAs Nil
    Stream(1, 2, 3).toList shouldBe List(1, 2, 3)
  }
}
