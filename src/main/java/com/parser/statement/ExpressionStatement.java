package com.parser.statement;

import com.parser.expression.Expression;
import com.semantic.ASTVisitor;
import com.semantic.StatementVisitor;

public class ExpressionStatement extends Statement{

    public Expression expr;

    public ExpressionStatement(Expression expr) {
        super(expr.loc);
        this.expr = expr;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        if(expr != null) {
            sb.append(indent(depth)).append("  expression:\n");
            sb.append(expr.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
