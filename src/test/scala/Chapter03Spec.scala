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

  "foldRightWithNilAndCons" should "return new List with the same elements" in {
    //when & then
    foldRightWithNilAndCons shouldBe List(1, 2, 3)
  }

  "length" should "return length of a List" in {
    //when & then
    Chapter03.length(List()) shouldBe 0
    Chapter03.length(List(1)) shouldBe 1
    Chapter03.length(List(1, 2)) shouldBe 2
    Chapter03.length(List(1, 2, 3)) shouldBe 3
    Chapter03.length(List(1, 2, 3, 4, 5)) shouldBe 5
  }

  "foldLeft" should "traverse elements from left to right" in {
    //when & then
    List.foldLeft(List(1, 2, 3), Nil: List[Int])((res, x) => Cons(x, res)) shouldBe List(3, 2, 1)
  }

  "sum3" should "return product of elements of a List using foldLeft" in {
    //when & then
    sum3(List()) shouldBe 0
    sum3(List(1, 2, 3, 4)) shouldBe 10
    sum3(List(1, 0, 3)) shouldBe 4
  }

  "product3" should "return product of elements of a List using foldLeft" in {
    //when & then
    product3(List()) shouldBe 1
    product3(List(1, 2, 3, 4)) shouldBe 24
    product3(List(1, 0, 3)) shouldBe 0
  }

  "length3" should "return length of a List using foldLeft" in {
    //when & then
    Chapter03.length3(List()) shouldBe 0
    Chapter03.length3(List(1)) shouldBe 1
    Chapter03.length3(List(1, 2)) shouldBe 2
    Chapter03.length3(List(1, 2, 3)) shouldBe 3
    Chapter03.length3(List(1, 2, 3, 4, 5)) shouldBe 5
  }

  "reverse" should "return length of a List using foldLeft" in {
    //when & then
    assertSameList(List(), List.reverse(_: List[Int]))
    assertSameList(List(1), List.reverse(_: List[Int]))

    List.reverse(List(1, 2)) shouldBe List(2, 1)
    List.reverse(List(1, 2, 3)) shouldBe List(3, 2, 1)
    List.reverse(List(1, 2, 3, 4)) shouldBe List(4, 3, 2, 1)
  }

  "foldRight2" should "traverse elements from right to left" in {
    //when & then
    foldRight2(List(), Nil: List[Int])((x, res) => Cons(x, res)) shouldBe Nil
    foldRight2(List(1, 2, 3), Nil: List[Int])((x, res) => Cons(x, res)) shouldBe List(1, 2, 3)
  }

  "append" should "add all the elements of one list to the end of another" in {
    //when & then
    append(List(), List()) shouldBe List()
    append(List(), List(1)) shouldBe List(1)
    append(List(1), List()) shouldBe List(1)
    append(List(1), List(2)) shouldBe List(1, 2)
    append(List(1, 2), List(3)) shouldBe List(1, 2, 3)
    append(List(1), List(2, 3)) shouldBe List(1, 2, 3)
    append(List(1, 2), List(3, 4)) shouldBe List(1, 2, 3, 4)
    append(List(1, 2, 3), List(4, 5, 6)) shouldBe List(1, 2, 3, 4, 5, 6)
  }

  "concatenate" should "concatenate a list of lists into a single list" in {
    //when & then
    concatenate(List(List())) shouldBe List()
    concatenate(List(List(), List())) shouldBe List()
    concatenate(List(List(1), List())) shouldBe List(1)
    concatenate(List(List(), List(1))) shouldBe List(1)
    concatenate(List(List(1), List(2))) shouldBe List(1, 2)
    concatenate(List(List(1, 2), List(3))) shouldBe List(1, 2, 3)
    concatenate(List(List(1), List(2, 3))) shouldBe List(1, 2, 3)
    concatenate(List(List(1, 2), List(3, 4))) shouldBe List(1, 2, 3, 4)
    concatenate(List(List(1, 2, 3), List(4), List(5, 6))) shouldBe List(1, 2, 3, 4, 5, 6)
  }

  "increment" should "transform a list of integers by adding 1 to each element" in {
    //when & then
    increment(List()) shouldBe List()
    increment(List(1)) shouldBe List(2)
    increment(List(1, 2)) shouldBe List(2, 3)
    increment(List(1, 2, 3)) shouldBe List(2, 3, 4)
  }

  "toString" should "turn each value in a List[Double] into a String" in {
    //when & then
    Chapter03.toString(List()) shouldBe List()
    Chapter03.toString(List(1.0)) shouldBe List("1.0")
    Chapter03.toString(List(1.0, 2.0)) shouldBe List("1.0", "2.0")
    Chapter03.toString(List(1.0, 2.0, 3.0)) shouldBe List("1.0", "2.0", "3.0")
  }

  "map" should "generalize modifying each element in a list" in {
    //when & then
    map(List())(_.toString) shouldBe List()
    map(List(1.0))(_.toString) shouldBe List("1.0")
    map(List(1.0, 2.0))(_.toString) shouldBe List("1.0", "2.0")
    map(List(1.0, 2.0, 3.0))(_.toString) shouldBe List("1.0", "2.0", "3.0")
  }

  "filter" should "remove elements from a list unless they satisfy a given predicate" in {
    //when & then
    filter(List[Int]())(_ > 0) shouldBe List()
    filter(List(1))(_ < 0) shouldBe List()
    filter(List(1, 2))(_ != 0) shouldBe List(1, 2)
    filter(List(1, 2, 3))(x => (x % 2) == 0) shouldBe List(2)
  }

  "flatMap" should "work like a map but with flattening the result lists" in {
    //when & then
    flatMap(List[Int]())(x => List(x, x)) shouldBe List()
    flatMap(List(1))(x => List(x, x)) shouldBe List(1, 1)
    flatMap(List(1, 2))(x => List(x, x)) shouldBe List(1, 1, 2, 2)
    flatMap(List(1, 2, 3))(x => List(x, x)) shouldBe List(1, 1, 2, 2, 3, 3)
    flatMap(List(1, 2, 3))(x => List(x, x + 1)) shouldBe List(1, 2, 2, 3, 3, 4)
  }

  "filterUsingFlatMap" should "remove elements from a list unless they satisfy a given predicate" in {
    //when & then
    filterUsingFlatMap(List[Int]())(_ > 0) shouldBe List()
    filterUsingFlatMap(List(1))(_ < 0) shouldBe List()
    filterUsingFlatMap(List(1, 2))(_ != 0) shouldBe List(1, 2)
    filterUsingFlatMap(List(1, 2, 3))(x => (x % 2) == 0) shouldBe List(2)
  }

  "zipAndAdd" should "accept two lists and construct a new list by adding corresponding elements" in {
    //when & then
    zipAndAdd(List(), List()) shouldBe List()
    zipAndAdd(List(1), List(1)) shouldBe List(2)
    zipAndAdd(List(1), List(1, 3)) shouldBe List(2, 3)
    zipAndAdd(List(1, 3), List(1)) shouldBe List(2, 3)
    zipAndAdd(List(1, 2, 3), List()) shouldBe List(1, 2, 3)
    zipAndAdd(List(1, 2, 3), List(4, 5, 6)) shouldBe List(5, 7, 9)
  }

  "zipWith" should "be generic version of zipAndAdd" in {
    //when & then
    zipWith[Int](List(), List())(_ + _) shouldBe List()
    zipWith(List(1), List(1))(_ + _) shouldBe List(2)
    zipWith(List(1), List(1))(_ - _) shouldBe List(0)
    zipWith(List(1), List(1, 3))(_ + _) shouldBe List(2)
    zipWith(List(1, 3), List(1))(_ - _) shouldBe List(0)
    zipWith(List(1, 2, 3), List())(_ + _) shouldBe List()
    zipWith(List(1.0, 2.0, 3.0), List(4.0, 5.0, 6.0))(_ + _) shouldBe List(5.0, 7.0, 9.0)
  }

  "hasSubsequence" should "check whether a List contains another List as a subsequence" in {
    //when & then
    hasSubsequence(List(), List()) shouldBe true
    hasSubsequence(List(1, 2, 3), List()) shouldBe true
    hasSubsequence(List(1), List(1)) shouldBe true
    hasSubsequence(List(1, 3), List(1)) shouldBe true
    hasSubsequence(List(1, 2, 3, 4), List(1, 2)) shouldBe true
    hasSubsequence(List(1, 2, 3, 4), List(2, 3)) shouldBe true
    hasSubsequence(List(1, 2, 3, 4), List(4)) shouldBe true
    hasSubsequence(List(), List(1)) shouldBe false
    hasSubsequence(List(1), List(1, 3)) shouldBe false
    hasSubsequence(List(1), List(3)) shouldBe false
    hasSubsequence(List(1, 2, 3), List(3, 2)) shouldBe false
  }

  "treeSize" should "count the number of nodes (leaves and branches) in a tree" in {
    //when & then
    treeSize(Leaf(5)) shouldBe 1
    treeSize(Branch(Leaf(1), Leaf(2))) shouldBe 3
    treeSize(Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))) shouldBe 5
  }

  "maximum" should "return the maximum element in a Tree[Int]" in {
    //when & then
    maximum(Leaf(5)) shouldBe 5
    maximum(Branch(Leaf(1), Leaf(2))) shouldBe 2
    maximum(Branch(Leaf(2), Leaf(1))) shouldBe 2
    maximum(Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))) shouldBe 3
    maximum(Branch(Leaf(1), Branch(Leaf(3), Leaf(2)))) shouldBe 3
  }

  "depth" should "return the maximum path length from the root of a tree to any leaf" in {
    //when & then
    depth(Leaf(5)) shouldBe 1
    depth(Branch(Leaf(1), Leaf(3))) shouldBe 2
    depth(Branch(Leaf(3), Leaf(1))) shouldBe 2
    depth(Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))) shouldBe 3
    depth(Branch(Leaf(1), Branch(Branch(Leaf(2), Leaf(3)), Leaf(4)))) shouldBe 4
  }

  "treeMap" should "generalize modifying each element in a tree" in {
    //when & then
    treeMap(Leaf(5))(_.toString) shouldBe Leaf("5")
    treeMap(Branch(Leaf(1), Leaf(2)))(_.toString) shouldBe Branch(Leaf("1"), Leaf("2"))
    treeMap(Branch(Leaf(1), Branch(Leaf(2), Leaf(3))))(_.toString) shouldBe {
      Branch(Leaf("1"), Branch(Leaf("2"), Leaf("3")))
    }
  }

  "sizeUsingFold" should "count the number of nodes (leaves and branches) in a tree" in {
    //when & then
    sizeUsingFold(Leaf(5)) shouldBe 1
    sizeUsingFold(Branch(Leaf(1), Leaf(2))) shouldBe 3
    sizeUsingFold(Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))) shouldBe 5
  }

  "maximumUsingFold" should "return the maximum element in a Tree[Int]" in {
    //when & then
    maximumUsingFold(Leaf(-1)) shouldBe -1
    maximumUsingFold(Leaf(5)) shouldBe 5
    maximumUsingFold(Branch(Leaf(1), Leaf(2))) shouldBe 2
    maximumUsingFold(Branch(Leaf(2), Leaf(-1))) shouldBe 2
    maximumUsingFold(Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))) shouldBe 3
    maximumUsingFold(Branch(Leaf(1), Branch(Leaf(3), Leaf(2)))) shouldBe 3
  }

  "depthUsingFold" should "return the maximum path length from the root of a tree to any leaf" in {
    //when & then
    depthUsingFold(Leaf(5)) shouldBe 1
    depthUsingFold(Branch(Leaf(1), Leaf(3))) shouldBe 2
    depthUsingFold(Branch(Leaf(3), Leaf(1))) shouldBe 2
    depthUsingFold(Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))) shouldBe 3
    depthUsingFold(Branch(Leaf(1), Branch(Branch(Leaf(2), Leaf(3)), Leaf(4)))) shouldBe 4
  }

  "mapUsingFold" should "generalize modifying each element in a tree" in {
    //when & then
    mapUsingFold(Leaf(5))(_.toString) shouldBe Leaf("5")
    mapUsingFold(Branch(Leaf(1), Leaf(2)))(_.toString) shouldBe Branch(Leaf("1"), Leaf("2"))
    mapUsingFold(Branch(Leaf(1), Branch(Leaf(2), Leaf(3))))(_.toString) shouldBe {
      Branch(Leaf("1"), Branch(Leaf("2"), Leaf("3")))
    }
  }

  private def assertSameList[A](xs: List[A], f: List[A] => List[A]): Unit = {
    f(xs) should be theSameInstanceAs xs
  }
}
