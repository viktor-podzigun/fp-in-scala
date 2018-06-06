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

  "Stream.takeWhile" should "return all starting elements that match given predicate" in {
    //when & then
    Stream().takeWhile(_ => false).toList shouldBe Nil
    Stream().takeWhile(_ => true).toList shouldBe Nil
    Stream(1, 2, 3).takeWhile(_ => false).toList shouldBe Nil
    Stream(1, 2, 3).takeWhile(_ < 0).toList shouldBe Nil
    Stream(1, 2, 3).takeWhile(_ == 1).toList shouldBe List(1)
    Stream(1, 1, 2, 1, 3).takeWhile(_ == 1).toList shouldBe List(1, 1)
    Stream(1, 2, 3, 1).takeWhile(_ < 3).toList shouldBe List(1, 2)
    Stream(1, 2, 3).takeWhile(_ < 5).toList shouldBe List(1, 2, 3)
  }

  "Stream.forAll" should "check that all elements in the Stream match a given predicate" in {
    //when & then
    Stream().forAll(_ => false) shouldBe true
    Stream().forAll(_ => true) shouldBe true
    Stream(1, 2, 3).forAll(_ => false) shouldBe false
    Stream(1, 2, 3).forAll(_ => true) shouldBe true
    Stream(1, 2, 3).forAll(_ < 0) shouldBe false
    Stream(1, 2, 3).forAll(_ > 0) shouldBe true
    Stream(1, 2, 3).forAll(_ == 1) shouldBe false
    Stream(1, 1, 1).forAll(_ == 1) shouldBe true
  }

  it should "terminate the traversal as soon as it encounters a non-matching value" in {
    //given
    val tail = mockFunction[Stream[Int]]

    //then
    tail.expects().never()

    //when
    Stream.cons(1, Stream.cons(2, tail())).forAll(_ == 1) shouldBe false
  }

  "takeWhileUsingFoldRight" should "return all starting elements that match given predicate" in {
    //when & then
    Stream().takeWhileUsingFoldRight(_ => false).toList shouldBe Nil
    Stream().takeWhileUsingFoldRight(_ => true).toList shouldBe Nil
    Stream(1, 2, 3).takeWhileUsingFoldRight(_ => false).toList shouldBe Nil
    Stream(1, 2, 3).takeWhileUsingFoldRight(_ < 0).toList shouldBe Nil
    Stream(1, 2, 3).takeWhileUsingFoldRight(_ == 1).toList shouldBe List(1)
    Stream(1, 1, 2, 1, 3).takeWhileUsingFoldRight(_ == 1).toList shouldBe List(1, 1)
    Stream(1, 2, 3, 1).takeWhileUsingFoldRight(_ < 3).toList shouldBe List(1, 2)
    Stream(1, 2, 3).takeWhileUsingFoldRight(_ < 5).toList shouldBe List(1, 2, 3)
  }

  it should "terminate the traversal as soon as it encounters a non-matching value" in {
    //given
    val tail = mockFunction[Stream[Int]]

    //then
    tail.expects().never()

    //when
    Stream.cons(1, Stream.cons(2, tail())).takeWhileUsingFoldRight(_ == 1).toList shouldBe List(1)
  }

  "headOptionUsingFoldRight" should "return first element of non-empty Stream or None for empty" in {
    //when & then
    Stream().headOptionUsingFoldRight shouldBe None
    Stream(1, 2, 3).headOptionUsingFoldRight shouldBe Some(1)
  }

  it should "terminate the traversal as soon as it encounters the first value" in {
    //given
    val tail = mockFunction[Stream[Int]]

    //then
    tail.expects().never()

    //when
    Stream.cons(1, tail()).headOptionUsingFoldRight shouldBe Some(1)
  }

  "Stream.map" should "convert all the elements in a Stream according to the given function" in {
    //when & then
    Stream().map(identity).toList shouldBe Nil
    Stream(1, 2, 3).map(_ * 2).toList shouldBe List(2, 4, 6)
  }

  "Stream.filter" should "return all the elements of a Stream that satisfy the given predicate" in {
    //when & then
    Stream().filter(_ => true).toList shouldBe Nil
    Stream(2, 1, 2, 3, 1, 2, 1).filter(_ > 1).toList shouldBe List(2, 2, 3, 2)
  }

  "Stream.append" should "append all the elements of the given Stream to the end of the current Stream" in {
    //when & then
    Stream().append(Stream()).toList shouldBe Nil
    Stream(1).append(Stream()).toList shouldBe List(1)
    Stream().append(Stream(1)).toList shouldBe List(1)
    Stream(1).append(Stream(2)).toList shouldBe List(1, 2)
    Stream(1).append(Stream(2, 3)).toList shouldBe List(1, 2, 3)
    Stream(1, 2).append(Stream(3)).toList shouldBe List(1, 2, 3)
  }

  "Stream.flatMap" should "convert all the elements in a Stream and flatten the results" in {
    //when & then
    Stream().flatMap(a => Stream(a, a)).toList shouldBe Nil
    Stream(1, 2, 3).flatMap(_ => Stream()).toList shouldBe Nil
    Stream(1, 2, 3).flatMap(a => Stream(a * 2, a * 2)).toList shouldBe List(2, 2, 4, 4, 6, 6)
  }
}
