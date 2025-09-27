package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.expression.OperatorExpression;

public class OperatorParselet implements InfixParselets{

    @Override
    public Expression parse(Parser par, Token t, precedence prec) {
        Expression left = par;
        Expression right = par.parseExpression();
        return new OperatorExpression(t.loc, t.type, left, right);
    }
}
