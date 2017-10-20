package net.neoremind.mycode.argorithm.other;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertThat;

/**
 * 题目是给你一个机器人和一个房间，你并不知道机器人在房间什么位置，你也不知道房间的形状大小，你有一个遥控器，可以让机器人走前后左右四个方向。这里给你一个move method : boolean move(int
 * direction), direction: 0,1,2,3 表示四个方向。能移动就返回true,不能移动表示false。问你：这个房间多大。
 * <p>
 * http://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid=281355&extra=page%3D5%26filter%3Dsortid%26sortid
 * %3D311&page=1
 * <p>
 * 答案贴到了http://www.1point3acres.com/bbs/thread-281355-3-1.html
 *
 * @author xu.zhang
 */
public class RobotAndArea {

    public int areaDFS(int i, int j) {
        Set<String> visited = new HashSet<>();
        dfs(i, j, visited);
        return visited.size();
    }

    void dfs(int i, int j, Set<String> visited) {
        if (visited.contains(encode(i, j))) {
            return;
        }
        visited.add(encode(i, j));
        for (int k = 0; k < 4; k++) {
            if (move(i, j, k)) {
                if (k == 0) {
                    dfs(i - 1, j, visited);
                } else if (k == 1) {
                    dfs(i, j - 1, visited);
                } else if (k == 2) {
                    dfs(i + 1, j, visited);
                } else if (k == 3) {
                    dfs(i, j + 1, visited);
                }
            }
        }
    }

    // 有优化，提前判断不放到queue里面
    public int areaBFS(int i, int j) {
        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{i, j});
        while (!queue.isEmpty()) {
            int[] xy = queue.poll();
            int currI = xy[0];
            int currJ = xy[1];
            if (visited.contains(encode(currI, currJ))) {
                continue;
            }
            visited.add(encode(currI, currJ));
            for (int k = 0; k < 4; k++) {
                if (move(currI, currJ, k)) {
                    if (k == 0) {
                        queue.offer(new int[]{currI - 1, currJ});
                    } else if (k == 1) {
                        queue.offer(new int[]{currI, currJ - 1});
                    } else if (k == 2) {
                        queue.offer(new int[]{currI + 1, currJ});
                    } else if (k == 3) {
                        queue.offer(new int[]{currI, currJ + 1});
                    }
                }
            }
        }
        return visited.size();
    }

    String encode(int i, int j) {
        return "x" + i + "y" + j;
    }

    boolean move(int i, int j, int dir) {
        if (dir == 0) { // up
            if (i <= 0 || (i > 0 && grid[i - 1][j] == 0)) return false;
        } else if (dir == 1) { // left
            if (j <= 0 || (j > 0 && grid[i][j - 1] == 0)) return false;
        } else if (dir == 2) { // down
            if (i >= grid.length - 1 || (i < grid.length - 1 && grid[i + 1][j] == 0)) return false;
        } else if (dir == 3) { // right
            if (j >= grid[0].length - 1 || (j < grid[0].length && grid[i][j + 1] == 0)) return false;
        } else {
            throw new RuntimeException("Invalid direction");
        }
        return true;
    }

//    int[][] grid = {
//            {0, 1, 1, 0},
//            {1, 0, 1, 0},
//            {1, 1, 1, 1},
//            {0, 1, 0, 1}
//    };

    int[][] grid = {
            {0, 1, 1, 0, 0},
            {1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1},
            {0, 1, 0, 0, 1},
            {1, 1, 1, 1, 1}
    };

    @Test
    public void test() {
        assertThat(areaDFS(2, 2), Matchers.is(17));
        assertThat(areaBFS(2, 2), Matchers.is(17));
    }

}
