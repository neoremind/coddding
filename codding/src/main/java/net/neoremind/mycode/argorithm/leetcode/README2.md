## DFS


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
|        \         \ \ \ \ \ \
)())()  (())()  .....
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

### [280. Wiggle Sort]()

方法1：排序，然后取2个对调，步长+2

方法2：找规律。
当i为奇数时，nums[i] >= nums[i - 1]

当i为偶数时，nums[i] <= nums[i - 1]

那么我们只要对每个数字，根据其奇偶性，跟其对应的条件比较，如果不符合就和前面的数交换位置即可。

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

### [158. Read N Characters Given Read4 II - Call multiple times]

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

### [157. Read N Characters Given Read4]

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