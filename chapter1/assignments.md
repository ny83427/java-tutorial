## Assignment of Chapter 1

### Required
* Download and install [jdk-8u192](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) x64,
after that execute `java -version` in terminal to verify your installation.
Copy the output results like below to a text file `java-version.txt` so that I know you really did it
```
Nathanael@NY83427 C:\Users\Nathanael
$ java -version
java version "1.8.0_192"
Java(TM) SE Runtime Environment (build 1.8.0_192-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.192-b12, mixed mode)
```

* Write your own `HelloWorld.java` which would print "Hello World" or anything you want

* Create a github account if you don't have, give me your link then

* Download and install these components for further study:  
  + git
  + [IDEA Community Edition](https://www.jetbrains.com/idea/download/#section=windows)
  + [jdk-11.0.1](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
  + Only for Windows Users: [everything](https://www.voidtools.com/), [notepad++](https://notepad-plus-plus.org/)
  + Download and checkout these [Java Demos and Samples](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Optional
* Write a script or find a solution to automate the installation process so that development environment
setup will be much easier. Provide me the link of your script or solution(gist is recommended)

* Install jdk8 and IDEA Community in a different operation system, write a `HelloWorld.java` in IDEA and run it.
Save a screen shot with filename `different-os.jpg` or `different-os.png`

### Challenge
* Create a Java source file `Fibonacci.java` and write a method `int calc(int number)` there(you may
change the return type if necessary) so that it would return the correct number in the sequence. For example:
```
calc(1) should return 1
calc(2) should return 1
calc(3) should return 2
...
calc(20) should return 6765
```
* You would probably find that the execution time increases when the given number increased, try `calc(100)` and how long it will
take your program to finish? Can you optimize so that we won't wait in pains?
* If you implement your solution with recursion, can you do without recursion?
* Can you handle the output correctly when the result might be too large, for example, how about `calc(10000)`?
* You submission should look like this:
```java
public class Fibonacci {
    private int calc(int n) {
        // You stuff here
        return n;
    }

    public static void main(String[] args) {
        Fibonacci fib = new Fibonacci();
        System.out.println(fib.calc(3));
        System.out.println(fib.calc(20));
        System.out.println(fib.calc(100));
    }
}
```
* Change the `main` method so that we can give your program an argument to run directly, like:
```cmd
java Fibonacci 20
java Fibonacci 100
```
* Or you can change the `main` method so that it will always be ready for argument input and we don't need to run it every time, like:
```cmd
java Fibonacci
10
You have input 10 and the fibonacci number is 55
20
You have input 20 and the fibonacci number is 6765
```
