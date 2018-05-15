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
    //when & then
    assertSameList(List(), drop(_: List[_], 0))
    assertSameList(List(), drop(_: List[_], 1))
    assertSameList(List(), drop(_: List[_], 2))
    assertSameList(List(1), drop(_: List[_], 0))
    assertSameList(List(1, 2), drop(_: List[_], 0))
    assertSameList(List(1, 2, 3), drop(_: List[_], 0))

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

  "dropWhile" should "remove elements from the List prefix as long as they match a predicate" in {
    //when & then
    assertSameList(List(), dropWhile(_: List[Int], (_: Int) => true))
    assertSameList(List(), dropWhile(_: List[Int], (_: Int) => false))
    assertSameList(List(1), dropWhile(_: List[Int], (_: Int) => false))
    assertSameList(List(1), dropWhile(_: List[Int], (_: Int) == 2))
    assertSameList(List(1, 2), dropWhile(_: List[Int], (_: Int) == 2))
    assertSameList(List(1, 2), dropWhile(_: List[Int], (_: Int) => false))
    assertSameList(List(1, 2, 3), dropWhile(_: List[Int], (_: Int) => false))
    assertSameList(List(1, 2, 3), dropWhile(_: List[Int], (_: Int) == 2))
    assertSameList(List(1, 2, 3), dropWhile(_: List[Int], (_: Int) == 3))

    dropWhile(List(1), (_: Int) => true) shouldBe Nil
    dropWhile(List(1, 2), (_: Int) => true) shouldBe Nil
    dropWhile(List(1, 2, 3), (_: Int) => true) shouldBe Nil

    dropWhile(List(1), (_: Int) == 1) shouldBe Nil
    dropWhile(List(1, 2), (_: Int) == 1) shouldBe List(2)
    dropWhile(List(1, 1, 2), (_: Int) == 1) shouldBe List(2)
    dropWhile(List(1, 1, 1, 2), (_: Int) == 1) shouldBe List(2)
    dropWhile(List(1, 2, 3), (_: Int) == 1) shouldBe List(2, 3)
    dropWhile(List(1, 1, 2, 3), (_: Int) == 1) shouldBe List(2, 3)
    dropWhile(List(1, 2, 1, 3), (_: Int) == 1) shouldBe List(2, 1, 3)
  }

  "init" should "return a List consisting of all but the last element of a List" in {
    //when & then
    assertSameList(List(), init(_: List[Int]))

    init(List(1)) shouldBe Nil
    init(List(1, 2)) shouldBe List(1)
    init(List(1, 2, 3)) shouldBe List(1, 2)
    init(List(1, 2, 3, 4)) shouldBe List(1, 2, 3)
  }

  "product" should "immediately halt the recursion and return 0.0 if it encounters a 0.0" in {
    //when & then
    product(List(1, 2, 3)) shouldBe 6
    product(List(1, 0, 3)) shouldBe 0
  }

  private def assertSameList[A](xs: List[A], f: List[A] => List[A]): Unit = {
    f(xs) should be theSameInstanceAs xs
  }
}
