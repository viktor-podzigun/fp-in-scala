import Chapter02._
import org.scalatest._

class Chapter02Spec extends FlatSpec with Matchers {

  "fib" should "compute the nth fibonacci number" in {
    //when & then
    fib(-2) shouldBe 0
    fib(-1) shouldBe 0
    fib(0) shouldBe 0
    fib(1) shouldBe 1
    fib(2) shouldBe 1
    fib(3) shouldBe 2
    fib(4) shouldBe 3
    fib(5) shouldBe 5
    fib(6) shouldBe 8
    fib(7) shouldBe 13
    fib(8) shouldBe 21
  }

  "isSorted" should "check whether an `Array[A]` is sorted" in {
    //given
    val gtInt = (x1: Int, x2: Int) => x1 <= x2
    val gtStr = (x1: String, x2: String) => x1 <= x2

    //when & then
    isSorted(Array(), gtInt) shouldBe true
    isSorted(Array(1), gtInt) shouldBe true
    isSorted(Array(1, 1), gtInt) shouldBe true
    isSorted(Array(1, 2), gtInt) shouldBe true
    isSorted(Array(1, 2, 2, 2, 3), gtInt) shouldBe true
    isSorted(Array(1, 1, 0, 3), gtInt) shouldBe false
    isSorted(Array(2, 1), gtInt) shouldBe false

    isSorted(Array(), gtStr) shouldBe true
    isSorted(Array("aa", "aa", "ab", "ac"), gtStr) shouldBe true
    isSorted(Array("aa", "aa", "ac", "ab"), gtStr) shouldBe false
  }

  "curry" should "perform currying" in {
    //given
    val f = (a: Int, b: Long) => a.toDouble + b

    //when
    val result = curry(f)(5)

    //then
    result(2) shouldBe 7.0
  }

  "uncurry" should "perform un-currying" in {
    //given
    val f = (a: Int) => (b: Long) => a.toDouble + b

    //when
    val result = uncurry(f)(5, 2)

    //then
    result shouldBe 7.0
  }

  "compose" should "compose two functions" in {
    //given
    val f: Long => Double = (b: Long) => b.toDouble
    val g: Int => Long = (a: Int) => a.toLong

    //when
    val result = compose(f, g)(7)

    //then
    result shouldBe 7.0
    result shouldBe (f compose g)(7)
  }
}
