package sample

object HighOrder extends App {

  case class User(private val name: String) {
    def print(f: (String) ⇒ Unit) {
      f(name)
    }
  }

  val u = User("Jane Doe")
  u.print(println)
  u.print {
    case x ⇒ println(x.toUpperCase)
  }

}
