package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class ForStatement extends LoopStatement{

    public Statement init;
    public Expression update;

    public ForStatement(location loc, Statement body, Expression condition, Expression update, Statement init) {
        super(loc, body, condition);
        this.init = init;
        this.update = update;
    }

    public ForStatement(location loc, Statement body, Expression condition, Expression update) {
        super(loc, body, condition);
        this.init = null;
        this.update = update;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        if(init != null) {
            sb.append(indent(depth)).append("  init:\n");
            sb.append(init.toStringHelper(depth + 2));
        }

        if(update != null) {
            sb.append(indent(depth)).append("  update:\n");
            sb.append(update.toStringHelper(depth + 2));
        }
        sb.append(super.toStringHelper(depth));

        return sb.toString();
    }
}
