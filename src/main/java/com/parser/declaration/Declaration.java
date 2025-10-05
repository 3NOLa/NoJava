package com.parser.declaration;

import com.parser.ASTnode;
import com.util.location;

public abstract class Declaration extends ASTnode {

    public Declaration(location loc) {
        super(loc);
    }
}
