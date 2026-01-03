package com.greefon.graphs.algo;

import com.greefon.graphs.impl.DirectedGraph;
import com.greefon.graphs.impl.UndirectedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConnectivityInspectorTest {
    private UndirectedGraph<String> undirectedGraph;
    private DirectedGraph<String> directedGraph;

    @BeforeEach
    void setUp() {
        undirectedGraph = new UndirectedGraph<>();
        directedGraph = new DirectedGraph<>();
    }

    @Test
    void testEmptyGraph() {
        List<List<String>> components = ConnectivityInspector.findComponents(undirectedGraph);
        assertTrue(components.isEmpty(), "Empty graph should have no components");
    }

    @Test
    void testSingleVertex() {
        undirectedGraph.addVertex("A");
        List<List<String>> components = ConnectivityInspector.findComponents(undirectedGraph);

        assertEquals(1, components.size());
        assertEquals(1, components.get(0).size());
        assertEquals("A", components.get(0).get(0));
    }

    @Test
    void testMultipleDisconnectedComponents() {
        // Component 1: A-B-C
        undirectedGraph.addEdge("A", "B", 1.0, true);
        undirectedGraph.addEdge("B", "C", 1.0, true);

        // Component 2: D-E
        undirectedGraph.addEdge("D", "E", 1.0, true);

        // Component 3: F (isolated)
        undirectedGraph.addVertex("F");

        List<List<String>> components = ConnectivityInspector.findComponents(undirectedGraph);

        assertEquals(3, components.size(), "Should find exactly 3 components");

        // Verify content regardless of component order
        boolean foundSize3 = false;
        boolean foundSize2 = false;
        boolean foundSize1 = false;

        for (List<String> component : components) {
            if (component.size() == 3) {
                assertTrue(component.containsAll(List.of("A", "B", "C")));
                foundSize3 = true;
            } else if (component.size() == 2) {
                assertTrue(component.containsAll(List.of("D", "E")));
                foundSize2 = true;
            } else if (component.size() == 1) {
                assertTrue(component.contains("F"));
                foundSize1 = true;
            }
        }

        assertTrue(foundSize3 && foundSize2 && foundSize1, "All specific components should be present");
    }

    @Test
    void testDirectedGraphReachability() {
        // In a directed graph, this implementation finds "reachability partitions"
        // based on the order of iteration in getVertices().

        // A -> B
        // C -> D
        directedGraph.addEdge("A", "B", 1.0, true);
        directedGraph.addEdge("C", "D", 1.0, true);

        List<List<String>> components = ConnectivityInspector.findComponents(directedGraph);

        assertEquals(2, components.size(), "Directed graph with two separate paths should have 2 components");

        int totalVertices = components.stream().mapToInt(List::size).sum();
        assertEquals(4, totalVertices, "All vertices should be accounted for");
    }

    @Test
    void testFullyConnectedGraph() {
        // Triangle graph
        undirectedGraph.addEdge("A", "B", 1.0, true);
        undirectedGraph.addEdge("B", "C", 1.0, true);
        undirectedGraph.addEdge("C", "A", 1.0, true);

        List<List<String>> components = ConnectivityInspector.findComponents(undirectedGraph);

        assertEquals(1, components.size(), "A fully connected graph should have 1 component");
        assertEquals(3, components.get(0).size());
        assertTrue(components.get(0).containsAll(List.of("A", "B", "C")));
    }
}
