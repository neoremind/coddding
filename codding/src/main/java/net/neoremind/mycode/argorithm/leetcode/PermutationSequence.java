package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;

/**
 * The set [1,2,3,…,n] contains a total of n! unique permutations.
 * <p>
 * By listing and labeling all of the permutations in order,
 * We get the following sequence (ie, for n = 3):
 * <p>
 * "123"
 * "132"
 * "213"
 * "231"
 * "312"
 * "321"
 * Given n and k, return the kth permutation sequence.
 * <p>
 * Note: Given n will be between 1 and 9 inclusive.
 * <p>
 * using wikipedia example
 * <p>
 * 2982th permutation of [1, 2, 3, 4, 5, 6, 7].
 * <p>
 * Convert k to factorial based number
 * <p>
 * k = 2982
 * n = 7
 * start form n - 1
 * <p>
 * 2982 / 6! = 4 and remainder 102     |  4
 * 102  / 5! = 0 and remainder 102     |  0
 * 102  / 4! = 4 and remainder 6       |  4
 * 6    / 3! = 1 and remainder 0       |  1
 * 0    / 2! = 0 and remainder 0       |  0
 * 0    / 1! = 0 and remainder 0       |  0
 * 0    / 0! = 0 and remainder 0       |  0
 * So, 2982(10base) = 4041000(!base)
 * <p>
 * Recover factorial based number to permutation.
 * <p>
 * index:  0  1  2  3  4  5  6
 * chars: [1, 2, 3, 4, 5, 6, 7]
 * final: []
 * <p>
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[4] to the end of final
 * <p>
 * <p>
 * chars: [1, 2, 3, 4, 5, 6, 7]  BEFORE
 * index:  0  1  2  3  4  5, 6
 * final: [5]
 * chars: [1, 2, 3, 4, 6, 7]     AFTER
 * <p>
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[0] to the end of final
 * <p>
 * chars: [1, 2, 3, 4, 6, 7]     BEFORE
 * index:  0  1  2  3  4  5
 * final: [5, 1]
 * chars: [2, 3, 4, 6, 7]        AFTER
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[4] to the end of final
 * <p>
 * chars: [2, 3, 4, 6, 7]       BEFORE
 * index:  0  1  2  3  4
 * final: [5, 1, 7]
 * chars: [2, 3, 4, 6]          AFTER
 * <p>
 * <p>
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[1] to the end of final
 * <p>
 * chars: [2, 3, 4, 6]          BEFORE
 * index:  0  1  2  3
 * final: [5, 1, 7, 3]
 * chars: [2, 4, 6]             AFTER
 * <p>
 * <p>
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[0] to the end of final
 * <p>
 * chars: [2, 4, 6]             BEFORE
 * index:  0  1  2
 * final: [5, 1, 7, 3, 2]
 * chars: [4, 6]                AFTER
 * <p>
 * <p>
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[0] to the end of final
 * <p>
 * chars: [4, 6]                BEFORE
 * index:  0  1
 * final: [5, 1, 7, 3, 2, 4]
 * chars: [6]                   AFTER
 * <p>
 * <p>
 * number: 4041000
 * ^
 * |
 * <p>
 * Move chars[0] to the end of final
 * <p>
 * chars: [6]                   BEFORE
 * index:  0
 * final: [5, 1, 7, 3, 2, 4, 6]
 * chars: []                    AFTER
 * The 2982th permutation of [1, 2, 3, 4, 5, 6, 7] is [5, 1, 7, 3, 2, 4, 6]
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/permutation-sequence/
 */
//TODO
public class PermutationSequence {

    int fact(int x) {
        int s = 1;
        for (int i = 1; i <= x; i++) {
            s *= i;
        }
        return s;
    }

    public String getPermutation(int n, int k) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        ArrayList<Character> chars = new ArrayList<Character>();
        for (int i = '1'; i < ('1' + n); i++) {
            chars.add((char) i);
        }
        k -= 1;
        char[] t = new char[n];
        int l = 0;
        for (int i = n - 1; i > 0; i--) {
            int f = fact(i);
            int c = k / f;
            t[l++] = chars.get(c);
            chars.remove(c);
            k %= f;
        }
        t[n - 1] = chars.get(0);
        return new String(t);
    }

}
