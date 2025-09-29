package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class EqualityExpression extends Expression{

    public Token.TokenType equalityOperator;
    public Expression right;
    public Expression left;

    public EqualityExpression(location loc, Token.TokenType equalityOperator, Expression right, Expression left){
        super(loc);
        this.equalityOperator = equalityOperator;
        this.right = right;
        this.left = left;
    }
}