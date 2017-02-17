package aia.persistence.calculator

import akka.actor._

object CalculatorMain extends App {
  val system = ActorSystem("calc")
  val calc = system.actorOf(Calculator.props, Calculator.name)

  calc ! Calculator.Add(2)
  calc ! Calculator.Multiply(4)
  calc ! Calculator.Divide(4)
  calc ! Calculator.PrintResult
}
