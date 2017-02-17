import scala.collection.immutable.ListMap
import scala.collection.mutable

val p = ListMap(1 -> 2) + (3 -> 4)

val q = p + (6 -> 9)

q + (1 -> 10)

val abc = mutable.LinkedHashMap(1 -> 2) + (3 -> 4)
abc += (8->12)

abc += (1 ->5)
