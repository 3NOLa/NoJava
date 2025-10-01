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

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("CallExpression\n");
        sb.append(indent(depth)).append("  func:\n");
        sb.append(func.toStringHelper(depth + 2));
        for (Expression arg : args) {
            sb.append(indent(depth)).append("  arg:\n");
            sb.append(arg.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
