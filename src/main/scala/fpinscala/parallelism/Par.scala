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
  def fork[A](a: => Par[A]): Par[A] = { es: ExecutorService =>
    es.submit(new Callable[A] {
      def call: A = a(es).get
    })
  }

  /**
    * Fully evaluates a given Par, spawning parallel computations
    * as requested by `fork` and extracting the resulting value.
    */
  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  /**
    * Evaluates the given function's result asynchronously.
    */
  def asyncF[A, B](f: A => B): A => Par[B] = { a: A =>
    lazyUnit(f(a))
  }

  def map[A, B](pa: Par[A])(f: A => B): Par[B] =
    map2(pa, unit(()))((a, _) => f(a))

  def sequence[A](ps: List[Par[A]]): Par[List[A]] = {
    Par.map(ps.foldLeft(unit(List.empty[A])) { (pr, pa) =>
      map2(pa, pr)(_ :: _)
    })(_.reverse)
  }

  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = fork {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }

  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
    val pm = parMap(as)(a => (a, f(a)))
    map(pm)(xs => xs.filter(_._2).map(_._1))
  }

  def map3[A, B, C, D](pa: Par[A], pb: Par[B], pc: Par[C])(f: (A, B, C) => D): Par[D] = {
    map2(map2(pa, pb)(_ -> _), pc) { case ((a, b), c) =>
      f(a, b, c)
    }
  }
  
  def map4[A, B, C, D, E](pa: Par[A], pb: Par[B], pc: Par[C], pd: Par[D])(f: (A, B, C, D) => E): Par[E] = {
    map2(map2(pa, pb)(_ -> _), map2(pc, pd)(_ -> _)) { case ((a, b), (c, d)) =>
      f(a, b, c, d)
    }
  }
  
  def map5[A, B, C, D, E, F](pa: Par[A], pb: Par[B], pc: Par[C], pd: Par[D], pe: Par[E])(f: (A, B, C, D, E) => F): Par[F] = {
    map2(map2(pa, pb)(_ -> _), map2(map2(pc, pd)(_ -> _), pe)(_ -> _)) { case ((a, b), ((c, d), e)) =>
      f(a, b, c, d, e)
    }
  }

  def delay[A](fa: => Par[A]): Par[A] = es => fa(es)
  
  def choice[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] = {
    choiceN(map(cond)(if (_) 0 else 1))(List(t, f))
  }

  def choiceN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] = { es: ExecutorService =>
    val index = run(es)(n).get
    choices(index)(es)
  }

  def choiceMap[K, V](key: Par[K])(choices: Map[K, Par[V]]): Par[V] = { es: ExecutorService =>
    val k = run(es)(key).get
    choices(k)(es)
  }
  
  def chooser[A, B](pa: Par[A])(choices: A => Par[B]): Par[B] = { es: ExecutorService =>
    val a = run(es)(pa).get
    choices(a)(es)
  }

  def choiceUsingChooser[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] = {
    chooser(cond)(if (_) t else f)
  }

  def choiceNUsingChooser[A](pn: Par[Int])(choices: List[Par[A]]): Par[A] = {
    chooser(pn)(choices)
  }
}
