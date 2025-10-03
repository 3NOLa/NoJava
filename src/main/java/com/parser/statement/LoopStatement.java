package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public abstract class LoopStatement extends Statement{

    Statement body;
    Expression condition;

    public LoopStatement(location loc, Statement body, Expression condition) {
        super(loc);
        this.body = body;
        this.condition = condition;
    }
}
