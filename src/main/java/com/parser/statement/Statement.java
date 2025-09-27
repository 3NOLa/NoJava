package com.parser.statement;

import com.parser.ASTnode;
import com.util.location;

public abstract class Statement extends ASTnode {

    public Statement(location loc){
        super(loc);
    }
}
