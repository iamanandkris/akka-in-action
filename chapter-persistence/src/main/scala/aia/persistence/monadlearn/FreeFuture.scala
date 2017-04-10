package aia.persistence.monadlearn

import akka.actor.ActorSystem
import akka.util.Timeout
import cats.{Id, Monad, ~>}
import cats.data.{Coproduct, OptionT}
import cats.free.{Free, Inject}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.collection.parallel.ParMap
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
//import cats.implicits.catsStdInstancesForFuture


object FreeFuture extends App{

  //Cats Monad for scala Future, to use in Free.
  implicit def futureMonad = new Monad[Future] {
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


  ///////////////////////////////////////////////////USUAL FREE DEFINITIONS START HERE//////////////////////////////////
  sealed trait ArithF[A]
  case class Add(x:Int, y:Int) extends ArithF[Int]
  case class Sub(x:Int, y:Int) extends ArithF[Int]
  class Arith[F[_]](implicit I: Inject[ArithF, F]) {
    def add(x: Int, y: Int) : Free[F,Int] = Free.inject[ArithF,F](Add(x,y))
    def sub(x: Int, y: Int) : Free[F,Int] = Free.inject[ArithF,F](Sub(x,y))
  }
  object Arith {
    implicit def arith[F[_]](implicit I: Inject[ArithF,F]): Arith[F] =new Arith[F]
  }

  sealed trait SagaAlgebra[A]
  case class Query[A,B](x:A) extends SagaAlgebra[B]
  class SagaOpFac[F[_]](implicit I: Inject[SagaAlgebra, F]) {
    def invokeSaga[A,B](x:A) : Free[F,B] = Free.inject[SagaAlgebra,F](Query[A,B](x))
  }
  object SagaOpFac {
    implicit def sagaQuery[F[_]](implicit I: Inject[SagaAlgebra,F]): SagaOpFac[F] =new SagaOpFac[F]
  }

  sealed trait FutureTransformer[T]
  case class Transform[A](x:Future[A]) extends FutureTransformer[A]
  class FutureTrasformOpfac[F[_]](implicit I: Inject[FutureTransformer, F]) {
    def transform[A](x:Future[A]) : Free[F,A] = Free.inject[FutureTransformer,F](Transform[A](x))
  }
  object FutureTrasformOpfac {
    implicit def dbo[F[_]](implicit I: Inject[FutureTransformer,F]): FutureTrasformOpfac[F] =new FutureTrasformOpfac[F]
  }

  def trans[A](x:Future[A])(implicit trasOp:FutureTrasformOpfac[Expr])={
    trasOp.transform[A](x)
  }

  case class Row(firstName:String, lastName:String, age:Int, salary:Int)
  case class Rows(map:Map[String, Row])

  sealed trait DBOperation[A]
  case class Get(key:String) extends DBOperation[Rows]
  case class Put(key:String, value:Int) extends DBOperation[Unit]
  class DBOpFac[F[_]](implicit I: Inject[DBOperation, F]) {
    def getFromDB(key:String) : Free[F,Rows] = Free.inject[DBOperation,F](Get(key))
    def putToDB(key:String, value:Int) : Free[F,Unit] = Free.inject[DBOperation,F](Put(key,value))
  }
  object DBOpFac {
    implicit def dbo[F[_]](implicit I: Inject[DBOperation,F]): DBOpFac[F] =new DBOpFac[F]
  }



  type Expr1[A] = Coproduct[ArithF, DBOperation, A]
  type Expr2[A] = Coproduct[SagaAlgebra, Expr1, A]
  type Expr[A] = Coproduct[FutureTransformer,Expr2,A]
  type ResultF[A] = Future[A]

  object ArithIdF  extends (ArithF ~> ResultF) {
    def apply[A](fa: ArithF[A]) = fa match {
      case Add(x,y) => Future.successful(x + y)
      case Sub(x,y) => Future.successful(x - y)
    }
  }

  object FutureTransformerInterpreter  extends (FutureTransformer ~> ResultF) {
    def apply[A](fa: FutureTransformer[A]) = fa match {
      case Transform(x) => x
    }
  }

  object DBOperationF  extends (DBOperation ~> ResultF) {
    val row1 = Row("A","B",12,0)
    val row2 = Row("E","F",45,100)
    val row3 = Row("H","I",30,300)
    val row4 = Row("J","K",19,50)

    val mapdata = mutable.Map.empty[String, Rows]
    mapdata+=("test1" -> Rows(Map("1" -> row1, "2" -> row2)))
    mapdata+=("test4" -> Rows(Map("1" -> row1, "2" -> row2,"3"->row3,"4"->row4)))

    def apply[A](fa: DBOperation[A]) = fa match {
      case Get(x) => {println(s"Reading db for ${x}");Future{mapdata(x).asInstanceOf[A]}}
      case Put(x,y) => Future{mapdata+(x -> y); ()}
    }
  }

  object SagaInter  extends (SagaAlgebra ~> ResultF) {
    import akka.pattern.ask
    import scala.concurrent.duration._
    implicit val timeout = Timeout(5 seconds)
    def apply[A](fa: SagaAlgebra[A]) = fa match {
      case Query(x) => {
        val actorSystem = ActorSystem("testActorsystem")
        val actor = actorSystem.actorOf(SagaActor.props)
        (actor ? x).map(x => x.asInstanceOf[A])
      }
    }
  }

  val interpreter1: Expr1 ~> ResultF = ArithIdF or DBOperationF
  val interpreter2:Expr2 ~> ResultF = SagaInter or interpreter1
  val interpreter:Expr ~> ResultF =  FutureTransformerInterpreter or interpreter2

  ///////////////////////////////////////////////////USUAL FREE DEFINITIONS END HERE//////////////////////////////////////

  def getValidatedResult(first:Int,second:Int):Boolean = first > second


  trait BoundedContextLibrary{
    //trait UserOP{
    //  user:User =>
    //}

    //def user:User

    def make[T,M](skeleton: => Free[Expr,T]):Free[Expr,T] = skeleton

    def ifF[T[?],B](condition : => Boolean)(left : => Free[T, B])(right : => B):Free[T, B] =
      if (condition) left else liftToFree(right)

    def liftToFree[T[?],B](processMessageResult:B):Free[T, B] =Free.pure[T, B] (processMessageResult)
    implicit def lift[T[?],B](value: => B): Free[T, B]=Free.pure[T,B](value)
  }

  trait User{}

  class ConcreteUser extends User

  object UserBoundedContext extends BoundedContextLibrary{

    val saga = SagaOpFac.sagaQuery[Expr]
    val arith = Arith.arith[Expr]
    val dbo = DBOpFac.dbo[Expr]

    import saga._
    import arith._
    import dbo._

    def combineFuture2[A,B](fa1:Future[A],fa2:Future[B]):Future[(A,B)]= {
      fa1.flatMap(a1 => fa2.map(a2 => (a1,a2)))
    }


    def updateUserLastName(un:String)(implicit interpreter:(Expr ~> ResultF)):Free[Expr,Int]={

      import cats.syntax.traverse._

      val second = getFromDB("test1").foldMap[ResultF](interpreter)
      val first = getFromDB("test4").foldMap[ResultF](interpreter)

      for {
        first1   <- trans(first)
        second1  <- trans(second)
        fourth   <- if (second1 == first1) 12 else add(10, second1.map("1").salary)
        valid    <- getValidatedResult(first1.map("1").salary,second1.map("2").salary)
        another  <- 1230
        reply    <- if (valid) 10 else another
        k        <- sub(1230,first1.map("1").salary)
        p        <- add(k, reply)
        q        <- add(p,another)
        r        <- add(q, fourth match {
                     case x:Int => 123
                     case _ => 234
                    })
      } yield r
    }
  }

  object APIModel {
    def execAPI()= {
      implicit val interp = interpreter
      val freeLogic = UserBoundedContext.make(UserBoundedContext.updateUserLastName("test"))
      val run1 = Await.result(freeLogic.foldMap[ResultF](interpreter), 10 seconds)
      println(run1)
    }
  }

  APIModel.execAPI()
}
