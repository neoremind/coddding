package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Integer.MAX_VALUE;

/**
 * You are given a m x n 2D grid initialized with these three possible values.
 * <p>
 * -1 - A wall or an obstacle.
 * 0 - A gate.
 * INF - Infinity means an empty room. We use the value 2^31 - 1 = 2147483647 to represent INF as you may assume that
 * the distance to a gate is less than 2147483647.
 * Fill each empty room with the distance to its nearest gate. If it is impossible to reach a gate, it should be
 * filled with INF.
 * <p>
 * For example, given the 2D grid:
 * INF  -1  0  INF
 * INF INF INF  -1
 * INF  -1 INF  -1
 * 0  -1 INF INF
 * After running your function, the 2D grid should be:
 * 3  -1   0   1
 * 2   2   1  -1
 * 1  -1   2  -1
 * 0  -1   3   4
 *
 * @author xu.zhang
 */
public class WallsAndGates {

    /**
     * dfs
     */
    public void wallsAndGates(int[][] rooms) {
        int m = rooms[0].length;
        int n = rooms.length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    dfs(rooms, i, j, m, n, 0);
                }
            }
        }
    }

    /**
     * bfs
     * https://discuss.leetcode.com/topic/25265/java-bfs-solution-o-mn-time/6
     */
    public void wallsAndGates2(int[][] rooms) {
        Queue<int[]> list = new LinkedList<int[]>();
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0)
                    list.add(new int[]{i, j});
            }
        }
        int[][] diff = new int[][]{{-1, 0, 1, 0}, {0, 1, 0, -1}};
        while (!list.isEmpty()) {
            int[] pop = list.remove();
            for (int i = 0; i < diff[0].length; i++) {
                int newR = pop[0] + diff[0][i];
                int newC = pop[1] + diff[1][i];
                if (newR < 0 || newR >= rooms.length || newC < 0 || newC >= rooms[0].length ||
                        rooms[newR][newC] != Integer.MAX_VALUE)
                    continue;
                rooms[newR][newC] = rooms[pop[0]][pop[1]] + 1;
                list.add(new int[]{newR, newC});
            }
        }
    }

    private void dfs(int[][] rooms, int i, int j, int m, int n, int step) {
        if (i < 0 || i >= m || j < 0 || j >= n || rooms[i][j] == -1) {
            return;
        }
        if (rooms[i][j] >= step) {
            rooms[i][j] = step;
            dfs(rooms, i + 1, j, m, n, step + 1);
            dfs(rooms, i - 1, j, m, n, step + 1);
            dfs(rooms, i, j + 1, m, n, step + 1);
            dfs(rooms, i, j - 1, m, n, step + 1);
        }
    }

    @Test
    public void test() {
        int[][] rooms = new int[][]{
                {MAX_VALUE, -1, 0, MAX_VALUE},
                {MAX_VALUE, MAX_VALUE, MAX_VALUE, -1},
                {MAX_VALUE, -1, MAX_VALUE, -1},
                {0, -1, MAX_VALUE, MAX_VALUE}
        };
        wallsAndGates(rooms);
        for (int i = 0; i < rooms[0].length; i++) {
            for (int j = 0; j < rooms.length; j++) {
                System.out.print(rooms[i][j] + ",");
            }
            System.out.println();
        }

        rooms = new int[][]{
                {MAX_VALUE, -1, 0, MAX_VALUE},
                {MAX_VALUE, MAX_VALUE, MAX_VALUE, -1},
                {MAX_VALUE, -1, MAX_VALUE, -1},
                {0, -1, MAX_VALUE, MAX_VALUE}
        };
        wallsAndGates2(rooms);
        for (int i = 0; i < rooms[0].length; i++) {
            for (int j = 0; j < rooms.length; j++) {
                System.out.print(rooms[i][j] + ",");
            }
            System.out.println();
        }
    }
}
