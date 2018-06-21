package fpinscala.testing

case class SGen[+A](forSize: Int => Gen[A])

object SGen {
  
  def unit[A](a: => A): SGen[A] = Gen.unit(a).unsized

  def listOf[A](g: Gen[A]): SGen[List[A]] = SGen(Gen.listOfN(_, g))
  
  def listOf1[A](g: Gen[A]): SGen[List[A]] = SGen { n =>
    val size = if (n == 0) 1 else n
    
    Gen.listOfN(size, g)
  }
}
