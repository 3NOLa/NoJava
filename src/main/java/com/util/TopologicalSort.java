package com.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.*;

public class TopologicalSort {

    private final LinkedHashSet<Vertex> sorted = new LinkedHashSet<>();
    private final Set<Vertex> visiting = new HashSet<>();
    private final Set<Vertex> visited = new HashSet<>();
    private HashMap<String, Vertex> vertices = null;

    public Set<Vertex> getSorted() {
        return sorted;
    }

    private void dfs(Vertex vertex) {
        if (visiting.contains(vertex))
            throw new RuntimeException("Cyclic dependency detected: " + vertex);

        if (visited.contains(vertex))
            return;

        visiting.add(vertex);

        for (Vertex edge : vertex.getEdges(this.vertices)) {
            dfs(edge);
        }

        visiting.remove(vertex);
        visited.add(vertex);
        sorted.add(vertex); // add *after* all dependencies
    }

    public void sort(HashMap<String, Vertex> vertices) {
        this.vertices = vertices;
        for (Vertex v : vertices.values()) {
            if (!visited.contains(v))
                dfs(v);
        }
    }
}
