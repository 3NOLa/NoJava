package com.parser.declaration.parselets;

import com.lexer.Token;
import com.parser.ParseException;
import com.parser.Parser;
import com.parser.Type;
import com.parser.declaration.Declaration;
import com.parser.declaration.VariableDeclaration;
import com.parser.expression.AssignmentExpression;
import com.parser.expression.Expression;
import com.parser.expression.PrimaryExpression;
import com.parser.parselets.LedParselets;

public class VariableDeclarationParselet implements DeclarationParselet{

    @Override
    public Declaration parse(Parser par) {
        Token varType = par.cur.get(); // e.g. 'int'
        Token identifier = par.cur.get(Token.TokenType.IDENTIFIER);

        Expression initializer = null;
        if (par.cur.match(Token.TokenType.OP_ASSIGN)) {
            initializer = par.parseExpression(LedParselets.Precedence.ASSIGNMENT); //im sending ASSIGNMENT Precedence because i will get only the right side of ana assignment;
        }

        //par.cur.consume(Token.TokenType.SEMICOLON); not always ends with semicolon exampole func(int x, int y)

        return new VariableDeclaration(varType.loc, new Type(varType.type, varType.value), identifier.value, initializer);
    }
}
