package net.neoremind.mycode.argorithm.leetcode;

/**
 * A message containing letters from A-Z is being encoded to numbers using the following mapping way:
 * <p>
 * 'A' -> 1
 * 'B' -> 2
 * ...
 * 'Z' -> 26
 * Beyond that, now the encoded string can also contain the character '*', which can be treated as one of the numbers
 * from 1 to 9.
 * <p>
 * Given the encoded message containing digits and the character '*', return the total number of ways to decode it.
 * <p>
 * Also, since the answer may be very large, you should return the output mod 109 + 7.
 * <p>
 * Example 1:
 * <p>
 * Input: "*"
 * Output: 9
 * Explanation: The encoded message can be decoded to the string: "A", "B", "C", "D", "E", "F", "G", "H", "I".
 * <p>
 * <p>
 * Example 2:
 * <p>
 * Input: "1*"
 * Output: 9 + 9 = 18
 * <p>
 * <p>
 * Note:
 * <p>
 * The length of the input string will fit in range [1, 105].
 * The input string will only contain the character '*' and digits '0' - '9'.
 * <p>
 * 和{@link DecodeWays}类似，引入了星号，可以代表1到9之间的任意数字，但是难度加大了不少，是个HARD题目。
 * <p>
 * 有了星号以后，整个题就变得异常的复杂，所以结果才让我们对一个很大的数求余，避免溢出。所以对一个很大的数字取模。
 * <p>
 * 精髓：各种情况都要考虑清楚。其实就是考虑1，2，26，*这些关系。别不合法。
 * <p>
 * 还是DP，状态dp[i]表示前i个字符的解码方法等个数，长度为字符串的长度加1，状态转移方程的分支很多。下面介绍：
 * <p>
 * 初始化：
 * 将dp[0]初始化为1，然后我们判断，如果字符串第一个字符是0，那么直接返回0，如果是*，则dp[1]初始化为9，否则初始化为1。
 * <p>
 * 一共三个分支：s[i-1]表示当前的字符
 * <p>
 * 分支考虑的情况都是先看单独一个s[i-1]能否成立，然后再看s[i-2,i-1]组成一个数字是否成立，考虑1和2 + * 三种子情况。
 * <p>
 * 分支1：s[i-1]==0
 * 先看单独一个s[i-1]能否成立：单独一个0肯定不行，所以不能加可能。
 * 然后再看s[i-2,i-1]组成一个数字是否成立：
 * 分支1-1：s[i-2]==1 or 2 有dp[i-2]种可能
 * 分支1-2：s[i-2]==*      要么为1，要么为2，所以有2*dp[i-2]种可能
 * 分支1-3：其他情况都return 0退出去，因为根本组成不了合法的数字。不合法。
 * <p>
 * 分支2：s[i-1] := [1,9]
 * 先看单独一个s[i-1]能否成立：单独一个数字成立，所以先至少有dp[i-1]中可能。
 * 然后再看s[i-2,i-1]组成一个数字是否成立：
 * 分支2-1：s[i-2]==1      有dp[i-2]种可能
 * 分支2-2：s[i-2]==2 and s[i-1] := [1,6]      有dp[i-2]种可能
 * 分支2-3：s[i-2]==*      如果 s[i-1] := [1,6]那么就可以选择1或者2，就是2*dp[i-2]种可能，否则就是dp[i-2]种可能
 * <p>
 * 分支3：s[i-1]==*
 * 先看单独一个s[i-1]能否成立：单独一个数字成立，先至少有9*dp[i-1]中可能。
 * 然后再看s[i-2,i-1]组成一个数字是否成立：
 * 分支3-1：s[i-2]==1，有9*dp[i-2]种可能
 * 分支3-2：s[i-2]==2  有6*dp[i-2]种可能
 * 分支3-3：s[i-2]==*  有（9+6）*dp[i-2]种可能
 * <p>
 * 最后的结果别忘记%10^9+7,dp要是long类型的。
 * <p>
 * 上面就是对这段话的总结：http://www.cnblogs.com/grandyang/p/7279152.html
 * 这道解码的题是之前那道Decode
 * Ways的拓展，难度提高了不少，引入了星号，可以代表1到9
 * 之间的任意数字，是不是有点外卡匹配的感觉。有了星号以后，整个题就变得异常的复杂，所以结果才让我们对一个很大的数求余，避免溢出。这道题的难点就是要分情况种类太多，一定要全部理通顺才行。我们还是用DP来做，建立一个一维dp
 * 数组，其中dp[i]表示前i个字符的解码方法等个数，长度为字符串的长度加1。将dp[0]初始化为1，然后我们判断，如果字符串第一个字符是0，那么直接返回0，如果是*，则dp[1]初始化为9，否则初始化为1
 * 。下面就来计算一般情况下的dp[i]了，我们从i=2开始遍历，由于要分的情况种类太多，我们先选一个大分支，就是当前遍历到的字符s[i-1]，只有三种情况，要么是0，要么是1到9的数字，要么是星号。我们一个一个来分析：
 * <p>
 * 首先来看s[i-1]为0的情况，这种情况相对来说比较简单，因为0不能单独拆开，只能跟前面的数字一起，而且前面的数字只能是1或2，其他的直接返回0即可。那么当前面的数字是1或2的时候，dp[i]的种类数就跟dp[i-2
 * ]相等，可以参见之前那道Decode Ways的讲解，因为后两数无法单独拆分开，就无法产生新的解码方法，所以只保持住原来的拆分数量就不错了；如果前面的数是星号的时候，那么前面的数可以为1或者2，这样就相等于两倍的dp[i-2
 * ]；如果前面的数也为0，直接返回0即可。
 * <p>
 * 再来看s[i-1]为1到9之间的数字的情况，首先搞清楚当前数字是可以单独拆分出来的，那么dp[i]至少是等于dp[i-1
 * ]的，不会拖后腿，还要看其能不能和前面的数字组成两位数进一步增加解码方法。那么就要分情况讨论前面一个数字的种类，如果当前数字可以跟前面的数字组成一个小于等于26的两位数的话，dp[i]还需要加上dp[i-2
 * ]；如果前面的数字为星号的话，那么要看当前的数字是否小于等于6，如果是小于等于6，那么前面的数字就可以是1或者2了，此时dp[i]需要加上两倍的dp[i-2]，如果大于6，那么前面的数字只能是1，所以dp[i]只能加上dp[i-2]。
 * <p>
 * 最后来看s[i-1]为星号的情况，如果当前数字为星号，那么就创造9种可以单独拆分的方法，所以那么dp[i]至少是等于9倍的dp[i-1
 * ]，还要看其能不能和前面的数字组成两位数进一步增加解码方法。那么就要分情况讨论前面一个数字的种类，如果前面的数字是1，那么当前的9种情况都可以跟前面的数字组成两位数，所以dp[i]需要加上9倍的dp[i-2]；如果前面的数字是2
 * ，那么只有小于等于6的6种情况都可以跟前面的数字组成两位数，所以dp[i]需要加上6倍的dp[i-2]；如果前面的数字是星号，那么就是上面两种情况的总和，dp[i]需要加上15倍的dp[i-2]。
 *
 * @author xu.zhang
 */
public class DecodeWays2 {

    int M = 1000000007;

    public int numDecodings(String s) {
        if (s == null || s.isEmpty()) return 0;
        long[] dp = new long[s.length() + 1];
        if (s.charAt(0) == '0') return 0;
        dp[0] = 1;
        if (s.charAt(0) == '*') {
            dp[1] = 9;
        } else {
            dp[1] = 1;
        }
        for (int i = 2; i <= s.length(); i++) {
            char curr = s.charAt(i - 1);
            char prev = s.charAt(i - 2);
            if (curr == '0') {
                // invalid is just 0
                if (prev == '1' || prev == '2') {
                    dp[i] += dp[i - 2];
                } else if (prev == '*') {
                    dp[i] += 2 * dp[i - 2];
                } else {
                    return 0; //invalid here
                }
            } else if (curr >= '1' && curr <= '9') {
                dp[i] += dp[i - 1];
                if (prev == '1') {
                    dp[i] += dp[i - 2];
                } else if (prev == '2' && (curr >= '1' && curr <= '6')) {
                    dp[i] += dp[i - 2];
                } else if (prev == '*') {
                    dp[i] += (curr >= '1' && curr <= '6') ? 2 * dp[i - 2] : dp[i - 2];
                }
            } else if (curr == '*') {
                dp[i] += 9 * dp[i - 1];
                if (prev == '1') {
                    dp[i] += 9 * dp[i - 2];
                } else if (prev == '2') {
                    dp[i] += 6 * dp[i - 2];
                } else if (prev == '*') {
                    dp[i] += 15 * dp[i - 2];
                }
            }
            dp[i] = dp[i] % M;
        }
        return (int) dp[s.length()];
    }
}
