package com.semantic;

import com.parser.ASTnode;
import com.parser.Type;

public class Symbol {
    public final String name;
    public final Type type;
    public final boolean isFunction;
    public final ASTnode node;


    public Symbol(String name, Type type, boolean isFunction, ASTnode node) {
        this.name = name;
        this.type = type;
        this.isFunction = isFunction;
        this.node = node;
    }

    public String toString(){
        return name + " Type: " + type + " isfunction: " + isFunction;
    }
}

