package aia.persistence.monadlearn

import akka.actor.ActorRef
import cats._
import cats.data._
import cats.free._
import cats.free.Free._

import scala.language.higherKinds

object Operations {

  //First Operation
  sealed trait ValueAN[A]
  case class IntValueAN(n: Int) extends ValueAN[Int]

  class ValueAND[F[_]](implicit I: Inject[ValueAN, F]) {
    def intValan(n:Int): Free[F,Int] = inject[ValueAN,F](IntValueAN(n))
  }

  object ValueAND {
    implicit def value[F[_]](implicit I: Inject[ValueAN,F]): ValueAND[F] =
      new ValueAND[F]
  }


  //First Operation
  sealed trait ValueF[A]
  case class IntValue(n: Int) extends ValueF[Int]

  class Value[F[_]](implicit I: Inject[ValueF, F]) {
    def intVal(n:Int): Free[F,Int] = inject[ValueF,F](IntValue(n))
  }

  object Value {
    implicit def value[F[_]](implicit I: Inject[ValueF,F]): Value[F] =
      new Value[F]
  }

  //Second Operation
  sealed trait ArithF[A]
  case class Add(x: Int, y: Int) extends ArithF[Int]
  case class Sub(x:Int,y:Int) extends ArithF[Int]

  class Arith[F[_]](implicit I: Inject[ArithF, F]) {
    def add(x: Int, y: Int) : Free[F,Int] = Free.inject[ArithF,F](Add(x,y))
    def sub(x: Int, y: Int) : Free[F,Int] = Free.inject[ArithF,F](Sub(x,y))
  }

  object Arith {
    implicit def arith[F[_]](implicit I: Inject[ArithF,F]): Arith[F] =
      new Arith[F]
  }


  //Creating Co Product
  type Expr[A] = Coproduct[ArithF, ValueF, A]

  type ExprExtnd[A] = Coproduct[ValueAN, Expr,  A]

  //The Operation results are written on to State
  type OutputState[T] = State[Map[String,Int], T]
  type OutputTestState[T] = State[List[Any], T]


  //Interpreters for both operations
  object ValueId extends (ValueF ~> OutputState) {
    def apply[A](fa: ValueF[A]) = fa match {
      case IntValue(n) => for {
        p <- State.modify[Map[String,Int]](s => {println("From ValueId 1");s+("Initial value "+n.toString->n)})
        q <- State.inspect[Map[String,Int],A](_ => {println("From ValueId 2");n})
      }yield (q)
    }
  }

  //Interpreters for both operations
  object TestValueId extends (ValueF ~> OutputTestState) {
    def apply[A](fa: ValueF[A]) = fa match {
      case IntValue(n) => State.modify[List[Any]](s => IntValue(n) :: s).inspect(_ => 1)
    }
  }

  //Interpreters for both operations
  object TestValueANDId extends (ValueAN ~> OutputTestState) {
    def apply[A](fa: ValueAN[A]) = fa match {
      case IntValueAN(n) => State.modify[List[Any]](s => IntValueAN(n) :: s).inspect(_ => 1)
    }
  }

  object TestArithId  extends (ArithF ~> OutputTestState) {
    def apply[A](fa: ArithF[A]) = fa match {
      case Add(x,y) => State.modify[List[Any]](s => (Add(x,y)) :: s).inspect(_ => 1)
      case Sub(x,y) => State.modify[List[Any]](s => (Sub(x,y)) :: s).inspect(_ => 1)
    }
  }

  object ArithId  extends (ArithF ~> OutputState) {
    def apply[A](fa: ArithF[A]) = fa match {
      case Add(x,y) => {
        val m = for {
          p <- State.modify[Map[String, Int]](s => {
            println("From ArithId 1");
            val f: Int = x + y; s + (x.toString + " and " + y.toString + " Add up to " -> f)
          })
          q <- State.inspect[Map[String, Int], A](_ => {
            println("From ArithId 2"); (x + y)
          })
        } yield (q)
        m
      }

      case Sub(x,y) => for {
        p <- State.modify[Map[String,Int]](s => {println("From ArithId 3"); val f:Int = x-y ; s + (x.toString + " minus " + y.toString+" is "->f)})
        q <- State.inspect[Map[String,Int],A](_ => {println("From ArithId 4"); (x - y)})
      }yield(q)
    }
  }

 type MM = ExprExtnd ~> OutputTestState

  val interpreter: Expr ~> OutputState = ArithId or ValueId

  val testInterpreter: Expr ~> OutputTestState = TestArithId or TestValueId

  val testExtendedInterpreter: ExprExtnd ~> State[List[Any], ?]  =   TestValueANDId or testInterpreter

  //type Expr[A] = Coproduct[ArithF, ValueF, A]

  //type ExprExtnd[A] = Coproduct[ValueAN, Expr,  A]


}
