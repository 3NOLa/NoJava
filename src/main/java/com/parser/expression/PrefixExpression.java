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
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("PostfixExpression(").append(operator).append(")\n");
        if (right != null) {
            sb.append(indent(depth)).append("  right:\n");
            sb.append(right.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
