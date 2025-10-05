package com.parser.declaration;

import com.lexer.Token;
import com.parser.statement.Statement;
import com.util.location;

import java.util.List;

public class FunctionDeclaration extends Declaration{

    public Token.TokenType[] Modifiers;
    public String funcName;
    public Token.TokenType returnType;
    public List<VariableDeclaration> args;
    public Statement body;

    public FunctionDeclaration(location loc, Token.TokenType[] Modifiers, String funcName,
                               Token.TokenType returnType, List<VariableDeclaration> args, Statement body) {
        super(loc);
        this.Modifiers = Modifiers;
        this.funcName = funcName;
        this.returnType = returnType;
        this.args = args;
        this.body = body;
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
        sb.append(indent(depth + 2)).append(returnType.name()).append("\n");

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
