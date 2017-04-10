package aia.persistence.monadlearn

import aia.persistence.monadlearn.Operations.{Add, Sub}
import cats._
import cats.data.{State, StateT}
import cats.free.{Free, Inject}

import scala.concurrent.Future

/**
  * Created by anand on 21/11/16.
  */
object FreeWithState extends App { /*
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


  type OutputState[T] = StateT[Future,Map[String,Int], T]

  //val interpreter: ArithF ~> OutputState

  import scala.concurrent.ExecutionContext.Implicits.global

  private val invitationsOpTaskInterpreter = new (ArithF ~> Future) {
    override def apply[A](testOP: ArithF[A]): Future[A] = testOP match {
      case Add(x,y) => Future{(x+y).asInstanceOf[A]}
      case Sub(x,y) => Future{(x-y).asInstanceOf[A]}
    }
  }

  //implicit  val appl =

  object ArithInterpreter extends (ArithF ~> OutputState) {
    def apply[A](fa: ArithF[A]):OutputState[A] = fa match {
      case Add(x,y) => StateT(s => invitationsOpTaskInterpreter(fa).map(p => (s, p)))
      case Sub(x,y) => StateT(s => invitationsOpTaskInterpreter(fa).map(p => (s, p)))
    }
  }


  def expr1(n:Int)(implicit arith : Arith[ArithF]): Free[ArithF, Int] = {
    import arith._
    for {
      m <- add(n, n)
    } yield m
  }


  val run1 = expr1(12).foldMap(ArithInterpreter)
  println(run1)
*/
}
