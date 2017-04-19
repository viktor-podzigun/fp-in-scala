
object Chapter02 {

  /**
    * Exercise 1: Write a function to compute the `n`th fibonacci number
    */
  def fib(n: Int): Int = {
    @annotation.tailrec
    def loop(i: Int, n0: Int, n1: Int): Int =
      if (i >= n) n0
      else loop(i + 1, n1, n0 + n1)

    loop(0, 0, 1)
  }

  /**
    * Exercise 2: Implement a polymorphic function to check whether an `Array[A]` is sorted
    */
  def isSorted[A](as: Array[A], gt: (A,A) => Boolean): Boolean = {
    @annotation.tailrec
    def loop(i: Int): Boolean =
      if ((i + 1) >= as.length) true
      else if (!gt(as(i), as(i + 1))) false
      else loop(i + 1)

    loop(0)
  }

  /**
    * Exercise 3: Implement `curry`.
    *
    * Note that `=>` associates to the right, so we could write the return type as `A => B => C`
    */
  def curry[A,B,C](f: (A, B) => C): A => (B => C) = {
    (a: A) => { b: B =>
      f(a, b)
    }
  }
}
