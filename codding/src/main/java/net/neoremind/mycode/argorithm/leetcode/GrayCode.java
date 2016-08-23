package net.neoremind.mycode.argorithm.leetcode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * The gray code is a binary numeral system where two successive values differ in only one bit.
 * <p>
 * Given a non-negative integer n representing the total number of bits in the code, print the sequence of gray code.
 * A gray code sequence must begin with 0.
 * <p>
 * For example, given n = 2, return [0,1,3,2]. Its gray code sequence is:
 * <p>
 * 00 - 0
 * 01 - 1
 * 11 - 3
 * 10 - 2
 * Note:
 * For a given n, a gray code sequence is not uniquely defined.
 * <p>
 * For example, [0,2,3,1] is also a valid gray code sequence according to the above definition.
 * <p>
 * For now, the judge is able to judge based on one instance of gray code sequence. Sorry about that.
 * <p>
 * Shamelessly, I copied it from Gray Code on Wikipedia.
 * https://en.wikipedia.org/wiki/Gray_code#Converting_to_and_from_Gray_code
 * <p>
 * Gray(N) = (n >> 1) XOR n
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/gray-code/
 */
public class GrayCode {

    /**
     * <pre>
     *
     * This function converts an unsigned binary
     * number to reflected binary Gray code.
     *
     * The operator >> is shift right. The operator ^ is exclusive or.
     *
     * unsigned int binaryToGray(unsigned int num)
     * {
     *     return num ^ (num >> 1);
     * }
     * </pre>
     *
     * @param n
     *
     * @return
     */
    public List<Integer> grayCode(int n) {
        return IntStream.range(0, (int) Math.pow(2, n))
                .map(i -> (i >> 1) ^ i)
                .boxed()
                .collect(Collectors.toList());
    }

    @Test
    public void test() {
        List<Integer> result = grayCode(5);
        result.stream().forEach(e -> System.out.println(Integer.toBinaryString(e)));
    }
}
