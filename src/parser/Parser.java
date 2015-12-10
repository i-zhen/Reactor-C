package parser;

import ast.*;
import lexer.Token;
import lexer.Tokeniser;
import lexer.Token.TokenClass;

import java.util.*;
import java.util.concurrent.Exchanger;


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
        List<VarDecl> vd = new ArrayList<>();
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID) && (lookAhead(2).tokenClass == TokenClass.SEMICOLON)){
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
                Var var = new Var(v.data);
                vd.add(new VarDecl(type, var));
            }

            expect(TokenClass.SEMICOLON);
            List<VarDecl> paras = parseDecls();
            if(paras != null) vd.addAll(paras);
        } else if (accept(TokenClass.IDENTIFIER)
                && (lookAhead(1).tokenClass != TokenClass.LPAR)
                && (lookAhead(1).tokenClass != TokenClass.ASSIGN)) {
            error(TokenClass.INT, TokenClass.CHAR);
        }

        return vd;
    }

    private List<VarDecl> parseParams(){
        List<VarDecl> vd = new ArrayList<>();
        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID)){

            Type type;

            if (token.tokenClass == TokenClass.INT ){
                type = Type.INT;
            } else if(token.tokenClass == TokenClass.CHAR){
                type = Type.CHAR;
            } else
                type = Type.VOID;

            nextToken();
            Token v = expect( TokenClass.IDENTIFIER );
            if(v != null) {
                Var var = new Var(v.data);
                vd.add(new VarDecl(type, var));
            }
            if( accept( TokenClass.COMMA ) ){
                nextToken();
                List<VarDecl> paras = parseParams();
                if(paras != null) vd.addAll(paras);
            }
        }
        return vd;
    }
    
    private List<Procedure> parseProcs() {
        List<Procedure> procs = new ArrayList<>();

        if (accept(TokenClass.INT, TokenClass.CHAR, TokenClass.VOID) &&
                (lookAhead(1).tokenClass != TokenClass.MAIN)){
            Type type;
            if (token.tokenClass == TokenClass.INT ){
                type = Type.INT;
            } else if (token.tokenClass == TokenClass.CHAR){
                type = Type.CHAR;
            } else {
                type = Type.VOID;
            }
            nextToken();
            String name = token.data;
            Token v = expect(TokenClass.IDENTIFIER);

            expect(TokenClass.LPAR);
            List<VarDecl> vd = parseParams();

            expect(TokenClass.RPAR);
            Block block = parseBlock();
            if(v != null && block != null && vd != null)
                procs.add(new Procedure(type, name, vd, block));

            List<Procedure> ps = parseProcs();
            if(ps != null) procs.addAll(ps);
        } else if ( ! accept( TokenClass.VOID, TokenClass.INT, TokenClass.CHAR ) ) {
            error( TokenClass.VOID, TokenClass.INT, TokenClass.CHAR );
        }

        return procs;
    }

    private Procedure parseMain() {
        expect( TokenClass.VOID );
        expect( TokenClass.MAIN );
        expect( TokenClass.LPAR );
        expect( TokenClass.RPAR );

        return new Procedure( Type.VOID, TokenClass.MAIN.name(), new ArrayList<VarDecl>(), parseBlock() );
    }

    private Block parseBlock() {
        expect( TokenClass.LBRA );
        List<VarDecl> vd = parseDecls();
        List<Stmt> stmts = parseStmtlist();
        expect(TokenClass.RBRA);
        return new Block(vd, stmts); //null is possible
    }

    private List<Stmt> parseStmtlist(){
        List<Stmt> stmts = new ArrayList<>();
        while (accept( TokenClass.LBRA      , TokenClass.IF    , TokenClass.WHILE
                     , TokenClass.IDENTIFIER, TokenClass.RETURN, TokenClass.PRINT
                     , TokenClass.READ ) ){
            Stmt st = parseStmt();
            if(st != null) stmts.add(st);
        }
        return stmts;
    }

    private Stmt parseStmt() {
        switch ( token.tokenClass ){
            case LBRA : {                                           // parse block
                return parseBlock();
            } case WHILE: {                                         // parse while loop
                nextToken();
                expect( TokenClass.LPAR );
                Expr exp = parseExpr();
                expect( TokenClass.RPAR );
                return new While(exp, parseStmt());
            } case IF: {                                            // parse if
                nextToken();
                expect( TokenClass.LPAR );
                Expr exp = parseExpr();
                expect( TokenClass.RPAR );
                Stmt ifst = parseStmt();
                if ( accept( TokenClass.ELSE ) ) {
                    nextToken();
                    return new If(exp, ifst, parseStmt());
                }
                return new If(exp, ifst);
            } case IDENTIFIER: {                                    // parse function call or assign
                if( lookAhead(1).tokenClass == TokenClass.LPAR ){
                    FunCallStmt foo = parseFunCallStmt();
                    expect( TokenClass.SEMICOLON );
                    return foo;
                } else if ( lookAhead(1).tokenClass == TokenClass.ASSIGN ) {
                    String name = token.data;
                    nextToken();                                    // IDENT
                    nextToken();                                    // EQ
                    Expr exp = parseLexp();
                    expect( TokenClass.SEMICOLON );
                    return new Assign(new Var(name), exp);
                } else {
                    nextToken();                                    // Pass the error
                    error( TokenClass.LPAR, TokenClass.ASSIGN );
                }
                break;
            } case RETURN: {                                        // parse return
                nextToken();
                if( ! accept( TokenClass.SEMICOLON ) ) {
                    Expr exp = parseLexp();
                    expect( TokenClass.SEMICOLON );
                    return new Return(exp);
                }
                expect( TokenClass.SEMICOLON );
                return new Return();
            } case PRINT: {                                         // parse print
                List<Expr> exp = new ArrayList<>();
                String name = token.data;
                nextToken();
                expect( TokenClass.LPAR );
                if ( accept(TokenClass.STRING_LITERAL )){
                    exp.add(new StrLiteral(token.data));
                    nextToken();
                } else {
                    exp.add(parseLexp());
                }
                expect( TokenClass.RPAR );
                expect( TokenClass.SEMICOLON );
                return new FunCallStmt(name, exp);
            } case READ: {                                          // parse read
                List<Expr> exp = new ArrayList<>();
                String name = token.data;
                nextToken();
                expect( TokenClass.LPAR );
                expect( TokenClass.RPAR );
                expect( TokenClass.SEMICOLON );
                return new FunCallStmt(name, exp);
            } default: {
                error(    TokenClass.LBRA      , TokenClass.IF    , TokenClass.WHILE
                        , TokenClass.IDENTIFIER, TokenClass.RETURN, TokenClass.PRINT
                        , TokenClass.READ );
            }
        }

        return null;
    }

    private Expr parseExpr() {
        Expr lhs = parseLexp();
        if (accept(   TokenClass.LE, TokenClass.LT, TokenClass.GE
                    , TokenClass.NE, TokenClass.EQ, TokenClass.GT ) ) {
            Op op;
            switch (token.tokenClass){
                case LE: op = Op.LE; break;
                case LT: op = Op.LT; break;
                case GE: op = Op.GE; break;
                case NE: op = Op.NE; break;
                case EQ: op = Op.EQ; break;
                case GT: op = Op.GT; break;
                default: op = Op.EQ; break;  //init
            }
            nextToken();
            Expr rhs = parseLexp();
            return new BinOp(lhs, op, rhs);
        }
        return lhs;
    }

    private Expr parseLexp() {
        Expr lhs = parseTerm();
        if ( accept( TokenClass.PLUS, TokenClass.MINUS )){
            Op op;
            if (token.tokenClass == TokenClass.PLUS)
                op = Op.ADD;
            else
                op = Op.SUB;

            nextToken();
            Expr rhs = parseLexp();
            return new BinOp(lhs, op, rhs);
        }
        return lhs;
    }

    private Expr parseTerm() {
        Expr lhs = parseFactor();
        if ( accept( TokenClass.DIV, TokenClass.TIMES, TokenClass.MOD ) ){
            Op op;
            switch (token.tokenClass){
                case DIV: op = Op.DIV; break;
                case TIMES: op = Op.MUL; break;
                case MOD: op = Op.MOD; break;
                default: op = Op.MOD; break;  //init
            }
            nextToken();
            Expr rhs = parseTerm();
            return new BinOp(lhs, op, rhs);
        }
        return lhs;
    }

    private Expr parseFactor() {
        switch ( token.tokenClass ) {
            case LPAR:{                                                //parse Lpar
                nextToken();
                Expr exp = parseLexp();
                expect( TokenClass.RPAR );
                return exp;
            } case CHARACTER:{                                          //parse character
                char ch = token.data.charAt(0);
                nextToken();
                return new ChrLiteral(ch);
            } case READ:{                                               //parse read
                List<Expr> exp = new ArrayList<>();
                String name = token.data;
                nextToken();
                expect( TokenClass.LPAR );
                expect( TokenClass.RPAR );
                return new FunCallExpr(name, exp);
            } case IDENTIFIER:{                                         //parse variable without minus or function
                String name = token.data;
                if ( lookAhead(1).tokenClass == TokenClass.LPAR ){
                    return parseFunCallExpr();
                } else {
                    nextToken();
                }
                return new Var(name);
            } case MINUS:{                                              //parse variable or number with minus
                nextToken();
                Token t = expect( TokenClass.IDENTIFIER, TokenClass.NUMBER );
                if(t != null && t.tokenClass.equals(TokenClass.NUMBER)){
                    return new BinOp(new IntLiteral(0), Op.SUB, new IntLiteral(Integer.parseInt(t.data)));
                }else if(t != null){
                    return new BinOp(new IntLiteral(0), Op.SUB, new Var(t.data));
                }
            } case NUMBER:{                                             //parse number without minus
                Token t = expect( TokenClass.NUMBER );
                //nextToken();
                return new IntLiteral(Integer.parseInt(t.data));
            } default: {
                error(    TokenClass.LPAR      , TokenClass.CHARACTER, TokenClass.READ
                        , TokenClass.IDENTIFIER, TokenClass.NUMBER   , TokenClass.MINUS );
            }
        }

        return null;
    }

    private FunCallExpr parseFunCallExpr() {
        String name = token.data;
        expect( TokenClass.IDENTIFIER );
        expect( TokenClass.LPAR );
        List<Expr> exp = parseIdent();
        expect( TokenClass.RPAR );
        return new FunCallExpr(name, exp);
    }

    private FunCallStmt parseFunCallStmt() {
        String name = token.data;
        expect( TokenClass.IDENTIFIER );
        expect( TokenClass.LPAR );
        List<Expr> exp = parseIdent();
        expect( TokenClass.RPAR );
        return new FunCallStmt(name, exp);
    }

    private List<Expr> parseIdent() {
        List<Expr> exp = new ArrayList<>();
        if ( accept( TokenClass.IDENTIFIER ) ){
            exp.add(new Var(token.data));
            nextToken();
            while ( accept( TokenClass.COMMA ) ) {
                nextToken();
                if(accept( TokenClass.IDENTIFIER)) {
                    exp.add(new Var(token.data));
                    expect(TokenClass.IDENTIFIER);
                }
            }
        }
        return exp;
    }
}
