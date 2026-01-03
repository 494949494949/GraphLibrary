package com.greefon.graphs.demo;

import com.greefon.graphs.algo.ConnectivityInspector;
import com.greefon.graphs.algo.Dijkstra;
import com.greefon.graphs.api.Graph;
import com.greefon.graphs.impl.UndirectedGraph;
import com.greefon.graphs.util.GraphRandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>();

        graph.addEdge(1,2, 65.67);
        graph.addEdge(2,0);
        graph.addEdge(0,3);
        graph.addEdge(4,5);

        printInfo(graph);

        int source = 1;
        int target = 4;
        Dijkstra.ShortestPaths<Integer> paths = Dijkstra.execute(graph, source);
        System.out.println(
                "Расстояние между " + source + " и " + target +
                        " - " + paths.getDistanceTo(target)
        );

        List<Integer> vertices = Arrays.asList(1, 2, 3, 4, 5);
        GraphRandomizer.randomize(graph, vertices, 7);
        printInfo(graph);

        GraphRandomizer.randomizeExisting(graph, 8);
        printInfo(graph);

        GraphRandomizer.randomizeNew(graph, vertices, 9);
        printInfo(graph);
    }

    public static <T> void printInfo(Graph<T> graph) {
        System.out.println("\n");
        System.out.println("Список вершин графа: " + graph.getVertexCount());
        System.out.println("Список рёбер графа: " + graph.getEdgeCount());
        printAdjacencyList(graph);
        printAdjacencyMatrix(graph);

        List<List<T>> allComponents = ConnectivityInspector.findComponents(graph);

        System.out.println("Найдено компонент: " + allComponents.size());
        for (int i = 0; i < allComponents.size(); ++i) {
            System.out.println("Компонента " + (i + 1) + ": " + allComponents.get(i));
        }
        System.out.println("\n");
    }

    public static <T> void printAdjacencyList(Graph<T> graph) {
        System.out.println("--- Список смежности ---");
        if (graph.getVertexCount() == 0) {
            System.out.println("(Пустой граф)");
            return;
        }

        for (T vertex : graph.getVertices()) {
            Map<T, Double> neighbors = graph.getNeighborsWithWeights(vertex);
            System.out.print(vertex + " -> ");
            System.out.println(neighbors);
        }
    }
    public static <T> void printAdjacencyMatrix(Graph<T> graph) {
        System.out.println("--- Матрица смежности ---");
        int n = graph.getVertexCount();
        if (n == 0) {
            System.out.println("(Пустой граф)");
            return;
        }

        List<T> vertices = new ArrayList<>(graph.getVertices());

        int cellWidth = 8;

        System.out.printf("%" + cellWidth + "s", "");
        for (T vertex : vertices) {
            System.out.printf("%" + cellWidth + "s", truncate(vertex.toString(), cellWidth));
        }
        System.out.println();

        for (T rowVertex : vertices) {
            System.out.printf("%" + cellWidth + "s", truncate(rowVertex.toString(), cellWidth));

            for (T colVertex : vertices) {
                double weight = graph.getWeight(rowVertex, colVertex).orElse(0.0);

                System.out.printf("%" + cellWidth + ".1f", weight);
            }
            System.out.println();
        }
    }

    private static String truncate(String text, int length) {
        if (text.length() <= length) {
            return text;
        }
        return text.substring(0, length - 1) + ".";
    }

}
