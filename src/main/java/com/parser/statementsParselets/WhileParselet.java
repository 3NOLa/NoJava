package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.Statement;
import com.parser.statement.WhileStatement;

public class WhileParselet implements StatementsParselets {
    @Override
    public WhileStatement parse(Parser par) {
        par.cur.consume(Token.TokenType.KW_WHILE); //while statement starts with a while

        Expression condition = par.parseExpression(LedParselets.Precedence.START); //while condition
        Statement stmt = par.parseStatement();

        return new WhileStatement(condition.loc, stmt, condition);
    }
}
