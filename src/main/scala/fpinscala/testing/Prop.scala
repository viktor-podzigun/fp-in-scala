package fpinscala.testing

import fpinscala.laziness.Stream
import fpinscala.purestate.RNG
import fpinscala.testing.Prop._

case class Prop(run: (TestCases, RNG) => Result) {

  def &&(p: Prop): Prop = Prop { (n, rng) =>
    val res1 = run(n, rng)
    if (res1.isFalsified) res1
    else {
      p.run(n, rng)
    }
  }
  
  def ||(p: Prop): Prop = Prop { (n, rng) =>
    val res1 = run(n, rng)
    if (!res1.isFalsified) res1
    else {
      p.run(n, rng)
    }
  }
}

object Prop {
  
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

  def forAll[A](as: Gen[A], tag: Tag = "")(f: A => Boolean): Prop = Prop { (n, rng) =>
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
}
