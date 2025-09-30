package com.parser.parselets;

import com.lexer.Token;
import com.parser.ParseException;
import com.parser.Parser;
import com.parser.expression.Expression;

public class GroupParselet implements NudParselets {

    @Override
    public Expression parse(Parser par, Token t) {
        Expression expr = par.parseExpression(par.mapBp.get(t.type));
        par.cur.consume(Token.TokenType.RPAREN);// checks if next token is RPAREN IF NOT THROE EXCEPTION
        return expr;
    }
}

