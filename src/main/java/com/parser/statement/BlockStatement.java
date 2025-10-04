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

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        for(Statement stm : statements){
            sb.append(stm.toStringHelper(depth+2));
        }
        return sb.toString();
    }
}
