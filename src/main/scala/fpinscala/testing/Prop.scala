package fpinscala.testing

trait Prop {

  def check: Boolean

  def &&(p: Prop): Prop = BooleanProp {
    check && p.check
  }
}

case class BooleanProp(check: Boolean) extends Prop
