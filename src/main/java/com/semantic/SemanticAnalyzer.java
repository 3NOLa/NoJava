package com.semantic;

import com.lexer.Token;
import com.parser.CompilationUnit;
import com.parser.Type;
import com.parser.declaration.*;
import com.parser.expression.*;
import com.parser.statement.*;
import com.util.TopologicalSort;
import com.util.Vertex;

import java.util.*;

public class SemanticAnalyzer implements ASTVisitor<Type>{

    public final SymbolTable scopes = new SymbolTable();
    private FunctionDeclaration currentFunction = null;
    private ClassDeclaration currentClass = null;

    private final HashMap<String, Vertex> superDeclarations = new HashMap<>();
    private final TopologicalSort topo = new TopologicalSort();

    private final List<FunctionDeclaration> superFunctions = new ArrayList<>();

    @Override
    public void visit(CompilationUnit node) {
        for(ImportDeclaration imp : node.imports){
            imp.accept((StatementVisitor) this);
        }

        scopes.enterScope("CompilationUnit");

        for(Declaration decl : node.types){
            if (decl instanceof ClassDeclaration clas){
                scopes.declare(clas.className, new Symbol(clas.className, new Type(Token.TokenType.IDENTIFIER, clas.className), false, clas));
                superDeclarations.put(clas.className, clas);

            }else if (decl instanceof InterfaceDeclaration inter){
                scopes.declare(inter.InterfaceName, new Symbol(inter.InterfaceName, new Type(Token.TokenType.IDENTIFIER, inter.InterfaceName), false, inter));
                superDeclarations.put(inter.InterfaceName, inter);
            }
        }

        topo.sort(this.superDeclarations);
        Set<Vertex> superSorted = topo.getSorted();

        for (Vertex v : superSorted){
            v.getValue().accept((StatementVisitor) this);
        }
    }

    @Override
    public void visit(FunctionDeclaration node) {
        if (this.currentClass != null){
            if (this.currentClass.Modifiers.contains(Token.TokenType.KW_ABSTRACT)){
                if (node.Modifiers.contains(Token.TokenType.KW_ABSTRACT) && node.body != null){
                    throw new SemanticError("cant be a body for a abstract function", node.loc);
                }
            }else {
                if (node.Modifiers.contains(Token.TokenType.KW_ABSTRACT))
                    throw new SemanticError("cant be an abstract function in a non abstract class", node.loc);
            }
        }

        this.currentFunction = (FunctionDeclaration) scopes.resolve(node.funcName).node;
        scopes.enterScope(node.funcName);


        for(VariableDeclaration var : node.args)
            var.accept((StatementVisitor) this);

        if (node.body != null && !currentFunction.returnType.getCanonicalName().equals("void")){
            BlockStatement stmtens = (BlockStatement) node.body;
            boolean returnFlag = false;
            for(Statement stmt : stmtens.statements){
                if (stmt instanceof ReturnStatement)
                    returnFlag = true;
                stmt.accept((StatementVisitor)this);
            }
            if (!returnFlag)
                throw new SemanticError("Function: " + currentFunction.funcName + " doesnt have a return statement");
        }

        scopes.exitScope();
        this.currentFunction = null;
    }

    @Override
    public void visit(VariableDeclaration node) {
        scopes.declare(node.name, new Symbol(node.name, node.type, false, node));

        if (node.initializer != null){
            Type initialize = node.initializer.accept((ExpressionVisitor<Type>) this);
            if (!node.type.equals(initialize))
                throw new SemanticError("type mismatch declaration var", node.loc);

        }
    }

    @Override
    public void visit(InterfaceDeclaration node) {
        scopes.enterScope(node.InterfaceName);
        if (node.body != null)
            node.body.accept((StatementVisitor) this);

        if (!node.superinterface.isEmpty()) {
            for (Token interToken : node.superinterface) {
                Symbol sym = scopes.resolve(interToken.value);

                if (!(sym.node instanceof InterfaceDeclaration superInter)) {
                    throw new SemanticError("Must implement an interface", interToken.loc);
                }

                node.superFunctions.addAll(getAllInterfaceFunctions(superInter));
            }
        }
    }

    private List<FunctionDeclaration> getAllInterfaceFunctions(InterfaceDeclaration interDecl) {
        List<FunctionDeclaration> allFuncs = new ArrayList<>(interDecl.getFunctions());

        for (Token superTok : interDecl.superinterface) {
            Symbol superSym = scopes.resolve(superTok.value);
            if (superSym.node instanceof InterfaceDeclaration superSuperInter) {
                allFuncs.addAll(getAllInterfaceFunctions(superSuperInter)); // recursive
            }
        }

        return allFuncs;
    }


    @Override
    public void visit(ImportDeclaration node) {
        //scopes.enterScope();

        StringBuilder sb = new StringBuilder();

    }


    @Override
    public void visit(BlockDeclaration node) {
        for(Declaration decl : node.body){
            if (decl instanceof FunctionDeclaration func){
                scopes.declare(func.funcName,  new Symbol(func.funcName, func.returnType, true, func));
            }
            else
                decl.accept((StatementVisitor)this);
        }

        for (Symbol sym : scopes.getCurrentScope().values()){
            if (sym.isFunction)
                sym.node.accept((StatementVisitor)this);
        }
    }

    @Override
    public void visit(ClassDeclaration node) {
        scopes.enterScope(node.className);
        this.currentClass = node;
        if (node.body != null)
            node.body.accept((StatementVisitor) this);

        if (!node.interfaces.isEmpty()){
            for (Token interToken : node.interfaces){
                Symbol sym = scopes.resolve(interToken.value);

                if (!(sym.node instanceof InterfaceDeclaration inter)){
                    throw new SemanticError("Must implement an interface" ,interToken.loc);
                }

                this.scopes.printScopes();

                for (FunctionDeclaration interSuperFunc : inter.superFunctions){
                    Symbol funcSym = scopes.getLocalTable(node.className).get(interSuperFunc.funcName);
                    if ( interSuperFunc.body == null){ //if interface function doesnt have a body else it doesnt must override it
                        if (funcSym == null) {
                            throw new SemanticError("Must implement function: " + interSuperFunc.funcName + " from interface: " + inter.InterfaceName);
                        }
                        if (!interSuperFunc.sameSignature(funcSym.node) || ((FunctionDeclaration)funcSym.node).body == null){
                            throw new SemanticError("Must have same function Signature and full body: " + (FunctionDeclaration)funcSym.node, funcSym.node.loc);
                        }
                    }
                }
            }
        }
        this.currentClass = null;
    }


    @Override
    public void visit(BlockStatement node) {
        scopes.enterScope("BlockStatement " + node.loc);

        for(Statement stmt : node.statements){
            stmt.accept((StatementVisitor)this);
        }
        scopes.exitScope();
    }

    @Override
    public void visit(IfStatement node) {
        node.condition.accept((ExpressionVisitor<Type>)this);

        scopes.enterScope("IfStatement");
        node.then.accept((StatementVisitor)this);
        scopes.exitScope();

        if (node._else != null){
            scopes.enterScope("elseStatemnet");
            node._else.accept((StatementVisitor)this);
            scopes.exitScope();
        }
    }

    @Override
    public void visit(WhileStatement node) {
        node.condition.accept((ExpressionVisitor<Type>)this);

        scopes.enterScope("WhileStatement");
        node.body.accept((StatementVisitor)this);
        scopes.exitScope();
    }

    @Override
    public void visit(DoWhileStatement node) {
        node.condition.accept((ExpressionVisitor<Type>)this);

        scopes.enterScope("DoWhileStatement");
        node.body.accept((StatementVisitor)this);
        scopes.exitScope();
    }

    @Override
    public void visit(ForStatement node) {
        node.init.accept((StatementVisitor)this);
        node.condition.accept((ExpressionVisitor<Type>)this);
        node.update.accept((ExpressionVisitor<Type>)this);

        scopes.enterScope("ForStatement");
        node.body.accept((StatementVisitor)this);
        scopes.exitScope();
    }

    @Override
    public void visit(ReturnStatement node) {
        Type returnType = node.value.accept((ExpressionVisitor<Type>) this);
        if (!returnType.equals(this.currentFunction.returnType)){
            throw new SemanticError("Function " + currentFunction.funcName + " doesn't return correct type , supposed function type: " + currentFunction.returnType, node.loc);
        }
    }

    @Override
    public void visit(ExpressionStatement node) {
        node.expr.accept((ExpressionVisitor<Type>) this);
    }

    @Override
    public void visit(VariableDeclarationStatement node) {
        node.dec.accept((StatementVisitor) this);
    }

    @Override
    public Type visit(AssignmentExpression node) {
        Type assigneeType = node.assignee.accept((ExpressionVisitor<Type>) this);
        Type assignedType = node.assigned.accept((ExpressionVisitor<Type>) this);

        if (!assigneeType.equals(assignedType))
            throw new SemanticError("assigneeType assignedType arent the same", node.loc);
        return assigneeType;
    }

    @Override
    public Type visit(AccessExpression node) {
        return null;
    }


    @Override
    public Type visit(CallExpression node) {
        Symbol sym = scopes.resolve(((PrimaryExpression)(node.func)).strValue, node.loc);
        FunctionDeclaration func = (FunctionDeclaration)sym.node;

        if(func.args.size() != node.args.size())
                throw new SemanticError("function declaration and call doesn't have the same amount of args", node.loc);

        for (int i = 0; i < func.args.size(); i++) {
            Type arg = node.args.get(i).accept((ExpressionVisitor<Type>) this);
            if (!func.args.get(i).type.equals(arg))
                throw new SemanticError("Type mismatch: expected argument type " + func.args.get(i).type.getCanonicalName() + " but found " + arg.getCanonicalName() + " in function call.", node.loc);
        }

        return func.returnType;
    }

    @Override
    public Type visit(ConditionalExpression node) {
        node.condition.accept((ExpressionVisitor<Type>)this);
        Type thenType = node.then.accept((ExpressionVisitor<Type>) this);
        Type elseType = node.then.accept((ExpressionVisitor<Type>) this);

        if (!thenType.equals(elseType))
            throw new SemanticError("in tenray if right and left side isnt the same type", node.loc);

        return thenType;
    }

    

    @Override
    public Type visit(EqualityExpression node) {
        return null;
    }

    

    @Override
    public Type visit(NewExpression node) {
        return node.constructorCall.accept((ExpressionVisitor<Type>) this);
    }

    

    @Override
    public Type visit(OperatorExpression node) {
        Type rightType = node.right.accept((ExpressionVisitor<Type>) this);
        Type leftype = node.left.accept((ExpressionVisitor<Type>) this);

        if (!rightType.equals(leftype))
            throw new SemanticError("in Operator right and left side arent the same type", node.loc);

        return rightType;
    }

    

    @Override
    public Type visit(PostfixExpression node) {
        return node.left.accept((ExpressionVisitor<Type>) this);
    }

    

    @Override
    public Type visit(PrefixExpression node) {
        return node.right.accept((ExpressionVisitor<Type>) this);
    }

    

    @Override
    public Type visit(PrimaryExpression node) {
        if (node.type == Token.TokenType.IDENTIFIER)
            return scopes.resolve(node.strValue, node.loc).type;
        return new Type(node.type, node.type.name());
    }

    
}
