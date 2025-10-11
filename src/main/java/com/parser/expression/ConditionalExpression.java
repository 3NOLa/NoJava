package com.parser.expression;

import com.semantic.ASTVisitor;
import com.semantic.ExpressionVisitor;
import com.util.location;

public class ConditionalExpression extends Expression{

    public Expression condition;
    public Expression then;
    public Expression _else;

    public ConditionalExpression(location loc, Expression condition, Expression then, Expression _else){
        super(loc);
        this.condition = condition;
        this.then = then;
        this._else = _else;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("ConditionalExpression").append("\n");
        if (condition != null) {
            sb.append(indent(depth)).append("  condition:\n");
            sb.append(condition.toStringHelper(depth + 2));
        }
        if (then != null) {
            sb.append(indent(depth)).append("  then:\n");
            sb.append(then.toStringHelper(depth + 2));
        }
        if (_else != null) {
            sb.append(indent(depth)).append("  else:\n");
            sb.append(_else.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
