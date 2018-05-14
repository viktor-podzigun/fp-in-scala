import Chapter03._
import fpinscala.datastructures._
import org.scalatest._

class Chapter03Spec extends FlatSpec with Matchers {

  "matchResult" should "equal 3" in {
    //when & then
    matchResult shouldBe 3
  }

  "tail" should "remove the first element of a List" in {
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

  "drop" should "remove the first n elements from a List" in {
    def assertSameList[A](xs: List[A], n: Int): Unit = {
      drop(xs, n) should be theSameInstanceAs xs
    }

    //when & then
    assertSameList(List(), 0)
    assertSameList(List(), 1)
    assertSameList(List(), 2)
    assertSameList(List(1), 0)
    assertSameList(List(1, 2), 0)
    assertSameList(List(1, 2, 3), 0)

    drop(List(1), 1) shouldBe Nil
    drop(List(1), 2) shouldBe Nil
    drop(List(1), 3) shouldBe Nil
    drop(List(1, 2), 2) shouldBe Nil
    drop(List(1, 2), 3) shouldBe Nil
    drop(List(1, 2, 3), 3) shouldBe Nil
    drop(List(1, 2, 3), 4) shouldBe Nil

    drop(List(1, 2), 1) shouldBe List(2)
    drop(List(1, 2, 3), 1) shouldBe List(2, 3)
    drop(List(1, 2, 3), 2) shouldBe List(3)
    drop(List(1, 2, 3, 4), 2) shouldBe List(3, 4)
    drop(List(1, 2, 3, 4), 3) shouldBe List(4)
  }
}
