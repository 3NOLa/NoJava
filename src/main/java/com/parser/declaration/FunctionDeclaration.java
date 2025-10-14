package com.parser.declaration;

import com.lexer.Token;
import com.parser.Type;
import com.parser.statement.Statement;
import com.semantic.SemanticError;
import com.semantic.StatementVisitor;
import com.util.location;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FunctionDeclaration extends Declaration{

    public HashSet<Token.TokenType> Modifiers;
    public String funcName;
    public Type returnType;
    public List<VariableDeclaration> args;
    public Statement body;

    public FunctionDeclaration(location loc, HashSet<Token.TokenType> Modifiers, String funcName,
                               Type returnType, List<VariableDeclaration> args, Statement body) {
        super(loc);
        this.Modifiers = Modifiers;
        this.funcName = funcName;
        this.returnType = returnType;
        this.args = args;
        this.body = body;


    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    public boolean sameSignature(Object o){
        if (!(o instanceof FunctionDeclaration func)){
            throw new RuntimeException("Object isn't a DeclarationFunction");
        }

        return funcName.equals(func.funcName) && returnType.equals(func.returnType) && args.equals(func.args) && Modifiers.equals(func.Modifiers);
    }

    public boolean overLoading(Object o){
        if (!(o instanceof FunctionDeclaration func)){
            throw new RuntimeException("Object isn't a DeclarationFunction");
        }

        return funcName.equals(func.funcName) && returnType.equals(func.returnType) && args.equals(func.args) && !Modifiers.equals(func.Modifiers);
    }

    public boolean sameSignatureAbstract(Object o){
        if (!(o instanceof FunctionDeclaration func)){
            throw new RuntimeException("Object isn't a DeclarationFunction");
        }

        //if (func.Modifiers.contains(Token.TokenType.KW_ABSTRACT) )

        return funcName.equals(func.funcName) && returnType.equals(func.returnType) && args.equals(func.args);
    }

    @Override
    public String toStringHelper(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(depth)).append(getClass().getSimpleName()).append("\n");

        sb.append(indent(depth)).append("  function name:\n");
        sb.append(indent(depth + 2)).append(funcName).append("\n");

        sb.append(indent(depth)).append("  function accessModifiers:\n");

        sb.append(indent(depth + 2));
        for(Token.TokenType mod : this.Modifiers){
            sb.append(mod.name() + ", ");
        }
        sb.append("\n");

        sb.append(indent(depth)).append("  function returnType:\n");
        sb.append(indent(depth + 2)).append(returnType.getCanonicalName()).append("\n");

        sb.append(indent(depth)).append("  function arguments:\n");
        for(VariableDeclaration arg : args){
            sb.append(arg.toStringHelper(depth+2));
        }

        if (body != null) {
            sb.append(indent(depth)).append("  body:\n");
            sb.append(body.toStringHelper(depth + 2));
        }
        return sb.toString();
    }
}
