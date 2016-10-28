package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Given an integer n, return 1 - n in lexicographical order.
 * <p>
 * For example, given 13, return: [1,10,11,12,13,2,3,4,5,6,7,8,9].
 * <p>
 * Please optimize your algorithm to use less time and space. The input size may be as large as 5,000,000.
 * <p>
 * https://leetcode.com/problems/lexicographical-numbers/
 *
 * @author zhangxu
 */
public class LexicographicalNumbers {

    /**
     * 结果就是[1..N]的一个排列而已，按照字典序的排列
     * <p>
     * 这种方法在LC上会TLE
     * <p>
     * 以561为例，
     * <pre>
     *     [1, 10, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
     *         11, 110, 111, 112, 113, 114,
     *         ....
     *         197, 198, 199, 2, 20, 200, 201, 202, 203, 204...]
     * </pre>
     * <p>
     * 算法描述：
     * <pre>
     * 初始curr=1
     *
     * loop N个数，把curr加入结果
     *
     * 1）如果curr*10不超过N，那么curr扩大10倍。这样可以保证按照1，10，100，1000...这样的节奏前进。
     * 2）如果curr的最后一位部位9，那么curr++。这里要注意判断curr++不超过N，否则就加错了，会挤掉本该在这个位置的数字。
     * 3）如果curr的末尾为9，那么就找到最后一个数字不为9的那一位，例如3456999，那么就循环(curr/10 % 10) == 9做判断，
     * 例如345699
     *     34569
     * 找到34569，那么curr = curr / 10 + 1 = 34567，正好就是下一个数字。
     * </pre>
     */
    public List<Integer> lexicalOrderInteratively(int n) {
        List<Integer> res = new ArrayList<>(n);
        int curr = 1;
        for (int i = 1; i <= n; i++) {
            res.add(curr);
            if (curr * 10 <= n) {
                curr = curr * 10;
            } else if (curr % 10 != 9 && curr + 1 <= n) {
                curr++;
            } else {
                while ((curr / 10) % 10 == 9) {
                    curr /= 10;
                }
                curr = curr / 10 + 1;
            }
        }
        return res;
    }

    /**
     * 1 - 9的一个森林，用DFS
     * <p>
     * <pre>
     *                                      1
     *                                      /
     *                      [10      ,                 11 ...19]
     *                      /                          /
     *        [100   ,    101,       102 ....  109]  [110-119] ...
     *       /            /
     *    [1000 - 1009]  [1010- 1019]
     * </pre>
     */
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>(n);
        for (int j = 1; j <= 9; j++) {
            dfs(res, j, n);
        }
        return res;
    }

    void dfs(List<Integer> res, int num, int n) {
        if (num > n) {
            return;
        }
        res.add(num);
        for (int i = 0; i <= 9; i++) {
            dfs(res, num * 10 + i, n);
        }
    }

    @Test
    public void test() {
        List<Integer> res = lexicalOrder(199);
        System.out.println(res);

        res = lexicalOrderInteratively(561);
        System.out.println(res);
    }

}
