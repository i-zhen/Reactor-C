# Setup #

## 1.Bitbucket ##
We will rely on bitbucket and it is mandatory to use it for this coursework.
Bitbucket is an online repository that can be used with the git or mercurial control revision system.

Your first task should be to setup a bitbucket account using your university email address.
You should then send me [christophe.dubach@ed.ac.uk](christophe.dubach@ed.ac.uk) your email address so that I can give you access to the SCC repository.
Once this is done you should fork SCC repository so that you can get your own private copy (click in the top left corner on the three dots and select fork).
Once you have forked SCC make sure to give you repository private.
Do not share your code and repository with anyone and keep your code secret.
If we identify that two students have identical portion of code, both will be considered to have cheated.

## Development environment (editor)
You will need to choose a development environment for your project. DICE machines have the following integrated development environments (IDE) for Java installed:

* Community edition of [IntelliJ](https://www.jetbrains.com/idea/).
* [Eclipse](https://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr) for Java.

Alternatively, you can use Emacs, vim, or your favourite text editor. Choose whichever you are confident with.

## Obtaining your own copy of the SCC repository 
We are going to be using the Git revision control system during the course. Git is installed on DICE machines. If you use your own machine then make sure to install Git.

You will need to have your own copy of the SCC repository. In order to fork this repository hover the cursor over the "three dots"-icon to the left, and then click "Fork" as shown in the figure below:

![Forking the SCC repository](/figures/howtofork.png "Forking this repository.")

Thereafter you will see a form similar to the below figure:

![Forking the SCC repository](/figures/forkpermissions.png "Remember to tick \"Inherit repository user/group permissions\".")

Here you can name your repository and give it an optional description. It is **important** that you tick "*Inherit repository user/group permissions*" such that the teaching staff has access to your repository. Finally, click "Fork repository" to finish. Next, you will have to clone the forked repository to your local machine. In order to clone the repository you should launch a terminal and type:
```
$ git clone https://YOUR-USERNAME@bitbucket.org/YOUR-USERNAME/YOUR-REPOSITORY-NAME
```
where `YOUR-USERNAME` must be *your* Bitbucket account name, and `YOUR-REPOSITORY-NAME` must be the name you chose for your fork of the SCC repository.

## Building the SCC project
In order to build the project you must have Ant installed. On DICE machines Ant is already installed.
Your local copy of the SCC repository contains an Ant build file (`build.xml`). If you are using an IDE, then you can import the build file. Otherwise, you can build the project from the commandline by typing:
```
$ ant build
```
This command outputs your compiler in a directory called `bin` within the project structure. Thereafter, you can run your compiler from the commandline by typing:
```
$ java -cp bin Main
```
The parameter `cp` instructs the Java Runtime to include the local directory `bin` when it looks for class files.

You can clean the `bin` directory by typing:
```
$ ant clean
```
This command effectively deletes the `bin` directory.

# Marking #
The marking will be done by an automated test suite.

# Part I : Parsing #
The goal of part I is to write a lexical and syntactic analyser - a parser - for a subset of C.
As you have learnt in the course, parsing consists of three parts:

1. Scanner: the job of the scanner is to read the input file one character at a time.
2. Lexer: the lexer transforms the stream of characters into a stream of tokens. These tokens represents the lexem (i.e. a word in natural languages)
3. Parser: the parser finally consumes the tokens and determine if the input conforms to the rule of the grammar.

The scanner has already been implemented for you and we provide some partial implementations of the lexer and parser.
You will have to implement the rest.
We strongly encourage you to write a recursive descent parser and as such make your grammar LL(k).
We have provided utility function in the parser class to allow look ahead.

## 1.Grammar ##
You first job will consists in taking the grammar expressed in EBNF form and transform it into an equivalent context-free LL(k) grammar.
You should make sure that the resulting grammar is non-ambiguous, eliminate left recursion and ensure that the usual precedence rules for arithmetic expression are respected (\*,/,% have precedence over +,-).
For instance, the expression 2\*3+2 should be parsed as (2\*3)+2.

## 2. Semantic analysis
Your parser checks whether a given source program has proper *syntax*, however, we also want to check whether the said program has proper *semantics*. In other words, we want to check whether a given program is meaningful. This includes populating a symbol table, type checking, etc. However, before you can do semantic analysis you will need an intermediate representation (IR) for source programs.

A commonly used IR is Abstract Syntax Tree (AST) which closely resembles the structure of a source program. An AST captures the important details of a source program and omit details such as parentheses. So, for every source-language construct, the compiler needs a designated AST node, e.g. you might have a `BinaryOp`-node, which represents a binary operation (+,-,\*,% or /), with two children that represent the left and right subexpression. Furthermore, to represent a while-loop you might have a `WhileStmt`-node with a child to represent the loop condition and another to represent the loop body (i.e. a list of statements). The AST nodes can be implemented as specialised classes in a class hierarchy in Java.

Once you have implemented the class hierarchy you are ready to perform semantic analysis. ASTs afford an uniform method for analysing source programs as an analysis boils down to performing a particular tree traversal. It is highly recommended that you use the *visitor pattern* to implement your analyses. When using the visitor pattern you will implement type checking, symbol table construction, etc. as distinct visitors.

## Files ##
* grammar/ebnf.txt : This file describes the grammar of our language in EBNF format.
* Scanner : This class implements the scanner which returns character strings.
* Token : This class represent the different tokens from the language.
* Tokeniser: This class converts character strings into tokens.