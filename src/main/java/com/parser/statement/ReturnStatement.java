package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class ReturnStatement extends Statement{

    public Expression value;

    public ReturnStatement(location loc, Expression value) {
        super(loc);
        this.value = value;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        if(value != null) {
            sb.append(indent(depth)).append("  value:\n");
            sb.append(value.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
