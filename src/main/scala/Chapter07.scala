import fpinscala.parallelism._

object Chapter07 {

  /**
    * Exercise 7.1
    *
    * `Par.map2` is a new higher-order function for combining the result
    * of two parallel computations. What is its signature?
    * Give the most general signature possible (don't assume it works only for `Int`).
    *
    * @see [[Par.map2]]
    */
  def sum(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.size <= 1) {
      Par.unit(ints.headOption getOrElse 0)
    }
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(sum(l), sum(r))(_ + _)
    }
}
