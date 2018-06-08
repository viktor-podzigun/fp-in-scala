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

  "Stream.constant" should "return an infinite Stream of a given value" in {
    //when & then
    Stream.constant("1").take(5).toList shouldBe List("1", "1", "1", "1", "1")
    Stream.constant(2).take(100).toList.size shouldBe 100
  }

  "Stream.from" should "return an infinite Stream of integers, starting from n, then n+1, n+2, and so on" in {
    //when & then
    Stream.from(0).take(3).toList shouldBe List(0, 1, 2)
    Stream.from(1).take(3).toList shouldBe List(1, 2, 3)
    Stream.from(2).take(3).toList shouldBe List(2, 3, 4)
  }

  "Stream.fibs" should "return an infinite Stream of Fibonacci numbers" in {
    //when & then
    Stream.fibs().take(8).toList shouldBe List(0, 1, 1, 2, 3, 5, 8, 13)
  }

  "Stream.unfold" should "provide general stream-building" in {
    //when & then
    Stream.unfold(1)(_ => None).toList shouldBe Nil
    Stream.unfold(1)(s => Some((s, s + 1))).take(3).toList shouldBe List(1, 2, 3)
  }

  "onesUsingUnfold" should "return an infinite Stream of 1s" in {
    //when & then
    Stream.onesUsingUnfold.take(5).toList shouldBe List(1, 1, 1, 1, 1)
  }

  "constantUsingUnfold" should "return an infinite Stream of a given value" in {
    //when & then
    Stream.constantUsingUnfold("1").take(5).toList shouldBe List("1", "1", "1", "1", "1")
    Stream.constantUsingUnfold(2).take(100).toList.size shouldBe 100
  }

  "fromUsingUnfold" should "return an infinite Stream of integers, starting from n, then n+1, n+2, and so on" in {
    //when & then
    Stream.fromUsingUnfold(0).take(3).toList shouldBe List(0, 1, 2)
    Stream.fromUsingUnfold(1).take(3).toList shouldBe List(1, 2, 3)
    Stream.fromUsingUnfold(2).take(3).toList shouldBe List(2, 3, 4)
  }

  "fibsUsingUnfold" should "return an infinite Stream of Fibonacci numbers" in {
    //when & then
    Stream.fibsUsingUnfold().take(8).toList shouldBe List(0, 1, 1, 2, 3, 5, 8, 13)
  }

  "mapUsingUnfold" should "convert all the elements in a Stream according to the given function" in {
    //when & then
    Stream().mapUsingUnfold(identity).toList shouldBe Nil
    Stream(1, 2, 3).mapUsingUnfold(_ * 2).toList shouldBe List(2, 4, 6)
  }

  "takeUsingUnfold" should "return the first n elements of a Stream" in {
    //when & then
    Stream().takeUsingUnfold(2).toList shouldBe Nil
    Stream(1, 2, 3).takeUsingUnfold(-1).toList shouldBe Nil
    Stream(1, 2, 3).takeUsingUnfold(0).toList shouldBe Nil
    Stream(1, 2, 3).takeUsingUnfold(1).toList shouldBe List(1)
    Stream(1, 2, 3).takeUsingUnfold(2).toList shouldBe List(1, 2)
    Stream(1, 2, 3).takeUsingUnfold(3).toList shouldBe List(1, 2, 3)
    Stream(1, 2, 3).takeUsingUnfold(4).toList shouldBe List(1, 2, 3)
  }

  "takeWhileUsingUnfold" should "return all starting elements that match given predicate" in {
    //when & then
    Stream().takeWhileUsingUnfold(_ => false).toList shouldBe Nil
    Stream().takeWhileUsingUnfold(_ => true).toList shouldBe Nil
    Stream(1, 2, 3).takeWhileUsingUnfold(_ => false).toList shouldBe Nil
    Stream(1, 2, 3).takeWhileUsingUnfold(_ < 0).toList shouldBe Nil
    Stream(1, 2, 3).takeWhileUsingUnfold(_ == 1).toList shouldBe List(1)
    Stream(1, 1, 2, 1, 3).takeWhileUsingUnfold(_ == 1).toList shouldBe List(1, 1)
    Stream(1, 2, 3, 1).takeWhileUsingUnfold(_ < 3).toList shouldBe List(1, 2)
    Stream(1, 2, 3).takeWhileUsingUnfold(_ < 5).toList shouldBe List(1, 2, 3)
  }

  "zipWithUsingUnfold" should "return new Stream by merging paired elements of current and the given Streams" in {
    //when & then
    Stream[Int]().zipWithUsingUnfold(Stream())(_ + _).toList shouldBe List()
    Stream(1).zipWithUsingUnfold(Stream(1))(_ + _).toList shouldBe List(2)
    Stream(1).zipWithUsingUnfold(Stream(1))(_ - _).toList shouldBe List(0)
    Stream(1).zipWithUsingUnfold(Stream(1, 3))(_ + _).toList shouldBe List(2)
    Stream(1, 3).zipWithUsingUnfold(Stream(1))(_ - _).toList shouldBe List(0)
    Stream(1, 2, 3).zipWithUsingUnfold(Stream())(_ + _).toList shouldBe List()
    Stream(1.0, 2.0, 3.0).zipWithUsingUnfold(Stream(4.0, 5.0, 6.0))(_ + _).toList shouldBe List(5.0, 7.0, 9.0)
  }

  "zipAll" should "return new Stream by merging all elements of current and the given Streams" in {
    //when & then
    Stream[Int]().zipAll(Stream()).toList shouldBe List()
    Stream(1).zipAll(Stream(2)).toList shouldBe List(Some(1) -> Some(2))
    Stream(1).zipAll(Stream(1, 2)).toList shouldBe List(Some(1) -> Some(1), None -> Some(2))
    Stream(1, 2).zipAll(Stream(3)).toList shouldBe List(Some(1) -> Some(3), Some(2) -> None)
    Stream(1, 2, 3).zipAll(Stream()).toList shouldBe List(Some(1) -> None, Some(2) -> None, Some(3) -> None)
    Stream(1.0, 2.0, 3.0).zipAll(Stream(4.0, 5.0, 6.0)).toList shouldBe {
      List(Some(1.0) -> Some(4.0), Some(2.0) -> Some(5.0), Some(3.0) -> Some(6.0))
    }
  }

  "Stream.startsWith" should "check if one Stream is a prefix of another" in {
    //when & then
    Stream().startsWith(Stream()) shouldBe true
    Stream(1).startsWith(Stream()) shouldBe true
    Stream(1).startsWith(Stream(1)) shouldBe true
    Stream(1).startsWith(Stream(1, 2)) shouldBe false
    Stream(1).startsWith(Stream(2)) shouldBe false
    Stream(1, 2, 3).startsWith(Stream()) shouldBe true
    Stream(1, 2, 3).startsWith(Stream(1)) shouldBe true
    Stream(1, 2, 3).startsWith(Stream(1, 2)) shouldBe true
    Stream(1, 2, 3).startsWith(Stream(1, 2, 3)) shouldBe true
    Stream(1, 2, 3).startsWith(Stream(2, 3, 1)) shouldBe false
  }
}
