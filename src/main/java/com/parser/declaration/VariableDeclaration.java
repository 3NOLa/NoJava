package com.parser.declaration;

import com.lexer.Token;
import com.parser.Type;
import com.parser.expression.Expression;
import com.semantic.ASTVisitor;
import com.semantic.StatementVisitor;
import com.util.location;

public class VariableDeclaration extends Declaration{

    public Type type;
    public String name;
    public Expression initializer;

    public VariableDeclaration(location loc, Type type, String name) {
        super(loc);
        this.type = type;
        this.name = name;
        this.initializer = null;
    }
    
    public VariableDeclaration(location loc, Type type, String name, Expression initializer) {
        super(loc);
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VariableDeclaration var))
            throw new RuntimeException("VariableDeclaration equals dgot another type");
        return name.equals(var.name);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(indent(depth)).append("  variable type:\n");
        sb.append(indent(depth + 2)).append(type.getCanonicalName()).append("\n");
        
        sb.append(indent(depth)).append("  variable name:\n");
        sb.append(indent(depth + 2)).append(name).append("\n");
        
        if (initializer != null) {
            sb.append(indent(depth)).append("  initializer:\n");
            sb.append(initializer.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
