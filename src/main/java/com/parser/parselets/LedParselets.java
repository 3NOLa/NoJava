package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;

public interface LedParselets extends Parselets{

    public enum Precedence {
        PRIMARY(0),
        CALL(1),        // function calls, array access, member access
        POSTFIX(2),     // postfix ++, --
        PREFIX(3),      // prefix ++, --, unary +, -, !
        PRODUCT(4),     // *, /
        SUM(5),         // +, -
        SHIFT(6),       // <<, >>
        RELATIONAL(7),  // <, >, <=, >=
        EQUALITY(8),    // ==, !=
        BITWISE(9),     // &, ^, |
        LOGICAL(10),    // &&, ||
        CONDITIONAL(11),// ?:
        ASSIGNMENT(12), // =, +=, -= ...
        EOF(13);

        public final int bp;

        Precedence(int bp){
            this.bp = bp;
        }
    }

    public Expression parse(Parser par, Token t, Expression left);
}