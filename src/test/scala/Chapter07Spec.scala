import java.util.concurrent._

import Chapter07._
import fpinscala.parallelism.Par.Par
import fpinscala.parallelism._
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class Chapter07Spec extends FlatSpec
  with Matchers
  with BeforeAndAfterAll
  with MockFactory {

  private val es = Executors.newFixedThreadPool(2)

  override protected def afterAll(): Unit = {
    super.afterAll()

    es.shutdown()
  }

  "map2" should "combine the results of two parallel computations" in {
    //when
    val result = Par.run(es)(Par.map2(compute(1), compute(2))(_ + _))

    //then
    result.get shouldBe 3
  }

  it should "respect timeouts" in {
    //when
    val result = Par.run(es)(Par.map2(
      compute(1, sleep = 100),
      compute(2, sleep = 200)
    )(_ + _))

    //then
    the[TimeoutException] thrownBy {
      result.get(150, TimeUnit.MILLISECONDS)
    }
  }

  "asyncF" should "evaluate the given function's result asynchronously" in {
    //given
    def f(x: Int): String = (x * 2).toString

    //when
    val result = Par.run(es)(
      Par.asyncF(f)(5)
    )

    //then
    result.get shouldBe "10"
  }

  "sequence" should "convert list of parallel computations to single Par holding list of results" in {
    //given
    val list = List(compute(1), compute(2), compute(3))

    //when
    val result = Par.run(es)(
      Par.sequence(list)
    )

    //then
    result.get shouldBe List(1, 2, 3)
  }

  "parFilter" should "filter elements of a list in parallel" in {
    //given
    val list = List(1, 2, 3)

    //when
    val result = Par.run(es)(
      Par.parFilter(list)(_ > 1)
    )

    //then
    result.get shouldBe List(2, 3)
  }

  "map3" should "combine the results of three parallel computations" in {
    //when
    val result = Par.run(es)(
      Par.map3(compute(1), compute(2), compute(3))(_ + _ + _)
    )

    //then
    result.get shouldBe 6
  }

  "map4" should "combine the results of four parallel computations" in {
    //when
    val result = Par.run(es)(
      Par.map4(compute(1), compute(2), compute(3), compute(4))(_ + _ + _ + _)
    )

    //then
    result.get shouldBe 10
  }

  "map5" should "combine the results of five parallel computations" in {
    //when
    val result = Par.run(es)(
      Par.map5(compute(1), compute(2), compute(3), compute(4), compute(5))(_ + _ + _ + _ + _)
    )

    //then
    result.get shouldBe 15
  }

  "choice" should "choose between two computations" in {
    //given
    val n = compute(false)
    
    //when
    val result = Par.run(es)(
      Par.choice(n)(compute(1), compute(2))
    )

    //then
    result.get shouldBe 2
  }

  "choiceN" should "choose between N computations" in {
    //given
    val n = compute(2)
    
    //when
    val result = Par.run(es)(
      Par.choiceN(n)(List(compute(1), compute(2), compute(3)))
    )

    //then
    result.get shouldBe 3
  }

  "choiceMap" should "choose between map of computations" in {
    //given
    val n = compute("2")
    
    //when
    val result = Par.run(es)(
      Par.choiceMap(n)(Map("0" -> compute(1), "1" -> compute(2), "2" -> compute(3)))
    )

    //then
    result.get shouldBe 3
  }

  private def compute[A](value: A, sleep: Int = 50): Par[A] = { es: ExecutorService =>
    es.submit(new Callable[A] {
      override def call(): A = {
        Thread.sleep(sleep)
        value
      }
    })
  }
}
