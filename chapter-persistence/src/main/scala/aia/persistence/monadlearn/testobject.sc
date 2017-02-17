case class A(name:String,first:String)

val p = A("a","b")

A.unapply(p) match {
  case Some(("t","v")) => println("one")
  case Some(("a","b")) => println("two")
  case None => println("testomg")
  case Some(_) => println("testomg")
}