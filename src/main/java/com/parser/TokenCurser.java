package com.parser;

import com.lexer.Token;
import com.util.curserInterface;

import java.util.List;

public class TokenCurser implements curserInterface <Token>{
    private final List<Token> tokens;
    private int tokenIndex;
    private final int tokensAmount;

    public TokenCurser(List<Token> tokens){
        this.tokens = tokens;
        this.tokenIndex = 0;
        this.tokensAmount = tokens.size();
    }

    @Override
    public Token peek() {
        if(tokenIndex < tokensAmount)
            return tokens.get(tokenIndex);
        return null;
    }

    @Override
    public Token get() {
        if(tokenIndex < tokensAmount)
            return tokens.get(tokenIndex++);
        return null;
    }

    public Token get(Token.TokenType type){
        if(peek().type == type){
            if (tokenIndex < tokensAmount)
                return tokens.get(tokenIndex++);
        } throw new ParseException("Expected token: " + type.name(), peek());
    }

    @Override
    public void consume() {
        if(tokenIndex < tokensAmount)
            tokenIndex++;
    }

    public void consume(Token.TokenType type) {
        if(peek().type == type) {
            if(tokenIndex < tokensAmount)
                tokenIndex++;
        }else throw new ParseException("Expected token: " + type.name(), peek());
    }

    public void consume(Token.TokenType ... types) {
        boolean found = false;
        for(Token.TokenType type : types) {
            if (peek().type == type) {
                found = true;
                if (tokenIndex < tokensAmount)
                    tokenIndex++;
            }
        }
        if(!found)
            throw new ParseException("not right token: ", peek());
    }

    @Override
    public boolean isEOF() {
        if(peek().type == Token.TokenType.EOF)
            return true;
        return false;
    }

    public boolean match(Token.TokenType type){
        if(peek().type == type){
            consume();
            return true;
        } return false;
    }

    public boolean match(Token.TokenType ... types){
        for(Token.TokenType type : types){
            if(peek().type == type){
                consume();
                return true;
            }
        } return false;
    }

}
