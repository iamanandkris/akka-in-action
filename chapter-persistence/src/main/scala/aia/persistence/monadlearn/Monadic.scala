package aia.persistence.monadlearn

import cats.free.Free
import cats.free.Free.liftF
import cats.arrow.FunctionK
import cats.{Id, ~>}
import scala.collection.mutable
object Monadic extends App {
/*
  sealed trait KVStoreA[A]
  case class Put[T](key: String, value: T) extends KVStoreA[Unit]
  case class Get[T](key: String) extends KVStoreA[Option[T]]
  case class Delete(key: String) extends KVStoreA[Unit]

  type KVStore[A] = Free[KVStoreA, A]

  // Put returns nothing (i.e. Unit).
  def put[T](key: String, value: T): KVStore[Unit] =
  liftF[KVStoreA, Unit](Put[T](key, value))

  // Get returns a T value.
  def get[T](key: String): KVStore[Option[T]] =
  liftF[KVStoreA, Option[T]](Get[T](key))

  // Delete returns nothing (i.e. Unit).
  def delete(key: String): KVStore[Unit] =
  liftF(Delete(key))

  // Update composes get and set, and returns nothing.
  def update[T](key: String, f: T => T): KVStore[Unit] =
  for {
    vMaybe <- get[T](key)
    _ <- vMaybe.map(v => put[T](key, f(v))).getOrElse(Free.pure(()))
  } yield ()


  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", (_ + 12))
      _ <- put("tame-cats", 5)
      n <- get[Int]("wild-cats")
      //_ <- delete("tame-cats")
    } yield n


  def impureCompiler: KVStoreA ~> Id  =
    new (KVStoreA ~> Id) {

      // a very simple (and imprecise) key-value store
      val kvs = mutable.Map.empty[String, Any]

      def apply[A](fa: KVStoreA[A]): Id[A] =
        fa match {
          case Put(key, value) =>
            println(s"put($key, $value)")
            kvs(key) = value
            ().asInstanceOf[A]
          case Get(key) =>
            println(s"get($key)")
            //kvs.get(key).map(_.asInstanceOf[A])
            kvs.get(key).asInstanceOf[A]
          case Delete(key) =>
            println(s"delete($key)")
            kvs.remove(key)
            ().asInstanceOf[A]
        }
    }

  val result: Option[Int] = program.foldMap(impureCompiler)
  println(result)

  import cats.data.State
  type KVStoreState[A] = State[Map[String, Any], A]
  val pureCompiler: KVStoreA ~> Id = new (KVStoreA ~> Id) {
    def apply[A](fa: KVStoreA[A]): Id[KVStoreState[_]] =
      fa match {
        case Put(key, value) => State.modify[Map[String,Any]](s => s.updated(key, value))
        case Get(key) =>
          State.inspect[Map[String,Any],A](_.get(key).map(_.asInstanceOf[A]))
        case Delete(key) => State.modify[Map[String,Any]](_ - key)
      }
  }

  println("Pure one--")
  //val ff:Id[KVStoreState[A]]] = Map.empty
  val result1 = program.foldMap(pureCompiler).run(Map.empty).value
  println(result1)
*/
}
