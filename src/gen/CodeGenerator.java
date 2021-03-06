package gen;

import ast.Program;

public class CodeGenerator {

    public void emitProgram(Program program) {
        GenerateVisitor vistor = new GenerateVisitor();
        program.accept(vistor);
        if (vistor.getErrorCount() > 0) {
            System.out.println("Cannot generate code, error found: " + vistor.getErrorCount());
        } else {
            System.out.println("Compilation completed successfully");
        }
        // TODO: emit a java class named Main that contains your program and write it to the file out/Main.class
        // to do this you will need to write a visitor which traverses the AST and emit the different global variables as static fields and the different procedures as static methods.
    }
}