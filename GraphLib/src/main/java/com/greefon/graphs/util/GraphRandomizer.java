package com.greefon.graphs.util;

import com.greefon.graphs.api.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphRandomizer {
    private static final Random random = new Random();
    public static <T> void randomize(Graph<T> graph, List<T> vertices, int max) {
        int vertexCount = vertices.size();

        for (T vertex : vertices) {
            graph.addVertex(vertex);
        }

        int maxPossible = vertexCount * (vertexCount - 1);
        if (max > maxPossible) {
            throw new IllegalArgumentException(
                    "Requested edges exceed maximum possible for " +
                            vertexCount + " vertices.");
        }

        // На случай, если граф слишком большой
        int attempt = 0;
        int maxAttempts = max * 100;

        while (graph.getEdgeCount() < max && attempt < maxAttempts) {
            T source = vertices.get(random.nextInt(vertices.size()));
            T destination = vertices.get(random.nextInt(vertices.size()));

            if (!source.equals(destination) &&
                    !graph.containsEdge(source, destination)) {
                graph.addEdge(source, destination);
            }
            ++attempt;
        }
    }
    public static <T> void randomizeNew(Graph<T> graph, List<T> vertices, int max) {
        graph.clear();
        randomize(graph, vertices, max);
    }

    public static <T> void randomizeExisting(Graph<T> graph, int max) {
        randomize(graph, new ArrayList<>(graph.getVertices()), max);
    }
}
