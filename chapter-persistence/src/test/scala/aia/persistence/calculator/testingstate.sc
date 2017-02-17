import cats.data._

object Example{



  def newstate:State[String, (String, Int)] = State {
    s =>
      if (s == "Anand") {println("From New State 1");("Arun",("a", 1))}
      else {println("From New State 1");("Meera",("b",2))}
  }



  type MyState[T] = State[String, T]


  def myCounter:MyState[Option[Int]]={
    State{ s =>
      if (s == "Anand") {println("$$1");("Arun",Some(10))}
      else {println("$$2");("Meera",None)}
    }
  }

  def continue (opt:Option[Int]):Boolean={
    opt match{
      case Some(x) => {println("&&1");true}
      case None => {println("&&2");false}
    }
  }

  def doubleIt(v:Boolean): Double ={
    v match {
      case true => {println("££1");100.0}
      case false => {println("££2");1000.0}
    }
  }

  def quadrupleIt(v:Boolean): Int ={
    v match {
      case true => 5000
      case false => 10000
    }
  }


  myCounter.map(x => {println("Here first");continue(x)}).map(y => {println("Here Second");doubleIt(y)}).get.run("Anand").value




  val m = myCounter map continue

  val dbl = m map doubleIt

  dbl.map(x => x * 4 ).run("Anand").value

  val q = myCounter.flatMap(x =>{
    x match {
      case Some(y) => State{s => (s,y)}
      case None => m.map(quadrupleIt)
    }
  })

  //m.run("Anand").value

  //myCounter.run(("Anand")).map(x =>  continue(x._2)).value

  //dbl.run("Anand").value

  //myCounter.run("Anand").value

  //myCounter.flatMap(x => {println ("|Here I am - "+x+"|"); State{s => {println("|Here s -| "+ s);(s,None)}}}).run("Anand").value

  //newstate.modify(x => {println ("Before Modify - " +x);"Lekha"}).get.run("Anand").value

  //State.get[String].flatMap(x => {println ("First - "+x); State{s => {println("Second - "+s); (s,x)}}}).run("Anand").value

  newstate.flatMap(x => {println ("First - "+x); State{s => {println("Second - "+s); (s,x)}}}).run("Anand").value
  //newstate.modify(x => "Lekha").run("Anand").value
}