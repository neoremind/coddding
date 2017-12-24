package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given two integers n and k, return all possible combinations of k numbers out of 1 ... n.
 * <p>
 * For example,
 * If n = 4 and k = 2, a solution is:
 * <p>
 * [
 * [2,4],
 * [3,4],
 * [2,3],
 * [1,2],
 * [1,3],
 * [1,4],
 * ]
 * <p>
 * 和{@link CombinationSum}的模板一模一样
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/combinations/
 */
public class Combinations {

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(n, k, 1, new ArrayList<>(2), result);
        return result;
    }

    private void backtrack(int n, int k, int i, List<Integer> tempList, List<List<Integer>> result) {
        if (tempList.size() == k) {
            result.add(new ArrayList<>(tempList));
        } else {
            for (int j = i; j <= n; j++) {
                tempList.add(j);
                backtrack(n, k, j + 1, tempList, result);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    public List<List<Integer>> combine2(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        helper(res, new ArrayList<>(), 1, n, k);
        return res;
    }

    void helper(List<List<Integer>> res, List<Integer> temp, int start, int n, int k) {
        if (k == 0) {
            res.add(new ArrayList<>(temp));
        } else {
            for (int i = start; i <= n; i++) {
                temp.add(i);
                helper(res, temp, i + 1, n, k - 1);
                temp.remove(temp.size() - 1);
            }
        }
    }

    @Test
    public void test() {
        List<List<Integer>> list = combine(4, 2);
        list.stream().forEach(System.out::println);
        assertThat(list.size(), Matchers.is(6));

        list = combine(5, 2);
        list.stream().forEach(System.out::println);
        assertThat(list.size(), Matchers.is(10));

        list = combine(5, 3);
        list.stream().forEach(System.out::println);
        assertThat(list.size(), Matchers.is(10));
    }
}
