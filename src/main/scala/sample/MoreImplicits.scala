package sample
object MoreImplicits extends App {
  trait mayAdd[T] {
    def +!(src: T): T
  }
  case class myString(param: String) extends mayAdd[myString] {
    override def +!(src: myString) = myString(param + ":" + src.param)
  }
  case class myInt(param: Int) extends mayAdd[myInt] {
    override def +!(src: myInt) = myInt(param * src.param)
  }

  implicit def pimpString(str: String) = myString(str)
  implicit def pimpInt(num: Int) = myInt(num)

  println(1 +! 2 +! 3); println("aaa" +! "bbb")

  type strFunc = (String) => myString
  def doSomethingWith(str: String)(implicit f: strFunc) = f(str.reverse)

  {
    implicit def upperCase : strFunc = x => myString(x.toUpperCase)
    println(doSomethingWith("hello world").param)
  }
  {
    implicit def upperCase : strFunc = x => myString(x.capitalize)
    println(doSomethingWith("hello world").param)
  }

}