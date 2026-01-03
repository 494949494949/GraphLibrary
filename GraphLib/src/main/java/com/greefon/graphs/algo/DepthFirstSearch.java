package com.greefon.graphs.algo;

import com.greefon.graphs.api.Graph;

import java.util.*;

public class DepthFirstSearch {
    public static <T> List<T> execute(Graph<T> graph) {
        List<T> result = new ArrayList<>();
        Set<T> visited = new HashSet<>();

        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                dfsComponent(graph, vertex, result, visited);
            }
        }

        return result;
    }
    public static <T> void dfsComponent(Graph<T> graph, T source, List<T> result, Set<T> visited) {
        Stack<T> stack = new Stack<>();

        stack.add(source);
        visited.add(source);

        while (!stack.isEmpty()) {
            T current = stack.pop();
            result.add(current);
            for (T neighbor : graph.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.add(neighbor);
                }
            }
        }
    }
}
