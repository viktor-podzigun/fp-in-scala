
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
}
