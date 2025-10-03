package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.IfStatement;
import com.parser.statement.Statement;

public class IfParselet implements StatementsParselets {
    @Override
    public IfStatement parse(Parser par) {
        par.cur.consume(Token.TokenType.KW_IF); //if statement starts with if keyword

        Expression condition = par.parseExpression(LedParselets.Precedence.START);
        Statement then = par.parseBlockStatement();
        if (par.cur.peek().type == Token.TokenType.KW_ELSE) {
            par.cur.consume(Token.TokenType.KW_ELSE);
            Statement _else = par.parseBlockStatement();
            return new IfStatement(then.loc, condition, then, _else);
        }
        return new IfStatement(then.loc, condition, then);
    }
}
