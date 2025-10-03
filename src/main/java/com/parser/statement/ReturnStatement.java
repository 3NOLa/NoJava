package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class ReturnStatement extends Statement{

    Expression value;

    public ReturnStatement(location loc, Expression value) {
        super(loc);
        this.value = value;
    }
}
