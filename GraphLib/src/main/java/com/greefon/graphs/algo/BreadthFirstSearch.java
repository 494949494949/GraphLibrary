package com.greefon.graphs.algo;

import com.greefon.graphs.api.Graph;

import java.util.*;

public class BreadthFirstSearch {
    public static <T> List<T> execute(Graph<T> graph) {
        List<T> result = new ArrayList<>();
        Set<T> visited = new HashSet<>();

        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                bfsComponent(graph, vertex, result, visited);
            }
        }

        return result;
    }
    public static <T> void bfsComponent(Graph<T> graph, T source, List<T> result, Set<T> visited) {
        Queue<T> queue = new LinkedList<>();

        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            result.add(current);

            for (T neighbor : graph.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }
}
