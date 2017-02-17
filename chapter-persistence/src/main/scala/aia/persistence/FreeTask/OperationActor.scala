package aia.persistence.FreeTask

import aia.persistence.FreeTask.OperationActor.{EventToPersist, Succeeded}
import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import aia.persistence.FreeTask.OperationLogic._

import monix.cats._
import monix.execution.Scheduler.Implicits.global

import scala.util.{Failure, Success}

/**
  * Created by anand on 15/12/16.
  */
object OperationActor{
  //case class OperationState(value: scala.collection.immutable.Seq[Task[_]])
  trait EventToPersist
  case class Added(x:Int,y:Int) extends EventToPersist
  case class Subtracted(x:Int,y:Int) extends EventToPersist
  case class PrintedToConsole(x:Any) extends EventToPersist

  type immSeq = scala.collection.immutable.Seq[EventToPersist]

  trait ExecutionOutput
  case object Succeeded extends ExecutionOutput
  case object Failed extends ExecutionOutput

  //def props:Props = {
  //  Props(new OperationActor)
  //}
}
class OperationActor extends PersistentActor with ActorLogging with Interpreters {
  override def persistenceId = self.path.toString

  //val interpreter:ExprFreeState ~> StateTask = ArithState or ConsoleState
  var state:String = ""

  override def receiveRecover:Receive = {
    case event:EventToPersist => {println("Recovering event - " + event)}
    case RecoveryCompleted => {println("RecoveryCompleted...")}
  }
  override def receiveCommand:Receive = {
    case "1" =>{
      val replyTo= sender()
      val interpretedRes = addAndPrint(1,10).foldMap(interpreter)
      val task = interpretedRes.run(Nil)
      task.runAsync{res => res match {
        case Success((events, Succeeded)) => {
          persistAll(events)(applyEvent)
          deferAsync(()) { _ => replyTo ! "Success" }
        }
        case Success(x) => replyTo ! "Success Something else - " + x
        case Failure(ex) =>{
          println("Exception - " + ex)
          replyTo ! "Failure during execution"
        }
      }}

    }

    case "2" =>{

    }
  }

  def applyEvent(ev:EventToPersist): Unit ={
    state = state + ev.toString
    println("Latest State - " + state)
  }

}
