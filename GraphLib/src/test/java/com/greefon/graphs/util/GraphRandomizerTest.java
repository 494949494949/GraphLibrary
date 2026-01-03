package com.greefon.graphs.util;

import com.greefon.graphs.impl.DirectedGraph;
import com.greefon.graphs.impl.UndirectedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphRandomizerTest {
    private DirectedGraph<String> directedGraph;
    private UndirectedGraph<String> undirectedGraph;
    private List<String> vertices;

    @BeforeEach
    void setUp() {
        directedGraph = new DirectedGraph<>();
        undirectedGraph = new UndirectedGraph<>();
        vertices = Arrays.asList("A", "B", "C", "D", "E");
    }

    @Test
    void testRandomizeDirectedGraph() {
        int requestedEdges = 10;
        GraphRandomizer.randomize(directedGraph, vertices, requestedEdges);

        assertEquals(vertices.size(), directedGraph.getVertexCount());
        assertEquals(requestedEdges, directedGraph.getEdgeCount(),
                "Directed graph should contain exactly the requested number of edges");

        // Verify no self-loops were created (source != destination check)
        for (String v : vertices) {
            assertFalse(directedGraph.containsEdge(v, v), "Should not contain self-loops");
        }
    }

    @Test
    void testRandomizeUndirectedGraph() {
        // For 5 vertices, max undirected edges = (5 * 4) / 2 = 10
        int requestedEdges = 5;
        GraphRandomizer.randomize(undirectedGraph, vertices, requestedEdges);

        assertEquals(vertices.size(), undirectedGraph.getVertexCount());
        assertEquals(requestedEdges, undirectedGraph.getEdgeCount());
    }

    @Test
    void testMaxEdgesExceededThrowsException() {
        // Max for 5 vertices in directed is 5 * 4 = 20
        int tooManyEdges = 21;
        assertThrows(IllegalArgumentException.class, () -> {
            GraphRandomizer.randomize(directedGraph, vertices, tooManyEdges);
        }, "Should throw exception when more edges are requested than theoretically possible");
    }

    @Test
    void testRandomizeNew() {
        // Pre-fill graph with dummy data
        directedGraph.addEdge("X", "Y", 1.0, true);

        GraphRandomizer.randomizeNew(directedGraph, vertices, 3);

        assertEquals(5, directedGraph.getVertexCount(), "Should only contain new vertices");
        assertFalse(directedGraph.containsVertex("X"), "Old vertices should be cleared");
        assertEquals(3, directedGraph.getEdgeCount());
    }

    @Test
    void testRandomizeExisting() {
        // Setup existing vertices manually
        directedGraph.addVertex("Node1");
        directedGraph.addVertex("Node2");
        directedGraph.addVertex("Node3");

        GraphRandomizer.randomizeExisting(directedGraph, 2);

        assertEquals(3, directedGraph.getVertexCount());
        assertEquals(2, directedGraph.getEdgeCount());
        assertTrue(directedGraph.getVertices().containsAll(Arrays.asList("Node1", "Node2", "Node3")));
    }

    @Test
    void testZeroEdges() {
        GraphRandomizer.randomize(directedGraph, vertices, 0);
        assertEquals(5, directedGraph.getVertexCount());
        assertEquals(0, directedGraph.getEdgeCount());
    }

    @Test
    void testUndirectedGraphLimit() {
        /*
         * The current randomize implementation calculates maxPossible as V*(V-1).
         * For an UndirectedGraph with 3 vertices, maxPossible is 6.
         * However, UndirectedGraph can only actually hold 3 edges (A-B, B-C, A-C).
         * This test verifies the 'maxAttempts' safety guard works and a halt happens.
         */
        List<String> smallList = Arrays.asList("1", "2", "3");
        GraphRandomizer.randomize(undirectedGraph, smallList, 6);

        assertTrue(undirectedGraph.getEdgeCount() <= 3,
                "Undirected graph edge count should be limited by its nature even if more were requested");
    }
}
