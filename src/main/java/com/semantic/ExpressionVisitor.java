package com.semantic;

import com.parser.expression.*;

public interface ExpressionVisitor<T> {
    // ===== Expressions =====
    T visit(AssignmentExpression node);

    T visit(AccessExpression node);

    T visit(CallExpression node);

    T visit(ConditionalExpression node);

    T visit(EqualityExpression node);

    T visit(NewExpression node);

    T visit(OperatorExpression node);

    T visit(PostfixExpression node);

    T visit(PrefixExpression node);

    T visit(PrimaryExpression node);
}
