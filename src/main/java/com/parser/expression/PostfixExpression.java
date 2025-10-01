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
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("PostfixExpression(").append(operator).append(")\n");
        if (left != null) {
            sb.append(indent(depth)).append("  left:\n");
            sb.append(left.toStringHelper(depth + 2));
        }
        return sb.toString();
    }

}
