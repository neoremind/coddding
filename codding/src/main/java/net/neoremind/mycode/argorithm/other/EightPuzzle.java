package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;

/**
 * 15-puzzle的简单版，8-puzzle
 * <p>
 * wikipedia：https://en.wikipedia.org/wiki/15_puzzle
 * <p>
 * 在线题目：http://migo.sixbit.org/puzzles/fifteen/
 * <p>
 * 基本思路：
 * 所有棋盘的状态有9!个，差不多360,000个。如果DFS太费时间了，可以考虑BFS。
 * <p>
 * BFS思想，看做一颗决策树，每走一步，都把下一步的可能记录在queue里面，下一次从queue里poll出来的，就有可能越接近正确答案，
 * 比DFS要好。
 * <p>
 * 每个棋盘的状态可以看做一个是4*9=36bit<64bit的long，用记忆化搜索存在memorizer里面，当遇到重复的棋盘时候就不要继续BFS了。
 * <p>
 * 如果某个步骤之间走过了，那么就不要BFS了，不要放入队列中，当队列为空时候就证明走不出去了
 * <p>
 * 定义end state就是按顺序 123 456 780组织起来的棋盘。
 * <p>
 * 打印路径使用backtrack
 * <p>
 * 挪动棋子要复制棋盘，先找到0，然后利用d1=new int[]{-1,1,0,0}来做上下左右的swap，出界了就不swap。
 *
 * @author zhangxu
 */
public class EightPuzzle {

    Set<Long> memorizer = new HashSet<>();

    int[] d1 = new int[] {-1, 1, 0, 0};
    int[] d2 = new int[] {0, 0, -1, 1};

    boolean minimalStepSolve(int[][] board) {
        memorizer.clear();
        Queue<Step> queue = new LinkedList<>();
        queue.add(new Step(board, 0, null));
        while (!queue.isEmpty()) {
            Step step = queue.poll();
            memorizer.add(encode(step.board));
            if (isEndState(step.board)) {
                step.printTransformStep();
                return true;
            }
            List<Step> availableSteps = getAvailableSteps(step);
            for (Step s : availableSteps) {
                if (!memorizer.contains(encode(s.board))) {
                    queue.offer(s);
                }
            }
        }
        return false;
    }

    /**
     * 从棋盘中得出下一步的棋盘，例如
     * <pre>
     *     1 2 3
     *     4 5 6
     *     7 0 8
     * </pre>
     * <p>
     * 那么下一步就是把0的上下左右都swap下，但是下明显越界了，所以只有3种步骤可能。
     * <pre>
     *     1 2 3
     *     4 0 6
     *     7 5 8
     *
     *     1 2 3
     *     4 5 6
     *     0 7 8
     *
     *     1 2 3
     *     4 5 6
     *     7 8 0
     * </pre>
     * <p>
     * 这里就可以看出来，如果用BFS，一下子就可以找到最终结果，但是如果用DFS，那么就太慢了，上面两种可能遍历完才会找到。
     */
    List<Step> getAvailableSteps(Step step) {
        List<Step> res = new ArrayList<>();
        int[][] board = step.board;
        int[] hole = new int[] {-1, -1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    hole = new int[] {i, j};
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            int newI = hole[0] + d1[i];
            int newJ = hole[1] + d2[i];
            if (newI >= 0 && newI <= 2 && newJ >= 0 && newJ <= 2) {
                swap(board, hole[0], hole[1], newI, newJ);
                res.add(new Step(board, step.depth + 1, step));
                swap(board, hole[0], hole[1], newI, newJ);
            }
        }
        return res;
    }

    void swap(int[][] board, int i, int j, int x, int y) {
        int temp = board[i][j];
        board[i][j] = board[x][y];
        board[x][y] = temp;
    }

    /**
     * 是否是最终的结果，棋盘如下：
     * <pre>
     *     1 2 3
     *     4 5 6
     *     7 8 0
     * </pre>
     */
    boolean isEndState(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 2 && j == 2) {
                    break;
                }
                if (board[i][j] != i * 3 + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 为了记忆化搜索，存储每一个棋盘的一个encoding，这里使用long
     * <p>
     * 因为每一个棋格里面存0-9，共需要4个bit，因此一共要4*9=36<64bit
     */
    Long encode(int[][] board) {
        long res = 0;
        int idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                long temp = board[i][j] << (idx++ * 4);
                res += temp;
            }
        }
        return res;
    }

    class Step {
        // 棋盘
        int[][] board;
        // BFS的深度，决策树的高度
        int depth;
        // 前一步步骤，用于backtrack打印步骤
        Step prev;

        Step(int[][] board, int depth, Step prev) {
            this.board = clone(board); //要clone一个棋盘出来
            this.depth = depth;
            this.prev = prev;
        }

        int[][] clone(int[][] board) {
            int[][] res = new int[board.length][board[0].length];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    res[i][j] = board[i][j];
                }
            }
            return res;
        }

        void printTransformStep() {
            if (prev != null) {
                prev.printTransformStep();
            }
            printStep(this);
        }

        void printStep(Step step) {
            System.out.println("======= depth " + step.depth);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(step.board[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    @Test
    public void test() {
        int[][] board = new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0},
        };
        boolean res = minimalStepSolve(board);
        System.out.println(res);

        board = new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 8},
        };
        res = minimalStepSolve(board);
        System.out.println(res);

        board = new int[][] {
                {1, 0, 2},
                {7, 4, 3},
                {5, 8, 6},
        };
        res = minimalStepSolve(board);
        System.out.println(res);
    }

}
