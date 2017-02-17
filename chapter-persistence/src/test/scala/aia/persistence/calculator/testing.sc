import cats.Functor

object testing {

  import cats._
  import cats.implicits._

  Semigroup[Int].combine(1, 2)
  Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
  Semigroup[Option[Int]].combine(Option(1), Option(2))
  Semigroup[Option[Int]].combine(Option(1), None)
  Semigroup[Int ⇒ Int].combine({ (x: Int) ⇒ x * 10 }, { (x: Int) ⇒ x + 1 }).apply(6)

  Map("foo" -> Map("bar" -> 5)).combine(Map("foo" -> Map("bar" -> 6), "baz" -> Map()))
  Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3, 4), "bar" -> List(42)))

  Map("foo" -> Map("bar" -> 5)) ++ Map("foo" -> Map("bar" -> 6), "baz" -> Map())
  Map("foo" -> List(1, 2)) ++ Map("foo" -> List(3, 4), "bar" -> List(42))


  val one = Option(1)
  val two = Option(2)
  val n = None

  one |+| two

  Monoid[String].combineAll(List("a", "b", "c"))
  Monoid[Map[String, Int]].combineAll(List(Map("a" → 1, "b" → 2), Map("a" → 3)))
  Monoid[Map[String, Int]].combineAll(List())


  val l = List(1, 2, 3, 4, 5)
  val k = l.foldMap(identity)
  val v = l.foldMap(i ⇒ i.toString)

  implicit def monoidTuple[A: Monoid, B: Monoid]: Monoid[(A, B)] =
    new Monoid[(A, B)] {
      def combine(x: (A, B), y: (A, B)): (A, B) = {
        val (xa, xb) = x
        val (ya, yb) = y
        (Monoid[A].combine(xa, ya), Monoid[B].combine(xb, yb))
      }

      def empty: (A, B) = (Monoid[A].empty, Monoid[B].empty)
    }


  val ex = Functor[List].map(List("qwer", "adsfg"))(_.length)

  val lenOption: Option[String] => Option[Int] = Functor[Option].lift(_.length)
  val source = List("Cats", "is", "awesome")
  val product = Functor[List].fproduct(source)(_.length)

  val kkk = Functor[List]
  val mmm = Functor[Option]

  kkk.map(List(1, 2, 3))(_ + 1)
  mmm.map(Some(1))(_ + 1)

  val fff = kkk compose mmm

  fff.map(List(Some(1), None, Some(3)))(_ + 1)

  val intToDouble: Int => Double = _.toDouble
  val intToString: Int => String = _.toString
  val doubleToString: Double => String = _.toString

  val ppk = (y: Double) => y.toString
  val kpp = (x: Int) => (ppk(_))

  val ppm = kpp(1)


  val mix = Apply[List] compose Apply[Option]

  //val f :Function1[Int, Function1[Double, String]] = Int => Double => String

  //  f(1)

  //val ffa:Function1[Int, String]= intToDouble.map(doubleToString)

  mix.ap(List(Some(intToDouble), Some(intToString)))(List(Some(1), Some(2), Some(3)))

  Monad[List].flatMap(List(1, 2, 3))(x ⇒ List(x, x + 1))

  import cats.data.State

  case class Integer(va:Int)
  def myState:State[String,Integer] = State{ x =>
    if (x == "abc") ("Anand", Integer(100))
    else ("Arun", Integer(100))
  }

  

  myState.run("Arun").value

  myState.map(x =>)

}