package net.neoremind.mycode.argorithm.shuffle;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import net.neoremind.mycode.argorithm.sort.ArrayHelper;

/**
 * https://www.zhihu.com/question/50649396
 * <p>
 * 有点像https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle，但是这个算法需要开辟O(N)的空间，这里是优化的版本
 * <p>
 * 算法导论第五章，先生成一个从1到N的数组A然后下面图中中间伪代码部分描述的算法
 * <p>
 * swap A[i] with A[i+random()*(N-i)]
 *
 * @author zhangxu
 */
public class SimpleShuffle {

    /**
     * copy from {@link java.util.Collections.shuffle();}
     * <p>
     * <pre>
     * index ->  0  1  2  3  4  5  6  7  8  9
     * value ->  A  B  C  D  E  F  G  H  I  J
     * step1：swap(9J, random[0, 9]) like 4E
     *
     * index ->  0  1  2  3  4  5  6  7  8  9
     * value ->  A  B  C  D  J  F  G  H  I  E
     * step2：swap(8I, random[0, 8]) like 2C
     *
     * index ->  0  1  2  3  4  5  6  7  8  9
     * value ->  A  B  I  D  J  F  G  H  C  E
     * step3：swap(7H, random[0, 7])
     * </pre>
     */
    public void shuffle(int[] nums) {
        Random rnd = new Random();
        for (int i = nums.length; i > 1; i--) {
            swap(nums, i - 1, rnd.nextInt(i));
        }
    }

    /**
     * http://introcs.cs.princeton.edu/java/15inout/Shuffle.java.html
     */
    // take as input an array of strings and rearrange them in random order
    public void shuffle2(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));   // between i and n-1
            swap(a, i, r);
        }
    }

    @Test
    public void test() {
        //int[] nums = Stream.generate(Math::random).limit(10).mapToInt(d -> (int) (d * 10)).toArray();
        int[] nums = ArrayHelper.getShuffledArray(10);
        shuffle(nums);
        System.out.println(Arrays.toString(nums));
    }

    protected void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
