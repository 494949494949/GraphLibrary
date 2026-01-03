package com.greefon.graphs.algo;

import com.greefon.graphs.api.Graph;

import java.util.*;

public class Dijkstra {
    public static class ShortestPaths<T> {
        private final T source;
        private final Map<T, Double> distances;
        private final Map<T, T> predecessors;

        public ShortestPaths(T source, Map<T, Double> distances,
                             Map<T, T> predecessors) {
            this.source = source;
            this.distances = distances;
            this.predecessors = predecessors;
        }

        public Double getDistanceTo(T target) {
            return distances.getOrDefault(target, Double.POSITIVE_INFINITY);
        }

        public List<T> getPathTo(T target) {
            if (!distances.containsKey(target) ||
                    distances.get(target) == Double.POSITIVE_INFINITY) {
                return null;
            }

            LinkedList<T> path = new LinkedList<>();
            for (T current = target;
                 current != null;
                 current = predecessors.get(current)) {
                path.addFirst(current);
            }
            return path;
        }
    }

    public static <T> ShortestPaths<T> execute(Graph<T> graph, T source) {
        // Использует алгоритм Дейкстры
        if (!graph.containsVertex(source)) {
            throw new IllegalArgumentException(
                    "Source vertex is absent in this graph."
            );
        }

        Map<T, Double> distances = new HashMap<>();
        Map<T, T> predecessors = new HashMap<>();
        PriorityQueue<NodeDistance<T>> pq = new PriorityQueue<>(
                Comparator.comparingDouble(node -> node.distance)
        );

        initializeDistances(graph, source, distances, pq);

        while (!pq.isEmpty()) {
            NodeDistance<T> current = pq.poll();
            T currentNode = current.node;

            if (current.distance > distances.get(currentNode)) {
                continue;
            }

            Map<T, Double> neighbors = graph.getNeighborsWithWeights(currentNode);
            for (Map.Entry<T, Double> entry : neighbors.entrySet()) {
                T vertex = entry.getKey();
                double weight = entry.getValue();
                double newDistance = distances.get(currentNode) + weight;

                if (newDistance < distances.get(vertex)) {
                    distances.put(vertex, newDistance);
                    predecessors.put(vertex, currentNode);
                    pq.add(new NodeDistance<>(vertex, newDistance));
                }
            }
        }

        return new ShortestPaths<T>(source, distances, predecessors);
    }

    private static class NodeDistance<T> {
        T node;
        Double distance;
        NodeDistance(T node, Double distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    private static <T> void initializeDistances(Graph<T> graph, T source, Map<T, Double> distances,
                                     PriorityQueue<NodeDistance<T>> pq) {
        for (T vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);
        pq.add(new NodeDistance<>(source, 0.0));
    }
}
