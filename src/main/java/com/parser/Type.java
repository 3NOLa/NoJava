package com.parser;

import com.lexer.Token;

import java.util.Map;
import java.util.Objects;

public class Type {

    public final Token.TokenType tokenType;
    private final String name;

    private static final Map<Token.TokenType, String> PRIMITIVE_MAP = Map.of(
            Token.TokenType.INT_LITERAL, "int",
            Token.TokenType.CHAR_LITERAL, "char",
            Token.TokenType.FLOAT_LITERAL, "float",
            Token.TokenType.TRUE_LITERAL, "boolean",
            Token.TokenType.FALSE_LITERAL, "boolean",
            Token.TokenType.STRING_LITERAL, "string",
            Token.TokenType.KW_VOID, "void"
    );

    public Type(Token.TokenType tokenType, String name) {
        this.tokenType = tokenType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return PRIMITIVE_MAP.containsKey(tokenType);
    }

    public String getCanonicalName() {
        return isPrimitive() ? PRIMITIVE_MAP.get(tokenType) : name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Type other)) return false;
        return Objects.equals(this.getCanonicalName(), other.getCanonicalName());
    }

    @Override
    public String toString() {
        return "Type{name='" + getCanonicalName() + "', tokenType=" + tokenType + "}";
    }
}
