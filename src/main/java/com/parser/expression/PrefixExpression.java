package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class PrefixExpression extends Expression{

    public Token.TokenType operator;
    public Expression right;

    public PrefixExpression(location loc, Token.TokenType operator, Expression right) {
        super(loc);
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return super.toString() + getClass().getName() + " (" + operator.name() + right.toString() + "))\n";
    }
}
