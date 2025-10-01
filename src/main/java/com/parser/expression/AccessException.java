package com.parser.expression;

import com.util.location;

public class AccessException extends Expression {

    public Expression access;
    public Expression member;

    public AccessException(location loc, Expression access, Expression member) {
        super(loc);
        this.access = access;
        this.member = member;
    }

    @Override
    public String toStringHelper(int depth) {
        return "";
    }

    public String toString() {

        return super.toString() + getClass().getSimpleName() + "\naccess: "  + access.toString() + "\nmember: " + member.toString() ;
    }
}
