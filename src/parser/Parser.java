package parser;

import ast.*;
import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;



/**
 * @author cdubach
 */
public class Parser {

    private         Token token;

    // use for backtracking (useful for distinguishing decls from procs when parsing a program for instance)
    private         Queue<Token> buffer = new LinkedList<>();

    private final   Tokeniser tokeniser;



    public Parser(Tokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    public Program parse() {
        // get the first token
        nextToken();

        return parseProgram();
    }

    public int getErrorCount() {
        return error;
    }

    private int error = 0;
    private Token lastErrorToken;

    private void error(TokenClass... expected) {

        if (lastErrorToken == token) {
            // skip this error, same token causing trouble
            return;
        }

        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (TokenClass e : expected) {
            sb.append(sep);
            sb.append(e);
            sep = "|";
        }
        System.out.println("Parsing error: expected ("+sb+") found ("+token+") at "+token.position);

        error++;
        lastErrorToken = token;
    }

    /*
     * Look ahead the i^th element from the stream of token.
     * i should be >= 1
     */
    private Token lookAhead(int i) {
        // ensures the buffer has the element we want to look ahead
        while (buffer.size() < i)
            buffer.add(tokeniser.nextToken());
        assert buffer.size() >= i;

        int cnt = 1;
        for (Token t : buffer) {
            if (cnt == i)
                return t;
            cnt++;
        }

        assert false; // should never reach this
        return null;
    }


    /*
     * Consumes the next token from the tokeniser or the buffer if not empty.
     */
    private void nextToken() {
        if (!buffer.isEmpty())
            token = buffer.remove();
        else
            token = tokeniser.nextToken();
    }

    /*
     * If the current token is equals to the expected one, then skip it, otherwise report an error.
     * Returns the expected token or null if an error occurred.
     */
    private Token expect(TokenClass... expected) {
        for (TokenClass e : expected) {
            if (e == token.tokenClass) {
                Token cur = token;
                nextToken();
                return cur;
            }
        }

        error(expected);
        return null;
    }

    /*
    * Returns true if the current token equals to any of the expected ones.
    */

    private boolean accept(TokenClass... expected) {
        boolean result = false;
        for (TokenClass e : expected)
            result |= (e == token.tokenClass);
        return result;
    }


    private Program parseProgram() {
        parseIncludes();
        List<VarDecl> varDecls = parseDecls();
        List<Procedure> procs = parseProcs();
        Procedure main = parseMain();
        expect(TokenClass.EOF);
        return new Program(varDecls, procs, main);
    }

    // includes are ignored, so does not need to return an AST node
    private void parseIncludes() {
	    if (accept(TokenClass.INCLUDE)) {
            nextToken();
            expect(TokenClass.STRING_LITERAL);
            parseIncludes();
        }
    }

    private List<VarDecl> parseDecls() {
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID) && (lookAhead(2).tokenClass == TokenClass.SEMICOLON)){
            List<VarDecl> vd = new ArrayList<>();
            Type type;

            if (token.tokenClass == TokenClass.INT ){
                type = Type.INT;
            } else if(token.tokenClass == TokenClass.VOID){
                type = Type.VOID;
            } else {
                type = Type.CHAR;
            }
            nextToken();

            Token v = expect(TokenClass.IDENTIFIER);

            if(v != null) {
                Var var = new Var(v.toString());
                vd.add(new VarDecl(type, var));
            }

            expect(TokenClass.SEMICOLON);
            List<VarDecl> paras = parseParams();
            if(paras != null) vd.addAll(paras);
            return vd;
        } else if (accept(TokenClass.IDENTIFIER)
                && (lookAhead(1).tokenClass != TokenClass.LPAR)
                && (lookAhead(1).tokenClass != TokenClass.ASSIGN)) {
            error(TokenClass.INT, TokenClass.CHAR);
        }

        return null;
    }

    private List<VarDecl> parseParams(){
        if (accept(TokenClass.INT, TokenClass.CHAR)){
            List<VarDecl> varDecls = new ArrayList<>();

            nextToken();
            expect( TokenClass.IDENTIFIER );
            if( accept( TokenClass.COMMA ) ){
                nextToken();
                parseParams();
            }
            return varDecls;
        }
        return null;
    }
    
    private List<Procedure> parseProcs() {
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID) &&
                (lookAhead(1).tokenClass != TokenClass.MAIN)){
            List<Procedure> procs = new ArrayList<>();
            Type type;
            if (token.tokenClass == TokenClass.INT ){
                type = Type.INT;
            } else if (token.tokenClass == TokenClass.CHAR){
                type = Type.CHAR;
            } else {
                type = Type.VOID;
            }
            nextToken();
            //String name = token.toString();
            expect(TokenClass.IDENTIFIER);

            expect(TokenClass.LPAR);
            List<VarDecl> varDecls = parseParams();

            expect(TokenClass.RPAR);
            Block block = parseBlock();

            //procs.add(new Procedure(type, name, varDecls, block));
            //procs.addAll(parseProcs());
            parseProcs();
            return null;
        } else if ( ! accept( TokenClass.VOID, TokenClass.INT, TokenClass.CHAR ) ) {
            error( TokenClass.VOID, TokenClass.INT, TokenClass.CHAR );
        }

        return null;
    }

    private Procedure parseMain() {
        expect( TokenClass.VOID );
        expect( TokenClass.MAIN );
        expect( TokenClass.LPAR );
        expect( TokenClass.RPAR );

        return new Procedure( Type.VOID, TokenClass.MAIN.toString(), null, parseBlock() );
    }

    private Block parseBlock() {
        expect( TokenClass.LBRA );
        parseDecls();
        parseStmtlist();
        expect(TokenClass.RBRA);
        return null;
    }

    private List<Stmt> parseStmtlist(){
        while (accept( TokenClass.LBRA      , TokenClass.IF    , TokenClass.WHILE
                     , TokenClass.IDENTIFIER, TokenClass.RETURN, TokenClass.PRINT
                     , TokenClass.READ ) ){
            parseStmt();
        }
        return null;
    }

    private Stmt parseStmt() {
        switch ( token.tokenClass ){
            case LBRA : {                                          // parse block
                parseBlock();
                break;
            } case WHILE: {                                         // parse while loop
                nextToken();
                expect( TokenClass.LPAR );
                parseExpr();
                expect( TokenClass.RPAR );
                parseStmt();
                break;
            } case IF: {                                            // parse if
                nextToken();
                expect( TokenClass.LPAR );
                parseExpr();
                expect( TokenClass.RPAR );
                parseStmt();
                if ( accept( TokenClass.ELSE ) ) {
                    nextToken();
                    parseStmt();
                }
                break;
            } case IDENTIFIER: {                                    // parse function call or assign
                if( lookAhead(1).tokenClass == TokenClass.LPAR ){
                    parseFunCall();
                    expect( TokenClass.SEMICOLON );
                } else if ( lookAhead(1).tokenClass == TokenClass.ASSIGN ) {
                    nextToken();                                    // IDENT
                    nextToken();                                    // EQ
                    parseLexp();
                    expect( TokenClass.SEMICOLON );
                } else {
                    nextToken();                                    // Pass the error
                    error( TokenClass.LPAR, TokenClass.ASSIGN );
                }
                break;
            } case RETURN: {                                        // parse return
                nextToken();
                if( ! accept( TokenClass.SEMICOLON ) )
                    parseLexp();
                expect( TokenClass.SEMICOLON );
                break;
            } case PRINT: {                                         // parse print
                nextToken();
                expect( TokenClass.LPAR );
                if ( accept(TokenClass.STRING_LITERAL )){
                    nextToken();
                } else {
                    parseLexp();
                }
                expect( TokenClass.RPAR );
                expect( TokenClass.SEMICOLON );
                break;
            } case READ: {                                          // parse read
                nextToken();
                expect( TokenClass.LPAR );
                expect( TokenClass.RPAR );
                expect( TokenClass.SEMICOLON );
                break;
            } default: {
                error(    TokenClass.LBRA      , TokenClass.IF    , TokenClass.WHILE
                        , TokenClass.IDENTIFIER, TokenClass.RETURN, TokenClass.PRINT
                        , TokenClass.READ );
            }
        }

        return null;
    }

    private Expr parseExpr() {
        parseLexp();
        if (accept(   TokenClass.LE, TokenClass.LT, TokenClass.GE
                    , TokenClass.NE, TokenClass.EQ, TokenClass.GT ) ) {
            nextToken();
            parseLexp();
        }
        return null;
    }

    private Expr parseLexp() {
        parseTerm();
        while ( accept( TokenClass.PLUS, TokenClass.MINUS )){
            nextToken();
            parseTerm();
        }
        return null;
    }

    private Expr parseTerm() {
        parseFactor();
        while ( accept( TokenClass.DIV, TokenClass.TIMES, TokenClass.MOD ) ){
            nextToken();
            parseFactor();
        }
        return null;
    }

    private Expr parseFactor() {
        switch ( token.tokenClass ) {
            case LPAR:{                                                //parse Lpar
                nextToken();
                parseLexp();
                expect( TokenClass.RPAR );
                break;
            } case CHARACTER:{                                          //parse character
                nextToken();
                break;
            } case READ:{                                               //parse read
                nextToken();
                expect( TokenClass.LPAR );
                expect( TokenClass.RPAR );
                break;
            } case IDENTIFIER:{                                         //parse variable without minus or function
                if ( lookAhead(1).tokenClass == TokenClass.LPAR ){
                    parseFunCall();
                } else {
                    nextToken();
                }
                break;
            } case MINUS:{                                              //parse variable or number with minus
                nextToken();
                expect( TokenClass.IDENTIFIER, TokenClass.NUMBER );
                break;
            } case NUMBER:{                                             //parse number without minus
                nextToken();
                break;
            } default: {
                error(    TokenClass.LPAR      , TokenClass.CHARACTER, TokenClass.READ
                        , TokenClass.IDENTIFIER, TokenClass.NUMBER   , TokenClass.MINUS );
            }
        }

        return null;
    }

    private Expr parseFunCall() {
        expect( TokenClass.IDENTIFIER );
        expect( TokenClass.LPAR );
        parseIdent();
        expect( TokenClass.RPAR );
        return null;
    }

    private Expr parseIdent() {
        if ( accept( TokenClass.IDENTIFIER ) ){
            nextToken();
            while ( accept( TokenClass.COMMA ) ) {
                nextToken();
                expect( TokenClass.IDENTIFIER );
            }
        }
        return null;
    }
}
