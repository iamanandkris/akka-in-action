case class Better(total: List[Int])

object BetterObject {

  def sum(i: Int)(b: Better): (Better, Int) = {
    val result = if (b.total.length > 1) 99 else b.total.sum
    val newBetter = b.copy(total = i :: b.total)
    (newBetter, result)
  }

}

val ffff:Better => (Better,Int) = BetterObject.sum(10)

type BetterUpdate = Better => (Better,Int)

val firstF:BetterUpdate = BetterObject.sum(1)
val secondF:BetterUpdate = BetterObject.sum(2)
val thirdF:BetterUpdate = BetterObject.sum(3)


val composeSumResults:BetterUpdate =  better =>  {

  val (firstState,firstResult) = firstF(better)
  println(firstState+" "+firstResult)
  val (secondState, secondResult) = secondF(firstState)
  println(secondState+" "+secondResult)
  val (thirdStater, thirdResult) = thirdF(secondState)    // now when this blows up, we have secondState in scope for inspection
  println(thirdStater+" "+thirdResult)

  (thirdStater, firstResult + secondResult + thirdResult)
}

composeSumResults(Better(List()))


case class StateChange[S,A](run: S => (S,A)){

  def mapResult[B](f:A => B):StateChange[S,B] = StateChange{ (s:S) =>
    println("Here comes the argument - " +s)
    val(s2,a) = run(s)
    (s2,f(a))
  }

  /*
   A magical moment!!!!!!  Now I can stick 2 StateChange's together: the resulting
   StateChange runs the first one, then uses the resulting state a the param for
   the second one: I have now threaded my state through 2 computations.
   */

  def doWithNewState[B](f: A => StateChange[S,B]):StateChange[S,B] = StateChange{ (s:S) =>
    val(s2,a) = run(s)
    val stateChangeToB = f(a)
    val (s3,b) = stateChangeToB.run(s2)
    (s3,b)
  }
}

val v = (x:Better) => (x, 101)

val ffp = StateChange[Better,Int](v).doWithNewState(x => )

val mmpg = StateChange[Better,Int](v).mapResult { x => println("this is x " +x );x.toString() }.run(Better(List()))