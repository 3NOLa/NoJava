package com.parser;

import com.lexer.Token;
import com.parser.declaration.Declaration;
import com.parser.declaration.ImportDeclaration;
import com.semantic.ASTVisitor;
import com.semantic.StatementVisitor;
import com.util.location;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnit extends ASTnode{

    public Token packageName;                   // may be null
    public List<ImportDeclaration> imports = new ArrayList<>();
    public List<Declaration> types = new ArrayList<>(); // classes, interfaces

    public CompilationUnit(Token packageName , List<ImportDeclaration> imports, List<Declaration> types) {
        super(new location(0,0));
        this.packageName = packageName;
        this.imports = imports;
        this.types = types;
    }

    public CompilationUnit(List<ImportDeclaration> imports, List<Declaration> types) {
        super(new location(0,0));
        this.packageName = null;
        this.imports = imports;
        this.types = types;
    }

    public CompilationUnit() {
        super(new location(0,0));
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        if(this.packageName != null){
            sb.append(indent(depth)).append("  Package name:\n");
            sb.append(indent(depth + 2)).append(this.packageName.value).append("\n");
        }

        sb.append(indent(depth)).append("  Imports:\n");

        if(!this.imports.isEmpty()) {
            for (ImportDeclaration imp : this.imports) {
                sb.append(imp.toStringHelper(depth + 2)).append("\n");
            }
        }

        if (!this.types.isEmpty()){
            sb.append(indent(depth)).append("  File Declarations:\n");
            for(Declaration decl : this.types){
                sb.append(decl.toStringHelper(depth + 2)).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

