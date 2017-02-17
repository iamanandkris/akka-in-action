package aia.persistence.FreeTask

import aia.persistence.FreeTask.OperationActor.{immSeq}
import cats.data.{Coproduct, State, StateT}
import cats.free.{Free, Inject}
import monix.eval.Task

/**
  * Created by anand on 15/12/16.
  */
object Operations {
  object ArithOperations {
    sealed trait ArithF[A]
    case class Add(x: Int, y: Int) extends ArithF[Int]
    case class Sub(x: Int, y: Int) extends ArithF[Int]

    class ArithOpFac[F[_]](implicit I: Inject[ArithF, F]) {
      def add(x: Int, y: Int): Free[F, Int] = Free.inject[ArithF, F](Add(x, y))
      def sub(x: Int, y: Int): Free[F, Int] = Free.inject[ArithF, F](Sub(x, y))
    }

    object ArithOpFac {
      implicit def arith[F[_]](implicit I: Inject[ArithF, F]): ArithOpFac[F] = new ArithOpFac[F]
    }


  }

  object OutputOperations {
    sealed trait OutputF[A]
    case class OutputConsole(a: Any) extends OutputF[Unit]

    class OutputOpFac[F[_]](implicit I: Inject[OutputF, F]) {
      def outputConsole(x: Any): Free[F, Unit] = Free.inject[OutputF, F](OutputConsole(x))
    }

    object OutputOpFac {
      implicit def outputop[F[_]](implicit I: Inject[OutputF, F]): OutputOpFac[F] = new OutputOpFac[F]
    }
  }

  type ExprFreeState[A] = Coproduct[ArithOperations.ArithF, OutputOperations.OutputF, A]
  type StateTask[A] = StateT[Task, immSeq, A]

}
