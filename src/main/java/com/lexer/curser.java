package com.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.Character;

public class curser {
    
    public String filepath;
    private String code;
    private int code_index;
    public StringBuilder current_lexeme;
    public Token.location cur_loc;

    public curser(String filepath) throws IOException{
        this.filepath = filepath;
        this.code_index = 0;
        this.current_lexeme = new StringBuilder();
        this.code = Files.readString(Path.of(filepath));
        this.cur_loc = new Token.location(0, 0);
    }

    public curser(File file) throws IOException{
        this.filepath = file.getAbsolutePath();
        this.code_index = 0;
        this.current_lexeme = new StringBuilder();
        this.code = Files.readString(Path.of(filepath));
        this.cur_loc = new Token.location(0, 0);
    }

    public char peek(){
        if (code_index < code.length())
            return code.charAt(code_index);
        return '\0';
    }

    public char get(){
        if (code_index < code.length()){
            cur_loc.update(code.charAt(code_index));
            return code.charAt(code_index++);   
        }
        return '\0';
    }

    public void consume(){
        if (code_index < code.length()){
            cur_loc.update(code.charAt(code_index + 1));
            code_index++;   
        }
    }

    public void updateLexeme(){
        current_lexeme.append(nextLexeme());
    }

    public void clearLexeme(){
        current_lexeme.setLength(0);;
    }

    @FunctionalInterface
    public interface FuncArg {
        boolean check(char c);
    }

    public static FuncArg isAlpha() {
        return (char c) -> {
            return Character.isAlphabetic(c); 
        };
    }

    public static FuncArg isDigit() {
        return (char c) -> {
            return Character.isDigit(c); 
        };
    }

    public static FuncArg isDigitOrAlpha() {
        return (char c) -> {
            return Character.isDigit(c) || Character.isAlphabetic(c); 
        };
    }

    public static FuncArg isDigitOrAlphaOrUnder() {
        return (char c) -> {
            return Character.isDigit(c) || Character.isAlphabetic(c) || c == '_'; 
        };
    }
    

    public static FuncArg isQuotes() {
        return (char c) -> {
            return c == '\"' || c == '\''; 
        };
    }

    public static FuncArg isNotQuotes() {
        return (char c) -> {
            return !(c == '\"') && !(c == '\''); 
        };
    }

    public void updateUntil(FuncArg func){
        while (func.check(peek())) {
            updateLexeme();
        }   
    }

    public char nextLexeme(){
        char lex;
        while ((lex = get()) != '\0') {
            if(lex == '/' && peek() == '/'){
                while ((lex = get()) != '\n');
                continue;
            }
            if (lex == ' ' || lex == '\n' || lex == '\t' || lex == '\r') {
                continue;
            }
            return lex;
        }
        return '\0';
    }
}
