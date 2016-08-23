# Leetcode records

### [322. Coin Change](https://leetcode.com/problems/coin-change/)

M, Dynamic Programming

举例，当3元时候，需要计算去掉1元硬币和3元硬币后的最少数量+1，即d(3)=min{d(3-1)+1, d(3-3)+1}。
1元和3元的挑选是所有的面值当中不小于当前要换的钱的几个值。

递推式如下：
```
 d(i) = Min{ d(i-Vj) + 1 }，其中i-Vj >=0，Vj表示第j个硬币的面值;
```

算法如下，对于找不到的解返回-1：
```
int[] dp = new int[amount + 1];
for (int i = 1; i <= amount; i++) {
    dp[i] = Integer.MAX_VALUE;
    for (int j = 0; j < coins.length; j++) {
        if (coins[j] <= i && dp[i - coins[j]] != Integer.MAX_VALUE) {
            dp[i] = Integer.min(dp[i], 1 + dp[i - coins[j]]);
        }
    }
}
if (dp[amount] == Integer.MAX_VALUE) {
    return -1;
} else {
    return dp[amount];
}
```

### [300. Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)

M, Dynamic Programming, Binary Search

找到状态转移方程的，例如N个数的序列是：
```
5，3，4，8，6，7
```
根据上面找到的状态，
* 前1个数的LIS长度d(1)=1(序列：5)
* 前2个数的LIS长度d(2)=1(序列：3；3前面没有比3小的)
* 前3个数的LIS长度d(3)=2(序列：3，4；4前面有个比它小的3，所以d(3)=d(2)+1)
* 前4个数的LIS长度d(4)=3(序列：3，4，8；8前面比它小的有3个数，所以 d(4)=max{d(1),d(2),d(3)}+1=3)

OK，分析到这，我觉得状态转移方程已经很明显了，如果我们已经求出了d(1)到d(i-1)， 那么d(i)可以用下面的状态转移方程得到：
```
d(i) = max{1, d(j)+1},其中j<i,A[j]<=A[i]
```

代码如下：
```
int len = 1;  //初始最长的子串
for(int i=0; i<n; ++i){  // 遍历
    d[i] = 1;
    for(int j=0; j<i; ++j)  // 往回找，所有的值，如果值小，并且以这个值结尾的最长递增子串+1>以现在字符结尾的子串，那么改变下值。
        if(A[j]<=A[i] && d[j]+1>d[i])
            d[i] = d[j] + 1;
    if(d[i]>len) len = d[i];
}
```

### [216. Combination Sum III](https://leetcode.com/problems/combination-sum-iii/)

M, Backtracking, Array

给一个k，表示数组大小，然后从1-9中挑出不重复的数字，使这些数字的和等于target，即n。和题目39，40非常类似，只不过给定的数组是[1..9]，并且只允许挑固定k个数字，不允许重复。

Example:
```
Input: k = 3, n = 9
```
Output:
```
[[1,2,6], [1,3,5], [2,3,4]]
```

模板和N-Queens完全一样，这是解决回溯问题的标准模板。一切都是套路！！

```
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
```


### [179. Largest Number](https://leetcode.com/problems/largest-number/)

M, Array

因此不要陷入数字计算的陷阱里，而是想着用字符串比较或者使用jdk的函数式编程思想

```
String[] array = Arrays.stream(num).mapToObj(String::valueOf).toArray(String[]::new);
Arrays.sort(array, (String s1, String s2) -> (s2 + s1).compareTo(s1 + s2));
return Arrays.stream(array).reduce((x, y) -> x.equals("0") ? y : x + y).get();
```

### [93. Restore IP Addresses](https://leetcode.com/problems/restore-ip-addresses/)

M, Backtracking, String

和N-Queens的回溯模板一样，一切都是套路！！！

```
public List<String> restoreIpAddresses(String s) {
    List<String> result = new ArrayList<>();
    backtrack(result, new ArrayList<>(), s, 0, s.length(), 0);
    return result;
}
public void backtrack(List<String> result, List<Integer> tempList, String s,
                      int start, int length, int maskNum) {
    if (maskNum == 4 && start == length) {
        add templist to result
    }
    if (maskNum > 4) {
        return;
    }
    for (int j = 1; j < 4 && start + j <= length; j++) {
        String num = s.substring(start, start + j);
        if (isValid(num)) {  // 如果 num.startsWith("0") && num.length() > 1 其他情况 n >= 0 && n < 256
            tempList.add(Integer.parseInt(num));
            backtrack(result, tempList, s, start + j, length, maskNum + 1);
            tempList.remove(tempList.size() - 1);
        }
    }
}
```

### [90. Subsets II](https://leetcode.com/problems/subsets-ii/)

M, Backtracking, Array

有重复数组子集，和题目78很类似，使用方法1回溯DFS，那么需要在加一个判断条件

If nums = [1,2,2], a solution is:
```
[
  [2],
  [1],
  [1,2,2],
  [2,2],
  [1,2],
  []
]
```

注意先排序，然后加入这个判断，这个和题目39，40计算子元素和的组合类似。
```
if (i > start && nums[i] == nums[i - 1])
    continue;
```

### [89. Gray Code](https://leetcode.com/problems/gray-code/)

M, Backtracking

这题完全无思路，从[wikipedia](https://en.wikipedia.org/wiki/Gray_code#Converting_to_and_from_Gray_code
)拷贝的一个函数实现
```
Gray(N) = (n >> 1) XOR n
```
这里注意异或的使用
```
5^5=0
5^0=5
1）^异或操作符：两个位相同为0，相异为1
2）一个数异或本身恒等于0，如5^5恒等于0
3）一个数异或0恒等于本身，如5^0恒等于5
异或通常用于数字交换
a ^= b;
b ^= a;
a ^= b;
```

解法如下：
```
return IntStream.range(0, (int) Math.pow(2, n))
        .map(i -> (i >> 1) ^ i)
        .boxed()
        .collect(Collectors.toList());
```

### [79. Word Search](https://leetcode.com/problems/word-search/)

M, Array Backtracking

二维char数组内找指定的string词。

这是一道我把自己的解法提到Discussion里面的题目，[链接点此](https://discuss.leetcode.com/topic/55137/java-backtracking-solution-with-gif-motion-picture-to-show-every-step)

Example1:
Given
```
{'A', 'B', 'C', 'E'}
{'S', 'F', 'C', 'S'}
{'A', 'D', 'E', 'E'}
```

find ABCCED

![](http://neoremind.com/wp-content/uploads/2016/08/wordsearch.gif)


Example2:
Given
```
{'A', 'B', 'C', 'E'}
{'S', 'F', 'C', 'S'}
{'A', 'D', 'E', 'E'}
```

find SEECFBC

![](http://neoremind.com/wp-content/uploads/2016/08/wordseach2.gif)

This time we check from the top-left, goes to the right and then next row, one by one until we find the S which located at last of row2. Then start to test from the right, left, down and up position to see if the element is the next expected character.

Example3:
Given
```
{'A', 'B', 'C', 'E'}
{'S', 'F', 'E', 'S'}
{'A', 'D', 'E', 'E'}
```

find ABCESEEEFS

![](http://neoremind.com/wp-content/uploads/2016/08/wordsearch3.gif)

This time we deliberately goes to the wrong triple EEE then correct it, this process shows the core of backtracking.

```
public boolean exist(char[][] board, String word) {
    // 处理word为空或者长度等于0的情况

    boolean[][] visited = new boolean[board.length][board[0].length];

    char[] words = word.toCharArray();
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
            if (backtrack(board, words, i, j, 0, visited)) {
                return true;
            }
        }
    }
    return false;
}

public boolean backtrack(char[][] board, char[] words, int x, int y, int step, boolean[][] visited) {
    if (step == words.length) {
        return true;
    }
    if (x < 0 || x == board.length || y < 0 || y == board[0].length) {
        return false;
    }
    if (visited[x][y]) {
        return false;
    }
    if (words[step] != board[x][y]) {
        return false;
    }
    visited[x][y] = true;
    boolean isAnyOk = backtrack(board, words, x, y + 1, step + 1, visited) ||
            backtrack(board, words, x, y - 1, step + 1, visited) ||
            backtrack(board, words, x + 1, y, step + 1, visited) ||
            backtrack(board, words, x - 1, y, step + 1, visited);
    visited[x][y] = false;
    return isAnyOk;
}
```

### [78. Subsets](https://leetcode.com/problems/subsets/)

M, Array Backtracking Bit Manipulation

无重复数组所有子集合。

For example,
If nums = [1,2,3], a solution is:
```
[
  [3],
  [1],
  [2],
  [1,2,3],
  [1,3],
  [2,3],
  [1,2],
  []
]
```

这是一道很有意思的题目，首先先用数学的角度分析，子集合，就是从一堆数里面，做一些选择，所有的选择如下：
* 不选
* 选一个
* 选两个
* ...
* 选N个

那么公式就是
```
       N     N-1     N-2          1    0
NUM = C  + C     + C     + .... C   + C
       N     N       N            N    N
```
那么，例子中就是N=3，NUM=1+3+3+1=8

N=4, NUM=1+4+6+4+1=16

正好是2次幂，当然这个只是分析，写代码有三种实现。

1）Recursive (Backtracking, DFS)

没有退出条件，“来者不拒”
```
public List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> list = new ArrayList<>();
    Arrays.sort(nums);
    dfs(list, new ArrayList<>(), nums, 0);
    return list;
}

private void dfs(List<List<Integer>> list, List<Integer> tempList, int[] nums, int start) {
    list.add(new ArrayList<>(tempList));
    for (int i = start; i < nums.length; i++) {
        tempList.add(nums[i]);
        dfs(list, tempList, nums, i + 1);
        tempList.remove(tempList.size() - 1);
    }
}
```

2）Bit Manipulation
很巧妙
Each element is either in the subset or not

* : element is in subset
* -: element NOT in subset

so subsets of [1,2,3] are
```
[-,-,*] = [3]
[*,-,-] = [1]
[-,*,-] = [2]
[*,*,*] = [1,2,3]
[*,-,*] = [1,3]
[-,*,*] = [2,3]
[*,*,-] = [1,2]
[-,-,-] = []
```
use binary to repsent only two states
```
001 = [3]
100 = [1]
010 = [2]
111 = [1,2,3]
101 = [1,3]
011 = [2,3]
110 = [1,2]
000 = []
```

```
public List<List<Integer>> subsets2(int[] S) {
    Arrays.sort(S);
    int totalNumber = 1 << S.length;
    List<List<Integer>> collection = new ArrayList<List<Integer>>(totalNumber);
    for (int i = 0; i < totalNumber; i++) {
        List<Integer> set = new LinkedList<Integer>();
        for (int j = 0; j < S.length; j++) {
            if ((i & (1 << j)) != 0) {
                set.add(S[j]);
            }
        }
        collection.add(set);
    }
    return collection;
}
```

3）Iterative

This problem can also be solved iteratively. Take [1, 2, 3] in the problem statement as an example. The processof generating all the subsets is like:

Initially: [[]]

Adding the first number to all the existed subsets: [[], [1]];

Adding the second number to all the existed subsets: [[], [1], [2], [1, 2]]

Adding the third number to all the existed subsets: [[], [1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]].

### [77. Combinations](https://leetcode.com/problems/combinations/)

M, Backtracking

给定一个数字n，表示只能从1..n里面选择数字，然后只能选择k个数字，列出所有的组合。

If n = 4 and k = 2, a solution is:
```
[
  [2,4],
  [3,4],
  [2,3],
  [1,2],
  [1,3],
  [1,4],
]
```

和题目39，40的回溯模板一模一样，一切都是套路！！！

```
public List<List<Integer>> combine(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(n, k, 1, new ArrayList<>(2), result);
    return result;
}

private void backtrack(int n, int k, int i, List<Integer> tempList, List<List<Integer>> result) {
    if (tempList.size() == k) {
        result.add(new ArrayList<>(tempList));
    } else {
        for (int j = i; j <= n; j++) {
            tempList.add(j);
            backtrack(n, k, j + 1, tempList, result);
            tempList.remove(tempList.size() - 1);
        }
    }
}
```


### [72. Edit Distance](https://leetcode.com/problems/edit-distance/)

HARD, DP

初始条件、递推式
```
d[i][0] = i
d[0][j] = j
d[i][j] = d[i-1][j-1] if word1[i-1]=word2][j-1]
d[i][j] = min(d[i-1][j-1] + 1, d[i-1][j] + 1, d[i][j-1] + 1)
```

### [70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)

E, DP

类似Fabonacci，先写递推式和初始条件：
```
        1, n=1
f(n) =  2, n=2
        f(n-1) + f(n-2), n>=2
```

递归解：
```
public int climbStairs(int n) {
    if (n <= 2) {
        return n;
    }
    return climbStairs(n - 1) + climbStairs(n - 2);
}
```

DP，节省空间的解法：
```
public int climbStairs2(int n) {
    if (n < 2) {
        return n;
    }
    int dp[] = new int[] {1, 1, 2};
    for (int i = 2; i <= n; i++) {
        dp[2] = dp[0] + dp[1];
        dp[0] = dp[1];
        dp[1] = dp[2];
    }
    return dp[2];
}
```

### [64. Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/)

M, Array, Dynamic Programming

从左上到右下，最少要多少步。动态规划，定义状态和状态转移方程，状态就是到达每一个点最少的步骤，这是一个最优子结构和具有子问题重叠的问题。无后效性。

```
s[i][j] = Min{s[i - 1][j] + s[i][j - 1]} + grid[i][j]
```

### [63. Unique Paths II](https://leetcode.com/problems/unique-paths-ii/)

M, Array, Dynamic Programming

和63类似，只是没有了权重，每个点都是1，动态规划，核心逻辑：
```
if (obstacleGrid[i][j] == 1) {
    obstacleGrid[i][j] = 0;
} else if (i == 0 && j == 0) {
    obstacleGrid[i][j] = 1;
} else if (i == 0) {
    obstacleGrid[i][j] = obstacleGrid[i][j - 1] * 1;  // 乘法用的很巧妙！！！
    // For row 0, if there are no paths to left cell, then its 0,else 1
} else if (j == 0) {
    obstacleGrid[i][j] = obstacleGrid[i - 1][j] * 1;
    // For col 0, if there are no paths to upper cell, then its 0,else 1
} else {
    obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
}
```


### [62. Unique Paths](https://leetcode.com/problems/unique-paths/)

M, Array, Dynamic Programming

m*n矩阵，top-left -> bottom-right共有多少路线.

动态规划，初始第一行、第一列为0，写出状态转移方程以及定义初值，计算，然后查表。O(N*M)的空间和时间复杂度。

```
[1, 1, 1, 1]
[1, 2, 3, 4]
[1, 3, 6, 10]
[1, 4, 10, 20]
```

```
for (int i = 0; i < m; i++)
    arr[i][0] = 1;
for (int j = 0; j < n; j++)
    arr[0][j] = 1;
for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++) {
        arr[i][j] = arr[i][j - 1] + arr[i - 1][j];  // 上面+左面的cell路径和
    }
```

### [60. Permutation Sequence](https://leetcode.com/problems/permutation-sequence/)

M, Backtracking, Math

给定一个排列，找全排列的下一个值，这个在C++的STL库里有标准的next_permutation函数，这里就是一个实现。

说实话，这是个技巧活，和Gray Code这道题很像，根本想不出来:-(

using wikipedia example

2982th permutation of [1, 2, 3, 4, 5, 6, 7].

Convert k to factorial based number
```
k = 2982
n = 7
```
start form n - 1
```
2982 / 6! = 4 and remainder 102     |  4
102  / 5! = 0 and remainder 102     |  0
102  / 4! = 4 and remainder 6       |  4
6    / 3! = 1 and remainder 0       |  1
0    / 2! = 0 and remainder 0       |  0
0    / 1! = 0 and remainder 0       |  0
0    / 0! = 0 and remainder 0       |  0
```
So, 2982(10base) = 4041000(!base)

怎么把这个4041000还原回来看这个帖子吧，[点击此](http://leetcode.tgic.me/permutation-sequence/index.html)。

### [53. Maximum Subarray](https://leetcode.com/problems/maximum-subarray/)

M,  Array, Dynamic Programming, Divide and Conquer

非常经典的动态规划问题

4种解法:

1、最暴力的解法，起点0-n，终点0-n，计算中间的，三层for循环，O(N^3)。

2、O(N^2)暴力求解
```
public int findMaxSumOfSubArray_BruteForce(int[] array) {
    int maxSum = array[0];
    for (int i = 0; i < array.length; i++) {
        int subSum = 0;
        for (int j = i; j < array.length; j++) {
            subSum += array[j];
            if (subSum > maxSum) {
                maxSum = subSum;
            }
        }
    }
    return maxSum;
}
```

3、DP，时间复杂度O(N)
```
/**
 * 动态规划求解。
 * s[i]表示以i结尾最大的子数组和，如果s[i - 1]小于0了，则说明在i肯定是保留现在的值更大，否则就叠加前面的s[i - 1]
 * <pre>
 * s[i] = Max{s[i] (s[i - 1] < 0), s[i - 1] + v[i]}
 * </pre>
 * <p>
 * 最后遍历一下s[i]数组即可。
 */
public int findMaxSumOfSubArray_DP(int[] array) {
    int[] s = new int[array.length];
    s[0] = array[0];
    for (int i = 1; i < array.length; i++) {
        if (s[i - 1] < 0) {
            s[i] = array[i];
        } else {
            s[i] = s[i - 1] + array[i];
        }
    }

    int maxSum = s[0];
    for (int i : s) {
        if (i > maxSum) {
            maxSum = i;
        }
    }
    return maxSum;
}
```

另外一种节省空间的做法。
```
int maxSum = nums[0], sum = 0;
for(int i = 0; i < nums.length; ++i){
    sum = Math.max(sum + nums[i], nums[i]);
    maxSum = Math.max(maxSum, sum);
}
return maxSum;
}
```

4、分治法，需要进一步研究[leetcode discussion](https://discuss.leetcode.com/topic/426/how-to-solve-maximum-subarray-by-using-the-divide-and-conquer-approach/2)

时间复杂度O(NlogN)。

们把数组A[1..n]分成两个相等大小的块：A[1..n/2]和A[n/2+1..n]，最大的子数组只可能出现在三种情况：
* A[1..n]的最大子数组和A[1..n/2]最大子数组相同；
* A[1..n]的最大子数组和A[n/2+1..n]最大子数组相同；
* A[1..n]的最大子数组跨过A[1..n/2]和A[n/2+1..n]

前两种情况的求法和整体的求法是一样的，因此递归求得。

第三种，我们可以采取的方法也比较简单，沿着第n/2向左搜索，直到左边界，找到最大的和maxleft，以及沿着第n/2+1向右搜索找到最大和maxright，那么总的最大和就是maxleft+maxright。
而数组A的最大子数组和就是这三种情况中最大的一个。
```
int maxSubArray(int *A,int l,int r) {
   if l == r
       return A[l]
       mid = (l+r)/2;
   ml = maxSubArray(A,l,mid); //分治
   mr = maxSubArray(A,mid+1,r);
   for i=mid downto l do
       search maxleft;
   for i=mid+1 to r do
       search maxright;
   return max(ml,mr,maxleft+maxright); //归并
}
```

### [52. N-Queens II](https://leetcode.com/problems/n-queens-ii/)

H, Backtracking

和51题类似，不用输出解，只要输出多少种解法即可。

回溯的时间复杂度比较高，试探的成本比较高，因此如下到计算16个皇后，是O(16!)是非常大的，(2^32)^2
```
assertThat(totalNQueens(4), Matchers.is(2));
assertThat(totalNQueens(5), Matchers.is(10));
assertThat(totalNQueens(6), Matchers.is(4));
assertThat(totalNQueens(7), Matchers.is(40));
assertThat(totalNQueens(8), Matchers.is(92));
assertThat(totalNQueens(9), Matchers.is(352));
assertThat(totalNQueens(10), Matchers.is(724));
assertThat(totalNQueens(11), Matchers.is(2680));
assertThat(totalNQueens(12), Matchers.is(14200));
assertThat(totalNQueens(13), Matchers.is(73712));
assertThat(totalNQueens(14), Matchers.is(365596));  // so slow....
```

### [51. N-Queens](https://leetcode.com/problems/n-queens/)

H, Backtracking

1、暴力解法，8个皇后64种位置，那么可以O(64^8)时间复杂度解决，理论可行，实际不可行。

2、已知肯定每个皇后放在不同的8行上，那么构造一个数组[1, 2, 3, 4, 5, 6, 7, 8]，索引表示行，值表示放在第几列上，可以转换为一个全排列问题，然后结合一个判断是否可行的函数即可，O(8!)，和#1相比是巨大的提升。记住一个解法84136275 :-)

3、采用DFS以及回溯backtracking的思想，近似于穷举暴力，来进行“剪枝”，最差也是O(8!)，但实际上不会这么差。

DFS是一种brute force的方法，有一个pattern可以遵循
```
selected_choice stores [step0: choice_x, step1: choice_y, ...]

dfs(step0) {
    if all steps had solutions
        selected_choices is an answer

    foreach choice in all choices in current step
        if choice is possible
            selected_choices[step] = choice
            dfs(next step)
            selected_choices[step] = no choice or last choice // this is called backtracking
}
```

放在8皇后问题上，

* step就是N行
* choices each step就是在某一行上可以放置的N列，有N个选择
* possible checker就是这个grid上，纵列没有皇后，对角线（diagonal）包括45度还有135度没有皇后

伪代码如下：
```
a boolean[n][n] array to simulate a chessboard
chessboard[row][column] = true is for putting a queen
```

检查是否可以构成不互相攻击的皇后函数：
```
function check_can_put(row, col)

  if queen on same column
    return false

  if queen on same diagonal
    return false
```

这里有一个技巧，就是判断是否皇后合法的函数里面，可以按照如下的公式进行计算
* 主对角线（45度）上的i - j为定值
* 辅对角线（135度）上的i + j为定值，因此可以做一个数组来记录，省去多次循环判断。

DFS和回溯解法的代码骨架：
```
function N_queen(row)
    if row > N
        convert chessboard into result
    else
        foreah col in current row
            if check_can_put(row, col)
            // put queen
            chessboard[row][col] = true
            N_queen(row + 1)
            // take queen away
            chessboard[row][col] = false
```

### [47. Permutations II](https://leetcode.com/problems/permutations-ii/)

M, Backtracking

有重复数全排列问题。

和46非常类似。在继续DFS之前要判断下，第一自己跟自己比的时候不判断是否相同，只判断后面的数和固定的数比较。从start即i开始往后看，算法的基本思路是从start开始依次和后面的数字交换，然后递归全排列，后面的数字就是j，如果j在之前交换过了，就没必要做了，否则就重复了。

```
private boolean isNotSame(int[] nums, int i, int j) {
    for (int k = i; k < j; k++) {
        if (nums[k] == nums[j]) {
            return false;
        }
    }
    return true;
}
```

### [46. Permutations](https://leetcode.com/problems/permutations/)

M, Backtracking

无重复数全排列问题。

全排列的算法是一个基础，排列算法在它的基础上增加了选数过程（select），即先选后排。

这里面主要是用到了一个递归的思想， 利用递归法来做这题关键下几点：
* 1.普通情况-取出序列中的一个数并且将剩下的序列全排列
* 2.特殊情况-序列中只剩一个数，则全排列就是其自身。将增个获得的序列输出。
* 3.在不止一个数的情况下，该位要分别交换剩下的数（例如：两个数A，B 则有两种情况，一个是AB 一个是BA）

全排列就是从第一个数字起每个数分别与它后面的数字交换。

例如1,2,3
* 步骤1.1）1和1交换（实际没动），然后加上2,3的全排列即可。
* 步骤1.2）1和2交换，然后加上1,3的全排列即可。
* 步骤1.3）1和3交换，然后加上2,1的全排列即可。

计算后面的排列是一个递归的过程，例如计算2,3的排列。
* 步骤2.1），2和2交换（实际没动），然后加上3的全排列即可，就是本身。
* 步骤2.2），2和3交换，然后加上2的全排列即可，就是本身。

所以步骤1.1加上子步骤2.1和2.2后，得出的结果如下： [1, 2, 3] [1, 3, 2]

全排列的数学公式如下: 从n个不同的元素中任取m个，按照一定的顺序排成一列，极为Pnm 当n=m时称作全排列，记为Pnn

排列数公式：
```
n!=1*2*3...*n, 规定0!=1
Pnm=n! / (n-m)! = n(n-1)(n-2)...(n-m+1)
Pnn=n!
```
所以1,2,3的全排列有3!种，即6种

```
public void permuation(int[] nums) {
    doPermuation(nums, 0, nums.length);
}

public void doPermuation(int[] nums, int m, int len) {
    if (m == len) {
        System.out.println(Arrays.toString(nums));
    } else {
        for (int i = m; i < len; i++) {
            swap(nums, i, m);
            doPermuation(nums, m + 1, len);
            swap(nums, i, m);
        }
    }
}
```

### [40. Combination Sum II](https://leetcode.com/problems/combination-sum-ii/)

M, Backtracking, Array

和题目39类似，只不过这回不允许重复使用数字。

在遍历每一个数字的时候加一个条件，首先start根自己不能算重复，之和前面的：
```
if (i > start && nums[i] == nums[i - 1]) {
    continue; // skip duplicates
}
```
同时，回溯的时候的start要+1，因为不允许重复使用某个数字了，注意结尾参数是`i+1`。一切都是套路！！
```
backtrack(list, tempList, nums, remain - nums[i], i + 1);
```

### [39. Combination Sum](https://leetcode.com/problems/combination-sum/)

M, Backtracking, Array

给一个数组和一个target，从数组中找一些“组合”，注意不是“排列”，使这些数字的和等于target。

这道题目允许数字重复利用。

given candidate set [2, 3, 6, 7] and target 7,
A solution set is:
```
[
  [7],
  [2, 2, 3]
]
```

注意这里三点：
* 退出条件是要不然相等、要不然减过头了。
* 必须先排序！一是为了加和考虑，二来为40题中解决重复数字的问题。
* `tempList.remove(tempList.size() - 1)`这个技巧是删除最后一个元素，需要回溯到之前的状态。


```
public List<List<Integer>> combinationSum(int[] nums, int target) {
    List<List<Integer>> list = new ArrayList<>();
    Arrays.sort(nums); // 先排序！！！
    backtrack(list, new ArrayList<>(), nums, target, 0);
    return list;
}

private void backtrack(List<List<Integer>> list, List<Integer> tempList, int[] nums, int remain, int start) {
    if (remain < 0) {
        return;
    } else if (remain == 0) {
        list.add(new ArrayList<>(tempList));
    } else {
        for (int i = start; i < nums.length; i++) {
            tempList.add(nums[i]);
            backtrack(list, tempList, nums, remain - nums[i], i); // not i + 1 because we can reuse same elements
            tempList.remove(tempList.size() - 1);
        }
    }
}

```


### [37. Sudoku Solver](https://leetcode.com/problems/sudoku-solver/)

H, Backtracking Hash Table

利用回溯法，详细见N-Queens问题的接替模板，是一个递归的过程。注意这里的返回true或者false，当所有值都填补进去的时候，
需要返回false，因为无解，就需要回溯到上次，换一个数字试试。

这里需要幸好我们已经有了is valid soduku的算法。

```
public boolean doSolveSudoku(char[][] board) {
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
            if (board[i][j] != '.') {
                continue;
            } else {
                for (int k = 1; k < 10; k++) {
                    board[i][j] = (char) ('0' + k);
                    if (isValid(board)) {
                        if (doSolveSudoku(board)) {
                            return true;
                        }
                    }
                    board[i][j] = '.';
                }
                return false;
            }
        }
    }
    return true;
}

```

### [36. Valid Sudoku](https://leetcode.com/problems/valid-sudoku/)

E, hash table

这道题是Sudoku Solver的一个子问题，在解数独的时候我们需要验证当前数盘是否合法。其实思路比较简单，也就是用brute
force。对于每一行，每一列，每个九宫格进行验证，总共需要27次验证，每次看九个元素。所以时间复杂度就是O(3*n^2), n=9。代码如下：

行列比较好说。直接双层for循环。

对于某个小九宫格，需要有点技巧：

```
for (int block = 0; block < 9; block++) {
    boolean[] map = new boolean[9];
    for (int i = block / 3 * 3; i < block / 3 * 3 + 3; i++) {
        for (int j = block % 3 * 3; j < block % 3 * 3 + 3; j++) {
            if (board[i][j] != '.') {
                if (map[(int) (board[i][j] - '1')]) {
                    return false;
                }
                map[(int) (board[i][j] - '1')] = true;
            }
        }
    }
}
```

### [22. Generate Parentheses](https://leetcode.com/problems/generate-parentheses/)

M, Backtracking

以n=3为例，生成结果如下：
```
((()))
(()())
(())()
()(())
()()()
```

1）基本思想就是不断的添加左括号 (，直到添加不了了，然后补右括号。
2）推一栈帧，补齐一个右括号)，然后剩下的继续如栈，重复1的操作。

这个步骤比较抽象，较难理解:-(

```
public List<String> generateParenthesis(int n) {
    List<String> list = new ArrayList<>();
    backtrack(list, "", 0, 0, n);
    return list;
}

public void backtrack(List<String> list, String str, int open, int close, int max) {
    if (str.length() == max * 2) {
        list.add(str);
        return;
    }
    if (open < max) {
        backtrack(list, str + "(", open + 1, close, max);
    }
    if (close < open) {
        backtrack(list, str + ")", open, close + 1, max);
    }
}
```



### [20. Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)

E, Stack, String

```
foreach c in s
    if c is open brackets like [ { (
        push c to stack
    else
       if stack is empty
           return false
       if stack.pop is NOT pair to c
           return false
return stack is empty true or else false
```

### [19. Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)

E，linked list，two pointers

技巧是搞一个ListNode在head的前面

### [17. Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/)

M, Backtracking String

Input:Digit string "23"
Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].

回溯法 + DFS，类似于笛卡尔乘积的方式
```
find(res, combination, 0, digits);

void find(List<String> res, char[] combination, int index, String digits) {
    if (index == digits.length()) {
        res.add(new String(combination));
    } else {
        int digit = digits.charAt(index) - '0';
        for (char c : charMap[digit]) {
            combination[index] = c;
            find(res, combination, index + 1, digits);
        }
    }
}
```

### [16. 3Sum Closest](https://leetcode.com/problems/3sum-closest/)

M, Array, Two pointers

和15题类似，

初始值
```
int closest = nums[0] + nums[1] + nums[2];
```

中间判断条件变为：
```
if (Math.abs(target - (left + mid + right)) < Math.abs(target - closest)) {
    closest = (left + mid + right);
}
```

### [15. 3Sum](https://leetcode.com/problems/3sum/)

M, Array, Two pointers

题目要求a, b, c in S such that a + b + c = 0

* 先排序
* 从后往前定住最后一个，然后从前往后定住一个，中间的遍历找。
* 找到了，那么为了去重，要跳过从前往后重复的元素，以及从后往前重复的元素。这样就不用维护Set了。

```
right = nums.length - 1;
left = 0;
while (right > 1) {
    rightNum = nums[right];
    while (left < right - 1) {
        leftNumber = nums[left];
        for (int mid = left + 1; mid < right; mid++) {
            int midNum = nums[mid];
            if (midNum + positiveNum + negativeNum == 0) {
                result.add(leftNum, midNum, rightNum);
                break;  // need to quit in case next number is the same as mid
            }
        }
        while (left < right && nums[left] == negativeNum) {
            left++;
        }
    }
    left = 0;
    while (right > 0 && nums[right] == positiveNum) {
        right--;
    }
}
```

### [14. Longest Common Prefix](https://leetcode.com/problems/longest-common-prefix/)

E, String

基本思想就是固定第一个str，对于其中的每一个char，后面的所有的str都去除第N个和其对比，直到发现不一样的为止。
```
foreach char in strs[0] {
    idx represents position from 0 to len -1
    for every str in strs
        if (strs[i].length() < idx + 1 || strs[0].charAt(idx) != strs[i].charAt(idx)) {
            return ret.toString();
        }
    ret.append(strs[0].charAt(idx));
}
```

### [11. Container With Most Water](https://leetcode.com/problems/container-with-most-water/)

M, Array Two Pointers
```
i = 0
j = end
while i and j not meet
    max = MAX(area(i,j) ,max)
    if height[i] < height[j]
        i = i + 1
    if height[i] > height[j]
        j = j - 1
```

### [9. Palindrome Number](https://leetcode.com/problems/palindrome-number/)

E

Problems related with numbers are frequently solved by / and %. No need of extra space is required. This problem
is similar with the Reverse Integer problem.

Note: no extra space here means do not convert the integer to string, since string will be a copy of the integerand take extra space. The space take by div, left, and right can be ignored.

计算高位和低位是不是一样的数组，用/和%运算符即可。

### [8. String to Integer (atoi)](https://leetcode.com/problems/string-to-integer-atoi/)

E, Math,String

大概简单来说，算法如下：
```
int x = ch - '0'; // char to int
num = num * 10;
num = num + x;
```
但是要处理一堆比如空格，正负数，溢出等等，总是没提交正确。faint。

### [7. Reverse Integer](https://leetcode.com/problems/reverse-integer/)

E, Math

注意溢出的问题。
```
while (x > 0) {
    // ret * 10 + (x % 10) > Integer.MAX_VALUE
    if (ret > (Integer.MAX_VALUE - x % 10) / 10) {
        return 0;
    }
    ret = ret * 10 + (x % 10);
    x /= 10;
}
```
负数也一样

### [6. ZigZag Conversion](https://leetcode.com/problems/zigzag-conversion/)

M, String

所谓的zigzag就是按照某种pattern去打印字符串。这道题解的实际就是一个找“规律”的问题。

我的解法是用笔和纸，按照numRows=2，3，4，5去尝试画出来实际的样子，然后计算

1）首先要区分单元unit，就是所谓的一个一个小格子，比如
numRows=3，那么ABCD，EFGH，IJK各自是一个单元，单元内元素的个数等于(numRows - 1)*2。

2）计算出一共有多少个单元unit。

3）一个单元一个单元的打印出来，小于numRows的很好安置位置，大于numRows的元素有一个规律，
就是他们是row--，col++的斜线往上走的，掌握的这个规律，就可以把一个char[][]的二维数组

看出我们要打印出来的样子，去确定这个row和col，遍历字符串填充进去即可。

此题比较恶心，不细说了。

### [5. Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/)

M, String

例如`dfabbax`的palindromic子串是`abba`。

O(N^2)，从第一个char开始左右扩展，记录一个low和maxLen表示palindromic的字符.

```
for(every char in s) {
   j, k
   while s[k] = s[k + 1]
      k++ // skip duplicates
   if s[j] to s[k] // handle well corner condition
      j--
      k++
   low = j
   maxLen = k - j + 1
}
```

### [4. Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/)

H, Binary Search, Array, Divide and Conquer

题目要求O(log(m+n))的时间复杂度，一般来说都是分治法或者二分搜索。

[refer1](http://www.07net01.com/2015/07/871155.html)
[refer2](https://leetcode.com/submissions/detail/69094393/)

另外最笨的方法就是排序下时间复杂度是O(NLogN)，不考虑。

首先定义解题的入口：

区分奇数和偶数，偶数需要计算两个值求平均，奇数直接取中间的即可。

```
if ((m + n) % 2 == 0) {
    return (findKth(nums1, nums2, 0, 0, m, n, k) + findKth(nums1, nums2, 0, 0, m, n, k + 1)) / 2;
} else {
    return findKth(nums1, nums2, 0, 0, m, n, k + 1);
}
```

把问题转换为Kth of two sorted array问题（搜索两个有序序列的第k个元素），归并计数法 Merge and Count，时间复杂度是O(K)，但是K比较大的时候，还是会退换成O(M+N)，即O(N)。在leetcode提交这个效率也比较高。这个算brute force版本，类似于two pointers的思想：

```
foreach n in array m and array n
    if isKth(n)
        found
```

优化为O(log(m+n))时间复杂度的算，采用分治法

假设序列都是从小到大排列，对于第一个序列中前p个元素和第二个序列中前q个元素，我们想要的最终结果是：p+q等于k-1,且一序列第p个元素和二序列第q个元素都小于总序列第k个元素。

因为总序列中，必然有k-1个元素小于等于第k个元素。这样第p+1个元素或者第q+1个元素就是我们要找的第k个元素。

实际上就是不断的二分，舍弃掉绝对不可能存在要找的解的区间段这种思想。

边界条件corner conditions非常多，写递归完成，退出条件要定义好。

1）保证arr1的长度较为小

2）arr1为空了，则直接在arr2里面直接索引定位

3）如果k=1，那么直接取arr1和arr2的头元素比较，小的就是要找的。

```
double findKth(int[] arr1, int[] arr2, int start1, int start2, int len1, int len2, int k) {
    if (len1 > len2) {
        return findKth(arr2, arr1, start2, start1, len2, len1, k);
    }
    if (len1 == 0) {
        return arr2[start2 + k - 1];
    }
    if (k == 1) {
        return Math.min(arr1[start1], arr2[start2]);
    }
    int p1 = Math.min(k / 2, len1);
    int p2 = k - p1;
    if (arr1[start1 + p1 - 1] < arr2[start2 + p2 - 1]) {
        return findKth(arr1, arr2, start1 + p1, start2, len1 - p1, len2, k - p1);
    } else if (arr1[start1 + p1 - 1] > arr2[start2 + p2 - 1]) {
        return findKth(arr1, arr2, start1, start2 + p2, len1, len2 - p2, k - p2);
    } else {
        return arr1[start1 + p1 - 1];
    }
}
```

### [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)

M,  Hash Table,Two Pointers,String

维护一个窗口，从当前char，叫做curr，往回看，如果发现相同的则，重置barrier，这个区间就是无重复的一个区间。常数时间，时间复杂度O(N)。
```
barrier = 0
foreach i in s
    while s[barrier .. i] contains charaters
        barrier = barrier + 1
    max = MAX(max, curr - barrier + 1)
```

### [2. Add Two Numbers](https://leetcode.com/problems/add-two-numbers/)
```
Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8
```
M, Linked list, Math

O(N)，two pointers分别齐头并进的在两个list中从后向前，利用/计算进位，利用%就算余数，知道一个list耗尽，把剩下的list按照刚刚相同的操作继续，最后需要注意如果进位为0，那么需要把最后一个ListNode删除。

### [1. Two Sum](https://leetcode.com/problems/two-sum/)

E, Array, Hash table

用一个hashmap，key是值，value是index+1。

```
for (int i = 0; i < nums.length; i++) {
    if (map.containsKey(target - nums[i])) {
        return new int[] {map.get(target - nums[i]), i + 1};
    }
    map.put(nums[i], i + 1);
}
```