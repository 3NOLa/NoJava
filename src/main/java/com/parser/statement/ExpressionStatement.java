package com.parser.statement;

import com.parser.expression.Expression;

public class ExpressionStatement extends Statement{

    Expression expr;

    public ExpressionStatement(Expression expr) {
        super(expr.loc);
        this.expr = expr;
    }
}
