package aia.persistence.monadlearn

import cats.free._
import Operations.{ExprExtnd, _}


import scala.language.higherKinds

object FreeCoProductExample extends App {


    def expr1[T[?]](implicit arith : Arith[T],value : Value[T], valuea:ValueAND[T]): Free[T, Int] = {
      import arith._, value._, valuea._
      for {
        n <- intVal(2)
        f <- intVal(6)
        q <- intValan(2)
        m <- add(n, f)
        k <- add (f,m)
        v <- sub(k,n)
      } yield (v)
    }

    def test[T[?]](implicit arith : Arith[T],value : Value[T], valuea:ValueAND[T]): Free[T, Int] = {
      val k = List(1,2,3,4,5,6)
      k.foldLeft(Free.pure[T, Int](0)){(temp,b) => {
        temp.flatMap(x => {
          Free.pure[T, Int](x + b)
        })
      }}
    }


    //val runTest = expr1.foldMap(testExtendedInterpreter).run(List.empty[String]).value
    implicit val interpreterimp = testExtendedInterpreter
    val runTest = IndependentObject.foldAndRead[ExprExtnd[?],List[Any]](List.empty[String],()=>expr1[ExprExtnd])
    println(runTest)

    //val runProd = expr1.foldMap(interpreter).run(List.empty[String]).value
    //println(runProd)


}
