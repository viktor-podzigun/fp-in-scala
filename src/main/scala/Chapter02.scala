
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
}
