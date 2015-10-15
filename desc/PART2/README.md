# Part II : AST builder

The goal of part II is to modify your parser so that it can build the Abstract Syntax Tree (AST) corresponding to your input program.

In order to achieve this goal, you will have to perform three tasks.
First, you will have to follow the abstract grammar specification and design the Java classes that represent the AST as seen during the course.
Then, you should write an AST printer in order to output the AST into a file.
Finally, you will have to modify your parser so that it builds the AST as your are parsing the tokens.

Note that you may favour a more iterative approach where you add AST nodes one by one, extend the printer and modify your parser as you go.
We also encourage you to write small test programs that test every AST node as you are building them rather than trying to implement everything at once.
If you encounter any problem, have any questions or find a bug with the newly provided files, please post a note on Piazza.

## 0. Setup

TO BE COMPLETED

You should pull from the master branch and merge with your version.

## 1. AST Nodes

As seen in the course, the AST can be defined using an abstract grammar.
You can find the abstract grammar [here](../../grammar/abstract_grammar.txt).
It is important to ensure that the design of your classes follows the abstract grammar;
the automated marking system will rely exclusively on the name of the class to determine the type of AST node and will expect the subtrees to appear in the same order as defined in the grammar file.

Note that a few AST node classes are already given as a starting point.
You should not have to modify these (unless otherwise stated in the file).

## 2. AST Printer

Your next job will consists in extending the AST printer class provided to handle your newly added AST node classes.
As seen during the course, the AST printer uses the visitor design pattern.

It is important to respect the following format when printing the AST to ensure that your output can be validated by our automatic marking system.
Using EBNF syntax, the output should be of the form: `AST_NODE_CLASS_NAME '(' [SUB_TREE (',' SUB_TREE)*] ')'`

### Examples:

* `y = 3*x;` should result in the following output: `Assign(Var(y),BinOp(IntLiteral(3), MUL, Var(x)))`.
* `void foo() { return; }` should result in: `Procedure(VOID, foo, Block(Return()))`

Note that you are free to add white spaces in your output format; spaces, newlines and tabulations will be ignore by our comparison tool.


## 3. Parser modifications

Your final tasks consists in updating your parser so that it creates the AST nodes as it parses your input program.
For most of your parseXYZ methods, you will have to modify the return type to the type of the node the parsing method should produce as seen during the lecture and implement the functionality that builds the AST nodes.
You may have to modify slightly the design of your parser in order to accommodate the creation of the AST nodes.

## New Files
* grammar/abstract_grammar.txt : This file describes the abstract grammar that defines our AST.
* src/ast/ASTVisitor.java : This is the visitor interface for the AST.
* src/ast/ASTPrinter.java : This the AST printer built as a visitor.
* src/ast/\*.java : The remaining java files are some of the AST nodes.

## Updated Files
* src/parser/Parser.java : The parser file has been updated to build the AST.
* src/Main.java : The main file has been updated to print the AST.
