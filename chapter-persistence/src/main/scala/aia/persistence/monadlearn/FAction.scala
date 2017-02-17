package aia.persistence.monadlearn

import java.util.UUID

import aia.persistence.monadlearn.FAction.{DoubleOutcome, IntOutcome, Outcome, StringOutcome}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

/**
  * Created by anand on 27/10/16.
  */
object FAction{

  sealed trait Outcome[+A]{
    val actionId:String
  }
  case class StringOutcome(val v:String, override val actionId:String) extends Outcome[String]
  case class IntOutcome(val v:Int, override val actionId:String) extends Outcome[Int]
  case class DoubleOutcome(val v:Double, override val actionId:String) extends Outcome[Double]
}

trait FAction[+A] {
  val id:String=UUID.randomUUID().toString
  def compute:Future[A]
  def map[B] (f:Future[A]=>Future[B]):Future[FAction[B]] = Future{FinalAction(compute)}
  def flatMap[B](f:Future[A]=>Future[FAction[B]]):Future[FAction[B]] = f(compute)
}

case class StringAction[+A](val str:String) extends FAction[StringOutcome] {
  override def compute ={Future {
      if (str == "Anand") StringOutcome("Anand",id)
      else if (str == "Nathan") StringOutcome("Nathan",id)
      else StringOutcome("Marek",id)
    }
  }
}

case class IntAction[+A](val str:Int) extends FAction[IntOutcome] {
  override def compute ={ Future {
      if (str == 1) IntOutcome(1,id)
      else if (str == 2) IntOutcome(2,id)
      else IntOutcome(3,id)
    }
  }
}

case class DoubleAction[+A](val str:Double) extends FAction[DoubleOutcome] {
  override def compute ={ Future {
      if (str == 1.0) DoubleOutcome(100.0,id)
      else if (str == 2.0) DoubleOutcome(200.0,id)
      else DoubleOutcome(300.0,id)
    }
  }
}

case class FinalAction[+T](val k:T) extends FAction[Nothing] {
  override def map[R]( f : Future[Nothing] => Future[R] ) : Future[FAction[Nothing]] = Future{this}
  override def flatMap[B](f: (Future[Nothing]) => Future[FAction[B]]): Future[FAction[Nothing]] = Future{this}
  override def compute: Future[Nothing] = throw new Exception("abc")
}

object test extends App{
/*
  val k = StringAction("Nathan")
  val m = IntAction(1)
  val n = IntAction(2)
  val o = IntAction(3)
  val p = StringAction("Anand")
  val q = StringAction("Nathan")
  //val f = k.map(x=>x)
  //println(f)

  val intA = IntAction(2)
  val strA = StringAction("Anand")
  val dblA = DoubleAction(2.0)
  val fnlA = FinalAction[String]("Finished")

  val aMp = Map(strA.id -> strA, intA.id -> intA, dblA.id -> dblA)

  val mp = Map (strA.id -> Map(StringOutcome("Anand",strA.id) -> intA.id,
                               StringOutcome("Nathan",strA.id) -> dblA.id,
                               StringOutcome("Marek",strA.id) -> fnlA.id),
                intA.id -> Map(IntOutcome(1,intA.id) -> dblA.id,
                               IntOutcome(2,intA.id) -> fnlA.id,
                               IntOutcome(3,intA.id) -> fnlA.id),
                dblA.id -> Map(DoubleOutcome(1.0,dblA.id) -> intA.id,
                               DoubleOutcome(2.0,dblA.id) -> fnlA.id,
                               DoubleOutcome(3.0,dblA.id) -> fnlA.id))



  def f[T <: Outcome[_]](oc:Future[T]):Future[FAction[T]]={

    val f = for {
      o <- oc
      k <- {
        val f = mp(o.actionId)
        //f(o)
      }/*o match {
        case StringOutcome("Marek") => IntAction(1)
        case StringOutcome("Anand") => IntAction(2)
        case StringOutcome("Nathan") => IntAction(3)
      }*/
    } yield(k)
    f
  }

  val fff = for {
    one <- k
    two <- f(one)
  }yield(two)

  val mmm = Await.result(fff, 10 nanos)

  println((mmm.asInstanceOf[FinalAction[Outcome[_]]].k))*/

}