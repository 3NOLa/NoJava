package com.parser;

import com.lexer.Lexer;
import com.lexer.Token;
import com.parser.declaration.*;
import com.parser.expression.Expression;
import com.parser.parselets.*;
import com.parser.statement.*;
import com.parser.statementsParselets.*;
import com.semantic.SemanticAnalyzer;

import java.io.IOException;
import java.security.PublicKey;
import java.util.*;

public class Parser {

    public final TokenCurser cur;

    public Parser(TokenCurser cur){
        this.cur = cur;
    }

    public boolean isAtEnd(){return cur.match(Token.TokenType.EOF);}

    public Expression parseExpression(LedParselets.Precedence prec){
        Token t = cur.get();

        NudParselets PrefixFunc = Grammer.mapPrefix.get(t.type);
        if (PrefixFunc == null){
            throw new ParseException("Expression doesn't start with a prefix token", t);
        }
        Expression left = PrefixFunc.parse(this, t);

        //System.out.println("token type: " + cur.peek().type);
        while(prec.bp < Grammer.mapBp.get(cur.peek().type).bp) {
            Token Inft = cur.get();
            LedParselets InfixFunc = Grammer.mapInfix.get(Inft.type);
            if (InfixFunc == null) {
                throw new ParseException("Expression doesn't continue with a Inifix token", Inft);
            }
            left = InfixFunc.parse(this, Inft, left);
        }
        return left;
    }

    public Statement parseStatement(){
        Token t = cur.peek();

        StatementsParselets parseFunc = Grammer.mapStatementPars.get(t.type);
        if (parseFunc != null)
            return parseFunc.parse(this);

        if (isTypeStart()) { //if the statement starts with an identifier but it's a type identifier
            return Grammer.VARDECSTMT.parse(this);
        }

        Statement stm = new ExpressionStatement(parseExpression(LedParselets.Precedence.START));
        cur.consume(Token.TokenType.SEMICOLON);// expression statement must end with semicolon ;
        return stm;
    }

    public Declaration parseDeclaration(){
        HashSet<Token.TokenType> modifiers = getModifiers();
        Token tokentype = cur.get(); //or kw (class, interface) or type(int ,char)
        Token name = cur.get(Token.TokenType.IDENTIFIER);

        if(tokentype.type == Token.TokenType.KW_CLASS){ // class
            return parseClass(modifiers, name);
        } else if (tokentype.type == Token.TokenType.KW_INTERFACE) { //interface
            return parseInterface(modifiers, name);
        }
        else {

            Type type = new Type(tokentype.type, tokentype.value);
            if (cur.match(Token.TokenType.SEMICOLON)) { //variable declaration
                return new VariableDeclaration(tokentype.loc, type, name.value);
            } else if (cur.match(Token.TokenType.OP_ASSIGN)) { //variable declaration with a value
                Expression initializer = parseExpression(LedParselets.Precedence.ASSIGNMENT); //im sending ASSIGNMENT Precedence because i will get only the right side of ana assignment;
                cur.consume(Token.TokenType.SEMICOLON); // must end with semicolon
                return new VariableDeclaration(tokentype.loc, type, name.value, initializer);
            } else if (cur.match(Token.TokenType.LPAREN)) { // function
                List<VariableDeclaration> args = new ArrayList<>();
                if (cur.peek().type != Token.TokenType.RPAREN)
                    args = parseIdentifierList();
                cur.consume(Token.TokenType.RPAREN);
                if (cur.match(Token.TokenType.SEMICOLON)) // function without a body
                    return new FunctionDeclaration(tokentype.loc, modifiers, name.value, type, args, null);

                Statement body = parseStatement(); // it's going to be a block statement
                return new FunctionDeclaration(tokentype.loc, modifiers, name.value, type, args, body); // function with a body
            }

            return null;
        }
    }

    private boolean isTypeStart() {
        Token t = cur.peek();

        // If it's an identifier followed by another identifier or brackets, it's likely a declaration:
        if (t.type == Token.TokenType.IDENTIFIER) {
            Token next = cur.peek(1);
            if (next.type == Token.TokenType.IDENTIFIER) return true;      // Foo bar;
            if (next.type == Token.TokenType.LBRACKET) return true;        // Foo[] x;
            if (next.type == Token.TokenType.DOT) return true;             // java.util.List x;
        }

        return false;
    }


    public List<VariableDeclaration> parseIdentifierList(){
        List<VariableDeclaration> args = new ArrayList<>();
        do{
            args.add((Grammer.VARDECSTMT.parseVar(this)));
        }while (cur.match(Token.TokenType.COMMA)); // comma between Identifiers (int y, int x)

        return args;
    }

    private HashSet<Token.TokenType>getModifiers(){
        HashSet<Token.TokenType> seenModifiers = new LinkedHashSet<>();
        Token.TokenType accessModifier = null;

        while (Grammer.Modifiers.contains(cur.peek().type)) {
            Token modifier = cur.get();

            if (!seenModifiers.add(modifier.type)) {
                throw new ParseException("Duplicate modifier: " , modifier);
            }

            if (Grammer.isAccessModifier.contains(modifier.type)) {
                if (accessModifier != null) {
                    throw new ParseException("Multiple access modifiers: "
                            + accessModifier + " and " + modifier.type, modifier);
                }
                accessModifier = modifier.type;
            }
        }
        return seenModifiers;
    }

    public Declaration parseDeclarationBlock(){
        List<Declaration> decls = new ArrayList<>();
        Token loc = cur.get(Token.TokenType.LBRACE);

        while(!cur.match(Token.TokenType.RBRACE)){
            decls.add(parseDeclaration());
        }
        return new BlockDeclaration(loc.loc,decls);
    }

    public Declaration parseClass(HashSet<Token.TokenType> modifiers, Token name){
        Token superclass = cur.match(Token.TokenType.KW_EXTENDS) ? cur.get(Token.TokenType.IDENTIFIER) : null;
        List<Token> interfaces = new ArrayList<>();
        if(cur.match(Token.TokenType.KW_IMPLEMENTS)){
            do{
                interfaces.add(cur.get());
            }while (cur.match(Token.TokenType.COMMA));
        }

        Declaration body = parseDeclarationBlock();

        return new ClassDeclaration(name.loc, modifiers, name.value, body, superclass, interfaces);
    }

    public Declaration parseInterface(HashSet<Token.TokenType> modifiers, Token name){
        List<Token> superinterface = new ArrayList<>();
        if(cur.match(Token.TokenType.KW_EXTENDS)){
            do{
                superinterface.add(cur.get());
            }while (cur.match(Token.TokenType.COMMA));
        }

        Declaration body = parseDeclarationBlock();

        return new InterfaceDeclaration(name.loc, modifiers, name.value, body, superinterface);
    }

    public CompilationUnit parseCompilationUnit() {
        CompilationUnit unit = new CompilationUnit();

        // 1. Optional package declaration
//        if (match(TokenType.PACKAGE)) {
//            unit.packageName = parseQualifiedIdentifier();
//            consume(TokenType.SEMICOLON);
//        }

        while (cur.match(Token.TokenType.KW_IMPORT)) {
            unit.imports.add(parseImportDeclaration());
        }

        while (!(isAtEnd())) {
            unit.types.add(parseDeclaration());
        }

        return unit;
    }

    public ImportDeclaration parseImportDeclaration() {
        ImportDeclaration imp = new ImportDeclaration(cur.peek().loc);

//        List<Token> parts = new ArrayList<>();
//        parts.add(cur.get(Token.TokenType.IDENTIFIER));
//
//        while (cur.match(Token.TokenType.DOT) && cur.peek(1).type != Token.TokenType.OP_MUL) {
//            parts.add(cur.get(Token.TokenType.IDENTIFIER));
//        }

        imp.path = parseQualifiedIdentifier(); // e.g. java.util.List

        if (cur.match(Token.TokenType.DOT)) {
            cur.consume(Token.TokenType.OP_MUL); // handles wildcard imports
            imp.isWildcard = true;
        }

        cur.consume(Token.TokenType.SEMICOLON);
        return imp;
    }

    public List<Token> parseQualifiedIdentifier() {
        List<Token> parts = new ArrayList<>();

        do {
            parts.add(cur.get(Token.TokenType.IDENTIFIER));
        } while (cur.match(Token.TokenType.DOT));

        return parts;
    }



    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer("C:\\projJava\\javacompiler\\src\\test\\java\\parser\\javaExpressionTest.txt");
        lex.analyze();
        System.out.println(lex.toString());
        Parser par = new Parser(new TokenCurser(lex.tokens));
        CompilationUnit unit = par.parseCompilationUnit();
        System.out.println(unit);
        SemanticAnalyzer s =  new SemanticAnalyzer();
        s.visit(unit);
        System.out.println("\n\n");
        s.scopes.printScopes();
//        while(par.cur.peek().type != Token.TokenType.EOF){
//            System.out.println(par.parseDeclaration());
//        }
    }

}
