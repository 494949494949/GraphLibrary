package com.greefon.graphs.impl;

import com.greefon.graphs.api.Graph;

import java.util.*;

public abstract class AbstractGraph<T> implements Graph<T> {

    protected final Map<T, Map<T, Double>> adjList;
    protected int vertexCount;
    protected int edgeCount;

    public AbstractGraph() {
        this.adjList = new HashMap<>();
        this.vertexCount = 0;
        this.edgeCount = 0;
    }
    
    @Override
    public void addVertex(T vertex) {
        // Если вершина существует - не добавляем её. Поведение аналогично
        // множествам.
        if (!containsVertex(vertex)) {
            ++vertexCount;
        }
        adjList.putIfAbsent(vertex, new HashMap<>());
    }

    @Override
    public boolean containsVertex(T vertex) {
        return adjList.containsKey(vertex);
    }

    @Override
    public int getVertexCount() {
        return this.vertexCount;
    }

    @Override
    public int getEdgeCount() {
        return this.edgeCount;
    }

    @Override
    public Set<T> getNeighbors(T vertex) {
        return adjList.containsKey(vertex)
                ? Collections.unmodifiableSet(adjList.get(vertex).keySet())
                : Collections.emptySet();
    }

    @Override
    public Map<T, Double> getNeighborsWithWeights(T vertex) {
        return adjList.containsKey(vertex)
                ? Collections.unmodifiableMap(adjList.get(vertex))
                : Collections.emptyMap();
    }

    @Override
    public Set<T> getVertices() {
        return Collections.unmodifiableSet(adjList.keySet());
    }

    @Override
    public boolean containsEdge(T source, T destination) {
        return adjList.containsKey(source) && adjList.get(source).containsKey(destination);
    }

    @Override
    public Optional<Double> getWeight(T source, T destination) {
        if (containsEdge(source, destination)) {
            return Optional.of(adjList.get(source).get(destination));
        }
        return Optional.empty();
    }

    @Override
    public void clear() {
        adjList.clear();
        this.edgeCount = 0;
        this.vertexCount = 0;
    }
}
