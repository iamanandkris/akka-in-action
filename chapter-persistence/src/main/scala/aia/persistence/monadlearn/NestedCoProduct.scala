package aia.persistence.monadlearn
import cats.data.Coproduct
import cats.free.{Free, Inject}

object NestedCoProduct {/*
  sealed trait Alg1[A]
  case class One[T] (v:T) extends Alg1[String]

  sealed trait Alg2[A]
  case class Two[T] (v:T) extends Alg2[String]

  sealed trait Alg3[A]
  case class Three[T] (v:T) extends Alg3[String]

  class Alg1Ops[F[_]](implicit I: Inject[Alg1, F]){
    def one[T](v:T): Free[F, String] = Free.inject[Alg1, F](One[T](v))
  }

  class Alg2Ops[F[_]](implicit I: Inject[Alg2, F]){
    def two[T](v:T): Free[F, String] = Free.inject[Alg2, F](Two[T](v))
  }

  class Alg3Ops[F[_]](implicit I: Inject[Alg3, F]){
    def three[T](v:T): Free[F, String] = Free.inject[Alg3, F](Three[T](v))
  }

  object NestedCoproduct extends App {
    //type API[A] = Coproduct[Alg1, Coproduct[Alg2, Alg3, ?], A]

    type PartialApi[A] = Coproduct[Alg2, Alg2, A]
    type API[A] = Coproduct[Alg1, PartialApi, A]

    implicit def alg1Ops[F[_]](implicit I: Inject[Alg1, F]): Alg1Ops[F] = new Alg1Ops[F]()
    implicit def alg2Ops[F[_]](implicit I: Inject[Alg2, F]): Alg2Ops[F] = new Alg2Ops[F]()
    implicit def alg3Ops[F[_]](implicit I: Inject[Alg3, F]): Alg3Ops[F] = new Alg3Ops[F]()

    //  compile if uncommented
    //  Error:(40, 3) could not find implicit value for parameter A1: sandbox.Alg1Ops[sandbox.NestedCoproduct.API]
    //  program
    //  ^
    //  implicit val inject3a: Inject[Alg1, API] = implicitly
   // implicit val inject3b: Inject[Alg2, API] = implicitly
   // implicit val inject3c: Inject[Alg3, API] = implicitly

    object TestValueId extends (ValueF ~> OutputTestState) {
      def apply[A](fa: ValueF[A]) = fa match {
        case IntValue(n) => State.modify[List[Any]](s => IntValue(n) :: s).inspect(_ => 1)
      }
    }

    object TestArithId  extends (ArithF ~> OutputTestState) {
      def apply[A](fa: ArithF[A]) = fa match {
        case Add(x,y) => State.modify[List[Any]](s => (Add(x,y)) :: s).inspect(_ => 1)
        case Sub(x,y) => State.modify[List[Any]](s => (Sub(x,y)) :: s).inspect(_ => 1)
      }
    }

    def program(implicit A1: Alg1Ops[API], A2: Alg2Ops[API], A3: Alg3Ops[API]): Free[API, Unit] = {
      import A1._, A2._, A3._
      for {
        _ <- one("a")
        _ <- two("b")
        _ <- three("c")
      } yield ()
    }

    program.foldMap()

  } */
}
