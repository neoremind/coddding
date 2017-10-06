# Leetcode records

### [440. K-th Smallest in Lexicographical Order](https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/)

H, 类似题目[386. Lexicographical Numbers](https://leetcode.com/problems/lexicographical-numbers/)

一个字典序排列的数组，找到第k个。

一个数数的过程，参考[链接](https://discuss.leetcode.com/topic/64624/concise-easy-to-understand-java-5ms-solution-with-explaination)

想象成为从node 1开始，最终要用pre-order走到k-1步，这个node就是要找的。

但是数数可以快速的数，首先需要依靠一个数数的函数，数curr -> curr + 1之间的数字有多少steps。

如果steps<=k，那么直接走到curr+1这个node，然后k=k-steps。否则下沉到curr*10，k=k-1.

数下面的子node的方法是，保持left=curr，right=curr+1，例如left=3，right=4，
那么不断的left<=n，那么steps += Math.min(right - left, n + 1)，left *= 10，right *= 10，

```
public int findKthNumber2(int n, int k) {
    return helper(n, k, 1);
}

int helper(int n, int k, int curr) {
    if (k == 1) {
        return curr;
    }
    int count = countNumber(n, curr, curr + 1);
    if (count <= k) {
        return helper(n, k - count, curr + 1);
    } else {
        return helper(n, k - 1, curr * 10);
    }
}

/**
 * 必须为long，避免溢出
 */
int countNumber(int n, long left, long right) {
    int count = 0;
    while (left <= n) {
        count += Math.min(n + 1, right) - left;
        left *= 10;
        right *= 10;
    }
    return count;
}
```


### [403. Frog Jump](https://leetcode.com/problems/frog-jump/)

H, Dynamic Programming

 第一次写代码错误的解读了：
`If the frog's last jump was k units, then its next jump must be either k - 1, k, or k + 1 units.`

认为如果frog是第K跳，则下次最多跳K-1，K，K+1的位置。其实是上次的跳跃结果。。。faint :-(。所以用了backtrack的模板，照着自己的理解是正确的，但是按照本题目的意思是不对的。

其实按照backtrack模板，解决本题目也是OK的，下面的解法更加巧妙，首先分析下DFS的解法。

```
例如示例：
[0,1,3,5,6,8,12,17]

There are a total of 8 stones.
The first stone at the 0th unit, second stone at the 1st unit,
third stone at the 3rd unit, and so on...
The last stone at the 17th unit.

Return true. The frog can jump to the last stone by jumping
1 unit to the 2nd stone, then 2 units to the 3rd stone, then
2 units to the 4th stone, then 3 units to the 6th stone,
4 units to the 7th stone, and 5 units to the 8th stone.
_ _   _   _ _   _         _              _
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
|_|   |   | |   |
 1    |   | |   |
  |___|   | |   |
    2     | |   |
      |___| |   |
        2   |   |
         |__|   |
           1    |
            |___|
              2  ===> [1,2,3] furtherest to 11 not 12 so backtrack

在5的位置“跳力”是[1,2,3]，那么下次可以选择跳到6和8，如果是6则如上跳不出去了，所以需要DFS选择8继续，如下图所示即可以跳出去。
_ _   _   _ _   _         _              _
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
|_|   |   |     |         |              |
 1    |   |     |         |              |
  |___|   |     |         |              |
    2     |     |         |              |
      |___|     |         |              |
        2       |         |              |
          |_____|         |              |
             3            |              |
                |_________|              |
                     4                   |
                          |______________|
                                  5

此时如果8不是下一个数字，而是9，那么也跳不出去，那么返回的结论就是跳不出去了。
```

代码如下：
```
public boolean canCross(int[] stones) {
    if (stones[1] > 1)
        return false;
    return dfs(stones, 0, 0);
}

boolean dfs(int[] stones, int index, int lastStep) {
    for (int i = index + 1; i < stones.length; i++) {
        if (stones[i] - stones[index] < lastStep - 1) {
            continue;
        if (stones[i] - stones[index] > lastStep + 1) {
            return false;
        if (dfs(stones, i, stones[i] - stones[index])) {
            return true;
    }
    return index == stones.length - 1;
}
```

这个解法在leetcode会TLE，所以需要做一个dp[][]来存储在i为位置，如果前一步lastStep步下能否有解，避免无谓的深入下去。


### [395. Longest Substring with At Least K Repeating Characters](https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/)

M

初步感觉和
* 3. Longest Substring Without Repeating Characters
* 76. Minimum Window Substring
* 209. Minimum Size Subarray Sum
* 59. Longest Substring with At Most Two Distinct Characters
* 340. Longest Substring with At Most K Distinct Characters
比较类似，但是仔细分析无法用sliding window来解决。

首先暴露求解brute force下，遍历从0到len-k的位置，然后没有字符加入到计数的int[26]个字母中，叫做counter，下一个字符也在
counter中找到索引+1，如果发现counter中不等于0的索引都大于k，那么就形成了一个解，计算长度，下一次那个i就从最长的那个记录开始，不要从i++
开始了，否则会TLE。
```
int i = 0;int res = 0;
while (i + k < s.length()) {
    int[] counter = new int[26];
    int maxIdx = i;
    for (int j = i; j < s.length(); j++) {
        int charIdx = s.charAt(j) - 'a';
        counter[charIdx]++;
        boolean isAllGreaterOrEqualK = true;
        for (int m = 0; m < 26; m++) {
            if (counter[m] != 0 && counter[m] < k)
                isAllGreaterOrEqualK = false;
                break;
        }
        if (isAllGreaterOrEqualK)
            res = Math.max(res, j - i + 1);
            maxIdx = j;
    }
    i = maxIdx + 1;
}
return res;
```

一个改进的算法是不用每次遍历counter了，而是用mask来做，如果其小于k，我们将mask的对应位改为1，如果大于等于k，将mask对应位改为0。然后看mask是否为0，是的话就更新res结果。
```
int i = 0;int res = 0;
while (i + k < s.length()) {
    int[] counter = new int[26];
    int mask = 0;
    int maxIdx = i;
    for (int j = i; j < s.length(); j++) {
        int charIdx = s.charAt(j) - 'a';
        counter[charIdx]++;
        if (counter[charIdx] < k) {
            mask |= (1 << charIdx);  //变成1的办法！
        } else {
            mask &= (~(1 << charIdx));  //变成0的办法！
        }
        if (mask == 0)
            res = Math.max(res, j - i + 1);
            maxIdx = j;
    }
    i = maxIdx + 1;
}
return res;
```

第三种解法就是利用DAC的思想，找到“断点”，分别计算两边的满足题目的长度。
```
char[] str = s.toCharArray();
return helper(str, 0, s.length(), k);

private int helper(char[] str, int start, int end, int k) {
    if (end < start)
        return 0;
    if (end - start < k)
        return 0;//substring length shorter than k.
    int[] count = new int[26];
    for (int i = start; i < end; i++)
        int idx = str[i] - 'a';
        count[idx]++;
    for (int i = 0; i < 26; i++) {
        if (count[i] == 0)
            continue;//i+'a' does not exist in the string, skip it.
        if (count[i] < k) {
            for (int j = start; j < end; j++) {
                if (str[j] == i + 'a') {
                    int left = helper(str, start, j, k);
                    int right = helper(str, j + 1, end, k);
                    return Math.max(left, right);
                }
            }
        }
    }
    return end - start;
```

### [386. Lexicographical Numbers](https://leetcode.com/problems/lexicographical-numbers/)

M，类似的题目[440. K-th Smallest in Lexicographical Order](https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/)

生成字典序的number序列，例如[1,10,11,12,13,2,3,4,5,6,7,8,9]

方法1：

结果就是[1..N]的一个排列而已，按照字典序的排列

这种方法在LC上会TLE

以561为例，
```
           [1, 10, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
               11, 110, 111, 112, 113, 114,
               ....
               197, 198, 199, 2, 20, 200, 201, 202, 203, 204...]
```
算法描述：
```
初始curr=1

loop N个数，把curr加入结果

1）如果curr*10不超过N，那么curr扩大10倍。这样可以保证按照1，10，100，1000...这样的节奏前进。
2）如果curr的最后一位部位9，那么curr++。这里要注意判断curr++不超过N，否则就加错了，会挤掉本该在这个位置的数字。
3）如果curr的末尾为9，那么就找到最后一个数字不为9的那一位，例如3456999，那么就循环(curr/10 % 10) == 9做判断，
例如345699
   34569
找到34569，那么curr = curr / 10 + 1 = 34567，正好就是下一个数字。
```
实现：
```
List<Integer> res = new ArrayList<>(n);
int curr = 1;
for (int i = 1; i <= n; i++) {
    res.add(curr);
    if (curr * 10 <= n) {
        curr = curr * 10;
    } else if (curr % 10 != 9 && curr + 1 <= n) {
        curr++;
    } else {
        while ((curr / 10) % 10 == 9) {
            curr /= 10;
        }
        curr = curr / 10 + 1;
    }
}
return res;
```

方法2：DFS

```
1 - 9的一个森林，用DFS

                                    1
                                    /
                    [10      ,                 11 ...19]
                    /                          /
      [100   ,    101,       102 ....  109]  [110-119] ...
     /            /
  [1000 - 1009]  [1010- 1019]
```

```
public List<Integer> lexicalOrder(int n) {
    List<Integer> res = new ArrayList<>(n);
    for (int j = 1; j <= 9; j++) {
        dfs(res, j, n);
    return res;
}

void dfs(List<Integer> res, int num, int n) {
    if (num > n) {
        return;
    }
    res.add(num);
    for (int i = 0; i <= 9; i++) {
        dfs(res, num * 10 + i, n);
    }
}
```


### [371. Sum of Two Integers](https://leetcode.com/problems/sum-of-two-integers/)

E, Bit Manipulation

0+0进位为0,1+0进位为0,0+1进位为0，1+1进位为1，该操作与位运算的&操作相似。
先考虑相加的结果，然后再考虑进位。
```
int getSum(int a, int b) {
    return b==0? a:getSum(a^b, (a&b)<<1);
}
```
一连串bitwise的操作见evernote笔记。

### [349. Intersection of Two Arrays](https://leetcode.com/problems/intersection-of-two-arrays/)

E, Binary Search Hash Table Two Pointers Sort

Given nums1 = [1, 2, 2, 1], nums2 = [2, 2], return [2].

三种[java解法](https://discuss.leetcode.com/topic/45685/three-java-solutions)

方法1：O(N)时间复杂度，但是空间大些，而且使用了java.util类库性能一般
建两个集合set，nums1放入其中一个，然后用set的API做contains判断，如果存在则加入结果。

方法2：O(NlogN)时间复杂度
* 排序两个数组，Arrays.sort(..)
* 用双指针：
```
while (i < nums1.length && j < nums2.length)
    if (nums1[i] < nums2[j])
        i++;
    else if (nums1[i] > nums2[j])
        j++;
    else
        set.add(nums1[i]);
        i++;
        j++;
```

方法3：O(NlogN)时间复杂度
* 排序一个数组，Arrays.sort(..)
* 使用二分查找
```
Arrays.sort(nums2);
for (Integer num : nums1) {
    if (binarySearch(nums2, num)) {
        set.add(num);
    }
}
```

### [367. Valid Perfect Square](https://leetcode.com/problems/valid-perfect-square/)

M, Binary Search Math

标准的二分，注意处理溢出情况。

和[题目Sqrt](https://github.com/neoremind/coddding/blob/master/codding/src/main/java/net/neoremind/mycode/argorithm/leetcode/README.md#69-sqrtx)一样

```
if (num < 1) {
    return false;
}
long left = 1, right = num;// long type to avoid 2147483647 case

while (left <= right) {
    long mid = left + (right - left) / 2;
    long t = mid * mid;
    if (t > num) {
        right = mid - 1;
    } else if (t < num) {
        left = mid + 1;
    } else {
        return true;
    }
}

return false;
```

### [358. Rearrange String k Distance Apart](https://leetcode.com/problems/rearrange-string-k-distance-apart/)

PAID, H

Given a non-empty string str and an integer k, rearrange the string such that the same characters are at least distance k from each other.

All input strings are given in lowercase letters. If it is not possible to rearrange the string, return an empty string "".

Example 1:
str = "aabbcc", k = 3

Result: "abcabc"

The same letters are at least distance 3 from each other.

使用了贪心、hashmap、优先队列，属于难度较高的题目。首先做一个字符到出现频率的映射，例如
```
a -> 3
b -> 2
c -> 1
```

然后建大顶堆，堆上面是a，循环堆不为空，poll出a，加入结果，然后a->2保留在大小为k的pending LIFO中等待“释放”，想象成为
刑满释放的意思，以为如果他早出来了也许由于他的出现频率很频繁，又被poll出来那么距离肯定不在k之外了。

```
Map<Character, Integer> char2Freq = new HashMap<>();
for (char ch : str.toCharArray()) {
    char2Freq.put(ch, char2Freq.getOrDefault(ch, 0) + 1);
}
PriorityQueue<Map.Entry<Character, Integer>> queue = new PriorityQueue<>((o1, o2) -> o2.getValue() - o1.getValue());
Queue<Map.Entry<Character, Integer>> pending = new LinkedList<>();
StringBuilder sb = new StringBuilder();
queue.addAll(char2Freq.entrySet());
while (!queue.isEmpty()) {
    Map.Entry<Character, Integer> e = queue.poll();
    sb.append(e.getKey());
    pending.add(e);
    if (pending.size() < k) {
        continue;
    }
    Map.Entry<Character, Integer> firstRelease = pending.poll();
    firstRelease.setValue(firstRelease.getValue() - 1);
    if (firstRelease.getValue() > 0) {
        queue.add(firstRelease);
    }
}
return sb.length() == str.length() ? sb.toString() : "";
```

### [347. Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)

M, Hash Table Heap

[参考链接](https://discuss.leetcode.com/topic/48158/3-java-solution-using-array-maxheap-treemap)

方法1：保存一个num->freq的map，然后针对这个map做一个类似倒排索引的List<Int>[]数组，数组的长度就是nums数组的长度，因为如果所有的数字相同，那么
List<Int>[]数组的最后一个位置存储的就是这个数字，所以不用类似bitset就能解决。中间可能会有空洞浪费空间，但是无所谓。

最后逆序遍历这个List<Int>[]数组，只取K个即可。

注意这里可以使用map.put(n, map.getOrDefault(n,0)+1);方法

方法2：使用大顶堆
```
PriorityQueue<Map.Entry<Integer, Integer>> maxHeap =
                 new PriorityQueue<>((a,b)->(b.getValue()-a.getValue()));
for(Map.Entry<Integer,Integer> entry: map.entrySet()){
    maxHeap.add(entry);
}
```
然后依次poll出来。

### [345. Reverse Vowels of a String](https://leetcode.com/problems/reverse-vowels-of-a-string/)

E, String, Two pointer

和344非常类似，用双指针即可。

```
while(start<end && !vowels.contains(chars[start]+"")){
    start++;
}
while(start<end && !vowels.contains(chars[end]+"")){
    end--;
}
```

### [344. Reverse String](https://leetcode.com/problems/reverse-string/)

E, String, Two pointer

多种方式，
1）双指针，注意这里的技巧是用了异或来做交换，而不是临时变量。
```
char[] str = s.toCharArray();
int begin = 0;
int end = s.length() - 1;
while (begin < end) {
  str[begin] = (char) (str[begin] ^ str[end]);
  str[end] = (char) (str[begin] ^ str[end]);
  str[begin] = (char) (str[end] ^ str[begin]);
  begin++;
  end--;
}
return new String(str);
```

2）栈，FILO

3）String += every char left

4）用JDK函数，new StringBuilder(s).reverse().toString()

5）比较巧妙的是还可以用分治算法
```
int length = s.length();
if (length <= 1)
    return s;
String left = s.substring(0, length / 2);
String right = s.substring(length / 2, length);
return reverse5(right) + reverse5(left);
```

### [342. Power of Four](https://leetcode.com/problems/power-of-four/)

E, Bit Manipulation

首先要是2的倍数，然后1都在奇数位上。所以有555这个东西。
```
(16 & 0x55555555) == 16
0001 0000
0101 0101
---------
0001 0000

(8 & 0x55555555) == 0
1000
0101
---- 0000
```

```
return num > 0 && (num & (num - 1)) == 0 && (num & 0x55555555) != 0;
```

### [340. Longest Substring with At Most K Distinct Characters]()

PAID

非常经典的sliding window解决方案，还有另外一道题目159. Longest Substring with At Most Two Distinct Characters，解决了这道，上面那个就OK了。

```
int[] flag = new int[256];
int counter = 0;
int start = 0;
char[] str = s.toCharArray();
int maxLen = 0;
for (int end = 0; end < str.length; end++) {
    if (flag[str[end]] == 0)
        counter++;
    flag[str[end]]++;
    while (counter > k) {
        flag[str[start]]--;
        if (flag[str[start]] == 0)
            counter--;
        start++;
    }
    maxLen = Math.max(maxLen, end - start + 1);
}
return maxLen;
```

### [329. Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/)

H, Depth-first Search Topological Sort Memoization

做过了那么多backtrack的题目，这种DFS非常的easy可以解决。

```
// some preconditions…
int res = 1;
int[][] records = new int[m][n]; //by default every element is 0
// for each [i, j] in matrix, res = max(res, dfs(matrix, records));
for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++) {
        res = Math.max(dfs(i, j, m, n, matrix, records), res);
    }
}
return res;


int dfs(int i, int j, int m, int n, int[][] matrix, int[][] records) {
    if (records[i][j] != 0)
        return records[i][j];   // 这点很重要！类似动态规划的记忆式搜索，like a greedy snake, memorize the result
    int res = 1;
    int[] d1 = new int[] {1, 0, -1, 0};
    int[] d2 = new int[] {0, 1, 0, -1};
    for (int x = 0; x < 4; x++) {
        int nextI = i + d1[x];
        int nextJ = j + d2[x];
        if (nextI >= 0 && nextI < m && nextJ >= 0 && nextJ < n &&
                matrix[nextI][nextJ] > matrix[i][j]) {
            // 是+1不是+res这点刚开始写错了！！！脑子一定要清楚！
            res = Math.max(dfs(nextI, nextJ, m, n, matrix, records) + 1, res);
        }
    }
    records[i][j] = res;
    return res;
}
```


### [326. Power of Three](https://leetcode.com/problems/power-of-three/)

E, Math

递归：
```
if (n <= 0) return false;
return (n == 1) || (n % 3 == 0 && isPowerOfThree(n / 3));
```

非递归：
```
if (n > 1) {
    while (n % 3 == 0)
        n /= 3;
}
return n == 1;
```

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

### [324. Wiggle Sort II](https://leetcode.com/problems/wiggle-sort-ii/)

M, sort

//TODO

quick select思想，中位数在中间，左边全是比他小的，右边全是比他大的。然后依次交换。

### [315. Count of Smaller Numbers After Self](https://leetcode.com/problems/count-of-smaller-numbers-after-self/)

H, Divide and Conquer, Binary Indexed Tree, Segment Tree, Binary Search Tree

比较难的一道题目，求数组的后继元素比自己小的个数。

一开始的错误思路：

使用DP的思想，类似LongestIncreasingSubsequence，从后往前看，写出递推式：
```
dp[i] = dp[j] + 1, j is the first num[j] < num[i]
```
发现结果完全不对，比如`9 5 3 7`，dp[0] = dp[1] + 1，而实际会漏掉7，完全不行。

看了看leetcode提示，有多种方法可以解决，也算是开了眼界。

方法1：BST（二分查找树） 树节点TreeNode记录下列信息：

[参考链接](https://discuss.leetcode.com/topic/31405/9ms-short-java-bst-solution-get-answer-when-building-bst)

构造BST
```
元素值：val
小于该节点的元素个数：leftCnt
节点自身的元素个数：cnt
左孩子：left
右孩子：right
```

方法2：DAC 归并排序merge sort

[参考链接](https://discuss.leetcode.com/topic/31554/11ms-java-solution-using-merge-sort-with-explanation)
//TODO目前只能记下来merge sort，但是一些扩展应用还不太会灵活应用。

方法3：排序后二分查找binary search left-most insertion position

这个方法是自己写出来的实现。套用二分查找search insert position的模板。

```
Integer[] res = new Integer[nums.length];
List<Integer> sorted = new ArrayList<>(nums.length);
for (int i = nums.length - 1; i >= 0; i--)
    int idx = findIndex(nums[i], sorted);  //每次都找在有序数组中的位置，那么就是小于它的元素就是这个索引位置值。
    sorted.add(idx, nums[i]);
    res[i] = idx;

return Arrays.asList(res);

//下面的是模板，必须记住！
int findIndex(int target, List<Integer> sorted) {
    if (sorted.size() == 0) return 0;
    int start = 0;
    int end = sorted.size() - 1;
    if (sorted.get(end) < target) {  //判断一下边界更好些
        return end + 1;
    }
    if (sorted.get(start) >= target) {
        return 0;
    }
    while (start <= end) {
        int mid = start + ((end - start) >> 1);
        int midVal = sorted.get(mid);
        if (target < midVal) {
            end = mid - 1;
        } else if (target > midVal) {
            start = mid + 1;
        } else {
            int idx = mid; //这里非常重要！！！否则结果又的总会比预期的大一点，要处理相同，找到最前面的那个位置，比如3，3，6，6，7，7，7，应该是要返回第一个6的位置，而不是第二个
            while (idx - 1 >= start && sorted.get(idx - 1) == target) {
                idx--;
            }
            return idx;  //否则直接返回mid即可
        }
    }
    return start;
}
```

方法4：树状数组 （Binary Indexed Tree / Fenwick Tree）

```
List<Integer> res = new LinkedList<Integer>();
    if (nums == null || nums.length == 0) {
        return res;
    }
    // find min value and minus min by each elements, plus 1 to avoid 0 element
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (int i = 0; i < nums.length; i++) {
        min = (nums[i] < min) ? nums[i] : min;
    }
    int[] nums2 = new int[nums.length];
    for (int i = 0; i < nums.length; i++) {
        nums2[i] = nums[i] - min + 1;  //从开始
        max = Math.max(nums2[i], max);
    }
    int[] tree = new int[max + 1];
    for (int i = nums2.length - 1; i >= 0; i--) {
        res.add(0, get(nums2[i] - 1, tree));  //-1是为了找比它小的数字
        update(nums2[i], tree);
    }
    return res;
}

private int get(int i, int[] tree) {
    int num = 0;
    while (i > 0) {
        num += tree[i];
        i -= i & (-i);
    }
    return num;
}

private void update(int i, int[] tree) {
    while (i < tree.length) {
        tree[i]++;
        i += i & (-i);
    }
}
```

非常巧妙和简单。详细的BIT的使用见题目307. Range Sum Query - Mutable

[top coder对于BIT的学习，非常值得一看](https://www.topcoder.com/community/data-science/data-science-tutorials/binary-indexed-trees/)

[leetcode讨论](https://discuss.leetcode.com/topic/39656/short-java-binary-index-tree-beat-97-33-with-detailed-explanation/2)


### [307. Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/)

M, Segment Tree Binary Indexed Tree

非常经典的题目，通过这套题要理解BIT和Segament Tree。

方法1：BIT

```
class NumArray {
    int[] nums;
    int[] BIT;
    int n;

    public NumArray(int[] nums) {
        this.nums = nums;

        n = nums.length;
        BIT = new int[n + 1];  //BIT从1-N
        for (int i = 0; i < n; i++) {
            init(i, nums[i]);
        }
    }

    public void init(int i, int val) {
        i++;
        while (i < BIT.length) {   //初始化就从0到N-1
            BIT[i] += val;
            i += (i & -i);
        }
    }

    void update(int i, int val) {
        int diff = val - nums[i];
        nums[i] = val;
        init(i, diff);
    }

    public int getSum(int i) {  //计算就从M到0
        int sum = 0;
        i++;
        while (i > 0) {
            sum += BIT[i];
            i -= (i & -i);
        }
        return sum;
    }

    public int sumRange(int i, int j) {
        return getSum(j) - getSum(i - 1);
    }
}
```

方法2：Segament Tree

```
[1,3,5]
                         [0, 2, sum=9]
                        /           \
               [0, 1,sum=4]         [2, 2,sum=5]
                /         \
        [0, 0,sum=1]    [1, 1,sum=3]


[1,3,5,7,9]
                                       [0, 4,sum=25]
                                      /            \
                           [0, 2,sum=9]           [3, 4,sum=16]
                           /         \             /           \
                  [0, 1,sum=4]    [2, 2,sum=5] [3, 3,sum=7]   [4, 4,sum=9]
                   /     \
        [0, 0,sum=1]   [1, 1,sum=3]


[1,3,5,7,9,11]
                                        [0, 5,sum=36]
                                      /              \
                           [0, 2,sum=9]               [3, 5,sum=27]
                           /         \                 /           \
                  [0, 1,sum=4]    [2, 2,sum=5]   [3, 4,sum=16]   [5, 5,sum=11]
                   /     \                       /          \
        [0, 0,sum=1]   [1, 1,sum=3]      [3, 3,sum=7]   [4, 4,sum=9]
```

```
class SegmentTreeNode {
    int start, end;
    SegmentTreeNode left, right;
    int sum;
}

SegmentTreeNode root = null;

public NumArray(int[] nums) {
    root = buildTree(nums, 0, nums.length - 1);
}

private SegmentTreeNode buildTree(int[] nums, int start, int end) {
    if (start > end) {
        return null;
    } else {
        SegmentTreeNode ret = new SegmentTreeNode(start, end);
        if (start == end) {
            ret.sum = nums[start];
        } else {
            int mid = start + (end - start) / 2;
            ret.left = buildTree(nums, start, mid);
            ret.right = buildTree(nums, mid + 1, end);
            ret.sum = ret.left.sum + ret.right.sum;
        }
        return ret;
    }
}

void update(int i, int val) {
    update(root, i, val);
}

void update(SegmentTreeNode root, int pos, int val) {
    if (root.start == root.end) {
        root.sum = val;
    } else {
        int mid = root.start + (root.end - root.start) / 2;
        if (pos <= mid) {
            update(root.left, pos, val);
        } else {
            update(root.right, pos, val);
        }
        root.sum = root.left.sum + root.right.sum;
    }
}

public int sumRange(int i, int j) {
    return sumRange(root, i, j);
}

public int sumRange(SegmentTreeNode root, int start, int end) {
    if (root.end == end && root.start == start) {
        return root.sum;
    } else {
        int mid = root.start + (root.end - root.start) / 2;
        if (end <= mid) {
            return sumRange(root.left, start, end);
        } else if (start >= mid + 1) {
            return sumRange(root.right, start, end);
        } else {
            return sumRange(root.right, mid + 1, end) + sumRange(root.left, start, mid);
        }
    }
}
```

另外线段树的一个经典题目是光束求阴影面积，在coddding代码里也有，这里可以看下应用线段树的通用模板。

```
class SegmentTreeNode {
    int start, end;
    SegmentTreeNode left, right;
    // 一个想利用的值
}

private SegmentTreeNode buildTree(int start, int end) {
    if (一些退出条件，例如start>end等) {
        //初始化的一些操作
    } else {
        int mid = start + (end - start) / 2;
        // 递归的分左右子树的初始化
        ret.left = buildTree(nums, start, mid);
        ret.right = buildTree(nums, mid + 1, end);
        ret.sum = ret.left.sum + ret.right.sum;
    }
}

public int getResult(SegmentTreeNode root, int start, int end) {
    if (root.end == end && root.start == start) {
        return root.sum;
    } else {
        int mid = root.start + (root.end - root.start) / 2;
        // 递归的来
        if (end <= mid) {
            return sumRange(root.left, start, end);
        } else if (start >= mid + 1) {
            return sumRange(root.right, start, end);
        } else {
            return sumRange(root.right, mid + 1, end) + sumRange(root.left, start, mid);
        }
    }
}
```



### [300. Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)

M, Dynamic Programming, Binary Search

另外和[128. Longest Consecutive Sequence](https://github.com/neoremind/coddding/blob/master/codding/src/main/java/net/neoremind/mycode/argorithm/leetcode/README.md)
不一样，本题只要求是递增的，并非连续，因此并查集并不行。

本地因为要保证元素的原始顺序，因此排序是不可行的，唯一的想法就是DP。

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

### [283. Move Zeroes](https://leetcode.com/problems/move-zeroes/)

E, Array Two Pointers

把0都挪到后面去，同时位置元素的相对位置，也就是说保持稳定。

例如,
```
1, 0, 0, 3, 12
1, 3, 12, 0, 0
```
方法1：遍历遇到0就和后面的第一个非0的元素交换。
```
int i = 0;
while (i < nums.length) {
    if (nums[i] == 0) {
        int k = i + 1;
        while (k < nums.length && nums[k] == 0) {
            k++;
        }
        if (k < nums.length) {
            swap(i, k, nums);
        }
        i++;
    } else {
        i++;
    }
}
```
方法2：原地不动，赋值，不交换，最后都赋0，这个和75题荷兰国旗差不多思想，同时还和26题数组去重差不多。
```
int idx = 0;
for (int i = 0; i < nums.length; i++)
    if (nums[i] != 0)
        nums[idx++] = nums[i];
for (int i = idx; i < nums.length; i++)
    nums[i] = 0;
```

另外如果不要求稳定，那么解法如下：
```
int i = 0; int j = nums.length - 1;
while (i < j)
    if (nums[i] == 0)
        swap(i, j, nums);
        j--;
    else
        i++;
```

### [279. Perfect Squares](https://leetcode.com/problems/perfect-squares/)

M, Dynamic Programming Breadth-first Search Math

1）初始化一个数字，n+1大小，递增，假设都是只有1这个因子

2）找到离n最近的那个平方数，用Math.sqrt来。

3）写动态规划的递推公式：
```
for every square less than i
   dp[i] = min(dp[i], dp[i - square] + 1)
```
4）返回最后的dp[n]即可。

举例来说：
```
* n    combination     count
* 1 => 1                1
* 2 => 1 + 1            2
* 3 => 1 + 1 + 1        3
* 4 => 4                1
* 5 => 4 + 1            2
* 6 => 4 + 1 + 1        3
* 7 => 4 + 1 + 1 + 1    4
* 8 => 4 + 4            2
* 9 => 9                1
* 10 => 9 + 1           2
* 11 => 9 + 1 + 1       3
* 12 => 4 + 4 + 4       3
* 13 => 9 + 4           2
* 14 => 9 + 4 + 1       3
* 15 => 9 + 4 + 1 + 1   4
* 16 => 16              1
```

比如12，

sqrt(12) = 3，

当先尝试减去3的平方，12 - 3^3 = 3，然后就是dp[3] + 1 = 3 + 1 = 4

当先尝试减去2的平方，12 - 2^2 = 8，然后就是dp[8] + 1 = 2 + 1 = 3

当先尝试减去1的平方，12 - 1^1 = 11，然后就是dp[11] + 1 = 3 + 1 = 4

那么最小的就是减去2的平方，所以等于3，而不是4。

如果按照贪心算法greedy来实现，那么结果就是不对的。

这个和找零[coin change](https://github.com/neoremind/coddding/blob/master/codding/src/main/java/net/neoremind/mycode/argorithm/leetcode/README.md#322-coin-change)是一个类型的问题。代码如下：

```
int[] dp = new int[n + 1];
for (int i = 0; i <= n; i++)
    dp[i] = i;
for (int i = 0; i <= n; i++) {
    int m = (int) Math.sqrt(i);
    for (int j = m; j >= 1; j--) {
        int square = j * j;
        if (i >= square) {
            dp[i] = Math.min(dp[i], dp[i - square] + 1);
        }
    }
}
return dp[n];
```

另外一种简单的写法：
```
// 递推式 d[i] = min(d[i - j*j] + 1), j in [0, i] && j * j <= i
int[] dp = new int[n + 1];
Arrays.fill(dp, Integer.MAX_VALUE);
dp[0] = 0;
for (int i = 1; i <= n; i++) {
    for (int j = 1; j * j <= i; j++) {
        dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
    }
}
return dp[n];
```

```
/**
 * 补充问题，输出某个数字的所有perfect square组合，使用回溯方法
 */
public List<List<Integer>> getAllValid(int n) {
    List<List<Integer>> res = new ArrayList<>();
    backtrack(res, new ArrayList<>(), n);
    return res.stream().distinct().collect(Collectors.toList());
}

void backtrack(List<List<Integer>> res, List<Integer> temp, int n) {
    if (n == 0) {
        List<Integer> result = new ArrayList<>(temp);
        Collections.sort(result, (a, b) -> Integer.compare(b, a));
        if (!res.contains(result)) {
            res.add(result);
        }
    } else {
        int m = (int) Math.sqrt(n);
        for (int j = m; j >= 1; j--) {
            int square = j * j;
            if (n >= square) {
                temp.add(square);
                backtrack(res, temp, n - square);
                temp.remove(temp.size() - 1);
            }
        }
    }
}

例如16：
[9, 4, 1, 1, 1]
[9, 1, 1, 1, 1, 1, 1, 1]
[4, 4, 4, 4]
[4, 4, 4, 1, 1, 1, 1]
[4, 4, 1, 1, 1, 1, 1, 1, 1, 1]
[4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
```

### [269. Alien Dictionary]()

PAID, Graph BFS DFS

给一串字符串，是按照字典序排列的，求这个顺序。
```
[
  "wrt",
  "wrf",
  "er",
  "ett",
  "rftt"
]
```
The correct order is: "wertf".

注意这里是字典序，因此每个单词都需要从第一个字符比较，不一样则输出X->Y的一个边，构造两个顶点，然后必须立即break掉。构造一个邻接矩阵的图，然后用BFS，找拓扑排序

```
根据题目构造图如下，

                   |---> r---------> t ----------->f
        w---->e----


adjacency list如下：
       r=Vertex{ch=r, indegree=1, neighbour=[t]}
       t=Vertex{ch=t, indegree=1, neighbour=[f]}
       e=Vertex{ch=e, indegree=1, neighbour=[r]}
       f=Vertex{ch=f, indegree=1, neighbour=[]}
       w=Vertex{ch=w, indegree=0, neighbour=[e]}

稍微修改下条件，在er前面加入tf，如"wrt", "wrf", "tf", "er", "ett", "rftt"，构造一个环，仍然可以识别抛出一个异常
                   |---> r---------> t ----------->f
        w---->e----                  |
             ^                       |
             |-----------------------


adjacency list如下：
       r=Vertex{ch=r, indegree=1, neighbour=[t]}
       t=Vertex{ch=t, indegree=2, neighbour=[f, e]}
       e=Vertex{ch=e, indegree=1, neighbour=[r]}
       f=Vertex{ch=f, indegree=1, neighbour=[]}
       w=Vertex{ch=w, indegree=0, neighbour=[t]}
```

```
class Vertex {
    char ch;
    int indegree;
    ArrayList<Character> neighbour = new ArrayList<>();

    Vertex(char ch) {
        this.ch = ch;
    }
}

public String alienOrder(String[] words) {
    Map<Character, Vertex> map = new HashMap<>();
    //Build the Graph
    for (int i = 0; i < words.length; i++) {
        if (i != words.length - 1) {
            for (int j = 0; j < Math.min(words[i].length(), words[i + 1].length()); j++) {
                if (words[i].charAt(j) != words[i + 1].charAt(j)) {
                    char start = words[i].charAt(j);
                    char end = words[i + 1].charAt(j);
                    map.putIfAbsent(end, new Vertex(end));
                    map.putIfAbsent(start, new Vertex(start));
                    map.get(end).indegree++;
                    map.get(start).neighbour.add(end);
                    break;
                }
            }
        }
    }
    map.entrySet().forEach(System.out::println);
    //Topological Sort
    Queue<Vertex> queue = new LinkedList<>();
    StringBuilder sb = new StringBuilder();

    int counter = 0;
    for (Map.Entry<Character, Vertex> e : map.entrySet()) {
        if (e.getValue().indegree == 0) {
            sb.append(e.getKey());
            queue.offer(e.getValue());
            counter++;
        }
    }
    while (!queue.isEmpty()) {
        Vertex v = queue.poll();
        for (Character ch : v.neighbour) {
            if (--map.get(ch).indegree == 0) {
                queue.offer(map.get(ch));
                counter++;
                sb.append(ch);
            }
        }
    }
    if (counter != map.size()) {
        throw new RuntimeException("Cyclic exists!");
    }
    return sb.toString();
}
```


### [268. Missing Number](https://leetcode.com/problems/missing-number/)

M,Array Math Bit Manipulation

```
index : 0   1   2   3   4
value   0   4   1   2
-XOR----------------------
      0-0  1-1 2-2
      剩下3和4，那么要得到3就是再次做XOR4也就是长度，去掉4即可。

index : 0   1   2   3   4
value   0   3   1   2
-XOR----------------------
      0-0  1-1 2-2  3-3
      剩下0，那么要得到0就是再次做XOR4也就是长度，4^0=4。
```

```
int res = 0;
for (int i = 0; i < nums.length; i++)
    res ^= i;
    res ^= nums[i];
return res ^ nums.length;
```

### [264. Ugly Number II](https://leetcode.com/problems/ugly-number-ii/)

M, Dynamic Programming Heap Math

方法1：暴力解法，利用题目263。

方法2：使用类似DP的思想

```
(1) 1×2, 2×2, 3×2, 4×2, 5×2, …
(2) 1×3, 2×3, 3×3, 4×3, 5×3, …
(3) 1×5, 2×5, 3×5, 4×5, 5×5, …
```
所有的丑数就是在上面那个集合里面去选择数字，维护三个指针index2，index3，index5分别指向各自的起点，然后依次找下一个，
三者比较Math.min(Math.min(factor2，factor3)，factor5)找最小的填在dp数组里面。

```
[1, 2, 3, 4, 5, 6, 8, 9, 10, 12]
    ^
    |
    |
    |
 index2/index3/index5
```

```
int[] ugly = new int[n];
ugly[0] = 1;
int index2 = 0, index3 = 0, index5 = 0;
int factor2 = 2, factor3 = 3, factor5 = 5;
for(int i=1;i<n;i++){
    int min = Math.min(Math.min(factor2,factor3),factor5);
    ugly[i] = min;
    if(factor2 == min)
        factor2 = 2*ugly[++index2];
    if(factor3 == min)
        factor3 = 3*ugly[++index3];
    if(factor5 == min)
        factor5 = 5*ugly[++index5];
return ugly[n-1];
```

### [263. Ugly Number](https://leetcode.com/problems/ugly-number/)

E, Math

丑数的质因数只包含2, 3, 5，例如6, 8是丑数。14不是，因为7是因数。注意1是特殊的丑数。

```
if (num < 1) {return false;}
if (num != 1)
    while(num % 2 == 0) num = num / 2;
    while(num % 3 == 0) num = num / 3;
    while(num % 5 == 0) num = num / 5;
return num == 1;
```

### [260. Single Number III](https://leetcode.com/problems/single-number-iii/)

M, Bit Manipulation

two elements appear only once and all the other elements appear exactly twice. Find the two elements that appear only once.

用(E) Single Number (M) Single Number II的那种解法不行了。考虑如下巧妙的解法：
```
// Pass 1 :
// Get the XOR of the two numbers we need to find
int diff = 0;
for (int num : nums)
    diff ^= num;
// Get its last set bit  这一步非常的重要，会返回某个数字的最后一个1出现的位置，也就是right-most 1!!!!!
diff &= -diff;

// Pass 2 :
int[] rets = {0, 0}; // this array stores the two numbers we will return
for (int num : nums)
{
    if ((num & diff) == 0) { // the bit is not set  // 这right-most两个数有分歧，那么就异或所有符合条件的数字肯定可以过滤出两个数字
        rets[0] ^= num;
    }
    else { // the bit is set
        rets[1] ^= num;
    }
}
return rets;
```

寻找right-most bit的过程如下，例如：
```
int x = 10; //1010
int res = x & -x;
assertThat(res, is(2));
x = 768; //1100000000
int res = x & -x;
assertThat(res, is(256));
```

另外请复习如下的[基础知识](http://neoremind.com/2014/09/java%E5%9F%BA%E7%A1%80%E7%B1%BB%E5%9E%8B%E5%AD%98%E5%82%A8%E4%B8%8E%E8%BF%90%E7%AE%97%E7%9F%A5%E8%AF%86/)


### [257. Binary Tree Paths](https://leetcode.com/problems/binary-tree-paths/)

E, Tree Depth-first Search

类似NQueens问题的回溯DFS。
```
public List<String> binaryTreePaths(TreeNode root) {
    List<String> result = new ArrayList<>();
    backtrack(root, result, new ArrayList<>());
    return result;
}

void backtrack(TreeNode root, List<String> result, List<Integer> tempList) {
    if (root == null) {
        return;
    } else if (root.left == null && root.right == null) {
        tempList.add(root.val);
        result.add(toPathString(tempList));
        tempList.remove(tempList.size() - 1);
    } else {
        tempList.add(root.val);
        backtrack(root.left, result, tempList);
        backtrack(root.right, result, tempList);
        tempList.remove(tempList.size() - 1);
    }
}

toPathString可以用java8的lambda表达式：
return tempList.stream().map(String::valueOf).collect(Collectors.joining("->"));
```

### [253. Meeting Rooms II]()

PAID

Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] find the minimum number of conference rooms required.

使用最小堆来解题的方法，这种方法先把所有的时间区间按照起始时间排序，然后新建一个最小堆，开始遍历时间区间，如果堆不为空，且首元素小于等于当前区间的起始时间，我们去掉堆中的首元素，把当前区间的结束时间压入堆，由于最小堆是小的在前面，那么假如首元素小于等于起始时间，说明上一个会议已经结束，可以用该会议室开始下一个会议了，所以不用分配新的会议室，遍历完成后堆中元素的个数即为需要的会议室的个数

```
if (intervals == null || intervals.length == 0) return 0;

// Sort the intervals by start time
Arrays.sort(intervals, (a, b) -> a.start - b.start);

// Use a min heap to track the minimum end time of merged intervals
PriorityQueue<Interval> heap = new PriorityQueue<>(intervals.length, (a, b) -> a.end - b.end);

// start with the first meeting, put it to a meeting room
heap.offer(intervals[0]);

for (int i = 1; i < intervals.length; i++)
    // get the meeting room that finishes earliest
    Interval interval = heap.poll();

    if (intervals[i].start >= interval.end)
        // if the current meeting starts right after there's no need for a new room, merge the interval
        interval.end = intervals[i].end;
    else
        // otherwise, this meeting needs a new room
        heap.offer(intervals[i]);

    // don't forget to put the meeting room back
    heap.offer(interval);

return heap.size();
```

### [252. Meeting Rooms]()

PAID

Given an array of meeting time intervals consisting of start and end times `[[s1,e1],[s2,e2],...] (si < ei)`,
determine if a person could attend all meetings.

For example, Given [[0, 30],[5, 10],[15, 20]], return false.

```
if (intervals == null)
    return false;

// Sort the intervals by start time
Arrays.sort(intervals, new Comparator<Interval>()
    public int compare(Interval a, Interval b)
        return a.start - b.start;

for (int i = 1; i < intervals.length; i++)
    if (intervals[i].start < intervals[i - 1].end) {
        return false;

return true;
```

### [251. Flatten 2D Vector]()

PAID

用JDK自带的iterator。
```
class Vector2D {
    List<Iterator<Integer>> its;
    int curr = 0;

    public Vector2D(List<List<Integer>> vec2d) {
        this.its = new ArrayList<>();
        for (List<Integer> l : vec2d) {
            // 只将非空的迭代器加入数组
            if (l != null && l.size() > 0) {
                this.its.add(l.iterator());
            }
        }
    }

    public int next() {
        Integer res = its.get(curr).next();
        // 如果该迭代器用完了，换到下一个
        if (!its.get(curr).hasNext()) {
            curr++;
        }
        return res;
    }

    public boolean hasNext() {
        return curr < its.size() && its.get(curr).hasNext();
    }
}
```

扩展的话，可以使用row、col标记，还可以支持remove方法。

### [242. Valid Anagram](https://leetcode.com/problems/valid-anagram/)

E, Hash Table Sort

```
int[] alphabet = new int[26];
for (int i = 0; i < s.length(); i++) alphabet[s.charAt(i) - 'a']++;
for (int i = 0; i < t.length(); i++) alphabet[t.charAt(i) - 'a']--;
for (int i : alphabet) if (i != 0) return false;
return true;
```


### [240. Search a 2D Matrix II](https://leetcode.com/problems/search-a-2d-matrix-ii/)

M, Binary Search, Divide and Conquer

和题目74类似。只不过不能看做一个sorted list，但是方法1还是通用适用的。

方法1：
剑指offer当中的题目3：二维数组中的查询的变种。可以用一个通用的方法：这个解法是从右上角开始找，如果target小于右上角的，则col--，如果大于右上角，则row++，直到数组越界或者找到target为止。

方法2：分治算法。
```
zone 1      zone 2
*  *  *  * | *  *  *  *
*  *  *  * | *  *  *  *
*  *  *  * | *  *  *  *
*  *  *  * | *  *  *  *
-----------------------
*  *  *  * | *  *  *  *
*  *  *  * | *  *  *  *
*  *  *  * | *  *  *  *
*  *  *  * | *  *  *  *
  zone 3      zone 4
```
We then compare the element in the center of the matrix with the target. There are three possibilities:

center < target. In this case, we discard zone 1 because all elements in zone 1 are less than target.

center > target. In this case, we discard zone 4.

center == target. return true.

```
boolean searchMatrixDAC(int[][] matrix, int stX, int stY, int edX, int edY, int target) {
    if (stX >= edX || stY >= edY)  //可以是==
        return false;
    int max = matrix[edX - 1][edY - 1];
    int min = matrix[stX][stY];
    if (min <= target && target <= max) {
        int mdX = (stX + edX) / 2;
        int mdY = (stY + edY) / 2;
        if (matrix[mdX][mdY] > target) { //ignore zone 4
            return searchMatrixDAC(matrix, stX, stY, mdX, mdY, target) ||
                    searchMatrixDAC(matrix, stX, mdY, mdX, edY, target) ||
                    searchMatrixDAC(matrix, mdX, stY, edX, mdY, target);
        } else if (matrix[mdX][mdY] < target) { //ignore zone 1
            return searchMatrixDAC(matrix, stX, mdY, mdX, edY, target) ||
                    searchMatrixDAC(matrix, mdX, stY, edX, mdY, target) ||
                    searchMatrixDAC(matrix, mdX, mdY, edX, edY, target);
        } else {
            return true;
        }
    }
    return false;
}

return searchMatrixDAC(matrix, 0, 0, matrix.length, matrix[0].length, target);
```

### [236. Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/)

M, Tree

[LCA问题]
(https://www.hrwhisper.me/algorithm-lowest-common-ancestor-of-a-binary-tree/)

[JULY的LCA]
(https://github.com/julycoding/The-Art-Of-Programming-By-July/blob/master/ebook/zh/03.03.md)

1. 普通玩法：
BST
很简单的思路就是看两个值在root的哪边：
两个值都在左边，则LCA在左边
两个值都在右边，则LCA在右边
一个在左一个在右，则说明LCA就是当前的root节点。

2. 普通二叉树：
* 带有父节点信息的二叉树：转换为链表求交点问题
* 一个简单的思路，对p和q向上走，用hashtable记录访问过的节点，如果某个节点已经被访问过了，那么返回该节点。

A Top-Down Approach (Worst case O(n2) ): Let’s try the top-down approach where we traverse the nodes from the top to the bottom. First, if the current node is one of the two nodes, it must be the LCA of the two nodes. If not, we count the number of nodes that matches either p or q in the left subtree (which we call totalMatches). If totalMatches equals 1, then we know the right subtree will contain the other node. Therefore, the current node must be the LCA. If totalMatches equals 2, we know that both nodes are contained in the left subtree, so we traverse to its left child. Similar with the case where totalMatches equals 0 where we traverse to its right child.
```
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || p == null || q == null) {
        return null;
    }
    if (root == p || root == q) {
        return root;
    }
    int totalMatches = countMatches(root.left, p, q);
    if (totalMatches == 1) {
        return root;
    } else if (totalMatches == 2) {
        return lowestCommonAncestor(root.left, p, q);
    } else {  //totalMatches = 0
        return lowestCommonAncestor(root.right, p, q);
    }
}

int countMatches(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null) {
        return 0;
    }
    int matches = countMatches(root.left, p, q) + countMatches(root.right, p, q);
    if (root == p || root == q) {
        return 1 + matches;
    } else {
        return matches;
    }
}
```

A Bottom-up Approach (Worst case O(n) ): Using a bottom-up approach, we can improve over the top-down approach by avoiding traversing the same nodes over and over again.
We traverse from the bottom, and once we reach a node which matches one of the two nodes, we pass it up to its parent. The parent would then test its left and right subtree if each contain one of the two nodes. If yes, then the parent must be the LCA and we pass its parent up to the root. If not, we pass the lower node which contains either one of the two nodes (if the left or right subtree contains either p or q), or NULL (if both the left and right subtree does not contain either p or q) up.
```
public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) {
        return root;
    }
    TreeNode left = lowestCommonAncestor2(root.left, p, q);
    TreeNode right = lowestCommonAncestor2(root.right, p, q);
    if (left != null && right != null) {
        return root;
    } else {
        return left != null ? left : right;
    }
}
```

3. 高级玩法：
* 多次查询，离线算法Tarjan，利用并查集优越的时空复杂度，可以实现O(n+q)的算法，q是查询次数。
* 在线算法RMQ（Sparse table）一个O(nlog2n)的预处理，O(1)的查询。
* 线段树（segement tree）


### [235. Lowest Common Ancestor of a Binary Search Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)

E, Tree

很简单的思路就是看两个值在root的哪边：
* 两个值都在左边，则LCA在左边
* 两个值都在右边，则LCA在右边
* 一个在左一个在右，则说明LCA就是当前的root节点。

```
if (p.val < root.val && q.val < root.val)
    return lowestCommonAncestor(root.left, p, q);
else if (p.val > root.val && q.val > root.val)
    return lowestCommonAncestor(root.right, p, q);
else
    return root;
```


### [232. Implement Queue using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/)

E, Stack Design

两个队列一进一出。

```
Stack<Integer> input = new Stack();
Stack<Integer> output = new Stack();

public void push(int x) { input.push(x); }

public void pop() { peek(); output.pop(); }

public int peek() {
    if (output.empty()) {
        while (!input.empty()) {
            output.push(input.pop());
        }
    }
    return output.peek();
}

public boolean empty() { return input.empty() && output.empty(); }
```

### [231. Power of Two](https://leetcode.com/problems/power-of-two/)

E, Math Bit Manipulation

```
if (n < 1) {return false;}
return (n & (n - 1)) == 0;
```


### [230. Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)

M, Binary Search Tree

三种解法：

1）二分查找思想
```
int count = countNodes(root.left);
if (k <= count) {
    return kthSmallest(root.left, k);
} else if (k > count + 1) {
    return kthSmallest(root.right, k - 1 - count); // 1 is counted as current node
}
return root.val;

int countNodes(TreeNode n) {
    if (n == null) return 0;
    return 1 + countNodes(n.left) + countNodes(n.right);
}
```

2）中序遍历，递归
```
int counter = 0;int num = 0;
public int kthSmallest(TreeNode root, int k) {
    helper(root, k);
    return num;
}

void helper(TreeNode node, int k) {
    if (node.left != null) {
        helper(node.left, k);
    }
    if (++counter == k) {
        num = node.val;
        return;
    }
    if (node.right != null) {
        helper(node.right, k);
    }
}
```

3）中序遍历，非递归，使用stack，模板！！！和中序遍历题目94一样。非常巧妙。
```
int counter = 0;
Stack<TreeNode> stack = new Stack<TreeNode>();
TreeNode cur = root;
while (cur != null || !stack.empty()) {
    if (cur != null) {
        stack.push(cur);
        cur = cur.left;
    } else {
        TreeNode node = stack.pop();
        counter++;
        if (counter == k) { return node.val; }
        cur = node.right;
    }
}
return -1;
```

### [226. Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree/)

E, Tree

```
public TreeNode invertTree(TreeNode root)
    if (root == null)
        return null;
    TreeNode temp = root.right;
    root.right = root.left;
    root.left = temp;
    invertTree(root.left);
    invertTree(root.right);
    return root;
```

### [225. Implement Stack using Queues](https://leetcode.com/problems/implement-stack-using-queues/)

E, Stack Design

There are two solutions cost O(n) and O(1) for different operations:
* push: O(n), pop/top: O(1)
* push: O(1), pop/top: O(n)
Time efficiency depends on operation frequency of push, pop, top:
* if push>pop+top, second solution is better.
* if push<pop+top, first solution is better.

下面的例子是O(n), pop/top: O(1)的方法：
```
Queue<Integer> queue = new LinkedList<Integer>();
public void push(int x) {
    queue.add(x);
    for (int i = 0; i < queue.size() - 1; i++) {
        queue.add(queue.poll());
    }
}
public void pop() { queue.poll(); }
public int top() { return queue.peek(); }
public boolean empty() { return queue.isEmpty(); }
```

### [224. Basic Calculator](https://leetcode.com/problems/basic-calculator/)

H, Stack  Math

把计算只看做加法，运算符实际就是符号位，遇到了()那么把之前的计算结果以及符号压栈保存，计算后面的，直到)弹出符号，符号就是后面计算结果的sign，然后和栈里的值相加。

```
int len = s.length(), sign = 1, result = 0;
for (int i = 0; i < len; i++)
    if (Character.isDigit(s.charAt(i)))
        int sum = s.charAt(i) - '0';
        while (i + 1 < len && Character.isDigit(s.charAt(i + 1)))
            sum = sum * 10 + s.charAt(i + 1) - '0';
            i++;
        result += sum * sign;
    else if (s.charAt(i) == '+')
        sign = 1;
    else if (s.charAt(i) == '-')
        sign = -1;
    else if (s.charAt(i) == '(')
        stack.push(result);
        stack.push(sign);
        result = 0;
        sign = 1;
    else if (s.charAt(i) == ')')
        result = result * stack.pop() + stack.pop();
return result;
```

### [223. Rectangle Area](https://leetcode.com/problems/rectangle-area/)

E, Math

```

        ----------------- C
       |                 |
       |                 |
        -----------------
      A               ----------------------    -> X axis
                     |                      |
                    E ---------------------- G


        ----------------- D
       |                 |
       |                 |
        -----------------                     ^ Y axis
      B               ---------------------- H
                     |                      |
                    F ----------------------

关键判断是否overlap：
1）left=左“边”最大，right=右“边”最小，如果overlap，那么left
同理,
2）top=上“边”最小，bottom=下“边”最大，如果overlap，那么top>bottom。
然后重合的大小就(right - left) * (top - bottom)
```

```
int area1 = (C - A) * (D - B);
int area2 = (G - E) * (H - F);
if(right > left && top > bottom) { //使用Math.min或者max计算边界的重合
    overlap = (right - left) * (top - bottom);
return area1 + area2 - overlap;
```

### [222. Count Complete Tree Nodes](https://leetcode.com/problems/count-complete-tree-nodes/)

M, Tree Binary Search

完全二叉树的数量=2^N-1，否则就单独计算左右子树+1.
```
public int countNodes(TreeNode root)
    int rightDepth = rightDepth(root);
    int leftDepth = leftDepth(root);
    if (leftDepth == rightDepth)
        return (1 << leftDepth) - 1;
    else
        return 1 + countNodes(root.left) + countNodes(root.right);

int rightDepth(TreeNode root)
    int res = 0;
    while (root != null) {
        res++;
        root = root.right;
    return res;

//left same as right
```

### [221. Maximal Square](https://leetcode.com/problems/maximal-square/)

M, Dynamic Programming

和[题目85-(H) Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/)非常的类似，和直方图题目84联系紧密，
但是这个题目完全不一样，是一个动态规划的问题。

最大的正方形是其三个邻居的最小值+1.

比如
```
0 1 1 1
0 1 1 1
0 1 1 1

dp矩阵是
0 1 1 1
0 1 2 2
0 1 2 3
```

```
if (matrix == null || matrix.length == 0) {
    return 0;
int result = 0;
int[][] dp = new int[matrix.length][matrix[0].length];
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[0].length; j++) {
        if (i == 0 || j == 0) {
            dp[i][j] = matrix[i][j] - '0';
        } else {
            if (matrix[i][j] == '1') {
                dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i - 1][j - 1], dp[i][j - 1])) + 1;
            }
        }
        result = Math.max(result, dp[i][j] * dp[i][j]);
    }
}
return result;
```

### [220. Contains Duplicate III](https://leetcode.com/problems/contains-duplicate-iii/)

M, Binary Search Tree

看数组中是否存在i，j，满足
* i, j的举例最多是k
* nums[i]，nums[j]的差最大是t

解法1：brute force

遍历数字，找一个固定的window，长度为k，然后去遍历这个小window和头元素的大小，如果绝对值在k之内（包含），则返回true。

会存在一个case是比如-1 - Integer.MIN，这会溢出，所以要用long。该暴力破解会TLE，时间复杂度是O(NK)。

```
注意三点
1）j=i+1
2）j - i <= k
3）j < nums.length，放在上面的i < nums.length无法防止k>nums.length的情况
4）绝对值要用long，避免减法溢出
for (int i = 0; i < nums.length; i++)
    for (int j = i + 1; j - i <= k && j < nums.length; j++)
        if (Math.abs((long) nums[i] - (long) nums[j]) <= t)
            return true;
return false;
```

解法2：BST

那么有没有什么好的办法，在第一种解法的基础上，优化时间复杂度呢，如果K很大，就会退化为O(N^2)，这是无法接受的，
在K上做文章，使之将为logK，这个是一个很大提高，那么内层循环的查询遍历，可以使用BST平衡树，比如AVL，red-black tree之类的。

这里使用了TreeSet里面的floor和ceiling方法，非常巧妙，floor找尽可能大的，ceiling找尽可能小的，同时要避免找到自己，所以加上>=自己或者<=自己的条件。

这个BST要维护一个大小，就是k，超过了k就需要删除掉nums[i - k]，保证这课树的元素的举例都在k之内（包含）。

```
if (nums == null || nums.length == 0 || k <= 0) return false;

TreeSet<Long> bst = new TreeSet<>();
for (int i = 0; i < nums.length; i++) {
    /**
     * Returns the greatest key less than or equal to the given key,
     * or {@code null} if there is no such key.
     */
    Long floor = bst.floor((long) nums[i] + t);
    /**
     * Returns the least key greater than or equal to the given key,
     * or {@code null} if there is no such key.
     */
    Long ceiling = bst.ceiling((long) nums[i] - t);
    if ((floor != null && floor >= nums[i]) || (ceiling != null && ceiling <= nums[i])) {
        return true;
    }
    bst.add((long) nums[i]);
    if (bst.size() > k) {
        bst.remove((long) nums[i - k]);
    }
}
return false;
```

解法3：bucket桶排序思想

这种解法有点突破，比解法2的性能还要好，在LC可以打败82%，而解法只能30%。

使用桶的思想如下，一个bucket sort如下：
```
int[] bucket = new int[maxVal + 1];
for (int i = 0; i < a.length; i++) {
    bucket[a[i]]++;
}
int outPos = 0;
for (int i = 0; i < bucket.length; i++) {
    for (int j = 0; j < bucket[i]; j++) {
        a[outPos++] = i;
    }
}
```

关于桶排序需要了解的如下：
```
桶排序的时间和空间复杂度如下：

Time
Worst case (n^2)
Best case  O(n + k)
Average case  O(n + k)

Space:
O(n+k) auxiliary

集中桶排序的分类：
Counting sort: buckets hold only a single value，1对1
Bucket sort: buckets hold a range of values，1个桶N个元素，基于range来
Radix sort: buckets hold values based on digits within their values

-----  -----  -----  -----     -----
| 1 |  | 2 |  | 3 |  | 4 | ... | N |
|   |  |   |  |   |  |   |     |   |
|   |  |   |  |   |  |   |     |   |
-----  -----  -----  -----     -----

  ^      ^      ^      ^         ^
  |      |      |      |

  4     11     23      37
  9     12     26
 21

来了一个数字先找自己的桶，放进入，然后桶内部的数据比较小，可以随意排序，如果桶和数字都是1对1的，那么是最快的，但是需要牺牲很多空间。
理论上bucketSize越大，桶排序就会越快。

一个实际的代码如下：http://www.growingwiththeweb.com/2015/06/bucket-sort.html
```

回到这道题，因为数字的是很大的，甚至有可能是Integer.MAX_VALUE，因此这样会太浪费空间，其实我们不需要最终的排序结果，
只需要找有没有两个元素符合我们的要求，这样我们可以把桶的size设为t，如果有两个元素都在一个桶里肯定就是距离小于t了，
当然还得考虑相邻的两个桶里的元素也可能复合要求，因此也得把邻居们和自己的里的数字计算下。

另外要维护的小window是k大小，因此一旦超过k就得从桶里删除掉最早插入的那个。

由于桶可能会非常的稀疏，因此采用Map，而不是顺序数据结构来解决。

bucket当中的key就是经过归一化处理的，必须大于0，然后“除以”桶的大小，可以保证基于range的分区落桶，
一旦在window内有数字在相同的桶里面，或者在相邻的桶里并且举例小于等于t，则返回true

```
if (k < 1 || t < 0) return false;
Map<Long, Long> bucket = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    long mappedValue = (long) nums[i] - Integer.MIN_VALUE;
    long hashedKey = mappedValue / ((long) t + 1);
    if (bucket.containsKey(hashedKey) ||
            (bucket.containsKey(hashedKey - 1) && mappedValue - bucket.get(hashedKey - 1) <= t) ||
            (bucket.containsKey(hashedKey + 1) && bucket.get(hashedKey + 1) - mappedValue <= t)) {
        return true;
    }
    bucket.put(hashedKey, mappedValue);
    if (bucket.size() > k) {
        bucket.remove(((long) nums[i - k] - Integer.MIN_VALUE) / ((long) t + 1));
    }
}
return false;
```


### [219. Contains Duplicate II](https://leetcode.com/problems/contains-duplicate-ii/)

E, Array Hash table

```
for (int i = 0; i < nums.length; i++)
    if (map.containsKey(nums[i]))
        if (Math.abs(map.get(nums[i]) - i) <= k) return true;
    map.put(nums[i], i);
return false;
```

### [217. Contains Duplicate](https://leetcode.com/problems/contains-duplicate/)

E, Array Hash table

方法1：使用set

方法2：排序之后，比较i和i-1


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


### [215. Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/)

M, Heap Divide and Conquer

方法1：纯排序：
时间 O(NlogN)，时间不可接受

方法2：部分排序
建立一个长度为k的数组，找到最小的元素MIN。
```
for num in a[n-k...n]
   if num > MIN
       delete MIN and find MIN again
```
时间复杂度O(k) + (n-k)O(k) = O(nk)，当k比较大的时候退化为O(N^2)，不可取。

方法3：建立一个小顶堆 Binary Heap

时间 O(NlogK)，空间 O(K)

可以用自己写的堆，也可以用JDK自带的优先队列PriorityQueue。这个比方法二的优点就是堆找MIN的时间复杂度降为了O(logK)。

自己实现堆只用实现buildHeap和deleteMin（也就是siftDown）方法即可，不用实现add（即siftUp）方法。

```
PriorityQueue<Integer> p = new PriorityQueue<Integer>();
for (int i = 0; i < k; i++) {
    p.add(nums[i]);
}
for (int i = k; i < nums.length; i++) {
    if (p.peek() < nums[i]) {
        p.add(nums[i]); //这里堆的大小如果加入了就是k+1，注意！！！
        p.poll();
    }
}
return p.poll();
```

方法4：分治思想的，线性选择算法

利用Quick Sort思想做一个Quick select

时间 Avg O(N) Worst O(N^2)，空间 O(1)

跟快速排序一个思路。先取一个枢纽值，将数组中小于枢纽值的放在左边，大于枢纽值的放在右边，具体方法是用左右两个指针，
如果他们小于枢纽值则将他们换到对面，一轮过后记得将枢纽值赋回分界点。如果这个分界点是k，说明分界点的数就是第k
个数。否则，如果分界点大于k，则在左半边做同样的搜索。如果分界点小于k，则在右半边做同样的搜索。

```
public int findKthLargest(int[] nums, int k) {
    // 处理两个特殊的情况
    // 找第最后大的，那就是找最小的
    if (nums.length == k)
        return min;

    // 找第一个大的，那就是最大的了
    if (k == 1)
        return max

    // 先分隔数组，枢纽是mid，左边的全大于mid，右边的全小于mid
    int mid = quickSelect(nums, 0, nums.length - 1);
    int right = nums.length - 1;
    int left = 0;
    while (true) {
        if (mid == k - 1) { // 这里是k-1的原因是，Kth大的，那就是枢纽索引是K-1，否则Kth大的就是枢纽左边中最小的，不是枢纽本身
            break;
        } else if (mid < k - 1) {
            left = mid + 1;  // 缩小左边界
            mid = quickSelect(nums, mid + 1, right);
        } else {
            right = mid - 1;  // 缩小右边界
            mid = quickSelect(nums, left, mid - 1);
        }
    }
    return nums[mid];
}

//快速选择，参考{@link net.neoremind.mycode.argorithm.sort.QuickSort}来实现
private int quickSelect(int[] nums, int low, int high) {
    int pivot = nums[low];
    int i = low;
    int j = high + 1;
    while (true) {
        while (nums[++i] > pivot) {
            if (i == high) {
                break;
            }
        }
        while (nums[--j] < pivot) {
            if (j == low) {
                break;
            }
        }
        if (i >= j) {
            break;
        }
        swap(nums, i, j);
    }
    swap(nums, low, j);
    return j;
}
```

### [213. House Robber II](https://leetcode.com/problems/house-robber-ii/)

M, Dynamic Programming

和题目198类似，这道题就是在上一题的基础上加了一个条件，变成了环，所以如果抢了第一家，就不能抢最后一家。所以我们可以分别计算抢了从第二家到最后一家与抢从第一家到倒数第二家的最大值，取两个值中更大的那个就是结果。

### [212. Word Search II](https://leetcode.com/problems/word-search-ii/)

H,  Backtracking Trie

类似[Boggle Game](http://www.geeksforgeeks.org/boggle-find-possible-words-board-characters/)，给多个字符串，在board里面找所有的字符串。非常传统的和Word Search一样，用backtrack

```
int[] d1 = new int[] {-1, 1, 0, 0};int[] d2 = new int[] {0, 0, -1, 1};

public List<String> findWords(char[][] board, String[] words) {
    Set<String> res = new HashSet<>();
    int row = board.length; int col = board[0].length;
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
            dfs(board, words, i, j, row, col, 0, res, new boolean[row][col], getAllWordsIndex(words.length));
    return new ArrayList<>(res);
}

void dfs(char[][] board, String[] words, int i, int j, int row, int col, int index, Set<String> res,
         boolean[][] visited, List<Integer> wordsIndex) {
    if (visited[i][j])
        return;
    List<Integer> toBeAbleToContinueWordsIndex = new ArrayList<>();
    for (Integer idx : wordsIndex) {  // idx is word index in String[] words
        String word = words[idx];
        if (index < word.length() && word.charAt(index) == board[i][j]) {
            if (index == word.length() - 1) {
                res.add(word);
            } else {
                toBeAbleToContinueWordsIndex.add(idx);
            }
        }
    }
    if (toBeAbleToContinueWordsIndex.isEmpty()) {
        return;
    visited[i][j] = true;
    for (int k = 0; k < 4; k++) {
        int newI = i + d1[k];
        int newJ = j + d2[k];
        if (newI < 0 || newI >= row || newJ < 0 || newJ >= col) {
            continue;
        dfs(board, words, newI, newJ, row, col, index + 1, res, visited, toBeAbleToContinueWordsIndex);
    visited[i][j] = false;
}
```

这个解法的问题就是当words集非常大的时候，内部维护了一个toBeAbleToContinueWordsIndex虽然看似搜索效率OK，虽然也做到了剪枝，但是频繁的新建对象，对于又成千山万次的backtrack来说
负担太大，因此考虑空间换时间，可以预先做好一个可以判断是否word合法的解决方案吗？有的，就是优化版本方法2，使用Trie Tree。

优化版本：

只需要修改dfs函数内部，使用trie树判断单词是否在字典里面，非常快了，不用频繁的创建对象。这是Trie树的优势所在。
```
void dfs(char[][] board, int i, int j, int row, int col, String temp, Set<String> res, boolean[][] visited, Trie trie) {
    if (visited[i][j]) {
        return;
    }
    temp += board[i][j];
    if (!trie.startsWith(temp)) {
        return;
    }
    if (trie.search(temp)) {
        res.add(temp);
    }
    visited[i][j] = true;
    for (int k = 0; k < 4; k++) {
        int newI = i + d1[k];
        int newJ = j + d2[k];
        if (newI < 0 || newI >= row || newJ < 0 || newJ >= col) {
            continue;
        }
        dfs(board, newI, newJ, row, col, temp, res, visited, trie);
    }
    visited[i][j] = false;
}
```

### [211. Add and Search Word - Data structure design](https://leetcode.com/problems/add-and-search-word-data-structure-design/)

M, Backtracking Trie Design

利用[208. Implement Trie (Prefix Tree)](https://github
.com/neoremind/coddding/blob/master/codding/src/main/java/net/neoremind/mycode/argorithm/leetcode/README.md#208-implement-trie-prefix-tree)
构造的字典树（前缀树）来实现，只需要修改search方法即可，使用dfs的backtrack来不断的搜索，实际就是剪枝的过程。

遇到'.'，那么就遍历所有的节点的child，skip这个几点的char验证，如果dfs下面任何一个是合法的，则返回true。

```
// Your WordDictionary object will be instantiated and called as such:
// WordDictionary wordDictionary = new WordDictionary();
// wordDictionary.addWord("bad");
// wordDictionary.search("b.d");

public class WordDictionary {
    Trie trie = new Trie();
    public void addWord(String word) {
        trie.insert(word);
    }
    public boolean search(String word) {
        return trie.search(word);
    }

    class TrieNode {
        char content;
        LinkedList<TrieNode> children = new LinkedList<>();
        boolean isEnd;
        int count;
        public TrieNode() {
        }
        public TrieNode(char c) {
            this.content = c;
        }
        public TrieNode subTrieNode(char c) {
            for (TrieNode child : children) {
                if (child.content == c) {
                    return child;
                }
            }
            return null;
        }
    }

    class Trie {
        private TrieNode root;
        public Trie() {
            root = new TrieNode();
        }
        public void insert(String word) {
            if (word == null || word.length() == 0)
                return;
            TrieNode curr = root;
            for (char c : word.toCharArray()) {
                TrieNode node = curr.subTrieNode(c);
                if (node == null) {
                    node = new TrieNode(c);
                    curr.children.add(node);
                }
                node.count++;
                curr = node;
            }
            curr.isEnd = true;
        }

        public boolean search(String word) {
            if (word == null || word.length() == 0)
                return false;
            return dfs(word.toCharArray(), 0, root);
        }

        public boolean dfs(char[] str, int idx, TrieNode node) {
            if (idx >= str.length) {
                if (node.isEnd) {
                    return true;
                } else {
                    return false;
                }
            }
            char target = str[idx];
            if (target == '.') {
                for (TrieNode child : node.children) {
                    if (dfs(str, idx + 1, child)) {
                        return true;
                    }
                }
                return false;
            } else {
                TrieNode child = node.subTrieNode(target);
                if (child == null) {
                    return false;
                }
                return dfs(str, idx + 1, child);
            }
        }
    }
}
```

### [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)

M,  Depth-first Search Breadth-first Search Graph Topological Sort

和题目207类似，只不过在queue里poll出来的时候，如果in-degree=0，重新add进去，顺便更新下返回的数组即可。

当然也可以按照下面的来，但是leetcode上一些corner condition总是过不去，没AC。

```
/** 图的顶点*/
class Vertex {
    /** 与该顶点邻接的顶点*/
    public ArrayList<Integer> adjacences = new ArrayList<>();
    /** 入度，也就是指向这个节点的边的数量*/
    public int inDegree = 0;
}

public int[] findOrder(int numCourses, int[][] prerequisites) {
    int[] orders = new int[numCourses];
    int counter = 0;

    // 初始化邻接表
    Vertex[] adjacencyList = new Vertex[numCourses];
    for (int i = 0; i < prerequisites.length; i++) {
        int source = prerequisites[i][1];
        int target = prerequisites[i][0];
        if (adjacencyList[source] == null) {
            adjacencyList[source] = new Vertex();
        }
        if (adjacencyList[target] == null) {
            adjacencyList[target] = new Vertex();
        }
        adjacencyList[source].adjacences.add(target);
        adjacencyList[target].inDegree++;
    }

    Queue<Vertex> queue = new LinkedList<>();
    // 遍历，入队列，入度=0的，即源头节点
    for (int i = 0; i < adjacencyList.length; i++) {
        if (adjacencyList[i] == null) {
            continue;
        }
        if (adjacencyList[i].inDegree == 0) {
            orders[counter++] = i;
            queue.offer(adjacencyList[i]);
        }
    }

    // 不断的poll节点出来，与它邻接的入度-1，然后看是不是入度=0，等于0则证明是个源头，放进去队列继续。
    while (!queue.isEmpty()) {
        Vertex vertex = queue.poll();
        for (Integer index : vertex.adjacences) {
            if (--adjacencyList[index].inDegree == 0) {
                orders[counter++] = index;
                queue.add(adjacencyList[index]);
            }
        }
    }

    if (counter != numCourses) {
        throw new RuntimeException("Cyclic graph");
    }

    return orders;
}
```

### [209. Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/)

M, Array Two Pointers Binary Search

方法1：暴力求解，会Time Limit Exceeded
固定某个位置，从这个位置往后找最小的>=s的连续子序列，记录这个长度。遍历完数组，最短的那个长度就是解。
```
int minLen = Integer.MAX_VALUE;
for (int i = 0; i < nums.length; i++)
    int sub = 0;
    for (int j = i; j < nums.length; j++)
        sub += nums[j];
        if (sub >= s)
            minLen = Math.min(minLen, j - i + 1);
            break;
return minLen == Integer.MAX_VALUE ? 0 : minLen;
```

方法2：通用的模板解法。

[参考链接1](https://discuss.leetcode.com/topic/30941/here-is-a-10-line-template-that-can-solve-most-substring-problems/2)

[参考链接2](https://discuss.leetcode.com/topic/37844/o-n-template-for-minimum-size-subarray-sum-minimum-window-substring-longest-substring-without-repeating-characters)

和以下题目的解法模板一样，follow the same pattern！一切都是套路：
* [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
* [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
* [209. Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/)

模板就是：
```
int findSubstring(string s){
    int[] map = new int[256];
    int counter; // check whether the substring is valid
    int begin=0, end=0; //two pointers, one point to tail and one  head
    int d; //the length of substring

    for() { /* initialize the hash map here */ }

    while(end<s.size()){

        if(map[s[end]] ?){  /* modify counter here */ }

        map[s[end]]--
        end++

        while(/* counter condition */){

             /* update d here if finding minimum*/

            //increase begin to make it invalid/valid again

            if(map[s[begin]] ?){ /*modify counter here*/ }

            map[s[begin]]++
            begin++;
        }

        /* update d here if finding maximum*/
    }
    return d;
}
```

这道题目的思想就是滑动窗口的概念，
* 1）start/end固定起点
* 2）固定start，滑动end，维护一个刚好>=s的窗口，记录这个长度是一个解
* 3）把窗口的左边往前移动，维持一个刚好小于s的窗口，因为要给end向前走的可能，继续这个窗口。
（证明这个要想象为什么不会错过start，因为第二步是刚好>=s就停止的了窗口，所以错过的start绝对不可能是起点，因为有他们没有他们都一样）
```
start/end
   2,      3,     1,      2,      4,     3

start                    end
  [2,      3,     1,      2,]     4,     3   是一个解，进入内部第二个循环, minLen=4

        start            end
   2,      3,     1,      2,      4,     3

        start                    end
   2,     [3,     1,      2,      4,]    3  是一个解，进入内部第二个循环,minLen=4

                 start           end
   2,      3,    [1,      2,      4,]    3  minLen=3

                        start    end
   2,      3,     1,      2,      4,     3

                        start           end
   2,      3,     1,     [2,      4,     3] 是一个解，进入内部第二个循环,minLen=3

                                 start   end
   2,      3,     1,      2,     [4,     3]   minLen=2

                                       start/end
   2,      3,     1,      2,      4,     3
```

```
int start = 0, end = 0;
int minLen = Integer.MAX_VALUE;
int sum = 0;
for (; end < nums.length; end++) {
    if (sum < s)   //上一把小于s我就加
        sum += nums[end];

    while (sum >= s) //条件满足正好是刚刚超过，就计算下长度，然后找前一个start位置刚好小于s
        minLen = Math.min(end - start + 1, minLen);
        sum -= nums[start];
        start++;
}
return minLen == Integer.MAX_VALUE ? 0 : minLen;
```


### [208. Implement Trie (Prefix Tree)](https://leetcode.com/problems/implement-trie-prefix-tree/)

M, Design Trie

抓住Trie树，字典树，前缀树的性质：
* 根节点不包含字符，除根节点外每一个节点都只包含一个字符。
* 从根节点到某一节点，路径上经过的字符连接起来，为该节点对应的字符串。
* 每个节点的所有子节点包含的字符都不相同。

基本思想：

1、插入过程：
对于一个单词，从根开始，沿着单词的各个字母所对应的树中的节点分支向下走，直到单词遍历完，将最后的节点标记为红色，表示该单词已插入Trie树。

2、查询过程：
同样的，从根开始按照单词的字母顺序向下遍历trie树，一旦发现某个节点标记不存在或者单词遍历完成而最后的节点未标记为红色，则表示该单词不存在，若最后的节点标记为红色，表示该单词存在。

比较全的一个实现[java-program-implement-trie](http://www.sanfoundry.com/java-program-implement-trie/)

//TODO 应用场景

discuss高分答案：
```
class TrieNode {
    public char val;
    public boolean isWord;
    public TrieNode[] children = new TrieNode[26];
}

public void insert(String word) {
    TrieNode ws = root;
    for(int i = 0; i < word.length(); i++){
        char c = word.charAt(i);
        if(ws.children[c - 'a'] == null){
            ws.children[c - 'a'] = new TrieNode(c);
        }
        ws = ws.children[c - 'a'];
    }
    ws.isWord = true;
}

public boolean search(String word) {
    TrieNode ws = root;
    for(int i = 0; i < word.length(); i++){
        char c = word.charAt(i);
        if(ws.children[c - 'a'] == null) return false;
        ws = ws.children[c - 'a'];
    }
    return ws.isWord;  //startWith直接返回true
}
```

### [207. Course Schedule](https://leetcode.com/problems/course-schedule/)

M,Depth-first Search Breadth-first Search Graph Topological Sort

输入示例：
```
      ------> 2 ----------
     |        |          |  ----------------
     |        \/        \/ |               \/
1 ---         5 -------> 4 --------         6
     |                  /\         |       /|\
     |                   |         \/       |
     ---------------------         3---------
     |                            /\
     ------------------------------
```

以下两个方法参考[discussion](https://discuss.leetcode.com/topic/15762/java-dfs-and-bfs-solution)
* BFS

输入时Edge lists，格式按照{course, pre_course}
```
{2, 1}, {3, 1}, {3, 4}, {4, 1}, {4, 2}, {4, 5}, {5, 2}, {6, 3}, {6, 4}
```

需要转换为邻接矩阵，Adjacency lists，二维数组
```
0 ->
1 -> 2,3,4
2 -> 5,4
3 -> 6
4 -> 3,6
5 -> 4
6 ->
```

保存一个入度in-degree数组：
```
0 = 0
1 = 0
2 = 1
3 = 2
4 = 3
5 = 1
6 = 2
```

代码如下，标准标准教科书般的邻接表，广度优先搜索解法。
```
// 邻接表（二维列表）以及初始化
ArrayList[] graph = new ArrayList[numCourses];
for (int i = 0; i < numCourses; i++) { graph[i] = new ArrayList(); }

// 课程的入度
int[] degree = new int[numCourses];

Queue queue = new LinkedList();
int count = 0;

// 第一步，总之就是先初始化邻接表
for (int i = 0; i < prerequisites.length; i++)
    degree[prerequisites[i][0]]++;  //入度+1
    graph[prerequisites[i][1]].add(prerequisites[i][0]);  //邻接表赋值

// 第二步，找度0的，放入队列
for (int i = 0; i < degree.length; i++)
    if (degree[i] == 0)
        queue.add(i);
        count++;

// 第三步，不断的出队列，把所有邻接的兄弟，也就是依赖于自己的全部度-1，如果发现度=0的，则入队列，否则就有环了
while (queue.size() != 0) {
    int course = (int) queue.poll();
    for (int i = 0; i < graph[course].size(); i++) {
        int next = (int) graph[course].get(i);
        degree[next]--;
        if (degree[next] == 0) {
            queue.add(next);
            count++;
        }
    }
}

return count == numCourses;
```

* DFS
也是要做一个邻接表，不需要in-degree入度表。不断探测，理论上，应该会收殓到所有的终点，如果发现探测过程中有已经访问过的，则证明有了环。

可以参考N-Queens题目的backtracking的模板，实际也是一个DFS的思想。

```
public boolean canFinish(int numCourses, int[][] prerequisites) {
    ArrayList[] graph = new ArrayList[numCourses];
    for (int i = 0; i < numCourses; i++) { graph[i] = new ArrayList();}

    boolean[] visited = new boolean[numCourses];
    for (int i = 0; i < prerequisites.length; i++) {
        graph[prerequisites[i][1]].add(prerequisites[i][0]);
    }

    for (int i = 0; i < numCourses; i++) {
        if (!dfs(graph, visited, i)) {
            return false;
        }
    }
    return true;
}

private boolean dfs(ArrayList[] graph, boolean[] visited, int course) {
    if (visited[course]) {
        return false;
    }
    visited[course] = true;
    for (int i = 0; i < graph[course].size(); i++) {
        if (!dfs(graph, visited, (int) graph[course].get(i))) {
            return false;
        }
    }
    visited[course] = false;
    return true;
}
```

### [206. Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/)

E, Linked List

想象过程，迭代和递归两种写法的思想一致。
```
while(head != null)
Step1: newHead   head
        null      7    ->  4  ->  9  ->  6  ->  3

Step2: newHead   head     next
        null      7    ->  4  ->  9  ->  6  ->  3

Step3: newHead   head     next
        null  <-  7    ->  4  ->  9  ->  6  ->  3

Step4:           newHead  head
        null  <-  7    ->  4  ->  9  ->  6  ->  3


Step1:          newHead   head
        0   <-   7   ->  4  ->  9  ->  6  ->  3

Step2:          newHead  head  next
        0   <-   7   ->  4  ->  9  ->  6  ->  3

Step3:          newHead  head  next
        0   <-   7   <-  4  ->  9  ->  6  ->  3

Step3       :          newHead head
        0   <-   7   <-  4  ->  9  ->  6  ->  3
```

```
//Iterative solution
public static ListNode reverseListIteratively(ListNode head) {
    ListNode newHead = null;
    while (head != null) {
        ListNode next = head.next;
        head.next = newHead;
        newHead = head;
        head = next;
    }
    return newHead;
}

//Recursive solution
public static ListNode reverseListRecursiveLy(ListNode head) {
    return doReverseListRecursiveLy(head, null);
}

public static ListNode doReverseListRecursiveLy(ListNode head, ListNode newHead) {
    if (head == null) {
        return newHead;
    }
    ListNode next = head.next;
    head.next = newHead;
    return doReverseListRecursiveLy(next, head);
}
```

### [204. Count Primes](https://leetcode.com/problems/count-primes/)

E, Hash Table Math

一道经典的数学题目，首先明确质数的概念，除了1和自己外没有别的约数。
2是最小的质数，也是唯一的偶数质数。可以记住下100以内有25个质数。例如下面的都是质数。
```
2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97 101 103 107 109 113 127 131 137 139 149
```

按照常规的解法：
```
for i less than n
    if i is prime
        count++

判断是否是质数的函数如下，这里已经是节省了计算了的，小于等于i的开平方，其实应该是到i的
for (int k = 2; j <= Math.sqrt(n); k++)
    if (n % k == 0)
        isPrime = false;
```

但是这个解法会TLE。

考虑更加好的方法，[参考链接](http://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/)
2 is prime... cross out it's multiples，需要删除掉4，6，8，10...

基本版本，
```
int count = 0;
boolean[] flag = new boolean[n];  // true represents that number is not prime
for (int i = 2; i < n; i++) {
    if (!flag[i]) {
        count++;
        for (int j = i; j < n; j = j + i) {
            flag[j] = true;
        }
    }
}
return count;
```

然后再优化，节约32倍的存储空间。
```
int[] notPrime = new int[n / 32 + 1];  //为了节省空间用bitmap存储，notPrime[0]表示0-31的质数，notPrime[1]表示31-63的质数，然后用位与位或
for (int i = 2; i < n; i++) {
    if (((notPrime[i / 32] >> i % 32) & 1) == 0) // 是质数
        count++;
        for (int j = i; j < n; j = j + i) {   //把当前质不断的*2加倍，存在非质数的数组里面
            notPrime[j / 32] |= 1 << (j % 32);
return count;
```

最后差个话题，求最大公约数GreatestCommonDivisor：
* 分解质因数法
* 短除法，得靠写在纸面上
* 辗转相除法（欧几里德算法），可以实现为算法

下面以辗转相除法演示，当除到reminder余数等于0的时候，那么n就是最大公约数
```
m  / n   =      reminder
64 / 40  = 1 ... 24
40 / 24  = 1 ... 16
24 / 16  = 1 ... 8
16 / 8   = 2 ... 0

m  / n  =      reminder
17 /  8  = 2 ... 1
8  /  1  = 8 ... 0
```

```
int gcd1(int m, int n) {   //方法一：循环法
    int temp;
    while (m % n != 0)
        temp = n;
        n = m % n;
        m = temp;
    return n;

int gcd2(int m, int n)     //方法二：递归法
    if (m % n == 0)
        return n;
    else
        return gcd2(n, m % n);
```

求最小公倍数LowestCommonMultiple

* 分解质因数法
* 短除法
* 公式法，两个数相乘除以最大公约数
```
int lcm(int m, int n) {
    return m * n / gcd.gcd1(m, n);
}
```

### [202. Happy Number](https://leetcode.com/problems/happy-number/)

E, Hash Table Math

```
input 19
1^2 + 9^2 = 82
8^2 + 2^2 = 68
6^2 + 8^2 = 100
1^2 + 0^2 + 0^2 = 1 //结果为1就是一个happy number
```

如果出现循环则证明不是一个happy number需要及时推出，理所当然的想到了使用set。

根据JDK的HashSet#add的javadoc，如果已经存在了元素，则返回false。
```
Adds the specified element to this set if it is not already present.
More formally, adds the specified element e to this set if this set contains no element e2 such that (e==null ? e2==null : e.equals(e2)).
If this set already contains the element, the call leaves the set unchanged and returns false.
```

```
Set<Integer> inLoop = new HashSet<Integer>();
while (inLoop.add(n))  //还能加，没重复
    while (n > 0)
        remain = n % 10;
        squareSum += remain * remain;
        n /= 10;
    if (squareSum == 1) {  //退出
        return true;
    else
        n = squareSum;  //继续走下一个数
return false;
```

### [200. Number of Islands](https://leetcode.com/problems/number-of-islands/)

M, Depth-first Search Breadth-first Search Union Find

```
Example 1:

11110
11010
11000
00000
Answer: 1

Example 2:

11000
11000
00100
00011
Answer: 3
```

方法1：并查集

```
//验证逻辑同dfs
UF uf = new UF(rows * cols);
for (int i = 0; i < rows; i++)
    for (int j = 0; j < cols; j++)
        if (grid[i][j] == '1')
            tryUnion(i, j, i + 1, j, rows, cols, uf, grid);
            tryUnion(i, j, i - 1, j, rows, cols, uf, grid);
            tryUnion(i, j, i, j + 1, rows, cols, uf, grid);
            tryUnion(i, j, i, j - 1, rows, cols, uf, grid);

Set<Integer> res = new HashSet<>();
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++)
        if (grid[i][j] == '1')
            res.add(uf.find(i * cols + j));  //find unique 1

return res.size();

// 这就是模板，tryUnion精妙！
void tryUnion(int i, int j, int x, int y, int rows, int cols, UF uf, char[][] grid) {
    if (i < 0 || i >= rows || j < 0 || j >= cols ||
        x < 0 || x >= rows || y < 0 || y >= cols)
        return;
    if (grid[x][y] == '0')
        return;
    uf.union(i * cols + j, x * cols + y);
}

// 同128. Longest Consecutive Sequence一样的weighted-quick-union并查集
class UF {
    int s[];
    int w[];
    int count;

    public UF(int size) {
        s = new int[size];
        w = new int[size];
        for (int i = 0; i < size; i++) {
            s[i] = i;
            w[i] = 1;
        }
        count = size;
    }

    int find(int p) {
        while (p != s[p]) {
            p = s[p];
        }
        return p;
    }

    void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;
        }
        if (w[rootP] < w[rootQ]) {
            s[rootP] = rootQ;
            w[rootQ] += w[rootP];
        } else {
            s[rootQ] = rootP;
            w[rootP] += w[rootQ];
        }
        count--;
    }
}
```


方法2：DFS解法如下，其实也是backtrack：
```
public int numIslands(char[][] grid) {
    if (grid == null) return 0;
    int count = 0;
    int rows = grid.length;
    if (rows == 0) return 0; //必须有
    int cols = grid[0].length;
    boolean[][] visited = new boolean[rows][cols];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (grid[i][j] == '1' && !visited[i][j])
                dfs(grid, visited, i, j, rows, cols);
                count++;
    return count;
}

void dfs(char[][] grid, boolean[][] visited, int i, int j, int rows, int cols)
    if (i < 0 || j < 0 || i >= rows || j >= cols || grid[i][j] == '0' || visited[i][j])
        return;
    visited[i][j] = true;
    dfs(grid, visited, i + 1, j, rows, cols);
    dfs(grid, visited, i, j + 1, rows, cols);
    dfs(grid, visited, i - 1, j, rows, cols);
    dfs(grid, visited, i, j - 1, rows, cols);
}
```


### [199. Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/)

M, Tree Depth-first Search Breadth-first Search

仿佛站在右边能看到的节点list。还是DFS的backtrack。可以看做是preorder的travers，只不过是反着来的，从右往左，而且有条件的添加元素到res中。
```
   1            <---
 /   \
2     3         <---
 \     \
  5     4       <---
```

```
public List<Integer> rightSideView(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    dfs(root, result, 0);
    return result;
}

void dfs(TreeNode root, List<Integer> result, int depth) {
    if (root == null) {
        return;
    } else {
        if (result.size() == depth) {  //太巧妙了！！
            result.add(root.val);
        }
        dfs(root.right, result, depth + 1);//先右边
        dfs(root.left, result, depth + 1);
    }
}
```

### [198. House Robber](https://leetcode.com/problems/house-robber/)

E, Dynamic Programming

两个房子同时rob就会触发报警，如何获取最大收益？

动态规划，设置maxV[i]表示到第i个房子位置，最大收益。

递推关系为maxV[i] = max(maxV[i-2]+num[i], maxV[i-1])

```
int[] dp = new int[nums.length];
dp[0] = nums[0];
dp[1] = Math.max(nums[0], nums[1]);
for (int i = 2; i < nums.length; i++) {
    dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
}

return dp[nums.length - 1];

另外一种不太好理解，但是和上面的思想是一样的。
int notTake = 0;
int take = 0;
int max = 0;
for (int i = 0; i < nums.length; i++)
    take = notTake + nums[i];
    notTake = max;
    max = Math.max(take, notTake);
return max;
```

示例：
```
1 5 3 2 6
1 5 5 7 11
  *     *

偷星星的店收益最大
```


### [191. Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/)

E, Bit Manipulation

题目要求是无符号整形，因此Java中要注意处理Integer.MAX_VALUE和Integer.MIN_VALUE做检查。

Integer.MAX_VALUE + 1 = Integer.MIN_VALUE

* hamming weight 汉明重量是字符串相对于同样长度的零字符串的汉明距离
* hamming distance 汉明距离就是将一个字符串替换成另外一个字符串所需要替换的字符长度

```
//方法1：n不动，flag依次和低位到高位（共32）个做“与”，转成2的幂，看是否不等于0，不能是>0因为，0x80000000是个负数。
public int hammingWeight(int n) {
    int flag = 1;
    int res = 0;
    for (int i = 0; i < 32; i++) {
        if ((n & (flag << i)) != 0) {
            res++;
        }
    }
    return res;
}

//方法2：n从高位往低位滑动，此时要用无符号右移，否则会死循环。
public int hammingWeight2(int n) {
    int res = 0;
    while (n != 0) {
        if ((n & 1) == 1) {
            res++;
        }
        n = n >>> 1;  //>>退出不了循环
    }
    return res;
}

// 也可以写成
for (int i = 0; i < 32; i++) {
    if (((n >>> (31 - i)) & 1) == 1) {
        res++;
    }
}

//方法3：bit位操作
while (n != 0) {
    n = n & (n - 1)
    count++;
return count;
```

### [190. Reverse Bits](https://leetcode.com/problems/reverse-bits/)

E, Bit Manipulation

```
00000010100101000001111010011100
as
00111001011110000010100101000000
```
想清楚就行，都通通挪到最低位，然后和1与，然后左移动到合适的位置，+=也可以是|=。这里使用的无符号右移。
```
for (int i = 0; i < 32; i++)
    res += ((n >>> (31 - i)) & 1) << i;
```

### [189. Rotate Array](https://leetcode.com/problems/rotate-array/)

E, Array

注意不管怎样都需要先**k %= len**

方法1：时间O(N)，空间O(N)

新建一个int[len]
```
k = 3
index:
[0,1,2,3,4,5,6]

nums:
[1,2,3,4,5,6,7]

result:
[5,6,7,1,2,3,4]

找规律：
result[i] = nums[(i + k)%len]

result[3] = nums[0]  3%7
result[4] = nums[1]  4%7
result[5] = nums[2]  5%7
result[6] = nums[3]  6%7
result[0] = nums[4]  7%7
result[1] = nums[5]  8%7
result[2] = nums[6]  9%7
```

方法2：时间O(N)，空间O(1)

in-place结合reverse函数，先reverse 0到len-k，再reverse后面的k个，然后整体reverse下。
```
k=4
step1: [1,2,3,4,5,6,7]
step2: [3,2,1,4,5,6,7]  reverse(nums, 0, nums.length - k - 1)
step3: [3,2,1,7,6,5,4]  reverse(nums, nums.length - k, nums.length - 1)
step4: [4,5,6,7,1,2,3]  reverse(0, nums.length - 1)
```

### [187. Repeated DNA Sequences](https://leetcode.com/problems/repeated-dna-sequences/)

M, Hash Table Bit Manipulation

伪代码如下，关键在于获取10个字符的签名算法，由于都是由字母构成，所以最大是26，小于1<<5，也就是需要6个bit的空间，6*10<64也就是long的存储空间，因此用一个long可以存储下来签名。
```
for (int i = 0; i <= str.length - 10; i++)
    get sign of str[i..i+10]
    if exist in hashmap than incr count  //注意使用getOrDefault
        if count > 1 then add to a set for distinction
    else put (sign, count=1) to the map

long getSign(char[] str, int start)
    long sign = 0;
    for (int i = start; i < start + 10; i++) {
        sign |= (str[i] - 'A');
        sign <<= 6;
    }
    return sign;
```

### [179. Largest Number](https://leetcode.com/problems/largest-number/)

M, Array

一开始的想法就是排序，大思路首先正确，就是需要实现自己的排序策略。可以使用各种排序算法，比如选择、快排等。
但是排序的策略，应该利用int转为str来比较的思路。
一直陷入，两个int，应该如何从高位比较到低位，当时的想法是比较高位，直到发现不同的位，则比较不同的位。 例如，384 > 3125 但是这里有一个坑，就是384 > 3842，因为 1）有公共最长前缀384， 2）公共最长前缀已经到了末尾 3）另外一个数字末尾这个2比首位3小 这三个条件极其难写算法去实现，会有很多边界条件和额外的计算量。 因此不要陷入数字计算的陷阱里，而是想着用字符串比较或者使用jdk的函数式编程思想（例如largestNumber2(int[])）

```
String[] array = Arrays.stream(num).mapToObj(String::valueOf).toArray(String[]::new);
Arrays.sort(array, (String s1, String s2) -> (s2 + s1).compareTo(s1 + s2));
return Arrays.stream(array).reduce((x, y) -> x.equals("0") ? y : x + y).get();
//Arrays.stream(array).reduce("", (x, y) -> x.equals("0") ? y : x + y);
```

传统的写法如下：
```
if (nums == null || nums.length == 0)
    return "";
String[] numsStr = new String[nums.length];
for (int i = 0; i < nums.length; i++)
    numsStr[i] = nums[i] + "";
Arrays.sort(numsStr, (o1, o2) -> (o2 + o1).compareTo(o1 + o2));
StringBuilder sb = new StringBuilder();
for (String str : numsStr)
    sb.append(str);
if (sb.charAt(0) == '0') //corner case，解决[0,0]的问题
    return "0";
return sb.toString();
```

注意这里的比较，compareTo的javadoc写的很清楚：Compares two strings lexicographically.
```
String[] tests = {"34", "3"};
// 343 > 334 因此compareTo返回1，那么就说明s1比s2大，也就是34>3，因此排序后数组是[3, 34]，但是实际我们要的是34，3，这样组成的数字更大，所以先比较s2+s1
Arrays.sort(tests, (String s1, String s2) -> (s1 + s2).compareTo(s2 + s1));
```

### [173. Binary Search Tree Iterator](https://leetcode.com/problems/binary-search-tree-iterator/)

M, Tree Stack Design

实现一个Tree的iterator模式。

又是一个标准的in-order遍历模板。
```
private Stack<TreeNode> stack;
private TreeNode curr;
public BSTIterator(TreeNode root) {
    stack = new Stack<>();
    curr = root;
}
public boolean hasNext() {
    return curr != null || !stack.isEmpty();
}
public int next() {
    while (curr != null || !stack.isEmpty()) {
        if (curr != null) {
            stack.push(curr);
            curr = curr.left;
        } else {
            TreeNode node = stack.pop();
            //... visit
            curr = node.right;
            return node.val;
        }
    }
    throw new RuntimeException("no next left");
}
```



### [171. Excel Sheet Column Number](https://leetcode.com/problems/excel-sheet-column-number/)

E, Math

相似题目[168. Excel Sheet Column Title](https://leetcode.com/problems/excel-sheet-column-title/)

```
int len = s.length();
int res = 0;
for (int i = 0; i < len; i++)
    res = res * 26 + (s.charAt(i) - 'A') + 1;
return res;
```


### [169. Majority Element](https://leetcode.com/problems/majority-element/)

E, Array Divide and Conquer Bit Manipulation

方法1：排序
```
Arrays.sort(nums); nums[nums.length / 2]是结果
```

方法2：哈希表，但是太浪费空间了O(N)

方法3：Moore voting algorithm
```
int count = 0, ret = 0;
for (int num : nums) {
    if (count == 0) {
        ret = num;
    }
    if (num != ret) {
        count--;
    } else {
        count++;
    }
}
return ret;
```

方法4：Bit manipulation
```
int[] bit = new int[32];
for (int num : nums) {
    for (int i = 0; i < 32; i++) {
        if ((num >> (31 - i) & 1) == 1) {
            bit[i]++;
        }
    }
}
int ret = 0;
for (int i = 0; i < 32; i++) {
    bit[i] = bit[i] > nums.length / 2 ? 1 : 0;
    ret += bit[i] * (1 << (31 - i));
}
return ret;
```

### [168. Excel Sheet Column Title](https://leetcode.com/problems/excel-sheet-column-title/)

E, Math

相似题目[171. Excel Sheet Column Number](https://leetcode.com/problems/excel-sheet-column-number/)

思路和[60-permutation-sequence](README.md#60-permutation-sequence)
的一样，其实就是根据base做进制的转换，参考通用的模板，一般都是用低位处理（取低位用%），然后处理高位（用除法或者>>>）

```
StringBuilder result = new StringBuilder();
while(n > 0)
    n--;  //这里很重要，否则需要处理小于26的情况，这里做test cases要考虑三种情况1）小于1，2）[1,26]，3）大于26
    result.insert(0, (char)('A' + n % 26));
    n /= 26;
return result.toString();

assertThat(convertToTitle(1), Matchers.is("A"));
assertThat(convertToTitle(26), Matchers.is("Z"));
assertThat(convertToTitle(52), Matchers.is("AZ"));
assertThat(convertToTitle(53), Matchers.is("BA"));
assertThat(convertToTitle(54), Matchers.is("BB"));
```

### [154. Find Minimum in Rotated Sorted Array II](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/)

H, Array Binary Search

见题目153，修改mid和end相等情况，end--即可。

### [153. Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/)

M, Array Binary Search

利用另外一种非递归的二分查找，start<end，中间做文章，判断mid和end，如果mid大于end，说明
最小的肯定还在最右边，start=mid+1即可，如果小于等于则可能说明这个元素在左边，并且很有可能就是mid。


```
int start = 0;
int end = nums.length - 1;
while (start < end) {
    int mid = (start + end) >> 1;
    if (nums[mid] > nums[end]) {
        start = mid + 1;
    } else if (nums[mid] <= nums[end]) {  //这个等于放到上面也可以，单独拿出来做end--就可以避免重复
        end = mid;
    }
}
return nums[start];
```

### [152. Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/)

M, Array Dynamic Programming

最大连续子数组的乘积

任何时候都可能会创造新的最大和最小，比如-2*-3直接就是6了，反而非常大，所以不能漏掉。

max * newnumber, min*newnumber, newnumber这三个肯定会出现max和min就得判断下。

```
-2 3 -4 5 -6

max min res
-2  -2  -2
3   -6  3    (3 > -2> -2*3)
24  -12 24  (-6*-4 > -4 > 3*-4)
120 -60 120  (24*5 > 5 > -12*5)
360  -720 360   (-6*-60 > -6 > 120 * -6)
```

```
int res = nums[0];
int max = nums[0];
int min = nums[0];
for (int i = 1; i < nums.length; i++)
    int tempMax = max;  //留存
    max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
    min = Math.min(Math.min(tempMax * nums[i], min * nums[i]), nums[i]);
    res = Math.max(max, res);

return res;
```

### [151. Reverse Words in a String](https://leetcode.com/problems/reverse-words-in-a-string/)

M, String

利用reverse string 344题的方法，步骤如下：
```
* 1. trim leading, trailing spaces
* 2. handle s.length == 0
* 3. trim inner multiple spaces
    // barrier - 1 is the trimmed ending index
    // like 316题 RemoveDuplicatesFromSortedArray 中间空格的办法
    int barrier = 1;
    for (int i = 1; i < str.length; i++)
        if (str[i] == ' ' && str[i] == str[i - 1])
            continue;
        else
            str[barrier++] = str[i];
* 4. reverse(str, 0, barrier - 1);
* 5. reverse each word
* 6. reverse the last word
* 7. return new String(str, 0, barrier);
```

### [147. Insertion Sort List](https://leetcode.com/problems/insertion-sort-list/)

M, Linked List Sort

想清楚了，举几个例子做，这种list的操作绝对可以一次性AC，dummy节点太有用了！

```
if (head == null || head.next == null)
    return head;
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode pre = head;
ListNode curr = head.next;
while (curr != null) {
    ListNode nextAfterCurr = curr.next;
    ListNode ins = dummy;
    while (ins.next.val < curr.val) {
        ins = ins.next;
    }
    if (ins.next == curr) {
        pre = curr;
    } else {
        pre.next = curr.next;
        curr.next = ins.next;
        ins.next = curr;
    }
    curr = nextAfterCurr;
}
return dummy.next;
```

另外附上基本的三种排序算法，

* 冒泡排序

连Obama都说过最慢的。。。

效率最低的排序算法，连Obama都说这是worst solution。每一趟排序都做交换，把大的（或者小的）值往后排，这样经过一趟排序后，最大的值就跟泡泡一样浮到了最后面，经过n-i趟后，最后的i个元素是排序好的。
```
for (int i = 0; i < array.length; i++) {
    for (int j = 0; j < array.length - i - 1; j++) {
        if (array[j] > array[j + 1]) { // 把大的值交换到后面
            swap(j, j + 1, array);
        }
    }
}
```

* 选择排序

n趟遍历，每一趟都往后找，找一个最小的元素，和这一趟的起点元素交换， 这样n-i趟后，就会有前i个元素就是排序好的，本例中始终找最小的。

```
for (int i = 0; i < array.length - 1; i++) {
    int min = i;
    for (int j = i + 1; j < array.length; j++) {
        if (array[j] < array[min]) {
            min = j;
        }
    }
    if (i != min) {
        swap(i, min, array);
    }
}
```

* 插入排序

每一趟都重新确认一个起点，暂时叫做所谓的“新人”，新人初来乍到，总得找个安身之所， 所以就和前面已经按照顺序就坐的“老人”们比较，依次往前比，大的依次靠后“挪动”，直到这个新来的 刚刚大于前面的兄弟，就是他的座次了，“插入”到这里即可。

这里注意：不断的重复这个过程，越往后挪动的兄弟可能越多，就会越慢了

```
for (int i = 1; i < array.length; i++) {
    int currentValue = array[i];
    int position = i;
    for (int j = i - 1; j >= 0; j--) {
        if (array[j] > currentValue) {
            array[j + 1] = array[j];
            position -= 1;
        } else {
            break;
        }
    }

    array[position] = currentValue;
}
```



### [146. LRU Cache](https://leetcode.com/problems/lru-cache/)

H, Design

一次性写的框架差不多，有一些边界条件还需要处理好。

重要的数据结构：
* Map<Integer, Node> map;
* Node包括key，value，prev，next
* put方法先检查是否存在key，存在则moveAhead，需要考虑head和tail的情况；如果不存在，则put map，更新size，
如果是第一次插入head和tail相连，其他情况则new node作为head，然后检查size，是否需要deleteLRU。
* get方法需要moveAhead。

```
public class LRUCache {
    Node head = null;
    Node tail = null;
    int capacity = 0;
    Map<Integer, Node> map;
    int size = 0;

    public LRUCache(int capacity) {
        // assume capacity > 0
        this.capacity = capacity;
        map = new HashMap<>(capacity / 3 * 4);
    }

    public int get(int key) {
        Node target = map.get(key);
        if (target == null)
            return -1;
        moveAhead(target);
        return target.value;
    }

    public void set(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            moveAhead(node);
        } else {
            Node node = new Node(key, value);
            map.put(key, node);
            size++;
            if (head == null && tail == null) {
                head = node;
                tail = node;
                tail.next = head;
                head.prev = tail;
                head.next = tail;
                tail.prev = head;
            } else {
                node.prev = tail;
                node.next = head;
                tail.next = node;
                head.prev = node;
                head = node;
                if (size > capacity)
                    deleteLRU();
            }
        }
    }

    void moveAhead(Node target) {
        if (target == head)
            return;
        if (tail == target)
            tail = target.prev;
             head = target;
             return;
        target.prev.next = target.next;
        target.next.prev = target.prev;
        tail.next = target;
        target.prev = tail;
        head.prev = target;
        target.next = head;
        head = target;
    }

    void deleteLRU() {
        Node t = tail;
        t.prev.next = head;
        head.prev = t.prev;
        tail = t.prev;
        size--;
        map.remove(t.key);
    }

    class Node {
        int key;
        int value;
        Node prev;
        Node next;
    }
}
```

### [145. Binary Tree Postorder Traversal](https://leetcode.com/problems/binary-tree-postorder-traversal/)

H, Tree Stack

Recursively:
```
public List<Integer> traverse(TreeNode root)
    List<Integer> result = new ArrayList<>();
    doPostorder(root, result);
    return result;

private void doPostorder(TreeNode root, List<Integer> result)
    if (root != null)
        doPostorder(root.left, result);
        doPostorder(root.right, result);
        result.add(root.val);
```

Iteratively:
非常的巧妙，leetcode上排名靠前的都是这个解法，考虑使用一个链表，先安排最后的节点，也就是根和右节点.
```
LinkedList<Integer> result = new LinkedList<>();
Stack<TreeNode> stack = new Stack<>();
if (root == null) { // 必须处理
    return result;
}

stack.push(root);
while (!stack.isEmpty()) {
    TreeNode cur = stack.pop();
    result.addFirst(cur.val);
    if (cur.left != null) {
        stack.push(cur.left);
    }
    if (cur.right != null) {
        stack.push(cur.right);
    }
}
return result;
```

### [144. Binary Tree Preorder Traversal](https://leetcode.com/problems/binary-tree-preorder-traversal/)

M, Tree Stack

Recursively:
```
public List<Integer> traverse(TreeNode root)
    List<Integer> result = new ArrayList<>();
    doPreorder(root, result);
    return result;

private void doPreorder(TreeNode root, List<Integer> result)
    if (root != null)
        result.add(root.val);
        doPreorder(root.left, result);
        doPreorder(root.right, result);
```

Iteratively:
```
List<Integer> result = new ArrayList<>();
Stack<TreeNode> stack = new Stack<>();
stack.push(root);
while (!stack.isEmpty()) {
    TreeNode top = stack.pop();
    if (top != null) { // 要判空
        result.add(top.val);
        stack.push(top.right);
        stack.push(top.left);
    }
}
return result;
```

### [142. Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/)

E,  Linked list Two Pointers

快（走两步）、慢指针（走一步）的方式，总会相遇。

X是runner（快指针）和walker（慢指针）相遇的点，设
* 1）从head到cycle起点的距离是A
* 2）cycle起点到X的举例是B，
* 3）环的长度是N 那么根据快慢指针的追的特性可以得出：

 A+B+N = 2A+2B

 那么可以推出：

 N=A+B

所以从X点再走A步，也相当于从head走A步，肯定相遇的点就是cycle的起点。
```
               B
                 o->o->X->o
                 |        |
                 o        o
      A          |        |
o->o->o->o->o->o->o        o
                 |        | A
                 |        |
                 o<-o<-o<-o
```

相比于题目141，在runner==walker相遇后，继续走相遇的点肯定是cycle的起点。
```
ListNode slow = walker;
while (head != slow) {
    slow = slow.next;
    head = head.next;
}
return slow;
```

### [141. Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/)

E,  Linked list Two Pointers

快（走两步）、慢指针（走一步）的方式，总会相遇。

```
if (head == null) return false;
ListNode walker = head;
ListNode runner = head;
while (runner.next != null && runner.next.next != null) {
    walker = walker.next;
    runner = runner.next.next;
    if (walker == runner)
        return true;
}
return false;
```

### [140. Word Break II](https://leetcode.com/problems/word-break-ii/)

H, Dynamic Programming Backtracking

是139题的延续，这次要输出所有的断句可能。还是分两种方法，DFS和DP。

方法1，DP。

利用139题的DP解法，在断句的地方记录一个StepWrapper数组，里面含有可以到这个位置的所有的前面的“跳跃点”。

利用回溯，实际就是一个组合combination的过程，找出所有的可能。

```
public List<String> wordBreak(String s, Set<String> wordDict) {
    boolean[] dp = new boolean[s.length() + 1];
    StepWrapper[] record = new StepWrapper[s.length() + 1];
    dp[0] = true;
    for (int i = 0; i < s.length(); i++) {
        if (!dp[i]) {
            continue;
        }
        for (String word : wordDict) {
            int end = i + word.length();
            if (end <= s.length() && s.substring(i, end).equals(word)) {
                dp[end] = true;
                if (record[end] == null) {
                    record[end] = new StepWrapper();
                }
                record[end].steps.add(new Step(record[i], word, i, end));
            }
        }
    }
    if (!dp[s.length()]) {
        return Collections.emptyList();
    }
    List<String> res = new ArrayList<>();
    backtrack(record[s.length()], res, new ArrayList<>());
    return res;
}

void backtrack(StepWrapper s, List<String> res, List<String> temp) {
    if (s == null) {
        List<String> copy = new ArrayList<>(temp);
        Collections.reverse(copy);
        res.add(copy.stream().collect(Collectors.joining(" ")));
    } else {
        for (Step step : s.steps) {
            temp.add(step.str);
            backtrack(step.prev, res, temp);
            temp.remove(temp.size() - 1);
        }
    }
}

class StepWrapper {
    List<Step> steps = new ArrayList<>();
}
class Step {
    StepWrapper prev;
    String str;
    int start;
    int end;
}
```

方法2，DFS。

```
public List<String> wordBreakDFS(String s, Set<String> wordDict) {
    return DFS(s, wordDict, new HashMap<String, LinkedList<String>>());
}

List<String> DFS(String s, Set<String> wordDict, HashMap<String, LinkedList<String>> map) {
    if (map.containsKey(s))
        return map.get(s);

    LinkedList<String> res = new LinkedList<>();
    if (s.length() == 0) {
        res.add("");
        return res;
    }
    for (String word : wordDict) {
        if (s.startsWith(word)) {
            List<String> sublist = DFS(s.substring(word.length()), wordDict, map);
            for (String sub : sublist) {
                res.add(word + (sub.isEmpty() ? "" : " ") + sub);
            }
        }
    }
    map.put(s, res);
    return res;
}
```

### [139. Word Break](https://leetcode.com/problems/word-break/)

判断是否能从set字典里找出一些单词，把给定的字符串给断开。例如，下面就是一个可以的例子：
```
s = "leetcode",
dict = ["leet", "code"].
```

[参考链接](http://www.programcreek.com/2012/12/leetcode-solution-word-break/)

方法1，DFS暴力破解。TLE。

```
public boolean wordBreak(String s, Set<String> wordDict) {
    return dfs(s, wordDict, 0);
}

boolean dfs(String s, Set<String> wordDict, int start) {
    if (start == s.length()) {
        return true;
    }
    for (String word : wordDict) {
        int end = start + word.length();
        if (end <= s.length() && s.substring(start, end).equals(word)) {
            if (dfs(s, wordDict, end)) {
                return true;
            }
        }
    }
    return false;
}
```

方法2，DP。

这是最快的beat 78%.
```
DP递推式：
boolean[] dp;
dp[i+1] = dp[j] && s.substring(j, i+1) is in dict, j in [0, i]
Time: O(string length * dict size).
```

```
boolean[] dp = new boolean[s.length() + 1];
dp[0] = true;
for (int i = 0; i < s.length(); i++) {
    if (!dp[i]) {
        continue;
    }
    for (String word : wordDict) {
        int end = i + word.length();
        if (end <= s.length() && s.substring(i, end).equals(word)) {
            dp[end] = true;
        }
    }
}
return dp[s.length()];
```


### [137. Single Number II](https://leetcode.com/problems/single-number-ii/)

M, Bit Manipulation

和136题的模板一样，针对这种题目*一个数组，里面每一个元素都出现了N次，只有一个是一次，求这一个*。这个N就是用来去模的。

另外[260. Single Number III](https://leetcode.com/problems/single-number-iii/)将仅出现的次数变成了两次，题目又难了一些。注意这个题目可以使用如下的JAVA API：
```
diff = Integer.highestOneBit(diff);  //求最近的Power of 2
Arrays.fill(result,0);
```

### [136. Single Number](https://leetcode.com/problems/single-number/)

M, Hash Table Bit Manipulation

```
int[] digits = new int[32];
for (int i = 0; i < 32; i++)
    for (int num : nums)
        digits[i] += (num >> i) & 1;
    digits[i] %= 2;

int res = 0;
for (int i = 0; i < 32; i++)
    res += digits[i] << i;

return res;
```
还有一种更加巧妙的方式，利用XOR的性质，XOR自己等于0，XOR 0等于自己。相当于消除。

```
int res = nums[0];
for (int i = 1; i < nums.length; i++)
    res ^= nums[i];
return res;
```

### [127. Word Ladder](https://leetcode.com/problems/word-ladder/)

M, Graph

经典的词梯。

```
public int ladderLength(String beginWord, String endWord, Set<String> wordList) {
    if (beginWord.endsWith(endWord)) { return 0; }
    wordList.add(endWord);
    Set<String> visited = new HashSet<>();
    Map<String, Vertex> map = new HashMap<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(beginWord);
    visited.add(beginWord);
    map.put(beginWord, new Vertex(beginWord));
    while (!queue.isEmpty()) {
        String currWord = queue.poll();
        char[] chs = currWord.toCharArray();
        for (int i = 0; i < currWord.length(); i++) {
            from 'a' to 'z' change current char to newWord
            if (!visited.contains(newWord) && wordList.contains(newWord)) {
                queue.add(newWord);
                Vertex v = new Vertex(newWord);
                v.prev = map.get(currWord);
                map.put(newWord, v);
                visited.add(newWord);
            }
            change back to currWord
        }
    }
    if (!map.containsKey(endWord))
        return 0;  //找不到路径
    //从end往前找prev计数并且return
}

class Vertex {
    String word;
    Vertex prev;
    int dist = Integer.MAX_VALUE;
}
```

### [134. Gas Station](https://leetcode.com/problems/gas-station/)

M, Greedy

先检查总加油和总消耗能不能满足跑完一圈。
```
15,5    180,20
A <---- E <--------- D 1,2
|                    |
|                    |
|                    |
------>B ----------->C
     5,10         20,100

```
从A开始走，可以走到C就不行就，这时候start可以是D，不用再从B返回走了，因为既然从A都能空邮箱走过来， 那么肯定A之后的E,D是有可能的起点，因为要过C，必须要有足够的储备。

```
int tank = 0;
for (int i = 0; i < gas.length; i++) tank += gas[i] - cost[i];
if (tank < 0) { return -1; } //检查总续航和总消耗是否满足要求

int start = 0; //然后在有解的基础上找起点。
int accumulate = 0;
for (int i = 0; i < gas.length; i++)
    int curGain = gas[i] - cost[i];
    if (accumulate + curGain < 0)
        start = i + 1;
        accumulate = 0;
    else
        accumulate += curGain;
return start;
```

### [130. Surrounded Regions](https://leetcode.com/problems/surrounded-regions/)

M, Breadth-first Search Union Find

```
For example,
X X X X
X O O X
X X O X
X O X X
After running your function, the board should be:

X X X X
X X X X
X X X X
X O X X
```

方法1：DFS
从边界找O然后，把边界内的O标记为*，然后里面的O自然就都变为X。如果从里面找就会TLE。
```
public void solve2(char[][] board)
    if (board == null || board.length == 0) {return;}
    for (int i = 0; i < board.length; i++)
        for (int j = 0; j < board[0].length; j++)
            if (i == 0 || i == board.length - 1 || j == 0 || j == board[0].length - 1)
                if (board[i][j] == 'O')
                    dfs(i, j, board);

    for (int i = 0; i < board.length; i++)
        for (int j = 0; j < board[0].length; j++)
            if (board[i][j] == '*') {
                board[i][j] = 'O';
            else
                board[i][j] = 'X';
    return;

private void dfs(int i, int j, char[][] board)
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length)
        return;
    if (board[i][j] == 'X' || board[i][j] == '*')
        return;
    board[i][j] = '*';
    if (i + 1 < board.length) dfs(i + 1, j, board);
    if (i - 1 > 0) dfs(i - 1, j, board);
    if (j + 1 < board[0].length) dfs(i, j + 1, board);
    if (j - 1 > 0) dfs(i, j - 1, board);
```

方法2：另外使用BFS就得用Queue,把肯定是边界O的标记为B。
```
if (board == null || board.length == 0) return;
int rows = board.length, columns = board[0].length;
int[][] direction = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < columns; j++)
        if ((i == 0 || i == rows - 1 || j == 0 || j == columns - 1) && board[i][j] == 'O')
            Queue<Point> queue = new LinkedList<>();
            board[i][j] = 'B';
            queue.offer(new Point(i, j));
            while (!queue.isEmpty()) {
                Point point = queue.poll();
                for (int k = 0; k < 4; k++) {
                    int x = direction[k][0] + point.x;
                    int y = direction[k][1] + point.y;
                    if (x >= 0 && x < rows && y >= 0 && y < columns && board[x][y] == 'O') {
                        board[x][y] = 'B';
                        queue.offer(new Point(x, y));
for (int i = 0; i < rows; i++)
    for (int j = 0; j < columns; j++)
        if (board[i][j] == 'B')
            board[i][j] = 'O';
        else if (board[i][j] == 'O')
            board[i][j] = 'X';
```

方法3：并查集
```
UF uf = new UF(m * n);
for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
        tryUnion(i, j, i - 1, j, m, n, board, uf);
        tryUnion(i, j, i + 1, j, m, n, board, uf);
        tryUnion(i, j, i, j - 1, m, n, board, uf);
        tryUnion(i, j, i, j + 1, m, n, board, uf);

Set<Integer> out = new HashSet<Integer>();
for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++) {
        if (i == 0 || j == 0 || i == m - 1 || j == n - 1)
            if (board[i][j] == 'O') {
                out.add(uf.find(i * n + j));  //把边界的O全找出来

for (int i = 1; i < m - 1; i++) {
    for (int j = 1; j < n - 1; j++) {
        if (!out.contains(uf.find(i * n + j)))    //里面不跟边界有交集的就置为X
            board[i][j] = 'X';


void tryUnion(int i, int j, int x, int y, int m, int n, char[][] board, UF uf) {
    if (x < 0 || x >= m || y < 0 || y >= n)
        return;
    if (board[x][y] == board[i][j]) {
        uf.union(x * n + y, i * n + j);
    }
}

可以参考题目200 Number of Islands的weigted-quick-union的并查集。
```


### [129. Sum Root to Leaf Numbers](https://leetcode.com/problems/sum-root-to-leaf-numbers/)

M,Tree Depth-first Search

直接就是一个NQueens经典的回溯DFS。

```
public int sumNumbers(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    helper(root, new ArrayList<>(), result);
    return result.stream().reduce(0, (a, b) -> a + b);
}

void helper(TreeNode root, List<Integer> tempList, List<Integer> result) {
    if (root == null) {
        return;
    } else if (root.left == null && root.right == null) {
        tempList.add(root.val);
        result.add(toInt(tempList));
        tempList.remove(tempList.size() - 1);
    } else {
        tempList.add(root.val);
        helper(root.left, tempList, result);
        helper(root.right, tempList, result);
        tempList.remove(tempList.size() - 1);
    }
}
```

### [128. Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/)

H, Array Union Find

这和动态规划的两个LCS问题（Longest Common Subsequence不连续）和（Longest Common Substring连续）不是一个LCS问题。

另外和[300. Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)
也不一样，因为这个要求连续，300题只要求是递增的。因此并查集并不行。

方法1：排序后遍历，lc可以接受，O(NlogN)
```
if (nums == null || nums.length == 0)
    return 0;
if (nums.length == 1)
    return 1;
Arrays.sort(nums);  // use Arrays, not Collections
int maxLen = 1;
int len = 1;
for (int i = 1; i < nums.length; i++)
    if (nums[i] == nums[i - 1]) {  // handle duplications like 0,1,1,2
        continue;
    } else if (nums[i] == nums[i - 1] + 1) {
        len++;
    } else {
        maxLen = Math.max(maxLen, len);
        len = 1;  //put this line after Math.max(..)
    }
return Math.max(maxLen, len);
```

方法2：并查集（union-find set），也叫作不想交集（disjoint set）。

下面的并查集类UF使用的是weighted-quick-union并查集算法，可以限制树的高度为O（logN），因为每次查询的事件复杂度也是O（logN）。
如果加上路径压缩（path compression）那么会非常近似等于1.

这里有个技巧是做映射， 因为并查集必须是0-N连续的，而题目给的数字不是，那么就可以利用一个hashmap，key为值，value就是index位置，做一个映射即可。
实际并的是这个index位置。

```
public int longestConsecutive2(int[] nums) {
if (nums == null || nums.length == 0)
    return 0;
UF uf = new UF(nums.length);
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    int num = nums[i];
    if (map.containsKey(num)) {
        continue;
    }
    if (map.containsKey(num + 1)) {
        uf.union(i, map.get(num + 1));
    }
    if (map.containsKey(num - 1)) {  //不是else if
        uf.union(i, map.get(num - 1));
    }
    map.put(num, i);

return uf.maxWeight();

class UF {
    int[] s;
    int[] weight;
    int count;

    UF(int size) {
        s = new int[size];
        weight = new int[size];
        count = size;
        for (int i = 0; i < size; i++) {
            s[i] = i;
            weight[i] = 1;
        }
    }

    int find(int p) {
        while (p != s[p]) {
            p = s[p];
        }
        // 下面是路径压缩path compression的写法，在Number of Islands II中非常有用，否则会TLE
        int root = p;
        while (root != s[root]) {
            root = s[root];
        }
        while (p != root) {
            int newp = s[p];
            s[p] = root;
            p = newp;
        }
        return p;
    }

    void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;
        }
        if (weight[rootP] < weight[rootQ]) {
            s[rootP] = rootQ;
            weight[rootQ] += weight[rootP];
        } else {
            s[rootQ] = rootP;
            weight[rootP] += weight[rootQ];
        }
        count--;
    }

    int maxWeight() {
        int max = 1;
        for (int w : weight) {
            max = Math.max(w, max);
        }
        return max;
    }
}
```

### [125. Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)

E,  Two Pointers String

```
if (s.isEmpty()) { return true;}
int head = 0, tail = s.length() - 1;
while(head <= tail) {  //注意小于等于，而不是!=
    cHead = s.charAt(head);
    cTail = s.charAt(tail);
    if (!Character.isLetterOrDigit(cHead))
        head++;
    else if(!Character.isLetterOrDigit(cTail))
        tail--;
    else
        if (Character.toLowerCase(cHead) != Character.toLowerCase(cTail))
            return false;
        head++;
        tail--;
return true;
```

### [124. Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/)

H, Tree Depth-first Search

返回树中任意两点路径的最大值。只要两点间有路径联通就可以。有点难理解。For this problem, a path is defined as any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The path does not need to go through the root.

如下图所示，局部上面4-2-5构成了一个局部的最长连通，值为11，而对于1来说，需要取5-2这个路径，而不是4-2。
```
    1
   / \
  2   3
 / \  /\
4   5 6 7
```

```
int max;

public int maxPathSum(TreeNode root) {
    max = Integer.MIN_VALUE;
    maxPathDown(root);
    return max;
}

private int maxPathDown(TreeNode node) {
    if (node == null) return 0;
    int left = Math.max(0, maxPathDown(node.left));
    int right = Math.max(0, maxPathDown(node.right));
    max = Math.max(max, left + right + node.val);
    return Math.max(left, right) + node.val;
}
```



### [123. Best Time to Buy and Sell Stock III](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/)

用一个数组表示股票每天的价格，数组的第i个数表示股票在第i天的价格。最多交易两次，手上最多只能持有一支股票，求最大收益。

分析：动态规划法。以第i天为分界线，计算第i天之前进行一次交易的最大收益preProfit[i]，和第i天之后进行一次交易的最大收益postProfit[i]，最后遍历一遍，max{preProfit[i] + postProfit[i]} (0≤i≤n-1)就是最大收益。第i天之前和第i天之后进行一次的最大收益求法同Best Time to Buy and Sell Stock I。

时间O(n)，空间O(n)。

[参考链接](http://liangjiabin.com/blog/2015/04/leetcode-best-time-to-buy-and-sell-stock.html)

另外还有一个更难的[题目188 Best Time to Buy and Sell Stock IV](https://leetcode
.com/problems/best-time-to-buy-and-sell-stock-iv) 最多进行K次交易，动态规划的方程比较难写。

```
if (prices.length < 2) { return 0; }

int n = prices.length;
int[] preProfit = new int[n];
int[] postProfit = new int[n];

int curMin = prices[0];
for (int i = 1; i < n; i++)
    curMin = Math.min(curMin, prices[i]);
    preProfit[i] = Math.max(preProfit[i - 1], prices[i] - curMin);

int curMax = prices[n - 1];
for (int i = n - 2; i >= 0; i--)
    curMax = Math.max(curMax, prices[i]);
    postProfit[i] = Math.max(postProfit[i + 1], curMax - prices[i]);

int maxProfit = 0;
for (int i = 0; i < n; i++)
    maxProfit = Math.max(maxProfit, preProfit[i] + postProfit[i]);

return maxProfit;
```

### [122. Best Time to Buy and Sell Stock II](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/)

M, Array Greedy

用一个数组表示股票每天的价格，数组的第i个数表示股票在第i天的价格。交易次数不限，但一次只能交易一支股票，也就是说手上最多只能持有一支股票，求最大收益。

贪心法。从前向后遍历数组，只要当天的价格高于前一天的价格，就算入收益。

时间O(n)，空间O(1)。

注意这里是T+0的交易.

```
if (prices.length < 2) { return 0; }
int maxProfit = 0;
for (int i = 1; i < prices.length; i++)
    int diff = prices[i] - prices[i - 1];
    if (diff > 0)
        maxProfit += diff;
return maxProfit;
```


### [121. Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)

E, Array Dynamic Programming

用一个数组表示股票每天的价格，数组的第i个数表示股票在第i天的价格。
如果只允许进行一次交易，也就是说只允许买一支股票并卖掉，求最大的收益。

动态规划法。从前向后遍历数组，记录当前出现过的最低价格，作为买入价格，并计算以当天价格出售的收益，作为可能的最大收益，整个遍历过程中，出现过的最大收益就是所求。

时间O(n)，空间O(1)

状态转移方程，d(j)表示在j这一天出售股票的最大收益：
```
d(j) = Vj - Min(Vi, i in [0, j-1])
```

```
int minPrice = Integer.MAX_VALUE;
int maxPro = 0;
for (int i = 0; i < prices.length; i++) {
    minPrice = Math.min(minPrice, prices[i]);
    maxPro = Math.max(prices[i] - minPrice, maxPro);
}
return maxPro;
```

和[53. Maximum Subarray](https://leetcode.com/problems/maximum-subarray/)类似的动态规划思想，都能做非常小的空间复杂度。

记录以i为结尾的，最大的连续子数组和，应该是当前前面最大的sum + 自己，或者干脆就自立门户自己了。
```
int maxSum = nums[0], sum = 0;
for(int i = 0; i < nums.length; ++i)
    sum = Math.max(sum + nums[i], nums[i]);
    maxSum = Math.max(maxSum, sum);
return maxSum;
```

### [120. Triangle](https://leetcode.com/problems/triangle/)

M,  Array Dynamic Programming

递推式如下：
```
          f(i - 1, j) + V(i,j) j=0
f(i, j) = Min{f(i-1,j-1), f(i-1,j)} + V(i,j) 0<j<len-1
          f(i - 1, j - 1) + V(i,j) j=len-1
```

简单的动态规划：
```
for (int i = 0; i < height; i++) {
    List<Integer> list = triangle.get(i);
    for (int j = 0; j < list.size(); j++) {
        if (i - 1 < 0) {
            s[i][j] = list.get(j);
        } else if (j - 1 < 0) {
            s[i][j] = s[i - 1][j] + list.get(j);
        } else if (j == list.size() - 1) {
            s[i][j] = s[i - 1][j - 1] + list.get(j);
        } else {
            s[i][j] = Math.min(s[i - 1][j - 1], s[i - 1][j]) + list.get(j);
        }
    }
}
return min in s[height - 1]
```

### [117. Populating Next Right Pointers in Each Node II](https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/)

M, Tree Depth-first Search
```
         1 -> NULL
       /  \
      2 -> 3 -> NULL
     / \    \
    4-> 5 -> 7 -> NULL
```

利用上一层已经是linkedlist的特性，不用level order traversal。下面的代码一次性写对的。
```
public void connect(TreeLinkNode root) {
    TreeLinkNode head = root;
    TreeLinkNode prev = null;
    while (head != null) {
        TreeLinkNode curr = head;
        TreeLinkNode nextLevelHead = null;
        while (curr != null) {
            if (curr.left != null) {
                if (prev == null) {
                    prev = curr.left;
                    nextLevelHead = prev;
                } else {
                    prev.next = curr.left;
                    prev = prev.next;
                }
            }
            if (curr.right != null) {
                if (prev == null) {
                    prev = curr.right;
                    nextLevelHead = prev;
                } else {
                    prev.next = curr.right;
                    prev = prev.next;
                }
            }
            curr = curr.next;
        }
        prev = null;
        head = nextLevelHead;
    }
}
```

后续参考level order遍历写了一个BFS更好理解：
```
if (root == null) return;
Queue<TreeLinkNode> queue = new LinkedList<>();
queue.offer(root);
while (!queue.isEmpty()) {
    TreeLinkNode head = null;
    TreeLinkNode curr = null;
    while (!queue.isEmpty()) {
        TreeLinkNode node = queue.poll();
        if (curr == null) {
            curr = node;
            head = curr;
        } else {
            curr.next = node;
            curr = curr.next;
        }
    }
    while (head != null) {
        if (head.left != null)
             queue.offer(head.left);
        if (head.right != null)
             queue.offer(head.right);
        head = head.next;
    }
}
```

### [116. Populating Next Right Pointers in Each Node](https://leetcode.com/problems/populating-next-right-pointers-in-each-node/)

M, Tree Depth-first Search

完全二叉树perfect binary tree是普通二叉树的特例。和题目117的解法一样。



### [114. Flatten Binary Tree to Linked List](https://leetcode.com/problems/flatten-binary-tree-to-linked-list/)

M, Tree Depth-first Search

For example,
Given
```
         1
        / \
       2   5
      / \   \
     3   4   6
```
The flattened tree should look like:
```
   1
    \
     2
      \
       3
        \
         4
          \
           5
            \
             6
```

```
public void flatten(TreeNode root) {
    if (root == null) return;

    TreeNode left = root.left;
    TreeNode right = root.right;

    flatten(left);
    flatten(right);

    root.right = left;
    root.left = null; //别忘了
    TreeNode curr = root;
    while (curr.right != null) { //顺序找到"接口"
        curr = curr.right;
    }

    curr.right = right;
}
```

### [113. Path Sum II](https://leetcode.com/problems/path-sum-ii/)

M, Tree Depth-first Search

在题目112的基础上要输出所有的路径。

完全就是N-Queens的backtrack的模板。
```
public List<List<Integer>> pathSum(TreeNode root, int sum) {
    List<List<Integer>> result = new ArrayList<>();
    helper(root, sum, new ArrayList<>(), result);
    return result;
}

private void helper(TreeNode node, int sum, List<Integer> tempList, List<List<Integer>> result) {
    if (node == null) {
        return;
    }

    if (node.left == null && node.right == null && sum - node.val == 0) {
        tempList.add(node.val);
        result.add(new ArrayList<>(tempList));
        tempList.remove(tempList.size() - 1);
        return;
    }

    tempList.add(node.val);
    helper(node.left, sum - node.val, tempList, result);
    helper(node.right, sum - node.val, tempList, result);
    tempList.remove(tempList.size() - 1);
}
```

### [112. Path Sum](https://leetcode.com/problems/path-sum/)

E, Tree Depth-first Search

只判断是否从root到leaf有一个路径和等于给定值。

```
public boolean hasPathSum(TreeNode root, int sum) {
    if (root == null) {
        return false;
    }
    if (root.left == null && root.right == null && root.val == sum) {
        return true;
    }
    return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
}
```


### [111. Minimum Depth of Binary Tree](https://leetcode.com/problems/minimum-depth-of-binary-tree/)

E, Tree Depth-first Search Breadth-first Search

```
public int minDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    if (root.left == null || root.right == null) { //很重要，处理不平衡用的
        return 1 + Math.max(minDepth(root.left), minDepth(root.right));
    }
    return 1 + Math.min(minDepth(root.left), minDepth(root.right));
}
```

发散下，max depth如下，不用中间的步骤。
```
if (root == null) {
    return 0;
}
return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
```

### [110. Balanced Binary Tree](https://leetcode.com/problems/balanced-binary-tree/)

E, Tree Depth-first Search

给定一棵二叉树，判定它是否为平衡二叉树。

算法分析：
平衡二叉树（Balanced Binary Tree）又被称为AVL树（有别于AVL算法），且具有以下性质：它是一棵空树或它的左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树。

下面的代码就完全按照定义，首先得到节点左右子树的高度（递归），然后判断左右子树是否为平衡二叉树，利用递归完成整棵树的判断。完全满足条件，就返回true.
```
public static boolean isBalanced(TreeNode root)
    if (root == null) return true;
    if (getHeight(root) == -1) return false;
    return true;

/** 不符合条件就返回-1 */
public static int getHeight(TreeNode root) {
    if (root == null) return 0;
    int left = getHeight(root.left);
    int right = getHeight(root.right);
    if (left == -1 || right == -1)
        return -1;
    if (Math.abs(left - right) > 1)
        return -1;
    return Math.max(left, right) + 1;
```

如果题目是检查一棵树是否平衡，平衡是指任意两个叶子节点到root的距离之差不大于1.

判断平衡就利用111题目中的max depth - min depth <=1 来判断。

下面的树满足本题的要求，是一个balanced binary tree，但是他不平衡。
```
                 6
               /   \
              4     9
             /\     / \
            2  5   8   10
           / \  \       \
          1  3  5        5
         /
        5
```


### [108. Convert Sorted Array to Binary Search Tree](https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/)

M, Tree Depth-first Search

二分查找的思想
```
return helper(nums, 0, nums.length - 1);

TreeNode helper(int[] nums, int start, int end) {
    if (start > end) { return null;}
    int mid = (start + end) >> 1;
    TreeNode node = new TreeNode(nums[mid]);
    node.left = helper(nums, start, mid - 1);
    node.right = helper(nums, mid + 1, end);
    return node;
}
```

### [107. Binary Tree Level Order Traversal II](https://leetcode.com/problems/binary-tree-level-order-traversal-ii/)

E, Tree Breadth-first Search

bottom-up level，和题目102类似，需要做一次backtrack的思想，把add操作放在DFS递归的后面。

```
public List<List<Integer>> traverse(TreeNode root) {
    if (root == null) return new ArrayList<>(0);
    List<List<Integer>> result = new ArrayList<>();
    List<TreeNode> level = new ArrayList<>(1);
    level.add(root);
    levelOrderBottom(level, result);
    return result;
}

private static void levelOrderBottom(List<TreeNode> level, List<List<Integer>> result) {
    List<TreeNode> newLevel = new ArrayList<>(level.size() << 1);
    List<Integer> levelValues = new ArrayList<>(level.size());
    for (TreeNode treeNode : level) {
        levelValues.add(treeNode.val);
        if (treeNode.left != null) {
            newLevel.add(treeNode.left);
        }
        if (treeNode.right != null) {
            newLevel.add(treeNode.right);
        }
    }
    if (!newLevel.isEmpty()) {
        levelOrderBottom(newLevel, result);
    }
    result.add(levelValues);
}
```

使用Queue的解法如下：
```
private static void levelOrderBottom2(Queue<TreeNode> queue, List<List<Integer>> result) {
    while (!queue.isEmpty()) {
        List<TreeNode> level = new ArrayList<>();
        while (!queue.isEmpty()) {
            level.add(queue.poll());
        }
        for (TreeNode node : level)
            if (node.left != null)
                queue.offer(node.left);
            if (node.right != null)
                queue.offer(node.right);
        levelOrderBottom2(queue, result);
        result.add(level.stream().map(n -> n.val).collect(Collectors.toList()));
    }
}
```

### [106. Construct Binary Tree from Inorder and Postorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)

和105类似，只不过postorder遍历尾巴是根节点。不再赘述。


### [105. Construct Binary Tree from Preorder and Inorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)

M, Tree Array Depth-first Search

剑指offer面试题6-重建二叉树。

利用递归来做Recursion

假设
```
Pre-order: F, B, A, D, C, E, G, I, H
           p

In-order:  A, B, C, D, E, F, G, H, I
                          q
```
F肯定是当前的根节点，那么久从In-order里面找到F即可，因为题目说了不重复数字。

从In-order相当于“抡”起来，两个部分，左边的就是左子树，右边的就是右子树。

```
               F
             /   \
A, B, C, D, E     G, H, I
```

A, B, C, D, E和G, H, I又是新的问题，所以可以递归的解决，然后赋值左右节点的node给F即可。

```
public TreeNode buildTree(int[] preorder, int[] inorder) {
    return doBuildTree(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
}

TreeNode doBuildTree(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd) {
    if (preStart > preEnd || inStart > inEnd) {
        return null;
    }
    int nodeIndex = find node index equal to preorder[preStart];
    int leftChildrenLen = nodeIndex - inStart;
    TreeNode node = new TreeNode(preorder[preStart]);
    // 做好边界条件即可
    node.left = doBuildTree(preorder, preStart + 1, preStart + leftChildrenLen, inorder, inStart,nodeIndex - 1);
    node.right = doBuildTree(preorder, preStart + leftChildrenLen + 1, preEnd, inorder,nodeIndex + 1, inEnd);
    return node;
}
```

### [104. Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/)

E, Tree Depth-first Search

max depth is the longest path from the root node down to the farthest leaf node.

```
public int maxDepth(TreeNode root)
    if (root == null)
        return 0;
    return 1 + Math.max(maxDepth(root.left),maxDepth(root.right));
```

### [102. Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/)

E, Tree Breadth-first Search

层序遍历。

```
if (root == null) return new ArrayList<>(0);
List<List<Integer>> result = new ArrayList<>();
List<TreeNode> level = new ArrayList<>(1);
level.add(root);
List<TreeNode> newLevel;
List<Integer> levelValues;
while (true) {
    temp = new list
    newLevel = new list
    add temp to result
    for each i in level
        add i to temp
        add i.left to newLevel
        add i.right to newLevel
    level = newLevel;
    if (level is empty) {
        break;
    }
}
return result;
```

更容易记忆的解法，用queue来实现。

```
if (root == null) return new ArrayList<>(0);
List<List<Integer>> result = new ArrayList<>();
Queue<TreeNode> queue = new LinkedList<>();
queue.offer(root);
while (!queue.isEmpty()) {
    List<TreeNode> level = new ArrayList<>();
    while (!queue.isEmpty()) {
        level.add(queue.poll());
    }
    result.add(level.stream().map(n -> n.val).collect(Collectors.toList()));  //JDK8 API
    for (TreeNode node : level) {
        if (node.left != null)
            queue.offer(node.left);
        if (node.right != null)
            queue.offer(node.right);
    }
}
return result;
```

### [101. Symmetric Tree](https://leetcode.com/problems/symmetric-tree/)

E, Tree Depth-first Search Breadth-first Search

是否是镜像树，和题目100-Same Tree的思想一样，首先可以利用DFS思想的Recursive递归，Iterative非递归（BFS思想利用queue）两种方式。
```
    1
   / \
  2   2
 / \ / \
3  4 4  3
```

DFS递归：
```
public boolean isSymmetric1(TreeNode root) {
    return root == null || helper(root.left, root.right);
}

public boolean helper(TreeNode node1, TreeNode node2)
    if (node1 == null && node2 == null)
        return true;
    else if (node1 != null && node2 != null)
        return node1.val == node2.val && helper(node1.left, node2.right) && helper(node1.right, node2.left);
    else
        return false;

另外helper也能这么写：
if (p == null || q == null)
    return p == q;
else
    return p.val == q.val && helper(p.left, q.right) && helper(p.right, q.left);
```

BFS用queue一层一层的对比：
```
if (root == null) return true;
Queue<TreeNode> queue = new LinkedList<>();
queue.add(root.left);
queue.add(root.right);
while (queue.size() > 1) {
    TreeNode node1 = queue.poll();
    TreeNode node2 = queue.poll();
    if (node1 == null && node2 == null)  //和递归的类似
        continue;
    else if (node1 != null && node2 != null)
        if (node1.val != node2.val) return false;
    else return false;
    queue.add(node1.left);
    queue.add(node2.right);
    queue.add(node1.right);
    queue.add(node2.left);
}
return true;
```

### [100. Same Tree](https://leetcode.com/problems/same-tree/)

E, Tree Depth-first Search

DFS用递归。
```
if (p != null && q != null)
    return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
else if (p == null && q == null)
    return true;
else
    return false;
```

```
if (p == null || q == null)
     return p == q;
else return p.val == q.val && isSameTree(p.left, q.left)&& isSameTree(p.right, q.right);
```

### [98. Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/)

M, Tree Depth-first Search

模拟in-order遍历的算法过程。中间visit的过程换成比较prev和node的小大。

```
Stack<TreeNode> stack = new Stack<>();
TreeNode curr = root;
TreeNode prev = null;
while (curr != null || !stack.isEmpty()) {
    if (curr != null) {
        stack.push(curr);
        curr = curr.left;
    } else {
        TreeNode node = stack.pop();
        if (prev != null && node.val < prev.val) {
            return false;
        }
        prev = node;
        curr = node.right;
    }
}
return true;
```

另外递归的算法如下，但是[1,1]OJ过不了：
```
TreeNode prev = null;
return helper2(root, prev);

boolean helper2(TreeNode node, TreeNode prev) {
    if (node == null)
        return true;
    if (!helper2(node.left, prev))
        return false;
    if (prev != null && prev.val >= node.val)
        return false;
    prev = node;
    return helper2(node.right, prev);
```

### [96. Unique Binary Search Trees](https://leetcode.com/problems/unique-binary-search-trees/)

M, Tree Dynamic Programming

Given n, how many structurally unique BST's (binary search trees) that store values 1...n?

For example,
Given n = 3, there are a total of 5 unique BST's.
```
   1         3     3      2      1
    \       /     /      / \      \
     3     2     1      1   3      2
    /     /       \                 \
   2     1         2                 3
```
[参考链接1](https://discuss.leetcode.com/topic/37310/fantastic-clean-java-dp-solution-with-detail-explaination)
[参考链接2](https://discuss.leetcode.com/topic/5673/dp-problem-10-lines-with-comments)

不懂问题的时候就画图，然后总结
```
n=1
1

n=2
  1     2
 /       \
2         1

n=3
左边一个没有，j=1
 1      1
  \      \
   2      3
    \    /
     3  2

左边有一个，j=2
  2
 / \
1   3
左边有两个，j=3，和j=1的情况一样，对称了！
    3     3
   /     /
  2     1
 /      \
1        2
```
所以总结出递推公式，可以用动态规划来解决。
```
Taking 1~n as root respectively:
1 as root: # of trees = F(0) * F(n-1) // F(0) == 1
2 as root: # of trees = F(1) * F(n-2)
3 as root: # of trees = F(2) * F(n-3)
...
n-1 as root: # of trees = F(n-2) * F(1)
n as root: # of trees = F(n-1) * F(0)

So, the formulation is: F(n) = F(0) * F(n-1) + F(1) * F(n-2) + F(2) * F(n-3) + ... + F(n-2) * F(1) + F(n-1) * F(0)
```

```
F(0) = 1
F(1) = 1
F(2) = F(0)*F(1) + F(1)*F(0) = 1*1 + 1*1 = 2
F(3) = F(0)*F(2) + F(1)*F(1) + F(2)*F(0) = 1*2 + 1*1 + 1*2 = 5
F(4) = F(0)*F(3) + F(1)*F(2) + F(2)*F(1) + F(0)*F(3) = 1*5 + 1*2 + 2*1 + 5*1 = 14
F(5) = F(0)*F(4) + F(1)*F(3) + F(2)*F(2) + F(3)*F(1) + F(4)*F(0) = 1*14 + 1*5 + 2*2 + 5*1 * 14*1 = 42
```

算法如下：
```
int[] dp = new int[n + 1];
dp[0] = dp[1] = 1;
for (int i = 2; i <= n; i++) {
    dp[i] = 0;
    for (int j = 1; j <= i; j++) {
        dp[i] += dp[j - 1] * dp[i - j];
    }
}
return dp[n];
```

### [95. Unique Binary Search Trees II](https://leetcode.com/problems/unique-binary-search-trees-ii/)

M, Tree Dynamic Programming

思路和unique-binary-search-tree一样，只不过稍微复杂需要输出所有的树.

根据动态规划的结果可以绘制出树的形状，然后按照in-order遍历填入数字即可，[参考链接](https://discuss.leetcode.com/topic/2940/java-solution-with-dp/2)


### [94. Binary Tree Inorder Traversal](https://leetcode.com/problems/binary-tree-inorder-traversal/)

M, Tree Hash Table Stack

Recursively:
```
public List<Integer> traverse(TreeNode root)
    List<Integer> result = new ArrayList<>();
    doInorder(root, result);
    return result;

private void doInorder(TreeNode root, List<Integer> result)
    if (root != null)
        doInorder(root.left, result);
        result.add(root.val);
        doInorder(root.right, result);
```

Iteratively:
```
List<Integer> result = new ArrayList<Integer>();
Stack<TreeNode> stack = new Stack<TreeNode>();
TreeNode cur = root;
while (cur != null || !stack.empty()) {
    if (cur != null) {
        stack.push(cur);
        cur = cur.left;
    } else {
        TreeNode node = stack.pop();
        result.add(node.val);  // Add after all left children
        cur = node.right;
    }
}
return result;
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

### [92. Reverse Linked List II](https://leetcode.com/problems/reverse-linked-list-ii/)

M, Linked list

复用了题目206 ReverseLinkedList的iterative solution的逻辑。

```
//定位到位置m，就是curr和tail，同时保存前面的last和begin
last/begin  curr/tail
   1     ->  2     ->  3  ->  4  ->  5

//利用题目206 ReverseLinkedList的逻辑做反转
begin      tail             last   curr
   1   <->  2     <-  3  <-  4      5


//begin接last
begin      tail             last   curr
   1   <-  2     <-  3  <-  4      5
   |_______________________/|


//tail接curr
            _________________________
           |                      |/
begin      tail             last   curr
   1       2     <-  3  <-  4      5
   |_______________________/|
```

### [91. Decode Ways](https://leetcode.com/problems/decode-ways/)

M, Dynamic Programming String

题目的要求不是和excel那道题似的考虑进位，而是直接A-Z的映射，没有AB这一说，因此不要理解错误题目！

动态规划：

从当前的数字往前看1个数组，如果数字不等于0，那么就等于前面的所有排列。
往前看两个数字，如果数字在10和26之间，则可以找到一个字母，则等于前面所有的排列，否则就是0，这根本构不成一个合法的解析数字。

这就是递推公式。

```
if (s == null || s.isEmpty() || s.charAt(0) == '0')
    return 0;

int[] dp = new int[s.length() + 1];
dp[0] = 1;
dp[1] = 1;

for (int i = 2; i <= s.length(); i++) {
    int num = Integer.parseInt(s.substring(i - 2, i));
    int twoStepsBehind = (num <= 26 && num >= 10) ? dp[i - 2] : 0;
    int oneStepBehind = (Integer.parseInt(s.substring(i - 1, i)) != 0) ? dp[i - 1] : 0;
    dp[i] = twoStepsBehind + oneStepBehind; // can reach here by either hopping 2 steps or 1 step
}

return dp[s.length()];
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

### [88. Merge Sorted Array](https://leetcode.com/problems/merge-sorted-array/)

E, Array Two Pointer

一开始限于从前往后merge的思路里，需要nums1不断的交换和后面的元素，非常难于解决。如果从后往前走，那么将豁然开朗！

```
int i = m - 1;
int j = n - 1;
int k = m + n - 1;
while (i >= 0 && j >= 0)
    if (nums1[i] > nums2[j])
        nums1[k--] = nums1[i--];
    else
        nums1[k--] = nums2[j--];
while (j >= 0) //最后不能忘
    nums1[k--] = nums2[j--];
```

举例：
```
nums1 = new int[] {3, 5, 7, 10, 11, 0, 0, 0, 0, 0};
nums2 = new int[] {2, 4, 6, 8, 9};

nums1[9] = nums1[4]
[3, 5, 7, 10, 11, 0, 0, 0, 0, 11]
nums1[8] = nums1[3]
[3, 5, 7, 10, 11, 0, 0, 0, 10, 11]
nums1[7] = nums2[4]
[3, 5, 7, 10, 11, 0, 0, 9, 10, 11]
nums1[6] = nums2[3]
[3, 5, 7, 10, 11, 0, 8, 9, 10, 11]
nums1[5] = nums1[2]
[3, 5, 7, 10, 11, 7, 8, 9, 10, 11]
nums1[4] = nums2[2]
[3, 5, 7, 10, 6, 7, 8, 9, 10, 11]
nums1[3] = nums1[1]
[3, 5, 7, 5, 6, 7, 8, 9, 10, 11]
nums1[2] = nums2[1]
[3, 5, 4, 5, 6, 7, 8, 9, 10, 11]
nums1[1] = nums1[0]
[3, 3, 4, 5, 6, 7, 8, 9, 10, 11]
nums1[0] = nums2[0]
[2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
```

### [87. Scramble String](https://leetcode.com/problems/scramble-string/)

H, Dynamic Programming String

搅拌字符串，是学习递归的一个非常好的典型例子。

所谓搅拌字符串，就是在任意的非叶子节点，交换左右两个子树。

```
    rgeat
   /    \
  rg    eat
 / \    /  \
r   g  e   at
           / \
          a   t
```
那么great和rgeat是一对Scramble String。

```
if (s1 == null || s2 == null) return s1 == s2;
if (s1.equals(s2)) {
    return true;  //非常重要！退出条件
}
if (s1.length() != s2.length()) return false;
int[] count = new int[26];
for (int i = 0; i < s1.length(); i++) {
    count[s1.charAt(i) - 'a']++;
    count[s2.charAt(i) - 'a']--;
}
for (int i = 0; i < 26; i++) {
    if (count[i] != 0)
        return false;
}
int len = s1.length();
for (int i = 1; i < len; i++) {
    if (isScramble(s1.substring(0, i), s2.substring(0, i)) &&
            isScramble(s1.substring(i), s2.substring(i))) {
        return true;
    }
    if (isScramble(s1.substring(0, i), s2.substring(len - i)) &&
            isScramble(s1.substring(i), s2.substring(0, len - i))) {
        return true;
    }
}
return false;
```

### [86. Partition List](https://leetcode.com/problems/partition-list/)

M, Linked List Two Pointers

curr2.next = null;非常重要，避免环的出现！

```
ListNode dummy1 = new ListNode(0), dummy2 = new ListNode(0);  //dummy heads of the 1st and 2nd queues
ListNode curr1 = dummy1, curr2 = dummy2;      //current tails of the two queues;
while (head != null)
    if (head.val < x) {
        curr1.next = head;
        curr1 = head;
    } else {
        curr2.next = head;
        curr2 = head;
    }
    head = head.next;
curr2.next = null;          //important! avoid cycle in linked list. otherwise u will get TLE.
curr1.next = dummy2.next;
return dummy1.next;
```

### [85. Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/)

H,  Array Hash Table Stack Dynamic Programming

承接84题，可以利用84题的简化这个题目。

实际就是把每一层看做一个直方图histogram，然后求最大area。每一层的最大aream再挑最大的。

下面是自己一次性写的实现。
```
public int maximalRectangle(char[][] matrix) {
    int max = 0;
    for (int i = 0; i < matrix.length; i++) {
        int[] heights = getHistogram(matrix, i);
        System.out.println("calc :" + Arrays.toString(heights));
        max = Math.max(max, largestRectangleInHistogram(heights));
        System.out.println("sub max :" + largestRectangleInHistogram(heights));
    }
    return max;
}

int[] getHistogram(char[][] matrix, int row) {
    int[] heights = new int[matrix[0].length];
    boolean[] hasEnd = new boolean[matrix[0].length];
    for (int i = row; i >= 0; i--) {
        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[i][j] == '1' && !hasEnd[j]) {
                heights[j]++;
            } else {
                hasEnd[j] = true;
            }
        }
    }
    return heights;
}

int largestRectangleInHistogram(int[] heights) {
    int maxArea = 0;
    Stack<Integer> stack = new Stack<>();
    for (int i = 0; i <= heights.length; i++) {
        int h;
        if (i == heights.length) {
            h = 0;
        } else {
            h = heights[i];
        }
        if (stack.isEmpty() || h >= heights[stack.peek()]) {
            stack.push(i);
        } else {
            int lastHighIdx = stack.pop();
            int area;
            if (!stack.isEmpty()) {
                area = heights[lastHighIdx] * (i - 1 - stack.peek());
            } else {
                area = heights[lastHighIdx] * i;
            }
            maxArea = Math.max(maxArea, area);
            i--;
        }
    }
    return maxArea;
}
```


### [84. Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)

H, Array Stack

直方图求最大长方形面积。

方法1：brute force，从0..len-1计算每一个以i开头的最大的长方形的面积，这个面积取决于i之后的那个最小的高度。

```
int max = 0;
for (int i = 0; i < heights.length; i++) {
    int minHeight = Integer.MAX_VALUE;
    for (int j = i; j < heights.length; j++) {
        if (heights[j] < minHeight) {
            minHeight = heights[j];
        }
        max = Math.max(max, minHeight * (j - i + 1));
    }
}
return max;
```

方法2：类似动态规划的思想。

使用动态规划，用left[i]表示第i个柱子可以最多向左延伸至第left[i]个柱子，形成一个矩形，right[i]则表示向右延伸。遍历两次，分别计算出这两个数组。

再遍历一次，即可求出所有的柱子可以形成的最大的矩形面积。

这里面helper函数就是计算left和right数组的。如果i=1的左右两边都大于i，那么left[i]=0，right[i]
=1，这就可以看做延展性，如不能延展自然就是自己的长度了。

```
∏ ∏
∏∏∏
∏∏∏
012
```

题目中延展后的数组如下：
```
   ∏
  ∏∏
  ∏∏
  ∏∏ ∏
∏ ∏∏∏∏
∏∏∏∏∏∏
012345

left= [0,0,2,3,2,5]
right=[0,5,3,3,5,5]
max  =[2,6,10,6,8,3]
```

实际这利用了memory记忆式算法，往左延展的时候可以“跳跃”的来。
```
   ∏
∏  ∏
∏∏ ∏∏
∏∏∏∏∏∏
∏∏∏∏∏∏∏
∏∏∏∏∏∏∏
0123456

left=[0,0,0,3,3,3,?]
下一个位置?应该是多少，首选<5>跳到<3>，<3>到<2>，<2>到<0>，三步即可计算出?位置的最左延展。
```

```
int max = 0;
int[] left = new int[len];
int[] right = new int[len];
helper(heights, left, right);
for (int i = 0; i < len; i++)
    max = Math.max(max, (right[i] - left[i] + 1) * heights[i]);
return max;

void helper(int[] heights, int[] left, int[] right)
    for (int i = 0; i < heights.length; i++)
        int k = i;
        while (k > 0 && heights[i] <= heights[k - 1]) //找最有可能的左边界
            k = left[k - 1];
        left[i] = k;

    for (int i = heights.length - 1; i >= 0; i--)
        int k = i;
        while (k < heights.length - 1 && heights[i] <= heights[k + 1])
            k = right[k + 1];
        right[i] = k;
```

方法3：[利用stack](https://discuss.leetcode.com/topic/7599/o-n-stack-based-java-solution)

O(n) stack based JAVA solution

太精巧的解法了。

* 栈存index
* 最后放0.
* 单调递增入栈
* 小于栈顶出栈，计算最大面积，max=(i - 1 - 栈顶idx)*高度，这就是它能包围的最大的区域，i保持不动。
* 注意如果栈为空，那么max=i * 高度

```
int len = heights.length;
Stack<Integer> stack = new Stack<>();
int maxArea = 0;
for (int i = 0; i <= len; i++) {
    int h;
    if (i == len) {
        h = 0;
    } else {
        h = heights[i];
    }
    if (stack.isEmpty() || h >= heights[stack.peek()]) {
        stack.push(i);
    } else {
        int tp = stack.pop();
        maxArea = Math.max(maxArea, heights[tp] * (stack.isEmpty() ? i : i - 1 - stack.peek()));
        i--;
    }
}
return maxArea;
```

```
    ∏   -6
 ∏  ∏   -5
 ∏  ∏   -4
∏∏  ∏∏  -3
∏∏∏∏∏∏  -2
∏∏∏∏∏∏∏  -1
0123456

1. stack is empty, push 0 to stack, stack = [0]
2. nums[1] >= stack.peek, push 1 to stack, stack = [0, 1]
3. nums[2] >= stack.peek, push 2 to stack, stack = [0, 1, 2]
4. nums[3] < stack.peek, max area = pop stack which is nums[2] * (i - 1 - stack.peek 1) = 5, keep i not move,
5. nums[3] < stack.peek, max area = pop stack which is nums[1] * (i - 1 - stack.peek 0) = 6, keep i not move
6. nums[3] >= stack.peek 0, push 3 to stack, stack = [0, 3]
7. nums[4] >= stack.peek 3, push 4 to stack, stack = [0, 3, 4]
8. nums[5] >= stack.peek 4, push 5 to stack, stack = [0, 3, 4, 5]
9. nums[6] < stack.peek, max area = pop stack which is nums[5] * (i - 1 - stack.peek 4) = 6, keep i not move
10.nums[6] >= stack.peek 4, push 6 to stack, stack = [0, 3, 4, 6]
11.out of bound which is 0 < stack.peek, max area = pop stack which is nums[6] * (i - 1 - stack.peek 4) = 6
12.out of bound which is 0 < stack.peek, max area = pop stack which is nums[4] * (i - 1 - stack.peek 3) = 8
13.out of bound which is 0 < stack.peek, max area = pop stack which is nums[3] * (i - 1 - stack.peek 0) = 12
14.out of bound which is 0 < stack.peek, max area = pop stack which is nums[0] * (stack is empty so i) = 7
```

一个很好的解释[http://www.cnblogs.com/lichen782/p/leetcode_Largest_Rectangle_in_Histogram.html](http://www.cnblogs.com/lichen782/p/leetcode_Largest_Rectangle_in_Histogram.html)


### [83. Remove Duplicates from Sorted List](https://leetcode.com/problems/remove-duplicates-from-sorted-list/)

E, Linked list

```
ListNode dummy = new ListNode(0);
dummy.next = head;
while (head != null)
    ListNode begin = head;
    while (head.next != null && head.next.val == head.val) {
        head = head.next;
    }
    begin.next = head.next;
    head = head.next;
return dummy.next;
```

另外一种递归的方法，从后往前倒，保留最后一个重复的节点，很巧妙！要理解深刻递归！
```
public ListNode deleteDuplicates2(ListNode head) {
    if (head == null || head.next == null) return head;
    head.next = deleteDuplicates(head.next);
    return head.val == head.next.val ? head.next : head;
}
```

### [82. Remove Duplicates from Sorted List II](https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/)

M, Linked list

比题目83稍微复杂一点
```
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode pre = dummy;
while (head != null)
    while (head.next != null && head.next.val == head.val) {
        head = head.next;
    }
    if (pre.next == head) {  //重点在这里可以跳过连续相同的比如1,1,2,2,3,4
        pre = pre.next;
    } else {
        pre.next = head.next;
    }
    head = head.next;
return dummy.next;
```

### [80. Remove Duplicates from Sorted Array II](https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/)

M, Array Two Pointers

和题目26用的模板一模一样，都是基于这里i和slow-N比较，如大于才共同进步
```
if (nums.length < 3)
    return 2;
int slow = 2;
for (int i = 2; i < nums.length; i++) {
    if (nums[i] > nums[slow - 2]) {
        nums[slow++] = nums[i];
    }
}
return slow;
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

### [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)

H,Hash Table Two Pointers String

和以下题目的解法模板一样，follow the same pattern！一切都是套路：
* [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
* [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
* [209. Minimum Size Subarray Sum，这道题更加典型](https://leetcode.com/problems/minimum-size-subarray-sum/)

这道题相对来说较为难。思想还是维护滑动窗口的概念，举例来说，
```
s=ADOBECODEBANC
t=ABC

end=0, s.charAt(0)=A, a[A]=1
end=0, s.charAt(0)=A, a[A]=0
end=1, s.charAt(1)=D, a[D]=-1
end=2, s.charAt(2)=O, a[O]=-1
end=3, s.charAt(3)=B, a[B]=1
end=3, s.charAt(3)=B, a[B]=0
end=4, s.charAt(4)=E, a[E]=-1
end=5, s.charAt(5)=C, a[C]=1
end=5, s.charAt(5)=C, a[C]=0
minStart=0, minLen=6, valid=ADOBEC //到这里就是发现了第一次匹配，最小的长度是6
start=1, end=6, maybe_valid=DOBEC //不断的移动start，直到不构成条件
end=6, s.charAt(6)=O, a[O]=-2
end=7, s.charAt(7)=D, a[D]=-2
end=8, s.charAt(8)=E, a[E]=-2
end=9, s.charAt(9)=B, a[B]=-1
end=10, s.charAt(10)=A, a[A]=1
end=10, s.charAt(10)=A, a[A]=0
start=2, end=11, maybe_valid=OBECODEBA //又满足条件了，长度不够小
start=3, end=11, maybe_valid=BECODEBA
start=4, end=11, maybe_valid=ECODEBA
start=5, end=11, maybe_valid=CODEBA
start=6, end=11, maybe_valid=ODEBA //不断的移动start，也就是去掉了c，直到不构成条件
end=11, s.charAt(11)=N, a[N]=-1
end=12, s.charAt(12)=C, a[C]=1
end=12, s.charAt(12)=C, a[C]=0
start=7, end=13, maybe_valid=DEBANC //又满足条件了，长度不够小
start=8, end=13, maybe_valid=EBANC //不断的移动start，直到不构成条件
minStart=8, minLen=5, valid=EBANC
start=9, end=13, maybe_valid=BANC
minStart=9, minLen=4, valid=BANC //移动的过程中不忘记更新minLen
start=10, end=13, maybe_valid=ANC
```

```
int start = 0, end = 0;
int counter = t.length();
int[] a = new int[256];
for (char c : t.toCharArray()) {
    a[c]++;
}
int minLen = Integer.MAX_VALUE;
int minStart = 0;
while (end < s.length()) {
    if (a[s.charAt(end)] > 0)
        counter--;
    a[s.charAt(end)]--;
    end++;
    while (counter == 0)
        if (minLen > end - start)
            minStart = start;
            minLen = end - start;
        a[s.charAt(start)]++;
        if (a[s.charAt(start)] > 0)
            counter++;
        start++;
return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
```

### [75. Sort Colors](https://leetcode.com/problems/sort-colors/)

M, Array Two Pointers Sort

荷兰国旗问题，类似快速排序的分区思想，只不过这里面是三段的分区

```
int begin = 0; int current = 0; int end = nums.length - 1;
while (current <= end) {
    if (nums[current] == 0) {
        swap(current, begin, nums);
        begin++;
        current++;
    } else if (nums[current] == 1) {
        current++;
    } else {
        swap(current, end, nums);
        end--;
    }
}
```

### [74. Search a 2D Matrix](https://leetcode.com/problems/search-a-2d-matrix/)

M, Array Binary Search

方法1：
剑指offer当中的题目3：二维数组中的查询的变种。可以用一个通用的方法：这个解法是从右上角开始找，如果target小于右上角的，则col--，如果大于右上角，则row++，直到数组越界或者找到target为止。

方法2：不要看做是个二维数组，实际这就是一个sorted list！用二分查找。
```
int begin = 0;
int end = m * n - 1;
while (begin <= end) {
    int mid = (begin + end) >> 1;
    if (matrix[mid / n][mid % n] < target) {
        begin = mid + 1;
    } else if (matrix[mid / n][mid % n] > target) {
        end = mid - 1;
    } else {
        return true;
    }
}
return false;
```

注意二分的模板是这样的，这是从JDK里拷贝出来的：
```
searchLoop(arr, 0, arr.length - 1, target)

private int searchLoop(int[] arr, int start, int end, int target) {
    while (start <= end) {
        int mid = (start + end) >>> 1;
        int midVal = arr[mid];

        if (midVal < target) {
            start = mid + 1;
        } else if (midVal > target) {
            end = mid - 1;
        } else {
            return mid;
        }
    }
    return -1;
}
```

### [73. Set Matrix Zeroes](https://leetcode.com/problems/set-matrix-zeroes/)

M, Array

技巧就是行列单独处理。

```
// if matrix[i][j] == 0 then set matrix[i][0] and matrix[0][j] == 0 to indicate later.
// but for matrix[0][0] this is double meaning, so allocate extra variable to indicate.
boolean row0 = false;
boolean col0 = false;

for (int i = 0; i < matrix.length; i++) {
    if (matrix[i][0] == 0) {
        col0 = true;
        break;

// row0 same as above

for (int i = 1; i < matrix.length; i++)
    for (int j = 1; j < matrix[0].length; j++)
        if (matrix[i][j] == 0) {
            matrix[0][j] = 0;
            matrix[i][0] = 0;

for (int i = 1; i < matrix.length; i++)
    for (int j = 1; j < matrix[0].length; j++)
        if (matrix[i][0] == 0 || matrix[0][j] == 0)
            matrix[i][j] = 0;

if (row0)
    for (int j = 0; j < matrix[0].length; j++)
        matrix[0][j] = 0;

// col0 same as above
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

### [71. Simplify Path](https://leetcode.com/problems/simplify-path/)

M, Stack String

非常简单，写对了，注意处理edge case，像..，.，//之类的。使用stack来处理..，最后用java8的stream来优雅处理。

```
if (path == null || path.length() == 0) return path;
String[] strs = path.split("/");
Stack<String> stack = new Stack<String>();
for (String str: strs) {
    if (str.equals("") || str.equals(".")) {
        continue;
    }else if (str.equals("..")) {
        if (!stack.isEmpty())
            stack.pop();
    } else {
        stack.push(str);
    }
}
String[] res = new String[stack.size()];
for (int i = stack.size() - 1; i >= 0; i--)
    res[i] = stack.pop();
return Arrays.stream(res).collect(Collectors.joining("/", "/", ""));
```

### [70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)

E, DP

这里注意Fabonacci是下面的解，和爬楼梯还不太一样，初始的值不一样
```
        0, n=0
        1, n=1
f(n) =  1, n=2
        f(n-1) + f(n-2), n>=3

if (n < 2)
    return n;
int dp[] = new int[] {0, 1, 1};
for (int i = 2; i <= n; i++)
    dp[2] = dp[0] + dp[1];
    dp[0] = dp[1];
    dp[1] = dp[2];

return dp[2];

assertThat(recursiveFibonacci(1), Matchers.is(1));
assertThat(recursiveFibonacci(2), Matchers.is(1));
assertThat(recursiveFibonacci(3), Matchers.is(2));
assertThat(recursiveFibonacci(4), Matchers.is(3));
assertThat(recursiveFibonacci(5), Matchers.is(5));
```

类似Fabonacci，先写递推式和初始条件：
```
        1, n=0
        1, n=1
f(n) =  2, n=2
        f(n-1) + f(n-2), n>=3

if (n < 2)
    return n;
int dp[] = new int[] {1, 1, 2};
for (int i = 2; i <= n; i++)
    dp[2] = dp[0] + dp[1];
    dp[0] = dp[1];
    dp[1] = dp[2];
return dp[2];
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

### [69. Sqrt(x)](https://leetcode.com/problems/sqrtx/)

M,Binary Search Math

Brute force的解法就是
```
from i to x
   find i such that i^2 == x or i^2 < x < (i + 1)^2.
```
超时了。

二分的模板需要稍加修改，这里注意判断mid^2>x需要考虑溢出的情况，改为mid>x/2
```
// 处理0的情况
if (0 == x)
    return 0;
int left = 1;
int right = x;
int ans = -1;
while (left <= right)
    int mid = left + (right - left) / 2;
    if (mid <= x / mid) {
        left = mid + 1;
        ans = mid;
    } else {
        right = mid - 1;
    }
return ans;
```

### [68. Text Justification](https://leetcode.com/problems/text-justification/)

H, String

```
1）遍历words，累计单词长度+空格数量 <= maxWidth就一直继续。
2）超过了maxWidth，证明这几个单词可以构成一行，分两种情况
 2.1）只有一个单词，全部在左边，右边补空格。
 2.2）多个单词利用 {maxWidth - wordLen / wordNum - 1} 计算空格间隙
 2.2）多个单词利用 {maxWidth - wordLen % wordNum - 1} 计算多余的空格
3）用sb加单词，最后一个单词没有空格。所以注意i = len - 1的情况
4）同时记录一个last表示这一批结束的index。还原一个sb用sb.setLength(0)。

5）上面都技术了，那么就只剩下最后一行了，题目要求最后一行不用补空格，所以按规矩一个单词，一个空格添加
6）使用maxWidht - sb.length来补其他空格。
```

```
if (words == null || words.length == 0) {
    return Collections.emptyList();
}
List<String> res = new ArrayList<>();
int wordLen = 0;
int wordNum = 0;
int last = 0;
List<String> subWords = new ArrayList<>();
for (int i = 0; i < words.length; i++) {
    String currWord = words[i];
    if (currWord.length() + wordLen + wordNum <= maxWidth) {
        subWords.add(currWord);
        wordLen += currWord.length();
        wordNum++;
        continue;
    }
    last = i;  //最后一行的一个起点
    StringBuilder sb = new StringBuilder();
    if (wordNum == 1) {
        sb.append(subWords.get(0));
        appendMultipleSpace(sb, maxWidth - wordLen);
    } else {
        int space = (maxWidth - wordLen) / (wordNum - 1);  //计算空格的空格数
        int extra = (maxWidth - wordLen) % (wordNum - 1);  //计算多余的留白，从第一个单词往后都多加一个space
        for (int j = 0; j < subWords.size(); j++) {
            sb.append(subWords.get(j));
            if (extra > 0) {
                sb.append(" ");
                extra--;
            }
            if (j != subWords.size() - 1) {
                appendMultipleSpace(sb, space);
            }
        }
    }
    i--;
    wordLen = 0;
    wordNum = 0;
    subWords.clear();
    res.add(sb.toString());
}

// 处理最后一行
StringBuilder sb = new StringBuilder();
for (int k = last; k < words.length; k++) {
    sb.append(words[k]);
    if (k != words.length - 1) {  //最后一个单词不要加空格，方式"a"这种情况，从而多加了一个。
        sb.append(" ");
    }
}
while (sb.length() < maxWidth) {
    sb.append(" ");
}
res.add(sb.toString());
return res;
```

### [67. Add Binary](https://leetcode.com/problems/add-binary/)

E, Math String

```
//任意一个为空返回另外一个
StringBuilder sb = new StringBuilder();
while (i >= 0 && j >= 0)
    int value = (a.charAt(i--) - '0') + (b.charAt(j--) - '0') + carry;
    sb.append(value % 2);
    carry = value / 2;
while (j >= 0) 参考上面的流程
while (i >= 0) 参考上面的流程
if (carry != 0) sb.append(carry);
return sb.reverse().toString(); //需要反转下，让低位在后面
```

### [66. Plus One](https://leetcode.com/problems/plus-one/)

E,  Array Math

```
//判断空返回0
int i = digits.length - 1;
while (i >= 0)
    if (digits[i] == 9) //遇到9就进位
        digits[i] = 0;
    else
        digits[i] = digits[i] + 1; //否则直接+1返回
        return digits;
    i--;
if (digits[0] == 0)   //处理99999+1的情况，需要补充高位表位100000
    int[] ret = new int[digits.length + 1];
    ret[0] = 1;
    return ret;
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

### [61. Rotate List](https://leetcode.com/problems/rotate-list/)

M,Linked List Two Pointers

```
if (head == null) return null;
ListNode tail = head;
len = 1;
while (tail.next != null)
    len++;
    tail = tail.next;   //1）遍历一遍知道长度，定位tail到尾巴
tail.next = head;  //cycle 2）连城一个环
k %= len;   //3）非常重要，取模！避免无谓的循环扫描
for (int i = 0; i < len - k; i++)   //4）既然成环了，就从tail往前跑到合适的“断点”位置，断开
    tail = tail.next;
ListNode newHead = tail.next;
tail.next = null;
return newHead;
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

```
int fact(int x) {
    int s = 1;
    for (int i = 1; i <= x; i++)
        s *= i;
    return s;
}

public String getPermutation(int n, int k) {
    // Note: The Solution object is instantiated only once and is reused by each test case.
    ArrayList<Character> chars = new ArrayList<Character>();
    for (int i = '1'; i < ('1' + n); i++) {  //1..9 inclusive
        chars.add((char) i);
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
    t[n - 1] = chars.get(0);  //avoid sub 0
    return new String(t);
}
```

怎么把这个4041000还原回来看这个帖子吧，[点击此](http://leetcode.tgic.me/permutation-sequence/index.html)。

同时联想这个题它是取余数不断的缩小，那么将十进制的数字转成其他进制，比如二进制、八进制、十六进制，在纸上的计算学会了。

一种通用的程序解法如下：
```
final static char[] digits = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b','c', 'd', 'e', 'f', 'g', 'h','i', 'j', 'k', 'l', 'm', 'n','o', 'p', 'q', 'r', 's', 't','u', 'v', 'w', 'x', 'y', 'z'};

public String ten2binary(int n)
    return toUnsignedString(n, 1);

public String ten2Octal(int n)
    return toUnsignedString(n, 3);

public String ten2hex(int n)
    return toUnsignedString(n, 4);

public String toUnsignedString(int n, int base)
    char[] res = new char[32];
    int idx = 32;
    int mask = (1 << base) - 1;
    do {
        res[--idx] = digits[n & mask];
        n >>>= base;
    } while (n != 0);
    return new String(res, idx, 32 - idx);
```



### [58. Length of Last Word](https://leetcode.com/problems/length-of-last-word/)

E, String

熟悉String的API。

```
s = s.trim();
return s.substring(s.lastIndexOf(" ") + 1, s.length()).length();

substring javadoc:
@param      beginIndex   the beginning index, inclusive.
@param      endIndex     the ending index, exclusive.
```

### [57. Insert Interval](https://leetcode.com/problems/insert-interval/)

H, Array Sort

```
List<Interval> result = new ArrayList<Interval>();
// 技巧就是用interval做一个pivot为null到时候就不断的添加，这是做尾巴
// 如果是i.end < newInterval.start就是头，也不断添加
// 遇到overlap的时候就不断的扩张
// 走到不能扩张的时候i.start > newInterval.end，就加进去，并且把interval=null
for (Interval i : intervals)
    if (newInterval == null || i.end < newInterval.start) {
        result.add(i);
    } else if (i.start > newInterval.end) {
        result.add(newInterval);
        result.add(i);
        newInterval = null;
    } else {
        newInterval.start = Math.min(newInterval.start, i.start);
        newInterval.end = Math.max(newInterval.end, i.end);
    }

if (newInterval != null)
    result.add(newInterval);
return result;
```


### [56. Merge Intervals](https://leetcode.com/problems/merge-intervals/)

H, Array Sort
```
Given [1,3],[2,6],[8,10],[15,18],
return [1,6],[8,10],[15,18].
```
算法如下，和这个类似的题目还有 (H)Insert Interval, (E) Meeting Rooms, (M) Meeting Rooms II

```
if (intervals.size() <= 1) return intervals;
//使用JDK8的lambda排序
Collections.sort(intervals, (i1, i2) -> Integer.compare(i1.start, i2.start));

List<Interval> result = new LinkedList<>();
int start = intervals.get(0).start;
int end = intervals.get(0).end;

for (Interval interval : intervals)
    if (interval.start <= end) // Overlapping intervals, move the end if needed
        end = Math.max(end, interval.end);
    else                     // Disjoint intervals, add the previous one and reset bounds
        result.add(new Interval(start, end));
        start = interval.start;
        end = interval.end;

// Add the last interval
result.add(new Interval(start, end));
return result;
```


### [55. Jump Game](https://leetcode.com/problems/jump-game/)

M, Array Greedy

经典的贪心算法，尽量往前跳，每个点的当前位置+它的跳跃力（i+nums[i]）就是它能reach的最远的点，记录这个点为max位置。

遍历数组，直到某个位置大于max，证明了前面怎么跳肯定都跳不到这，自然就无解了。

```
int max = 0;
for (int i = 0; i < nums.length; i++)
    if (i > max)
        return false;
    max = Math.max(nums[i] + i, max);
return true;
```


### [54. Spiral Matrix](https://leetcode.com/problems/spiral-matrix/)

M, Array

```
int rowBegin = 0;
int rowEnd = matrix.length-1;
int colBegin = 0;
int colEnd = matrix[0].length - 1;
while (rowBegin <= rowEnd && colBegin <= colEnd) {
    from colBegin to colEnd add matrix[rowBegin][$var]) // Traverse Right
    rowBegin++;

    from rowBegin to rowEnd add matrx[$var][colEnd] // Traverse Down
    colEnd--;

    if (rowBegin <= rowEnd)  // Traverse Left
        from colEnd to colBegin add matrix[rowEnd][$var]
    rowEnd--;

    if (colBegin <= colEnd)  // Traver Up
        from rowEnd to rowBegin add matrix[$var][colBegin]
    colBegin ++;
}
```


### [53. Maximum Subarray](https://leetcode.com/problems/maximum-subarray/)

M,  Array, Dynamic Programming, Divide and Conquer

非常经典的动态规划问题，题目要求是contiguous subarray

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
记录以i为结尾的，最大的连续子数组和，应该是当前前面最大的sum + 自己，或者干脆就自立门户自己了。
```
int maxSum = nums[0], sum = 0;
for(int i = 0; i < nums.length; ++i)
    sum = Math.max(sum + nums[i], nums[i]);
    maxSum = Math.max(maxSum, sum);
return maxSum;
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

回溯的时间复杂度是O(N!) factorial 或者O(2^N) exponential。

这里N皇后是O(N!)，全排列也是O(N!)，01背包是O(2^N)，实际可以把解空间看做一个树，遍历所有到根节点。

### [50. Pow(x, n)](https://leetcode.com/problems/powx-n/)

M, Binary Search Math

对于正负数都OK的代码：
```
if (n == 0) {
    return 1;
}
double temp = myPow(x, n / 2);
if (n % 2 == 0) {
    return temp * temp;
} else {
    if (n > 0) {
        return x * temp * temp;
    } else {
        return (temp * temp) / x;
    }
}
```
以12^4为例，12^4=12^2 * 12^2 = (12^1*12^1) * (12^1*12^1) = ...

以12^5为例，12^4=12^2 * 12^2 * 12 = (12^1*12^1) * (12^1*12^1) * 12 = ...

以12^-4为例，12^-4=12^-2 * 12^-2 = (12^-1*12^-1) * (12^-1*12^-1) = (1/12*1/12) * (1/12*1/12) = ...

```
6^2 = 6^1 * 6^1 = 6 * 6 = 36
6^1 = 6 * 6^0 * 6^0 = 6 * 1 * 1 = 6
6^0 = 1

6^3 = 6 * 6^2 * 6^2

6 ^-3 = ( 6^-1 * 6^-1) / 6
```

其实，这可以看做是二分，也可以看做是分治法，下面的方法适用于整数，和大于1的幂：
```
if (n == 0)
    return 1;
else if (n == 1)
    return num;
else
    if (n % 2 == 0) {
        return powerDAC(num, n / 2) * power(num, n / 2);
    } else {
        return powerDAC(num, (n - 1) / 2) * power(num, (n - 1) / 2) * num;
    }
```

### [49. Group Anagrams](https://leetcode.com/problems/anagrams/)

M,  Hash Table String

Anagrams:由颠倒字母顺序而构成的字

很简单遍历列表，用一个散列表，key是Sting.valueOf(Arrays.sort(str.toCharArray[]))，value就是一个list，里面是单词。

然后输出散列表.values();

可以尝试使用JDK8的stream API，但是AC后的性能比较差。
```
return Arrays.stream(strs).collect(Collectors.groupingBy(s -> {
    char[] c = s.toCharArray();
    Arrays.sort(c);
    return String.valueOf(c);
})).values().stream().collect(Collectors.toList());
```

```
if (strs == null || strs.length == 0)
    return new ArrayList<List<String>>(0);
Map<String, List<String>> map = new HashMap<String, List<String>>();
//Arrays.sort(strs);
for (String s : strs) {
    char[] ca = s.toCharArray();
    Arrays.sort(ca);
    String keyStr = String.valueOf(ca);
    // 改成map.putIfAbsent(keyStr, new ArrayList<String>());也可以
    if (!map.containsKey(keyStr)) {
        map.put(keyStr, new ArrayList<String>());
    }
    map.get(keyStr).add(s);
}
return new ArrayList<List<String>>(map.values());
```

### [48. Rotate Image](https://leetcode.com/problems/rotate-image/)

M, Array

clockwise
行颠倒，对角线互换
```
1 2 3     7 8 9     7 4 1
4 5 6  => 4 5 6  => 8 5 2
7 8 9     1 2 3     9 6 3
```
或者对角线互换，列颠倒
```
1 2 3     1 4 7     7 4 1
4 5 6  => 2 5 8  => 8 5 2
7 8 9     3 6 9     9 6 3
```
互换的代码如下：
```
for (int i = 0; i < matrix.length; i++) {
    for (int j = i + 1; j < matrix[0].length; j++)
        swap(matrix, i, j, j, i);
```

anti-clockwise
列颠倒，对角线互换
或者对角线互换，行颠倒


### [47. Permutations II](https://leetcode.com/problems/permutations-ii/)

M, Backtracking

有重复数全排列问题。

和46非常类似。在继续DFS之前要判断下，第一自己跟自己比的时候不判断是否相同，只判断后面的数和固定的数比较。从start即i开始往后看，算法的基本思路是从start开始依次和后面的数字交换，然后递归全排列，后面的数字就是j，如果j在之前交换过了，就没必要做了，否则就重复了。

1,3,1,2

保证第二个1不再和第一个1交换。

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
            swap(nums, i, m);  //还原，和进来的时候一样。
        }
    }
}
```

例如1，2，3，4的结果如下，这个结果和题目31. Next Permutation的结果不一样，不是自然的排序，注意从[1, 3, 4, 2]到
[1, 4, 3, 2]是2和依次后面交换的结果，而next permutation的下一个是[1, 4, 2, 3]。

```
[1, 2, 3, 4]
[1, 2, 4, 3]
[1, 3, 2, 4]
[1, 3, 4, 2]
[1, 4, 3, 2]
[1, 4, 2, 3]
[2, 1, 3, 4]
[2, 1, 4, 3]
[2, 3, 1, 4]
[2, 3, 4, 1]
[2, 4, 3, 1]
[2, 4, 1, 3]
[3, 2, 1, 4]
[3, 2, 4, 1]
[3, 1, 2, 4]
[3, 1, 4, 2]
[3, 4, 1, 2]
[3, 4, 2, 1]
[4, 2, 3, 1]
[4, 2, 1, 3]
[4, 3, 2, 1]
[4, 3, 1, 2]
[4, 1, 3, 2]
[4, 1, 2, 3]
```

### [45. Jump Game II](https://leetcode.com/problems/jump-game-ii/)

H, Array Greedy

参考[最小生成树的算法](http://www.geeksforgeeks.org/greedy-algorithms-set-5-prims-minimum-spanning-tree-mst-2/)
的思想，其实就是贪心。每次就尽量找最远的点，最小生成树只找能加入集合V的最短的边E。

首先要明确，联想JumpGame，在位置i，最大跳nums[i]步骤，那么就是说下次最大跳到i+nums[i]的索引位置， 每一步都保存这个最大值，那么如果遍历到某个位置，发现最大的max都比这个i小，自然表示肯定跳不到这了，这个问题就没有解。

时间复杂度O(N)

```
max=nums[0]目前可以reach的最大索引位置
last=0 上次reach的最大索引位置
count=0
for i in nums
   count++
   for idx in [last+1, max]  从max往上次能跳到的最大的max看这个区间里面下次能reach的最远索引位置,不能漏过每一个，例如如果有一个的跳跃步数是1000，那么就是非常大的跳跃，也许一下就到头了。
       max = MAX(nums[i]+i, max)

   if max <= i
       return -1 原地不动了，调不到了。当然根据leetcode题目不可能出现，但是以防万一
   last = i
   i = max
return count
```

### [44. Wildcard Matching](https://leetcode.com/problems/wildcard-matching/)

H,Dynamic Programming Backtracking Greedy String

一个解如下，中间两个else if中的逻辑还是不太能理解。
```
 a b c d e f h h x  <= S
           a *           h x  <= P

过程如下，tow pointer，i，j
1）S从头开始，P从头开始
2）char相等或者P中是?，则i++，j++
3）如果没有遇到任何*，就不动了，那么肯定是不match了，直接返回false
4）如果遇到*，记录*的位置starIdx，那么j就一直固定在starIdx+1，等待这S中后面的某个char和其匹配
5）如果S往后一直没有匹配starIdx+1，那么一直走
6）直到一个匹配了，那么继续回到#1，i++，j++
7）如果下一个字符又不匹配了，如上面的例子到了h h 和 h x，后面的h和x不匹配，那么这时候j返回starIdx+1，i从x的位置也就是iIdx+1开始
8）这时候竟然匹配了，如#6，然后又回到了#1.
9）最后如果j还剩，除非是*，那么比较j==P.len判断是否match。
```

```
int i = 0;
int j = 0;
int starIndex = -1;
int iIndex = -1;

while (i < s.length()) {
    if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
        ++i;
        ++j;
    } else if (j < p.length() && p.charAt(j) == '*') {
        starIndex = j;
        iIndex = i;
        j++;
    } else if (starIndex != -1) {
        j = starIndex + 1;
        i = iIndex + 1;
        iIndex++;
    } else {
        return false;
    }
}

while (j < p.length() && p.charAt(j) == '*') {
    ++j;
}

return j == p.length();
```


### [43. Multiply Strings](https://leetcode.com/problems/multiply-strings/)

M, Math String

```
       0   1   2  i   <- index
           0   1  j   <- index
==================
       1   2   3      <- num1
           4   5      <- num2
------------------
           1   5
       1   0
   0   5
==================
       1   2
   0   8
   4
==================
0   1   2   3   4      <- 结果数组index
```
首先可以判断结果数组的长度是len1+len2，低索引存储高位，最低索引可能是0，因为没那么大。

```
for (int i = len1 - 1; i >= 0; i--) {
    for (int j = len2 - 1; j >= 0; j--) {
        int value = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
        int sum = value + res[i + j + 1];  //这个很重要，处理了低位的进位
        res[i + j] = res[i + j] + sum / 10;  //处理高位进位
        res[i + j + 1] = (sum) % 10;
    }
}
```
这里的技巧是，先计算低位，第i位*第j位相乘，结果的高位存储在(i+j)位，低位存储在(i+j+1)位。


### [42. Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/)

H, Array Stack Two Pointers

图片地址：[http://articles.leetcode.com/wp-content/uploads/2012/08/rainwatertrap.png](http://www.leetcode.com/wp-content/uploads/2012/08/rainwatertrap.png)

看起来复杂，但是其实非常简单，首先可以找到最高点，然后就分两步，遍历左边、然后右边，时间复杂度O(N)。

对于左边，维护一个当前最高的高度maxLeft（初始化为0），做最左边到最高点，如果当前高度比maxLeft小，
那么可以用maxLeft - height[index]取得当前的可以承载的水量，直到遇到又一个新高度，那么之前的水就
到头了，重新赋值maxLeft，依次按照前面的逻辑来，说白了，就是不断找高点，把水圈进去。

```
       ∏
   ∏   ∏∏ ∏
 ∏ ∏∏ ∏∏∏∏∏∏
∏∏∏∏∏∏∏∏∏∏∏∏
012345678910

例如下面的水maxHeight=3，maxHeightIndex=7，
那么左边就是从0开始，
index=0，maxLeft不变=0
index=1，maxLeft=1，
index=2，加上水量maxLeft - height[2] = 1
index=3，又是一个高点，maxLeft=2
index=4，加上水量maxLeft - height[4] = 1
index=5，加上水量maxLeft - height[5] = 2
index=6，加上水量maxLeft - height[6] = 1
所以左边的水量就是1+1+2+1=5，右边的同理
```

```
int maxHeight = find max height;
int maxHeightIdx = find max height index;
int res = 0;
int left = 0, maxLeft = 0;
int right = a.length - 1, maxRight = 0;
while (left < maxHeightIdx) {
    if (a[left] > maxLeft) maxLeft = a[left];
    else  res += maxLeft - a[left];
    left++;
}
while (right > maxHeightIdx) {
    if (a[right] > maxRight) maxRight = a[right];
    else res += maxRight - a[right];
    right--;
}
return res;
```


### [41. First Missing Positive](https://leetcode.com/problems/first-missing-positive/)

H, Array

比较难，想法难，而且处理边界条件很迷惑。第一次完全做不出来。
过程就是按照nums[i]的值安插在合适的位置，进行nums[i] <-> nums[nums[i] -
1]进行交换，大于0或者没有出界才换，否则不动i++，交换完了i不能动，还需要继续判断，知道这个位置小于0或者是合适的值了。和冒泡排序的思想有些像。
```
[3, 4, -1, 1]
 1  2   3  4    [index starts from 1]

 i
3 should be at index 3, swap index i and index 3

[-1, 4, 3, 1]
  1  2  3  4    [index starts from 1]

  i
-1 should not be in the set, ignore and i move to next

[-1, 4, 3, 1]
  1  2  3  4    [index starts from 1]

     i
4 should be at index 4, swap index i and index 4

[-1, 1, 3, 4]
  1  2  3  4    [index starts from 1]

     i
1 should be at index 1, swap index i and index 1

[1, -1, 3, 4]
 1   2  3  4    [index starts from 1]

     i
-1 should not be in the set, ignore and i move to next

[1, -1, 3, 4]
 1   2  3  4    [index starts from 1]

        i
3 is already at the right position, move i to next

[1, -1, 3, 4]
 1   2  3  4    [index starts from 1]

           i
4 is already at the right position, move i to next

we can know 2 is the first missing positive number, because 2 != -1.
```

```
for (int i = 0; i < nums.length; i++) {
    while (nums[i] > 0 && nums[i] <= nums.length && nums[nums[i] - 1] != nums[i]) {
        swap(i, nums[i] - 1, nums);
    }
}
for (int i = 0; i < nums.length; i++)
    if (nums[i] != i + 1)
        return i + 1;
return nums.length + 1;
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
和题目47不重复数组全排列有些不同，因为全排列没有先排序好，所有需要for循环来判断，从curr往最先前面看，而这个只用比邻居。

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

### [35. Search Insert Position](https://leetcode.com/problems/search-insert-position/)

M, Array Binary Search

二分查找的两种实现方式要背熟！end都是length - 1，是指index。

```
//递归查找
int searchRecursive(int[] arr, int start, int end, int target) {
    int mid = (start + end) / 2;
    if (start > end) {
        return -1;
    }
    if (arr[mid] > target) {
        return searchRecursive(arr, start, mid - 1, target);
    } else if (arr[mid] < target) {
        return searchRecursive(arr, mid + 1, end, target);
    } else {
        return mid;
    }
}

//iterative查找
int searchLoop(int[] arr, int start, int end, int target) {
    while (start <= end) {
        int mid = (start + end) >>> 1;
        int midVal = arr[mid];
        if (midVal < target) {
            start = mid + 1;
        } else if (midVal > target) {
            end = mid - 1;
        } else {
            return mid;
        }
    }
    return -1;
}

//另外可以iterative还可以写成：
int searchLoop(int[] arr, int start, int end, int target) {
    while (start < end) {
        int mid = (start + end) >>> 1;
        int midVal = arr[mid];
        if (midVal < target) {
            start = mid + 1;
        } else if (midVal > target) {
            end = mid - 1;
        } else {
            return mid;
        }
    }
    return arr[start] == target ? start : -1;
}
```

这道题解法就是判断找不到的时候返回end+1(也就是start)即可。
```
int start = 0;
int end = nums.length - 1;
if (nums[end] < target) {  //判断一下边界更好些
    return end + 1;
}
if (nums[start] > target) {
    return 0;
}
while (start <= end) {
    int mid = start + ((end - start) >> 1);
    int midVal = nums[mid];
    if (target < midVal) {
        end = mid - 1;
    } else if (target > midVal) {
        start = mid + 1;
    } else {
        return mid;
    }
}
return start;
```

### [34. Search for a Range](https://leetcode.com/problems/search-for-a-range/)

M, Binary Search Array

和35题一样在mid==target的逻辑要变下，分别找左、右的终点，注意不要溢出。
```
int left = mid;
int right = mid;
while (left > 0 && nums[left - 1] == target) {
    left--;
}
while (right < nums.length - 1 && nums[right + 1] == target) {
    right++;
}
return new int[] {left, right};
```

### [33. Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/)

H, Binary Search Array

这是数组非重复的情况，如果重复需要看题目81//TODO

考虑如下三种情况，是逐步演进的：
```
       /|
      / |
     /  |
    /   |
   /    |
  /     |
 /      |
/       |
---------
s < m < e

    /|
   / |
  /  |
 /   |
/    |
|    |  /|
|    | / |
|    |/  |
----------
s < m > e

 /|
/ |
| |     /|
| |    / |
| |   /  |
| |  /   |
| | /    |
| |/     |
----------
s > m < e

NO!
s > m > e
```
**三种情况，一二掐头/三去尾！找到合适位置lo，否则-1，注意while是lo<hi不是<=**。

对于第一种和第二种来说都是nums[lo] <= nums[mid]，第三种是nums[lo] > nums[mid]。

针对nums[lo] <= nums[mid]，缩小的范围判断是nums[lo]<=target<nums[mid]，那么就hi=mid-1，在左半部分。否则就lo=mid+1，在右半部分。

针对nums[lo] > nums[mid]，缩小的范围判断是nums[mid]<=target<nums[hi]，那么就lo=mid+1，在右半部分。否则就hi=mid-1，在左半部分。

最后判断target和nums[lo]是否相等，lo就是最合适插入的地方，不想当返回-1，找不到。
```
int lo = 0;
int hi = nums.length - 1;
while (lo < hi) {
    int mid = (lo + hi) / 2;
    if (nums[mid] == target) {
        return mid;
    }

    if (nums[lo] <= nums[mid]) {
        if (target >= nums[lo] && target < nums[mid]) {
            hi = mid - 1;
        } else {
            lo = mid + 1;
        }
    } else {
        if (target > nums[mid] && target <= nums[hi]) {
            lo = mid + 1;
        } else {
            hi = mid - 1;
        }
    }
}
return nums[lo] == target ? lo : -1;
```

### [31. Next Permutation](https://leetcode.com/problems/next-permutation/)

M, Array

如果自己可以手写出来全排列，那么就有利于分析，例如下面的1，2，3，4全排列序列，等于4!=24个。

```
[1, 2, 3, 4]
[1, 2, 4, 3]
[1, 3, 2, 4]
[1, 3, 4, 2]
[1, 4, 2, 3]
[1, 4, 3, 2]
[2, 1, 3, 4]
[2, 1, 4, 3]
[2, 3, 1, 4]
[2, 3, 4, 1]
[2, 4, 1, 3]
[2, 4, 3, 1]
[3, 1, 2, 4]
[3, 1, 4, 2]
[3, 2, 1, 4]
[3, 2, 4, 1]
[3, 4, 1, 2]
[3, 4, 2, 1]
[4, 1, 2, 3]
[4, 1, 3, 2]
[4, 2, 1, 3]
[4, 2, 3, 1]
[4, 3, 1, 2]
[4, 3, 2, 1]
```

```
1,2,3 → 1,3,2
3,2,1 → 1,2,3
1,1,5 → 1,5,1

这是个标准步骤最好记忆下来。

//1.找到最后一个升序位置pos
int pos = -1;
for (int i = num.length - 1; i > 0; i--)
    if (num[i] > num[i - 1])
        pos = i - 1;
        break;

//2.如果不存在升序，即这个数是最大的，那么反排这个数组
if (pos < 0)
    reverse(num, 0, num.length - 1);
    return;

//3.存在升序，那么找到pos之后最后一个比它大的位置
for (int i = num.length - 1; i > pos; i--)
    if (num[i] > num[pos]) {
        int tmp = num[i];
        num[i] = num[pos];
        num[pos] = tmp;
        break;

//4.反排pos之后的数
reverse(num, pos + 1, num.length - 1);

reverse用two pointer while(left < right)即可
```


### [29. Divide Two Integers](https://leetcode.com/problems/divide-two-integers/)

M, Math Binary Search

首先看核心的方法，这是非常耐人寻味的一段代码，里面有二分的思想，有递归，很经典！
```
// Recursion exit condition
if (ldividend < ldivisor) {
    return 0;
}

//  Find the largest multiple so that (divisor * multiple <= dividend),
//  whereas we are moving with stride 1, 2, 4, 8, 16...2^n for performance reason.
//  Think this as a binary search.
long sum = ldivisor;
long multiple = 1;
while ((sum + sum) <= ldividend) {
    sum += sum;
    multiple += multiple;
}

//Look for additional value for the multiple from the reminder (dividend - sum) recursively.
return multiple + ldivide(ldividend - sum, ldivisor);
```

思想大概就是：
```
按照421/3演示：
3+3=6       < 421    1+1=2个3
6+6=12      < 421    2+2=4个3
12+12=24    < 421    4+4=8个3
24+24=48    < 421    8+8=16个3
48+48=96    < 421    16+16=32个3
96+96=192   < 421    32+32=64个3
192+192=384 < 421    64+64=128个3
384+384=768 > 421
----------------
reminder is 421 - 384 = 37


3+3=6       < 37    1+1=2个3
6+6=12      < 37    2+2=4个3
12+12=24    < 37    4+4=8个3
24+24=48    > 37
----------------
reminder is 37 - 24 = 13


3+3=6       < 13    1+1=2个3
6+6=12      < 13    2+2=4个3
12+12=24    > 13
----------------
reminder is 13 - 12 = 1


3+3=6       > 1 退出

所以上面一共出现了128个3 + 8个3 + 4个3 = 140个3，所以结果就是140，这是*3之后最接近421的值。
```

然后还需要处理一些特殊的逻辑：
* 判断sign，正负号，然后两个值都取绝对值 Math.abs(..)
* 如果除数=0返回MAX_INT
* 如果被除数=0，或者被除数<除数返回0
* 算法要注意在JAVA里要用long来计算，因为没有UINT类型，会溢出，所以最后还需要判断下。



### [28. Implement strStr()](https://leetcode.com/problems/implement-strstr/)

E, Two Pointer, String

两种解法：
1）Brute force

JDK的`String.indexOf(..)`的实现就是这个

```
return 0 if needle is empty
return -1 if needle is longer than haystack
for each char in haystack[0 .. haystack.len - needle.len + 1]
    j=0
    while haystack[i+j] == target[j]
        j++
    if j == needle.len
        return char index in haystack
return -1;
```

2）KMP算法

KMP算法的重点在于每次模式串不用回到匹配串的i+1的位置去重新匹配，而是可以利用最长前缀后缀的方法来挪到i+N的位置，从而可以跳过不必要的元素。

重点在于如何求这个N，是利用一个next数组的东西，需要提前计算好，可以利用递推式搞定。

实际的KMP代码非常短小，这里暂时没有是实现。

### [27. Remove Element](https://leetcode.com/problems/remove-element/)

E, Array Two Pointers

发现元素后，思路为直接从最后的tail补一个元素上来，一个指向tail的指针索引减1。

```
int currIndex = 0
int tailIndex = nums.length - 1
while (currIndex <= tailIndex)
    if (nums[currIndex] == val)
        nums[currIndex] = nums[tailIndex--];
        continue;
    currIndex++;
return tailIndex + 1;
```


### [26. Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/)

E, Array, Two pointers

双指针，维护一个slow指针，一旦相同就i++，slow原地不动，等着大的i赋值过来，最后返回的length就是slow，时间复杂度是O(N)，如果每次都移动那么就是O(N!)。

记住i总是和slow-1比较，不相等或者大于才两个都往前进
```
       [ 1    2    3    3    3    4    4   5   6 ]
             slow
              i
                 slow
                   i
                      slow
                       i
                            i
       [ 1    2    3    4    3    4    4   5   6 ]
                            slow  i
                                       i
       [ 1    2    3    4    5    4    4   5   6 ]
                                 slow      i
       [ 1    2    3    4    5    6    4   5   6 ]
                                      slow
                                               i
```


```
if (nums.length < 2) {
    return 1;
}
int slow = 1;
for (int i = 1; i < nums.length; i++) {
    if (nums[i] != nums[slow - 1]) {
        nums[slow++] = nums[i];
    }
}
return slow;
```

### [24. Swap Nodes in Pairs](https://leetcode.com/problems/swap-nodes-in-pairs/)

E, Linked list

一次性写对，画个图什么就都知道了。

还是利用一个dummy作为开头。这里注意使用了curr.next.next.next看着恐怖，但是逻辑正确即可。

```
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode curr = dummy;
while (curr.next != null && curr.next.next != null) {
    ListNode t = curr.next.next;
    ListNode temp = curr.next.next.next;
    curr.next.next.next = curr.next;
    curr.next.next = temp;
    curr.next = t;
    curr = curr.next.next;
}
return dummy.next;
```

### [23. Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)

H, Divide and Conquer, Linked List, Heap

这道题目在分布式系统中非常常见，来自不同client的sorted list要在central server上面merge起来。这个题目一般有两种做法，下面一一介绍并且分析复杂度。

方法1：分治法

第一种做法比较容易想到，就是有点类似于MergeSort的思路,就是分治法，不了解MergeSort的朋友，请参见归并排序-维基百科，是一个比较经典的O(nlogn)
的排序算法，还是比较重要的。思路是先分成两个子任务，然后递归求子任务，最后回溯回来。这个题目也是这样，先把k个list分成两半，然后继续划分，知道剩下两个list就合并起来，合并时会用到 题目21-Merge Two Sorted
Lists这道题。
分析一下上述算法的时间复杂度。假设总共有k个list，每个list的最大长度是n，那么运行时间满足递推式T(k) = 2T(k/2)+O(n*k)。 根据主定理，可以算出算法的总复杂度是O(nklogk) 。如果不了解主定理的朋友，可以参见主定理-维基百科。空间复杂度的话是递归栈的大小O(logk)。 很多参考了net.neoremind.mycode.argorithm.sort.MergeSort的代码。 另外这个实现在leetcode上打败了79%的java代码。

```
public ListNode mergeKLists(ListNode[] lists) {
   // 处理lists为空或者长度为0的情况，否则就会StackOverFlow
   return doMergeKLists(lists, 0, lists.length - 1);
}

public ListNode doMergeKLists(ListNode[] lists, int left, int right) {
    if (left == right) {
        return lists[left];
    } else {
        int mid = left + ((right - left) >> 1);
        ListNode l1 = doMergeKLists(lists, left, mid);
        ListNode l2 = doMergeKLists(lists, mid + 1, right);
        return mergeTwoSortedList(l1, l2);  //参考题目21
    }
}
```

方法2：建堆
这种方法用到了堆的数据结构，思路比较难想到，但是其实原理比较简单。

维护一个大小为k的堆，每次去堆顶的最小元素放到结果中，然后读取该元素的下一个元素放入堆中，重新维护好。
因为每个链表是有序的，每次又是去当前k个元素中最小的，所以当所有链表都读完时结束，这个时候所有元素按从小到大放在结果链表中。

这个算法每个元素要读取一次，即是k*n次，然后每次读取元素要把新元素插入堆中要logk的复杂度，所以总时间复杂度是O(nklogk)。
空间复杂度是堆的大小，即为O(k)。

这个实现在leetcode上打败了71%的java代码。

可以用java自带的优先队列PriorityQueue，也可以自己实现一个二叉堆（Binary Heap）。

实现的模板如下：
```
// 处理空或者长度为1

PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>(lists.length, new Comparator<ListNode>() {
    @Override
    public int compare(ListNode o1, ListNode o2) {
        if (o1.val < o2.val) {
            return -1;
        } else if (o1.val > o2.val) {
            return 1;
        } else {
            return 0;
        }
    }
});

ListNode dummy = new ListNode(0);
ListNode curr = dummy;
for (ListNode node : lists) {
    if (node != null) {  //注意考虑为空的corner case
        priorityQueue.add(node);
    }
}

while (!priorityQueue.isEmpty()) {
    ListNode node = priorityQueue.poll();
    curr.next = node;
    curr = curr.next;
    if (node.next != null) {  //注意考虑为空的corner case
        priorityQueue.add(node.next);
    }
}

return dummy.next;
```

自己实现堆的一个模板如下，记住自己总结的口诀吧：
* 插入放尾，上滤，把大的爸爸挪下来，给小的新人一个合适的位置。
* 获取弹根，尾上位，下滤，把小的孩子上移上去，给大的爸爸一个合适的位置。
* 建堆，从N/2的位置开始到顶，下滤，把小的孩子上移上去，给大的爸爸一个合适的位置沉下来。

动画地址：

* [wikimedia](https://upload.wikimedia.org/wikipedia/commons/4/4d/Heapsort-example.gif)
* [usfca](http://www.cs.usfca.edu/~galles/visualization/Heap.html)


```
class BinaryHeap {
    int[] array = new int[size];
    int currentSize = 1;
    int size;

    //插入新元素n，在最后的元素位置上建立一个空穴，放入n。然后比较n和空穴父亲的值，把“大的爸爸”下移，以此类推，直到n放置到合适的位置。
    void insert(int n) {
        int hole = currentSize++;
        array[hole] = n;
        percolateUp(hole);
    }

    //上滤，应用于插入{@link #insert(int)}操作，新元素在堆中不断的上浮（找到父亲），直到找到正确的位置
    void percolateUp(int hole) {
        int tmp = array[hole];
        for (; hole > 1 && tmp < array[hole / 2]; hole = hole / 2) {
            array[hole] = array[hole / 2];
        }
        array[hole] = tmp;
    }

    //找到最小元素很容易，就是第一个元素，难的是恢复堆的性质：需要假设把最后一个元素放到第一个元素的位置，也就是根节点，然后依次下沉，把较小的孩子换上来的思想，直到到达合适的位置。
    int deleteMin() {
        int ret = array[1];
        array[1] = array[--currentSize];
        array[currentSize] = 0;
        percolateDown(1);
        return ret;
    }

    //下滤用于{@link #deleteMin()}删除和{@link #buildHeap(int[])}建堆。
    //应用堆的性质，左儿子在2i，右儿子在2i+1，父亲在i/2
    void percolateDown(int hole) {
        int tmp = array[hole];
        for (int child; hole * 2 < currentSize; hole = child) {
            child = hole * 2;
            if (child + 1 < currentSize && array[child] > array[child + 1]) {
                child++;
            }
            if (array[child] < tmp) {
                array[hole] = array[child];
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }

    //建堆，每个insert O(1)，下滤最坏情况是O(logN)，因此总的时间复杂度是O(N)，而不是O(NlogN)
    void buildHeap(int[] from) {
        if (from == null || from.length == 0) {
            throw new RuntimeException("Input array should not be empty");
        }
        this.array = new int[from.length + 2];
        System.arraycopy(from, 0, this.array, 1, from.length);
        currentSize = from.length + 1;
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
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

下面利用了string的不可变特性，也可以使用char[]。

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

### [21. Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)

E, Linked list

注意这里用一个dummy是个技巧。

```
ListNode dummy = new ListNode(0);
ListNode curr = dummy;
while (l1 != null && l2 != null) {
    if (l1.val < l2.val) {
        curr.next = l1;
        l1 = l1.next;
    } else {
        curr.next = l2;
        l2 = l2.next;
    }
    curr = curr.next;  // dont forget!
}

if (l1 != null) {
    curr.next = l1;
}
if (l2 != null) {
    curr.next = l2;
}
return dummy.next;
```


### [20. Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)

E, Stack, String

```
char[] brackets = new char[] {'[', ']'};
char[] braces = new char[] {'{', '}'};
Set<Character> openSet = new HashSet<Character>(3);
openSet.add('[');
openSet.add('{');
Stack<Character> stack = new Stack<Character>();
for (char c : s.toCharArray()) {
    if (openSet.contains(c)) {
        stack.push(c);
    } else {
        if (stack.isEmpty())
            return false;
        Character top = stack.pop();
        if (!(brackets[0] == top && c == brackets[1]) && !(braces[0] == top && c == braces[1]) ...)
            return false;
    }
}
return stack.empty();
```

### [19. Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)

E，linked list，two pointers

技巧是搞一个ListNode在head的前面

```
start=end=dummy
for (int i = 0; i <= n; i++) 
    end = end.next;  //多走一步
while (end != null) 
    start = start.next; end = end.next;
start.next = start.next.next;
return dummy.next;
```



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

//todo 二分查找会不会优化最内层循环的性能？

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
    max = MAX(area(j-i,min(h[i], h[j])) ,max)  // 注意宽度是j-i没有+1！
    if height[i] < height[j]
        i = i + 1
    if height[i] > height[j]
        j = j - 1
```

### [10. Regular Expression Matching](https://leetcode.com/problems/regular-expression-matching/)

H, Dynamic Programming Backtracking String

首先要理解题意:
```
"a"对应"a", 这种匹配不解释了
任意字母对应".", 这也是正则常见
0到多个相同字符x,对应"x*", 比起普通正则,这个地方多出来一个前缀x. x代表的是 相同的字符中取一个,比如"aaaab"对应是"a*b"
"*"还有一个易于疏忽的地方就是它的"贪婪性"要有一个限度.比如"aaa"对应"a*a", 代码逻辑不能一路贪婪到底
正则表达式如果期望着一个字符一个字符的匹配,是非常不现实的.而"匹配"这个问题,非 常容易转换成"匹配了一部分",整个匹配不匹配,要看"剩下的匹配"情况.这就很好的把 一个大的问题转换成了规模较小的问题:递归
确定了递归以后,使用java来实现这个问题,会遇到很多和c不一样的地方,因为java对字符 的控制不像c语言指针那么灵活charAt一定要确定某个位置存在才可以使用.
如果pattern是"x*"类型的话,那么pattern每次要两个两个的减少.否则,就是一个一个 的减少. 无论怎样减少,都要保证pattern有那么多个.比如s.substring(n), 其中n 最大也就是s.length()

(p.charAt(1) == '*')这个条件下需要分两种情况，p直接去掉前两个匹配，
1）s不去掉，例如s=abc，p=a*bc，下一个递归比较s=abc, p=bc，不OK
2）s一个个去掉，先去掉a，下一个递归比较s=bc, p=bc，OK了就行
```

[参考代码](http://www.programcreek.com/2012/12/leetcode-regular-expression-matching-in-java/)
```
if (p.length() == 0) {
    return s.length() == 0;
} else if (p.length() == 1) {
    if (s.length() < 1) {
        return false;
    }
    if (s.charAt(0) != p.charAt(0) && p.charAt(0) != '.') {
        return false;
    }
    return isMatch(s.substring(1), p.substring(1)); //避免aa,a的情况
} else if (p.charAt(1) == '*') {
    if (isMatch(s, p.substring(2))) {
        return true;
    }
    int i = 0;
    while (i < s.length() && (s.charAt(i) == p.charAt(0) || p.charAt(0) == '.')) {
        if (isMatch(s.substring(i + 1), p.substring(2))) {
            return true;
        }
        i++;
    }
    return false;
} else {
    if (s.length() < 1) {
        return false;
    }
    if (s.charAt(0) != p.charAt(0) && p.charAt(0) != '.') {
        return false;
    }
    return isMatch(s.substring(1), p.substring(1));
}
```


### [9. Palindrome Number](https://leetcode.com/problems/palindrome-number/)

E

Problems related with numbers are frequently solved by / and %. No need of extra space is required. This problem
is similar with the Reverse Integer problem.

Note: no extra space here means do not convert the integer to string, since string will be a copy of the integerand take extra space. The space take by div, left, and right can be ignored.

计算高位和低位是不是一样的数组，用/和%运算符即可。

```
//negative numbers are not palindrome
if (x < 0) {return false;}

// initialize how many zeros
int div = 1;
while (x / div >= 10)
    div *= 10;

while (x != 0)   // 必须等于0，不能>=10，否则无法解决10000021这种case。
    int left = x / div;
    int right = x % 10;
    if (left != right) return false;
    x = (x % div) / 10;
    div /= 100;

return true;
```

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
// 这里的k代表第几个，而不是索引，所以我们都只需要计算第几个，而不是索引，索引在比较的时候才-1.
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

和以下题目的解法模板一样，follow the same pattern！一切都是套路：
* [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)
* [76. Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/)
* [209. Minimum Size Subarray Sum，这道题更加典型](https://leetcode.com/problems/minimum-size-subarray-sum/)

```
if (s == null || s.length() == 0) { return 0; }
int[] a = new int[256];
int start = 0, end = 0;
int minLen = Integer.MIN_VALUE;
while (end < s.length())
    //if (a[s.charAt(end)] == 0) //去掉这个条件是OK的，因为后面的while保证了下一次进入循环前面一定没有重复的，还没有重复的，则增长minLen
        minLen = Math.max(minLen, end - start + 1);
    a[s.charAt(end)]++;  //记数
    end++;
    while (end < s.length() && a[s.charAt(end)] > 0)  //如果发现了重复，那么需要从start开始滑动窗口到重复的字符。
        a[s.charAt(start)]--;
        start++;
return minLen == Integer.MAX_VALUE ? 0 : minLen;
```

### [2. Add Two Numbers](https://leetcode.com/problems/add-two-numbers/)
```
低位在前逆序的
Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8
```
M, Linked list, Math

```
ListNode r = new ListNode(0); ListNode head = r; ListNode beforeEnd = r;
while (l1 != null && l2 != null)
    int value = r.val;
    value += l1.val + l2.val;
    r.next = new ListNode(value / 10);
    r.val = value % 10;
    beforeEnd = r;r = r.next;l1 = l1.next;l2 = l2.next;

ListNode left;if (l1 == null) left = l2;else left = l1;
while (left != null)
    int value = r.val;
    value += left.val;
    r.next = new ListNode(value / 10);
    r.val = value % 10;
    beforeEnd = r;r = r.next;left = left.next;

if (beforeEnd.next != null && beforeEnd.next.val == 0) {beforeEnd.next = null;}
return head;
```

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