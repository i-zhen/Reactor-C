package lexer;

import util.Position;

/**
 * @author cdubach
 */
public class Token {

    public enum TokenClass {

        IDENTIFIER, // [a-zA-Z][0-9a-zA-Z_]*

        ASSIGN, // =

        // delimiters
        LBRA, // {
        RBRA, // }
        LPAR, // (
        RPAR, // )
        SEMICOLON, // ;
        COMMA, // ,

        MAIN,  // main function identifier
        PRINT, // print function identifier
	READ,  // read function identifier

        // types
        INT,
        VOID,
        CHAR,

        // control flow
        IF,
        ELSE,
        WHILE,
        RETURN,

        // include
        INCLUDE, // #include

        // literals
        STRING_LITERAL, // ".*"
        NUMBER, // [0-9]+
        CHARACTER, // a character (e.g. 'a')

        // comparisons
        EQ, // ==
        NE, // !=
        LT, // <
        GT, // >
        LE, // <=
        GE, // >=

        // arithmetic operators
        PLUS, // +
        MINUS, // -
        TIMES, // *
        DIV, // /
        MOD, // %

        // special tokens
        EOF,    // to signal end of file
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


