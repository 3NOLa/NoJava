package com.lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lexer.Token.TokenType;

public class Lexer {

    private curser cur;
    private List<Token> tokens;


    public Lexer(String filepath) throws IOException{ // because curser constructor is also throwing IOException  
        tokens = new ArrayList<Token>();
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

    public void reAnalyze() throws IOException{
        tokens.clear();
        cur.reset();
        analyze();
    }

    public char firstLexemeChar(){
        return cur.current_lexeme.charAt(0);
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
        if(Token.KEYWORDS.containsKey(cur.current_lexeme.toString())){
            return Token.KEYWORDS.get(cur.current_lexeme.toString());
        } else 
            return TokenType.UNKNOWN;
    }

    public TokenType literalType(){
        if(firstLexemeChar() == '\"'){
            cur.clearLexeme(); //to skip the first quote
            cur.updateUntilwithCon(curser.isNotQuotes(), curser.isBackSlashWithCon());
            cur.consume(); //to skip the seconed quote
            return TokenType.STRING_LITERAL;
        }else if (firstLexemeChar() == '\'') {
            cur.clearLexeme(); //to skip the first quote
            cur.updateUntilwithCon(curser.isNotSingleQuotes(), curser.isBackSlashWithCon());
            cur.consume(); //to skip the seconed quote
            return TokenType.CHAR_LITERAL;
        }else if(curser.isDigit().check(firstLexemeChar())){
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
        switch (firstLexemeChar()){
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