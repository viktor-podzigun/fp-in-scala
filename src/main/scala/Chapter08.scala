
import fpinscala.testing._

object Chapter08 {

  /**
    * Exercise 8.1
    *
    * To get used to thinking about testing in this way,
    * come up with properties that specify the implementation of
    * a `sum: List[Int] => Int` function. You don't have to write your properties
    * down as executable ScalaCheck code — an informal description is fine.
    * 
    * {{{
    *   forAll(intList)(ns => ns.reverse.sum == ns.sum)
    *   
    *   // if list has the same elements
    *   forAll(intList)(ns => ns.sum == (ns.head * ns.size))
    *   
    *   // if list is empty
    *   forAll(intList)(ns => ns.sum == 0)
    *   
    *   // if list has all elements negated
    *   forAll(intList)(ns => ns.map(_ * -1).sum == (ns.sum * -1))
    *   
    *   // if list has all elements multiplied by a constant
    *   forAll(intList)(ns => ns.map(_ * 2).sum == (ns.sum * 2))
    * }}}
    */
  
  /**
    * Exercise 8.2
    *
    * What properties specify a function that finds the maximum of a `List[Int]`?
    * 
    * {{{
    *   forAll(intList)(ns => ns.reverse.max == ns.max)
    *   
    *   // if list has the same elements
    *   forAll(intList)(ns => ns.max == ns.head)
    *   
    *   // if list is empty
    *   forAll(intList)(ns => ns.max == Int.MIN_VALUE)
    *   
    *   forAll(intList)(ns => ns.max == ns.sorted.last)
    * }}}
    */
  
  /**
    * Exercise 8.3
    *
    * Assuming the following representation of `Prop`, implement `&&` as a method of `Prop`.
    * 
    * @see [[fpinscala.testing.Prop.&&]]
    */
  
  /**
    * Exercise 8.4
    *
    * Implement `Gen.choose` using this representation of `Gen`.
    * It should generate integers in the range `start` to `stopExclusive`.
    * Feel free to use functions you've already written.
    * 
    * @see [[Gen.choose]]
    */
  
  /**
    * Exercise 8.5
    *
    * Let's see what else we can implement using this representation of `Gen`.
    * Try implementing `unit`, `boolean`, and `listOfN`.
    * 
    * @see [[Gen.unit]]
    * @see [[Gen.boolean]]
    * @see [[Gen.listOfN]]
    */
  
  /**
    * Exercise 8.6
    *
    * Implement `flatMap`, and then use it to implement this more dynamic version of `listOfN`.
    * Put `flatMap` and `listOfN` in the Gen class.
    * 
    * @see [[Gen.flatMap]]
    * @see [[Gen.listOfN]]
    */
  
  /**
    * Exercise 8.7
    *
    * Implement `union`, for combining two generators of the same type into one,
    * by pulling values from each generator with equal likelihood.
    * 
    * @see [[Gen.union]]
    */
  
  /**
    * Exercise 8.9
    *
    * Now that we have a representation of `Prop`, implement `&&` and `||`
    * for composing `Prop` values. Notice that in the case of failure
    * we don't know which property was responsible, the left or the right.
    * Can you devise a way of handling this, perhaps by allowing `Prop` values
    * to be assigned a tag or label which gets displayed in the event of a failure?
    * 
    * @see [[Prop.&&]]
    * @see [[Prop.||]]
    */
  
  /**
    * Exercise 8.10
    *
    * Implement helper functions for converting `Gen` to `SGen`.
    * You can add this as a method on `Gen`.
    * 
    * @see [[Gen.unsized]]
    */
  
  /**
    * Exercise 8.11
    *
    * Not surprisingly, `SGen` at a minimum supports many of the same operations as `Gen`,
    * and the implementations are rather mechanical. Define some convenience functions
    * on `SGen` that simply delegate to the corresponding functions on `Gen`.
    * 
    * @see [[SGen.unit]]
    */
  
  /**
    * Exercise 8.12
    *
    * Implement a `listOf` combinator that doesn't accept an explicit `size`.
    * It should return an `SGen` instead of a `Gen`.
    * The implementation should generate lists of the requested size.
    * 
    * @see [[SGen.listOf]]
    */
  
  /**
    * Exercise 8.13
    *
    * Define `listOf1` for generating nonempty lists,
    * and then update your specification of `max` to use this generator.
    * 
    * @see [[SGen.listOf1]]
    */
}
