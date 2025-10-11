package com.parser.statement;

import com.parser.expression.Expression;
import com.semantic.ASTVisitor;
import com.semantic.StatementVisitor;
import com.util.location;

public class DoWhileStatement extends LoopStatement{

    public DoWhileStatement(location loc, Statement body, Expression condition) {
        super(loc, body, condition);
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");
        sb.append(super.toStringHelper(depth));

        return sb.toString();
    }
}
