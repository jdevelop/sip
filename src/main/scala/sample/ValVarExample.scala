package sample

object ValVarExample extends App {

  val something = 10
  val somethingInt: Int = 20

  println(s"Something is $something, another something is $somethingInt")

  // something = 20  - doesn't compile

  var mutableSomething = 10
  println(s"Mutable? $mutableSomething")
  mutableSomething = 20
  println(s"Mutable! $mutableSomething")

}