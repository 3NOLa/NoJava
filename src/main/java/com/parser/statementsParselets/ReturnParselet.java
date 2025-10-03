package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.ReturnStatement;

public class ReturnParselet implements StatementsParselets {
    @Override
    public ReturnStatement parse(Parser par) {
        par.cur.consume(Token.TokenType.KW_RETURN);
        Expression value = par.parseExpression(LedParselets.Precedence.START);
        return new ReturnStatement(value.loc, value);
    }
}
