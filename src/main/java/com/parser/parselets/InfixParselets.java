package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;

public interface InfixParselets extends Parselets{

    public enum precedence{
        LO_OR(1),
        LO_AND(2),
        BIT_OR(3),
        BIT_XOR(4),
        BIT_AND(5),
        EQUALITY(6),
        RELATIONAL(7),
        SHIFT(8),
        SUM(9),
        MUL(10),
        PREFIX(11),
        POSTFIX(12);

        public final int bp;

        precedence(int bp){
            this.bp = bp;
        }
    }

    public Expression parse(Parser par, Token t, precedence prec);
}