package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class PrimaryExpression extends Expression{

    Token.TokenType type;

    public PrimaryExpression(location loc) {
        super(loc);
    }
}
