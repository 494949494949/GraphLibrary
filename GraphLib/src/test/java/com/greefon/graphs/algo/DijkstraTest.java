package com.greefon.graphs.algo;

import com.greefon.graphs.impl.DirectedGraph;
import com.greefon.graphs.impl.UndirectedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {
    private DirectedGraph<String> directedGraph;
    private UndirectedGraph<String> undirectedGraph;

    @BeforeEach
    void setUp() {
        directedGraph = new DirectedGraph<>();
        undirectedGraph = new UndirectedGraph<>();
    }

    @Test
    void testBasicShortestPathDirected() {
        // A --(1)--> B --(2)--> C
        // A --(4)--> C
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("B", "C", 2.0, true);
        directedGraph.addEdge("A", "C", 4.0, true);

        Dijkstra.ShortestPaths<String> result = Dijkstra.execute(directedGraph, "A");

        // Distance check: 1 + 2 = 3, which is better than the direct edge of 4
        assertEquals(0.0, result.getDistanceTo("A"));
        assertEquals(1.0, result.getDistanceTo("B"));
        assertEquals(3.0, result.getDistanceTo("C"));

        // Path check
        assertEquals(List.of("A", "B", "C"), result.getPathTo("C"));
    }

    @Test
    void testShortestPathUndirected() {
        // A --(10)-- B
        // A --(1)--- C --(1)--- B
        undirectedGraph.addEdge("A", "B", 10.0, true);
        undirectedGraph.addEdge("A", "C", 1.0, true);
        undirectedGraph.addEdge("C", "B", 1.0, true);

        Dijkstra.ShortestPaths<String> result = Dijkstra.execute(undirectedGraph, "A");

        assertEquals(2.0, result.getDistanceTo("B"));
        assertEquals(List.of("A", "C", "B"), result.getPathTo("B"));
    }

    @Test
    void testUnreachableVertex() {
        directedGraph.addEdge("A", "B", 1.0, true);
        // Isolated vertex
        directedGraph.addVertex("C");

        Dijkstra.ShortestPaths<String> result = Dijkstra.execute(directedGraph, "A");

        assertEquals(Double.POSITIVE_INFINITY, result.getDistanceTo("C"));
        assertNull(result.getPathTo("C"), "Path to unreachable vertex should be null");
    }

    @Test
    void testSourceNotFound() {
        directedGraph.addVertex("A");

        assertThrows(IllegalArgumentException.class, () -> {
            Dijkstra.execute(directedGraph, "NonExistent");
        });
    }

    @Test
    void testCyclicGraph() {
        // A --(1)--> B --(1)--> C --(1)--> A
        // A --(5)--> C
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("B", "C", 1.0, true);
        directedGraph.addEdge("C", "A", 1.0, true);
        directedGraph.addEdge("A", "C", 5.0, true);

        Dijkstra.ShortestPaths<String> result = Dijkstra.execute(directedGraph, "A");

        assertEquals(2.0, result.getDistanceTo("C"), "Should find path through B");
        assertEquals(List.of("A", "B", "C"), result.getPathTo("C"));
    }

    @Test
    void testPathToSelf() {
        directedGraph.addVertex("A");
        Dijkstra.ShortestPaths<String> result = Dijkstra.execute(directedGraph, "A");

        assertEquals(0.0, result.getDistanceTo("A"));
        assertEquals(List.of("A"), result.getPathTo("A"));
    }

    @Test
    void testComplexWeights() {
        // Testing that it picks the weight 0.5 over 0.6
        directedGraph.addEdge("Start", "Middle1", 0.1, true);
        directedGraph.addEdge("Middle1", "End", 0.4, true);

        directedGraph.addEdge("Start", "Middle2", 0.3, true);
        directedGraph.addEdge("Middle2", "End", 0.3, true);

        Dijkstra.ShortestPaths<String> result = Dijkstra.execute(directedGraph, "Start");

        assertEquals(0.5, result.getDistanceTo("End"), 0.00001);
        assertEquals(List.of("Start", "Middle1", "End"), result.getPathTo("End"));
    }
}
