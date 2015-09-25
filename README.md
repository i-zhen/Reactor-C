# Deadlines #

1. Part 1 (parser),  Thursday 15 October 2015  at 4pm, weight = 10%
2. Part 2 (ast builder),  Thursday 29 October 2015  at 4pm, weight = 20%
3. Part 3 (semantic analyser),  Thursday 12 November 2015 at 4pm, weight = 20%
4. Part 4 (code generator) , Thursday 3  December 2015 at 4pm, weight = 50% 

# Marking #

The marking will be done using an automated test suite.
Please note that you are not allowed to modify the `Main.java` file which is the main entry point to the compiler.
A checksum on the file will be performed to ensure the file has not be tempered with.
Furthermore, you may not use any external libraries.

# Setup #

## Bitbucket ##
We will rely on bitbucket and it is mandatory to use it for this coursework.
Bitbucket is an online repository that can be used with the git control revision system.

Your first task should be to setup a bitbucket account using your university email address.
You should then send to [daniel.hillerstrom@ed.ac.uk](daniel.hillerstrom@ed.ac.uk) your bitbucket account id so that we can run the automated test suite on your repository.
Details on how to fork the CT-15-16 repository are given below.
Important: do not share your code and repository with anyone and keep your source code secret.
If we identify that two students have identical portion of code, both will be considered to have cheated.

#include "PRELIMINARIES.md"

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

## 1. Grammar
You first job will consists in taking the grammar expressed in EBNF form and transform it into an equivalent context-free LL(k) grammar.
You should make sure that the resulting grammar is non-ambiguous, eliminate left recursion and ensure that the usual precedence rules for arithmetic expression are respected (\*,/,% have precedence over +,-).
For instance, the expression 2\*3+2 should be parsed as (2\*3)+2.

## 2. Lexing
The file `Lexer.java` contains a partial implementation of a lexer. Your job is to complete the implementation.
In particular, you have to complete the implementation of the method `next` in the `Lexer`-class. It is strongly recommended that you fill in the missing details, rather than rolling our own `Lexer` from scratch. Furthermore, do not remove the existing public methods, e.g. `getErrorCount` and `nextToken`. The tokens that your lexer needs to recognise are given in the file `Token.java`. Note that you are **not** allowed to use [the Java regular expression matcher](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Matcher.html) in your implementation.

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
