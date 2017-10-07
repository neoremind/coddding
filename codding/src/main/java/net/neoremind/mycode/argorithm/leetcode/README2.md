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