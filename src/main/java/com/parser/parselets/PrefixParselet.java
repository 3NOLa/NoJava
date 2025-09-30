package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.expression.PrefixExpression;

public class PrefixParselet implements NudParselets{
    @Override
    public Expression parse(Parser par, Token t) {
        Expression right = par.parseExpression(LedParselets.Precedence.PREFIX);
        return new PrefixExpression(t.loc, t.type, right);
    }
}
