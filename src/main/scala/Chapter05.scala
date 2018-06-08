
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

  /**
    * Exercise 5.6
    *
    * Hard: Implement `headOption` using `foldRight`.
    *
    * @see [[Stream.headOptionUsingFoldRight]]
    */

  /**
    * Exercise 5.7
    *
    * Implement `map`, `filter`, `append`, and `flatMap` using `foldRight`.
    * The `append` method should be non-strict in its argument.
    *
    * @see [[Stream.map]]
    * @see [[Stream.filter]]
    * @see [[Stream.append]]
    * @see [[Stream.flatMap]]
    */

  /**
    * Exercise 5.8
    *
    * Generalize `ones` slightly to the function `constant`, which returns an infinite `Stream`
    * of a given value.
    *
    * @see [[Stream.constant]]
    */

  /**
    * Exercise 5.9
    *
    * Write a function that generates an infinite stream of integers,
    * starting from `n`, then `n + 1`, `n + 2`, and so on.
    *
    * @see [[Stream.from]]
    */

  /**
    * Exercise 5.10
    *
    * Write a function `fibs` that generates the infinite stream of Fibonacci numbers:
    * `0, 1, 1, 2, 3, 5, 8`, and so on.
    *
    * @see [[Stream.fibs]]
    */

  /**
    * Exercise 5.11
    *
    * Write a more general stream-building function called `unfold`. It takes an initial `state`,
    * and a function for producing both the next state and the next value in the generated stream.
    *
    * @see [[Stream.unfold]]
    */

  /**
    * Exercise 5.12
    *
    * Write `fibs`, `from`, `constant`, and `ones` in terms of unfold.
    *
    * @see [[Stream.onesUsingUnfold]]
    * @see [[Stream.constantUsingUnfold]]
    * @see [[Stream.fromUsingUnfold]]
    * @see [[Stream.fibsUsingUnfold]]
    */

  /**
    * Exercise 5.13
    *
    * Use `unfold` to implement `map`, `take`, `takeWhile`, `zipWith` (as in chapter 3), and `zipAll`.
    * The `zipAll` function should continue the traversal as long as either stream has more elements —
    * it uses `Option` to indicate whether each stream has been exhausted.
    *
    * @see [[Stream.mapUsingUnfold]]
    * @see [[Stream.takeUsingUnfold]]
    * @see [[Stream.takeWhileUsingUnfold]]
    * @see [[Stream.zipWithUsingUnfold]]
    * @see [[Stream.zipAll]]
    */
}
