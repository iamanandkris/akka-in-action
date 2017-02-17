package aia.persistence.monadlearn

import cats.Functor
import cats.kernel.{Monoid, Semigroup}
import cats.implicits._
object learncats extends App {
  println(Semigroup[Int].combine(1, 2))
  val v = Semigroup[Int ⇒ Int].combine({ (x: Int) ⇒ x + 1 }, { (x: Int) ⇒ x * 10 })
  println(v.apply(2))

  val aMap = Map("foo" → 5)
  val anotherMap = Map("foo" → 6)
  val combinedMap = Semigroup[Map[String, Int]].combine(aMap, anotherMap)

  println(combinedMap)

  val aMap1 = Map("foo" → Map("bar" → 5))
  val anotherMap1 = Map("fool" → Map("bar" → 6))
  val combinedMap1 = Semigroup[Map[String, Map[String, Int]]].combine(aMap1, anotherMap1)

  println(combinedMap1)

  val vr = Monoid[Map[String, Int]].combineAll(List(Map("a" → 1, "b" → 2), Map("a" → 3)))
  println(vr)

  val l = List(1, 2, 3, 4, 5)
  l.flatMap(x => List(x))
  println(l.foldMap(identity))
  println(l.foldMap(i ⇒ (i,Map("one" ->i.toString))))

  type  m[T] = Int => Map[String, T]

  implicit val testFunctor:Functor[m] = new Functor[m]{
    def map[A,B](fa:m[A])(f:A=>B) = fa.andThen(x => x.map(y => (y._1,f(y._2))))
  }

  val s:m[Int] = (x:Int) => Map(x.toString->x)

  val vvv = Functor[m].map(s)(x => x.toString)
  println(vvv.apply(1234))

  val mVal:m[String] => m[Int] = Functor[m].lift(x => x.charAt(1).hashCode())

  val stp = mVal.apply(vvv)
  println(stp.apply(6789))

  val source = List("Cats", "is", "awesome")
  val product = Functor[List].fproduct(source)(_.length)

  val listOpt = Functor[List] compose Functor[Option]
  val op = listOpt.map(List(Some(1), None, Some(3)))(_ + 1)
  println(op)

  /*implicit val strFunctor:Functor[String] = new Functor[String]{
    def map[A,B] (fa:String) (f:String => B) ={
      f(fa)
    }
  }*/

}
