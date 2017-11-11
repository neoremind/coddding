### [713. Subarray Product Less Than K](https://leetcode.com/problems/subarray-product-less-than-k/description/)
找到连续的subarray他们的乘积小于K，给出有多少个。

Input: nums = [10, 5, 2, 6], k = 100
Output: 8
Explanation: The 8 subarrays that have product less than 100 are: [10], [5], [2], [6], [10, 5], [5, 2], [2, 6], [5, 2, 6].
Note that [10, 5, 2] is not included as the product of 100 is not strictly less than k.

```
public int numSubarrayProductLessThanK(int[] nums, int k) {
    if (k <= 1) {
        return 0;
    }
    int res = 0;
    int product = 1;
    int left = 0;
    for (int i = 0; i < nums.length; i++) {
        product *= nums[i];
        while (product >= k) {
            product = product / nums[left++];
        }
        res += i - left + 1;
    }
    return res;
}
```

### [695. Max Area of Island](https://leetcode.com/problems/max-area-of-island/discuss/)

[200. Number of Islands](https://leetcode.com/problems/number-of-islands/)的延伸题目。

```
public int maxAreaOfIsland(int[][] grid) {
    int max_area = 0;
    for(int i = 0; i < grid.length; i++)
        for(int j = 0; j < grid[0].length; j++)
            if(grid[i][j] == 1)max_area = Math.max(max_area, AreaOfIsland(grid, i, j));
    return max_area;
}

public int AreaOfIsland(int[][] grid, int i, int j){
    if( i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == 1){
        grid[i][j] = 0;
        return 1 + AreaOfIsland(grid, i+1, j) + AreaOfIsland(grid, i-1, j) + AreaOfIsland(grid, i, j-1) + AreaOfIsland(grid, i, j+1);
    }
    return 0;
}
```

### [679. 24 Game](https://leetcode.com/problems/24-game/description/)

H, DFS, 用`*, /, +, -, (, ) `to get the value of 24.

```
Input: [4, 1, 8, 7]
Output: True
Explanation: (8-4) * (7-1) = 24
```

注意double用`Math.abs(...) < 1e-6`判断。DFS的标准写法，首先判断退出条件，然后枚举所有的可能，
找两个数字做+、-、*、/，合并成一个数字，然后对这剩下的3个数字再做同样的操作，直到只剩下一个数字，看是否等于24.
注意最后删除掉递归backtracking进去的组合数字，重新用新的组合。

```
ArrayList A = new ArrayList<Double>();
for (int v : nums) A.add((double) v);
return solve(A);

private boolean solve(ArrayList<Double> nums) {
    if (nums.size() == 0) return false;
    if (nums.size() == 1) return Math.abs(nums.get(0) - 24) < 1e-6;
    for (int i = 0; i < nums.size(); i++) {
        for (int j = 0; j < nums.size(); j++) {
            if (i != j) {
                ArrayList<Double> nums2 = new ArrayList<Double>();
                for (int k = 0; k < nums.size(); k++) {
                    if (k != i && k != j) {
                        nums2.add(nums.get(k));
                for (int k = 0; k < 4; k++) {
                    if (k < 2 && j > i) continue; /// why???
                    if (k == 0) nums2.add(nums.get(i) + nums.get(j));
                    if (k == 1) nums2.add(nums.get(i) * nums.get(j));
                    if (k == 2) nums2.add(nums.get(i) - nums.get(j));
                    if (k == 3) {
                        if (nums.get(j) != 0) nums2.add(nums.get(i) / nums.get(j)); else continue;
                    if (solve(nums2)) return true;
                    nums2.remove(nums2.size() - 1);
    return false;
}
```

### [674. Longest continuous increasing subsequence](https://leetcode.com/problems/longest-continuous-increasing-subsequence/discuss/)

和最长递增子序列(Longest Increasing Subsequence,LIS)[OK-leetcode300]比，要求连续，不用DP。
```
public int findLengthOfLCIS(int[] nums) {
    int res = 0, cnt = 0;
    for (int i = 0; i < nums.length; i++) {
        if (i == 0 || nums[i - 1] < nums[i]) res = Math.max(res, ++cnt);
        else cnt = 1;
    }
    return res;
}
```

### [639. Decode Ways II](https://leetcode.com/problems/decode-ways-ii/description/)

HARD,

和DecodeWays类似，引入了星号，可以代表1到9之间的任意数字，但是难度加大了不少，是个HARD题目。

有了星号以后，整个题就变得异常的复杂，所以结果才让我们对一个很大的数求余，避免溢出。所以对一个很大的数字取模。

精髓：各种情况都要考虑清楚。其实就是考虑1，2，26，*这些关系。别不合法。

还是DP，状态dp[i]表示前i个字符的解码方法等个数，长度为字符串的长度加1，状态转移方程的分支很多。下面介绍：
初始化： 将dp[0]初始化为1，然后我们判断，如果字符串第一个字符是0，那么直接返回0，如果是*，则dp[1]初始化为9，否则初始化为1。
一共三个分支：s[i-1]表示当前的字符
分支考虑的情况都是先看单独一个s[i-1]能否成立，然后再看s[i-2,i-1]组成一个数字是否成立，考虑1和2 + * 三种子情况。
```
分支1：s[i-1]==0 
先看单独一个s[i-1]能否成立：单独一个0肯定不行，所以不能加可能。 
然后再看s[i-2,i-1]组成一个数字是否成立： 
分支1-1：s[i-2]==1 or 2 有dp[i-2]种可能 
分支1-2：s[i-2]==* 要么为1，要么为2，所以有2*dp[i-2]种可能 
分支1-3：其他情况都return 0退出去，因为根本组成不了合法的数字。不合法。

分支2：s[i-1] := [1,9] 
先看单独一个s[i-1]能否成立：单独一个数字成立，所以先至少有dp[i-1]中可能。 
然后再看s[i-2,i-1]组成一个数字是否成立： 
分支2-1：s[i-2]==1 有dp[i-2]种可能 
分支2-2：s[i-2]==2 and s[i-1] := [1,6] 有dp[i-2]种可能 
分支2-3：s[i-2]==* 如果 s[i-1] := [1,6]那么就可以选择1或者2，就是2*dp[i-2]种可能，否则就是dp[i-2]种可能

分支3：s[i-1]==* 
先看单独一个s[i-1]能否成立：单独一个数字成立，先至少有9*dp[i-1]中可能。 
然后再看s[i-2,i-1]组成一个数字是否成立： 
分支3-1：s[i-2]==1，有9*dp[i-2]种可能 
分支3-2：s[i-2]==2 有6*dp[i-2]种可能 
分支3-3：s[i-2]==* 有（9+6）*dp[i-2]种可能

最后的结果别忘记%10^9+7,dp要是long类型的。
```

### [637. Average of Levels in Binary Tree](https://leetcode.com/problems/average-of-levels-in-binary-tree/description/)

写BFS一定要数量，注意别忘记
```
if (node.left != null) {
    q.add(node.left);
}
```
还有使用long来叠加，避免溢出。
```
Queue<TreeNode> q = new LinkedList<>();
q.add(root);
while (!q.isEmpty()) {
    long sum = 0L;
    int size = q.size();
    for (int i = 0; i < size; i++) {
        TreeNode node = q.poll();
        sum += node.val;
        if (node.left != null) {
            q.add(node.left);
        }
        if (node.right != null) {
            q.add(node.right);
        }
    }
    double res = sum * 1.0 / size;
    ret.add(res);
}
return ret;
```

### [636. Exclusive Time of Functions](https://leetcode.com/problems/exclusive-time-of-functions/description/)
```
Input:
n = 2
logs = 
["0:start:0",
 "1:start:2",
 "1:end:5",
 "0:end:6"]
Output:[3, 4]，每个index表示每个func的id，分别是0和1的执行CPU时间片段。
```

```
if (n == 0 || logs == null || logs.size() == 0) {
        return new int[]{};
    }
    Stack<Integer> stack = new Stack<>();
    int[] res = new int[n];
    int prevTime = 0;
    for (String s : logs) {
        String[] fields = s.split(":");
        int funcId = Integer.parseInt(fields[0]);
        String action = fields[1];
        int time = Integer.parseInt(fields[2]);
        if (!stack.isEmpty()) {
            res[stack.peek()] += time - prevTime;
        }
        prevTime = time;
        if (action.equals("start")) {
            stack.push(funcId);
        } else {
            res[stack.pop()]++;
            prevTime++;
        }
    }
    return res;
```

### [621. Task Scheduler](https://leetcode.com/problems/task-scheduler/description/)

给一个task列表，然后一个interval N，相同的task必须至少有N个间隔的CPU周期，问最短需要多少时间间隔完成任务。
```
Input: tasks = ["A","A","A","B","B","B"], n = 2
Output: 8
Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.
```
贪心算法，有两种解法。

解法1，数学归纳法。采用分段的方法，有些巧妙，不赘述。

解法2，使用贪心的算法。维护一个task->count的map，然后放到PriorityQueue里面，每次取出count最大的task开始执行，直到到达了N+1的大小
或者没有任何task可以执行了，这就算是一个周期结束了。往复这个过程，注意queue为空的话，证明所有任务都放进去了，所以大小不会大于N+1。

```
public int leastInterval(char[] tasks, int n) {
    if (tasks == null || tasks.length == 0) {
        return 0;
    Map<Character, Integer> task2cnt = new HashMap<>(tasks.length * 4 / 3);
    for (char task : tasks) {
        task2cnt.put(task, task2cnt.getOrDefault(task, 0) + 1);
    Queue<Map.Entry<Character, Integer>> q = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
    q.addAll(task2cnt.entrySet());
    int totalSlot = 0;
    while (!q.isEmpty()) {
        int k = n + 1;
        List<Map.Entry<Character, Integer>> temp = new ArrayList<>();
        while (k > 0 && !q.isEmpty()) {
            temp.add(q.poll());
            k--;
        }
        for (Map.Entry<Character, Integer> e : temp) {
            e.setValue(e.getValue() - 1);
            if (e.getValue() != 0) {
                q.add(e);
        if (!q.isEmpty()) {
            totalSlot += n + 1;
        } else {
            totalSlot += temp.size();
        }
    }
    return totalSlot;
}
```

扩展，如果要求保证任务顺序呢？没那么难，直接遍历数组，维护一个task上次最近执行时间的map，不够就加idle即可。

### [560. Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/description/)

和另外一道题[325. Maximum Size Subarray Sum Equals k]()很类似，

注意map.put每次都得put。
```
int sum = 0;
int result = 0;
Map<Integer, Integer> map = new HashMap<>();
map.put(0, 1);
for (int i = 0; i < nums.length; i++) {
    sum += nums[i];
    if (map.containsKey(sum - k)) {
        result += map.get(sum - k);
    map.put(sum, map.getOrDefault(sum, 0) + 1);
return result;
```

### [543. Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/discuss/)

二叉树的直径。For every node, length of longest path which pass it = MaxDepth of its left subtree + MaxDepth of its right subtree.

```
public int diameterOfBinaryTree2(TreeNode root) {
    if (root == null) {
        return 0;
    }
    int dia = depth(root.left) + depth(root.right);
    int ldia = diameterOfBinaryTree(root.left);
    int rdia = diameterOfBinaryTree(root.right);
    return Math.max(dia, Math.max(ldia, rdia));

}

public int depth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    return 1 + Math.max(depth(root.left), depth(root.right));
}
```

### [542. 01 Matrix](https://leetcode.com/problems/01-matrix/description/)

M, 有一点技巧的BFS,用DFS很难写，我的第一个解法是DFS的需要很多临时辅助变量。

BFS很合适，一个技巧是一次性把所有元素加入queue，然后置所有1为-1好比较大小。C++如下：

```
while (q.size()) {
    point temp = q.front();
    q.pop();
    int x = temp.first;
    int y = temp.second;
    for (int i=0; i<4; i++) {
        int xx = x + d[i].first;
        int yy = y + d[i].second;
        if (xx>=0 && xx<rows && yy>=0 && yy<clos && ret[xx][yy]==-1) {
            ret[xx][yy] = ret[x][y] + 1;
            q.push(point(xx,yy));
```

### [525. Contiguous Array](https://leetcode.com/problems/contiguous-array/description/)

只包含0和1，找到最长的一段，包含数目相等的0和1.和Maximum Size Subarray Sum Equals k非常类似。
```
public int findMaxLength(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] == 0) nums[i] = -1;
    }
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);
    int sum = 0;
    int res = 0;
    for (int i = 0; i < nums.length; i++) {
        sum += nums[i];
        if (map.containsKey(sum)) {
            res = Math.max(res, i - map.get(sum));
        } else {
            map.put(sum, i);
        }
    }
    return res;
}
```

### [505. The Maze II]()  

LOCK, M, 
给定一个二维迷宫，其中包含一个小球和一个洞。小球可以向上（u）、下（d）、左（l）、右（r）四个方向移动。 每当小球选择一个方向后，会持续移动直到遇到墙壁为止。小球经过洞时会落入洞中。 给定小球的初始位置ball，洞的位置hole。二维迷宫maze中1表示墙壁，0表示空地，四周被墙壁包围。 求小球到洞的最短路径.
和上一题Maze不一样的是：这道题要求最短的路径，普通的遍历dfs和bfs都是可以做的，但是求最短路径的话还是用Dijksra。

使用`Queue<Point> q = new PriorityQueue<>((o1, o2) -> o1.fromStartSteps - o2.fromStartSteps);`

使用`Map<String, Integer> map = new HashMap<>();` 存储每个点的最近距离。输出终点的那个key对应的value即可。

```
class Point {
    int x;
    int y;
    int fromStartSteps = 0;
    int fromPreSteps = 0;
```

### [499. The Maze III]()

LOCK, H, 和MazeII不一样的是，如果存在距离相等的多条路径，返回字典序最短的那条。输出路径用backtrack来做。

### [494. Target Sum](https://leetcode.com/problems/target-sum/description/)

M, 给一串数字，添加+,-符号，是的和等于给定值，问有多少种方法？
```
Input: nums is [1, 1, 1, 1, 1], S is 3. 
Output: 5
```

方法1：和Game24类似。使用DFS的模板，找两个数字合并，然后直到只剩下一个数字为止，时间复杂度2^n，exponential，会TLE。

仔细想想N皇后的时间复杂度是N!，实际因为有choice is possible的判断，所有不可能这么高，但是还是高于2^n的，N=8的时候是2057，2^8才等于256。
```
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

方法2：方法1实际是自己想出来的，但是有非常简单的DFS，不会TLE。

```
int count = 0;
public int findTargetSumWays2(int[] nums, int S) {
    calculate(nums, 0, 0, S);
    return count;

public void calculate(int[] nums, int i, int sum, int S) {
    if (i == nums.length) {
        if (sum == S)
            count++;
    } else {
        calculate(nums, i + 1, sum + nums[i], S);
        calculate(nums, i + 1, sum - nums[i], S);
```

方法3：在方法2基础上，有子问题重叠，所以加入记忆化优化。
保持一个map，key是跑到了哪个index位置，value也是一个map记录在这个位置上，家和为sum的组合有多少种。

```
public int findTargetSumWays3(int[] nums, int S) {
    Map<Integer, Map<Integer, Integer>> mem = new HashMap<>();
    return calculate(nums, 0, 0, S, mem);

public int calculate(int[] nums, int i, int sum, int S, Map<Integer, Map<Integer, Integer>> mem) {
    if (mem.containsKey(i) && mem.get(i).get(sum) != null) {
        return mem.get(i).get(sum);
    if (i == nums.length) {
        if (sum == S)
            return 1;
        else
            return 0;
    } else {
        int add = calculate(nums, i + 1, sum + nums[i], S, mem);
        int sub = calculate(nums, i + 1, sum - nums[i], S, mem);
        if (!mem.containsKey(i)) {
            mem.put(i, new HashMap<>());
        }
        mem.get(i).put(sum, mem.get(i).getOrDefault(sum, 0) + add + sub);
        return add + sub;
```

方法4：DP，有1D DP和2D DP，没理解。

方法5：转换为subset sum问题的DP，TODO。

### [490. The Maze]()

LOCK, M, 给定一个球和一个洞，问球是否可以掉进洞。迷宫的特殊之处在于，it won't stop rolling until hitting a wall。

方法1：BFS。
非常非常标准的BFS，一定要背下来。

```
先3个初始化2个步骤，然后while循环
new queue，visited，direction
queue.offer
visisted初始化
while queue not empty
    p = queue.poll
    for every direction of p
        new position based on p
        if visited continue
        mark visited
        if position is ok then do...
        else queue.offer new position
```

```
Queue<Point> q = new LinkedList<>();
boolean[][] visited = new boolean[m][n];
int[][] d = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
q.add(new Point(start[0], start[1]));
visited[start[0]][start[1]] = true;
while (!q.isEmpty()) {
    Point cur = q.poll();
    for (int i = 0; i < 4; i++) {
        Point next = getNextPoint(cur.x, cur.y, maze, m, n, d[i][0], d[i][1]);
        if (visited[next.x][next.y]) {
            continue;
        }
        visited[next.x][next.y] = true;
        if (next.x == dest[0] && next.y == dest[1]) {
            return true;
        }
        q.add(next);
    }
}
return false;

Point getNextPoint(int x, int y, int[][] maze, int m, int n, int incrX, int incrY) {
    while (x + incrX >= 0 && x + incrX < m && y + incrY >= 0 && y + incrY < n && maze[x + incrX][y + incrY] != 1) {
        x += incrX;
        y += incrY;
    return new Point(x, y);
```

方法2：DFS.非常标准的DFS。

```
初始化visited
dfs() {
  if visited false
  if ok true
  mark visited
  for every direction
     if dfs new direction
        true
  false

如果是返回所有结果的，一般是在第一个if判断里面添加结果，for every direction里面有add+dfs+remove三个连着。
```

```
boolean[][] visited = new boolean[m][n];
return dfs(maze, m, n, start, dest, visited);

public boolean dfs(int[][] maze, int m, int n, int[] start, int[] dest, boolean[][] visited) {
    if (visited[start[0]][start[1]]) return false;
    if (start[0] == dest[0] && start[1] == dest[1]) return true;
    visited[start[0]][start[1]] = true;
    for (int i = 0; i < 4; i++) {
        Point next = getNextPoint(start[0], start[1], maze, m, n, DIRECTION[i][0], DIRECTION[i][1]);
        if (dfs(maze, m, n, new int[]{next.x, next.y}, dest, visited)) {
            return true;
    return false;
```

### [477. Total Hamming Distance](https://leetcode.com/problems/total-hamming-distance/description/)
```
int len = nums.length;
int[] countOfOnes = new int[32];
for (int i = 0; i < len; i++) {
    for (int j = 0; j < 32; j++) {
        countOfOnes[j] += (nums[i] >> j) & 1;
    }
}
int sum = 0;
for (int count: countOfOnes) {
    sum += count * (len - count);
}
return sum;
```

### [472. Concatenated Words](https://leetcode.com/problems/concatenated-words/description/)

H, 复用word break。先排序，然后一个个加入preWords，word和PreWords做word break的测试

```
Input: ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]

Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
```

这里必须用word break的解法3，否则会TLE。
```
if (preWords.isEmpty()) return false;
boolean[] dp = new boolean[s.length() + 1];
dp[0] = true;
for (int i = 1; i <= s.length(); i++) {
    for (int j = 0; j < i; j++) {
        if (dp[j] && preWords.contains(s.substring(j, i))) {
            dp[i] = true;
            break;
return dp[s.length()];
```

### [464. Can I Win](https://leetcode.com/problems/can-i-win/description/)

M, 轮流拿数字，直到拿到指定的target为赢，问是否可以一定赢。和nim game类似。

一般这类题目，都可以用DFS解决，使用模板
```
dfs()
  if ok return true/false
  for every state next
     add
     if dfs() return true
     remove
  return false
```

```
boolean helper(List<Integer> numbers, int desiredTotal, boolean[] used, Map<String, Boolean> mem) {
    String key = encode(numbers);
    if (mem.containsKey(key)) {
        return mem.get(key);
    if (desiredTotal <= 0) {
        return false;
    for (int i = 0; i < numbers.size(); i++) {
        int temp = numbers.get(i);
        numbers.remove(i);
        if (!helper(numbers, desiredTotal - temp, used, mem)) {
            mem.put(key, true);
            numbers.add(i, temp);
            return true;
        numbers.add(i, temp);
    mem.put(key, false);
    return false;
```

### [463. Island Perimeter](https://leetcode.com/problems/island-perimeter/discuss/)

算岛的周长，复用[200. Number of Islands](https://leetcode.com/problems/number-of-islands/)的DFS方法。
```
public int islandPerimeter(int[][] grid) {
    if (grid == null) {
        return 0;
    int count = 0;
    int rows = grid.length;
    if (rows == 0) { //corner case
        return 0;
    int cols = grid[0].length;
    boolean[][] visited = new boolean[rows][cols];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (grid[i][j] == 1 && !visited[i][j]) {
                dfs(grid, visited, i, j, rows, cols);
                count++;
    return x;

int x = 0;

void dfs(int[][] grid, boolean[][] visited, int i, int j, int rows, int cols) {
    if (i < 0 || j < 0 || i >= rows || j >= cols || grid[i][j] == 0) {
        x++;
        return;
    if (visited[i][j]) {
        return;
    visited[i][j] = true;
    dfs(grid, visited, i + 1, j, rows, cols);
    dfs(grid, visited, i, j + 1, rows, cols);
    dfs(grid, visited, i - 1, j, rows, cols);
    dfs(grid, visited, i, j - 1, rows, cols);
}
```

一个更简单的O(N*M)的解法是：
loop over the matrix and count the number of islands;
if the current dot is an island, count if it has any right neighbour or down neighbour;
the result is islands * 4 - neighbours * 2
```
int islands = 0, neighbours = 0;
for (int i = 0; i < grid.length; i++) {
    for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == 1) {
            islands++; // count islands
            if (i < grid.length - 1 && grid[i + 1][j] == 1) neighbours++; // count down neighbours
            if (j < grid[i].length - 1 && grid[i][j + 1] == 1) neighbours++; // count right neighbours
return islands * 4 - neighbours * 2;
```

### [461. Hamming Distance](https://leetcode.com/problems/hamming-distance/description/)

计算Hamming Distance
方法1：
```
public int hammingDistance(int x, int y) {
    int dis = 0;
    for (int i = 0; i < 32; i++) {
        int m = (x >>> (31 - i)) & 1;
        int n = (y >>> (31 - i)) & 1;
        if ((m ^ n) == 1) {
            dis++;
    return dis;
}
```
方法2：
```
int xor = x ^ y, count = 0;
for (int i = 0; i < 32; i++) {
    count += (xor >> i) & 1;
}
return count;
```


### [438. Find All Anagrams in a String](https://leetcode.com/problems/find-all-anagrams-in-a-string/description/)

和Minimum Window Substring如初一折的模板，唯一不同的就是括起来的地方。
```
 Given a string S and a string T, find the minimum window in S which will contain all the characters in T in
 complexity O(n).
 For example,
 S = "ADOBECODEBANC"
 T = "ABC"
 Minimum window is "BANC".
```
```
public List<Integer> findAnagrams(String s, String p) {
    int start = 0, end = 0;
    int counter = p.length();
    int[] a = new int[256];
    for (char c : p.toCharArray()) {
        a[c]++;
    }
    List<Integer> res = new ArrayList<>();
    while (end < s.length()) {
        if (a[s.charAt(end)] > 0) {
            counter--;
        }
        a[s.charAt(end)]--;
        end++;
        //******************
        while (counter == 0 && end - start >= p.length()) {
            if (end - start == p.length()) {
                res.add(start);
            }  ******************//
            a[s.charAt(start)]++;
            if (a[s.charAt(start)] > 0) {
                counter++;
            }
            start++;
    return res;
```

### [419. Battleships in a Board](https://leetcode.com/problems/battleships-in-a-board/description/)

和number of islands非常的相似，但是存在一个非常简单的O(MN)的解法。
方法1：DFS，4个方法一条路走到黑。
```
public int countBattleships(char[][] board) {
    int m = board.length;
    int n = board[0].length;
    boolean[][] visited = new boolean[m][n];
    int count = 0;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (board[i][j] == 'X' && !visited[i][j]) {
                //System.out.println(i + " " + j);
                visited[i][j] = true;
                helper(board, visited, i + 1, j, m, n, 0);
                helper(board, visited, i - 1, j, m, n, 1);
                helper(board, visited, i, j + 1, m, n, 2);
                helper(board, visited, i, j - 1, m, n, 3);
                count++;
            }
        }
    }
    return count;
}

void helper(char[][] board, boolean[][] visited, int i, int j, int m, int n, int dir) {
    if (i < 0 || i >= m || j < 0 || j >= n || board[i][j] == '.' || visited[i][j]) {
        return;
    }
    visited[i][j] = true;
    if (dir == 0) {
        helper(board, visited, i + 1, j, m, n, dir);
    } else if (dir == 1) {
        helper(board, visited, i - 1, j, m, n, dir);
    } else if (dir == 2) {
        helper(board, visited, i, j + 1, m, n, dir);
    } else {
        helper(board, visited, i, j - 1, m, n, dir);
    }
}
```

方法2：往上和左看，如果是X，则之前计算过，跳过即可。
```
int m = board.length;
if (m==0) return 0;
int n = board[0].length;

int count=0;

for (int i=0; i<m; i++) {
    for (int j=0; j<n; j++) {
        if (board[i][j] == '.') continue;
        if (i > 0 && board[i-1][j] == 'X') continue;
        if (j > 0 && board[i][j-1] == 'X') continue;
        count++;
    }
}

return count;
```

### [415. Add strings](https://leetcode.com/problems/add-strings/)

和lc#2的两个list做加法一样，注意carry的使用和剩下的数字的相加，以及最后的0的处理。
```
if (num1 == null || num1.length() == 0) return num2;
if (num2 == null || num2.length() == 0) return num1;
StringBuilder sb = new StringBuilder();
int i = num1.length() - 1;
int j = num2.length() - 1;
int carry = 0;
while (i >= 0 && j >= 0) {
    //TODO check k1 and k2 valid
    int k1 = num1.charAt(i) - '0';
    int k2 = num2.charAt(j) - '0';
    int value = carry + k1 + k2;
    sb.append(value % 10);
    carry = value / 10;
    i--;
    j--;
}

while (i >= 0) {
    int k1 = num1.charAt(i) - '0';
    int value = carry + k1;
    sb.append(value % 10);
    carry = value / 10;
    i--;
}

while (j >= 0) {
    int k2 = num2.charAt(j) - '0';
    int value = carry + k2;
    sb.append(value % 10);
    carry = value / 10;
    j--;
}

if (carry > 0) {
    sb.append(carry);
}
return sb.reverse().toString();
```

### [398. RandomPickIndex](https://leetcode.com/problems/random-pick-index/discuss/)

蓄水池算法（Reservoir Sampling）Simple Reservoir Sampling solution。 

```
//stream代表数据流 
//reservoir代表返回长度为k的池塘
//从stream中取前k个放入reservoir；
   for ( int i = 1; i < k; i++)
       reservoir[i] = stream[i];
   for (i = k; stream != null; i++) {
       p = random(0, i);
       if (p < k)
           reservoir[p] = stream[i];
   return reservoir;
```
   
```
int[] nums;
Random rnd;

public RandomPickIndex(int[] nums) {
    this.nums = nums;
    this.rnd = new Random();
}

public int pick(int target) {
    int result = -1;
    int count = 0;
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] != target)
            continue;
        // 左开右闭
        if (rnd.nextInt(++count) == 0)
            result = i;
    }

    return result;
}
```

### [394. Decode String](https://leetcode.com/problems/decode-string/description/)

M, `s = "2[abc]3[cd]ef", return "abcabccdcdcdef".` 另外一道对应的471. Encode String with Shortest Length比较难，用DP。

```
while (i < chars.length) {
    if (Character.isDigit(c)) {
        while循环找数字
        stack.push(digit.toString());
    } else if (c == '[') {
        stack.push(String.valueOf(c));
        i++;
    } else if (c == ']') {
        第一次stack.pop();出字符串
        第二次stack.pop();出[
        第三次String number = stack.pop();出repeated次数
        再次stack.pop();出前面用于处理3[2x[ab]i]这种x开头的情况
        stack.push(repeatedStr.toString());
        i++;
    } else if (c >= 'a' && c <= 'z') {
        把字符串全部连接起来用stringbuilder
        再次stack.pop();出前面用于处理3[2[ab]i]这种i结尾的情况
        stack.push(alpha.toString());
stack全部pop出来倒序输出即可
```

### [325. Maximum Size Subarray Sum Equals k]()

HIDE, 给一个数组，找到长度最长的一个子串，其和等于K。时间复杂度要求O(N)。

如果找不大答案返回-1. 核心就是叠加sum，然后把sum存起来，到达某个i，找sum-k的index，如果存在，那么中间的这段就是要找的子串。
注意和560. Subarray Sum Equals K很像，但是mem.put不用每次都put，只put第一次就好了，因为肯定是最靠前的，因为我们要的是最长的子串。
```
int sum = 0;
int maxLen = Integer.MIN_VALUE;
Map<Integer, Integer> mem = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    sum += nums[i];
    if (sum == k) {
        maxLen = i + 1;
    } else if (mem.containsKey(sum - k)) {
        maxLen = Math.max(maxLen, i - mem.get(sum - k));
    if (!mem.containsKey(sum)) {
        mem.put(sum, i);
    }
}
return maxLen == Integer.MIN_VALUE ? -1 : maxLen;
```

### [317. Shortest Distance from All Buildings](https://leetcode.com/problems/shortest-distance-from-all-buildings)

LOCK, H 

给一个矩阵，1是building或者机床，2是block，0是可走的路，找出一个点到各个building距离最短。

```
1 - 0 - 2 - 0 - 1
|   |   |   |   |
0 - 0 - 0 - 0 - 0
|   |   |   |   |
0 - 0 - 1 - 0 - 0
```

[leetcode discussion](https://discuss.leetcode.com/topic/31925/java-solution-with-explanation-and-time-complexity-analysis)

BFS，找出所有的1，依次使用BFS搜索，更新`dist[i][j]`和`reach[i][j]`，分别表示到底此点的最短距离和是否可以达到该点。

`dist[i][j]`是1计算完，下一个1叠加的计算的，这里注意BFS要按照level来遍历，和二叉树层序遍历的思想一样，因为每一层都是一个距离。

最后遍历reach，先满足所有的点可达，然后再看dist找最小的距离使用Math.min即可。

```
for (int i = 0; i < row; i++) {
    for (int j =0; j < col; j++) {
        if (grid[i][j] == 1) {
            buildingNum++;
            Queue<int[]> myQueue = new LinkedList<int[]>();
            myQueue.offer(new int[] {i,j});
            boolean[][] isVisited = new boolean[row][col];
            int level = 1;
            while (!myQueue.isEmpty()) {
                int qSize = myQueue.size();
                for (int q = 0; q < qSize; q++) {
                    int[] curr = myQueue.poll();
                    for (int k = 0; k < 4; k++) {
                        int nextRow = curr[0] + shift[k];
                        int nextCol = curr[1] + shift[k + 1];
                        if (nextRow >= 0 && nextRow < row && nextCol >= 0 && nextCol < col
                            && grid[nextRow][nextCol] == 0 && !isVisited[nextRow][nextCol]) {
                                //The shortest distance from [nextRow][nextCol] to thic building
                                // is 'level'.
                                distance[nextRow][nextCol] += level;
                                reach[nextRow][nextCol]++;
                                isVisited[nextRow][nextCol] = true;
                                myQueue.offer(new int[] {nextRow, nextCol});
                level++;


int shortest = Integer.MAX_VALUE;
for (int i = 0; i < row; i++) {
    for (int j = 0; j < col; j++) {
        if (grid[i][j] == 0 && reach[i][j] == buildingNum) {
            shortest = Math.min(shortest, distance[i][j]);

return shortest == Integer.MAX_VALUE ? -1 : shortest;
```

### [311. Sparse Matrix Multiplication]()

矩阵乘法。
这道题让我们实现稀疏矩阵相乘，稀疏矩阵的特点是矩阵中绝大多数的元素为0，而相乘的结果是还应该是稀疏矩阵，即还是大多数元素为0，那么我们使用传统的矩阵相乘的算法肯定会处理大量的0乘0 的无用功，所以我们需要适当的优化算法，使其可以顺利通过OJ，我们知道一个 i x k 的矩阵A乘以一个 k x j 的矩阵B会得到一个 i x j 大小的矩阵C，那么我们来看结果矩阵中的某个元素C[i][j]是怎么来的，起始是A[i][0]*B[0][j] + A[i][1]*B[1][j] + ... + A[i][k]*B[k][j]，那么为了不重复计算0乘0，我们首先遍历A数组，要确保A[i][k]不为0，才继续计算，然后我们遍历B矩阵的第k行，如果B[K][J]不为0，我们累加结果矩阵res[i][j] += A[i][k] * B[k][j]; 这样我们就能高效的算出稀疏矩阵的乘法，参见代码如下

```
for (int i = 0; i < m; i++) {
    for (int k = 0; k < n; k++) {
        if (A[i][k] != 0) {
            for (int j = 0; j < nB; j++) {
                if (B[k][j] != 0) C[i][j] += A[i][k] * B[k][j];
```

扩展问题，如果要做矩阵的dot product呢？
类似SearchA2DMatrix问题，把矩阵化作一个一维的数组，然后相乘。

### [301. Remove Invalid Parentheses](https://leetcode.com/problems/remove-invalid-parentheses/)

H, 删除"最少的"括号
```
"()())()" -> ["()()()", "(())()"]
"(a)())()" -> ["(a)()()", "(a())()"]
")(" -> [""]
```

BFS解法，每移除一个括号都看做一种状态，实际就是一个N!个可能性的树搜索问题。
```
         ()())()
        /        \       \ \ \ \ \ \
      )())()    (())()  .....
        |     \ 
      ())() )))()
```

```
List<String> res = new ArrayList<>();
Queue<String> q = new LinkedList<>();
q.offer(s);
Set<String> visited = new HashSet<>();
// found这个标志位很重要，用于保证题目中删除"最少的字符"的要求，
// 否则就会产生很多的解法，包括(())(), ()()(), (()), ()(), ()
// 只需要找到了之后把剩下的再队列中的元素检查完即可。
boolean found = false;
while (!q.isEmpty()) {
    String str = q.poll();
    if (visited.contains(str)) continue;
    visited.add(str);
    if (isValid(str)) 
        res.add(str); found = true;
    if (found) continue;
    for (int i = 0; i < str.length(); i++) 
        if (str.charAt(i) != '(' && str.charAt(i) != ')') continue;
        String temp = str.substring(0, i) + str.substring(i + 1);
        q.offer(temp);
}
return res;

/**
 * 复用valid parentheses题目的解法，加入判断非([{等符号的字符continue判断
 */
public boolean isValid(String s) 
```

BFS解法，非常不推荐，因为会不断的往下走，题目的要求是删除"最少的"使字符串合法。

```
public List<String> removeInvalidParenthesesDfs(String s) {
    List<String> res = new ArrayList<>();
    if (s == null || s.length() == 0) {
        res.add("");
        return res;
    }

    Set<String> visited = new HashSet<>();
    dfs(s, res, visited);
    if (res.isEmpty()) {
        return res;
    int max = -1;
    for (String str: res) {
        max = Math.max(max, str.length());
    final int maxLen = max;
    return res.stream().filter(str -> str.length() == maxLen).collect(Collectors.toList());
}

void dfs(String s, List<String> res, Set<String> visited) {
    if (visited.contains(s)) {
        return;
    visited.add(s);
    if (s.length() == 0) {
        res.add(s);
        return;
    } else if (isValid(s)) {
        res.add(s);
    } else {
        for (int k = 0; k < s.length(); k++) {
            if (s.charAt(k) != '(' && s.charAt(k) != ')') continue;
            String str = s.substring(0, k) + s.substring(k + 1);
            dfs(str, res, visited);
        }
```

### [304. Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/)
```
Given matrix = [
  [3, 0, 1, 4, 2],
  [5, 6, 3, 2, 1],
  [1, 2, 0, 1, 5],
  [4, 1, 0, 1, 7],
  [1, 0, 3, 0, 5]
]

查询区域
2，0，1
1，0，1
0，3，0
sumRegion(2, 1, 4, 3) -> 8

其他如下
sumRegion(1, 1, 2, 2) -> 11
sumRegion(1, 2, 2, 4) -> 12
```
画图即可：
```
To calculate sums, the ideas as below

+-----+-+-------+     +--------+-----+     +-----+---------+     +-----+--------+
|     | |       |     |        |     |     |     |         |     |     |        |
|     | |       |     |        |     |     |     |         |     |     |        |
+-----+-+       |     +--------+     |     |     |         |     +-----+        |
|     | |       |  =  |              |  +  |     |         |  -  |              |
+-----+-+       |     |              |     +-----+         |     |              |
|               |     |              |     |               |     |              |
|               |     |              |     |               |     |              |
+---------------+     +--------------+     +---------------+     +--------------+

   sums[i][j]      =    sums[i-1][j]    +     sums[i][j-1]    -   sums[i-1][j-1]   +  matrix[i-1][j-1]

So, we use the same idea to find the specific area's sum.

+---------------+   +--------------+   +---------------+   +--------------+   +--------------+
|               |   |         |    |   |   |           |   |         |    |   |   |          |
|   (r1,c1)     |   |         |    |   |   |           |   |         |    |   |   |          |
|   +------+    |   |         |    |   |   |           |   +---------+    |   +---+          |
|   |      |    | = |         |    | - |   |           | - |      (r1,c2) | + |   (r1,c1)    |
|   |      |    |   |         |    |   |   |           |   |              |   |              |
|   +------+    |   +---------+    |   +---+           |   |              |   |              |
|        (r2,c2)|   |       (r2,c2)|   |   (r2,c1)     |   |              |   |              |
+---------------+   +--------------+   +---------------+   +--------------+   +--------------+
```

注意要初始化一个len+1的dp二维数组。
```
class NumMatrix {
    int[][] dp;
    public NumMatrix(int[][] matrix) {
        if (matrix.length == 0) {
            return;
        int m = matrix.length;
        int n = matrix[0].length;
        dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1] + matrix[i - 1][j - 1];
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (dp == null) {
            return 0;
        int iMin = Math.min(row1, row2) + 1;
        int iMax = Math.max(row1, row2) + 1;
        int jMin = Math.min(col1, col2) + 1;
        int jMax = Math.max(col1, col2) + 1;
        return dp[iMax][jMax] - dp[iMin - 1][jMax] - dp[iMax][jMin - 1] + dp[iMin - 1][jMin - 1];
    }
}
```

### [303. Range Sum Query - Immutable](https://leetcode.com/problems/range-sum-query-immutable/description/)

```
class NumArray {
    int[] sum;
    public NumArray(int[] nums) {
        sum = new int[nums.length + 1];
        if (nums.length != 0) {
            for (int i = 1; i <= nums.length; i++) {
                sum[i] = sum[i - 1] + nums[i - 1];
    }

    public int sumRange(int i, int j) {
        if (i == 0) {
            return sum[j + 1];
        return sum[j + 1] - sum[i];
    }
}
```

### [297. Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/description/)

H, 序列化和反序列化二叉树

方法1：使用queue来做，按照层序来遍历。

https://discuss.leetcode.com/topic/31264/short-and-straight-forward-bfs-java-code-with-a-queue

```
 *      4
 *    /   \
 *   2     6
 *  / \   / \
 * 1   3 5   7
```
序列化为`4,2,6,1,3,5,7,#,#,#,#,#,#,#,#,`

```
if (root == null) return "";
Queue<TreeNode> q = new LinkedList<>();
StringBuilder res = new StringBuilder();
q.add(root);
while (!q.isEmpty()) {
    TreeNode node = q.poll();
    if (node == null) 
        res.append("#,");
        continue;
    res.append(node.val + ",");
    q.add(node.left);
    q.add(node.right);
}
return res.toString();

/////////

if (data == "") return null;
Queue<TreeNode> q = new LinkedList<>();
String[] values = data.split(",");
TreeNode root = new TreeNode(Integer.parseInt(values[0]));
q.add(root);
for (int i = 1; i < values.length; i++) {
    TreeNode parent = q.poll();
    if (!values[i].equals("#")) {
        TreeNode left = new TreeNode(Integer.parseInt(values[i]));
        parent.left = left;
        q.add(left);
    }
    if (!values[++i].equals("#")) {
        TreeNode right = new TreeNode(Integer.parseInt(values[i]));
        parent.right = right;
        q.add(right);
    }
}
return root;
```

方法2：

https://discuss.leetcode.com/topic/28029/easy-to-understand-java-solution

上面的二叉树会序列化为`4,2,1,#,#,3,#,#,6,5,#,#,7,#,#,`

```
// 递归的preorder
public String serialize2(TreeNode root) 
    StringBuilder sb = new StringBuilder();
    buildString(root, sb);
    return sb.toString();

private void buildString(TreeNode node, StringBuilder sb) 
    if (node == null) {
        sb.append("#").append(",");
    } else {
        sb.append(node.val).append(",");
        buildString(node.left, sb);
        buildString(node.right, sb);


//非递归的preorder
public String serialize2_(TreeNode root) {
    if (root == null) return null;
    Stack<TreeNode> stack = new Stack<TreeNode>();
    stack.push(root);
    StringBuilder sb = new StringBuilder();
    while (!stack.isEmpty()) {
        TreeNode h = stack.pop();
        if (h != null) {
            sb.append(h.val + ",");
            stack.push(h.right);
            stack.push(h.left);
        } else {
            sb.append("#,");
    return sb.toString();
}

public TreeNode deserialize2(String data) {
    Deque<String> nodes = new LinkedList<>();
    nodes.addAll(Arrays.asList(data.split(",")));
    return buildTree(nodes);

private TreeNode buildTree(Deque<String> nodes) {
    String val = nodes.poll();
    if (val.equals("#")) return null;
    else {
        TreeNode node = new TreeNode(Integer.valueOf(val));
        node.left = buildTree(nodes);
        node.right = buildTree(nodes);
        return node;
```

### [294. Flip Game II]

M，和can i win类似的题目。

```
public boolean canWin(String s) {
    for (int i = 1; i < s.length(); i++) {
        if (s.charAt(i) == '+' && s.charAt(i - 1) == '+' && !canWin(s.substring(0, i - 1) + "--" + s.substring(i + 1))) {
            return true;
    return false;
}
```

### [293. Flip Game]

E，下一个状态的所有选择。

```
for (int i = 1; i < s.length(); i++) {
    if (s.charAt(i) == '+' && s.charAt(i - 1) == '+') {
        res.add(s.substring(0, i - 1) + "--" + s.substring(i + 1));
```

### [292. Nim Game](https://leetcode.com/articles/nim-game/)

E, 两个人玩游戏，在一堆石头里面捡石子，可以捡1，2，3个石子，
假设两个人都是clever and have optimal strategies的策略，问当还剩几个石子的时候，我肯定可以赢。

这和Can I win以及flip game II很类似，都是有点技巧的游戏类型题目。

方法1：DFS，会TLE
```
public boolean canWinNim(int n) {
    if (n <= 3) return true;
    for (int i = 1; i <= 3; i++) {
        if (!canWinNim(n - i)) {
            return true;
        }
    }
    return false;
}
```

方法2：DP，一般这类题目能用DFS，都能用DP来解决，避免重复的计算。
```
public boolean canWinNim2(int n) {
    if (n <= 3) return true;
    boolean[] dp = new boolean[n + 1];
    dp[1] = true;
    dp[2] = true;
    dp[3] = true;
    for (int i = 4; i <= n; i++) {
        for (int j = 1; j <= 3; j++) {
            if (!dp[i - j]) {
                dp[i] = true;
            }
        }
    }
    return dp[n];
}
```

DP可以用bit map来解决space问题。
```
public boolean canWinNim3(int n) {
    if (n <= 3) return true;
    int[] dp = new int[n / 32 + 1];
    encode(1, dp);
    encode(2, dp);
    encode(3, dp);
    for (int i = 4; i <= n; i++) {
        for (int j = 1; j <= 3; j++) {
            if (!isSet(i - j, dp)) {
                encode(i, dp);
    return isSet(n, dp);
}

void encode(int n, int[] dp) {
    dp[n / 32] |= 1 << (n % 32);

boolean isSet(int x, int[] dp) {
    return (dp[x / 32] & (1 << (x % 32))) != 0;
```

方法3：最简单的归纳法。
```
public boolean canWinNim4(int n) {
    return n % 4 != 0;
}
```

### [286. Walls and Gates](https://leetcode.com/problems/walls-and-gates)

LOCK, M, 给一个下面的矩阵
```
INF  -1  0  INF
INF INF INF  -1
INF  -1 INF  -1
  0  -1 INF INF
```
0是gate，-1是block，INF是INT_MAX，把所有的INF设置成最近可以到达的gate的距离。

```
3  -1   0   1
2   2   1  -1
1  -1   2  -1
0  -1   3   4
```

和`SurroundedRegions`类似的思想，不要从INF开始DFS或者BFS，而是从0-gate开始。

```
for (int i = 0; i < m; i++) 
    for (int j = 0; j < n; j++) 
        if (rooms[i][j] == 0) 
            dfs(rooms, i, j, m, n, 0);
            
private void dfs(int[][] rooms, int i, int j, int m, int n, int step) {
    if (i < 0 || i >= m || j < 0 || j >= n || rooms[i][j] == -1) 
        return;
    if (rooms[i][j] >= step) { //INT_MAX肯定要设置值，如果有别的gate到某个点的距离更短也设置，再DFS下去。
        rooms[i][j] = step;
        dfs(rooms, i + 1, j, m, n, step + 1);
        dfs(rooms, i - 1, j, m, n, step + 1);
        dfs(rooms, i, j + 1, m, n, step + 1);
        dfs(rooms, i, j - 1, m, n, step + 1);
    }
} 
```

### [285. inorder successor in bst]()
```
TreeNode successor(TreeNode root, TreeNode p) {
    if (root == null) {
        return null;
    }
    if (root.val <= p.val) {
        return successor(root.right, p);
    } else {
        TreeNode left = successor(root.left, p);
        return left != null ? left : root;
    }
}

TreeNode predecessor(TreeNode root, TreeNode p) {
    if (root == null) {
        return null;
    }
    if (root.val >= p.val) {
        return predecessor(root.left, p);
    } else {
        TreeNode right = predecessor(root.right, p);
        return right != null ? right : root;
    }
}
```

### [280. Wiggle Sort]()

方法1：排序，然后取2个对调，步长+2

方法2：找规律。
当i为奇数时，nums[i] >= nums[i - 1]

当i为偶数时，nums[i] <= nums[i - 1]

那么我们只要对每个数字，根据其奇偶性，跟其对应的条件比较，如果不符合就和前面的数交换位置即可。

### [278. First Bad Version](https://leetcode.com/problems/first-bad-version/description/)

二分查找模板。
```
int m = 1;
while (m < n) {
    int mid = m + ((n - m) >>> 1);
    if (isBadVersion(mid)) {
        n = mid;
    } else {
        m = mid + 1;
    }
}
return m;
```

### [273. Integer to English Words](https://leetcode.com/problems/integer-to-english-words/description/)
```
String[] less20 = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
        "Eighteen", "Nineteen"};
String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
String[] thousands = {"", "Thousand", "Million", "Billion"};


public String numberToWords(int num) {
    if (num == 0) {
        return "Zero";
    int i = 0;
    String str = "";
    while (num > 0) {
        if (num % 1000 != 0) {
            str = helper(num % 1000) + thousands[i] + " " + str;
        }
        num /= 1000;
        i++;
    return str.trim();
}

public String helper(int num) {
    if (num == 0) {
        return "";
    } else if (num < 20) {
        return less20[num % 20] + " ";
    } else if (num >= 20 && num < 100) {
        return tens[num / 10] + " " + helper(num % 10);
    } else if (num >= 100 && num < 1000) {
        return less20[num / 100] + " Hundred " + helper(num % 100);
    }
    throw new RuntimeException("should not go here");
}
```

### [270. Closest Binary Search Tree Value]()

方法1：in-order遍历，一个一个的比较，维护一个最小值，不停的更新，直到找到临界点。 

方法2：下面这种递归的写法和上面迭代的方法思路相同，都是根据二分搜索树的性质来优化查找，但是递归的写法用的是回溯法，先遍历到叶节点，然后一层一层的往回走，把最小值一层一层的运回来 

方法3：和方法2类似的二分思想，实际我们可以利用二分搜索树的特点(左<根<右) 来快速定位，由于根节点是中间值，我们在往下遍历时，我们根据目标值和根节点的值大小关系来比较，如果目标值小于节点值，则我们应该找更小的值，于是我们到左子树去找，反之我们去右子树找
```
int closestValue(TreeNode root, double target) {
    if (root == null) {
        return 0;
    }
    Stack<TreeNode> stack = new Stack<>();
    TreeNode curr = root;
    boolean flag = false;
    TreeNode pre = null;
    while (curr != null || !stack.isEmpty()) {
        if (curr != null) {
            stack.push(curr);
            curr = curr.left;
        } else {
            TreeNode node = stack.pop();
            if (flag) {
                if (target >= pre.val && target <= node.val) {
                    return Math.abs(target - pre.val) < Math.abs(target - node.val) ? pre.val : node.val;
                }
            } else {
                flag = true;
            }
            pre = node;
            curr = node.right;
        }
    }
    return pre.val;
}

int closestValue2(TreeNode root, double target) {
    if (root == null) {
        return 0;
    }
    TreeNode child = root.val < target ? root.right : root.left;
    if (child == null) {
        return root.val;
    }
    int childVal = closestValue2(child, target);
    return Math.abs(target - childVal) < Math.abs(target - root.val) ? childVal : root.val;
}

int closestValue3(TreeNode root, double target) {
    int ret = root.val;
    while (root != null) {
        if (Math.abs(target - root.val) < Math.abs(target - ret)) {
            ret = root.val;
        }
        if (root.val > target) {
            root = root.left;
        } else {
            root = root.right;
        }
    }
    return ret;
}
```

### [261. graph valid tree]()
判断输入的边是否能构成一个树，我们需要确定两件事：
* 这些边是否构成环路，如果有环则不能构成树
* 这些边是否能将所有节点连通，如果有不能连通的节点则不能构成树

```
boolean validTree(int n, int[][] nodes) {
    UF uf = new UF(n);
    for (int i = 0; i < nodes.length; i++) {
        int x = uf.find(nodes[i][0]);
        int y = uf.find(nodes[i][1]);
        if (x == y) {
            return false;
        uf.union(nodes[i][0], nodes[i][1]);
    }
    return uf.cnt == 0;
}

static class UF {
    int[] w;
    int[] a;
    int cnt = 0;

    public UF(int n) {
        w = new int[n];
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
            w[i] = 1;
        }
        cnt = n - 1;
    }

    int find(int p) {
        while (p != a[p]) {
            p = a[p];
        }
        return p;
    }

    void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;
        } else {
            if (w[rootP] < w[rootQ]) {
                a[rootP] = rootQ;
                w[rootQ] += w[rootP];
            } else {
                a[rootQ] = rootP;
                w[rootP] += w[rootQ];
            }
        }
        cnt--;
    }
}
```

### [238. Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/description/)
除自己外的其他元素的乘积，O(N)时间复杂度解决。
```
int n = nums.length;
int[] res = new int[n];
res[0] = 1;
for (int i = 1; i < n; i++) {
    res[i] = res[i - 1] * nums[i - 1];
}
int right = 1;
for (int i = n - 1; i >= 0; i--) {
    res[i] *= right;
    right *= nums[i];
}
return res;
```

### [203. Remove Linked List Elements](https://leetcode.com/problems/remove-linked-list-elements/description/)

E, 
```
Example
Given: 1 --> 2 --> 6 --> 3 --> 4 --> 5 --> 6, val = 6
Return: 1 --> 2 --> 3 --> 4 --> 5
```

方法1：非常的简单，递归的方式。
```
public ListNode removeElements(ListNode head, int val) {
    if (head == null) return null;
    head.next = removeElements(head.next, val);
    return head.val == val ? head.next : head;
}
```

方法2：尾递归的方式。

```
public ListNode removeElementsTailRec(ListNode head, int val) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    helper(dummy, val);
    return dummy.next;

private ListNode helper(ListNode node, int val) {
    if (node == null) {
        return null;
    } else {
        ListNode temp = node.next;
        while (temp != null && temp.val == val) {
            temp = temp.next;
        }
        node.next = temp;
        return helper(temp, val);
```

### [161. One Edit Distance]()

```
public boolean isOneEditDistance(String s, String t) {
    if (Math.abs(s.length() - t.length()) > 1) {
        return false;
    if (s.length() == t.length()) {
        int diff = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != t.charAt(i)) {
                diff++;
        return diff == 1;
    } else if (s.length() > t.length()) {
        return helper(s, t);
    } else {
        return helper(t, s);

 * s is longer
private boolean helper(String s, String t) {
    for (int i = 0; i < t.length(); i++) {
        if (s.charAt(i) != t.charAt(i)) {
            return s.substring(i + 1).equals(t.substring(i));
    return true;
```

### [158. Read N Characters Given Read4 II - Call multiple times]()

这道题是之前那道Read N Characters Given Read4的拓展，那道题说read函数只能调用一次，而这道题说read函数可以调用多次，那么难度就增加了，为了更简单直观的说明问题，我们举个简单的例子吧，比如：
buf = "ab", [read(1),read(2)]，返回 ["a","b"]

那么第一次调用read(1)后，从buf中读出一个字符，那么就是第一个字符a，然后又调用了一个read(2)，想取出两个字符，但是buf中只剩一个b了，所以就把取出的结果就是b。再来看一个例子：

buf = "a", [read(0),read(1),read(2)]，返回 ["","a",""]

第一次调用read(0)，不取任何字符，返回空，第二次调用read(1)，取一个字符，buf中只有一个字符，取出为a，然后再调用read(2)，想取出两个字符，但是buf中没有字符了，所以取出为空。

```
char[] prevBuf = new char[4];
int prevSize = 0;
int prevIndex = 0;

public int read(char[] buf, int n) {
    int counter = 0;
    while (counter < n) {
        if (prevIndex < prevSize) {
            buf[counter++] = prevBuf[prevIndex++];
        } else {
            prevSize = read4(prevBuf);
            prevIndex = 0;
            if (prevSize == 0) {
                // no more data to consume from stream
                break;
    return counter;
}
```

### [157. Read N Characters Given Read4]()

这道题给了我们一个Read4函数，每次可以从一个文件中最多读出4个字符，如果文件中的字符不足4个字符时，
返回准确的当前剩余的字符数。现在让我们实现一个最多能读取n个字符的函数。

```
public int read(char[] buf, int n) {
    char[] buffer = new char[4];
    boolean endOfFile = false;
    int readBytes = 0;

    while (readBytes < n && !endOfFile) {
        int size = read4(buffer);
        if (size != 4) {
            endOfFile = true;
        }
        int length = Math.min(n - readBytes, size);
        for (int i = 0; i < length; i++) {
            buf[readBytes + i] = buffer[i];
        }
        readBytes += length;
    }
    return readBytes;
}
```

### [138. Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/description/)

方法1：使用map存储老node->新的node的映射。时间O(N)，空间O(N)

方法2：老node中间插入新的node，空间会优化。
 
```
public RandomListNode copyRandomList(RandomListNode head) {
  if (head == null) return null;
  
  Map<RandomListNode, RandomListNode> map = new HashMap<RandomListNode, RandomListNode>();
  
  // loop 1. copy all the nodes
  RandomListNode node = head;
  while (node != null) {
    map.put(node, new RandomListNode(node.label));
    node = node.next;
  }
  
  // loop 2. assign next and random pointers
  node = head;
  while (node != null) {
    map.get(node).next = map.get(node.next);
    map.get(node).random = map.get(node.random);
    node = node.next;
  }
  
  return map.get(head);
}
```

### [131. Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/description/)

M, aab分割成所有的回文串，标准的backtracking。复用isPalindrome那道题的字符串。

```
void helper(String s, int index, int len, List<List<String>> res, List<String> temp) {
    if (index == len) {
        res.add(new ArrayList<>(temp));
    } else {
        for (int i = index; i < len; i++) {
            String sub = s.substring(index, i + 1);
            if (isPalindrome(sub)) {
                temp.add(sub);
                helper(s, i + 1, len, res, temp);
                temp.remove(temp.size() - 1);
```


### [103. Binary Tree Zigzag Level Order Traversal](https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/)

M, BFS，就是层序遍历树，用一个order标志位标记是正序还是倒序。

```
LList<List<Integer>> res = new ArrayList<>();
if (root == null) return res;
boolean order = true;
Queue<TreeNode> q = new LinkedList<>();
q.offer(root);
while (!q.isEmpty()) 
    int size = q.size();
    List<Integer> subList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
        TreeNode t = q.poll();
        if (order) subList.add(t.val); else subList.add(0, t.val);
        if (t.left != null)  q.offer(t.left);
        if (t.right != null) q.offer(t.right);
    }
    res.add(subList);
    order = order ? false : true;
return res;
```