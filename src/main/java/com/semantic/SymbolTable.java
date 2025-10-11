package com.semantic;

import com.util.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import java.util.*;

public class SymbolTable {

    private final Deque<String> scopeStack = new ArrayDeque<>();
    private final Map<String, Map<String, Symbol>> scopes = new HashMap<>();

    public SymbolTable() {
        enterScope("global");
    }

    public void enterScope(String name) {
        if (scopes.containsKey(name)){
            printScopes();
            throw new SemanticError("Scope already exists: " + name);

        }
        scopes.put(name, new HashMap<>());
        scopeStack.push(name);
    }

    public void exitScope() {
        if (scopeStack.isEmpty())
            throw new SemanticError("No scope to exit");
        String name = scopeStack.pop();
        scopes.remove(name);
    }

    public String getCurrentScopeName() {
        return scopeStack.peek();
    }

    public Map<String, Symbol> getCurrentScope() {
        String name = getCurrentScopeName();
        return scopes.get(name);
    }

    public void declare(String name, Symbol sym) {
        String scopeName = getCurrentScopeName();
        Map<String, Symbol> current = scopes.get(scopeName);

        if (current.containsKey(name))
            throw new SemanticError("Redeclaration of " + name, sym.node.loc);

        current.put(name, sym);
    }

    public Symbol resolve(String name) {
        for (String scopeName : scopeStack) {
            Map<String, Symbol> scope = scopes.get(scopeName);
            if (scope != null && scope.containsKey(name))
                return scope.get(name);
        }
        throw new SemanticError("Undeclared identifier: " + name);
    }

    public Symbol resolve(String name, location loc) {
        for (String scopeName : scopeStack) {
            Map<String, Symbol> scope = scopes.get(scopeName);
            if (scope != null && scope.containsKey(name))
                return scope.get(name);
        }
        throw new SemanticError("Undeclared identifier: " + name, loc);
    }

    public Map<String, Symbol> getLocalTable(String name) {
        return scopes.get(name);
    }

    public void printScopes() {
        System.out.println("=== Symbol Table ===");
        for (Map.Entry<String, Map<String, Symbol>> entry : scopes.entrySet()) {
            System.out.println("Scope: " + entry.getKey());
            for (Map.Entry<String, Symbol> sym : entry.getValue().entrySet()) {
                System.out.println("  " + sym.getKey() + " -> " + sym.getValue());
            }
        }
    }
}
