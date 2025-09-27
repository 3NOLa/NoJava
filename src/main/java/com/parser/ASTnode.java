package com.parser;

import com.util.location;

public abstract class ASTnode{
    public location loc;

    public ASTnode(location loc){
        this.loc = loc;
    }

    @Override
    public String toString(){
        return "(AST node, location: " + loc.toString() + ", ";
    }

    //public abstract <T> T accept();
}
