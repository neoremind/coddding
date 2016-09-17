package net.neoremind.mycode.argorithm.codility;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class Equi3 {

    /**
     * First aggregate sums from 0 to len -1.
     * <p>
     * Using two pointers, left pointer goes from A[1] while right pointer goes from A[len - 2],
     * Compare the left sum, middle sum and right sum to see if there are equal. If result meets expectation,
     * then we know we find the answer, else we increase the left pointer or right pointer or both accordingly.
     * Here we leverage the aggregation sum from the step so we do not need to traverse up and down, so we
     * can control the time complexity to O(N).
     * <p>
     * Until we reach <code>left + 1 = right</code> criteria, we know there is no answer then return 0.
     * <p>
     * This space complexity is O(N) since we aggregate sum at the first step.
     *
     * @param A
     *
     * @return
     */
    public int solution(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        int[] sum = new int[A.length];
        sum[0] = A[0];
        for (int i = 1; i < A.length; i++) {
            sum[i] = sum[i - 1] + A[i];
        }
        int left = 1;
        int right = A.length - 2;
        while (left + 1 < right) {
            int leftSum = sum[left - 1];
            int rightSum = sum[A.length - 1] - sum[right];
            if (leftSum == rightSum && (sum[right - 1] - sum[left]) == leftSum) {
                return 1;
            } else {
                if (leftSum > rightSum) {
                    right--;
                } else if (leftSum < rightSum) {
                    left++;
                } else {
                    left++;
                    right--;
                }
            }
        }
        return 0;
    }

    @Test
    public void test() {
        int[] A = new int[] {4, 5, 1, 1, 1, 1, 4, 3, 1};
        assertThat(solution(A), Matchers.is(1));
    }

}
