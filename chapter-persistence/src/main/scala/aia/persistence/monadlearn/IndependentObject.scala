package aia.persistence.monadlearn

import cats.data.State
import cats.free.Free
import cats.~>

object IndependentObject {
  def foldAndRead[T[?],V](state:V,programLogic :() =>Free[T[?], Int])(implicit interpreter: (T ~> State[V, ?]))=
  {
    //val mm:cats.free.Free[[α$0$]T[α$0$],Int] = programLogic()
    val mk = programLogic
    val mm:Free[T[?],Int] = mk()
    val kk = mm.foldMap[State[V, ?]](interpreter)
    val pp = kk.run(state).value
    pp
  }
}
