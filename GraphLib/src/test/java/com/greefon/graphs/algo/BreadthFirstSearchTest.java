package com.greefon.graphs.algo;

import com.greefon.graphs.impl.DirectedGraph;
import com.greefon.graphs.impl.UndirectedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BreadthFirstSearchTest {
    private DirectedGraph<String> directedGraph;
    private UndirectedGraph<String> undirectedGraph;

    @BeforeEach
    void setUp() {
        directedGraph = new DirectedGraph<>();
        undirectedGraph = new UndirectedGraph<>();
    }

    @Test
    void testExecuteEmptyGraph() {
        List<String> result = BreadthFirstSearch.execute(directedGraph);
        assertTrue(result.isEmpty(), "BFS on empty graph should return empty list");
    }

    @Test
    void testExecuteSingleComponent() {
        // A -> B, A -> C, B -> D
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("A", "C", 1.0, true);
        directedGraph.addEdge("B", "D", 1.0, true);

        List<String> result = BreadthFirstSearch.execute(directedGraph);

        assertEquals(4, result.size());
        assertEquals("A", result.get(0), "Root should be first");

        // In BFS, B and C (distance 1) must appear before D (distance 2)
        int idxB = result.indexOf("B");
        int idxC = result.indexOf("C");
        int idxD = result.indexOf("D");

        assertTrue(idxB < idxD, "B should be visited before D");
        assertTrue(idxC < idxD, "C should be visited before D");
    }

    @Test
    void testExecuteDisconnectedGraph() {
        // Component 1: A-B
        directedGraph.addEdge("A", "B", 1.0, true);
        // Component 2: C-D
        directedGraph.addEdge("C", "D", 1.0, true);

        List<String> result = BreadthFirstSearch.execute(directedGraph);

        assertEquals(4, result.size());
        assertTrue(result.containsAll(List.of("A", "B", "C", "D")));
    }

    @Test
    void testBfsComponent() {
        // Setup Undirected Graph: A -- B -- C
        undirectedGraph.addEdge("A", "B", 1.0, true);
        undirectedGraph.addEdge("B", "C", 1.0, true);

        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        // Run BFS specifically starting from "B"
        BreadthFirstSearch.bfsComponent(undirectedGraph, "B", result, visited);

        assertEquals(3, result.size());
        assertEquals("B", result.get(0));
        // A and C are both distance 1 from B, so they come after
        assertTrue(result.contains("A"));
        assertTrue(result.contains("C"));
        assertTrue(visited.containsAll(List.of("A", "B", "C")));
    }

    @Test
    void testGraphWithCycle() {
        // A -> B -> C -> A
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("B", "C", 1.0, true);
        directedGraph.addEdge("C", "A", 1.0, true);

        List<String> result = BreadthFirstSearch.execute(directedGraph);

        assertEquals(3, result.size(), "Should handle cycles without infinite loop");
        assertTrue(result.containsAll(List.of("A", "B", "C")));
    }
}