package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertThat;

/**
 * 给定一个二维迷宫，其中包含一个小球和一个洞。小球可以向上（u）、下（d）、左（l）、右（r）四个方向移动。
 * 每当小球选择一个方向后，会持续移动直到遇到墙壁为止。小球经过洞时会落入洞中。
 * 给定小球的初始位置ball，洞的位置hole。二维迷宫maze中1表示墙壁，0表示空地，四周被墙壁包围。
 * 求小球到洞的最短路径.
 * <p>
 * 和上一题{@link Maze}不一样的是：这道题要求最短的路径，普通的遍历dfs和bfs都是可以做的，但是求最短路径的话还是用Dijksra。
 *
 * @author xu.zhang
 */
public class MazeII {

    public int shortestDistance2(int[][] maze, int[] start, int[] destination) {
        // 与maze 1区别，没有visited，需要更新最短路径。加入dist[][]距离计算
        if (start[0] == destination[0] && start[1] == destination[1]) return 0;
        Queue<int[]> q = new LinkedList<>();
        q.add(start);
        int rows = maze.length;
        int cols = maze[0].length;
        int[][] k = new int[][]{
                {0, 1}, {1, 0}, {-1, 0}, {0, -1}
        };
        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[start[0]][start[1]] = 0;
        while (!q.isEmpty()) {
            int[] p = q.poll();
            for (int i = 0; i < 4; i++) {
                int x = p[0];
                int y = p[1];
                int step = 0;
                while ((x + k[i][0] >= 0) && (x + k[i][0] < rows)
                        && (y + k[i][1] >= 0) && (y + k[i][1] < cols)
                        && (maze[x + k[i][0]][y + k[i][1]] != 1)) {
                    x += k[i][0];
                    y += k[i][1];
                    step++;
                }
                if (dist[x][y] > dist[p[0]][p[1]] + step) {
                    dist[x][y] = dist[p[0]][p[1]] + step;
                    q.add(new int[]{x, y});
                }
            }
        }
        return dist[destination[0]][destination[1]] == Integer.MAX_VALUE ? -1 : dist[destination[0]][destination[1]];
    }

    public int shortestDistanceDijstra(int[][] maze, int[] start, int[] destination) {
        if (start[0] == destination[0] && start[1] == destination[1]) return 0;

        int rows = maze.length;
        int cols = maze[0].length;
        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        Queue<int[]> q = new PriorityQueue<int[]>(
                (a, b) -> Integer.compare(dist[a[0]][a[1]], dist[b[0]][b[1]]));
        q.add(start);

        int[][] k = new int[][]{
                {0, 1}, {1, 0}, {-1, 0}, {0, -1}
        };

        dist[start[0]][start[1]] = 0;
        while (!q.isEmpty()) {
            int[] p = q.poll();
            for (int i = 0; i < 4; i++) {
                int x = p[0];
                int y = p[1];
                int step = 0;
                while ((x + k[i][0] >= 0) && (x + k[i][0] < rows)
                        && (y + k[i][1] >= 0) && (y + k[i][1] < cols)
                        && (maze[x + k[i][0]][y + k[i][1]] != 1)) {
                    x += k[i][0];
                    y += k[i][1];
                    step++;
                }
                if (dist[x][y] > dist[p[0]][p[1]] + step) {
                    dist[x][y] = dist[p[0]][p[1]] + step;
                    q.add(new int[]{x, y});
                }
            }
        }
        return dist[destination[0]][destination[1]] == Integer.MAX_VALUE ? -1 : dist[destination[0]][destination[1]];
    }

    /**
     * BFS
     */
    public int shortestDistance(int[][] maze, int[] start, int[] dest) {
        if (start[0] == dest[0] && start[1] == dest[1]) return 0;
        int m = maze[0].length;
        int n = maze.length;
        boolean visited[][] = new boolean[m][n];
        Queue<Point> q = new PriorityQueue<>((o1, o2) -> o1.fromStartSteps - o2.fromStartSteps);
        q.offer(new Point(start[0], start[1]));
        visited[start[0]][start[1]] = true;
        Map<String, Integer> map = new HashMap<>();
        map.put(serialize(start[0], start[1]), 0);
        while (!q.isEmpty()) {
            Point p = q.poll();
            for (int i = 0; i < 4; i++) {
                Point next = getNext(p.x, p.y, m, n, d[i], maze);
                if (visited[next.x][next.y]) {
                    continue;
                }
                visited[next.x][next.y] = true;
                String prevKey = serialize(p.x, p.y);
                String nextKey = serialize(next.x, next.y);
                if (!map.containsKey(nextKey)) {
                    int steps = map.get(prevKey) + next.fromPreSteps;
                    map.put(nextKey, steps);
                    next.fromStartSteps = steps;
                } else {
                    if (map.get(prevKey) + next.fromPreSteps < map.get(nextKey)) {
                        map.put(nextKey, map.get(prevKey) + next.fromPreSteps);
                        next.fromStartSteps = map.get(prevKey) + next.fromPreSteps;
                    }
                }
                q.offer(next);
            }
        }
        if (visited[dest[0]][dest[1]]) {
            return map.get(serialize(dest[0], dest[1]));
        }
        return -1;
    }

    int[][] d = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    Point getNext(int x, int y, int m, int n, int[] move, int[][] maze) {
        int steps = 0;
        while (x + move[0] >= 0 && x + move[0] < m &&
                y + move[1] >= 0 && y + move[1] < n &&
                maze[x + move[0]][y + move[1]] != 1) {
            x += move[0];
            y += move[1];
            steps++;
        }
        return new Point(x, y, steps);
    }

    class Point {
        int x;
        int y;
        int fromStartSteps = 0;
        int fromPreSteps = 0;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point(int x, int y, int fromPreSteps) {
            this.x = x;
            this.y = y;
            this.fromPreSteps = fromPreSteps;
        }
    }

    @Test
    public void testShortestDistance() {
        int[][] maze = new int[][]{
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 1}
        };
        assertThat(shortestDistance(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is(4));

        maze = new int[][]{
                {0, 0, 0, 0, 0},
                {1, 1, 0, 0, 1},
                {0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1},
                {0, 1, 0, 0, 0}
        };
        assertThat(shortestDistance(maze, buildPoint(4, 3), buildPoint(0, 0)), Matchers.is(7));

        maze = new int[][]{
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 1, 0},
                {0, 0, 0, 1}
        };
        assertThat(shortestDistance(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is(-1));

        maze = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        assertThat(shortestDistance(maze, buildPoint(0, 4), buildPoint(4, 4)), Matchers.is(12));

        maze = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        assertThat(shortestDistance(maze, buildPoint(0, 4), buildPoint(3, 2)), Matchers.is(-1));
    }

    int[] buildPoint(int x, int y) {
        return new int[]{x, y};
    }

    String serialize(int x, int y) {
        return x + "-" + y;
    }
}
