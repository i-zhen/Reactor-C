package lexer;

import lexer.Token.TokenClass;

import java.io.EOFException;
import java.io.IOException;

/**
 * @author cdubach
 */
public class Tokeniser {

    private Scanner scanner;

    private int error = 0;
    public int getErrorCount() {
	return this.error;
    }

    public Tokeniser(Scanner scanner) {
        this.scanner = scanner;
    }

    private void error(char c, int line, int col) {
        System.out.println("Lexing error: unrecognised character ("+c+") at "+line+":"+col);
	    error++;
    }


    public Token nextToken() {
        Token result;
        try {
             result = next();
        } catch (EOFException eof) {
            // end of file, nothing to worry about, just return EOF token
            return new Token(TokenClass.EOF, scanner.getLine(), scanner.getColumn());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // something went horribly wrong, abort
            System.exit(-1);
            return null;
        }
        return result;
    }

    /*
     * To be completed
     */
    private Token next() throws IOException {

        int line = scanner.getLine();
        int column = scanner.getColumn();

	// get the next character
        char c = scanner.next();

        // skip white spaces
        if (Character.isWhitespace(c))
            return next();

        //include
        if (c == '#') {
            c = scanner.next();
            if (c != 'i') return new Token(TokenClass.INVALID, line, column);
            c = scanner.next();
            if (c != 'n') return new Token(TokenClass.INVALID, line, column);
            c = scanner.next();
            if (c != 'c') return new Token(TokenClass.INVALID, line, column);
            c = scanner.next();
            if (c != 'l') return new Token(TokenClass.INVALID, line, column);
            c = scanner.next();
            if (c != 'u') return new Token(TokenClass.INVALID, line, column);
            c = scanner.next();
            if (c != 'd') return new Token(TokenClass.INVALID, line, column);
            c = scanner.next();
            if (c != 'e') return new Token(TokenClass.INVALID, line, column);
            return new Token(TokenClass.INCLUDE, line, column);
        }

        // skip comments, or return div
        if (c == '/'){
            c = scanner.peek();
            if (c == '/') {
                scanner.next();
                c = scanner.next();
                while (!(c == '\n' || c =='\r')) {
                    c = scanner.next();
                }
                return next();
            }
            if (c == '*') {
                scanner.next();
                while (true){
                    c = scanner.next();
                    if (c == '*') {
                        c = scanner.next();
                        if(c == '/')
                            return next();
                    }
                }
            }
            return new Token(TokenClass.DIV, line, column);
        }

	    // recognises the operators
        if (c == '+')
	        return new Token(TokenClass.PLUS, line, column);
        if (c == '-')
            return new Token(TokenClass.MINUS, line, column);
        if (c == '*')
            return new Token(TokenClass.TIMES, line, column);
        if (c == '%')
            return new Token(TokenClass.MOD, line, column);
        if (c == '=') {
            c = scanner.peek();
            if (c == '=') {
                scanner.next();
                return new Token(TokenClass.EQ, line, column);
            }
            return new Token(TokenClass.ASSIGN, line, column);
        }
        if (c == '!'){
            c = scanner.next();
            if (c == '=')
                return new Token(TokenClass.NE, line, column);
            return new Token(TokenClass.INVALID, line, column);
        }

        if (c == '<'){
            c = scanner.peek();
            if (c == '='){
                scanner.next();
                return new Token(TokenClass.LE, line, column);
            }
            return new Token(TokenClass.LT, line, column);
        }

        if (c == '>'){
            c = scanner.peek();
            if (c == '=') {
                scanner.next();
                return new Token(TokenClass.GE, line, column);
            }
            return new Token(TokenClass.GT, line, column);
        }

        if (c == '(')
            return new Token(TokenClass.LPAR, line, column);
        if (c == ')')
            return new Token(TokenClass.RPAR, line, column);
        if (c == '{')
            return new Token(TokenClass.LBRA, line, column);
        if (c == '}')
            return new Token(TokenClass.RBRA, line, column);
        if (c == ';')
            return new Token(TokenClass.SEMICOLON, line, column);
        if (c == ',')
            return new Token(TokenClass.COMMA, line, column);


        //String
        if(c == '\"') {
            StringBuilder sb = new StringBuilder();
            c = scanner.next();
            while (c != '\"') {
                if (c != '\\') sb.append(c);
                c = scanner.next();
            }
            return new Token(TokenClass.STRING_LITERAL, sb.toString(), line, column);
        }

        //Character
        if(c == '\'') {
            c = scanner.next();
            if (c == '\\')
                c = scanner.next();
            String chr = "" + c;
            c = scanner.next();
            if(c != '\'')
                return new Token(TokenClass.INVALID, line, column);
            return new Token(TokenClass.CHARACTER, chr, line, column);
        }


        // identifier
        if(Character.isLetter(c)){
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = scanner.peek();
            while (Character.isLetterOrDigit(c) || c =='_') {
                sb.append(c);
                scanner.next();      //refresh the pointer
                c = scanner.peek();  //pre-read
            }
            switch (sb.toString()){
                case "main" : return new Token(TokenClass.MAIN, line, column);
                case "print_s" : return new Token(TokenClass.PRINT, "print_s",line, column);
                case "print_i" : return new Token(TokenClass.PRINT, "print_i",line, column);
                case "print_c" : return new Token(TokenClass.PRINT, "print_c",line, column);
                case "read_i" : return new Token(TokenClass.READ, "read_i",line, column);
                case "read_c" : return new Token(TokenClass.READ, "read_c",line, column);
                case "int" : return new Token(TokenClass.INT, line, column);
                case "void" : return new Token(TokenClass.VOID, line, column);
                case "char" : return new Token(TokenClass.CHAR, line, column);
                case "if" : return new Token(TokenClass.IF, line, column);
                case "else" : return new Token(TokenClass.ELSE, line, column);
                case "while" : return new Token(TokenClass.WHILE, line, column);
                case "return" : return new Token(TokenClass.RETURN, line, column);
                default: return new Token(TokenClass.IDENTIFIER, sb.toString() ,line, column);
            }
        }

        //number
        if(Character.isDigit(c)) {
            StringBuilder sb = new StringBuilder();
            sb.append(c);
            c = scanner.peek();
            while(Character.isDigit(c)) {
                sb.append(c);
                scanner.next();
                c = scanner.peek();
            }
            return new Token(TokenClass.NUMBER, sb.toString(), line, column);
        }

        // if we reach this point, it means we did not recognise a valid token
        error(c,line,column);
        return new Token(TokenClass.INVALID, line, column);
    }


}