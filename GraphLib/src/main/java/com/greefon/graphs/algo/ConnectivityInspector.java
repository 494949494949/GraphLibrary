package com.greefon.graphs.algo;

import com.greefon.graphs.api.Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectivityInspector {
    public static <T> List<List<T>> findComponents(Graph<T> graph) {
        List<List<T>> components = new ArrayList<>();
        Set<T> visited = new HashSet<>();

        for (T vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                List<T> component = new ArrayList<>();

                DepthFirstSearch.dfsComponent(
                        graph, vertex, component, visited
                );

                components.add(component);
            }
        }
        return components;
    }
}
