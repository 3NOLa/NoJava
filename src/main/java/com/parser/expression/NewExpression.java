package com.parser.expression;

import com.semantic.ASTVisitor;
import com.semantic.ExpressionVisitor;
import com.util.location;

public class NewExpression extends Expression{

    public final Expression constructorCall;

    public NewExpression(location loc, Expression constructorCall) {
        super(loc);
        this.constructorCall = constructorCall;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");
        if (constructorCall != null) {
            sb.append(indent(depth)).append("  call:\n");
            sb.append(constructorCall.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
