package com.semantic;

import com.parser.CompilationUnit;
import com.parser.declaration.*;
import com.parser.expression.*;
import com.parser.statement.*;


// The visitor interface defines one "visit" method per concrete AST node type.
// This lets the visitor pattern achieve double dispatch — the right visit()
// is called automatically based on both the node and the visitor type.

public interface ASTVisitor <T> extends StatementVisitor, ExpressionVisitor<T>{


}

