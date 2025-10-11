package com.parser.declaration;

import com.lexer.Token;
import com.semantic.ASTVisitor;
import com.semantic.StatementVisitor;
import com.util.location;

import java.util.List;

public class InterfaceDeclaration extends Declaration{

    public Token.TokenType[] Modifiers;
    public String InterfaceName;
    public Declaration body;
    public List<Token> superinterface;

    public InterfaceDeclaration(location loc, Token.TokenType[] Modifiers, String InterfaceName, Declaration body, List<Token> superinterface) {
        super(loc);
        this.Modifiers = Modifiers;
        this.InterfaceName = InterfaceName;
        this.body = body;
        this.superinterface = superinterface;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    public BlockDeclaration getDeclarationList(){
        return (BlockDeclaration) this.body;
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
