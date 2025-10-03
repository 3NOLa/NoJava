package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class IfStatement extends Statement{

    Expression condition;
    Statement then;
    Statement _else;

    public IfStatement(location loc, Expression condition, Statement then) {
        super(loc);
        this.condition = condition;
        this.then = then;
        this._else = null;
    }


    public IfStatement(location loc, Expression condition, Statement then, Statement _else) {
        super(loc);
        this.condition = condition;
        this.then = then;
        this._else = _else;
    }
}
