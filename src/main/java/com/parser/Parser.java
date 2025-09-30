package com.parser;

import com.lexer.Token;
import com.parser.expression.Expression;
import com.parser.parselets.nudParselets;
import com.parser.parselets.ledParselets;
import java.util.HashMap;

public class Parser {

    private final TokenCurser cur;
    private final HashMap<Token.TokenType, nudParselets> mapInifix = new HashMap<>();
    private final HashMap<Token.TokenType, ledParselets> mapPrefix = new HashMap<>();
    public final HashMap<Token.TokenType, nudParselets.precedence> mapBp = new HashMap<>(); // bp = binding power


    public Parser(TokenCurser cur){
        this.cur = cur;
    }

    public Expression parseExpression(nudParselets.precedence prec){
        Token t = cur.get();

        ledParselets PrefixFunc = mapPrefix.get(t.type);
        if (PrefixFunc == null){
            throw new ParseException("Expression doesn't start with a prefix token", t);
        }
        Expression left = PrefixFunc.parse(this, t);

        while(prec.bp < mapBp.get(cur.peek().type).bp) {
            Token Inft = cur.get();
            nudParselets InfixFunc = mapInifix.get(Inft.type);
            if (InfixFunc == null) {
                throw new ParseException("Expression doesn't continue with a Inifix token", Inft);
            }
            left = InfixFunc.parse(this, Inft, left);
        }
        return left;
    }

    private void populateMaps(){

    }
}
