package com.parser.expression;

import com.lexer.Token;
import com.util.location;

public class PrimaryExpression extends Expression{

    public Token.TokenType type;
    public String strValue;

    public PrimaryExpression(location loc, Token.TokenType type, String strValue){
        super(loc);
        this.type = type;
        this.strValue = strValue;
    }

    @Override
    public String toStringHelper(int depth) {
        return indent(depth) + "PrimaryExpression(" + type + ", " + strValue + ")\n";
    }
}
