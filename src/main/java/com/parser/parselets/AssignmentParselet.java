package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.AssignmentExpression;
import com.parser.expression.Expression;

public class AssignmentParselet implements LedParselets{
    @Override
    public Expression parse(Parser par, Token t, Expression left) {
        Expression right = par.parseExpression(par.mapBp.get(t.type));
        return new AssignmentExpression(t.loc, t.type, left, right);
    }
}
