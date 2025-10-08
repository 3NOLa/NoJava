package com.parser.parselets;

import com.lexer.Token;
import com.parser.Grammer;
import com.parser.Parser;
import com.parser.expression.CallExpression;
import com.parser.expression.Expression;
import com.parser.expression.NewExpression;
import com.parser.expression.PrimaryExpression;

import java.util.ArrayList;
import java.util.List;

public class NewParselet implements NudParselets {
    @Override
    public Expression parse(Parser par, Token newToken) {

        Token id = par.cur.get(Token.TokenType.IDENTIFIER);
        Expression primary = new PrimaryExpression(id.loc, id.type, id.value);
        par.cur.consume(Token.TokenType.LPAREN);
        Expression call = Grammer.CALL.parse(par, newToken, primary);

        return new NewExpression(newToken.loc, call);
    }
}
