package sample;

public class JValVarExample {

  static int something;

  static int somethingInt;

  static int mutableSomething;

  public static void main(String[] args) throws Exception {
    something = 10;
    somethingInt = 20;
    System.out.println(
            String.format(
                    "Something is %1$d, another something is %2$d",
                    something,
                    somethingInt)
    );
    something = 20; // you can't have a true "val" here - the closest would be "final"
                    // however you can't initialize a "static final" in "main"

    mutableSomething = 10;
    System.out.println(String.format("Mutable? %1$d", mutableSomething));
    mutableSomething = 20;
    System.out.println(String.format("Mutable! %1$d", mutableSomething));

  }
}