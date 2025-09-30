package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.expression.OperatorExpression;

public class OperatorParselet implements LedParselets {

    @Override
    public Expression parse(Parser par, Token t, Expression left) {
        Expression right = par.parseExpression(par.mapBp.get(t.type));
        return new OperatorExpression(t.loc, t.type, left, right);
    }
}
