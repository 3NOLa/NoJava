package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.expression.PostfixExpression;

public class PostfixParselet implements LedParselets{
    @Override
    public Expression parse(Parser par, Token t, Expression left) {
        return new PostfixExpression(t.loc, t.type, left);
    }
}
