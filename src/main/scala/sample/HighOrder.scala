package sample

object HighOrder extends App {

  case class User(private val name: String) {
    def print(f: (String) => Unit) {
      f(name)
    }
  }

  def putStr(s: String) {
    println(s)
  }

  val u = User("Jane Doe")
  u.print(putStr) // pass a functional reference
  // pass an anonymous function
  u.print {
    case x => println(x.toUpperCase)
  }
  // another way to create a simple anonimous function
  u.print(x => println(x.reverse))

}
