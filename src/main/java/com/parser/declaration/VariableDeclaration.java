package com.parser.declaration;

import com.lexer.Token;
import com.parser.expression.Expression;
import com.util.location;

public class VariableDeclaration extends Declaration{

    public Token.TokenType type;
    public String name;
    public Expression initializer;

    public VariableDeclaration(location loc, Token.TokenType type, String name) {
        super(loc);
        this.type = type;
        this.name = name;
        this.initializer = null;
    }
    
    public VariableDeclaration(location loc, Token.TokenType type, String name, Expression initializer) {
        super(loc);
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(indent(depth)).append("  variable type:\n");
        sb.append(indent(depth + 2)).append(type.name()).append("\n");
        
        sb.append(indent(depth)).append("  variable name:\n");
        sb.append(indent(depth + 2)).append(name).append("\n");
        
        if (initializer != null) {
            sb.append(indent(depth)).append("  initializer:\n");
            sb.append(initializer.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
