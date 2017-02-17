package aia.persistence.monadlearn


import cats.free.Free

/**
  * Created by anand on 21/11/16.
  */
object FreeWithStateApp extends App {
/*
  def expr1(implicit arith : Arith[ArithF]): Free[ArithF, Int] = {
    import arith._
    for {
      m <- add(1, 2)
      k <- add (3,m)
      v <- sub(k,5)
    } yield (v)
  }

  val runTest = expr1.foldMap(ArithInterpreter)
    runTest.run(Map.empty[String,Int])
*/
}
