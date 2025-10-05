package com.parser.statement;

import com.parser.expression.Expression;
import com.util.location;

public class IfStatement extends Statement{

    public Expression condition;
    public Statement then;
    public Statement _else;

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

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");
        if (condition != null) {
            sb.append(indent(depth)).append("  condition:\n");
            sb.append(condition.toStringHelper(depth + 2));
        }
        if (then != null) {
            sb.append(indent(depth)).append("  then:\n");
            sb.append(then.toStringHelper(depth + 2));
        }
        if (_else != null) {
            sb.append(indent(depth)).append("  else:\n");
            sb.append(_else.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
