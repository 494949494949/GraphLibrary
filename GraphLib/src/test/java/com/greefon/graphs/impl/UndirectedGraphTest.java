package com.greefon.graphs.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UndirectedGraphTest {
    private UndirectedGraph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new UndirectedGraph<>();
    }

    @Test
    void testAddEdgeMutual() {
        // Adding A -> B in undirected graph
        graph.addEdge("A", "B", 10.0, true);

        assertTrue(graph.containsEdge("A", "B"));
        assertTrue(graph.containsEdge("B", "A"), "Undirected graph must have mutual edges");
        assertEquals(1, graph.getEdgeCount(), "Mutual edge should count as 1 edge");
    }

    @Test
    void testRemoveEdgeMutual() {
        graph.addEdge("A", "B", 1.0, true);
        graph.removeEdge("A", "B");

        assertFalse(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "A"));
        assertEquals(0, graph.getEdgeCount());
    }

    @Test
    void testRemoveVertexWithEdges() {
        // A -- B -- C
        graph.addEdge("A", "B", 1.0, true);
        graph.addEdge("B", "C", 1.0, true);

        assertEquals(2, graph.getEdgeCount());

        // Removing B should remove edges (A,B) and (B,C)
        graph.removeVertex("B");

        assertEquals(2, graph.getVertexCount()); // A and C remain
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.getNeighbors("A").contains("B"));
        assertFalse(graph.getNeighbors("C").contains("B"));
    }

    @Test
    void testSelfLoop() {
        // A -- A
        graph.addEdge("A", "A", 1.0, true);

        assertEquals(1, graph.getVertexCount());
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.containsEdge("A", "A"));

        graph.removeVertex("A");
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
    }

    @Test
    void testGetNeighborsWithWeights() {
        graph.addEdge("A", "B", 3.5, true);

        var weightsA = graph.getNeighborsWithWeights("A");
        var weightsB = graph.getNeighborsWithWeights("B");

        assertEquals(3.5, weightsA.get("B"));
        assertEquals(3.5, weightsB.get("A"), "Weight should be accessible from both sides");
    }
}