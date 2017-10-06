package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertThat;

/**
 * There is a ball in a maze with empty spaces and walls. The ball can go through empty spaces by rolling up (u),
 * down (d), left (l) or right (r), but it won't stop rolling until hitting a wall. When the ball stops, it could
 * choose the next direction. There is also a hole in this maze. The ball will drop into the hole if it rolls on to
 * the hole.
 * <p>
 * Given the ball position, the hole position and the maze, your job is to find out how the ball could drop into the
 * hole by moving shortest distance in the maze. The distance is defined by the number of empty spaces the ball go
 * through from the start position (exclude) to the hole (include). Output the moving directions by using 'u', 'd',
 * 'l' and 'r'. Since there may have several different shortest ways, you should output the lexicographically
 * smallest way. If the ball cannot reach the hole, output "impossible".
 * <p>
 * The maze is represented by a binary 2D array. 1 means the wall and 0 means the empty space. You may assume that
 * the borders of the maze are all walls. The ball and hole coordinates are represented by row and column indexes.
 * <p>
 * Example 1
 * <p>
 * Input 1: a maze represented by a 2D array
 * <p>
 * 0 0 0 0 0
 * 1 1 0 0 1
 * 0 0 0 0 0
 * 0 1 0 0 1
 * 0 1 0 0 0
 * <p>
 * Input 2: ball coordinate (rowBall, colBall) = (4, 3)
 * Input 3: hole coordinate (rowHole, colHole) = (0, 1)
 * <p>
 * Output: "lul"
 * Explanation: There are two shortest ways for the ball to drop into the hole.
 * The first way is left -> up -> left, represented by "lul".
 * The second way is up -> left, represented by 'ul'.
 * Both ways have shortest distance 6, but the first way is lexicographically smaller because 'l' < 'u'. So the
 * output is "lul".
 * <p>
 * Example 2
 * <p>
 * Input 1: a maze represented by a 2D array
 * <p>
 * 0 0 0 0 0
 * 1 1 0 0 1
 * 0 0 0 0 0
 * 0 1 0 0 1
 * 0 1 0 0 0
 * <p>
 * Input 2: ball coordinate (rowBall, colBall) = (4, 3)
 * Input 3: hole coordinate (rowHole, colHole) = (3, 0)
 * Output: "impossible"
 * Explanation: The ball cannot reach the hole.
 * <p>
 * Note:
 * <p>
 * There are only one ball and one hole in the maze.
 * The ball and hole will only exist in the empty space, and they will not at the same position initially.
 * The given maze doesn't contain border (like the red rectangle in the example pictures), but you should assume the
 * border of the maze are all walls.
 * The maze contains at least 2 empty spaces, and the length and width of the maze won't exceed 30.
 * <p>
 * 和{@link MazeII}不一样的是，如果存在距离相等的多条路径，返回字典序最短的那条。
 * <p>
 * //TODO 字典序最短的那条，没实现。
 *
 * @author xu.zhang
 */
public class MazeIII {

    /**
     * BFS
     */
    public String findShortestDistance(int[][] maze, int[] start, int[] dest) {
        if (start[0] == dest[0] && start[1] == dest[1]) return "";
        int m = maze[0].length;
        int n = maze.length;
        boolean visited[][] = new boolean[m][n];
        Queue<Point> q = new PriorityQueue<>((o1, o2) -> o1.fromStartSteps - o2.fromStartSteps);
        q.offer(new Point(start[0], start[1]));
        visited[start[0]][start[1]] = true;
        Map<String, Integer> map = new HashMap<>();
        map.put(serialize(start[0], start[1]), 0);
        Point ending = null;
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
                // 如果是新的步骤探测到的，直接赋值
                if (!map.containsKey(nextKey)) {
                    int steps = map.get(prevKey) + next.fromPreSteps;
                    map.put(nextKey, steps);
                    next.fromStartSteps = steps;
                    next.prev = p;
                    next.direction = direction[i];
                } else {
                    // 否则存在的话看是不是现在的步数更优
                    if (map.get(prevKey) + next.fromPreSteps < map.get(nextKey)) {
                        map.put(nextKey, map.get(prevKey) + next.fromPreSteps);
                        next.fromStartSteps = map.get(prevKey) + next.fromPreSteps;
                        next.prev = p;
                        next.direction = direction[i];
                    }
                }
                if (next.x == dest[0] && next.y == dest[1]) {
                    ending = next;
                }
                q.offer(next);
            }
        }
        if (visited[dest[0]][dest[1]]) {
            StringBuilder sb = new StringBuilder();
            backtrack(ending, start, sb);
            return sb.reverse().toString();
        }
        return "impossible";
    }

    void backtrack(Point p, int[] start, StringBuilder sb) {
        if (p.x == start[0] && p.y == start[1]) {
            return;
        } else {
            sb.append(p.direction);
            backtrack(p.prev, start, sb);
        }
    }

    int[][] d = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    char[] direction = new char[]{'u', 'r', 'd', 'l'};

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
        Point prev;
        char direction;
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
        assertThat(findShortestDistance(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is("dld"));

        maze = new int[][]{
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 1, 0},
                {0, 0, 0, 1}
        };
        assertThat(findShortestDistance(maze, buildPoint(0, 3), buildPoint(3, 2)), Matchers.is("impossible"));

        maze = new int[][]{
                {0, 0, 0, 0, 0},
                {1, 1, 0, 0, 1},
                {0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1},
                {0, 1, 0, 0, 0}
        };
        assertThat(findShortestDistance(maze, buildPoint(4, 3), buildPoint(0, 0)), Matchers.is("ul"));

        maze = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        assertThat(findShortestDistance(maze, buildPoint(0, 4), buildPoint(4, 4)), Matchers.is("ldldrdr"));

        maze = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 0, 0}
        };
        assertThat(findShortestDistance(maze, buildPoint(0, 4), buildPoint(3, 2)), Matchers.is("impossible"));
    }

    int[] buildPoint(int x, int y) {
        return new int[]{x, y};
    }

    String serialize(int x, int y) {
        return x + "-" + y;
    }

}
