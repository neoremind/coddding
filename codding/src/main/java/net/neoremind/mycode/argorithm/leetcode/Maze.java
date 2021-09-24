package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertThat;

/**
 * There is a ball in a maze with empty spaces and walls. The ball can go through empty spaces by rolling up, down,
 * left or right, but it won‘t stop rolling until hitting a wall. When the ball stops, it could choose the next
 * direction.
 * <p>
 * Given the ball‘s start position, the destination and the maze, determine whether the ball could stop at the
 * destination.
 * <p>
 * The maze is represented by a binary 2D array. 1 means the wall and 0 means the empty space. You may assume that
 * the borders of the maze are all walls. The start and destination coordinates are represented by row and column
 * indexes.
 * <p>
 * Example 1
 * <p>
 * Input 1: a maze represented by a 2D array
 * <p>
 * 0 0 1 0 0
 * 0 0 0 0 0
 * 0 0 0 1 0
 * 1 1 0 1 1
 * 0 0 0 0 0
 * <p>
 * Input 2: start coordinate (rowStart, colStart) = (0, 4)
 * Input 3: destination coordinate (rowDest, colDest) = (4, 4)
 * <p>
 * Output: true
 * Explanation: One possible way is : left -> down -> left -> down -> right -> down -> right.
 * 技术分享
 * <p>
 * <p>
 * Example 2
 * <p>
 * Input 1: a maze represented by a 2D array
 * <p>
 * 0 0 1 0 0
 * 0 0 0 0 0
 * 0 0 0 1 0
 * 1 1 0 1 1
 * 0 0 0 0 0
 * <p>
 * Input 2: start coordinate (rowStart, colStart) = (0, 4)
 * Input 3: destination coordinate (rowDest, colDest) = (3, 2)
 * <p>
 * Output: false
 * Explanation: There is no way for the ball to stop at the destination.
 * Note:
 * <p>
 * There is only one ball and one destination in the maze.
 * Both the ball and the destination exist on an empty space, and they will not be at the same position initially.
 * The given maze does not contain border (like the red rectangle in the example pictures), but you could assume the
 * border of the maze are all walls.
 * The maze contains at least 2 empty spaces, and both the width and height of the maze won‘t exceed 100.
 * <p>
 * 图的遍历问题，就是简单的遍历，所以dfs和bfs都可以做，复杂度也是一样的。
 * 这道题要求球不能停下来，即使碰到destination，必须是碰到wall才能停下来。
 * <p>
 * Time complexity: - O(n^2).
 *
 * @author xu.zhang
 */
public class Maze {

    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        if (start[0] == destination[0] && start[1] == destination[1]) return true;
        Queue<int[]> q = new LinkedList<>();
        q.add(start);
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] v = new boolean[rows][cols];
        v[start[0]][start[1]] = true;
        int[][] k = new int[][]{
                {0, 1}, {1, 0}, {-1, 0}, {0, -1}
        };
        while (!q.isEmpty()) {
            int[] p = q.poll();
            for (int i = 0; i < 4; i++) {
                int x = p[0];
                int y = p[1];
                while ((x + k[i][0] >= 0) && (x + k[i][0] < rows)
                        && (y + k[i][1] >= 0) && (y + k[i][1] < cols)
                        && (maze[x + k[i][0]][y + k[i][1]] != 1)) {
                    x += k[i][0];
                    y += k[i][1];
                }
                if (v[x][y]) {
                    continue;
                }
                v[x][y] = true;
                if (x == destination[0] && y == destination[1]) return true;
                q.add(new int[]{x, y});
            }
        }
        return false;
    }

    /**
     * BFS
     */
    public boolean canSolve(int[][] maze, int[] start, int[] dest) {
        if (maze == null || maze.length == 0) return false;
        if (start[0] == dest[0] && start[1] == dest[1]) return true;
        int m = maze[0].length;
        int n = maze.length;
        Queue<Point> q = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        int[][] d = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        q.add(new Point(start[0], start[1]));
        visited[start[0]][start[1]] = true;
        while (!q.isEmpty()) {
            Point cur = q.poll();
            for (int i = 0; i < 4; i++) {
                Point next = getNextPoint(cur.x, cur.y, maze, m, n, d[i][0], d[i][1]);
                if (visited[next.x][next.y]) {
                    continue;
                }
                visited[next.x][next.y] = true;
                if (next.x == dest[0] && next.y == dest[1]) {
                    return true;
                }
                q.add(next);
            }
        }
        return false;
    }

    static int[][] DIRECTION = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    /**
     * DFS
     */
    public boolean canSolve2(int[][] maze, int[] start, int[] dest) {
        if (maze == null || maze.length == 0) return false;
        if (start[0] == dest[0] && start[1] == dest[1]) return true;
        int m = maze[0].length;
        int n = maze.length;
        boolean[][] visited = new boolean[m][n];
        return dfs(maze, m, n, start, dest, visited);
    }

    public boolean dfs(int[][] maze, int m, int n, int[] start, int[] dest, boolean[][] visited) {
        if (visited[start[0]][start[1]]) return false;
        if (start[0] == dest[0] && start[1] == dest[1]) return true;
        visited[start[0]][start[1]] = true;
        for (int i = 0; i < 4; i++) {
            Point next = getNextPoint(start[0], start[1], maze, m, n, DIRECTION[i][0], DIRECTION[i][1]);
            if (dfs(maze, m, n, new int[]{next.x, next.y}, dest, visited)) {
                return true;
            }
        }
        return false;
    }

    Point getNextPoint(int x, int y, int[][] maze, int m, int n, int incrX, int incrY) {
        while (x + incrX >= 0 && x + incrX < m &&
                y + incrY >= 0 && y + incrY < n &&
                maze[x + incrX][y + incrY] != 1) {
            x += incrX;
            y += incrY;
        }
        return new Point(x, y);
    }

    class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Test
    public void testMaze() {
        int[][] maze = new int[][]{
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 1}
        };
        assertThat(canSolve(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is(true));
        assertThat(canSolve2(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is(true));

        maze = new int[][]{
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 1, 0},
                {0, 0, 0, 1}
        };
        assertThat(canSolve(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is(false));
        assertThat(canSolve2(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is(false));

        maze = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        assertThat(canSolve(maze, buildPoint(0, 4), buildPoint(4, 4)), Matchers.is(true));
        assertThat(canSolve2(maze, buildPoint(0, 4), buildPoint(4, 4)), Matchers.is(true));

        maze = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        assertThat(canSolve(maze, buildPoint(0, 4), buildPoint(3, 2)), Matchers.is(false));
        assertThat(canSolve2(maze, buildPoint(0, 4), buildPoint(3, 2)), Matchers.is(false));
    }

    int[] buildPoint(int x, int y) {
        return new int[]{x, y};
    }

}
