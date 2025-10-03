package com.parser.parselets;

import com.lexer.Token;
import com.parser.Grammer;
import com.parser.Parser;
import com.parser.expression.EqualityExpression;
import com.parser.expression.Expression;

public class EqualityParselet implements LedParselets{
    @Override
    public Expression parse(Parser par, Token t, Expression left) {
        Expression right = par.parseExpression(Grammer.mapBp.get(t.type));
        return new EqualityExpression(t.loc, t.type, left, right);
    }
}
