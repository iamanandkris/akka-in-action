package aia.persistence.FreeTask

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by anand on 15/12/16.
  */
object MainRunAp extends App {
  implicit val system = ActorSystem("Example1-system")
  implicit val timeout = Timeout(2.seconds)

  val testService = system.actorOf(Props[OperationActor])
  def test(inp: String) ={
    (testService ? inp).onComplete { case res => println(s"Command result: $res") }
  }

  test("1")
}
