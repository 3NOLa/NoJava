package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class AssignmentExpression extends Expression{

    public Token.TokenType condition;
    public Expression assignee;
    public Expression assigned;

    public AssignmentExpression(location loc, Token.TokenType condition, Expression assignee, Expression assigned){
        super(loc);
        this.condition = condition;
        this.assignee = assignee;
        this.assigned = assigned;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append("AssignmentExpression\n");
        sb.append(indent(depth)).append("  assignee:\n");
        sb.append(assignee.toStringHelper(depth + 2));
        sb.append(indent(depth)).append("  assigned:\n");
        sb.append(assigned.toStringHelper(depth + 2));
        return sb.toString();
    }
}
