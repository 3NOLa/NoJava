package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class OperatorExpression extends Expression{

    public Token.TokenType Operator;
    public Expression right;
    public Expression left;

    public OperatorExpression(location loc, Token.TokenType Operator, Expression right, Expression left){
        super(loc);
        this.Operator = Operator;
        this.right = right;
        this.left = left;
    }
}
