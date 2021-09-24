package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import java.util.*;

/**
 * Examples:
 * <p>
 * Input: board = [[1,2,3],[4,0,5]]
 * Output: 1
 * Explanation: Swap the 0 and the 5 in one move.
 * Input: board = [[1,2,3],[5,4,0]]
 * Output: -1
 * Explanation: No number of moves will make the board solved.
 * Input: board = [[4,1,2],[5,0,3]]
 * Output: 5
 * Explanation: 5 is the smallest number of moves that solves the board.
 * An example path:
 * After move 0: [[4,1,2],[5,0,3]]
 * After move 1: [[4,1,2],[0,5,3]]
 * After move 2: [[0,1,2],[4,5,3]]
 * After move 3: [[1,0,2],[4,5,3]]
 * After move 4: [[1,2,0],[4,5,3]]
 * After move 5: [[1,2,3],[4,5,0]]
 * Input: board = [[3,2,4],[1,5,0]]
 * Output: 14
 *
 * @author xu.zx
 */
public class SlidingPuzzle {

    public int slidingPuzzle(int[][] board) {
        int w = board.length;
        int h = board[0].length;
        int len = w * h;

        // if already done
        if (isEnd(board)) {
            return 0;
        }

        // find solutions
        Queue<int[][]> q = new LinkedList<>();
        q.add(copyOf(board));
        Set<String> v = new TreeSet<>();
        v.add(encode(board));
        int step = 0;
        int[][] k = new int[][]{
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        while (!q.isEmpty()) {
            step++;
            int size = q.size();
            // for all choices
            for (int i = 0; i < size; i++) {
                int[][] curr = q.poll();
                for (int j = 0; j < 4; j++) {
                    int[][] next = next(curr, k[j][0], k[j][1], w, h);
                  // printBoard(next);
                    if (v.contains(encode(next))) {
                        continue;
                    }
                    v.add(encode(next));
                    if (isEnd(next)) {
                        return step;
                    }
                    q.add(next);
                }
            }
        }
        return -1;
    }

    private String encode(int[][] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                sb.append(b[i][j]).append(" ");
            }
        }
        return sb.toString();
    }

    private int[][] next(int[][] b, int x, int y, int w, int h) {
        int[][] res = copyOf(b);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                if (res[i][j] == 0) {
                    if (i + x >= 0 && i + x < w && j + y >= 0 && j + y < h) {
                        int temp = res[i][j];
                        res[i][j] = res[i + x][j + y];
                        res[i + x][j + y] = temp;
                    }
                    break;
                }
            }
        }
        return res;
    }

    private int[][] copyOf(int[][] b) {
        int[][] res = new int[b.length][b[0].length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                res[i][j] = b[i][j];
            }
        }
        return res;
    }

    private boolean isEnd(int[][] b) {
        int t = 1;
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                if (i == b.length - 1 && j == b[0].length - 1) break;
                if (b[i][j] != t++) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void test() {
        int[][] b = new int[][]{
                {1, 2, 3},
                {4, 0, 5}
        };
        System.out.println(slidingPuzzle(b));

        b = new int[][]{
                {1, 2, 3},
                {5, 4, 0}
        };
        System.out.println(slidingPuzzle(b));

        b = new int[][]{
                {4, 1, 2},
                {5, 0, 3}
        };
        System.out.println(slidingPuzzle(b));

        b = new int[][]{
                {3, 2, 4},
                {1, 5, 0}
        };
        System.out.println(slidingPuzzle(b));
    }

    private void printBoard(int[][] b) {
        System.out.println("---------");
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                System.out.print(b[i][j] + ",");
            }
            System.out.println();
        }
    }

}
