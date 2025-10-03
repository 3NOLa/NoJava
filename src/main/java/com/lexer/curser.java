package com.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.util.curserInterface;
import com.util.location;

import java.lang.Character;

public class curser implements curserInterface<Character> {
    
    public String filepath;
    private String code;
    private int code_index;
    public StringBuilder current_lexeme;
    public location cur_loc;

    public curser(String filepath) throws IOException{
        this.filepath = filepath;
        this.code_index = 0;
        this.current_lexeme = new StringBuilder();
        this.code = Files.readString(Path.of(filepath));
        this.cur_loc = new location();
    }

    public curser(File file) throws IOException{
        this.filepath = file.getAbsolutePath();
        this.code_index = 0;
        this.current_lexeme = new StringBuilder();
        this.code = Files.readString(Path.of(filepath));
        this.cur_loc = new location();
    }

    public void reset() throws IOException{
        this.current_lexeme.setLength(0);
        this.code = Files.readString(Path.of(filepath));
        this.code_index = 0;
        this.cur_loc.reset();
    }

    public Character peek(){
        if (code_index < code.length())
            return code.charAt(code_index);
        return '\0';
    }

    public Character get(){
        if (code_index < code.length()){
            if(isEndLine())
                cur_loc.newLine();
            else
                cur_loc.update();
            return code.charAt(code_index++);   
        }
        return '\0';
    }

    public void consume(){
        if (code_index < code.length()){
            if(isEndLine())
                cur_loc.newLine();
            else
                cur_loc.update();
            code_index++;   
        }
    }

    @Override
    public boolean isEOF() {
        return false;
    }

    public boolean isEndLine(){
        return peek() == '\n';
    }

    public void updateLexeme(){
        current_lexeme.append(nextLexeme());
    }

    public void clearLexeme(){
        current_lexeme.setLength(0);;
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

    @FunctionalInterface
    public interface FuncArg {
        boolean check(char c);
    }

    public static FuncArg isAlpha() {
        return Character::isAlphabetic;
    }

    public static FuncArg isDigit() {
        return Character::isDigit;
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

    public static FuncArg isSingleQuotes() {
        return (char c) -> {
            return (c == '\'');
        };
    }

    public static FuncArg isNotQuotes() {
        return (char c) -> {
            return !(c == '\"'); 
        };
    }

    public static FuncArg isNotSingleQuotes() {
        return (char c) -> {
            return !(c == '\''); 
        };
    }

    public static FuncArg isBackSlash() {
        return (char c) -> {
            return (c == '\\'); 
        };
    }

    public static FuncArg isBackSlashWithCon() {
        final boolean[] wasBackSlash = {false};
        return (char c) -> {
            if (c == '\\') {
                if (!wasBackSlash[0]) {
                    wasBackSlash[0] = true;
                    return true; // first backslash
                } else {
                    // second backslash in a row
                    return false;
                }
            } else {
                wasBackSlash[0] = false;
                return false;
            }
        };
    }


    public void updateUntil(FuncArg func){
        while (func.check(peek())) {
            updateLexeme();
        }   
    }

    public void updateUntilwithCon(FuncArg func, FuncArg cond){
        while (func.check(peek())) {
            if (cond.check(peek()))
                updateLexeme();
            updateLexeme();
        }   
    }

}
