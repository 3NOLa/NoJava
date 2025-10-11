package com.parser.expression;

import com.semantic.ASTVisitor;
import com.semantic.ExpressionVisitor;
import com.util.location;

public class AccessExpression extends Expression {

    public Expression access;
    public Expression member;

    public AccessExpression(location loc, Expression access, Expression member) {
        super(loc);
        this.access = access;
        this.member = member;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        return "";
    }

    public String toString() {

        return super.toString() + getClass().getSimpleName() + "\naccess: "  + access.toString() + "\nmember: " + member.toString() ;
    }
}
