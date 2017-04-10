package aia.persistence.monadlearn

import akka.actor.{Actor, Props}

/**
  * Created by anand on 30/03/17.
  */
object SagaActor{
  def props:Props = Props(new SagaActor)
}
class SagaActor extends Actor{

  override def receive ={
    case x:Int => sender() ! x
    case y:String => sender() ! y.foldLeft(0)((a,b) => a + b)
    case z@_ => sender() ! 30
  }
}
