package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.ForStatement;
import com.parser.statement.Statement;

public class ForParselet implements StatementsParselets {
    @Override
    public Statement parse(Parser par) {
        par.cur.consume(Token.TokenType.KW_FOR); //for statement starts with a for

        Statement init = par.parseStatement();
        Expression condition = par.parseExpression(LedParselets.Precedence.START); //while condition
        Expression update = par.parseExpression(LedParselets.Precedence.START);
        Statement stmt = par.parseStatement();

        return new ForStatement(init.loc, stmt, condition, update, init);

    }
}
