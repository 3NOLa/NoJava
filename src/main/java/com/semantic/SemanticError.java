package com.semantic;

import com.util.location;

public class SemanticError extends RuntimeException {
    private final location loc;

    public SemanticError(String message) {
        super("SemanticError: " + message);
        this.loc = new location();
    }

    public SemanticError(String message, location loc) {
        super("SemanticError: " + message + " at " + loc.toString());
        this.loc = loc;
    }
}
