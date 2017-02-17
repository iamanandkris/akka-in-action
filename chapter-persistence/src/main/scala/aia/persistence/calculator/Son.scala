package aia.persistence.calculator

import akka.actor.{Actor, ActorLogging, ActorRef, AllForOneStrategy, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration._


object Son{
  def props(name:String):Props = Props(new Son(name))
}
class Son(val name:String) extends Actor with ActorLogging {
  //import akka.actor.SupervisorStrategy._
  //override val supervisorStrategy = SupervisorStrategy.stoppingStrategy
  //AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
  //    case _: Exception => println("From Son Strategy");Restart
  //}

  def persistenceId = "TextActor"
  var message:String = ""
  var actr:ActorRef = Actor.noSender

  def receive:Receive={
    case "crash" => throw new Exception("killed by self")
    case "there" => println("I am the son -> " + "still here")
    case x:String => {
      val k = sender()
      println("I am the son -> " + x)
      actr = context.actorOf(GrandSon.props())
      actr ! ("Test GrandChild ", k)
      //persist(x)(x => message=x)
    }
  }

  override def preStart(): Unit = {
    println("preStart the name is " + name)
  }
  override def postStop(): Unit = {
    println("postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestart")
    //super.preRestart (reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestart")
    super.postRestart(reason)
  }

  /*
    def receiveRecover:Receive ={
      case y:String => message = y
    }*/
}
