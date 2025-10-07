package com.parser.declaration;

import com.lexer.Token;
import com.util.location;

import java.util.List;

public class ClassDeclaration extends Declaration{

    public Token.TokenType[] Modifiers;
    public String className;
    public Token superClass;
    public Declaration body;
    public List<Token> interfaces;

    public ClassDeclaration(location loc, Token.TokenType[] Modifiers, String className, Declaration body) {
        super(loc);
        this.Modifiers = Modifiers;
        this.className = className;
        this.body = body;
        this.superClass = null;
    }


    public ClassDeclaration(location loc, Token.TokenType[] Modifiers, String className, Declaration body, Token superClass, List<Token> interfaces) {
        super(loc);
        this.Modifiers = Modifiers;
        this.className = className;
        this.body = body;
        this.superClass = superClass;
        this.interfaces = interfaces;
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
