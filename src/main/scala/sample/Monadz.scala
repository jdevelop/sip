package sample
object Monadz extends App {

  //sealed trait Maybe[A] { - doesn't work - need a variance here
  sealed trait Maybe[+A] {
    def map[B](g: A => B): Maybe[B]
    def flatMap[B](g: A => Maybe[B]): Maybe[B]
  }

  case class Just[+A](a: A) extends Maybe[A] {
    override def map[B](g: A => B): Maybe[B] = Just(g(a))
    override def flatMap[B](g: A => Maybe[B]) = g(a)
  }

  case object None extends Maybe[Nothing] {
    override def map[B](g: Nothing => B): Maybe[B] = None
    override def flatMap[B](g: Nothing => Maybe[B]) = None
  }

  println(for (x <- Just("John"); y <- Just("Doe")) yield (x, y))
  println(for (x <- Just("John"); y <- None) yield (x, y))
  println(for (x <- None; y <- Just("Anything else")) yield (x, y))

}