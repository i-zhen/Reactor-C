package parser;

import ast.*;
import com.sun.corba.se.impl.oa.toa.TOA;
import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * @author cdubach
 */
public class Parser {

    private Token token;

    // use for backtracking (useful for distinguishing decls from procs when parsing a program for instance)
    private Queue<Token> buffer = new LinkedList<>();

    private final Tokeniser tokeniser;



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
        if (accept(TokenClass.INT, TokenClass.CHAR) && (lookAhead(2).tokenClass == TokenClass.SEMICOLON)){
            List<VarDecl> varDecls = new ArrayList<>();
            Type type;

            if (token.tokenClass == TokenClass.INT ){
                type = Type.INT;
            } else{
                type = Type.CHAR;
            }
            nextToken();

            ast.Var var = new ast.Var(token.toString());
            expect(TokenClass.IDENTIFIER);

            //varDecls.add(new VarDecl(type, var));
            expect(TokenClass.SEMICOLON);
            //varDecls.addAll(parseDecls());
            parseDecls();
            return null;
        }

        return null;
    }

    private List<VarDecl> parseParams(){
        if (accept(TokenClass.INT, TokenClass.CHAR)){
            List<VarDecl> varDecls = new ArrayList<>();
            //incomplete
            nextToken();
            expect(TokenClass.IDENTIFIER);
            if(accept(TokenClass.COMMA)){
                nextToken();
                parseParams();
            }
            return varDecls;
        }
        return null;
    }

    private List<Procedure> parseProcs() {
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)){
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
            String name = token.toString();
            expect(TokenClass.IDENTIFIER);

            expect(TokenClass.LPAR);
            List<VarDecl> varDecls = parseParams();

            expect(TokenClass.RPAR);
            Block block = parseBlock();

            //procs.add(new Procedure(type, name, varDecls, block));
            //procs.addAll(parseProcs());
            return null;
        }

        return null;
    }

    private Procedure parseMain() {
        expect(TokenClass.VOID);
        expect(TokenClass.MAIN);
        expect(TokenClass.LPAR);
        expect(TokenClass.RPAR);

        return new Procedure(Type.VOID, "main", null, parseBlock());
    }

    private Block parseBlock() {
        expect(TokenClass.LBRA);
        parseDecls();
        parseStmt();
        expect(TokenClass.RBRA);
        return null;
    }

    private List<Stmt> parseStmt() {
        if(accept(TokenClass.LBRA)) {
            // parse block
            parseBlock();
        } else if (accept(TokenClass.WHILE)) {
            // parse while loop
            nextToken();
            expect(TokenClass.LPAR);
            parseExpr();
            expect(TokenClass.RPAR);
            parseStmt();
        } else if (accept(TokenClass.IF)) {
            //parse if
            nextToken();
            expect(TokenClass.LPAR);
            parseExpr();
            expect(TokenClass.RPAR);
            parseStmt();
            if (accept(TokenClass.ELSE)) {
                nextToken();
                parseStmt();
            }
        } else if (accept(TokenClass.IDENTIFIER)){
            //parse funcall or assign
            if(lookAhead(1).tokenClass == TokenClass.LPAR){
                parseFunCall();
                expect(TokenClass.SEMICOLON);
            } else if (lookAhead(1).tokenClass == TokenClass.ASSIGN) {
                nextToken(); // IDENT
                nextToken(); // EQ
                parseLexp();
                expect(TokenClass.SEMICOLON);
            } else {
                error();
            }
        } else if (accept(TokenClass.RETURN)) {
            //parse return
            nextToken();
            parseLexp();
            expect(TokenClass.SEMICOLON);
        } else if (accept(TokenClass.PRINT)) {
            //parse print
            nextToken();
            expect(TokenClass.LPAR);
            if (accept(TokenClass.STRING_LITERAL)){
                nextToken();
            } else {
                parseLexp();
            }
            expect(TokenClass.RPAR);
            expect(TokenClass.SEMICOLON);
        } else if (accept(TokenClass.READ)){
            //parse read
            nextToken();
            expect(TokenClass.LPAR);
            expect(TokenClass.RBRA);
            expect(TokenClass.SEMICOLON);
        }

        if (accept(TokenClass.LBRA, TokenClass.WHILE, TokenClass.IF, TokenClass.IDENTIFIER,
                TokenClass.RETURN, TokenClass.PRINT, TokenClass.READ)) {
            parseStmt();
        }
        return null;
    }

    private Expr parseExpr() {
        parseLexp();
        if (accept(TokenClass.LE, TokenClass.LT, TokenClass.GE,
                TokenClass.NE, TokenClass.EQ, TokenClass.GT)) {
            nextToken();
            parseLexp();
        }
        return null;
    }

    private Expr parseLexp() {
        parseTerm();
        if (accept(TokenClass.PLUS, TokenClass.MINUS)){
            nextToken();
            parseTerm();
        }
        return null;
    }

    private Expr parseTerm() {
        parseFactor();
        if (accept(TokenClass.DIV, TokenClass.TIMES, TokenClass.MOD)){
            nextToken();
            parseFactor();
        }
        return null;
    }

    private Expr parseFactor() {
        if (accept(TokenClass.LPAR)){
            //parse Lpar
            nextToken();
            parseLexp();
            expect(TokenClass.RPAR);
        } else if (accept(TokenClass.CHARACTER)){
            //parse character
            nextToken();
        } else if (accept(TokenClass.READ)){
            //parse read
            nextToken();
            expect(TokenClass.LPAR);
            expect(TokenClass.RPAR);
        } else if (accept(TokenClass.IDENTIFIER)){
            //parse variable without minus or function
            if (lookAhead(1).tokenClass == TokenClass.LPAR){
                parseFunCall();
            } else {
                nextToken();
            }
        } else if (accept(TokenClass.MINUS)){
            //parse variable or number with minus
            expect(TokenClass.IDENTIFIER, TokenClass.NUMBER);
        } else if (accept(TokenClass.NUMBER)){
            //parse number without minus
            nextToken();
        } else {
            error();
        }
        return null;
    }

    private Expr parseFunCall() {
        expect(TokenClass.IDENTIFIER);
        expect(TokenClass.LPAR);
        parseIdent();
        expect(TokenClass.RPAR);

        return null;
    }

    private Expr parseIdent() {
        if (accept(TokenClass.IDENTIFIER)){
            if (lookAhead(1).tokenClass == TokenClass.COMMA) {
                nextToken();
                parseIdent();
            }
        }
        return null;
    }
    // to be completed ...        
}
