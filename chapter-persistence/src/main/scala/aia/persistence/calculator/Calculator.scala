//<start id="persistence-calc"/>
package aia.persistence.calculator

import akka.actor._
import akka.persistence._

object Calculator {
  def props = Props(new Calculator("a","b"))
  def name = "my-calculator-test8"

  //<start id="persistence-calc_commands_events"/>
  sealed trait Command //<co id="persistence-calc_command"/>
  case object Clear extends Command
  case class Add(value: Double) extends Command
  case class Subtract(value: Double) extends Command
  case class Divide(value: Double) extends Command
  case class Multiply(value: Double) extends Command
  case object PrintResult extends Command
  case object GetResult extends Command

  sealed trait Event extends Serializable//<co id="persistence-calc_event"/>
  case class Reset(actor:ActorRef) extends Event
  case class Added(value: Double,actor:ActorRef) extends Event
  case class Subtracted(value: Double,actor:ActorRef) extends Event
  case class Divided(value: Double,actor:ActorRef) extends Event
  case class Multiplied(value: Double,actor:ActorRef) extends Event
  //<end id="persistence-calc_commands_events"/>

  //<start id="persistence-calc_result"/>
  case class CalculationResult(result: Double = 0) {
    def reset = copy(result = 0)
    def add(value: Double) = copy(result = this.result + value)
    def subtract(value: Double) = copy(result = this.result - value)
    def divide(value: Double) = copy(result = this.result / value)
    def multiply(value: Double) = copy(result = this.result * value)
  }
  //<end id="persistence-calc_result"/>
}

//<start id="persistence-extend_persistent_actor"/>
class Calculator(val one:String, two:String) extends PersistentActor with ActorLogging {
  import Calculator._

  override def persistenceId = Calculator.name

  val testing ="Testing"
  context.actorSelection("/user/callSupervisor") ! Identify("Testing")

  var anotherActor = ActorRef.noSender
  var printActor= context.actorOf(Props(new GrandSon),"PrintActorTest")
  printActor ! "Starting the Action!!!!"

  var state = CalculationResult()

  val receiveRecover: Receive = {
    case event: Event => updateState(event)
    case RecoveryCompleted => log.info("Calculator recovery completed") //<co id="recovery_completed"/>
  }

  val receiveCommand: Receive = {

    case Add(value)      => persist(Added(value,printActor))(updateState)
    case Subtract(value) => persist(Subtracted(value,printActor))(updateState)
    case Divide(value)   => if(value != 0) persist(Divided(value,printActor))(updateState)
    case Multiply(value) => persist(Multiplied(value,printActor))(updateState)
    case PrintResult     => println(s"the result is: ${state.result}")
    case GetResult       => sender() ! state.result
    case Clear           => persist(Reset(printActor))(updateState)
  }

  val updateState: Event => Unit = {
    case Reset(x:ActorRef)            => state = state.reset; x ! state.result.toString
    case Added(value,x:ActorRef)      => state = state.add(value); x ! state.result.toString
    case Subtracted(value,x:ActorRef) => state = state.subtract(value); x ! state.result.toString
    case Divided(value,x:ActorRef)    => state = state.divide(value); x ! state.result.toString
    case Multiplied(value,x:ActorRef) => state = state.multiply(value); x ! state.result.toString
  }
}

