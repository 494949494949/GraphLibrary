package com.greefon.graphs.algo;

import com.greefon.graphs.impl.DirectedGraph;
import com.greefon.graphs.impl.UndirectedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepthFirstSearchTest {
    private DirectedGraph<String> directedGraph;
    private UndirectedGraph<String> undirectedGraph;

    @BeforeEach
    void setUp() {
        directedGraph = new DirectedGraph<>();
        undirectedGraph = new UndirectedGraph<>();
    }

    @Test
    void testExecuteEmptyGraph() {
        List<String> result = DepthFirstSearch.execute(directedGraph);
        assertTrue(result.isEmpty(), "DFS on empty graph should return empty list");
    }

    @Test
    void testExecuteSingleVertex() {
        directedGraph.addVertex("A");
        List<String> result = DepthFirstSearch.execute(directedGraph);
        assertEquals(List.of("A"), result);
    }

    @Test
    void testExecuteLinearPath() {
        // A -> B -> C
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("B", "C", 1.0, true);

        List<String> result = DepthFirstSearch.execute(directedGraph);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of("A", "B", "C")));
        // In a simple linear directed graph starting from A, order must be A, B, C
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));
        assertEquals("C", result.get(2));
    }

    @Test
    void testExecuteDisconnectedGraph() {
        // Component 1: A -> B
        directedGraph.addEdge("A", "B", 1.0, true);
        // Component 2: C -> D
        directedGraph.addEdge("C", "D", 1.0, true);

        List<String> result = DepthFirstSearch.execute(directedGraph);

        assertEquals(4, result.size());
        assertTrue(result.containsAll(List.of("A", "B", "C", "D")),
                "DFS should visit all components");
    }

    @Test
    void testExecuteWithCycles() {
        // A -> B -> C -> A (Cycle)
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("B", "C", 1.0, true);
        directedGraph.addEdge("C", "A", 1.0, true);

        List<String> result = DepthFirstSearch.execute(directedGraph);

        assertEquals(3, result.size(), "DFS should handle cycles using the visited set");
        assertTrue(result.containsAll(List.of("A", "B", "C")));
    }

    @Test
    void testBfsComponentLogic() {
        // Testing the sub-method specifically
        // A -- B
        // |    |
        // D -- C
        undirectedGraph.addEdge("A", "B", 1.0, true);
        undirectedGraph.addEdge("B", "C", 1.0, true);
        undirectedGraph.addEdge("C", "D", 1.0, true);
        undirectedGraph.addEdge("D", "A", 1.0, true);

        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        DepthFirstSearch.dfsComponent(undirectedGraph, "A", result, visited);

        // Check if all were visited
        assertEquals(4, result.size());
        assertEquals("A", result.get(0), "Source should be the first element in result");
        assertTrue(visited.containsAll(List.of("A", "B", "C", "D")));
    }

    @Test
    void testDirectedBacktrack() {
        // A -> B, A -> C
        // Even if it branches, DFS should visit everything reachable
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("A", "C", 1.0, true);

        List<String> result = DepthFirstSearch.execute(directedGraph);

        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("C"));
    }
}