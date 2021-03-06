
import fpinscala.datastructures._

object Chapter04 {

  def mean(xs: Seq[Double]): Option[Double] = {
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)
  }

  /**
    * Exercise 4.1
    *
    * Implement all of the preceding functions on `Option`.
    * As you implement each function, try to think about what it means
    * and in what situations you'd use it.
    *
    * @see [[Option.map]]
    * @see [[Option.flatMap]]
    * @see [[Option.getOrElse]]
    * @see [[Option.orElse]]
    * @see [[Option.filter]]
    */

  /**
    * Exercise 4.2
    *
    * Implement the `variance` function in terms of `flatMap`.
    * If the `mean` of a sequence is `m`, the `variance` is the `mean` of `math.pow(x - m, 2)`
    * for each element `x` in the sequence.
    */
  def variance(xs: Seq[Double]): Option[Double] = {
    mean(xs).flatMap { m =>
      mean(xs.map { x =>
        math.pow(x - m, 2)
      })
    }
  }

  /**
    * Exercise 4.3
    *
    * Write a generic function `map2` that combines two `Option` values using a binary function.
    * If either `Option` value is None, then the return value is too.
    */
  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a.flatMap { v1 =>
      b.map { v2 =>
        f(v1, v2)
      }
    }
  }

  /**
    * Exercise 4.4
    *
    * Write a function sequence that combines a list of `Option`s into one `Option`
    * containing a `List` of all the `Some` values in the original list.
    * If the original list contains `None` even once, the result of the function
    * should be `None`; otherwise the result should be `Some` with a list of all the values.
    *
    * @see [[Option.sequence]]
    */

  /**
    * Exercise 4.5
    *
    * Implement `traverse` function.
    * It's straightforward to do using `map` and `sequence`, but try for a more efficient implementation
    * that only looks at the list once.
    * In fact, implement `sequence` in terms of `traverse`.
    *
    * @see [[Option.traverse]]
    * @see [[Option.sequence2]]
    */

  /**
    * Exercise 4.6
    *
    * Implement versions of `map`, `flatMap`, `orElse`, and `map2` on `Either`
    * that operate on the `Right` value.
    *
    * @see [[Either.map]]
    * @see [[Either.flatMap]]
    * @see [[Either.orElse]]
    * @see [[Either.map2]]
    */

  /**
    * Exercise 4.7
    *
    * Implement `sequence` and `traverse` for `Either`.
    * These should return the first error that's encountered, if there is one.
    *
    * @see [[Either.sequence]]
    * @see [[Either.traverse]]
    */

  /**
    * Exercise 4.8
    *
    * In this implementation, `map2` is only able to report one error,
    * even if both the name and the age are invalid. What would you need to change
    * in order to report both errors? Would you change `map2` or the signature of `mkPerson`?
    * Or could you create a new data type that captures this requirement better than `Either` does,
    * with some additional structure?
    * How would `orElse`, `traverse`, and `sequence` behave differently for that data type?
    *
    * @see [[mkPerson2]]
    */
  case class Person(name: Name, age: Age)
  case class Name(value: String)
  case class Age(value: Int)

  def mkName(name: String): Either[String, Name] =
    if (name == "" || name == null) Left("Name is empty.")
    else Right(Name(name))

  def mkAge(age: Int): Either[String, Age] =
    if (age < 0) Left("Age is out of range.")
    else Right(Age(age))

  def mkPerson(name: String, age: Int): Either[String, Person] =
    mkName(name).map2(mkAge(age))(Person)

  def mkPerson2(name: String, age: Int): Either[List[String], Person] = {
    (mkName(name), mkAge(age)) match {
      case (Left(e1), Left(e2)) => Left(List(e1, e2))
      case (Left(e), _) => Left(List(e))
      case (_, Left(e)) => Left(List(e))
      case (Right(n), Right(a)) => Right(Person(n, a))
    }
  }
}
