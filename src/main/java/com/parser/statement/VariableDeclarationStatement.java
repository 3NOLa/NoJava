package com.parser.statement;

import com.lexer.Token;
import com.parser.declaration.VariableDeclaration;
import com.parser.expression.Expression;
import com.util.location;

public class VariableDeclarationStatement extends Statement{

    VariableDeclaration dec;

    public VariableDeclarationStatement(VariableDeclaration dec)  {
        super(dec.loc);
        this.dec = dec;
    }

    public VariableDeclarationStatement(location loc, Token.TokenType type, String name, Expression initializer)  {
        super(loc);
        this.dec = new VariableDeclaration(loc, type, name, initializer);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(dec.toStringHelper(depth + 2));

        return sb.toString();
    }
}
