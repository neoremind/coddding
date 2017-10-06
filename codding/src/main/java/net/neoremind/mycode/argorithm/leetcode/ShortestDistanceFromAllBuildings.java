package net.neoremind.mycode.argorithm.leetcode;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * You want to build a house on an empty land which reaches all buildings in the shortest amount of distance. You can
 * only move up, down, left and right. You are given a 2D grid of values 0, 1 or 2, where:
 * <p>
 * Each 0 marks an empty land which you can pass by freely.
 * Each 1 marks a building which you cannot pass through.
 * Each 2 marks an obstacle which you cannot pass through.
 * For example, given three buildings at (0,0), (0,4), (2,2), and an obstacle at (0,2):
 * <p>
 * 1 - 0 - 2 - 0 - 1
 * |   |   |   |   |
 * 0 - 0 - 0 - 0 - 0
 * |   |   |   |   |
 * 0 - 0 - 1 - 0 - 0
 * The point (1,2) is an ideal empty land to build a house, as the total travel distance of 3+3+1=7 is minimal. So
 * return 7.
 * <p>
 * Note:
 * There will be at least one building. If it is not possible to build such house according to the above rules,
 * return -1.
 * <p>
 * 参考https://discuss.leetcode.com/topic/31925/java-solution-with-explanation-and-time-complexity-analysis
 *
 * @author xu.zhang
 */
public class ShortestDistanceFromAllBuildings {

    public int shortestDistance(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        int[][] dist = new int[row][col];
        int[][] reach = new int[row][col];
        int buildingNumber = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 1) {
                    buildingNumber++;
                    bfs(grid, i, j, dist, reach, row, col);
                }
            }
        }
        int result = Integer.MAX_VALUE;
        int[] resultPos = new int[2];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0 && reach[i][j] == buildingNumber) {
                    if (dist[i][j] < result) {
                        result = dist[i][j];
                        resultPos[0] = i;
                        resultPos[1] = j;
                    }
                }
            }
        }
        Arrays.stream(dist).forEach(e -> System.out.println(ArrayUtils.toString(e)));
        System.out.println(result + " | " + resultPos[0] + "," + resultPos[1]);
        return result == Integer.MAX_VALUE ? -1 : result;
    }

    private void bfs(int[][] grid, int i, int j, int[][] dist, int[][] reach, int row, int col) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{i, j});
        boolean[][] visited = new boolean[row][col];
        visited[i][j] = true;
        int[][] d = new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        int level = 1;
        while (!q.isEmpty()) {
            int qSize = q.size();
            for (int m = 0; m < qSize; m++) {
                int[] p = q.poll();
                int x = p[0], y = p[1];
                for (int k = 0; k < 4; k++) {
                    int xx = x + d[k][0];
                    int yy = y + d[k][1];
                    if (xx >= 0 && xx < row && yy >= 0 && yy < col && grid[xx][yy] == 0) {
                        if (visited[xx][yy]) continue;
                        visited[xx][yy] = true;
                        dist[xx][yy] += level;
                        reach[xx][yy]++;
                        q.offer(new int[]{xx, yy});
                    }
                }
            }
            level++;
        }
        Arrays.stream(dist).forEach(e -> System.out.println(ArrayUtils.toString(e)));
        System.out.println("=====");
    }

    @Test
    public void test() {
        int[][] grid = new int[][]{
                {1, 0, 2, 0, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0}
        };
        assertThat(shortestDistance(grid), is(7));
    }
}
