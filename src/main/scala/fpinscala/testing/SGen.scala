package fpinscala.testing

case class SGen[+A](forSize: Int => Gen[A])

object SGen {
  
  def unit[A](a: => A): SGen[A] = Gen.unit(a).unsized

  def listOf[A](g: Gen[A]): SGen[List[A]] = SGen(Gen.listOfN(_, g))
}
