package com.util;

import com.parser.ASTnode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface Vertex {
    public List<Vertex> getEdges(HashMap<String, Vertex> vertices);
    public ASTnode getValue();
}
