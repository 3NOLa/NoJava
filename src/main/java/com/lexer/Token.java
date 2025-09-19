package com.lexer;

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

    public static class location{
        int line, column;

        public location(int line, int column){
            this.line = line;
            this.column = column;
        }

        public location copy(){
            return new location(this.line, this.column);
        }

        public void update(char c){
            if (c == '\n') {
                this.line++;
                this.column = 0;
            }else{
                this.column++;
            }

        }

        @Override
        public String toString(){
            return "line: " + line + ", column: " + column;
        }
    }

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
        return "Token: " + value + " type: " + type.name() + " " + loc.toString();
    }

}