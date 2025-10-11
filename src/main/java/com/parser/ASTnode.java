package com.parser;

import com.parser.expression.AssignmentExpression;
import com.semantic.ASTVisitor;
import com.semantic.ExpressionVisitor;
import com.semantic.StatementVisitor;
import com.util.location;

public abstract class ASTnode{
    public location loc;

    public ASTnode(location loc){
        this.loc = loc;
    }

    public <T> T accept(ExpressionVisitor<T> visitor) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " is not an expression"
        );
    }

    public void accept(StatementVisitor visitor) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " is not a statement or declaration"
        );
    }

    @Override
    public String toString(){
        return toStringHelper(0);
    }

    public abstract String toStringHelper(int depth);

    protected String indent(int depth) {
        return "  ".repeat(depth);
    }

}
