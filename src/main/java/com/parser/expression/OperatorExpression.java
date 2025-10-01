package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class OperatorExpression extends Expression{

    public Token.TokenType Operator;
    public Expression right;
    public Expression left;

    public OperatorExpression(location loc, Token.TokenType Operator, Expression right, Expression left){
        super(loc);
        this.Operator = Operator;
        this.right = right;
        this.left = left;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("OperatorExpression(").append(Operator).append(")\n");
        if (left != null) {
            sb.append(indent(depth)).append("  left:\n");
            sb.append(left.toStringHelper(depth + 2));
        }
        if (right != null) {
            sb.append(indent(depth)).append("  right:\n");
            sb.append(right.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
