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

### [179. Largest Number](https://leetcode.com/problems/largest-number/)

M, Array

因此不要陷入数字计算的陷阱里，而是想着用字符串比较或者使用jdk的函数式编程思想

```
String[] array = Arrays.stream(num).mapToObj(String::valueOf).toArray(String[]::new);
Arrays.sort(array, (String s1, String s2) -> (s2 + s1).compareTo(s1 + s2));
return Arrays.stream(array).reduce((x, y) -> x.equals("0") ? y : x + y).get();
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