
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
}
