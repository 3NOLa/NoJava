package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;

public interface ledParselets extends Parselets{
    public Expression parse(Parser par, Token t);
}
