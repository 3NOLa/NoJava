package com.parser;

import com.lexer.Token;
import com.parser.expression.Expression;
import com.parser.expression.PrefixExpression;
import com.parser.parselets.InfixParselets;
import com.parser.parselets.PrefixParselets;

import java.util.HashMap;

public class Parser {

    private final TokenCurser cur;
    private final HashMap<Token.TokenType, InfixParselets> mapInifix = new HashMap<>();
    private final HashMap<Token.TokenType, PrefixParselets> mapPrefix = new HashMap<>();


    public Parser(TokenCurser cur){
        this.cur = cur;
    }

    public Expression parseExpression(){
        Token t = cur.get();

        PrefixParselets preFunc = mapPrefix.get(t.type);
        if (preFunc == null){
            throw new ParseException("doesn't start with a prefix token", t);
        }
        Expression left = preFunc.parse(t.type);

        return null;
    }
}
