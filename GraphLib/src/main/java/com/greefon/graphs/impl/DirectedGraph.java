package com.greefon.graphs.impl;

public class DirectedGraph<T> extends AbstractGraph<T> {
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
    }

    @Override
    public void removeVertex(T vertex) {
        if (!adjList.containsKey(vertex)) {
            return;
        }

        edgeCount -= adjList.get(vertex).size();

        for (T current : adjList.keySet()) {
            if (adjList.get(current).remove(vertex) != null) {
                --edgeCount;
            }
        }

        adjList.remove(vertex);
        --vertexCount;
    }

    @Override
    public void removeEdge(T source, T destination) {
        if (containsEdge(source, destination)) {
            adjList.get(source).remove(destination);
            --edgeCount;
        }
    }
}
