# Deadlines #

1. Part 1 (parser),  Thursday 15 October 2015  at 4pm, weight = 10%
2. Part 2 (ast builder),  Thursday 29 October 2015  at 4pm, weight = 20%
3. Part 3 (semantic analyser),  Thursday 12 November 2015 at 4pm, weight = 20%
4. Part 4 (code generator) , Thursday 3  December 2015 at 4pm, weight = 50% 

# Marking #

The marking will be done using an automated test suite.
Please note that you are not allowed to modify the Main.java file which is the main entry point to the compiler.
A checksum on the file will be performed to ensure the file has not be tempered with.


# Setup #

## Bitbucket ##
We will rely on bitbucket and it is mandatory to use it for this coursework.
Bitbucket is an online repository that can be used with the git control revision system.

Your first task should be to setup a bitbucket account using your university email address.
You should then send to [daniel.hillerstrom@ed.ac.uk](daniel.hillerstrom@ed.ac.uk) your bitbucket account id so that we can run the automated test suite on your repository.
Details on how to fork the CT-15-16 repository are given below.
Important: do not share your code and repository with anyone and keep your source code secret.
If we identify that two students have identical portion of code, both will be considered to have cheated.

## Development environment (editor)
You can choose to use a development environment for your project. DICE machines have the following integrated development environments (IDE) for Java installed:

* Community edition of [IntelliJ](https://www.jetbrains.com/idea/).
* [Eclipse](https://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr) for Java.

Alternatively, you can use Emacs, vim, or your favourite text editor. Choose whichever you are confident with.

## Obtaining your own copy of the CT-15-16 repository 
We are going to be using the Git revision control system during the course. Git is installed on DICE machines. If you use your own machine then make sure to install Git.

You will need to have your own copy of the CT-15-16 repository. In order to fork this repository hover the cursor over the "three dots"-icon to the left, and then click "Fork" as shown in the figure below:

![Forking the CT-15-16 repository](/figures/howtofork.png "Forking this repository.")

Thereafter you will see a form similar to the below figure:

![Forking the CT-15-16 repository](/figures/forking.png "Forking this repository.")

Here you can name your repository and give it an optional description. Finally, click "Fork repository" to finish. After forking you should grant the teaching staff read access to your repository. Click on Settings (the gear icon), and then go to "Access management", the window should look similar to the figure below:

![Granting the teaching staff read access](/figures/repopermissions.png "Granting the teaching staff read access.")

You should grant the following users read access:

* Christophe Dubach (username: cdubach)
* Daniel Hillerström (username: dhil)

Next, you will have to clone the forked repository to your local machine. In order to clone the repository you should launch a terminal and type:
```
$ git clone https://YOUR-USERNAME@bitbucket.org/YOUR-USERNAME/YOUR-REPOSITORY-NAME
```
where `YOUR-USERNAME` must be *your* Bitbucket account name, and `YOUR-REPOSITORY-NAME` must be the name you chose for your fork of the CT-15-16 repository.

## Building the CT-15-16 project
In order to build the project you must have Ant installed. On DICE machines Ant is already installed.
Your local copy of the CT-15-16 repository contains an Ant build file (`build.xml`). If you are using an IDE, then you can import the build file. Otherwise, you can build the project from the commandline by typing:
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


# Part I : Parsing
The goal of part I is to write a lexical and syntactic analyser - a parser - for a subset of C.
As you have learnt in the course, parsing consists of three parts:

1. Scanner: the job of the scanner is to read the input file one character at a time.
2. Lexer: the lexer transforms the stream of characters into a stream of tokens. These tokens represents the lexem (i.e. a word in natural languages)
3. Parser: the parser finally consumes the tokens and determine if the input conforms to the rule of the grammar.

The scanner has already been implemented for you and we provide some partial implementations of the lexer and parser.
You will have to implement the rest.
We strongly encourage you to write a recursive descent parser and as such make your grammar LL(k).
We have provided utility function in the parser class to allow look ahead.
Furthermore, **do not** alter the `Main.java` file.

## 1. Grammar
You first job will consists in taking the grammar expressed in EBNF form and transform it into an equivalent context-free LL(k) grammar.
You should make sure that the resulting grammar is non-ambiguous, eliminate left recursion and ensure that the usual precedence rules for arithmetic expression are respected (\*,/,% have precedence over +,-).
For instance, the expression 2\*3+2 should be parsed as (2\*3)+2.

## 2. Lexing
The file `Lexer.java` contains a partial implementation of a lexer. Your job is to complete the implementation.
In particular, you have to complete the implementation of the method `next` in the `Lexer`-class. It is strongly recommended that you fill in the missing details, rather than rolling our own `Lexer` from scratch. Furthermore, do not remove the existing public methods, e.g. `getErrorCount` and `nextToken`. The tokens that your lexer needs to recognise are given in the file `Token.java`.

A hint: It is recommended to use the [Character-class methods](https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html) to test whether a character is a digit, whitespace, etc.

## 3. Parser
After having transformed the grammar into a LL(k)-grammar and implemented the lexer you will have to implement a parser. The parser determines whether a given source program is syntactically correct or incorrect. A partial implementation of a recursive-decent parser has already been provided. The provided `Parser`-class has the following interface:

* `int getErrorCount()` returns the number of parsing errors.
* `void parse()` initiates the parsing of a given source program.

In addition, the `Parser`-class contains various private methods, of which some are ultility methods, e.g.

* `void error(TokenClass... expected)` takes a variable number of expected tokens, and emits an error accordingly.
* `Token lookAhead(int i)` returns the `i`'th token in the token-stream.
* `void nextToken()` advances the token-stream by one, i.e. it consumes one token from the stream.
* `Token expect(TokenClass... expected)` takes a variable number of expected tokens, and consumes them from the token-stream if present, otherwise it generates an error using the `error`-method.
* `Token accept(TokenClass... expected)` tests whether the next token(s) are identical to the `expected`. However, it *does not* consume any tokens from the token-stream.
* `void parseProgram()` parses a "Program-production" from the LL(k) grammar. Similarly, `void parseIncludes()` parses an "Includes-production". Three additional empty methods have been provided: `parseDecls`, `parseProcs` and `parseMain` are to be completed by you. Furthermore, you will need to add more parse methods yourself. For each nonterminal you should have a corresponding parse method.

Your parser *should* only determine whether a given source program is syntactically correct. The `Main`-class relies on the error count provided by the `Parser`-class. Therefore, make sure you use the `error`-method in the `Parser`-class to report errors correctly!

## Files
* grammar/ebnf.txt : This file describes the grammar of our language in EBNF format.
* Scanner : This class implements the scanner which returns character strings.
* Token : This class represent the different tokens from the language.
* Tokeniser: This class converts character strings into tokens.
