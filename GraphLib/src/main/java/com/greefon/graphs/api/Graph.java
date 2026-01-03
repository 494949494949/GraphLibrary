package com.greefon.graphs.api;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


public interface Graph<T> {

    void addVertex(T vertex);

    default void addEdge(T source, T destination) {
        addEdge(source, destination, 1.0);
    }

    default void addEdge(T source, T destination, double weight) {
        addEdge(source, destination, weight, true);
    }

    void addEdge(T source, T destination, double weight, boolean createVertices);

    void removeVertex(T vertex);

    void removeEdge(T source, T destination);

    boolean containsVertex(T vertex);

    boolean containsEdge(T source, T destination);

    int getVertexCount();

    int getEdgeCount();

    Set<T> getVertices();

    Set<T> getNeighbors(T vertex);

    Map<T, Double> getNeighborsWithWeights(T vertex);

    Optional<Double> getWeight(T source, T destination);

    void clear();
}
