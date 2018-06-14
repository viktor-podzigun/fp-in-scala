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

  private def compute(value: Int, sleep: Int = 50): Par[Int] = { es: ExecutorService =>
    es.submit(new Callable[Int] {
      override def call(): Int = {
        Thread.sleep(sleep)
        value
      }
    })
  }
}
