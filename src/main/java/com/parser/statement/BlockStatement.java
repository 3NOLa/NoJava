package com.parser.statement;

import com.util.location;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement{

    List<Statement> statements = new ArrayList<>();

    public BlockStatement(location loc, List<Statement> statements) {
        super(loc);
        this.statements = statements;
    }
}
