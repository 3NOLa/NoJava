package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

import java.io.StringReader;

public abstract class LoopStatement extends Statement{

    public Statement body;
    public Expression condition;

    public LoopStatement(location loc, Statement body, Expression condition) {
        super(loc);
        this.body = body;
        this.condition = condition;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();

        if(condition != null) {
            sb.append(indent(depth)).append("  condition:\n");
            sb.append(condition.toStringHelper(depth + 2));
        }

        if(body != null) {
            sb.append(indent(depth)).append("  body:\n");
            sb.append(body.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
