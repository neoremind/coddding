package net.neoremind.mycode.argorithm.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.junit.Test;

/**
 * http://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
 */
public class Dijkstra {

    /**
     * a -> c(9) -> d(20) -> e(26)
     */
    @Test
    public void test() {
        Graph.Edge[] GRAPH = {
                new Graph.Edge("a", "b", 7),
                new Graph.Edge("a", "c", 9),
                new Graph.Edge("a", "f", 14),
                new Graph.Edge("b", "c", 10),
                new Graph.Edge("b", "d", 15),
                new Graph.Edge("c", "d", 11),
                new Graph.Edge("c", "f", 2),
                new Graph.Edge("d", "e", 6),
                new Graph.Edge("e", "f", 9),
        };

        String START = "a";
        String END = "e";

        Graph g = new Graph(GRAPH);
        g.dijkstra(START);
        g.printPath(END);
        g.printAllPaths();
    }

    /**
     * 《数据结构与算法分析》第9章图论9.3.2小节的例子
     * <p>
     * v1 -> v4(1) -> v7(5) -> v6(6)
     */
    @Test
    public void test2() {
        Graph.Edge[] GRAPH = {
                new Graph.Edge("v1", "v2", 2),
                new Graph.Edge("v1", "v4", 1),
                new Graph.Edge("v2", "v4", 3),
                new Graph.Edge("v2", "v5", 10),
                new Graph.Edge("v4", "v5", 2),
                new Graph.Edge("v4", "v3", 2),
                new Graph.Edge("v3", "v1", 4),
                new Graph.Edge("v4", "v6", 8),
                new Graph.Edge("v3", "v6", 5),
                new Graph.Edge("v4", "v7", 4),
                new Graph.Edge("v7", "v6", 1),
                new Graph.Edge("v5", "v7", 6)
        };

        String START = "v1";
        String END = "v6";

        Graph g = new Graph(GRAPH);
        g.dijkstra(START);
        g.printPath(END);
        g.printAllPaths();
    }
}

class Graph {

    private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

    /**
     * One edge of the graph (only used by Graph constructor)
     */
    public static class Edge {
        public final String v1, v2;
        public final int dist;

        public Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /**
     * One vertex of the graph, complete with mappings to neighbouring vertices
     */
    public static class Vertex implements Comparable<Vertex> {
        public final String name;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        // key为节点，value为dist
        public final Map<Vertex, Integer> neighbours = new HashMap<>();

        public Vertex(String name) {
            this.name = name;
        }

        private void printPath() {
            if (this == this.previous) {
                System.out.printf("%s", this.name);
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.name);
            } else {
                this.previous.printPath();
                System.out.printf(" -> %s(%d)", this.name, this.dist);
            }
        }

        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(dist, other.dist);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Builds a graph from a set of edges
     */
    public Graph(Edge[] edges) {
        graph = new HashMap<>(edges.length);

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) {
                graph.put(e.v1, new Vertex(e.v1));
            }
            if (!graph.containsKey(e.v2)) {
                graph.put(e.v2, new Vertex(e.v2));
            }
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /**
     * Runs dijkstra using a specified source vertex
     */
    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        PriorityQueue<Vertex> q = new PriorityQueue<>();  //贪婪算法的本质，每次都认为是最优解，先放进去的也是最近的最短的路径

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        System.out.println(q);
        dijkstra(q);
    }

    /**
     * Implementation of dijkstra's algorithm using a binary heap.
     */
    private void dijkstra(final PriorityQueue<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.poll(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE) {
                // 对于连通的图不会走到这一步
                break; // we can ignore u (and any other remaining vertices) since they are unreachable
            }

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /**
     * Prints a path from the source to the specified vertex
     */
    public void printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }

        graph.get(endName).printPath();
        System.out.println();
    }

    /**
     * Prints the path from the source to every vertex (output order is not guaranteed)
     */
    public void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }
}
