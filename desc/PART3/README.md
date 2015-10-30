# Part III : Semantic Analyser

The goal of part III is to build the semantic analyser. This consists of two phases: name analysis and type analysis. The description of the type analysis phase will be explained after next Tuesday class.

As usual you will first have to pull from the master branch the changes which include some skeleton implementation for this part.

## Name Analysis

The goal of the name analysis is to ensure that the scoping and visibility rules of the language are respected. This means for instance ensuring identifiers are only declared once or that any use of an identifier is preceded by a declaration in the current or enclosing scope.

Please note that an identifier can either be a variable or a function.

### Global and local scopes

As seen during the lectures, our language only have two scopes: global and local.

The global scope corresponds to the global variables declared outside any procedure and for the procedure declarations. Identifiers declared in the global scope can be accessed anywhere in the program.

The block scope (or local scope) is a set of statements enclosed within left and right braces ({ and } respectively). Blocks may be nested (a block may contain other blocks inside it). A variable declared in a block is accessible in the block and all inner blocks of that block, but not accessible outside the block. Procedure parameter identifiers have block scope, as if they had been declared inside the block forming the body of the procedure.

In both cases (global or local), it is illegal to declare twice the same identifiers in the same current block (note that this means it is illegal to declare a variable with the same name as a procedure at the global level).


### Shadowing

Shadowing occurs when an identifier declared within a given scope has the same name as an identifier declared in an outer scope. The outer identifier is said to be shadowed and any use of the identifier will refer to the one from the inner scope.

### Task

Your task is to implement a visitor that traverses the AST and identifies when the above rules are violated. In addition, you should add, for the two function call AST nodes and for the variable AST node, a field referencing the declaration (either a Procedure or VarDecl). This field should be updated to point to the actual declaration of the identifier when traversing the AST with the name analysis visitor. This will establish the link between the use of a variable or function and its declaration.


## Type Analysis

To appear soon...

## New Files

A new package has been added under `src/sem/`. This package contains template classes to get you started on implementing the semantic analysis.

 * The `sem.SemanticAnalyzer` is the only class which `Main.java` directly interfaces with. Inside this class you should run all your semantic visitors.
 * The `sem.NameAnalysisVisitor` is a template for the name analysis.
 * The `sem.TypeCheckVisitor` is a template for typechecker.
 * The `sem.Symbol` is an abstract parent class for other concrete symbols (e.g. variables and procedures).
 * The `sem.Scope` is a partial implementation of the `Scope`-class discussed in the lectures.

## Updated Files

The `Main.java` has been updated to provide a new commandline pass `-sem` which runs your semantic analyzer on the input.
