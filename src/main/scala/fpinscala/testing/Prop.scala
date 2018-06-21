package fpinscala.testing

import fpinscala.laziness.Stream
import fpinscala.purestate.{RNG, SimpleRNG}
import fpinscala.testing.Prop._

case class Prop(run: (MaxSize, TestCases, RNG) => Result) {

  def &&(p: Prop): Prop = Prop { (max, n, rng) =>
    val res1 = run(max, n, rng)
    if (res1.isFalsified) res1
    else {
      p.run(max, n, rng)
    }
  }
  
  def ||(p: Prop): Prop = Prop { (max, n, rng) =>
    val res1 = run(max, n, rng)
    if (!res1.isFalsified) res1
    else {
      p.run(max, n, rng)
    }
  }
}

object Prop {

  type MaxSize = Int
  type FailedCase = String
  type SuccessCount = Int
  type TestCases = Int
  type Tag = String

  sealed trait Result {
    def isFalsified: Boolean
  }

  case object Passed extends Result {
    val isFalsified = false
  }

  case class Falsified(failure: FailedCase,
                       successes: SuccessCount,
                       tag: Tag) extends Result {

    val isFalsified = true
  }

  def forAll[A](as: Gen[A], tag: Tag = "")(f: A => Boolean): Prop = Prop { (_, n, rng) =>
    randomStream(as)(rng).zip(Stream.from(0)).take(n).map {
      case (a, i) => try {
        if (f(a)) Passed
        else Falsified(a.toString, i, tag)
      } catch {
        case e: Exception => Falsified(buildMsg(a, e), i, tag)
      }
    }.find(_.isFalsified).getOrElse(Passed)
  }

  def randomStream[A](g: Gen[A])(rng: RNG): Stream[A] =
    Stream.unfold(rng)(rng => Some(g.sample.run(rng)))

  def buildMsg[A](s: A, e: Exception): String =
    s"test case: $s\n" +
      s"generated an exception: ${e.getMessage}\n" +
      s"stack trace:\n ${e.getStackTrace.mkString("\n")}"

  def forAll[A](g: SGen[A])(f: A => Boolean): Prop = forAll(g.forSize)(f)

  def forAll[A](g: Int => Gen[A])(f: A => Boolean): Prop = Prop { (max, n, rng) =>
    val casesPerSize = (n + (max - 1)) / max
    val props: Stream[Prop] =
      Stream.from(0).take((n min max) + 1).map(i => forAll(g(i))(f))
    val prop: Prop =
      props.map(p => Prop { (max, _, rng) =>
        p.run(max, casesPerSize, rng)
      }).toList.reduce(_ && _)
    prop.run(max, n, rng)
  }

  def run(p: Prop,
          maxSize: Int = 100,
          testCases: Int = 100,
          rng: RNG = SimpleRNG(System.currentTimeMillis)): Unit = p.run(maxSize, testCases, rng) match {

    case Falsified(msg, n, _) =>
      println(s"! Falsified after $n passed tests:\n\t$msg")
    case Passed =>
      println(s"+ OK, passed $testCases tests.")
  }
}
