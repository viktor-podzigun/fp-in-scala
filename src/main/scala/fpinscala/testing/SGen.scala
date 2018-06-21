package fpinscala.testing

case class SGen[+A](forSize: Int => Gen[A])

object SGen {
  
  def unit[A](a: => A): SGen[A] = Gen.unit(a).unsized
}
