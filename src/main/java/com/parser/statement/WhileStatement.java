package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class WhileStatement extends LoopStatement{

    public WhileStatement(location loc, Statement body, Expression condition) {
        super(loc, body, condition);
    }
}
