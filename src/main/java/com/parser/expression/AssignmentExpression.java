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
}
