package com.parser.expression;

import com.parser.ASTnode;
import com.util.location;

public abstract class Expression extends ASTnode {

    public Expression(location loc){
        super(loc);
    }

    @Override
    public String toString() {
        return super.toString() + "Expression: ";
    }
}
