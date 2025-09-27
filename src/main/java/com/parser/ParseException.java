package com.parser;

import com.lexer.Token;
import com.util.location;

// ParseException.java
public class ParseException extends RuntimeException {  // or extends Exception
    private final location loc;

    public ParseException(String message) {
        super(message);
        this.loc = new location();
    }

    public ParseException(String message, location loc) {
        super(message + " at " + loc.toString());
        this.loc = loc;
    }

    public ParseException(String message, Token t) {
        super(message + " Unexpected token: " + t.toString());
        this.loc = t.loc;
    }

    public location getLocation(){ return loc;}
}

