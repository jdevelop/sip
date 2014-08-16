package sample
object Typeclass extends App{
  trait Serialize[U] {
    def load(src: String): U
    def save(u: U) : String
  }

  object Printer {
    def print[U : Serialize](u : U) {
      println(implicitly[Serialize[U]].save(u))
    }
  }

  case class User(name: String)

  implicit object UserSerializer extends Serialize[User] {
    override def load(src: String): User = User(src)
    override def save(u: User): String = s"User name is ${u.name}"
  }

  Printer.print(User("John Doe"))
}