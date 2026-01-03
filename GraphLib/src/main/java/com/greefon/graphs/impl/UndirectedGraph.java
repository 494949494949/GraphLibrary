package com.greefon.graphs.impl;

public class UndirectedGraph<T> extends AbstractGraph<T> {
    @Override
    public void addEdge(T source, T destination, double weight, boolean createVertices) {
        if (createVertices) {
            addVertex(source);
            addVertex(destination);
        }

        if (!containsEdge(source, destination)) {
            ++edgeCount;
        }

        adjList.get(source).put(destination, weight);
        adjList.get(destination).put(source, weight);
    }

    @Override
    public void removeVertex(T vertex) {
        if (!adjList.containsKey(vertex)) {
            return;
        }

        for (T neighbor : getNeighbors(vertex)) {
            adjList.get(neighbor).remove(vertex);
            --edgeCount;
        }
        adjList.remove(vertex);
        --vertexCount;
    }

    @Override
    public void removeEdge(T source, T destination) {
        if (containsEdge(source, destination)) {
            adjList.get(source).remove(destination);
            adjList.get(destination).remove(source);
            --edgeCount;
        }
    }
}
