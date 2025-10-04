package com.parser;

import com.lexer.Lexer;
import com.lexer.Token;
import com.parser.expression.Expression;
import com.parser.parselets.*;
import com.parser.statement.*;
import com.parser.statementsParselets.*;

import java.io.IOException;
import java.util.HashMap;

public class Parser {

    public final TokenCurser cur;

    public Parser(TokenCurser cur){
        this.cur = cur;
    }

    public boolean endExpression(){
        return cur.isEOF() || cur.match(Token.TokenType.SEMICOLON);
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

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer("C:\\projJava\\javacompiler\\src\\test\\java\\parser\\javaExpressionTest.txt");
        lex.analyze();
        System.out.println(lex.toString());
        Parser par = new Parser(new TokenCurser(lex.tokens));

        while(par.cur.peek().type != Token.TokenType.EOF){
            System.out.println(par.parseStatement());
        }
    }

}
