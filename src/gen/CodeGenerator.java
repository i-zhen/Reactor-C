package gen;

import ast.Program;
import jdk.internal.org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;


public class CodeGenerator {
    public void emitProgram(Program program) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        cw.visitEnd();

        // TODO: emit a java class named Main that contains your program and write it to the file out/Main.class
        // to do this you will need to write a visitor which traverses the AST and emit the different global variables as static fields and the different procedures as static methods.
        writeClass(cw);
    }

    public void writeClass(ClassWriter cw){
        //Output the final bytecode to the file
        byte [] b = cw.toByteArray();
        try (FileOutputStream outputFile = new FileOutputStream("out/Main.class")) {
            outputFile.write(b);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
