package aia.persistence.FreeTask

import cats.data.Coproduct
import cats.free.{Free, Inject}

import scala.language.higherKinds
import scala.language.implicitConversions
import cats.~>

object FreeUtils {

  object InterpreterImplicits {

    /*implicit def coproductInterpreter[F[_], G[_], M[_]](implicit fm: F ~> M, gm: G ~> M): Coproduct[F, G, ?] ~> M = new (Coproduct[F, G, ?] ~> M) {
      override def apply[A](ca: Coproduct[F, G, A]): M[A] = ca.run match {
        case Left(fa) => fm(fa)
        case Right(ga) => gm(ga)
      }
    }

    implicit class NaturalTransformationOrOps[F[_], H[_]](private val nt: F ~> H) extends AnyVal {
      type Copro[F[_], G[_]] = { type f[x] = Coproduct[F, G, x] } // just for better readability

      // given F ~> H and G ~> H we derive Copro[F, G]#f ~> H
      def or[G[_]](f: G ~> H): Copro[F, G]#f ~> H =
        new (Copro[F, G]#f ~> H) {
          def apply[A](c: Coproduct[F, G, A]): H[A] = c.run match {
            case Left(fa) => nt(fa)
            case Right(ga) => f(ga)
          }
        }
    }

  }

  object LiftImplicit {
    implicit def lift[F[_], G[_], A](fa: F[A])(implicit I: Inject[F, G]): Free[G, A] = Free liftF I.inj(fa)
  }*/

  }

}