package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.expression.PrimaryExpression;

public class PrimaryParselet implements ledParselets {
    @Override
    public Expression parse(Parser par, Token t) {
        return new PrimaryExpression(t.loc, t.type, t.value);
    }
}
