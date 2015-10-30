package lexer;

import util.Position;

/**
 * @author cdubach
 */
public class Token {

    public enum TokenClass {

        IDENTIFIER, // ('a'|...|'z'|'A'|...|'Z'|'_')('0'|...|'9'|'a'|...|'z'|'A'|...|'Z'|'_')*

        ASSIGN, // '='

        // delimiters
        LBRA, // '{'
        RBRA, // '}'
        LPAR, // '('
        RPAR, // ')'
        SEMICOLON, // ';'
        COMMA, // ','

	// special functions
        MAIN,  // "main"
        PRINT, // "print_s" | "print_i" | "print_c"
	    READ,  // "read_i" | "read_c"

        // types
        INT,  // "int"
        VOID, // "void"
        CHAR, // "char"

        // control flow
        IF,     // "if"
        ELSE,   // "else"
        WHILE,  // "while"
        RETURN, // "return"

        // include
        INCLUDE, // "#include"

        // literals
        STRING_LITERAL, // \".*\"  any sequence of characters enclosed in double quote " (please be aware of the escape character backslash \)
        NUMBER,         // ('0'|...|'9')+
        CHARACTER,      // \'('a'|...|'z'|'A'|...|'Z'|'\t'|'\n'|'.'|','|'_'|...)\'  a character starts and end with a single quote '

        // comparisons
        EQ, // "=="
        NE, // "!="
        LT, // '<'
        GT, // '>'
        LE, // "<="
        GE, // ">="

        // arithmetic operators
        PLUS,  // '+'
        MINUS, // '-'
        TIMES, // '*'
        DIV,   // '/'
        MOD,   // '%'

        // special tokens
        EOF,    // signal end of file
        INVALID // in case we cannot recognise a character as part of a valid token
    }


    public final TokenClass tokenClass;
    public final String data;
    public final Position position;

    public Token(TokenClass type, int lineNum, int colNum) {
        this(type, "", lineNum, colNum);
    }

    public Token (TokenClass tokenClass, String data, int lineNum, int colNum) {
        assert (tokenClass != null);
        this.tokenClass = tokenClass;
        this.data = data;
        this.position = new Position(lineNum, colNum);
    }



    @Override
    public String toString() {
        if (data.equals(""))
            return tokenClass.toString();
        else
            return tokenClass.toString()+"("+data+")";
    }

}


