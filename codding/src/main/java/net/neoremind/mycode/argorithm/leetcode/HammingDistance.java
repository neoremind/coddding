package net.neoremind.mycode.argorithm.leetcode;

/**
 * The Hamming distance between two integers is the number of positions at which the corresponding bits are different.
 * <p>
 * Given two integers x and y, calculate the Hamming distance.
 * <p>
 * Note:
 * 0 ≤ x, y < 231.
 * <p>
 * Example:
 * <p>
 * Input: x = 1, y = 4
 * <p>
 * Output: 2
 * <p>
 * Explanation:
 * 1   (0 0 0 1)
 * 4   (0 1 0 0)
 * ↑   ↑
 * <p>
 * The above arrows point to positions where the corresponding bits are different.
 *
 * @author xu.zhang
 */
public class HammingDistance {

    public int hammingDistance(int x, int y) {
        int dis = 0;
        for (int i = 0; i < 32; i++) {
            int m = (x >>> (31 - i)) & 1;
            int n = (y >>> (31 - i)) & 1;
            if ((m ^ n) == 1) {
                dis++;
            }
        }
        return dis;
    }

    public int hammingDistance2(int x, int y) {
        int xor = x ^ y, count = 0;
        for (int i = 0; i < 32; i++) {
            count += (xor >> i) & 1;
        }
        return count;
    }

}
