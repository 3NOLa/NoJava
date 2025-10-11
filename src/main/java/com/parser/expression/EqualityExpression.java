package com.parser.expression;

import com.lexer.Token;
import com.semantic.ASTVisitor;
import com.semantic.ExpressionVisitor;
import com.util.location;

public class EqualityExpression extends Expression{

    public Token.TokenType equalityOperator;
    public Expression right;
    public Expression left;

    public EqualityExpression(location loc, Token.TokenType equalityOperator, Expression right, Expression left){
        super(loc);
        this.equalityOperator = equalityOperator;
        this.right = right;
        this.left = left;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("EqualityExpression(").append(equalityOperator).append(")\n");
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