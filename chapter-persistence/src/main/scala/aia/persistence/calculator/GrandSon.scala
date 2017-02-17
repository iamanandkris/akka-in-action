package aia.persistence.calculator

import akka.actor.{Actor, ActorLogging, ActorRef, AllForOneStrategy, OneForOneStrategy, Props}
import scala.concurrent.duration._

object GrandSon{
  def props() = Props(new GrandSon)
}
class GrandSon extends Actor with ActorLogging {
  //import akka.actor.SupervisorStrategy._
  //override val supervisorStrategy =
  //  AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
  //   case _: Exception => Restart
  // }
  def persistenceId = "PrintActor"
  var message:String = ""

  def receive:Receive={
    case (x:String, y:ActorRef) => {
      println("I am the grand child -> " + x)
      message = "Anand"
      y ! "killfather"
      //throw new Exception("killed by self")
    }
    case "Are you Alive?" => println("I am Grand Child, grandpa I am still alive ->" + message)
  }

  override def preStart(): Unit = {
    println("preStartGS")
  }
  override def postStop(): Unit = {
    println("postStopGS")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestartGS")
    super.preRestart (reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestartGS")
    super.postRestart(reason)
  }
/*
  def receiveRecover:Receive ={
    case y:String => message = y
  }*/
}
