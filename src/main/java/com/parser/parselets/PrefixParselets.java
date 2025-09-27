package com.parser.parselets;

import com.lexer.Token;
import com.parser.expression.Expression;

public interface PrefixParselets extends Parselets{
    public Expression parse(Token.TokenType type);
}
