package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 子序列个数
 * <p>
 * 子序列的定义：对于一个序列a=a[1],a[2],......a[n]，则非空序列a'=a[p1],a[p2]......a[pm]为a的一个子序列
 * 其中1<=p1<p2<.....<pm<=n。 例如：4,14,2,3和14,1,2,3都为4,13,14,1,2,3的子序列。
 * 对于给出序列a，请输出不同的子序列的个数。
 * <p>
 * f(k)的个数包括f(k-1)的个数，因为f(k-1)的每一个都是f(k)的子序列，然后把f(k-1)的每个序列和a[k]组合起来，这些序列也是f(k)的子序列，个数还是f(k-1),载加上单独的a[k]，那么
 * <pre>
 *     f(k)=2*f(k-1)+1
 * </pre>
 * 上面这个表达式是当a[k]和前面的数都不同的时候的情况，如果a[k]在前面出现过的话，那f(k)的个数除了上面那些的话：
 * 还需要减去最近一次出现a[k]这个数值的地方-1的子序列个数，因为这些算重复了
 * +1也没有了，因为f(a[k]上次出现的位置)包括了a[k]单独算一次的情况
 * <pre>
 *     f(k)=2*f(k-1)-f(a[k]上次出现的位置-1)
 * </pre>
 *
 * @author zhangxu
 */
public class SubseqNum {

    public int subSeqNum(int[] a) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        int[] subArray = new int[max + 1];
        for (int i = 0; i <= max; i++) {
            subArray[i] = Integer.MIN_VALUE;
        }
        subArray[a[0]] = 1;
        int[] dp = new int[a.length];
        dp[0] = 1;
        for (int i = 1; i < a.length; i++) {
            if (subArray[a[i]] == Integer.MIN_VALUE) {
                dp[i] = (dp[i - 1] * 2) + 1;
            } else {
                dp[i] = ((dp[i - 1] * 2) - dp[subArray[a[i - 1]] - 1]);
            }
            subArray[a[i]] = i;
        }
        System.out.println(Arrays.toString(subArray));
        return dp[a.length - 1];
    }

    @Test
    public void test() {
        int[] a = new int[] {1, 2};
        assertThat(subSeqNum(a), Matchers.is(3));
        a = new int[] {1, 2, 3};
        assertThat(subSeqNum(a), Matchers.is(7));
        a = new int[] {1, 2, 4, 3};
        assertThat(subSeqNum(a), Matchers.is(15));
        a = new int[] {1, 2, 4, 3, 4};
        assertThat(subSeqNum(a), Matchers.is(23));
    }

}
