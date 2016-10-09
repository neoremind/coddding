package net.neoremind.mycode.argorithm.graph;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 经典的BFS算法
 * <p>
 * Given a snake and ladder board, find the minimum number of dice throws required to reach the destination or last
 * cell from source or 1st cell. Basically, the player has total control over outcome of dice throw and wants to find
 * out minimum number of throws required to reach last cell.
 * <p>
 * If the player reaches a cell which is base of a ladder, the player has to climb up that ladder and if reaches a
 * cell is mouth of the snake, has to go down to the tail of snake without a dice throw.
 * <p>
 * snakesladders
 * <p>
 * For example consider the board shown on right side (taken from here), the minimum number of dice throws required
 * to reach cell 30 from cell 1 is 3. Following are steps.
 * <p>
 * a) First throw two on dice to reach cell number 3 and then ladder to reach 22
 * b) Then throw 6 to reach 28.
 * c) Finally through 2 to reach 30.
 * <p>
 * There can be other solutions as well like (2, 2, 6), (2, 4, 4), (2, 3, 5).. etc.
 * <p>
 * We strongly recommend to minimize the browser and try this yourself first.
 * The idea is to consider the given snake and ladder board as a directed graph with number of vertices equal to the
 * number of cells in the board. The problem reduces to finding the shortest path in a graph. Every vertex of the
 * graph has an edge to next six vertices if next 6 vertices do not have a snake or ladder. If any of the next six
 * vertices has a snake or ladder, then the edge from current vertex goes to the top of the ladder or tail of the
 * snake. Since all edges are of equal weight, we can efficiently find shortest path using Breadth First Search of
 * the graph.
 * <p>
 * Following is C++ implementation of the above idea. The input is represented by two things, first is ‘N’ which is
 * number of cells in the given board, second is an array ‘move[0…N-1]’ of size N. An entry move[i] is -1 if there is
 * no snake and no ladder from i, otherwise move[i] contains index of destination cell for the snake or the ladder at i.
 * http://www.geeksforgeeks.org/snake-ladder-problem-2/
 *
 * @author zhangxu
 */
public class SnakeAndLadderProblem {

    int getMinDiceThrows(int move[], int N) {
        Queue<Vertex> queue = new LinkedList<>();
        Vertex starter = new Vertex(0, 0);
        queue.add(starter);
        boolean[] visited = new boolean[N];
        Vertex res = null;
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (v.id == N - 1) {
                res = v;
                break;
            }
            for (int i = v.id + 1; i <= v.id + 6 && i < N; i++) {
                if (visited[i]) {
                    continue;
                }
                Vertex neighbour = new Vertex();
                if (move[i] != -1) {
                    neighbour.id = move[i];
                } else {
                    neighbour.id = i;
                }
                neighbour.dist = v.dist + 1;
                queue.add(neighbour);
                visited[i] = true;
            }
        }

        if (res == null) {
            throw new RuntimeException("No solution!");
        }

        return res.dist;
    }

    class Vertex {
        int id;
        int dist;

        public Vertex() {
        }

        public Vertex(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "id=" + id +
                    ", dist=" + dist +
                    '}';
        }
    }

    /**
     * <pre>
     *     -1 -1 -1 -1
     *     -1 -1 -1 -1
     *     -1 -1 -1 -1
     *     -1 -1 -1 -1
     * </pre>
     */
    @Test
    public void test() {
        int N = 16;
        int[] moves = new int[N];
        Arrays.fill(moves, -1);
        assertThat(getMinDiceThrows(moves, N), Matchers.is(3));
    }

    /**
     * <pre>
     *     -1 -1 -1 -1
     *     -1 -1 -1 -1
     *     -1 -1 -1 -1
     *     -1 -1 15 -1
     * </pre>
     */
    @Test
    public void test2() {
        // Let us construct the board given in above diagram
        int N = 16;
        int[] moves = new int[N];
        Arrays.fill(moves, -1);

        // Ladders
        moves[2] = 15; //shortcut to final dest

        // Snakes
        //        moves[26] = 0;
        assertThat(getMinDiceThrows(moves, N), Matchers.is(1));
    }

    /**
     * <pre>
     *     1   1  1 -1
     *     -1  1  1  1
     *     -1 -1 -1 -1
     *     -1 -1 8 -1
     * </pre>
     */
    @Test
    public void test3() {
        // Let us construct the board given in above diagram
        int N = 16;
        int[] moves = new int[N];
        Arrays.fill(moves, -1);

        // Ladders
        moves[2] = 8; //shortcut to final dest

        // Snakes
        moves[9] = 1;  //拦路虎全是SNAKE
        moves[10] = 1;
        moves[11] = 1;
        moves[12] = 1;
        moves[13] = 1;
        moves[14] = 1;
        assertThat(getMinDiceThrows(moves, N), Matchers.is(1));
    }

    /**
     * <pre>
     *     -1  1  1 -1
     *     -1  1  1  1
     *     -1 -1 -1 -1
     *     -1 -1 8 -1
     * </pre>
     */
    @Test
    public void test4() {
        // Let us construct the board given in above diagram
        int N = 16;
        int[] moves = new int[N];
        Arrays.fill(moves, -1);

        // Ladders
        moves[2] = 8; //shortcut to final dest

        // Snakes
        moves[9] = 1;
        moves[10] = 1;
        moves[11] = 1;
        //moves[12] = 1; //有一个不是拦路虎就行
        moves[13] = 1;
        moves[14] = 1;
        assertThat(getMinDiceThrows(moves, N), Matchers.is(3));
    }

}
