package aia.persistence.monadlearn
import cats.data.Coproduct
import cats.free.Free
import cats.{Id, ~>}
import freasymonad.cats.free

object FreasyFree  extends App {

  @free trait IntValue {
    type IntVal[A] = Free[Adt, A]
    sealed trait Adt[A]
    def intValue(n: Int): IntVal[Int]
    def intValueSquare(n: Int): IntVal[Int]
  }
  @free trait Arithmetic {
    type Arith[A] = Free[Adt, A]
    sealed trait Adt[A]
    def add(x: Int, y: Int): Arith[Int]
  }

  type Expr[A] = Coproduct[Arithmetic.Adt, IntValue.Adt, A]
  type Result[A] = Id[A]

  object IntValueInterpreter extends IntValue.Interp[Result] {
    override def intValue(n:Int):Result[Int]=n
    override def intValueSquare(n: Int):Result[Int] = n *n
  }
  object ArithmeticInterpreter extends Arithmetic.Interp[Result] {
    def add(x:Int,y:Int):Result[Int]= x+y
  }

  val interpreter1: Expr ~> Result = ArithmeticInterpreter.interpreter or IntValueInterpreter.interpreter

  def expr1(implicit value : IntValue.Injects[Expr], arith : Arithmetic.Injects[Expr]): Free[Expr, Int] = {
    import value._, arith._
    for {
      n <- intValueSquare(2)
      m <- add(n, n)
    } yield m
  }

  val run1 = expr1.foldMap(interpreter1)
  println(run1)
}
