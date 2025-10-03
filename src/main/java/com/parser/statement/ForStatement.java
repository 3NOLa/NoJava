package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class ForStatement extends LoopStatement{

    Statement init;
    Expression update;

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
}
