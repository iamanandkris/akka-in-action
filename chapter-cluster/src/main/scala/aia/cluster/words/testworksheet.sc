object foo {

  trait Bar[Q[_]]

  implicit object OptionBar extends Bar[Option]

  def test[T, C[_]](c: C[T])(implicit bar: Bar[C]) = ()

  def main(args: Array[String]) {
    test(Some(42))  //???
  }
}

val k = "how are you"