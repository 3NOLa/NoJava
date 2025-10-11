package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.ReturnStatement;

public class ReturnParselet implements StatementsParselets {
    @Override
    public ReturnStatement parse(Parser par) {
        Token returnToken = par.cur.get(Token.TokenType.KW_RETURN);
        Expression value = par.parseExpression(LedParselets.Precedence.START);
        par.cur.consume(Token.TokenType.SEMICOLON);
        return new ReturnStatement(returnToken.loc, value);
    }
}
