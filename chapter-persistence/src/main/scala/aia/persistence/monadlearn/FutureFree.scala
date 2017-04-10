package aia.persistence.monadlearn

import cats.{Id, Monad, ~>}
import cats.free.{Free, Inject}

import scala.concurrent.{Await, Future}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by anand on 29/03/17.
  */
object FutureFree extends App{
  implicit val futureMonad = new Monad[Future] {
    override def flatMap[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa.flatMap(f)
    override def tailRecM[A, B](a: A)(f: (A) => Future[Either[A, B]]): Future[B] = {
      f(a).flatMap(_ match {
        case Right(eb) => Future.successful(eb)
        case Left(ea) => tailRecM(ea)(f)
      })
    }
    override def pure[A](x: A): Future[A] = Future.successful(x)
    def ap[A, B](fa: Future[A])(f: Future[(A) => B]): Future[B] =
      fa.flatMap(a => f.map(ff => ff(a)))
  }


  sealed trait Exec[A]
  case class ExecAsync(future:Future[Int]) extends Exec[Future[Int]]
  case class ExecSync(future:Future[Int],timeout:Int) extends Exec[Int]

  class ExecOpFac[F[_]](implicit I: Inject[Exec, F]) {
    def execAsync(toDo:Future[Int]) : Free[F,Future[Int]] = Free.inject[Exec,F](ExecAsync(toDo))
    def execSync(toDo:Future[Int],timeout:Int) : Free[F,Int] = Free.inject[Exec,F](ExecSync(toDo,timeout))
  }
  object ExecOpFac {
    implicit def execOperation[F[_]](implicit I: Inject[Exec,F]): ExecOpFac[F] =new ExecOpFac[F]
  }

  type ResultF[A] = Id[A]

  object ExecInterpreter  extends (Exec ~> ResultF) {
    def apply[A](fa: Exec[A]) = fa match {
      case ExecAsync(x) => x
      case ExecSync(x,y) => Await.result(x,y seconds)
    }
  }

  def expr1(implicit arith : ExecOpFac[Exec]): Free[Exec, Int] = {
    import arith._
    for {
      first <- execSync(Future{10},4)
      secon <- execSync(Future{3},4)
      third <- execSync(Future{5},4)
    } yield(first + secon + third)
  }

  val run1:Id[Int] = expr1.foldMap(ExecInterpreter)
  println(run1)

}
