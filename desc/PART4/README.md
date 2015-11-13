# Part IV : Code Generation

The goal of part IV is to write the code generator.
In order to simplify the development of the code generator, we are going to use version 4.2 of the ASM Java ByteCode manipulation library.
We will restrict our-self to the use of the Core API and it is **forbidden** to use the Tree API (do not use any of the classes in the org.objectweb.asm.tree.* packages).

## 0. Setup and Learning

You first task will consist of reading the ASM [guide](./asm4-guide.pdf), in particular section 2 and 3.
Use the examples of section 2.2.3 and 3.2.2 to learn how to generate a class and a method using the Core API.


## 1. Generating the Main class

Your next task will consists of producing in your compiler the Main class using the ASM API containing an empty main function with the following signature: `static int main(String[])`.
It is important to ensure that the generated class file is named `Main.class` and located in the `out/` directory for our automated testing system to work correctly.
You should then check that you can correctly execute the class produced by running the `java Main` command in the `out/` directory


## 2. Handling global variable declarations

The next task will consists of writing a visitor which emits all the global variable in your program as static fields in the Main class.

## 3. Handling of procedure declarations

Next, we suggest that you extend your code generator visitor to handle procedure declarations but leave the body empty for now.
All procedures should be statically declared in the class file.

## 4. Generating function code

Finally, you should extend your visitor to handle the actual code found in each method and proceed to generate the corresponding Java ByteCode instructions.

## 5. Handling of built-in functions

To be completed...


## New Files

A new package has been added under `src/sem/`. This package contains template classes to get you started on implementing the semantic analysis.

 * The `sem.SemanticAnalyzer` is the only class which `Main.java` directly interfaces with. Inside this class you should run all your semantic visitors.

## Updated Files

* The `Main.java` has been updated to provide a new commandline pass `-gen` which runs your code generator.

