package fpinscala.parallelism

import java.util.concurrent._

object Par {

  type Par[A] = ExecutorService => Future[A]

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit): A = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }

  private case class Map2Future[A, B, C](af: Future[A], bf: Future[B], f: (A, B) => C) extends Future[C] {
    lazy val get: C = f(af.get, bf.get)

    def get(timeout: Long, units: TimeUnit): C = {
      val started = System.nanoTime()
      val a = af.get(timeout, units)

      val nanos = System.nanoTime() - started
      val b = bf.get(units.toNanos(timeout) - nanos, TimeUnit.NANOSECONDS)
      f(a, b)
    }

    def isDone: Boolean = af.isDone && bf.isDone
    def isCancelled: Boolean = af.isCancelled && bf.isCancelled
    def cancel(evenIfRunning: Boolean): Boolean = {
      val aCancel = af.cancel(evenIfRunning)
      val bCancel = bf.cancel(evenIfRunning)
      aCancel && bCancel
    }
  }

  /**
    * Creates a computation that immediately results in the value a.
    */
  def unit[A](a: A): Par[A] = { _: ExecutorService => UnitFuture(a) }

  /**
    * Combines the results of two parallel computations with a binary function.
    */
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = { es: ExecutorService =>
    val af = a(es)
    val bf = b(es)
    Map2Future(af, bf, f)
  }

  /**
    * Wraps the expression a for concurrent evaluation by `run`.
    */
  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  /**
    * Marks a computation for concurrent evaluation by `run`.
    */
  def fork[A](a: => Par[A]): Par[A] = ???

  /**
    * Fully evaluates a given Par, spawning parallel computations
    * as requested by `fork` and extracting the resulting value.
    */
  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)
}
