package net.neoremind.mycode.argorithm.leetcode;

import java.util.List;

/**
 * Given a triangle, find the minimum path sum from top to bottom. Each step you may move to adjacent numbers on the
 * row below.
 * <p>
 * For example, given the following triangle
 * [
 * [2],
 * [3,4],
 * [6,5,7],
 * [4,1,8,3]
 * ]
 * The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).
 * <p>
 * Note:
 * Bonus point if you are able to do this using only O(n) extra space, where n is the total number of rows in the
 * triangle.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/triangle/
 */
public class Triangle {

    /**
     * <pre>
     *               f(i - 1, j) + V(i,j) j=0
     *     f(i, j) = Min{f(i-1,j-1), f(i-1,j)} + V(i,j) 0<j<len-1
     *              f(i - 1, j - 1) + V(i,j) j=len-1
     * </pre>
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        int width = triangle.get(triangle.size() - 1).size();
        int height = triangle.size();
        int[][] s = new int[height][width];
        for (int i = 0; i < height; i++) {
            List<Integer> list = triangle.get(i);
            for (int j = 0; j < list.size(); j++) {
                if (i - 1 < 0) {
                    s[i][j] = list.get(j);
                } else if (j - 1 < 0) {
                    s[i][j] = s[i - 1][j] + list.get(j);
                } else if (j == list.size() - 1) {
                    s[i][j] = s[i - 1][j - 1] + list.get(j);
                } else {
                    s[i][j] = Math.min(s[i - 1][j - 1], s[i - 1][j]) + list.get(j);
                }
            }
        }
        // Arrays.asList(s).stream().forEach(e -> System.out.println(Arrays.toString(e)));
        int[] bottom = s[height - 1];
        int min = Integer.MAX_VALUE;
        for (int i : bottom) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

}
