import ast.ASTPrinter;
import ast.Program;
import lexer.Scanner;
import lexer.Token;
import lexer.Tokeniser;
import parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author cdubach
 */
public class Main {

    private enum Mode {
        LEXER, PARSER
    }

    public static void usage() {
        System.out.println("Usage: java "+Main.class.getSimpleName()+" pass inputfile outputfile");
        System.out.println("where pass is either: -lexer or -parser");
        System.exit(-1);
    }

    public static void main(String[] args) {

        if (args.length != 3)
            usage();

        Mode mode = null;
        switch (args[0]) {
            case "-lexer":
                mode = Mode.LEXER;
                break;
            case "-parser":
                mode = Mode.PARSER;
                break;
            default:
                usage();
                break;
        }

        File inputFile = new File(args[1]);
        File outputFile = new File(args[2]);

        Scanner scanner;
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("File "+inputFile.toString()+" does not exist.");
            System.exit(-1);
            return;
        }

        Tokeniser tokeniser = new Tokeniser(scanner);
        if (mode == Mode.LEXER) {
            for (Token t = tokeniser.nextToken(); t.tokenClass != Token.TokenClass.EOF; t = tokeniser.nextToken()) 
		System.out.println(t);
	    if (tokeniser.getErrorCount() == 0)
		System.out.println("Lexing: pass");
	    else
		System.out.println("Lexing: failed ("+tokeniser.getErrorCount()+" errors)");	    
	    System.exit(tokeniser.getErrorCount() == 0 ? 0 : -1);
        }	

        Parser parser = new Parser(tokeniser);
        Program programAst = parser.parse();
        if (parser.getErrorCount() == 0) {
            System.out.println("Parsing: pass");
            System.out.println("Writing AST into output file");
            programAst.accept(new ASTPrinter(outputFile));
        }
        else
            System.out.println("Parsing: failed ("+parser.getErrorCount()+" errors)");

	    System.exit(parser.getErrorCount() == 0 ? 0 : -1);
    }

}
