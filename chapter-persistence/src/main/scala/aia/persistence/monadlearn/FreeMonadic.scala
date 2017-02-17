package aia.persistence.monadlearn


import cats.{Id, ~>}
import cats.data.{Coproduct, State}
import cats.free.{Free, Inject}

/**
  * Created by anand on 31/10/16.
  */
object FreeMonadic extends App{
/*
  sealed trait Instruction[A]
  case class Forward(position: Position, length: Int) extends Instruction[Position]
  case class Backward(position: Position, length: Int) extends Instruction[Position]
  case class RotateLeft(position: Position, degree: Degree) extends Instruction[Position]
  case class RotateRight(position: Position, degree: Degree) extends Instruction[Position]
  case class ShowPosition(position: Position) extends Instruction[Unit]

  sealed trait PencilInstruction[A]
  case class PencilUp(position: Position) extends PencilInstruction[Unit]
  case class PencilDown(position: Position) extends PencilInstruction[Unit]

  case class Position(x: Double = 0, y: Double = 0, heading: Degree = Degree())
  case class Degree(private val d: Int = 0) {val value = d % 360}

  object dsl {
    class Moves[F[_]](implicit I: Inject[Instruction, F]) {
      def forward(pos: Position, l: Int): Free[F, Position] = Free.inject[Instruction, F](Forward(pos, l))
      def backward(pos: Position, l: Int): Free[F, Position] = Free.inject[Instruction, F](Backward(pos, l))
      def left(pos: Position, degree: Degree): Free[F, Position] = Free.inject[Instruction, F](RotateLeft(pos, degree))
      def right(pos: Position, degree: Degree): Free[F, Position] = Free.inject[Instruction, F](RotateRight(pos, degree))
      def showPosition(pos: Position): Free[F, Unit] = Free.inject[Instruction, F](ShowPosition(pos))
    }

    object Moves {
      implicit def moves[F[_]](implicit I: Inject[Instruction, F]): Moves[F] = new Moves[F]
    }

    class PencilActions[F[_]](implicit I: Inject[PencilInstruction, F]) {
      def pencilUp(pos: Position): Free[F, Unit] = Free.inject[PencilInstruction, F](PencilUp(pos))
      def pencilDown(pos: Position): Free[F, Unit] = Free.inject[PencilInstruction, F](PencilDown(pos))
    }

    object PencilActions {
      implicit def pencilActions[F[_]](implicit I: Inject[PencilInstruction, F]): PencilActions[F] = new PencilActions[F]
    }
  }

  type LogoApp[A] = Coproduct[Instruction, PencilInstruction, A]

  def program(implicit M: Moves[LogoApp], P: PencilActions[LogoApp]): (Position => Free[LogoApp, Unit]) = {
    import M._, P._
    s: Position =>
      for {
        p1 <- forward(s, 10)
        p2 <- right(p1, Degree(90))
        _ <- pencilUp(p2)
        p3 <- forward(p2, 10)
        _ <- pencilDown(p3)
        p4 <- backward(p3, 20)
        _ <- showPosition(p4)
      } yield ()
  }

  object InterpreterId extends (Instruction ~> Id) {
    //import Computations._
    override def apply[A](fa: Instruction[A]): Id[A] = fa match {
      case Forward(p, length) => forward(p, length)
      case Backward(p, length) => backward(p, length)
      case RotateLeft(p, degree) => left(p, degree)
      case RotateRight(p, degree) => right(p, degree)
      case ShowPosition(p) => println(s"showing position $p")
    }
  }

  object PenInterpreterId extends (PencilInstruction ~> Id) {
    def apply[A](fa: PencilInstruction[A]): Id[A] = fa match {
      case PencilUp(p) => println(s"stop drawing at position $p")
      case PencilDown(p) => println(s"start drawing at position $p")
    }
  }

  val interpreter: LogoApp ~> Id = InterpreterId or PenInterpreterId
  implicit val startPosition = Position(0.0, 0.0, Degree(0))
  implicit val penAction = PencilUp(startPosition)
  implicit val move = Forward(startPosition, 1)

  val mm = program*/
}
