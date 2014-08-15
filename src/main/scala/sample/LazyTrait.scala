package sample
object LazyTrait extends App {

  trait Worker[T] {
    val name: String
    println(s"$name I'm initialized!") // define working code in trait
    def work(aWork: T): Unit
  }

  // value class - no new instance generated
  case class CoolString(param: String) extends AnyVal
  implicit def lift2Cool(src: String) = CoolString(src) // "new" isn't necessary here

  lazy val lazyWorker = new {val name = "Laaazy"} with Worker[CoolString] {
    override def work(stringWork: CoolString) {
      println(s"Omg I've been called! ${stringWork.param}") // method call in braces
    }
  }

  val eagerWorker = new {val name = "Eager!"} with Worker[String] {
    override def work(stringWork: String) {
      println(s"Omg I've been called! $stringWork")
    }
  }
  eagerWorker.work("Work'o'holic")
  lazyWorker.work(CoolString("Laaaazy")) // simple constructor calls via .apply()
  lazyWorker.work("Implicit laaaazy") // implicit conversion in place
}
