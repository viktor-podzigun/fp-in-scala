import Chapter03._
import org.scalatest._
import fpinscala.datastructures._

class Chapter03Spec extends FlatSpec with Matchers {

  "matchResult" should "equal 3" in {
    //when & then
    matchResult shouldBe 3
  }

  "tail" should "remove the first element of a list" in {
    //when & then
    tail(List()) shouldBe Nil
    tail(List(1)) shouldBe Nil
    tail(List(1, 2)) shouldBe List(2)
    tail(List(1, 2, 3)) shouldBe List(2, 3)
  }

  "setHead" should "replace the first element of a List with a different value" in {
    //when & then
    setHead(List(), 2) shouldBe Nil
    setHead(List(1), 2) shouldBe List(2)
    setHead(List(1, 2), 3) shouldBe List(3, 2)
  }
}
