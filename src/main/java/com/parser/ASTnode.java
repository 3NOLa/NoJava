package com.parser;

import com.util.location;

public abstract class ASTnode{
    public location loc;

    public ASTnode(location loc){
        this.loc = loc;
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
