package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;

public interface InfixParselets extends Parselets{

    public enum precedence{
        Assignment(1),
        Conditional(2),
        Logical(3),
        BITWISE(4),
        EQUALITY(5),
        RELATIONAL(6),
        SHIFT(7),
        SUM(8),
        MUL(9),
        PREFIX(10),
        POSTFIX(11);

        public final int bp;

        precedence(int bp){
            this.bp = bp;
        }
    }

    public Expression parse(Parser par, Token t, Expression left);
}