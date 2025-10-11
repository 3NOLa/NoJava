package com.parser;

import com.lexer.Token;
import com.util.location;

// ParseException.java
public class ParseException extends RuntimeException {  // or extends Exception
    private final location loc;

    public ParseException(String message) {
        super("Syntactic Error" + message);
        this.loc = new location();
    }

    public ParseException(String message, location loc) {
        super("Syntactic Error" + message + " at " + loc.toString());
        this.loc = loc;
    }

    public ParseException(String message, Token t) {
        super("Syntactic Error" + message + " Unexpected token: " + t.toString());
        this.loc = t.loc;
    }

    public location getLocation(){ return loc;}
}

