package com.parser.declaration;

import com.parser.statement.Statement;
import com.util.location;

import java.util.ArrayList;
import java.util.List;

public class BlockDeclaration extends Declaration{

    public List<Declaration> body = new ArrayList<>();

    public BlockDeclaration(location loc, List<Declaration> body) {
        super(loc);
        this.body = body;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        for(Declaration dec : body){
            sb.append(dec.toStringHelper(depth+2));
        }
        return sb.toString();
    }
}
