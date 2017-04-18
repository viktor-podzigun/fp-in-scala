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
}
