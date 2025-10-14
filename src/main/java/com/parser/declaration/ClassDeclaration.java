package com.parser.declaration;

import com.lexer.Token;
import com.parser.ASTnode;
import com.semantic.ASTVisitor;
import com.semantic.SemanticError;
import com.semantic.StatementVisitor;
import com.util.Vertex;
import com.util.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ClassDeclaration extends Declaration implements Vertex {

    public HashSet<Token.TokenType> Modifiers;
    public String className;
    public Token superClass;
    public Declaration body;
    public List<Token> interfaces;

    public ClassDeclaration(location loc, HashSet<Token.TokenType> Modifiers, String className, Declaration body) {
        super(loc);
        this.Modifiers = Modifiers;
        this.className = className;
        this.body = body;
        this.superClass = null;
    }


    public ClassDeclaration(location loc, HashSet<Token.TokenType> Modifiers, String className, Declaration body, Token superClass, List<Token> interfaces) {
        super(loc);
        this.Modifiers = Modifiers;
        this.className = className;
        this.body = body;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    public BlockDeclaration getBlockDeclaration() {
        return (BlockDeclaration) this.body;
    }

    @Override
    public List<Vertex> getEdges(HashMap<String, Vertex> vertices) {
        List<Vertex> superDeclarations = new ArrayList<>();
        for (Token t : this.interfaces){
            if (!vertices.containsKey(t.value)){
                throw new SemanticError("Interface: " + t.value + " isn't declared from class: " + this.className);
            }
            superDeclarations.add(vertices.get(t.value));
        }
        if (this.superClass != null){
            if (!vertices.containsKey(this.superClass.value)){
                throw new SemanticError("Super class: " + this.superClass.value + " isn't declared from class: " + this.className);
            }
            superDeclarations.add(vertices.get(this.superClass.value));
        }
        return superDeclarations;
    }

    @Override
    public ASTnode getValue() {
        return this;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(indent(depth)).append("  class name:\n");
        sb.append(indent(depth + 2)).append(className).append("\n");

        sb.append(indent(depth)).append("  class Modifiers:\n");
        sb.append(indent(depth + 2));
        for(Token.TokenType mod : this.Modifiers){
            sb.append(mod.name() + ", ");
        }
        sb.append("\n");

        if(this.superClass != null){
            sb.append(indent(depth)).append("  super class:\n");
            sb.append(indent(depth + 2)).append(className).append("\n");
        }

        if (!this.interfaces.isEmpty()){
            sb.append(indent(depth)).append("  class implements:\n");
            sb.append(indent(depth + 2));
            for(Token inter : this.interfaces){
                sb.append(inter.value + ", ");
            }
            sb.append("\n");
        }

        if (body != null) {
            sb.append(indent(depth)).append("  body:\n");
            sb.append(body.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
