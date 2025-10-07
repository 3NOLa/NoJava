package com.parser;

import com.lexer.Lexer;
import com.lexer.Token;
import com.parser.declaration.*;
import com.parser.expression.Expression;
import com.parser.parselets.*;
import com.parser.statement.*;
import com.parser.statementsParselets.*;

import java.io.IOException;
import java.util.*;

public class Parser {

    public final TokenCurser cur;

    public Parser(TokenCurser cur){
        this.cur = cur;
    }

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

        Statement stm = new ExpressionStatement(parseExpression(LedParselets.Precedence.START));
        cur.consume(Token.TokenType.SEMICOLON);// expression statement must end with semicolon ;
        return stm;
    }

    public Declaration parseDeclaration(){
        Token.TokenType[] modifiers = getModifiers();
        Token type = cur.get(); //or kw (class, interface) or type(int ,char)
        Token name = cur.get(Token.TokenType.IDENTIFIER);

        if(type.type == Token.TokenType.KW_CLASS){ // class
            return parseClass(modifiers, name);
        } else if (type.type == Token.TokenType.KW_INTERFACE) { //interface
            return parseInterface(modifiers, name);
        } else if(cur.match(Token.TokenType.SEMICOLON)){ //variable declaration
            return new VariableDeclaration(type.loc, type.type, name.value);
        } else if (cur.match(Token.TokenType.OP_ASSIGN)) { //variable declaration with a value
            Expression initializer = parseExpression(LedParselets.Precedence.ASSIGNMENT); //im sending ASSIGNMENT Precedence because i will get only the right side of ana assignment;
            cur.consume(Token.TokenType.SEMICOLON); // must end with semicolon
            return new VariableDeclaration(type.loc, type.type, name.value, initializer);
        } else if (cur.match(Token.TokenType.LPAREN)) { // function
            List<VariableDeclaration> args = new ArrayList<>();
            if(cur.peek().type != Token.TokenType.RPAREN)
                args = parseIdentifierList();
            cur.consume(Token.TokenType.RPAREN);
            if(cur.match(Token.TokenType.SEMICOLON)) // function without a body
                return new FunctionDeclaration(type.loc, modifiers, name.value, type.type, args, null);

            Statement body = parseStatement(); // it's going to be a block statement
            return new FunctionDeclaration(type.loc, modifiers, name.value, type.type, args, body); // function with a body
        }

        return null;
    }

    private List<VariableDeclaration> parseIdentifierList(){
        List<VariableDeclaration> args = new ArrayList<>();
        do{
            args.add((Grammer.VARDECSTMT.parseVar(this)));
        }while (cur.match(Token.TokenType.COMMA)); // comma between Identifiers (int y, int x)

        return args;
    }

    private Token.TokenType[] getModifiers(){
        Set<Token.TokenType> seenModifiers = new LinkedHashSet<>();
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
        return seenModifiers.toArray(new Token.TokenType[0]);
    }

    public Declaration parseDeclarationBlock(){
        List<Declaration> decls = new ArrayList<>();
        Token loc = cur.get(Token.TokenType.LBRACE);

        while(!cur.match(Token.TokenType.RBRACE)){
            decls.add(parseDeclaration());
        }
        return new BlockDeclaration(loc.loc,decls);
    }

    public Declaration parseClass(Token.TokenType[] modifiers, Token name){
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

    public Declaration parseInterface(Token.TokenType[] modifiers, Token name){
        List<Token> superinterface = new ArrayList<>();
        if(cur.match(Token.TokenType.KW_IMPLEMENTS)){
            do{
                superinterface.add(cur.get());
            }while (cur.match(Token.TokenType.COMMA));
        }

        Declaration body = parseDeclarationBlock();

        return new InterfaceDeclaration(name.loc, modifiers, name.value, body, superinterface);
    }

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer("C:\\projJava\\javacompiler\\src\\test\\java\\parser\\javaExpressionTest.txt");
        lex.analyze();
        System.out.println(lex.toString());
        Parser par = new Parser(new TokenCurser(lex.tokens));
        while(!par.cur.match(Token.TokenType.EOF))
            System.out.println(par.parseDeclaration());

//        while(par.cur.peek().type != Token.TokenType.EOF){
//            System.out.println(par.parseDeclaration());
//        }
    }

}
