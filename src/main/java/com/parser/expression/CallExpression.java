package com.parser.expression;

import com.util.location;

import java.util.ArrayList;
import java.util.List;

public class CallExpression extends Expression{

    Expression func;
    List<Expression> args = new ArrayList<>();

    public CallExpression(location loc, Expression func, List<Expression> args) {
        super(loc);
        this.func = func;
        this.args = args;
    }
}
