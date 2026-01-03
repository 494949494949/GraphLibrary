package com.greefon.graphs.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphTest {
    private DirectedGraph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new DirectedGraph<>();
    }

    @Test
    void testAddVertex() {
        graph.addVertex("A");
        assertTrue(graph.containsVertex("A"));
        assertEquals(1, graph.getVertexCount());

        // Adding same vertex again should not increase count
        graph.addVertex("A");
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testAddEdge() {
        // A -> B (weight 5.0)
        graph.addEdge("A", "B", 5.0, true);

        assertEquals(2, graph.getVertexCount());
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "A"), "Directed graph should not be mutual");

        assertEquals(5.0, graph.getWeight("A", "B").orElse(0.0));
    }

    @Test
    void testRemoveEdge() {
        graph.addEdge("A", "B", 1.0, true);
        graph.removeEdge("A", "B");

        assertFalse(graph.containsEdge("A", "B"));
        assertEquals(0, graph.getEdgeCount());
        assertEquals(2, graph.getVertexCount(), "Vertices should remain after edge removal");
    }

    @Test
    void testRemoveVertexCleanup() {
        // A -> B, C -> B
        graph.addEdge("A", "B", 1.0, true);
        graph.addEdge("C", "B", 1.0, true);

        // Removing B should remove both edges (incoming)
        graph.removeVertex("B");

        assertEquals(2, graph.getVertexCount());
        // A and C remain
        assertEquals(0, graph.getEdgeCount(), "All edges connected to B should be gone");
        assertFalse(graph.containsEdge("A", "B"));
    }

    @Test
    void testClear() {
        graph.addEdge("A", "B", 1.0, true);
        graph.clear();
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.containsVertex("A"));
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge("A", "B", 1.0, true);
        graph.addEdge("A", "C", 2.0, true);

        Set<String> neighbors = graph.getNeighbors("A");
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains("B"));
        assertTrue(neighbors.contains("C"));
    }
}
