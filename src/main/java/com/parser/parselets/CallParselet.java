package com.parser.parselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.CallExpression;
import com.parser.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class CallParselet implements LedParselets {
    @Override
    public Expression parse(Parser parser, Token t, Expression left) {
        List<Expression> args = new ArrayList<>();
        if (!parser.cur.match(Token.TokenType.RPAREN)) {
            do {
                args.add(parser.parseExpression(Precedence.CALL));
            } while (parser.cur.match(Token.TokenType.COMMA));
            parser.cur.consume(Token.TokenType.RPAREN);
        }
        return new CallExpression(t.loc, left, args); // AST node for function call
    }
}

