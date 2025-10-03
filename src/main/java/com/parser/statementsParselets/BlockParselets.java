package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.statement.BlockStatement;
import com.parser.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class BlockParselets implements StatementsParselets {
    @Override
    public BlockStatement parse(Parser par) {
        par.cur.consume(Token.TokenType.LBRACE); //block statement starts with a {

        List<Statement> stmts = new ArrayList<>();
        while (par.cur.peek().type != Token.TokenType.RBRACE) { //block statement ends with a }
            stmts.add(par.parseStatement());
        }
        par.cur.consume(Token.TokenType.RBRACE);

        return new BlockStatement(stmts.get(0).loc, stmts);
    }
}
