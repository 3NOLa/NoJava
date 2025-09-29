package com.lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lexer.Token.TokenType;

public class Lexer {

    private final curser cur;
    private final List<Token> tokens;


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
            case '?': return TokenType.QUESTION;
            case '~': return TokenType.OP_BIT_NOT;
            case '+':
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_ADD_ASSIGN;
                } else if(cur.peek() == '+'){
                    cur.updateLexeme();
                    return TokenType.OP_INC;
                } return TokenType.OP_PLUS;
            case '-':
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_SUB_ASSIGN;
                } else if(cur.peek() == '-'){
                    cur.updateLexeme();
                    return TokenType.OP_DEC;
                } return TokenType.OP_MINUS;
            case '*':
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_MUL_ASSIGN;
                }return TokenType.OP_MUL;
            case '/':
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_DIV_ASSIGN;
                } return TokenType.OP_DIV;
            case '%':
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_MOD_ASSIGN;
                } return TokenType.OP_MOD;
            case '=':
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_EQ;
                } return TokenType.OP_ASSIGN;
            case '!': 
                if(cur.peek() == '='){
                    cur.updateLexeme();
                    return TokenType.OP_NEQ;
                } return TokenType.OP_NOT;
            case '>': 
                if(cur.peek() == '=') {
                    cur.updateLexeme();
                    return TokenType.OP_GTE; 
                } return TokenType.OP_GT;
            case '<': 
                if(cur.peek() == '='){
                    cur.updateLexeme();
                    return TokenType.OP_LTE;
                } return TokenType.OP_LT;
            case '&':
                if(cur.peek() == '='){
                    cur.updateLexeme();
                    return TokenType.OP_AND_ASSIGN;
                }else if(cur.peek() == '&'){
                    cur.updateLexeme();
                    return TokenType.OP_AND;
                } return TokenType.OP_BIT_AND;
            case '|':
                if(cur.peek() == '='){
                    cur.updateLexeme();
                    return TokenType.OP_OR_ASSIGN;
                } else if(cur.peek() == '|'){
                    cur.updateLexeme();
                    return TokenType.OP_OR;
                } return TokenType.OP_BIT_OR;
            case '^':
                if(cur.peek() == '^'){
                    cur.updateLexeme();
                    return TokenType.OP_XOR_ASSIGN;
                } return TokenType.OP_BIT_XOR;
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