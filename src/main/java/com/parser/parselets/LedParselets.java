package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;

public interface LedParselets extends Parselets{

    public enum Precedence {
        START(-1),
        ASSIGNMENT(0),
        CONDITIONAL(1), // ?:
        LOGICAL(2),     // &&, ||
        BITWISE(3),     // &, ^, |
        EQUALITY(4),    // ==, !=
        RELATIONAL(5),  // <, >, <=, >=
        SHIFT(6),       // <<, >>
        SUM(7),         // +, -
        PRODUCT(8),     // *, /
        PREFIX(9),      // prefix ++, --, unary +, -, !
        CALL(10),       // function calls, array access, member access
        POSTFIX(11),    // postfix ++, --
        PRIMARY(12),
        EOF(13);
        // =, +=, -= ...

        public final int bp;

        Precedence(int bp){
            this.bp = bp;
        }
    }

    public Expression parse(Parser par, Token t, Expression left);
}