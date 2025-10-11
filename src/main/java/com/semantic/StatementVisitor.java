package com.semantic;

import com.parser.CompilationUnit;
import com.parser.declaration.*;
import com.parser.statement.*;

public interface StatementVisitor{
    // ===== Top-level =====
    void visit(CompilationUnit node);

    void visit(FunctionDeclaration node);

    void visit(VariableDeclaration node);

    void visit(InterfaceDeclaration node);

    void visit(ImportDeclaration node);

    void visit(BlockDeclaration node);

    void visit(ClassDeclaration node);

    // ===== Statements =====
    void visit(BlockStatement node);

    void visit(IfStatement node);

    void visit(WhileStatement node);

    void visit(DoWhileStatement node);

    void visit(ForStatement node);

    void visit(ReturnStatement node);

    void visit(ExpressionStatement node);

    void visit(VariableDeclarationStatement node);
}
