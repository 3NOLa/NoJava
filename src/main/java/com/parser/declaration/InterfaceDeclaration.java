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

public class InterfaceDeclaration extends Declaration implements Vertex{

    public HashSet<Token.TokenType> Modifiers;
    public String InterfaceName;
    public Declaration body;
    public List<Token> superinterface;
    public List<FunctionDeclaration> superFunctions;

    public InterfaceDeclaration(location loc, HashSet<Token.TokenType> Modifiers, String InterfaceName, Declaration body, List<Token> superinterface) {
        super(loc);
        this.Modifiers = Modifiers;
        this.InterfaceName = InterfaceName;
        this.body = body;
        this.superinterface = superinterface;
        this.superFunctions = getFunctions();
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    public BlockDeclaration getDeclarationList(){
        return (BlockDeclaration) this.body;
    }

    @Override
    public List<Vertex> getEdges(HashMap<String, Vertex> vertices) {
        List<Vertex> superDeclarations = new ArrayList<>();
        for (Token t : this.superinterface){
            if (!vertices.containsKey(t.value)){
                throw new SemanticError("Super Interface: " + t.value + " isn't declared from interface: " + this.InterfaceName);
            }
            superDeclarations.add(vertices.get(t.value));
        }
        return superDeclarations;
    }

    @Override
    public ASTnode getValue() {
        return this;
    }

    public List<FunctionDeclaration> getFunctions(){
        List<FunctionDeclaration> funcs = new ArrayList<>();

        BlockDeclaration block = getDeclarationList();
        for (Declaration decl : block.body){
            if (decl instanceof FunctionDeclaration f){
                funcs.add(f);
            }
        }
        return funcs;
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(indent(depth)).append("  interface name:\n");
        sb.append(indent(depth + 2)).append(InterfaceName).append("\n");

        sb.append(indent(depth)).append("  interface Modifiers:\n");

        sb.append(indent(depth + 2));
        for(Token.TokenType mod : this.Modifiers){
            sb.append(mod.name() + ", ");
        }
        sb.append("\n");

        if (!this.superinterface.isEmpty()){
            sb.append(indent(depth)).append("  super interface:\n");
            sb.append(indent(depth + 2));
            for(Token inter : this.superinterface){
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
