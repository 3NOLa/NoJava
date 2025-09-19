package com.lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.lexer.Token.TokenType;

public class Lexer {

    private curser cur;
    private ArrayList<Token> tokens; 

    private static final Map<String, TokenType> KEYWORDS = Map.ofEntries(
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


    public Lexer(String filepath) throws IOException{ // because curser constructor is also throwing IOException  
        tokens = new ArrayList<>();
        this.cur = new curser(filepath);
    }

    public void analyze(){
        Token t = nextToken();
        while (t.type != TokenType.EOF) {
            tokens.add(t);
            t = nextToken();
        }
        tokens.add(t);
    }

    public Token nextToken(){
        cur.clearLexeme();
        cur.updateLexeme();

        TokenType type = TokenType.UNKNOWN;
        if ((type = symbolType()) != TokenType.UNKNOWN) {
            return new Token(cur.current_lexeme.toString(), type, cur.cur_loc);            
        }else if((type = literalType()) != TokenType.UNKNOWN) {
            return new Token(cur.current_lexeme.toString(), type, cur.cur_loc);       
        }else{
            cur.updateUntil(curser.isAlpha());
            if ((type = keyWordType()) != TokenType.UNKNOWN) {
                return new Token(cur.current_lexeme.toString(), type, cur.cur_loc); 
            }else{
                cur.updateUntil(curser.isDigitOrAlphaOrUnder());
                return new Token(cur.current_lexeme.toString(), TokenType.IDENTIFIER, cur.cur_loc); 
            }             
        }
    }

    public TokenType keyWordType(){
        if(KEYWORDS.containsKey(cur.current_lexeme.toString())){
            return KEYWORDS.get(cur.current_lexeme.toString());
        } else 
            return TokenType.UNKNOWN;
    }

    public TokenType literalType(){
        if(cur.current_lexeme.charAt(0) == '\"'){
            cur.clearLexeme(); //to skip the first quote
            cur.updateUntil(curser.isNotQuotes());
            cur.consume(); //to skip the seconed quote
            if (cur.current_lexeme.length() == 1) {
                return TokenType.CHAR_LITERAL;
            }else
                return TokenType.STRING_LITERAL;
        }else if(curser.isDigit().check(cur.current_lexeme.charAt(0))){
            cur.updateUntil(curser.isDigit());
            if(cur.peek() == '.'){
                cur.updateLexeme();
                cur.updateUntil(curser.isDigit());
                return TokenType.FLOAT_LITERAL;
            }else 
                return TokenType.INT_LITERAL;
        }else 
            return TokenType.UNKNOWN;
    }

    public TokenType symbolType(){
        switch (cur.current_lexeme.charAt(0)){
            case '\0': return TokenType.EOF;
            case '(': return TokenType.LPAREN;
            case ')': return TokenType.RPAREN;
            case ';': return TokenType.SEMICOLON;
            case ':': return TokenType.COLON;
            case ',': return TokenType.COMMA;
            case '.': return TokenType.DOT;
            case '{': return TokenType.LBRACE;
            case '}': return TokenType.RBRACE;
            case '[': return TokenType.LBRACKET;
            case ']': return TokenType.RBRACKET;
            case '+': return TokenType.OP_PLUS;
            case '-': return TokenType.OP_MINUS;
            case '*': return TokenType.OP_MUL;
            case '/': return TokenType.OP_DIV;
            case '=': 
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_EQ;
                }  else return TokenType.OP_ASSIGN;
            case '!': 
                if(cur.peek() == '='){
                    cur.updateLexeme();
                    return TokenType.OP_NEQ;
                } else return TokenType.OP_NOT;
            case '>': 
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_GTE; 
                } else return TokenType.OP_GT;
            case '<': 
                if(cur.peek() == '='){
                    cur.updateLexeme();
                    return TokenType.OP_LTE;
                }  else return TokenType.OP_LT;
            case '&': if(cur.peek() == '&'){
                    cur.updateLexeme();
                    return TokenType.OP_AND;
                } 
            case '|': if(cur.peek() == '|'){
                    cur.updateLexeme();
                    return TokenType.OP_OR;
                } 
            default:
               return TokenType.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            sb.append("\n" + t.toString());
        }
        return sb.toString();
    }


    public static void main(String[] args) throws IOException{
        Lexer lex = new Lexer("C:\\projJava\\javacompiler\\src\\main\\java\\com\\lexer\\curser.java");
        lex.analyze();
        System.out.println(lex.toString());
    }

}