
import fpinscala.laziness._

object Chapter05 {

  /**
    * Exercise 5.1
    *
    * Write a function to convert a `Stream` to a `List`, which will force its evaluation
    * and let you look at it in the REPL. You can convert to the regular `List` type
    * in the standard library.
    * You can place this and other functions that operate on a `Stream` inside the `Stream` trait.
    *
    * @see [[Stream.toList]]
    */

  /**
    * Exercise 5.2
    *
    * Write the function `take(n)` for returning the first `n` elements of a `Stream`,
    * and `drop(n)` for skipping the first `n` elements of a `Stream`.
    *
    * @see [[Stream.take]]
    * @see [[Stream.drop]]
    */

  /**
    * Exercise 5.3
    *
    * Write the function `takeWhile` for returning all starting elements of a `Stream`
    * that match the given predicate.
    *
    * @see [[Stream.takeWhile]]
    */

  /**
    * Exercise 5.4
    *
    * Implement `forAll`, which checks that all elements in the `Stream` match a given predicate.
    * Your implementation should terminate the traversal as soon as it encounters a non-matching value.
    *
    * @see [[Stream.forAll]]
    */

  /**
    * Exercise 5.5
    *
    * Use `foldRight` to implement `takeWhile`.
    *
    * @see [[Stream.takeWhileUsingFoldRight]]
    */
}
