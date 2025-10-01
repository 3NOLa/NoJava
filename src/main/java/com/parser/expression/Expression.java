package com.parser.expression;

import com.parser.ASTnode;
import com.util.location;

public abstract class Expression extends ASTnode {

    public abstract String toStringHelper(int depth);


    public Expression(location loc){
        super(loc);
    }

    //public abstract void eval();

    @Override
    public String toString() {
        return toStringHelper(0);
    }

    protected String indent(int depth) {
        return "  ".repeat(depth);
    }
}
