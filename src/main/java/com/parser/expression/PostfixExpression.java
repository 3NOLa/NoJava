package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class PostfixExpression extends Expression{

    public Token.TokenType operator;
    public Expression left;

    public PostfixExpression(location loc, Token.TokenType operator, Expression left) {
        super(loc);
        this.operator = operator;
        this.left = left;
    }

    @Override
    public String toString() {
        return super.toString() + getClass().getName() + " (" + operator.name() + "))\n";
    }
}
