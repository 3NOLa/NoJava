package com.parser.declaration;

import com.lexer.Token;
import com.semantic.ASTVisitor;
import com.semantic.StatementVisitor;
import com.util.location;

import java.util.List;

public class ImportDeclaration extends Declaration{

    public List<Token> path;    // e.g. [java, util, List]
    public boolean isWildcard;  // true if import java.util.*;

    public ImportDeclaration(location loc) {
        super(loc);
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(indent(depth)).append("  Import Path:\n");
        sb.append(indent(depth + 2));

        for(Token t : this.path){
            sb.append(t.value + ".");
        }
        sb.append("\n");

        return sb.toString();
    }
}

