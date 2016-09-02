# Leetcode records

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
String left = s.substring(0, length / 2);
String right = s.substring(length / 2, length);
return reverse(right) + reverse(left);
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
    if (stX >= edX || stY >= edY)
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

### [179. Largest Number](https://leetcode.com/problems/largest-number/)

M, Array

因此不要陷入数字计算的陷阱里，而是想着用字符串比较或者使用jdk的函数式编程思想

```
String[] array = Arrays.stream(num).mapToObj(String::valueOf).toArray(String[]::new);
Arrays.sort(array, (String s1, String s2) -> (s2 + s1).compareTo(s1 + s2));
return Arrays.stream(array).reduce((x, y) -> x.equals("0") ? y : x + y).get();
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

### [151. Reverse Words in a String](https://leetcode.com/problems/reverse-words-in-a-string/)

M, String

利用reverse string 344题的方法，步骤如下：
```
* 1. trim leading, trailing spaces
* 2. handle s.length == 0
* 3. trim inner multiple spaces
    // barrier - 1 is the trimmed ending index
    // like 316题 RemoveDuplicatesFromSortedArray
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
int left = 1;
int right = x;
while (left <= right) {
    int mid = left + (right - left) / 2;
    if (mid <= x / mid) {
        left = mid + 1;
        ans = mid;
    } else {
        right = mid - 1;
    }
}
return ans;
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

怎么把这个4041000还原回来看这个帖子吧，[点击此](http://leetcode.tgic.me/permutation-sequence/index.html)。

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

可以尝试使用JDK8的stream API。

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

### [35. Search Insert Position](https://leetcode.com/problems/search-insert-position/)

M, Array Binary Search

二分查找的两种实现方式要背熟！end都是length - 1，是指index。

```
//递归查找
private int searchRecursive(int[] arr, int start, int end, int target) {
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

这道题解法就是判断找不到的时候返回end+1即可。
```
if (start > end)
    return end + 1
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


### [26. Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/)

E, Array, Two pointers

双指针，维护一个slow指针，一旦相同就i++，slow原地不动，等着大的i赋值过来，最后返回的length就是slow，时间复杂度是O(N)，如果每次都移动那么就是O(N!)。

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
    if (node != null) {
        priorityQueue.add(node);
    }
}

while (!priorityQueue.isEmpty()) {
    ListNode node = priorityQueue.poll();
    curr.next = node;
    curr = curr.next;
    if (node.next != null) {
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
    curr = curr.next;
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