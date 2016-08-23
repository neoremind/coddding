package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 有重复数全排列问题
 * Given a collection of numbers that might contain duplicates, return all possible unique permutations.
 * <p>
 * For example,
 * [1,1,2] have the following unique permutations:
 * <pre>
 * [
 * [1,1,2],
 * [1,2,1],
 * [2,1,1]
 * ]
 * </pre>
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/permutations-ii/
 * @see net.neoremind.mycode.argorithm.backtracking.Permutation
 * @see Permutation
 */
public class PermutationII {

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        doPermute(nums, 0, result);
        return result;
    }

    private void doPermute(int[] nums, int i, List<List<Integer>> result) {
        if (i == nums.length) {
            List<Integer> numsList = new ArrayList<>(nums.length);
            for (int num : nums) {
                numsList.add(num);
            }
            result.add(numsList);
            System.out.println(numsList);
        } else {
            for (int j = i; j < nums.length; j++) {
                if (isNotSame(nums, i, j)) {
                    swap(nums, i, j);
                    doPermute(nums, i + 1, result);
                    swap(nums, i, j);
                }
            }
        }
    }

    /**
     * 这个函数是个技巧，第一自己跟自己比的时候不判断是否相同，只判断后面的数和固定的数比较
     */
    private boolean isNotSame(int[] nums, int i, int j) {
        for (int k = i; k < j; k++) {
            if (nums[k] == nums[j]) {
                return false;
            }
        }
        return true;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * http://blog.csdn.net/sbitswc/article/details/36154559
     * http://blog.csdn.net/hackbuteer1/article/details/6657435
     * 全排列的算法是一个基础，排列算法在它的基础上增加了选数过程（select），即先选后排。这里面主要是用到了一个递归的思想， 利用递归法来做这题关键下几点：
     * 1.普通情况-取出序列中的一个数并且将剩下的序列全排列
     * 2.特殊情况-序列中只剩一个数，则全排列就是其自身。将增个获得的序列输出。
     * 3.在不止一个数的情况下，该位要分别交换剩下的数（例如：两个数A，B 则有两种情况，一个是AB 一个是BA）
     * https://discuss.leetcode.com/topic/46162/a-general-approach-to-backtracking-questions-in-java-subsets
     * -permutations-combination-sum-palindrome-partioning
     */
    @Test
    public void test() {
        int[] nums = new int[] {1};
        assertThat(permuteUnique(nums).size(), Matchers.is(1));

        nums = new int[] {1, 2};
        assertThat(permuteUnique(nums).size(), Matchers.is(2));

        nums = new int[] {1, 1, 2};
        assertThat(permuteUnique(nums).size(), Matchers.is(3));
    }

}
