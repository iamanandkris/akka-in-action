package aia.persistence.monadlearn

import cats.data

/**
  * Created by anand on 31/10/16.
  */
object FreeMonadONe extends App{

  import cats.data.Coproduct
  import cats.free.{Inject, Free}
  import cats.{Id, ~>}
  import scala.collection.mutable.ListBuffer
  /* Handles user interaction */
  sealed trait Interact[A]
  case class Ask(prompt: String) extends Interact[String]
  case class Tell(msg: String) extends Interact[Unit]

  /* Represents persistence operations */
  sealed trait DataOp[A]
  case class AddCat(a: String) extends DataOp[Unit]
  case class GetAllCats() extends DataOp[List[String]]

  type CatsApp[A] = Coproduct[DataOp, Interact, A]

  class Interacts[F[_]](implicit I: Inject[Interact, F]) {
    def tell(msg: String): Free[F, Unit] = Free.inject[Interact, F](Tell(msg))
    def ask(prompt: String): Free[F, String] = Free.inject[Interact, F](Ask(prompt))
  }

  object Interacts {
    implicit def interacts[F[_]](implicit I: Inject[Interact, F]): Interacts[F] = new Interacts[F]
  }

  class DataSource[F[_]](implicit I: Inject[DataOp, F]) {
    def addCat(a: String): Free[F, Unit] = Free.inject[DataOp, F](AddCat(a))
    def getAllCats: Free[F, List[String]] = Free.inject[DataOp, F](GetAllCats())
  }

  object DataSource {
    implicit def dataSource[F[_]](implicit I: Inject[DataOp, F]): DataSource[F] = new DataSource[F]
  }

  def program(implicit I : Interacts[CatsApp], D : DataSource[CatsApp]): Free[CatsApp, Unit] = {

    import I._, D._

    for {
      cat <- ask("What's the kitty's name?")
      _ <- addCat(cat)
      cats <- getAllCats
      _ <- tell(cats.toString)
    } yield ()
  }

  object ConsoleCatsInterpreter extends (Interact ~> Id) {
    def apply[A](i: Interact[A]) = i match {
      case Ask(prompt) =>
        println(prompt)
        scala.io.StdIn.readLine()
      case Tell(msg) =>
        println(msg)
    }
  }

  object InMemoryDatasourceInterpreter extends (DataOp ~> Id) {

    private[this] val memDataSet = new ListBuffer[String]

    def apply[A](fa: DataOp[A]) = fa match {
      case AddCat(a) => memDataSet.append(a); ()
      case GetAllCats() => memDataSet.toList
    }
  }

  val interpreter: CatsApp ~> Id = InMemoryDatasourceInterpreter or ConsoleCatsInterpreter

  import DataSource._, Interacts._
  val evaled: Unit = program.foldMap(interpreter)
}
