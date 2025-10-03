package com.parser.parselets;

import com.lexer.Token;
import com.parser.Grammer;
import com.parser.ParseException;
import com.parser.Parser;
import com.parser.expression.ConditionalExpression;
import com.parser.expression.EqualityExpression;
import com.parser.expression.Expression;

public class ConditionalParselet implements LedParselets{
    @Override
    public Expression parse(Parser par, Token t, Expression cond) {
        Expression then = par.parseExpression(Grammer.mapBp.get(t.type));
        par.cur.consume(Token.TokenType.COLON);//get reed of the : in the cond (()? () : ();) IF NOT THROW EXCEPTION
        Expression _else = par.parseExpression(Grammer.mapBp.get(t.type));
        return new ConditionalExpression(t.loc, cond, then, _else);
    }
}
