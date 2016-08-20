package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * N皇后问题，要求输出所有的解。见evernote
 * <p>
 * 解题思路：
 * 1、暴力解法，8个皇后64种位置，那么可以O(64^8)时间复杂度解决，理论可行，实际不可行。
 * <p>
 * 2、已知肯定每个皇后放在不同的8行上，那么构造一个数组[1, 2, 3, 4, 5, 6, 7, 8]，索引表示行，值表示放在第几列上，可以转换为一个全排列问题，然后结合一个判断是否可行的函数即可，O(8!)
 * ，和#1相比是巨大的提升。记住一个解法84136275 :-)
 * <p>
 * 3、采用DFS以及回溯backtracking的思想，近似于穷举暴力，来进行“剪枝”，最差也是O(8!)，但实际上不会这么差。
 * <p>
 * <p>
 * DFS是一种brute force的方法，有一个pattern可以遵循
 * selected_choice stores [step0: choice_x, step1: choice_y, ...]
 * <pre>
 * dfs(step0) {
 *     if all steps had solutions
 *         selected_choices is an answer
 *     foreach choice in all choices in current step
 *         if choice is possible
 *             selected_choices[step] = choice
 *             dfs(next step)
 *             selected_choices[step] = no choice or last choice // this is called backtracking
 * }
 * </pre>
 * 放在8皇后问题上，
 * step就是N行
 * choices each step就是在某一行上可以放置的N列，有N个选择
 * possible checker就是这个grid上，纵列没有皇后，对角线（diagonal）包括45度还有135度没有皇后
 * <p>
 * 伪代码如下：
 * fake board
 * <pre>
 * a boolean[n][n] array to simulate a chessboard
 *  </pre>
 * chessboard[row][column] = true is for putting a queen
 * possibility checker function:
 * <pre>
 * function check_can_put(row, col)
 *     if queen on same column
 *         return false
 *     if queen on same diagonal
 *         return false
 * </pre>
 * DFS function
 * <pre>
 * function N_queen(row)
 *     if row > N
 *         convert chessboard into result
 *     else
 *         foreah col in current row
 *             if check_can_put(row, col)
 *                 // put queen
 *                 chessboard[row][col] = true
 *                 N_queen(row + 1)
 *                 // take queen away
 *                 chessboard[row][col] = false
 * </pre>
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/n-queens/
 */
public class NQueens {

    public List<List<String>> solveNQueens(int n) {
        char[][] chessboard = new char[n][n];
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[0].length; j++) {
                chessboard[i][j] = '.';
            }
        }
        List<List<String>> result = new ArrayList<>();
        solveNQueens(chessboard, 0, result);
        return result;
    }

    public void solveNQueens(char[][] chessboard, int n, List<List<String>> result) {
        if (n == chessboard.length) {
            result.add(chessboardToString(chessboard));
        } else {
            for (int i = 0; i < chessboard[0].length; i++) {
                if (isValid(chessboard, n, i)) {
                    chessboard[n][i] = 'Q';
                    solveNQueens(chessboard, n + 1, result);
                    chessboard[n][i] = '.';
                }
            }
        }
    }

    public List<String> chessboardToString(char[][] chessboard) {
        List<String> result = new ArrayList<>(chessboard.length);
        for (int i = 0; i < chessboard.length; i++) {
            result.add(new String(chessboard[i]));
        }
        return result;
    }

    /**
     * 这里有一个可改进的技巧，就是判断是否皇后合法的函数里面，可以按照如下的公式进行计算
     * 主对角线（45度）上的i - j为定值
     * 辅对角线（135度）上的i + j为定值，因此可以做一个数组来记录，省去多次循环判断。
     */
    public boolean isValid(char[][] chessboard, int x, int y) {
        int rowLen = chessboard.length;
        int colLen = chessboard[0].length;
        // check single column
        for (int i = 0; i < chessboard.length; i++) {
            if (chessboard[i][y] == 'Q') {
                return false;
            }
        }
        // no need to check single row
        // check 45 diagonal
        for (int i = x, j = y; i >= 0 && j >= 0; i--, j--) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        for (int i = x, j = y; i < rowLen && j < colLen; i++, j++) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        // check 135 diagonal
        for (int i = x, j = y; i < rowLen && j >= 0; i++, j--) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        for (int i = x, j = y; i >= 0 && j < colLen; i--, j++) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }

        return true;
    }

    @Test
    public void test() {
        List<List<String>> result = solveNQueens(8);
        System.out.println("Total " + result.size() + " solutions");
        result.stream().forEach(e -> {
            e.forEach(System.out::println);
            System.out.println();
        });
    }

}
