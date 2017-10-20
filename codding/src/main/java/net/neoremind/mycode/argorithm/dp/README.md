## DP动态规划

### 已解决
* 硬币找零[OK]

状态：d[i]表示面额为1的情况下，最少需要多少硬币
状态转移方程：d[i] = Min{d[i-Pj] + 1, Pj<i}  + 1
       1     Pj = i
       -1    Pj>i not possible
Pj表示第j个硬币的面值

* 最大连续子序列和[OK-53. Maximum Subarray]

状态：s[i]表示以i结尾最大的子数组和，如果s[i - 1]小于0了，则说明在i肯定是保留现在的值更大，否则就叠加前面的s[i - 1]
状态转移方程：s[i] = Max{s[i] (s[i - 1] < 0), s[i - 1] + v[i]}

* 01背包、挖金矿[OK]

状态：状态d(i,j)表示前i个物品装到剩余体积为j的背包里能达到的最大价值，
状态转移方程：f[i,j] = Max{f[i-1, j-Wi] + Pi( j >= Wi ),  f[i-1, j] }
二维矩阵，长度+1，横轴为capacity，纵轴为item数量。

* 矩阵从左上到右下的路线总数[OK-leetcode62/63]

状态：走到i,j点的路线的总数，依赖于上面和左面的步骤佳和。
状态转移方程：P[i,j]=P[i - 1][j] + P[i][j - 1]

* 矩阵从左上到右下的最短步数，收集苹果[OK-leetcode64]

状态：s[i, j]表示走到i,j点的最短步数
状态转移方程：s[i][j] = Min{s[i - 1][j] + s[i][j - 1]} + grid[i][j]

* 最长递增子序列(Longest Increasing Subsequence,LIS)[OK-leetcode300]

状态：d(i)表示到i位置的最长递增子序列的长度
状态转移方程：d(i) = max{1, d(j) + 1},其中j<i,A[j]<=A[i]

* Fibonacci[OK]

1、1、2、3、5、8、13、21、34...

递归的
```
if (n == 0) return 0;
if (n == 1) return 1;
return recursiveFibonacci(n - 1) + recursiveFibonacci(n - 2);
```

非递归的，空间复杂度N
```
int[] fib = new int[n + 1];
for (int i = 0; i < fib.length; i++) {
    if (i == 0) {fib[i] = 0;
    } else if (i == 1) { fib[i] = 1;
    } else fib[i] = fib[i - 2] + fib[i - 1];
return fib[n];
```

非递归，空间复杂度1
```
if (n < 2) return n;
int dp[] = new int[] {0, 1, 1};
for (int i = 2; i <= n; i++) {
    dp[2] = dp[0] + dp[1];
    dp[0] = dp[1];
    dp[1] = dp[2];
return dp[2];
```

* Climbing stairs[OK]

类似Fibonacci
```
if (n < 2) return n;
int dp[] = new int[] {1, 1, 2};
for (int i = 2; i <= n; i++) {
    dp[2] = dp[0] + dp[1];
    dp[0] = dp[1];
    dp[1] = dp[2];
}
return dp[2];
```

* 最长公共子序列(Longest Common Subsequence,LCS)[OK]

状态：C[i,j]表示字符串A的i位置和字符串B的j位置的最长公共子序列。
状态转移方程：
```
           0                               if i = 0 or j = 0
C[i, j] =  C[i - 1, j - 1] + 1             if i,j > 0 and Xi = Yj
           max{C[i, j - 1] , c[i - 1, j]}  if i,j > 0 and Xi != Yj
```

举例：
```
        G　　C　　T　  A　
　　0　　0　　0　　0　　0
G　 0　　1　　1　　1　　1
B　 0　　1　　1　　1　　1
T　 0　　1　　1　　2　　2
A   0　　1　　1　　2　　3 
```

扩展回溯方法找路径。

* 最长公共子串(Longest Common Substring,LCS)[OK]

最长公共子序列(Subsequence)是不连续的字符，最长公共子串(Substring)必须是连续的字符。

状态：C[i, j]表示走到第一个字符串到i位置和第二个字符串到j位置，连续的公共子串的长度。
状态转移方程：
```
           0                               if i = 0 or j = 0
C[i, j] =  C[i - 1, j - 1] + 1             if i,j > 0 and Xi = Yj
           0    
```
和{@link LCS#longestCommonSubsequenceDP(String, String)}非常类似，只是当i>0,j>0并且Xi!=Yj时，需要归零，并且要记录最长子串的end和长度

举例
```
　　 b　　a　　b
c　　0　　0　　0
a　　0　　1　　0
b　　1　　0　　2
a　　0　　2　　0
```

* 子序列个数[OK]

* 字符串相似度/编辑距离（edit distance）[OK]

状态：d[i, j]表示走到第一个字符串到i位置和第二个字符串到j位置，最短的编辑距离。
状态转移方程：
```
d[i][0] = i
d[0][j] = j
d[i][j] = d[i-1][j-1] if word1[i-1]=word2[j-1]
d[i][j] = min(d[i-1][j-1] + 1, d[i-1][j] + 1, d[i][j-1] + 1)
```
d矩阵长度等于各个单词+1
如果相同，那么就是个西北角的子问题，否则就是北、西北、西三者的最小值决定。

* Triangle三角形从顶到底找最短路[OK-leetcode120]

状态：f[i, j]表示走到第一个第i行，第j列位置的最短路径。
状态转移方程：
```
         f(i - 1, j) + V(i,j) j=0
f(i, j) = Min{f(i-1,j-1), f(i-1,j)} + V(i,j) 0<j<len-1
         f(i - 1, j - 1) + V(i,j) j=len-1
```

* Decode Ways

12 有两种decode ways，分别是AB和L。

状态：d[i]表示到第i个字符可以有多少种decode组合。
状态转移方程：
```
d[i] = (d[i-2] if s.sub[i-2, i] > '10' & < '26' else 0) + d[i-1] if s.substring[i-1,i] >= '1' & <= '9' else 0
d[0] = 1
d[1] = 1
``` 

* UniqueBinarySearchTrees
```
Taking 1~n as root respectively:
 * 1 as root: # of trees = F(0) * F(n-1)  // F(0) == 1
 * 2 as root: # of trees = F(1) * F(n-2)
 * 3 as root: # of trees = F(2) * F(n-3)
 * ...
 * n-1 as root: # of trees = F(n-2) * F(1)
 * n as root:   # of trees = F(n-1) * F(0)
 * <p>
 * So, the formulation is:
 * F(n) = F(0) * F(n-1) + F(1) * F(n-2) + F(2) * F(n-3) + ... + F(n-2) * F(1) + F(n-1) * F(0)
 * <p>
 * <p>
 * F(0) = 1
 * F(1) = 1
 * F(2) = F(0)*F(1) + F(1)*F(0) = 1*1 + 1*1 = 2
 * F(3) = F(0)*F(2) + F(1)*F(1) + F(2)*F(0) = 1*2 + 1*1 + 1*2 = 5
 * F(4) = F(0)*F(3) + F(1)*F(2) + F(2)*F(1) + F(0)*F(3) = 1*5 + 1*2 + 2*1 + 5*1 = 14
 * F(5) = F(0)*F(4) + F(1)*F(3) + F(2)*F(2) + F(3)*F(1) + F(4)*F(0) = 1*14 + 1*5 + 2*2 + 5*1 * 14*1 = 42
```

* word break
给一个单词，和一个数组，看是否可以拼出单词来。
```
 * s = "leetcode",
 * dict = ["leet", "code"].
```

* Maximum Product Subarray

最大连续子数组的乘积
```
max * newnumber, min*newnumber, newnumber这三个肯定会出现max和min就得判断下。
```

* Best Time to Buy and Sell Stock

```
状态转移方程，d(j)表示在j这一天出售股票的最大收益：
d(j) = Vj - Min(Vi, i in [0, j-1])
```

* House Robber

和01背包问题很像。动态规划，设置maxV[i]表示到第i个房子位置，最大收益。

递推关系为maxV[i] = max(maxV[i-2]+num[i], maxV[i-1])

* Maximal Square

和Maximal Rectangle以及Maximal Histogram看起来很像，但是完全不一样，这是DP问题。

dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i - 1][j - 1], dp[i][j - 1])) + 1;

```
0 1 1 1
0 1 1 1
0 1 1 1

dp矩阵是
0 1 1 1
0 1 2 2
0 1 2 3
```

### 未解决
* Max sum sub matrix

https://github.com/careermonk/DataStructureAndAlgorithmsMadeEasyInJava/blob/master/src/chapter19dynamicprogramming/MaximumSumSubMatrix.java

* Max sub square of the matrix with 1s

https://github.com/careermonk/DataStructureAndAlgorithmsMadeEasyInJava/blob/master/src/chapter19dynamicprogramming/MaxSubSquareMatrixWith1s.java

* 矩阵链乘法

