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
}
