package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.DoWhileStatement;
import com.parser.statement.Statement;

public class DoWhileParselet implements StatementsParselets {
    @Override
    public DoWhileStatement parse(Parser par) {
        par.cur.consume(Token.TokenType.KW_DO); //DOwhile statement starts with a DO

        Statement stmt = par.parseStatement();
        par.cur.consume(Token.TokenType.KW_WHILE); // MUST HAVE WHILE KEYWORD AT THE END;
        Expression condition = par.parseExpression(LedParselets.Precedence.START);

        return new DoWhileStatement(stmt.loc, stmt, condition);
    }
}
