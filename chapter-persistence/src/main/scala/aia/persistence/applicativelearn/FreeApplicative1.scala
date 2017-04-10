package aia.persistence.applicativelearn

import cats.free.{Free, FreeApplicative, Inject}

/**
  * Created by anand on 05/04/17.
  */
object FreeApplicative1 extends App{

  sealed trait ArithF[A]
  case class Add(x:Int, y:Int) extends ArithF[Int]
  case class Sub(x:Int, y:Int) extends ArithF[Int]
  class Arith[F[_]](implicit I: Inject[ArithF, F]) {
    def add(x: Int, y: Int)  = FreeApplicative.lift(Add(x,y))
    def sub(x: Int, y: Int)  = FreeApplicative.lift(Sub(x,y))
  }
  object Arith {
    implicit def arith[F[_]](implicit I: Inject[ArithF,F]): Arith[F] =new Arith[F]
  }

}
