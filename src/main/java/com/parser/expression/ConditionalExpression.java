package com.parser.expression;

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
}
