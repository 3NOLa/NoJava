package com.parser.statementsParselets;

import com.lexer.Token;
import com.parser.Parser;
import com.parser.declaration.VariableDeclaration;
import com.parser.expression.Expression;
import com.parser.parselets.LedParselets;
import com.parser.statement.Statement;
import com.parser.statement.VariableDeclarationStatement;

public class VariableDecStmtParselet implements StatementsParselets {

    public VariableDeclaration parseVar(Parser par){
        Token varType = par.cur.get(); // e.g. 'int'
        Token identifier = par.cur.get(Token.TokenType.IDENTIFIER);

        Expression initializer = null;
        if (par.cur.match(Token.TokenType.OP_ASSIGN)) {
            initializer = par.parseExpression(LedParselets.Precedence.ASSIGNMENT); //im sending ASSIGNMENT Precedence because i will get only the right side of ana assignment;
        }
        return new VariableDeclaration(varType.loc, varType.type, identifier.value, initializer);
    }

    @Override
    public Statement parse(Parser par) {
        VariableDeclaration var = parseVar(par);
        par.cur.consume(Token.TokenType.SEMICOLON);
        return new VariableDeclarationStatement(var);
    }
}
