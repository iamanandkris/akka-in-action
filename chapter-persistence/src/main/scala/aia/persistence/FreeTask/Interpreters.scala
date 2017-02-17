package aia.persistence.FreeTask

import aia.persistence.FreeTask.OperationActor.{Added, PrintedToConsole, Subtracted, immSeq}
import aia.persistence.FreeTask.Operations.ArithOperations._
import aia.persistence.FreeTask.Operations.OutputOperations._
import aia.persistence.FreeTask.Operations.{ExprFreeState, StateTask}
import akka.actor.{Actor, Stash}
import cats.data.StateT
import cats.~>
import monix.eval.Task
import monix.cats._

import scala.language.higherKinds
import scala.language.implicitConversions

/**
  * Created by anand on 15/12/16.
  */
trait Interpreters {
  this: Actor with Stash =>

  implicit object ArithState  extends (ArithF ~> StateTask) {
    def apply[A](fa: ArithF[A]):StateTask[A] = fa match {
      case Add(x,y) => {
        StateT(s => {
          val task = Task.eval {println(s"going to add ${x} and ${y}"); x + y}
          val kk = task.map(addTask => (s :+ Added(x,y),addTask))
          kk
        })
      }
      case Sub(x,y) => {
        StateT(s => {
          val task = Task.eval{x + y}
          val kk = task.map(addTask => (s :+ Subtracted(x,y),addTask))
          kk
        })
      }
    }
  }

  implicit object ConsoleState  extends (OutputF ~> StateTask) {
    def apply[A](fa: OutputF[A]):StateTask[A] = fa match {
      case OutputConsole(x) => {
        StateT(s => {
          val task = Task.eval {println("Printing the output -" + x)}
          val kk = task.map(addTask => (s :+ PrintedToConsole(x),addTask))
          kk
        })
      }
    }
  }

  val interpreter:ExprFreeState ~> StateTask = ArithState or ConsoleState
}
