package aia.persistence.calculator

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, AllForOneStrategy, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy}

import scala.concurrent.duration._

/**
  * Created by anand on 07/10/16.
  */
object GrandFather{
  def props()=Props(new GrandFather)
}
class GrandFather extends Actor with ActorLogging {
  import akka.actor.SupervisorStrategy._
  override val supervisorStrategy =
    AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Exception => println("From Grand Father Strategy");Restart
  }

  var child:ActorRef=Actor.noSender
  //var grandchild:ActorRef = Actor.noSender
  override def receive: Receive = {
    case x:String if x.equals("Test Grandpa") =>
      println("I am grand father ->" + x)
      child = context.actorOf(Son.props("testName"))
      child ! "Test Son"
    case "killfather" =>
      println("I am the grandfather, Got Message from grand child, gonna kill my son")
      val grandchild = sender()
      child ! "crash"
      child ! "there"
      grandchild ! "Are you Alive?"
      child ! "there"
      grandchild ! "Are you Alive?"
      child ! "there"
      grandchild ! "Are you Alive?"
      child ! "there"
      grandchild ! "Are you Alive?"
      child ! "there"

  }

}
