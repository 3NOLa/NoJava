package com.parser.parselets;

import com.lexer.Token;
import com.parser.Grammer;
import com.parser.Parser;
import com.parser.expression.AccessException;
import com.parser.expression.Expression;

public class MemberAccessParselet implements LedParselets {
    @Override
    public Expression parse(Parser par, Token t, Expression left) {
        Expression right = par.parseExpression(Grammer.mapBp.get(t.type));
        return new AccessException(t.loc, left, right);
    }
}
