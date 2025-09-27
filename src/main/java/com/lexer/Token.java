package com.lexer;

import java.util.Map;

import com.util.location;

public class Token{

    public enum TokenType {
        // Identifiers & literals
        IDENTIFIER,
        INT_LITERAL,
        FLOAT_LITERAL,
        CHAR_LITERAL,
        STRING_LITERAL,
        TRUE_LITERAL,
        FALSE_LITERAL,
        NULL_LITERAL,

        // Keywords
        KW_IF,
        KW_ELSE,
        KW_WHILE,
        KW_FOR,
        KW_DO,
        KW_SWITCH,
        KW_CASE,
        KW_DEFAULT,
        KW_BREAK,
        KW_CONTINUE,
        KW_RETURN,
        KW_IMPORT,
        KW_PACKAGE,
        KW_PUBLIC,
        KW_PRIVATE,
        KW_PROTECTED,
        KW_CLASS,
        KW_INTERFACE,
        KW_ENUM,
        KW_EXTENDS,
        KW_IMPLEMENTS,
        KW_ABSTRACT,
        KW_FINAL,
        KW_STATIC,
        KW_VOID,
        KW_BOOLEAN,
        KW_BYTE,
        KW_SHORT,
        KW_INT,
        KW_LONG,
        KW_FLOAT,
        KW_DOUBLE,
        KW_CHAR,
        KW_STRING,
        KW_SUPER,
        KW_THIS,
        KW_NEW,
        KW_TRY,
        KW_CATCH,
        KW_FINALLY,
        KW_THROW,
        KW_THROWS,
        KW_SYNCHRONIZED,
        KW_VOLATILE,
        KW_TRANSIENT,
        KW_INSTANCEOF,
        KW_ASSERT,
        KW_NATIVE,
        KW_STRICTFP,

        // Operators
        OP_PLUS,        // +
        OP_MINUS,       // -
        OP_MUL,         // *
        OP_DIV,         // /
        OP_MOD,         // %
        OP_INC,         // ++
        OP_DEC,         // --
        OP_ASSIGN,      // =
        OP_ADD_ASSIGN,  // +=
        OP_SUB_ASSIGN,  // -=
        OP_MUL_ASSIGN,  // *=
        OP_DIV_ASSIGN,  // /=
        OP_MOD_ASSIGN,  // %=
        OP_EQ,          // ==
        OP_NEQ,         // !=
        OP_LT,          // <
        OP_GT,          // >
        OP_LTE,         // <=
        OP_GTE,         // >=
        OP_AND,         // &&
        OP_OR,          // ||
        OP_NOT,         // !
        OP_BIT_AND,     // &
        OP_BIT_OR,      // |
        OP_BIT_XOR,     // ^
        OP_BIT_NOT,     // ~
        OP_SHL,         // <<
        OP_SHR,         // >>
        OP_USHR,        // >>>
        OP_SHL_ASSIGN,  // <<=
        OP_SHR_ASSIGN,  // >>=
        OP_USHR_ASSIGN, // >>>=
        OP_AND_ASSIGN,  // &=
        OP_OR_ASSIGN,   // |=
        OP_XOR_ASSIGN,  // ^=

        // Punctuators / delimiters
        SEMICOLON,      // ;
        COLON,          // :
        COMMA,          // ,
        DOT,            // .
        LPAREN,         // (
        RPAREN,         // )
        LBRACE,         // {
        RBRACE,         // }
        LBRACKET,       // [
        RBRACKET,       // ]
        QUESTION,       // ?

        // Misc
        EOF,
        UNKNOWN
    }

    public static final Map<String, TokenType> KEYWORDS = Map.ofEntries(
        Map.entry("if", TokenType.KW_IF),
        Map.entry("else", TokenType.KW_ELSE),
        Map.entry("while", TokenType.KW_WHILE),
        Map.entry("for", TokenType.KW_FOR),
        Map.entry("do", TokenType.KW_DO),
        Map.entry("switch", TokenType.KW_SWITCH),
        Map.entry("case", TokenType.KW_CASE),
        Map.entry("default", TokenType.KW_DEFAULT),
        Map.entry("break", TokenType.KW_BREAK),
        Map.entry("continue", TokenType.KW_CONTINUE),
        Map.entry("return", TokenType.KW_RETURN),
        Map.entry("import", TokenType.KW_IMPORT),
        Map.entry("package", TokenType.KW_PACKAGE),
        Map.entry("public", TokenType.KW_PUBLIC),
        Map.entry("private", TokenType.KW_PRIVATE),
        Map.entry("protected", TokenType.KW_PROTECTED),
        Map.entry("class", TokenType.KW_CLASS),
        Map.entry("interface", TokenType.KW_INTERFACE),
        Map.entry("enum", TokenType.KW_ENUM),
        Map.entry("extends", TokenType.KW_EXTENDS),
        Map.entry("implements", TokenType.KW_IMPLEMENTS),
        Map.entry("abstract", TokenType.KW_ABSTRACT),
        Map.entry("final", TokenType.KW_FINAL),
        Map.entry("static", TokenType.KW_STATIC),
        Map.entry("void", TokenType.KW_VOID),
        Map.entry("boolean", TokenType.KW_BOOLEAN),
        Map.entry("byte", TokenType.KW_BYTE),
        Map.entry("short", TokenType.KW_SHORT),
        Map.entry("int", TokenType.KW_INT),
        Map.entry("long", TokenType.KW_LONG),
        Map.entry("float", TokenType.KW_FLOAT),
        Map.entry("double", TokenType.KW_DOUBLE),
        Map.entry("char", TokenType.KW_CHAR),
        Map.entry("super", TokenType.KW_SUPER),
        Map.entry("this", TokenType.KW_THIS),
        Map.entry("new", TokenType.KW_NEW),
        Map.entry("try", TokenType.KW_TRY),
        Map.entry("catch", TokenType.KW_CATCH),
        Map.entry("finally", TokenType.KW_FINALLY),
        Map.entry("throw", TokenType.KW_THROW),
        Map.entry("throws", TokenType.KW_THROWS),
        Map.entry("synchronized", TokenType.KW_SYNCHRONIZED),
        Map.entry("volatile", TokenType.KW_VOLATILE),
        Map.entry("transient", TokenType.KW_TRANSIENT),
        Map.entry("instanceof", TokenType.KW_INSTANCEOF),
        Map.entry("assert", TokenType.KW_ASSERT),
        Map.entry("native", TokenType.KW_NATIVE),
        Map.entry("strictfp", TokenType.KW_STRICTFP)
    );


    public String value;
    public TokenType type;
    public location loc;

    public Token(String value, TokenType type, location loc){
        this.value = value;
        this.type = type;
        this.loc = loc.copy();  
    }

    @Override
    public String toString(){
        return "Token(" + type.name() + ", " + value + ", " + loc.toString() + ")";
    }

}