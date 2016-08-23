package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Find all possible combinations of k numbers that add up to a number n, given that only numbers from 1 to 9 can be
 * used and each combination should be a unique set of numbers.
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: k = 3, n = 7
 * <p>
 * Output:
 * <p>
 * [[1,2,4]]
 * <p>
 * Example 2:
 * <p>
 * Input: k = 3, n = 9
 * <p>
 * Output:
 * <p>
 * [[1,2,6], [1,3,5], [2,3,4]]
 * <p>
 * {@link CombinationSum}非常类似的模板！！！！
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/combination-sum-ii/
 */
public class CombinationSumIII {

    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> list = new ArrayList<>();
        backtrack(list, new ArrayList<>(), k, n, 1);
        return list;
    }

    private void backtrack(List<List<Integer>> list, List<Integer> tempList, int k, int remain, int currDigit) {
        if (k < 0 || remain < 0) {
            return;
        } else if (remain == 0 && k == 0) {  // 去掉这个K==0就会输出各种组合，不会保证有K个数字
            list.add(new ArrayList<>(tempList));
        } else {
            for (int i = currDigit; i < 10; i++) {
                tempList.add(i);
                backtrack(list, tempList, k - 1, remain - i, i + 1);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    @Test
    public void test() {
        List<List<Integer>> list = combinationSum3(3, 7);
        list.stream().forEach(System.out::println);
        assertThat(list.size(), Matchers.is(1));

        list = combinationSum3(3, 9);
        list.stream().forEach(System.out::println);
        assertThat(list.size(), Matchers.is(3));
    }
}