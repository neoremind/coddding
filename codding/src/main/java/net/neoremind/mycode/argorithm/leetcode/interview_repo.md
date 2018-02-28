
- [DFS/BFS](#dfsbfs)
- [Backtracking](#backtracking)
- [Dynamic programming](#dynamic-programming)
- [Divide and Conquer & Heap](#divide-and-conquer--heap)
- [Hashtable](#hashtable)
- [Array](#array)
- [Two pointers](#two-pointers)
- [String](#string)
- [Tree](#tree)
- [Binary Search](#binary-search)
- [Sort](#sort)
- [Graph](#graph)
- [Union Find](#union-find)
- [Trie](#trie)
- [Greedy](#greedy)
- [Math](#math)
- [Linked list](#linked-list)

## DFS/BFS

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

初始化visited
dfs() {
  if visited false
  if ok true
  mark visited
  for every direction
     if dfs new direction
        true
  false
  
dfs()
  if ok return true/false
  for every state next
     add
     if dfs() return true
     remove
  return false
```

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

非常经典的[Maze](https://github.com/neoremind/coddding/blob/master/codding/src/main/java/net/neoremind/mycode/argorithm/leetcode/README2.md#490-the-maze)/[NQueens](https://github.com/neoremind/coddding/blob/master/codding/src/main/java/net/neoremind/mycode/argorithm/leetcode/README.md#51-n-queens)解题都是利用的上面的模板。

### NQueens

```
public List<List<String>> solveNQueens(int n) {
    char[][] chessboard = new char[n][n];
    for (int i = 0; i < chessboard.length; i++) {
        for (int j = 0; j < chessboard[0].length; j++) {
            chessboard[i][j] = '.';
        }
    }
    List<List<String>> result = new ArrayList<>();
    solveNQueens(chessboard, 0, result);
    return result;
}

public void solveNQueens(char[][] chessboard, int n, List<List<String>> result) {
    if (n == chessboard.length) {
        result.add(chessboardToString(chessboard));
    } else {
        for (int i = 0; i < chessboard[0].length; i++) {
            if (isValid(chessboard, n, i)) {
                chessboard[n][i] = 'Q';
                solveNQueens(chessboard, n + 1, result);
                chessboard[n][i] = '.';
            }
        }
    }
}

public List<String> chessboardToString(char[][] chessboard) {
    List<String> result = new ArrayList<>(chessboard.length);
    for (int i = 0; i < chessboard.length; i++) {
        result.add(new String(chessboard[i]));
    return result;
}

public boolean isValid(char[][] chessboard, int x, int y) {
    int rowLen = chessboard.length;
    int colLen = chessboard[0].length;
    // check single column
    for (int i = 0; i < chessboard.length; i++) {
        if (chessboard[i][y] == 'Q') {
            return false;
        }
    }
    // no need to check single row
    // check 45 diagonal
    for (int i = x, j = y; i >= 0 && j >= 0; i--, j--) {
        if (chessboard[i][j] == 'Q') {
            return false;
        }
    }
    for (int i = x, j = y; i < rowLen && j < colLen; i++, j++) {
        if (chessboard[i][j] == 'Q') {
            return false;
        }
    }
    // check 135 diagonal
    for (int i = x, j = y; i < rowLen && j >= 0; i++, j--) {
        if (chessboard[i][j] == 'Q') {
            return false;
        }
    }
    for (int i = x, j = y; i >= 0 && j < colLen; i--, j++) {
        if (chessboard[i][j] == 'Q') {
            return false;
        }
    }

    return true;
}
```

### Maze

```
/**
 * BFS
 */
public boolean canSolve(int[][] maze, int[] start, int[] dest) {
    if (maze == null || maze.length == 0) return false;
    if (start[0] == dest[0] && start[1] == dest[1]) return true;
    int m = maze[0].length;
    int n = maze.length;
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
}
```
```
/**
 * DFS
 */
public boolean canSolve2(int[][] maze, int[] start, int[] dest) {
    if (maze == null || maze.length == 0) return false;
    if (start[0] == dest[0] && start[1] == dest[1]) return true;
    int m = maze[0].length;
    int n = maze.length;
    boolean[][] visited = new boolean[m][n];
    return dfs(maze, m, n, start, dest, visited);
}

public boolean dfs(int[][] maze, int m, int n, int[] start, int[] dest, boolean[][] visited) {
    if (visited[start[0]][start[1]]) return false;
    if (start[0] == dest[0] && start[1] == dest[1]) return true;
    visited[start[0]][start[1]] = true;
    for (int i = 0; i < 4; i++) {
        Point next = getNextPoint(start[0], start[1], maze, m, n, DIRECTION[i][0], DIRECTION[i][1]);
        if (dfs(maze, m, n, new int[]{next.x, next.y}, dest, visited)) {
            return true;
        }
    }
    return false;
}
```

### 补充一个时间复杂度的推导
当有探索的时候。（比如word search）
http://www.1point3acres.com/bbs/thread-139380-1-1.html
```
T(N) = 4 * T(N-1)
     = 4 * 4 * T(N-2)
     = 4 * 4 * 4 * T(N-3)
     = 4 * 4 * 4 * T(N-4)
     = 4^N * T(1)
```

### Number of islands
```
DFS:
Time complexity : O(M×N) where M is the number of rows and N is the number of columns.

Space complexity : worst case O(M×N) in case that the grid map is filled with lands where DFS goes by M×N deep.

如果采用BFS:
Time complexity : OO(M×N) where M is the number of rows and N is the number of columns.

Space complexity : O(min(M,N)) because in worst case where the grid is filled with lands, the size of queue can grow up to min(M,N).
```
```
public int numIslands(char[][] grid) {
    if (grid == null) {
        return 0;
    }
    int count = 0;
    int rows = grid.length;
    if (rows == 0) { //corner case
        return 0;
    }
    int cols = grid[0].length;
    boolean[][] visited = new boolean[rows][cols];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (grid[i][j] == '1' && !visited[i][j]) {
                dfs(grid, visited, i, j, rows, cols);
                count++;
            }
        }
    }
    return count;
}

void dfs(char[][] grid, boolean[][] visited, int i, int j, int rows, int cols) {
    if (i < 0 || j < 0 || i >= rows || j >= cols || grid[i][j] == '0' || visited[i][j]) {
        return;
    }
    visited[i][j] = true;
    dfs(grid, visited, i + 1, j, rows, cols);
    dfs(grid, visited, i, j + 1, rows, cols);
    dfs(grid, visited, i - 1, j, rows, cols);
    dfs(grid, visited, i, j - 1, rows, cols);
}
```

算最大面积，[200. Number of Islands](https://leetcode.com/problems/number-of-islands/)的延伸题目。

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

Shifting Matrix
一个m x n 的 array 只有 0 和 1  给一个 int k 
需要把 小于 k 数量 连续的 1 变成 0
连续： 上下左右和四个斜线方向
这题说的是，当island的面积小于k的时候，把1翻成0

思路：先做dfs来计算面积并把1翻成2，如果面积小于k，再做一次dfs把2翻成0. 最后再把所有的2翻成0
这题直接bfs就可以了。union find的精髓在于要不断的merge，图是动态变化的，eg：number of islands ii那道题有新的点加进来。

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

### Print All Paths in a 2D Board
```
public class Solution {
    private int m;
    private int n;
    public int numIslands(char[][] grid) {
        int count=0;
        m=grid.length;
        if(m==0) return 0;
        n=grid[0].length;
        dfs(grid,0,0,"");
    }
    public void dfs(char[][] grid,int i,int j,String s){
        if(i>m||j>n) return;
        if(i==m&&j==n) print(s+grid[i][j]);
        dfs(grid,i,j+1,s+grid[i][j]+"->");
        dfs(grid,i+1,j,s+grid[i][j]+"->");
    }
}
```

follow up是如果board很大，怎么优化。答用stringbuilder
```
public List<String> printAllPath(char[][] board){
    List<String> res = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    dfs(board,res,sb,0,0);
    return res;
}
public void dfs(char[][] b, List<String> res, StringBuilder sb, int x, int y){
    int len = sb.length();
    sb.append(b[y][x]);
    
    if( x == b[0].length - 1 && y == b.length - 1){
        String s = sb.toString();
        res.add(s);
    } else{
        if( x + 1 < b[0].length) dfs(b,res,sb,x+1,y);
        if( y + 1 < b.length ) dfs(b,res,sb,x,y+1); 
    }
    sb.setLength(len);  
}
```

### Battleships in a Board

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

### Robot and room area
题目是给你一个机器人和一个房间，你并不知道机器人在房间什么位置，你也不知道房间的形状大小，你有一个遥控器，
可以让机器人走前后左右四个方向。这里给你一个move method : boolean move(int direction), direction: 0,1,2,3 表示四个方向。
能移动就返回true,不能移动表示false。问你：这个房间多大。

```
public int areaDFS(int i, int j) {
    Set<String> visited = new HashSet<>();
    dfs(i, j, visited);
    return visited.size();
}

void dfs(int i, int j, Set<String> visited) {
    if (visited.contains(encode(i, j))) {
        return;
    }
    visited.add(encode(i, j));
    for (int k = 0; k < 4; k++) {
        if (move(i, j, k)) {
            if (k == 0) {
                dfs(i - 1, j, visited);
            } else if (k == 1) {
                dfs(i, j - 1, visited);
            } else if (k == 2) {
                dfs(i + 1, j, visited);
            } else if (k == 3) {
                dfs(i, j + 1, visited);
            }
        }
    }
}

// 有优化，提前判断不放到queue里面
public int areaBFS(int i, int j) {
    Set<String> visited = new HashSet<>();
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{i, j});
    while (!queue.isEmpty()) {
        int[] xy = queue.poll();
        int currI = xy[0];
        int currJ = xy[1];
        if (visited.contains(encode(currI, currJ))) {
            continue;
        }
        visited.add(encode(currI, currJ));
        for (int k = 0; k < 4; k++) {
            if (move(currI, currJ, k)) {
                if (k == 0) {
                    queue.offer(new int[]{currI - 1, currJ});
                } else if (k == 1) {
                    queue.offer(new int[]{currI, currJ - 1});
                } else if (k == 2) {
                    queue.offer(new int[]{currI + 1, currJ});
                } else if (k == 3) {
                    queue.offer(new int[]{currI, currJ + 1});
                }
            }
        }
    }
    return visited.size();
}
```

### LetterCombinationsPhoneNumber

回溯法 + DFS，类似于笛卡尔乘积的方式

```
final char[][] charMap = {
        {}, //0
        {}, //1
        {'a', 'b', 'c'}, //2
        {'d', 'e', 'f'}, //3
        {'g', 'h', 'i'}, //4
        {'j', 'k', 'l'}, //5
        {'m', 'n', 'o'}, //6
        {'p', 'q', 'r', 's'}, //7
        {'t', 'u', 'v'}, //8
        {'w', 'x', 'y', 'z'} //9
};

public List<String> letterCombinations(String digits) {
    if (digits == null || digits.length() == 0) {
        return Collections.emptyList();
    }
    List<String> res = new ArrayList<>();
    char[] combination = new char[digits.length()];
    find(res, combination, 0, digits);
    return res;
}

public void find(List<String> res, char[] combination, int index, String digits) {
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

### restore ip address

重点考虑：
1、测试用例，需要考虑01不符合要求。
2、int i = index + 1; i < index + 4是所有选择条件。
3、templist.size() > 4迅速剪枝退出。
```
public List<String> restoreIpAddresses(String s) {
    if (s == null || s.length() == 0) {
        return Collections.emptyList();
    List<String> res = new ArrayList<>();
    helper(res, s, 0, new ArrayList<>());
    return res;
}

void helper(List<String> res, String s, int index, List<Integer> temp) {
    if (index == s.length() && temp.size() == 4) {
        res.add(toIpString(temp));
    } else if (temp.size() > 4) {
        return;
    } else {
        for (int i = index + 1; i < index + 4 && i <= s.length(); i++) {
            String sub = s.substring(index, i);
            if (isValid(sub)) {
                temp.add(Integer.parseInt(sub));
                helper(res, s, i, temp);
                temp.remove(temp.size() - 1);
            }
        }
    }
}

boolean isValid(String s) {
    if (s.startsWith("0") && s.length() > 1) return false;
    Integer num = Integer.parseInt(s);
    return num >= 0 && num < 256) 
}

String toIpString(List<Integer> temp) {
    return temp.stream().map(String::valueOf).collect(Collectors.joining("."));
```

### 24 Game

You have 4 cards each containing a number from 1 to 9. 
You need to judge whether they could operated through *, /, +, -, (, ) to get the value of 24.
```
Example 1:
Input: [4, 1, 8, 7]
Output: True
Explanation: (8-4) * (7-1) = 24
```

题目其实给了很宽松的条件，可以有（），因此不用考虑DFS加法或者减法的结合律问题。简单了很多
思路就是找两个数字做+、-、*、/，合并成一个数字，然后对这剩下的3个数字再做同样的操作，直到只剩下一个数字，看是否等于24。
标准DFS模板：
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
public boolean judgePoint24(int[] nums) {
    ArrayList A = new ArrayList<Double>();
    for (int v : nums) A.add((double) v);
    return solve(A);
}

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
                    if (k == 0) nums2.add(nums.get(i) + nums.get(j));
                    if (k == 1) nums2.add(nums.get(i) * nums.get(j));
                    if (k == 2) nums2.add(nums.get(i) - nums.get(j));
                    if (k == 3) {
                        if (nums.get(j) != 0) {
                            nums2.add(nums.get(i) / nums.get(j));
                        } else {
                            continue;
                        }
                    }
                    if (solve(nums2)) return true;
                    nums2.remove(nums2.size() - 1);
                }
    return false;
}
```

### Target Sum

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

可以想象一个多层的搜索树结构，每一层如果上面累计的sum相同，那么下面的数量肯定一样，就避免重复计算。

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

### Remove Invalid Parentheses

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
public boolean isValid(String s) {
    char[] brackets = new char[] {'[', ']'};
    char[] braces = new char[] {'{', '}'};
    char[] parentheses = new char[] {'(', ')'};
    Set<Character> openSet = new HashSet<Character>(3);
    openSet.add('[');
    openSet.add('{');
    openSet.add('(');
    Stack<Character> stack = new Stack<Character>();
    for (char c : s.toCharArray()) {
        if (openSet.contains(c)) {
            stack.push(c);
        } else {
            if (stack.isEmpty()) {
                return false;
            }
            Character top = stack.pop();
            if (!(brackets[0] == top && c == brackets[1])
                    && !(braces[0] == top && c == braces[1])
                    && !(parentheses[0] == top && c == parentheses[1])) {
                return false;
            }
        }
    }
    return stack.empty();
}
```

DFS解法，非常不推荐，因为会不断的往下走，题目的要求是删除"最少的"使字符串合法。

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
http://www.1point3acres.com/bbs/thread-192179-1-1.html
```
public String addToValid(String s) {
    s = delR(s);
    System.out.println(s);
    s = delL(s);
    return s;
}

private String delR(String s) {
    int l = 0;
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray()) {
        sb.append(c);
        if (c == '(') {
            l++;
        } else if (c == ')') {
            if (l > 0) l--;
            else {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }
    return sb.toString();
}


private String delL(String s) {
    int l = 0;
    StringBuilder sb = new StringBuilder();
    for (int i = s.length() - 1; i >= 0; i--) {
        char c = s.charAt(i);
        sb.insert(0, c);
        if (c == ')') {
            l++;
        } else if (c == '(') {
            if (l > 0) l--;
            else sb.deleteCharAt(0);
        }
    }
    return sb.toString();
}
```

### word search

注意问是否允许重复使用字母，如果不允许就必须维护一个visited。

```
public boolean exist(char[][] board, String word) {
    if (word == null || word.length() == 0) return true;
    int row = board.length;
    int col = board[0].length;
    boolean[][] visited = new boolean[row][col]; //放在这里，因为回溯所以复用
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
            if (dfs(board, visited, word, row, col, i, j, 0)) {
                return true;
    return false;
}

int[][] d = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

boolean dfs(char[][] board, boolean[][] visited, String word, int row, int col, int i, int j, int index) {
    if (index == word.length()) {
        return true;
    if (i < 0 || i >= row || j < 0 || j >= col || visited[i][j]) {
        return false;
    if (word.charAt(index) != board[i][j]) {
        return false;
    visited[i][j] = true;
    for (int k = 0; k < 4; k++) {
        if (dfs(board, visited, word, row, col, i + d[k][0], j + d[k][1], index + 1)) {
            return true;
    visited[i][j] = false;
    return false;
}
```

### surrounded region
dfs
```
public void solve(char[][] board) {
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
            if (i == 0 || i == board.length - 1 || j == 0 || j == board[0].length - 1) {
                if (board[i][j] == 'O') {
                    dfs(i, j, board);
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
            if (board[i][j] == '*') {
                board[i][j] = 'O';
            } else {
                board[i][j] = 'X';

private void dfs(int i, int j, char[][] board) {
    if (i >= board.length || i < 0 || j >= board[0].length || j < 0 || board[i][j] == 'X' || board[i][j] == '*') {
        return;
    board[i][j] = '*';
    dfs(i + 1, j, board);
    dfs(i - 1, j, board);
    dfs(i, j + 1, board);
    dfs(i, j - 1, board);
```
bfs
```
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < columns; j++) {
        if ((i == 0 || i == rows - 1 || j == 0 || j == columns - 1) && board[i][j] == 'O') {
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
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < columns; j++) {
        if (board[i][j] == 'B') {
            board[i][j] = 'O';
        } else if (board[i][j] == 'O') {
            board[i][j] = 'X';
```

### walls and gates
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
和SurroundedRegions类似的思想，不要从INF开始DFS或者BFS，而是从0-gate开始。
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

bfs
```
public void wallsAndGates3(int[][] rooms) {
    for (int i = 0; i < rooms.length; i++) {
        for (int j = 0; j < rooms[0].length; j++) {
            if (rooms[i][j] == 0)
                bfs(rooms, i, j);

int[][] dir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

private void bfs(int[][] rooms, int i, int j) {
    Queue<int[]> q = new LinkedList<>();
    q.add(new int[]{i, j});
    while (!q.isEmpty()) {
        int[] xy = q.poll();
        for (int k = 0; k < 4; k++) {
            int m = xy[0] + dir[k][0];
            int n = xy[1] + dir[k][1];
            if (m >= 0 && m < rooms.length && n >= 0 && n < rooms[0].length &&
                    rooms[m][n] > rooms[xy[0]][xy[1]] + 1) {
                rooms[m][n] = rooms[xy[0]][xy[1]] + 1;
                q.add(new int[]{m, n});
```

### palindrome partitioning

Given a string s, partition s such that every substring of the partition is a palindrome.
Return all possible palindrome partitioning of s.
For example, given s = "aab",
[
 ["aa","b"],
 ["a","a","b"]
]

```
public List<List<String>> partition(String s) {
    List<List<String>> res = new ArrayList<>();
    helper(s, 0, s.length(), res, new ArrayList<>());
    return res;
}

void helper(String s, int index, int len, List<List<String>> res, List<String> temp) {
    if (index == len) {
        res.add(new ArrayList<>(temp));
    } else {
        for (int i = index; i < len; i++) {
            String sub = s.substring(index, i + 1);
            if (isP(sub)) {
                temp.add(sub);
                helper(s, i + 1, len, res, temp);
                temp.remove(temp.size() - 1);
            }
        }
    }
}

boolean isP(String s) {
    if (s == null || s.length() == 0) return true;
    if (s.length() == 1) return true;
    char[] chars = s.toCharArray();
    int left = 0;
    int right = chars.length - 1;
    while (left < right) {
        if (chars[left++] != chars[right--]) {
            return false;
        }
    }
    return true;
}
```

## Backtracking

### Permutation

无重复数全排列问题。全排列的算法是一个基础，排列算法在它的基础上增加了选数过程（select），即先选后排。全排列有N!种组合。

时间复杂度，n^2 < 2^n < n!，2^n是subsets，n!是全排列。

```
private void doPermute(int[] nums, int index, List<List<Integer>> result) {
    if (index == nums.length) {
        List<Integer> numsList = new ArrayList<>(nums.length);
        for (int num : nums) {
            numsList.add(num);
        }
        result.add(numsList);
        System.out.println(numsList);
    } else {
        for (int j = index; j < nums.length; j++) {
            if (isNotSame(nums, index, j)) {
                swap(nums, index, j);
                doPermute(nums, index + 1, result);
                swap(nums, index, j);
            }
        }
    }
}

/**
 * 这个函数是个技巧，第一自己跟自己比的时候不判断是否相同，只判断后面的数和固定的数比较
 *
 * 这个方法会挪动到最后的那个1再开始全排列。
 */
private boolean isNotSame(int[] nums, int index, int j) {
    for (int k = index; k < j; k++) {
        if (nums[k] == nums[j]) {
            return false;
        }
    }
    return true;
}
```

### subset 
子集，需要先排序好！！！！！结果数量正好是2次幂。

```
T(N) = T(N-1) + T(N-2) + T(N-3) + ... T(1)
     = 2 [T(N-2) + T(N-3) + ... T(1)]
     = 2 * 2 * [T(N-3) + T(N-4)... T(1)]
     = 2^n * T(1)
     = 2^n
所以时间复杂度O(2^N)
```

```
public List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> list = new ArrayList<>();
    Arrays.sort(nums); //!!!!!
    dfs(list, new ArrayList<>(), nums, 0);
    return list;

private void dfs(List<List<Integer>> list, List<Integer> tempList, int[] nums, int start) {
    list.add(new ArrayList<>(tempList));
    for (int i = start; i < nums.length; i++) {
        tempList.add(nums[i]);
        dfs(list, tempList, nums, i + 1);
        tempList.remove(tempList.size() - 1);
```

解决重复，加入条件
```
if (i > start && nums[i] == nums[i - 1]) {
    continue;
```

扩展combinations，combination sums三个问题。

给一个数组和一个target，从数组中找一些“组合”，注意不是“排列”，使这些数字的和等于target。

这道题目允许数字重复利用。

given candidate set [2, 3, 6, 7] and target 7, A solution set is:
```
[
  [7],
  [2, 2, 3]
]
```
注意这里三点：
退出条件是要不然相等、要不然减过头了。
必须先排序！一是为了加和考虑，二来为40题中解决重复数字的问题。
tempList.remove(tempList.size() - 1)这个技巧是删除最后一个元素，需要回溯到之前的状态。
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
```

```
For example, given candidate set [10, 1, 2, 7, 6, 1, 5] and target 8,
 * A solution set is:
 * [
 * [1, 7],
 * [1, 2, 5],
 * [2, 6],
 * [1, 1, 6]
 * ]
```
如果条件改成不允许重复使用数字，那么在遍历每一个数字的时候加一个条件，首先start根自己不能算重复，之和前面的，要好好理解。
```
if (i > start && nums[i] == nums[i - 1]) {
    continue; // skip duplicates
}
```
否则就会出现：
```
[1, 1, 6]
[1, 2, 5]
[1, 7]
[1, 2, 5]
[1, 7]
[2, 6]
```


## Dynamic programming

dynamic programming is a method for solving a complex problem by breaking it down into a collection of simpler subproblems.

最优子结构、无后效性、子问题重叠（not neccessary）

状态的定义+状态转移方程+初值

### coin change
```
if (coins == null || coins.length == 0 || amount == 0) {
    return 0;
int[] dp = new int[amount + 1];
for (int i = 1; i <= amount; i++) {
    dp[i] = Integer.MAX_VALUE;
    for (int j = 0; j < coins.length; j++) {
        if (coins[j] <= i && dp[i - coins[j]] != Integer.MAX_VALUE) {
            dp[i] = Integer.min(dp[i], 1 + dp[i - coins[j]]);
if (dp[amount] == Integer.MAX_VALUE) {
    return -1;
} else {
    return dp[amount];
```

### word break

given s = "leetcode", dict = ["leet", "code"].
```
boolean[] dp = new boolean[s.length() + 1];
dp[0] = true;
for (int i = 1; i <= s.length(); i++) {
    for (int j = 0; j < i; j++) {
        if (dp[j] && wordDict.contains(s.substring(j, i))) {
            dp[i] = true;
            break;
return dp[s.length()];
```

如果集合里面的字符串很少就用下面的。
```
boolean[] dp = new boolean[s.length() + 1];
dp[0] = true;
for (int i = 0; i < s.length(); i++) {
    if (!dp[i]) {
        continue;
    for (String word : wordDict) {
        int end = i + word.length();
        if (end <= s.length() && s.substring(i, end).equals(word)) {
            dp[end] = true;
}
return dp[s.length()];
```

第二问找出所有的可能组合，加上backtracking。
given s = "catsanddog", dict = ["cat", "cats", "and", "sand", "dog"]. A solution is ["cats and dog", "cat sand dog"].
```
public List<String> wordBreak(String s, List<String> wordDict) {
    List<String> res = new ArrayList<>();
    boolean[] dp = new boolean[s.length() + 1];
    MultiStep[] steps = new MultiStep[s.length() + 1];
    dp[0] = true;
    for (int i = 1; i <= s.length(); i++) {
        for (int j = 0; j < i; j++) {
            if (dp[j] && wordDict.contains(s.substring(j, i))) {
                dp[i] = true;
                if (steps[i] == null) {
                    steps[i] = new MultiStep();
                }
                steps[i].list.add(new Step(j, s.substring(j, i)));
                // 没有break了！！！
            }
        }
    }
    if (!dp[s.length()]) {
        return res;
    }
    helper(steps[s.length()], res, new ArrayList<>(), steps);
    return res;
}

void helper(MultiStep ms, List<String> res, List<String> temp, MultiStep[] steps) {
    if (ms == null) {
        List<String> copy = new ArrayList<>(temp);
        Collections.reverse(copy);
        res.add(copy.stream().collect(Collectors.joining(" ")));
        return;
    }
    for (int i = 0; i < ms.list.size(); i++) {
        Step step = ms.list.get(i);
        temp.add(step.s);
        helper(steps[step.prev], res, temp, steps);
        temp.remove(temp.size() - 1);
    }
}

class MultiStep {
    List<Step> list = new ArrayList<>();
}

class Step {
    int prev;
    String s;

    Step(int prev, String s) {
        this.prev = prev;
        this.s = s;
    }
}
```

扩展ConcatenatedWords，如果单词很多，就先排序，然后依次调用word break的canBuild。还可以用Trie树解决，但是很难写，有很多标记位，所以不要用Trie来做。
A concatenated word is defined as a string that is comprised entirely of at least two shorter words in the given
array.Example:Input: ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]

```
public List<String> findAllConcatenatedWordsInADict(String[] words) {
    List<String> wordList = new ArrayList<>(words.length);
    for (String word : words) {
        wordList.add(word);
    }
    Collections.sort(wordList, (w1, w2) -> (w1.length() - w2.length()));
    List<String> preWords = new ArrayList<>(words.length);
    List<String> res = new ArrayList<>();
    for (String word : wordList) {
        if (canBuild(word, preWords)) {
            res.add(word);
        }
        preWords.add(word);
    }
    return res;
}
```

### Decode ways

```
'A' -> 1
'B' -> 2
...
'Z' -> 26
```
数字有多少种decode的方法，比如12，有两种，分别是"AB" (1 2) or "L" (12).

递推式
```
d[i] = (d[i-2] if s.sub[i-2, i] >= '10' && <= '26' else 0) + d[i-1] if s.substring[i-1,i] >= '1' & <= '9'
       1  i=0
       1  i=1
```

```
if (s == null || s.isEmpty() || s.charAt(0) == '0') {
    return 0;
int[] dp = new int[s.length() + 1];
dp[0] = 1;
dp[1] = 1;

for (int i = 2; i <= s.length(); i++) {
    int num = Integer.parseInt(s.substring(i - 2, i));
    int twoStepsBehind = (num <= 26 && num >= 10) ? dp[i - 2] : 0;
    int oneStepBehind = (Integer.parseInt(s.substring(i - 1, i)) != 0) ? dp[i - 1] : 0;
    dp[i] = twoStepsBehind + oneStepBehind; // can reach here by either hopping 2 steps or 1 step

return dp[s.length()];
```

### Maximum Subarray 最大连续子序列和[53. Maximum Subarray]

状态：s[i]表示以i结尾最大的子数组和，如果s[i - 1]小于0了，则说明在i肯定是保留现在的值更大，否则就叠加前面的s[i - 1]

状态转移方程：s[i] = Max{v[i] (s[i - 1] < 0), s[i - 1] + v[i]}

```
Find the contiguous subarray within an array (containing at least one number) which has the largest sum.

For example, given the array [-2,1,-3,4,-1,2,1,-5,4],
the contiguous subarray [4,-1,2,1] has the largest sum = 6.
```

```
int[] dp = new int[nums.length];
dp[0] = nums[0];
for (int i = 1; i < nums.length; i++) {
    if (dp[i - 1] < 0) {
        dp[i] = nums[i];
    } else {
        dp[i] = dp[i - 1] + nums[i];
return IntStream.of(dp).max().getAsInt();
```

O(1)的DP：
```
int maxSum = array[0];
int sum = 0;
for (int i = 0; i < array.length; ++i) {
    sum = Math.max(sum + array[i], array[i]);
    maxSum = Math.max(maxSum, sum);
return maxSum;
```

### 最长递增子序列(Longest Increasing Subsequence,LIS)[OK-leetcode300]

```
Given an unsorted array of integers, find the length of longest increasing subsequence.

For example,
Given [10, 9, 2, 5, 3, 7, 101, 18],
The longest increasing subsequence is [2, 3, 7, 101], therefore the length is 4. Note that there may be more than one LIS combination, it is only necessary for you to return the length.

Your algorithm should run in O(n2) complexity.

Follow up: Could you improve it to O(n log n) time complexity?
```

返回长度即可。

状态：d(i)表示到i位置的最长递增子序列的长度。
状态转移方程：d(i) = max{1, d(j) + 1},其中j<i,A[j]<=A[i]

```
int[] longest = new int[nums.length];
for (int i = 0; i < nums.length; i++) {
    longest[i] = 1;
}
for (int i = 0; i < nums.length; i++) {
    for (int j = 0; j < i; j++) {
        if (nums[i] > nums[j] && longest[i] < longest[j] + 1) {
            longest[i] = longest[j] + 1;
        }
    }
}
return IntStream.of(longest).max().getAsInt();  //JDK8
```

```
下面我们来看一种优化时间复杂度到O(nlgn)的解法，这里用到了二分查找法，所以才能加快运行时间哇。思路是，我们先建立一个数组ends，把首元素放进去，然后比较之后的元素，如果遍历到的新元素比ends数组中的首元素小的话，替换首元素为此新元素，如果遍历到的新元素比ends数组中的末尾元素还大的话，将此新元素添加到ends数组末尾(注意不覆盖原末尾元素)。如果遍历到的新元素比ends数组首元素大，比尾元素小时，此时用二分查找法找到第一个不小于此新元素的位置，覆盖掉位置的原来的数字，以此类推直至遍历完整个nums数组，此时ends数组的长度就是我们要求的LIS的长度，特别注意的是ends数组的值可能不是一个真实的LIS，比如若输入数组nums为{4, 2， 4， 5， 3， 7}，那么算完后的ends数组为{2， 3， 5， 7}，可以发现它不是一个原数组的LIS，只是长度相等而已，千万要注意这点。参见代码如下：
if (nums.empty()) return 0;
vector<int> ends{nums[0]};
for (auto a : nums) {
    if (a < ends[0]) ends[0] = a;
    else if (a > ends.back()) ends.push_back(a);
    else {
        int left = 0, right = ends.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (ends[mid] < a) left = mid + 1;
            else right = mid;
        }
        ends[right] = a;
    }
}
return ends.size();
```

### Longest continuous increasing subsequence
和最长递增子序列(Longest Increasing Subsequence,LIS)[OK-leetcode300]比，要求连续，不用DP。
```
Given an unsorted array of integers, find the length of longest continuous increasing subsequence.

Example 1:
Input: [1,3,5,4,7]
Output: 3
Explanation: The longest continuous increasing subsequence is [1,3,5], its length is 3. 
Even though [1,3,5,7] is also an increasing subsequence, it's not a continuous one where 5 and 7 are separated by 4. 
Example 2:
Input: [2,2,2,2,2]
Output: 1
Explanation: The longest continuous increasing subsequence is [2], its length is 1. 
Note: Length of the array will not exceed 10,000.

public int findLengthOfLCIS(int[] nums) {
    int res = 0, cnt = 0;
    for (int i = 0; i < nums.length; i++) {
        if (i == 0 || nums[i - 1] < nums[i]) res = Math.max(res, ++cnt);
        else cnt = 1;
    }
    return res;
}
```

还有一个Number of Longest Increasing Subsequence，需要两个DP数组。

### Maximum Product Subarray

```
暴力
public int maxProduct2(int[] nums) {
    if (nums == null || nums.length == 0) {
        return 0;
    }
    int res = nums[0];
    for (int i = 0; i < nums.length; i++) {
        int max = nums[i];
        int p = nums[i];
        for (int j = i + 1; j < nums.length; j++) {
            p *= nums[j];
            max = Math.max(p, max);
        }
        res = Math.max(max, res);
    }
    return res;
}

DP
max * newnumber, min*newnumber, newnumber这三个肯定会出现max和min就得判断下。
public int maxProduct3(int[] nums) {
    if (nums.length == 1) {
        return nums[0];
    }
    // dp[i] = max(max[i - 1] * nums[i], min[i - 1] * nums[i], nums[i])
    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    int[] max = new int[nums.length];
    max[0] = nums[0];
    int[] min = new int[nums.length];
    min[0] = nums[0];
    for (int i = 1; i < nums.length; i++) {
        max[i] = Math.max(Math.max(max[i - 1] * nums[i], min[i - 1] * nums[i]), nums[i]);
        min[i] = Math.min(Math.min(max[i - 1] * nums[i], min[i - 1] * nums[i]), nums[i]);
        dp[i] = max[i];
    }
    return IntStream.of(dp).max().getAsInt();
}

简化
int res = nums[0];
int max = nums[0];
int min = nums[0];
for (int i = 1; i < nums.length; i++) {
    int tempMax = max;
    max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
    min = Math.min(Math.min(tempMax * nums[i], min * nums[i]), nums[i]);
    res = Math.max(max, res);
}
```

### Unique path

M, Array, Dynamic Programming

m*n矩阵，top-left -> bottom-right共有多少路线.

动态规划，初始第一行、第一列为0，写出状态转移方程以及定义初值，计算，然后查表。O(N*M)的空间和时间复杂度。
```
[1, 1, 1, 1]
[1, 2, 3, 4]
[1, 3, 6, 10]
[1, 4, 10, 20]
for (int i = 0; i < m; i++)
    arr[i][0] = 1;
for (int j = 0; j < n; j++)
    arr[0][j] = 1;
for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++) {
        arr[i][j] = arr[i][j - 1] + arr[i - 1][j];  // 上面+左面的cell路径和
    }
```

如果地图里面有障碍就是unique path II
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

如果是带权重的点，找最短步数。

```
public int minPathSum(int[][] grid) {
    // s[i][j] = Min{s[i - 1][j] + s[i][j - 1]} + grid[i][j]
    int m = grid.length; // row
    int n = grid[0].length; // column
    int[][] s = new int[m][n];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (i == 0 && j != 0) {
                s[i][j] = s[i][j - 1] + grid[i][j];
            } else if (i != 0 && j == 0) {
                s[i][j] = s[i - 1][j] + grid[i][j];
            } else if (i == 0 && j == 0) {
                s[i][j] = grid[i][j];
            } else {
                s[i][j] = Math.min(s[i - 1][j], s[i][j - 1]) + grid[i][j];
            }
        }
    }
    return s[m - 1][n - 1];
```

### Maximal Square

和题目85-(H) Maximal Rectangle非常的类似，和直方图题目84联系紧密， 但是这个题目完全不一样，是一个动态规划的问题。

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
        result = Math.max(result, dp[i][j] * dp[i][j]);
return result;
```

### Maximal Rectangle

复用Largest Rectangle in Histogram。
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

构建一个直方图。
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
```

### Largest Rectangle in Histogram

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

这里面helper函数就是计算left和right数组的。如果i=1的左右两边都大于i，那么left[i]=0，right[i] =1，这就可以看做延展性，如不能延展自然就是自己的长度了。

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

### edit distance

```
初始条件、递推式
d[i][0] = i
d[0][j] = j
d[i][j] = d[i-1][j-1] if word1[i-1]=word2[j-1]
d[i][j] = min(d[i-1][j-1] + 1, d[i-1][j] + 1, d[i][j-1] + 1)
```

类似的题目One Edit Distance

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

### Fibonacci

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

### Climbing stairs

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

## Divide and Conquer & heap

### Kth Largest Element in an Array

方法1：纯排序： 时间 O(NlogN)，时间不可接受

方法2：部分排序 建立一个长度为k的数组，找到最小的元素MIN。时间复杂度O(k) + (n-k)O(k) = O(nk)，当k比较大的时候退化为O(N^2)，不可取。
```
for num in a[n-k...n]
   if num > MIN
       delete MIN and find MIN again
```

方法3：建立一个小顶堆 Binary Heap。时间 O(NlogK)，空间 O(K)。这个比方法二的优点就是堆找MIN的时间复杂度降为了O(logK)。
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

方法4：分治思想的，线性选择算法。利用Quick Sort思想做一个Quick select。时间 Avg O(N) Worst O(N^2)，空间 O(1)

跟快速排序一个思路。先取一个枢纽值，将数组中小于枢纽值的放在左边，大于枢纽值的放在右边，具体方法是用左右两个指针， 
如果他们小于枢纽值则将他们换到对面，一轮过后记得将枢纽值赋回分界点。
如果这个分界点是k，说明分界点的数就是第k 个数。否则，如果分界点大于k，则在左半边做同样的搜索。
如果分界点小于k，则在右半边做同样的搜索。
```
public int findKthLargest(int[] nums, int k) {
    // 处理两个特殊的情况
    // 找第最后大的，那就是找最小的
    if (nums.length == k) {
        int min = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[min]) {
                min = i;
        return nums[min];
    }
    
    // 找第一个大的，那就是最大的了
    if (k == 1) {
        int max = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[max]) {
                max = i;
        return nums[max];
    }

    int right = nums.length - 1;
    int left = 0;
    while (left <= right) { //和二分查找的模板一模一样！
        int mid = quickSelect(nums, left, right);
        if (mid == k - 1) { 
            return nums[mid];
        } else if (mid < k - 1) {
            left = mid + 1;  
        } else {
            right = mid - 1;  
        }
    }
    throw new RuntimeException("not here");
}

//快速选择，参考{@link net.neoremind.mycode.argorithm.sort.QuickSort}来实现
private int quickSelect(int[] nums, int low, int high) {
    int pivot = nums[low];
    int i = low;
    int j = high + 1;
    while (true) {
        while (nums[++i] > pivot) {  // 这里是大于，因为是反向的排序！！！
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

### Merge k Sorted Lists

O(nlogk)

方法1：DAC, merge sort 2-way归并排序
```
public ListNode mergeKLists(ListNode[] lists) {
    if (lists == null || lists.length == 0) return null;
    // 处理lists为空或者长度为0的情况，否则就会StackOverFlow
    return doMergeKLists(lists, 0, lists.length - 1);
}

public ListNode doMergeKLists(ListNode[] lists, int left, int right) {
    if (left >= right) {
        return lists[left];
    } else {
        int mid = left + ((right - left) >> 1);
        ListNode l1 = doMergeKLists(lists, left, mid);
        ListNode l2 = doMergeKLists(lists, mid + 1, right);
        return mergeTwoSortedList(l1, l2);  //参考题目21
    }
}

ListNode mergeTwoSortedList(ListNode a, ListNode b) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;
    while (a != null && b != null) {
        if (a.val < b.val) {
            curr.next = a;
            a = a.next;
        } else {
            curr.next = b;
            b = b.next;
        }
        curr = curr.next;
    }
    if (a != null) {
        curr.next = a;
    }
    if (b != null) {
        curr.next = b;
    }
    return dummy.next;
}

//recursive
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {      
    if (l1 == null)     return l2;
    if (l2 == null)     return l1;
    if (l1.val < l2.val) {
        l1.next = mergeTwoLists(l1.next, l2);
        return l1;
    } else {
        l2.next = mergeTwoLists(l2.next, l1);
        return l2;
    }
}
```
方法2：MinHeap, 小顶堆, O(Nlogk) ，多路归并排序
```
PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));

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

### Median of two sorted array

区分奇数和偶数，偶数需要计算两个值求平均，奇数直接取中间的即可。
```
if ((m + n) % 2 == 0) {
    return (findKth(nums1, nums2, 0, 0, m, n, k) + findKth(nums1, nums2, 0, 0, m, n, k + 1)) / 2;
} else {
    return findKth(nums1, nums2, 0, 0, m, n, k + 1);
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

## hashtable

### valid anagrams

方法1：
```
int[] alphabet = new int[26];
for (int i = 0; i < s.length(); i++) {
    alphabet[s.charAt(i) - 'a']++;
}
for (int i = 0; i < t.length(); i++) {
    alphabet[t.charAt(i) - 'a']--;
}
for (int i : alphabet) {
    if (i != 0) {
        return false;
return true;
```

方法2：全放在hashtable，最后看value是否有大于0的，如果有就false。

### Group anagrams
```
if (strs == null || strs.length == 0) {
    return new ArrayList<List<String>>(0);
Map<String, List<String>> map = new HashMap<String, List<String>>();
//Arrays.sort(strs);
for (String s : strs) {
    char[] ca = s.toCharArray();
    Arrays.sort(ca);
    String keyStr = String.valueOf(ca);
    if (!map.containsKey(keyStr)) {
        map.put(keyStr, new ArrayList<String>());
    map.get(keyStr).add(s);
return new ArrayList<List<String>>(map.values());
```


run length encoding
```
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, ArrayList<String>> map = new HashMap<>();
    for (String s : strs) {
        int[] count = new int[26]; //cuz inputs are lowercase letters, we only need 26
        for (int i = 0; i < s.length(); i++) 
            count[s.charAt(i) - 'a']++;
        String anagram = "";//build a string key, eg."aabcccdd" -> 2a1b3c2d
        for (int i = 0; i < count.length; i++) 
            if (count[i] != 0) 
                anagram += String.valueOf(count[i]) + String.valueOf((char)('a' + i));
        if (!map.containsKey(anagram)) 
            map.put(anagram, new ArrayList<>());
        map.get(anagram).add(s);
    }
    return new ArrayList<List<String>>(map.values());
}
```

### Two sum

```
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    if (map.containsKey(target - nums[i])) {
        return new int[]{map.get(target - nums[i]), i};
    }
    map.put(nums[i], i);
}
return null;
```

存在重复，返回所有的index pair
```
public List<int[]> findNumbersThatSumToTarget(int[] arr, int target) {
    Map<Integer, Set<Integer>> map = new HashMap<>();
    List<int[]> res = new ArrayList<int[]>();
    for (int i = 0; i < arr.length; i++){
        if (!map.containsKey(arr[i]))
            map.put(arr[i], new HashSet<Integer>());
        map.get(arr[i]).add(i);
        if (map.containsKey(target - arr[i])){
            for(Integer j: map.get(target - arr[i]))
                if(j != i)    res.add(new int[]{i, j}); // notice: j != i
        }
    }
    return res;
}
```

如果是BST呢？其实这个方法适用于所有的Binary Tree。

```
public boolean findTarget(TreeNode root, int k) {
    HashSet<Integer> set = new HashSet<>();
    return dfs(root, set, k);
}

public boolean dfs(TreeNode root, HashSet<Integer> set, int k) {
    if (root == null) return false;
    if (set.contains(k - root.val)) return true;
    set.add(root.val);
    return dfs(root.left, set, k) || dfs(root.right, set, k);
}
```

## Array

下面都是仿照采用采用滑动窗口的概念，利用Map来存sum。

### Contiguous Array
只包含0和1，找到最长的一段，包含数目相等的0和1.学会转换问题，把0变成-1，就和Maximum Size Subarray Sum Equals k一样。
```
public int findMaxLength(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] == 0) nums[i] = -1;
    }
    Map<Integer, Integer> map = new HashMap<>(); 
    int res = Integer.MIN_VALUE;
    int k = 0;
    int sum = 0;
    for (int i = 0; i < nums.length; i++) {
        sum += nums[i];
        if (sum == k) {
            res = Math.max(res, i + 1);
        } else if (map.containsKey(sum - k)) {
            res = Math.max(res, i - map.get(sum - k));
        } 
        if (!map.containsKey(sum)) {
            map.put(sum, i);
        }
    }
    return res == Integer.MIN_VALUE ? 0 : res;
}
```

### Subarray Sum Equals K
数组找连续的subarray，和等于K，给出存在几个。
Given an array of integers and an integer k, 
you need to find the total number of **continuous subarrays** whose sum equals to k.
```
int sum = 0;
int result = 0;
Map<Integer, Integer> map = new HashMap<>(); //sum->数量
map.put(0, 1);
for (int i = 0; i < nums.length; i++) {
    sum += nums[i];
    if (map.containsKey(sum - k)) {
        result += map.get(sum - k);
    }
    map.put(sum, map.getOrDefault(sum, 0) + 1);
}
return result;
```

### Maximum Size Subarray Sum Equals k
给一个数组，找到长度最长的一个子串，其和等于K。时间复杂度要求O(N)。

Given an array nums and a target value k, find the maximum length of a subarray that sums to k. If there isn't
one, return 0 instead.

如果找不大答案返回-1. 核心就是叠加sum，然后把sum存起来，到达某个i，找sum-k的index，如果存在，那么中间的这段就是要找的子串。 注意和560. Subarray Sum Equals K很像，但是mem.put不用每次都put，只put第一次就好了，因为肯定是最靠前的，因为我们要的是最长的子串。
```
int sum = 0;
int maxLen = Integer.MIN_VALUE;
Map<Integer, Integer> mem = new HashMap<>(); //sum->第一个出现这个sum的index
for (int i = 0; i < nums.length; i++) {
    sum += nums[i];
    if (sum == k) {
        maxLen = i + 1;
    } else if (mem.containsKey(sum - k)) {
        maxLen = Math.max(maxLen, i - mem.get(sum - k));
    if (!mem.containsKey(sum)) { //只保存最远的，因为要求最远的。
        mem.put(sum, i);
    }
}
return maxLen == Integer.MIN_VALUE ? -1 : maxLen;
```

其他类似的包括：
Subarray Product Less Than K。

======

下面同样采用滑动窗口，保存start游标。一般一看minimum或者at most就是滑动窗口了。

### Minimum Size Subarray Sum
Given an array of n positive integers and a positive integer s, find the minimal length of a subarray of which the
sum ≥ s. If there isn't one, return 0 instead.
For example, given the array [2,3,1,2,4,3] and s = 7,
the subarray [4,3] has the minimal length under the problem constraint.
```
int sum = 0;
int minLen = Integer.MAX_VALUE;
int start = 0;
for (int i = 0; i< nums.length; i++) {
    sum += nums[i];
    while (sum >= s) {
        minLen = Math.min(i - start + 1, minLen);
        sum -= nums[start];
        start++;
return minLen == Integer.MAX_VALUE ? 0 : minLen;
```

### Minimum Window Substring

Given a string S and a string T, find the minimum window in S which will contain all the characters in T in
complexity O(n).
For example,
S = "ADOBECODEBANC"
T = "ABC"
Minimum window is "BANC".
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
        if (minLen > end - start)  //因为end已经++了，所以不用+1
            minStart = start;
            minLen = end - start;
        a[s.charAt(start)]++;
        if (a[s.charAt(start)] > 0)
            counter++;
        start++;
return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
```

### Longest Substring Without Repeating Characters
```
if (s == null || s.length() == 0) { return 0; }
int[] a = new int[256];
int start = 0, end = 0;
int minLen = Integer.MIN_VALUE;
while (end < s.length())
    minLen = Math.max(minLen, end - start + 1);
    a[s.charAt(end)]++;  //记数
    end++;
    while (end < s.length() && a[s.charAt(end)] > 0)  //如果发现了重复，那么需要从start开始滑动窗口到重复的字符。
        a[s.charAt(start)]--;
        start++;
return minLen == Integer.MAX_VALUE ? 0 : minLen;
```

### Find All Anagrams in a String
先预习hashtable的valid anagram和group anagrams。
和上面的Minimum Window Substring一样的模板。
```
Input:
s: "cbaebabacd" p: "abc"

Output:
[0, 6]

Explanation:
The substring with start index = 0 is "cba", which is an anagram of "abc".
The substring with start index = 6 is "bac", which is an anagram of "abc".

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
        while (counter == 0) {
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


其他类似：
LongestSubstringWithAtMostKDistinctCharacters
```
public int longestSubWithAtMostKDistinctChar(String s, int k) {
    int[] flag = new int[256];
    int counter = 0;
    int start = 0;
    char[] str = s.toCharArray();
    int maxLen = 0;
    for (int end = 0; end < str.length; end++) {
        if (flag[str[end]] == 0) {
            counter++;
        }
        flag[str[end]]++;
        while (counter > k) {
            flag[str[start]]--;
            if (flag[str[start]] == 0) {
                counter--;
            }
            start++;
        }
        maxLen = Math.max(maxLen, end - start + 1);
    }
    return maxLen;
}
```

LongestSubstringWithAtLeastKRepeatingCharacters // 这个必须brute force了，非sliding window系列
https://leetcode.com/problems/substring-with-concatenation-of-all-words/

======
### Three sum
1）先排序
2）从后往前定住最后一个，然后从前往后定住一个，中间的遍历找。
3）找到了，那么为了去重，要跳过从前往后重复的元素，以及从后往前重复的元素。这样就不用维护Set了。
```
Arrays.sort(num);
List<List<Integer>> res = new LinkedList<>();
for (int i = 0; i < num.length - 2; i++) {
    if (i == 0 || (i > 0 && num[i] != num[i - 1])) {
        int lo = i + 1, hi = num.length - 1, sum = 0 - num[i];
        while (lo < hi) {
            if (num[lo] + num[hi] == sum) {
                res.add(Arrays.asList(num[i], num[lo], num[hi]));
                while (lo < hi && num[lo] == num[lo + 1]) lo++;
                while (lo < hi && num[hi] == num[hi - 1]) hi--;
                lo++;
                hi--;
            } else if (num[lo] + num[hi] < sum) {
                lo++;
            } else {
                hi--;
            }
        }
    }
}
return res;
```

### Four Sum

复用O(N^2)的3 sum。
```
public List<List<Integer>> fourSum(int[] nums, int target) {
    List<List<Integer>> res = new LinkedList<>();
    Arrays.sort(nums);
    for (int i = 0; i < nums.length - 3; i++) {
        if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {
            int newTarget = target - nums[i];
            List<List<Integer>> temp = threeSum(nums, i + 1, nums.length - 1, newTarget);
            for (List<Integer> list : temp) {
                List<Integer> newList = new ArrayList<>(list);
                list.add(nums[i]);
                res.add(newList);
            }
        }
    }
    return res;
}

public List<List<Integer>> threeSum(int[] num, int start, int end, int target) {
    List<List<Integer>> res = new LinkedList<>();
    for (int i = start; i < end - 1; i++) {
        if (i == start || (i > start && num[i] != num[i - 1])) {
            int lo = i + 1, hi = end, sum = target - num[i];
            while (lo < hi) {
                if (num[lo] + num[hi] == sum) {
                    res.add(Arrays.asList(num[i], num[lo], num[hi]));
                    while (lo < hi && num[lo] == num[lo + 1]) lo++;
                    while (lo < hi && num[hi] == num[hi - 1]) hi--;
                    lo++;
                    hi--;
                } else if (num[lo] + num[hi] < sum) {
                    lo++;
                } else {
                    hi--;
                }
            }
        }
    }
    return res;
}
```

### Plus one
注意最后一位是0的情况。
```
if (digits == null || digits.length == 0) {
    return digits;
int i = digits.length - 1;
while (i >= 0) {
    if (digits[i] == 9) {
        digits[i] = 0;
    } else {
        digits[i] = digits[i] + 1;
        break;
    i--;
if (digits[0] == 0) {
    int[] ret = new int[digits.length + 1];
    ret[0] = 1;
    return ret;
return digits;
```

### Intersection of Two Arrays II

Intersection of Two Arrays要求unique。三种[java解法](https://discuss.leetcode.com/topic/45685/three-java-solutions)

方法1：O(N)时间复杂度，但是空间大些，而且使用了java.util类库性能一般
建两个集合set，nums1放入其中一个，然后用set的API做contains判断，如果存在则加入结果。

方法2：O(NlogN)时间复杂度
* 排序两个数组，Arrays.sort(..)
* 用双指，见下（可以处理不unique的情况）

方法3：O(NlogN)时间复杂度
* 排序一个数组，Arrays.sort(..)，另外一个不用排序哦！
* 使用二分查找
```
Arrays.sort(nums2);
for (Integer num : nums1) {
    if (binarySearch(nums2, num)) {
        set.add(num);
    }
}
```

第一问比较简单，给两个数组，找并集。
方法1：利用two pointers
Time: O(max(m, n)log(max(m, n))
Space: O(m+n)
```
Set<Integer> set = new HashSet<>();
Arrays.sort(nums1);
Arrays.sort(nums2);
int i = 0;
int j = 0;
while (i < nums1.length && j < nums2.length) {
    if (nums1[i] < nums2[j]) {
        i++;
    } else if (nums1[i] > nums2[j]) {
        j++;
    } else {
        set.add(nums1[i]);
        i++;
        j++;
int[] result = new int[set.size()];
int k = 0;
for (Integer num : set) {
    result[k++] = num;
}
return result;
```
方法2：排序一个，然后另外一个循环，找一个小的循环，大的二分查找。复用二分查找模板。

第二问的条件加了一个"Each element in the result should appear as many times as it shows in both arrays.The result can be in any order."
​       
用方法1还是可以对付元素重复的问题。

还要一种方法：用hashtable，第一个数组加入map，第二个数组就找存在key并且value>0的，然后--value即可。
Time: O(m+n)
Space: O(m)
```
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < nums1.length; i++) {
    map.put(nums1[i], map.getOrDefault(nums1[i], 0) + 1);
}
List<Integer> res = new ArrayList<>();
for (int i = 0; i < nums2.length; i++) {
    if (map.containsKey(nums2[i]) && map.get(nums2[i]) > 0) {
        res.add(nums2[i]);
        map.put(nums2[i], map.get(nums2[i]) - 1);
    }
}
int[] a = new int[res.size()];
int k = 0;
for (int num : res) {
    a[k++] = num;
}
return a;
```

What if elements of nums2 are stored on disk, and the memory is
limited such that you cannot load all elements into the memory at
once?

If only nums2 cannot fit in memory, put all elements of nums1 into a HashMap, read chunks of array that fit into the memory, and record the intersections.

If both nums1 and nums2 are so huge that neither fit into the memory, sort them individually (external sort), then read 2 elements from each array at a time in memory, record intersections.

//TODO 多路归并排序。2路归并排序。

### Merge Sorted Array

一开始限于从前往后merge的思路里，需要nums1不断的交换和后面的元素，非常难于解决。如果从后往前走，那么将豁然开朗！

```
int i = m - 1;
int j = n - 1;
int k = m + n - 1;
while (i >= 0 && j >= 0) {
    if (nums1[i] > nums2[j]) {
        System.out.println(String.format("nums1[%d] = nums1[%d]", k, i));
        nums1[k--] = nums1[i--];
        System.out.println(Arrays.toString(nums1));
    } else {
        System.out.println(String.format("nums1[%d] = nums2[%d]", k, j));
        nums1[k--] = nums2[j--];
        System.out.println(Arrays.toString(nums1));
    }
}
while (j >= 0) {
    System.out.println(String.format("nums1[%d] = nums2[%d]", k, j));
    nums1[k--] = nums2[j--];
    System.out.println(Arrays.toString(nums1));
}
```

### Sparse Matrix Multiplication

矩阵乘法。
这道题让我们实现稀疏矩阵相乘，稀疏矩阵的特点是矩阵中绝大多数的元素为0，而相乘的结果是还应该是稀疏矩阵，即还是大多数元素为0，那么我们使用传统的矩阵相乘的算法肯定会处理大量的0乘0 的无用功，所以我们需要适当的优化算法，使其可以顺利通过OJ，我们知道一个 i x k 的矩阵A乘以一个 k x j 的矩阵B会得到一个 i x j 大小的矩阵C，那么我们来看结果矩阵中的某个元素C[i][j]是怎么来的，起始是A[i][0]*B[0][j] + A[i][1]*B[1][j] + ... + A[i][k]*B[k][j]，那么为了不重复计算0乘0，我们首先遍历A数组，要确保A[i][k]不为0，才继续计算，然后我们遍历B矩阵的第k行，如果B[K][J]不为0，我们累加结果矩阵res[i][j] += A[i][k] * B[k][j]; 这样我们就能高效的算出稀疏矩阵的乘法，参见代码如下

```
int m1 = a.length;
int n1 = a[0].length;
int m2 = b.length;
int n2 = b[0].length;
if (n1 != m2) {
    throw new RuntimeException("Illegal matrix dimensions.");
int[][] c = new int[m1][n2];
for (int i = 0; i < m1; i++) {
    for (int j = 0; j < n2; j++) {
        for (int k = 0; k < n1; k++) {
            c[i][j] += a[i][k] * b[k][j];
return c;
```
其实就是把暴力做法的内部两个循环调换顺序，这样可以只检查一次A中的某个元素是否为0.
```
for (int i = 0; i < m; i++) {
    for (int k = 0; k < n; k++) {
        if (A[i][k] != 0) {
            for (int j = 0; j < nB; j++) {
                if (B[k][j] != 0) C[i][j] += A[i][k] * B[k][j];
```

扩展问题，如果要做矩阵的dot product呢？
类似SearchA2DMatrix问题，把矩阵化作一个一维的数组，然后相乘。

### SearchA2DMatrix

通用的解法时间复杂度O(M+N)。从右上角开始找，如果target小于右上角的，则col--，如果大于右上角，则row++，直到数组越界或者找到target为止。
```
public boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length;
    int n = matrix[0].length;
    int row = 0;
    int col = n - 1;
    while (row < m && col >= 0) {
        if (matrix[row][col] < target) {
            row++;
        } else if (matrix[row][col] > target) {
            col--;
        } else {
            return true;
        }
    }
    return false;
}
```
如果2D Matrix是一个排序好的，用二分查找。
```
public boolean searchMatrix2(int[][] matrix, int target) {
    int m = matrix.length;
    int n = matrix[0].length;
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
}
```

### Set Matrix Zeroes

Given a m x n matrix, if an element is 0, set its entire row and column to 0. Do it in place.

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

### Merge Intervals

Given [1,3],[2,6],[8,10],[15,18],
return [1,6],[8,10],[15,18].
算法如下，和这个类似的题目还有 (H)Insert Interval, (E) Meeting Rooms, (M) Meeting Rooms II
```
if (intervals.size() <= 1) return intervals;
//使用JDK8的lambda排序 !!!!!!! 别忘记了
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

### Insert Interval
把某个interval插入到合适的位置上。
```
public List<Interval> insert2(List<Interval> intervals, Interval newInterval) {
    if (intervals.size() <= 1) {
        return intervals;
    List<Interval> res = new ArrayList<Interval>();
    Collections.sort(intervals, (a, b) -> Integer.compare(a.start, b.start));
    boolean hasInsert = false;
    for (Interval i : intervals) {
        if (hasInsert || newInterval.start > i.end) {
            res.add(i);
        } else if (newInterval.end < i.start) {
            res.add(newInterval);
            hasInsert = true;
            res.add(i);
        } else {
            newInterval.start = Math.min(newInterval.start, i.start);
            newInterval.end = Math.max(newInterval.end, i.end);
        }
    if (!hasInsert) {
        res.add(newInterval);
    return res;
}
```

扩展题目：
given 2 list of interval representing users online offline timestamp e.g (already sorted), find all intervals that both of users are online.

e.g A= [(3, 5), (7, 11)] B=[(2, 4), (9, 10)] --> [(3, 4), (9, 10)].
```
public List<Interval> Intersection(List<Interval> a, List<Interval> b){
    var result = new List<Interval>();
    int i = 0;
    int j = 0;
    while(i < a.Count && j < b.Count)
        if (a.end <= b[j].start)
            i++;
        else if(b[j].end <= a.start)
            j++;
        else
            Interval temp = new Interval(Math.Max(a.start, b[j].start), Math.Min(a.end, b[j].end));
            result.Add(temp);
            if(a.end <= b[j].end)
                i++; 
            else
                j++;
    return result;
}
```

### Meeting room

Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.

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

### Meeting room II

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

### ReadNCharactersGivenRead4
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
//            for (int i = 0; i < length; i++) {
//                buf[readBytes + i] = buffer[i];
//            }
        System.arraycopy(buffer, 0, buf, readBytes, length);
        readBytes += length;
    }
    return readBytes;
}
```

### RandomPickIndex

蓄水池算法（Reservoir [[ˈrezəvwɑ:(r)] Sampling）Simple Reservoir Sampling solution。 

相比随机，好处是1）长度未知的海量数据流，2）可以工作在巨大的K下面，比如1w抽样5k，随机数冲突太大了，蓄水池抽样不会。

1）先选取数据流中的前k个元素，保存在集合A中；

2）从第j（k + 1 <= j <= n）个元素开始，每次先以概率p = k/j选择是否让第j个元素留下。若j被选中，则从A中随机选择一个元素并用该元素j替换它；否则直接淘汰该元素；

3）重复步骤2直到结束，最后集合A中剩下的就是保证随机抽取的k个元素。

java的random.nextInt(N)，返回`[0,n)`之间的随机数。

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

Mine Sweeper 扫雷
windows里面的扫雷，给一个h,w和m. 生成一个高度h，宽度w，总共m颗雷的矩阵。要求m颗雷随机分布。

参见 @Random subset of size K

Time: O(n)

public int[][] putBomb(int h, int w, int count){
Random r = new Random();
int[] bombLocs = new int[count]; // bomb location array
for (int i = 0; i < count; i++)
    bombLocs[i] = i;
for (int i = count; i < h * w; i++) {
    int j = r.nextInt(i + 1);
    if (j < count)	bombLocs[j] = i;
}
int[][] res = new int[h][w];
for (int i = 0; i < bombLocs.length; i++) {
    int x = bombLocs[i] / w;
    int y = bombLocs[i] % w;
    res[x][y] = 1;
}
return res;
}
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

变形版：randomly return one of the maximal elements indices

注意此题需要先找到cur max使其作为target同时更新res和count值。在求cur max时注意用"if (max == Integer.MIN_VALUE || nums[i] > max)"，因为数组中可能有Integer.MIN_VALUE
```
public class Solution {
    int[] nums;
    Random r;
    public Solution(int[] nums) {
        this.nums = nums;
        r = new Random();
    }   
    public int pick(int target) {
        int max = Integer.MIN_VALUE;
        int res = -1，count = 0;
        for (int i = 0; i < nums.length; i++) 
            if (max == Integer.MIN_VALUE || nums[i] > max) { //1st time meet the cur max, update res & count
                max = nums[i];
                res = i;
                count = 1;
            } else if (nums[i] == max) {
                if (rand.nextInt(++count) == 0) //later meet max, randomly pick
                    res = i;
            }
        return res;
    }
}
```


Random Slot

给你一个数字n, 代表从1->n  n个slot， 再给一个数组存着空slot的id. 让你同概率下randomly 返回一个非空的slot。
example:
n = 8, slots: 1,2,3,4,5,6,7,8
emptycells = {3, 5}
non empty cells: 1,2,4,6,7,8
return one of non empty cells randomly with equal probability.
```
public int returnEmptyCell(int slots , int[] emptyCell){
    Set<Integer> hs = new HashSet<>();
    for(int i = 0 ; i < emptyCell.length ; i++){
        hs.add(emptyCell[i]);
    }
    List<Integer> list  = new ArrayList<>();
    for(int i = 1 ; i <= slots ; i++){
        if(!hs.contains(i)){
            list.add(i);
        }
    }
    int rand = new Random().nextInt(list.size());
    return list.get(rand);
}
```

### Linked List Random Node
同样蓄水池抽样
```
ListNode head;
Random random;
public LinkedListRandomNode(ListNode head) {
    this.head = head;
    random = new Random();
}
public int getRandom() {
    if (head == null) {
        return -1;
    }
    int count = 1;
    ListNode curr = head;
    int res = curr.val;
    while (curr != null) {
        if (random.nextInt(count++) == 0) {
            res = curr.val;
        }
        curr = curr.next;
    }
    return res;
}
```

### Remove Duplicates from Sorted Array
```
if (nums.length < 2) {
    return 1;
}
int idx = 1;
for (int i = 1; i < nums.length; i++) {
    if (nums[i] != nums[idx - 1]) {
        nums[idx++] = nums[i];
    }
}
return idx;
```

### Remove Duplicates from Sorted Array II
允许最多两个2重复。把上面的idx改成2就行。
```
if (nums.length < 3) {
    return 2;
}
int idx = 2;
for (int i = 2; i < nums.length; i++) {
    if (nums[i] != nums[idx - 2]) {
        nums[idx++] = nums[i];
    }
}
return idx;
```


## Two pointers

### Move zeros
```
不稳定
    int i = 0;
    int j = nums.length - 1;
    while (i < j) {
        if (nums[i] == 0) {
            swap(i, j, nums);
            j--;
        } else {
            i++;
        }
    }

代码只需要返回最后有效数组的长度，有效长度之外的数字是什么无所谓，原先input里面的数字不一定要保持原来的相对顺序。
1.不用保持非零元素的相对顺序
2.不用把0移到右边
思路：把右边的非0元素移动到左边的0元素位置。这样就可以minimize writes.

public int moveZeroesWithMinWrites(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        while (left < right && nums[left] != 0)		left++;
        while (left < right && nums[right] == 0)	right--;
        if (left < right)	nums[left++] = nums[right--];
    }
    return left;
}

不够快，交换次数太多
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

最好的
    int idx = 0;
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] != 0) {
            nums[idx++] = nums[i];
        }
    }
    for (int i = idx; i < nums.length; i++) {
        nums[i] = 0;
    }
```

### ValidPalindrome
注意使用isLetterOrDigit方法
```
if (s.isEmpty()) {
    return true;
}
int head = 0, tail = s.length() - 1;
char cHead, cTail;
// < or <= both ok
while (head < tail) {
    cHead = s.charAt(head);
    cTail = s.charAt(tail);
    if (!Character.isLetterOrDigit(cHead)) {
        head++;
    } else if (!Character.isLetterOrDigit(cTail)) {
        tail--;
    } else {
        if (Character.toLowerCase(cHead) != Character.toLowerCase(cTail)) {
            return false;
        }
        head++;
        tail--;
return true;
```

### Valid Palindrome II
at most删除一个字符，是否可以形成一个回文串？

```
public boolean validPalindrome(String s) {
    int i = 0, j = s.length() - 1;
    while (i < j && s.charAt(i) == s.charAt(j)) {
        i++; j--;
    }
    if (i >= j) return true;
    if (isPalin(s, i + 1, j) || isPalin(s, i, j - 1)) return true;
    return false;
}

private boolean isPalin(String s, int i, int j) {
    while (i < j) {
        if (s.charAt(i) == s.charAt(j)) {
            i++; j--;
        else return false;
    return true;
```

扩展
1. PalindromeNumber，先计算多少位，然后利用n = (n % k) / 10, k是一个10的倍数。
2. PalindromePair找规律
```
   Case1: If s1 is a blank string, then for any string that is palindrome s2, s1+s2 and s2+s1 are palindrome.
   Case 2: If s2 is the reversing string of s1, then s1+s2 and s2+s1 are palindrome.
   Case 3: If s1[0:cut] is palindrome and there exists s2 is the reversing string of s1[cut+1:] , then s2+s1 is palindrome.
   Case 4: Similiar to case3. If s1[cut+1: ] is palindrome and there exists s2 is the reversing string of s1[0:cut] , then s1+s2 is palindrome.
```
3. Palindrome Partitioning。 找出单词所有回文串组合，DFS经典。看上面的DFS部分。
4. PalindromeLinkedList。找中点，反序后半部分，然后依次往头尾跑验证。需要很仔细的写。看下面的linkedlist部分。
5. LongestPalindromicSubstring。注意skip重复元素。
```
public String longestPalindrome(String s) {
    if (s == null || s.length() == 0) {
        return "";
    if (s.length() == 1) {
        return s;
    int low = 0;
    int maxLen = 0;
    for (int i = 0; i < s.length(); i++) {
        int k = i;
        int j = i;
        while (k < s.length() - 1 && s.charAt(k) == s.charAt(k + 1)) {
            k++; //skip duplicates
        while (j > 0 && k < s.length() - 1 && s.charAt(j - 1) == s.charAt(k + 1)) {
            j--;
            k++;
        if (k - j + 1 > maxLen) {
            maxLen = k - j + 1;
            low = j;
    return s.substring(low, low + maxLen);
```

## String

### reverse string
```
public String reverse(String s) {
    if (s == null) {
        return null;
    }
    char[] c = s.toCharArray();
    int length = s.length();
    int mid = length >> 1;
    for (int i = 0; i < mid; i++) {
        char temp = c[i];
        c[i] = c[length - i - 1];
        c[length - i - 1] = temp;
    }
    return new String(c);
}

public String reverse2(String s) {
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
}

public String reverse3(String s) {
    char[] str = s.toCharArray();
    Stack<Character> stack = new Stack<Character>();
    for (int i = 0; i < str.length; i++) {
        stack.push(str[i]);
    }

    String reversed = "";
    for (int i = 0; i < str.length; i++) {
        reversed += stack.pop();
    }

    return reversed;
}

public String reverse4(String s) {
    int length = s.length();
    String reverse = "";
    for (int i = 0; i < length; i++) {
        reverse = s.charAt(i) + reverse;
    }
    return reverse;
}

public String reverse5(String s) {
    int length = s.length();
    if (length <= 1) {
        return s;
    }
    String left = s.substring(0, length / 2);
    String right = s.substring(length / 2, length);
    return reverse5(right) + reverse5(left);
}
```

### reverse words in string
```
public String reverseWords(String s) {
    if (s == null) {
        return null;
    }
    // 1. trim leading, trailing spaces
    s = s.trim();

    // 2. empty
    if (s.length() == 0) {
        return "";
    }

    // 3. trim inner multiple spaces
    // barrier - 1 is the trimmed ending index
    // like RemoveDuplicatesFromSortedArray
    char[] str = s.toCharArray();
    int barrier = 1;
    for (int i = 1; i < str.length; i++) {
        if (str[i] == ' ' && str[i] == str[i - 1]) {
            continue;
        } else {
            str[barrier++] = str[i];
        }
    }

    // 4. reverse the whole string
    reverse(str, 0, barrier - 1);

    // 5. reverse each word
    int start = 0;
    for (int i = 0; i < barrier; i++) {
        if (str[i] == ' ') {
            reverse(str, start, i - 1);
            start = i + 1;
        }
    }

    // 6. reverse the last word
    reverse(str, start, barrier - 1);

    //7. return new String by invoking new String(char value[], int offset, int count)
    return new String(str, 0, barrier);
}
```

### Multiply strings

```
if (num1 == null || num1.length() == 0) {
    return num2;
if (num2 == null || num2.length() == 0) {
    return num1;
int len1 = num1.length();
int len2 = num2.length();
int[] res = new int[len1 + len2];
for (int i = len1 - 1; i >= 0; i--) {
    for (int j = len2 - 1; j >= 0; j--) {
        int value = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
        int sum = value + res[i + j + 1];
        res[i + j] = res[i + j] + sum / 10;
        res[i + j + 1] = (sum) % 10;

StringBuilder sb = new StringBuilder();
for (int p : res) {
    if (!(sb.length() == 0 && p == 0)) {
        sb.append(p);

return sb.length() == 0 ? "0" : sb.toString();
```

### Simplify paths

注意：第二个if里面的s.equals("..")不能少，/..=>/过不了。最后要倒序输出stack，prefix有/，suffix没有/。
使用linkedlist的pollLast做双端队列用。
```
if (path == null) return "";
    Stack<String> stack = new Stack<>();
    for (String s : path.split("/")) {
        if (s.equals("..") && !stack.isEmpty()) {
            stack.pop();
        } else if (s.equals("..") || s.equals("") || s.equals(".")) {
            continue;
        } else {
            stack.push(s);
        }
    }
    LinkedList<String> res = new LinkedList<>();
    while (!stack.isEmpty()) {
        res.add(stack.pop());
    }
    if (res.size() == 0) {
        return "/";
    } else {
        StringBuilder sb = new StringBuilder();
        while (!res.isEmpty()) {
            sb.append("/");
            sb.append(res.pollLast());
        }
        return sb.toString();
    }
```

最后也可以用：
```
String[] res = new String[stack.size()];
for (int i = stack.size() - 1; i >= 0; i--) {
    res[i] = stack.pop();
}
return Arrays.stream(res).collect(Collectors.joining("/", "/", "")); //分隔符、prefix、suffix
```

### add binary

```
public String addBinary(String a, String b) {
    if (a == null || a.length() == 0) {
        return b;
    if (b == null || b.length() == 0) {
        return a;
    int i = a.length() - 1;
    int j = b.length() - 1;
    int carry = 0;
    StringBuilder sb = new StringBuilder();
    while (i >= 0 && j >= 0) {
        int value = (a.charAt(i--) - '0') + (b.charAt(j--) - '0') + carry;
        sb.append(value % 2);
        carry = value / 2;

    while (j >= 0) {
        int value = (b.charAt(j--) - '0') + carry;
        sb.append(value % 2);
        carry = value / 2;

    while (i >= 0) {
        int value = (a.charAt(i--) - '0') + carry;
        sb.append(value % 2);
        carry = value / 2;
    }

    if (carry != 0) {
        sb.append(carry);
    return sb.reverse().toString();
```

### Add strings

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

### Implement strStr()
找子串。两种解法： 1）Brute force

JDK的String.indexOf(..)的实现就是这个，没用KMP
```
public int strStr(String haystack, String needle) {
    if (needle == null || needle.length() == 0) {
        return 0;
    if (needle.length() > haystack.length()) {
        return -1;
    char[] src = haystack.toCharArray();
    char[] target = needle.toCharArray();
    for (int i = 0; i < src.length - target.length + 1; i++) {
        int j = 0;
        while (j < target.length) {
            if (src[i + j] != target[j]) {
                break;
            }
            j++;
        }
        if (j == target.length) {
            return i;
        }
    }
    return -1;
}
```

2）KMP算法

KMP算法的重点在于每次模式串不用回到匹配串的i+1的位置去重新匹配，而是可以利用最长前缀后缀的方法来挪到i+N的位置，从而可以跳过不必要的元素。

重点在于如何求这个N，是利用一个next数组的东西，需要提前计算好，可以利用递推式搞定。

实际的KMP代码非常短小，这里暂时没有是实现。


## Tree
遍历方法，pre、in、post遍历。
```
public List<Integer> traverse(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    doInorder(root, result);
    return result;
}

private void doInorder(TreeNode root, List<Integer> result) {
    if (root != null) {
        doInorder(root.left, result);
        result.add(root.val);
        doInorder(root.right, result);
    }
}
```

in 中序
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
return result;
```

pre 前序
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

post 后序
```
LinkedList<Integer> result = new LinkedList<>();
Stack<TreeNode> stack = new Stack<>();
if (root == null) { // 必须处理
    return result;
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

```
   4
 2    6
1 3  5 7
    
pre  4,2,1,3,6,5,7
in   1,2,3,4,5,6,7
post 1,3,2,5,7,6,4
```

层序
```
List<List<Integer>> res = new ArrayList<>();
if (root == null) {
    return res;
Queue<TreeNode> q = new LinkedList<>();
q.add(root);
while (!q.isEmpty()) {
    int size = q.size();
    List<Integer> tmp = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
        TreeNode node = q.poll();
        tmp.add(node.val);
        if (node.left != null) {
            q.add(node.left);
        if (node.right != null) {
            q.add(node.right);
    res.add(tmp);
}
return res;
```

倒层序,递归的方式

```
public List<List<Integer>> findLeaves(TreeNode root) {
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    helper(result, root);
    return result;
}

private int helper(List<List<Integer>> result, TreeNode root) {
    if (root == null) return 0;
    int depth = 1 + Math.max(helper(result, root.left), helper(result, root.right));
    if (result.size() < depth) {
        result.add(new ArrayList<>());
    }
    result.get(depth - 1).add(root.val);
    return depth;
}
```

### Lowest Common Ancestor of a Binary Search Tree

```
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (p.val < root.val && q.val < root.val) {
        return lowestCommonAncestor(root.left, p, q);
    } else if (p.val > root.val && q.val > root.val) {
        return lowestCommonAncestor(root.right, p, q);
    } else {
        return root;
    }
}
```

普通二叉树

下面这种bottom-up的方法还出现在110. Balanced Binary Tree题目中，都可以O(N)的解决问题。

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

带有父节点信息的二叉树

那么，一个简单的思路，对p和q向上走，用hashtable记录访问过的节点，

如果某个节点已经被访问过了，那么返回该节点。

复杂度O(h),h为树的高度
```
def LCA(root, p, q):
    vis = set()
    while p or q:
        if p:
            if p in vis:
                return p
            vis.add(p)
            p = p.parent
        if q:
            if q in vis:
                return q
            vis.add(q)
            q = q.parent
    return None
```

更好的方法是求出p和q的高度，高度比较高的向上移动，直到两个节点相遇。

复杂度O(h),h为树的高度

```
int getHeight(Node *p) {
  int height = 0;
  while (p) {
    height++;
    p = p->parent;
  }
  return height;
}
 
// As root->parent is NULL, we don't need to pass root in.
Node *LCA(Node *p, Node *q) {
  int h1 = getHeight(p);
  int h2 = getHeight(q);
  // swap both nodes in case p is deeper than q.
  if (h1 > h2) {
    swap(h1, h2);
    swap(p, q);
  }
  // invariant: h1 <= h2.
  int dh = h2 - h1;
  for (int h = 0; h < dh; h++)
    q = q->parent;
  while (p && q) {
    if (p == q) return p;
    p = p->parent;
    q = q->parent;
  }
  return NULL;  // p and q are not in the same tree
}
```


### BinarySearchTreeIterator
```
class BSTIterator {

    private Stack<TreeNode> stack;

    private TreeNode curr;

    public BSTIterator(TreeNode root) {
        stack = new Stack<>();
        curr = root;
    }

    /**
     * @return whether we have a next smallest number
     */
    public boolean hasNext() {
        return curr != null || !stack.isEmpty();
    }

    /**
     * @return the next smallest number
     */
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
}
```

### TreeToDoublyLinkedList
实际就是递归的遍历。
```
public TreeNode convert(TreeNode root) {
    if (root == null || (root.left == null && root.right == null)) return root;
    TreeNode dummy = new TreeNode(0);
    Stack<TreeNode> stack = new Stack<>();
    TreeNode curr = root;
    TreeNode seeker = dummy;
    while (curr != null || !stack.isEmpty()) {
        if (curr != null) {
            stack.push(curr);
            curr = curr.left;
        } else {
            TreeNode node = stack.pop();
            //
            seeker.right = node;
            node.left = seeker;
            seeker = seeker.right;

            curr = node.right;
        }
    }
    TreeNode res =  dummy.right;
    res.left = null;
    return res;
}
```

O(1)空间的写法，递归：
```
TreeNode bintree2listUtil(TreeNode node) {
    if (node == null)
        return node;

    if (node.left != null) {
        TreeNode left = bintree2listUtil(node.left);
        for (; left.right != null; left = left.right) ;
        left.right = node;
        node.left = left;
    }

    if (node.right != null) {
        TreeNode right = bintree2listUtil(node.right);
        for (; right.left != null; right = right.left) ;
        right.left = node;
        node.right = right;
    }
    return node;
}

TreeNode bintree2list(TreeNode node) {
    if (node == null)
        return node;

    node = bintree2listUtil(node);

    while (node.left != null)
        node = node.left;
            
    return node;
}
```

### TreeToCircularDoublyLinkedList

```
public TreeNode convert(TreeNode root) {
    if (root == null || (root.left == null && root.right == null)) return root;
    TreeNode dummy = new TreeNode(0);
    Stack<TreeNode> stack = new Stack<>();
    TreeNode curr = root;
    TreeNode seeker = dummy;
    while (curr != null || !stack.isEmpty()) {
        if (curr != null) {
            stack.push(curr);
            curr = curr.left;
        } else {
            TreeNode node = stack.pop();
            //
            seeker.right = node;
            node.left = seeker;
            seeker = seeker.right;

            curr = node.right;
        }
    }
    TreeNode head = dummy.right;
    TreeNode tail = getRightMost(root, null);
    tail.right = head;
    head.left = tail;
    return head;
}
```

### FlattenBinaryTreeToLinkedList
```
         1
        / \
       2   5
      / \   \
     3   4   6
The flattened tree should look like:
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
    if (root == null) {
        return;
    }
    TreeNode left = root.left;
    TreeNode right = root.right;

    flatten(left);
    flatten(right);

    root.right = left;
    root.left = null;
    TreeNode curr = root;
    while (curr.right != null) {
        curr = curr.right;
    }

    curr.right = right;
}
```

另外很巧妙的题目是：BinaryTreeRightSideView
```
        1            <---
      /   \
     2     3         <---
      \     \
       5     4       <---
You should return [1, 3, 4].
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
        dfs(root.right, result, depth + 1);
        dfs(root.left, result, depth + 1);
    }
}
```

### ConvertSortedListToBinarySearchTree
```
public TreeNode sortedListToBST(ListNode head) {
    return helper(head, null);
}

TreeNode helper(ListNode head, ListNode tail) {
    if (head == tail) {
        return null;
    }
    ListNode slow = head;
    ListNode fast = head;
    while (fast.next != tail && fast.next.next != tail) {
        slow = slow.next;
        fast = fast.next.next;
    }
    TreeNode node = new TreeNode(slow.val);
    node.left = helper(head, slow);
    node.right = helper(slow.next, tail);
    return node;
}
```

如果是sorted array呢？
```
public TreeNode sortedArrayToBST(int[] nums) {
    return helper(nums, 0, nums.length - 1);
}

TreeNode helper(int[] nums, int start, int end) {
    if (start > end) {
        return null;
    }
    int mid = (start + end) >> 1;
    TreeNode node = new TreeNode(nums[mid]);
    node.left = helper(nums, start, mid - 1);
    node.right = helper(nums, mid + 1, end);
    return node;
}
```

### BinaryTreePaths
```
      1
    /   \
   2     3
    \
    5
```
All root-to-leaf paths are: ["1->2->5", "1->3"] 标准的backtrack回溯，参考NQueens实现 和PathSum和PathSumII类似

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
```

### SubtreeOfAnotherTree

复用same tree的代码。
```
public boolean isSubtree(TreeNode s, TreeNode t) {
    if (s != null) {
        if (sameTree(s, t) || isSubtree(s.left, t) || isSubtree(s.right, t)) {
            return true;
        }
    }
    return false;
}

public boolean sameTree(TreeNode s, TreeNode t) {
    if (s == null || t == null) {
        return s == t;
    }
    return s.val == t.val && sameTree(s.left, t.left) && sameTree(s.right, t.right);
}
```

### 树最大和最浅深度
```
public static int maxDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

```
public int minDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    if (root.left == null || root.right == null) {
        return 1 + Math.max(minDepth(root.left), minDepth(root.right));
    }
    return 1 + Math.min(minDepth(root.left), minDepth(root.right));
}
```

### count nodes
```
public int countNodes(TreeNode n) {
    if (n == null) {
        return 0;
    return 1 + countNodes(n.left) + countNodes(n.right);
```

### BinaryTreeMaximumPathSum

find the length of longest path between two nodes in a tree

返回树中任意两点路径的最大值。只要两点间有路径联通就可以。binary tree，找任意节点的longest path，不一定是叶子节点

```
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      -2     -7   -10
     *     / \     \
     *    1  3     8
```
```
21
对于一个节点，有下面几种可能：
1）root+left取或者0。
2）root+right取或者0。
返回大的就是经过这个节点某一条路的最大值。
全局的最大值需要是root+left取或者0+right取或者0。
     
public int maxPathSum(TreeNode root) {
    max = Integer.MIN_VALUE;
    maxPathDown(root);
    return max;
}

private int maxPathDown(TreeNode node) {
    if (node == null) {
        return 0;
    }
    int left = Math.max(0, maxPathDown(node.left));
    int right = Math.max(0, maxPathDown(node.right));
    max = Math.max(max, left + right + node.val);
    return Math.max(left, right) + node.val;
}
```

最短的路径呢?
```
-10
int min;

public int minPathSum(TreeNode root) {
    min = Integer.MAX_VALUE;
    minPathDown(root);
    return min;
}

/**
 * 和{@link #maxPathDown(TreeNode)}对应
 */
private int minPathDown(TreeNode node) {
    if (node == null) {
        return 0;
    }
    int left = Math.min(0, minPathDown(node.left));
    int right = Math.min(0, minPathDown(node.right));
    min = Math.min(min, left + right + node.val);
    return Math.min(left, right) + node.val;
}
```

从leaf到leaf的最短路径呢？

```
21
int maxLeaf2Leaf;

public int maxLeaf2Leaf(TreeNode root) {
    maxLeaf2Leaf = Integer.MIN_VALUE;
    maxLeaf2LeafDown(root);
    return maxLeaf2Leaf;
}

private int maxLeaf2LeafDown(TreeNode node) {
    if (node == null) {
        return 0;
    }
    int left = maxLeaf2LeafDown(node.left);
    int right = maxLeaf2LeafDown(node.right);
    maxLeaf2Leaf = Math.max(maxLeaf2Leaf, left + right + node.val);
    return Math.max(left, right) + node.val;
}
```

求从根到底最长的路径
```
16
对于一个节点，有下面几种可能：
1）算root+left。
2）算root+right.
返回大的就行。
     
public int maxSinglyPathSum(TreeNode root) {
    if (root == null) {
        return 0;
    }
    int left = maxSinglyPathSum(root.left);
    int right = maxSinglyPathSum(root.right);
    return root.val + Math.max(left, right);
}
```

find the “maximum sum” subtree sum in the binary tree

```
22
 * 对于一个节点，有下面几种可能：
 * 1）不算root。
 * 2）算root。
 * 3）算root+left+right
 * 4）算root+left。
 * 5）算root+right.
 * 上面5个找最大的就行。
public int maxSubTreeSum(TreeNode root) {
    if (root == null) {
        return 0;
    }
    int left = maxSubTreeSum(root.left);
    int right = maxSubTreeSum(root.right);
    int sum = root.val + left + right;
    int res = Math.max(0, Math.max(root.val, Math.max(sum, Math.max(root.val + left, root.val + right))));
    return res;
}
```

### ConstructBinaryTreeInorderAndPostorderTraversal

```
public TreeNode buildTree(int[] inorder, int[] postorder) {
    if (inorder == null || postorder.length == 0 ||
            inorder == null || inorder.length == 0) {
        return null;
    }
    if (inorder.length != postorder.length) {
        throw new RuntimeException("Tree not same!");
    }
    return doBuildTree(inorder, 0, inorder.length - 1, postorder, 0, postorder.length - 1);
}

TreeNode doBuildTree(int[] inorder, int inStart, int inEnd, int[] postorder, int postStart, int postEnd) {
    if (inStart > inEnd || postStart > postEnd) {
        return null;
    }
    int nodeIndex = -1;
    for (int i = inStart; i <= inEnd; i++) {
        if (inorder[i] == postorder[postEnd]) {
            nodeIndex = i;
            break;
        }
    }
    int leftChildrenLen = nodeIndex - inStart;
    if (nodeIndex < 0) {
        throw new RuntimeException("Tree not same!");
    }
    TreeNode node = new TreeNode(postorder[postEnd]);
    node.left = doBuildTree(inorder, inStart, nodeIndex - 1, postorder, postStart,
            postStart + leftChildrenLen - 1);
    node.right = doBuildTree(inorder, nodeIndex + 1, inEnd, postorder,
            postStart + leftChildrenLen, postEnd - 1);
    return node;
}
```

这个方法是“Construct BST from given preorder traversal”的O(n)解法，使用 MIN-MAX 思想
```
// For example, if the given traversal is {10, 5, 1, 7, 40, 50}, 
// then the output should be root of following tree.
//      10
//    /   \
//   5     40
//  /  \      \
// 1    7      50

public int idx = 0;

private TreeNode constructBST(int[] pre) {
    return constructBSTfromPreorder(pre, Integer.MIN_VALUE, Integer.MAX_VALUE);
}

private TreeNode constructBSTfromPreorder(int[] pre, int min, int max) {
    if (idx >= pre.length) return null;
    if (pre[idx] <= min || pre[idx] >= max) return null;
    TreeNode root = new TreeNode(pre[idx++]);
    root.left = constructBSTfromPreorder(pre, min, root.val);
    root.right = constructBSTfromPreorder(pre, root.val, max);
    return root;
}
```


### SerializeAndDeserializeBinaryTree

层序遍历也可以，先序也可以，这里就选先序的做。和lc-449. Serialize and Deserialize BST一样。

```
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
        }
    }

    return sb.toString();
}

private void buildString(TreeNode node, StringBuilder sb) {
    if (node == null) {
        sb.append("#").append(",");
    } else {
        sb.append(node.val).append(",");
        buildString(node.left, sb);
        buildString(node.right, sb);
    }
}

public TreeNode deserialize2(String data) {
    if (data == null || data.equals("")) return null;
    String[] chars = data.split(",");
    Queue<String> q = new LinkedList<>();
    q.addAll(Arrays.asList(chars));
    return helper(q);
}

TreeNode helper(Queue<String> q) {
    String val = q.poll();
    if (val.equals("#")) {
        return null;
    } else {
        TreeNode parent = new TreeNode(Integer.parseInt(val));
        parent.left = helper(q);
        parent.right = helper(q);
        return parent;
    }
}
```

### BST Two sum
非LC。
三种方法：
方法1：类似array的two sum，因为不用返回index，所以使用一个hashset。
time: O(N)
space: O(N)

```
if (root == null) {
    return null;
}
Set<Integer> hashset = new HashSet<>();
Stack<TreeNode> stack = new Stack<>();
TreeNode curr = root;
while (curr != null || !stack.isEmpty()) {
    if (curr != null) {
        stack.push(curr);
        curr = curr.left;
    } else {
        TreeNode node = stack.pop();
        // 替换逻辑开始
        if (hashset.contains(target - node.val)) {
            return new int[]{target - node.val, node.val};
        }
        hashset.add(node.val);
        // 替换逻辑结束
        curr = node.right;
    }
}
return null;
```

方法2：BST二分查找，进一步缩小空间复杂度
time: O(Nlogh)
space: O(h)
```
if (search(root, target - node.val, node) != null) {
    return new int[]{target - node.val, node.val};
}

if (root == null) {
    return null;
if (root.val == target) {
    if (root == node) {
        return null;
    return target;
} else if (root.val < target) {
    return search(root.right, target, node);
} else {
    return search(root.left, target, node);
```


方法3：two pointer压缩空间复杂度到O(1)，同时时间也是最优的。
BSTLeftIterator和BSTRightIterator就参考BinarySearchTreeIterator即可。
```
if (root == null) {
    return null;
}
BSTLeftIterator leftIterator = new BSTLeftIterator(root);
BSTRightIterator rightIterator = new BSTRightIterator(root);
int left = leftIterator.next();
int right = rightIterator.next();
while (left < right) {
    int sum = left + right;
    if (sum == target) {
        return new int[]{left, right};
    }
    if (sum < target && leftIterator.hasNext()) {
        left = leftIterator.next();
    } else if (sum < target && !leftIterator.hasNext()) {
        break;
    }
    if (sum > target && rightIterator.hasNext()) {
        right = rightIterator.next();
    } else if (sum > target && !rightIterator.hasNext()) {
        break;
    }
}
return null;
```


### Valid BST

中序遍历即可
```
public boolean isValidBST(TreeNode root) {
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
}
```


### Populating Next Right Pointers in Each Node II
```
         1 -> NULL
       /  \
      2 -> 3 -> NULL
     / \    \
    4-> 5 -> 7 -> NULL
```
参考level order遍历写了一个BFS更好理解：
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

### [Balanced Binary Tree](https://leetcode.com/problems/balanced-binary-tree/)

E, Tree Depth-first Search

给定一棵二叉树，判定它是否为平衡二叉树。

A balanced binary tree is the binary tree where the depth of the two subtrees of every node never differ by more than 1.

A complete binary tree is a binary tree whose all levels except the last level are completely filled and all the leaves in the last level are all to the left side.

Below is a balanced binary tree but not a complete binary tree. Every complete binary tree is balanced but not the other way around.
```
        1
     1     1
   1   1     1
 1 
```
As implies, in a complete tree, always the level difference will be no more than 1 so it is always balanced.

TOP-DOWN O(N^2), DOWN-UP O(N)

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

validate是否为complete tree:
```
final Queue<TreeNode> queue = new LinkedList<TreeNode>();
queue.add(root);
boolean incompleteDetected = false;
while (!queue.isEmpty()) {
    TreeNode node = queue.poll();
    if (node.left != null) {
        if (incompleteDetected) return false;
        queue.add(node.left);
    } else {
        incompleteDetected = true;

    if (node.right != null) {
        if (incompleteDetected) return false;
        queue.add(node.right);
    } else {
        incompleteDetected = true;
return true;
```

如果题目是检查一棵树是否平衡，平衡是指任意两个叶子节点到root的距离之差不大于1.

下面的树满足本题的要求，是一个balanced binary tree。
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

## Binary search
两个模板：
```
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
return -1;

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
return arr[start] == target ? start : -1;

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
```

### Divide two integers

```
public int divide(int dividend, int divisor) {
    int sign = 1;
    if ((dividend > 0 && divisor < 0) || (dividend < 0 && divisor > 0)) {
        sign = -1;
    long ldividend = Math.abs((long) dividend);
    long ldivisor = Math.abs((long) divisor);

    if (ldivisor == 0) {
        return Integer.MAX_VALUE;
    }
    if ((ldividend == 0) || (ldividend < ldivisor)) {
        return 0;
    }

    long lans = ldivide(ldividend, ldivisor);

    int ans;
    if (lans > Integer.MAX_VALUE) { //Handle overflow.
        ans = (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
    } else {
        ans = (int) (sign * lans);
    }
    return ans;
}
private long ldivide(long ldividend, long ldivisor) {
    if (ldividend < ldivisor) {
        return 0;
    }
    //  whereas we are moving with stride 1, 2, 4, 8, 16...2^n for performance reason.
    long sum = ldivisor;
    long multiple = 1;
    while ((sum + sum) <= ldividend) {
        sum += sum;
        multiple += multiple;
    }
    return multiple + ldivide(ldividend - sum, ldivisor);
```

测试用例：
```
System.out.println(s.divide(-6, 2)); //-3 验证符号
System.out.println(s.divide(10, 0)); //MAX_INT 验证除数是0
System.out.println(s.divide(0, 10)); //0 验证被除数是0
System.out.println(s.divide(Integer.MAX_VALUE, 1)); //MAX_INT
System.out.println(s.divide(Integer.MIN_VALUE, -1)); //解决res>max_int的overflow case，否则就会得到min_int，是个负数，并且Math.abs里面必须有long转型。
```

### First Bad Version

二分查找模板。
```
int m = 1;
while (m < n) {
    int mid = (m + n) >>> 1;
    if (isBadVersion(mid)) {
        n = mid;
    } else {
        m = mid + 1;
    }
}
return m;
```

### Search in Rotated Sorted Array

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

    if (nums[lo] <= nums[mid]) {
        if (target >= nums[lo] && target < nums[mid]) {
            hi = mid - 1;
        } else {
            lo = mid + 1;
    } else {
        if (target > nums[mid] && target <= nums[hi]) {
            lo = mid + 1;
        } else {
            hi = mid - 1;
return nums[lo] == target ? lo : -1;
```

### Find Minimum in Rotated Sorted Array

```
if (nums == null || nums.length == 0) {
    return -1;
}
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

避免重复
```
if (nums == null || nums.length == 0) {
    return -1;
}
int start = 0;
int end = nums.length - 1;
while (start < end) {
    int mid = (start + end) >> 1;
    if (nums[mid] > nums[end]) {
        start = mid + 1;
    } else if (nums[mid] < nums[end]) {
        end = mid;
    } else {
        end--;
    }
}
return nums[start];
```

## Sort

### sort colors 荷兰国旗
```
int begin = 0;
int current = 0;
int end = nums.length - 1;
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
sort k colors,复用sort 3 colors也就是上面的模板，找最大和最小值，分别当做2和0.
```
public void sortKColors(int[] nums, int k) {
    int left = 0;
    int right = nums.length - 1;
    while (left < right) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = left; i <= right; i++) {
            max = Math.max(nums[i], max);
            min = Math.min(nums[i], min);
        }
        /** 代码复用
        int current = left;
        while (current <= right) {
            if (nums[current] == min) {
                swap(current, left, nums);
                current++;
                left++;
            } else if (nums[current] > min && nums[current] < max) {
                current++;
            } else {
                swap(current, right, nums);
                right--;
            }
        }
        **/
    }
}
```

## Graph

### Course Schedule
拓扑排序，看是否有环。

BFS
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
      
visit - 1, queue = 2
                     2 ----------
                     |          |
                     \/         \/
                     5 -------> 4 -------> 3 ------> 6
                                |                  /|\
                                 -------------------
       
visit - 2, queue = 5
      
                     5 -------> 4 -------> 3 ------> 6
                                |                  /|\
                                 -------------------
       
visit - 5, queue = 4
      
                                4 -------> 3 ------> 6
                                |                  /|\
                                 -------------------
       
visit - 4, queue = 3
      
                                 3 ------> 6
       
visit - 3, queue = 6
      
                                 6
       
visit - 6, queue = null
```

```
public boolean canFinishBFS(int numCourses, int[][] prerequisites) {
    // 每门课的依赖
    ArrayList[] graph = new ArrayList[numCourses];

    // 所有课程的度
    int[] degree = new int[numCourses];

    Queue queue = new LinkedList();
    int count = 0;

    for (int i = 0; i < numCourses; i++) {
        graph[i] = new ArrayList();
    }

    // 第一步，总之就是先初始化邻接表
    for (int i = 0; i < prerequisites.length; i++) {
        degree[prerequisites[i][0]]++;
        graph[prerequisites[i][1]].add(prerequisites[i][0]);
    }

    // 第二步，找度0的，放入队列
    for (int i = 0; i < degree.length; i++) {
        if (degree[i] == 0) {
            queue.add(i);
            count++;
        }
    }

    // 第三步，不断的出队列，把所有邻接的兄弟的，也就是依赖于自己的全部度-1，如果发现度=0的，则入队列，否则就有环了
    while (queue.size() != 0) {
        int course = (int) queue.poll();
        for (int i = 0; i < graph[course].size(); i++) {
            int pointer = (int) graph[course].get(i);
            degree[pointer]--;
            if (degree[pointer] == 0) {
                queue.add(pointer);
                count++;
            }
        }
    }
    if (count == numCourses) {
        return true;
    } else {
        return false;
    }
}
```

DFS
```
public boolean canFinishDFS(int numCourses, int[][] prerequisites) {
    ArrayList[] graph = new ArrayList[numCourses];
    for (int i = 0; i < numCourses; i++) {
        graph[i] = new ArrayList();
    }

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

### Course Schedule II
返回任意一个合法的order即可。
```
/**
 * 图的顶点
 */
class Vertex {

    /**
     * 与该顶点邻接的顶点
     */
    public ArrayList<Integer> adjacences = new ArrayList<>();

    /**
     * 入度，也就是指向这个节点的边的数量
     */
    public int inDegree = 0;

    @Override
    public String toString() {
        return adjacences + "\t" + inDegree;
    }
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

    printAdjacencyList(adjacencyList);

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

### Word Ladder

```
hit -> hot ------> dot -> dog ----->cog
              |                |
               --> lot -> log --
```
输出转换的最短路径，BFS。一条道走到黑，下面的解法只能按照"hit-hot-dot-dog-cog"的方式找到，不能走下面lot-log的路径了。
贪心的算法，哪一层先找到了，实际就是提前设置了好了路径，别人touch不到了。因为所有的字母都遍历一遍。但是下面的做法会TLE。

另外要输出所有的最短路径是Word Ladder II还没有攻破。

```
        ------------------------------
        |                            |
 hit -> hot ------> dot ------------->cot
         |                 |
          --> lot -> bot --
```

```
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    if (beginWord.equals(endWord)) {
        return 0;
    }
    Map<String, Vertex> map = new HashMap<>();
    Queue<Vertex> q = new LinkedList<>();
    q.add(new Vertex(beginWord, 1));
    map.put(beginWord, q.peek());
    while (!q.isEmpty()) {
        Vertex v = q.poll();
        String word = v.word;
        int level = v.level;
        char[] arr = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            char old = arr[i];
            for (int j = 0; j < 26; j++) {
                arr[i] = (char) ('a' + j);
                String newWord = new String(arr);
                if (!map.containsKey(newWord) && wordList.contains(newWord)) {
                    Vertex newVertex = new Vertex(newWord, level + 1);
                    newVertex.prev = v;
                    q.offer(newVertex);
                    map.put(newWord, newVertex);
                }
            }
            arr[i] = old;
        }
    }
    if (!map.containsKey(endWord)) {
        return 0;
    }
    return map.get(endWord).level;
}

class Vertex {
    String word;
    int level;
    Vertex prev;
```

### Alien Dictionary

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

### clone graph
```
if (node == null) {
    return null;
}

UndirectedGraphNode newNode = new UndirectedGraphNode(node.label); //new node for return
HashMap<Integer, UndirectedGraphNode> map = new HashMap(); //store visited nodes

map.put(newNode.label, newNode); //add first node to HashMap

LinkedList<UndirectedGraphNode> queue = new LinkedList(); //to store **original** nodes need to be visited
queue.add(node); //add first **original** node to queue

while (!queue.isEmpty()) { //if more nodes need to be visited
    UndirectedGraphNode n = queue.pop(); //search first node in the queue
    for (UndirectedGraphNode neighbor : n.neighbors) {
        if (!map.containsKey(neighbor.label)) { //add to map and queue if this node hasn't been searched before
            map.put(neighbor.label, new UndirectedGraphNode(neighbor.label));
            queue.add(neighbor);
        }
        map.get(n.label).neighbors.add(map.get(neighbor.label)); //add neighbor to new created nodes
    }
}

return newNode;
```

## Union Find

Weighted Quick Union Disjoint sets
```
Find：O(logN)
Union：O(logN)
比上面的好处在于控制了树的深入，避免了最快的情况

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

### Longest Consecutive Sequence

这和动态规划的两个LCS问题（Longest Common Subsequence不连续）和（Longest Common Substring连续）不是一个LCS问题。

另外和300. Longest Increasing Subsequence 也不一样，因为这个要求连续，300题只要求是递增的。因此并查集并不行。

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

下面的并查集类UF使用的是weighted-quick-union并查集算法，可以限制树的高度为O（logN），因为每次查询的事件复杂度也是O（logN）。 如果加上路径压缩（path compression）那么会非常近似等于1.

这里有个技巧是做映射， 因为并查集必须是0-N连续的，而题目给的数字不是，那么就可以利用一个hashmap，key为值，value就是index位置，做一个映射即可。 实际并的是这个index位置。

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

### graph valid tree
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

## Trie
字典树模板
```
class _TrieNode {
    public char val;
    public boolean isWord;
    public _TrieNode[] children = new _TrieNode[26];
    public _TrieNode() {
    }
    _TrieNode(char c) {
        _TrieNode node = new _TrieNode();
        node.val = c;
    }
}
class _Trie {
    private _TrieNode root;
    public _Trie() {
        root = new _TrieNode();
        root.val = ' ';
    }
    public void insert(String word) {
        _TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) {
                ws.children[c - 'a'] = new _TrieNode(c);
            }
            ws = ws.children[c - 'a'];
        }
        ws.isWord = true;
    }
    public boolean search(String word) {
        _TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) {
                return false;
            }
            ws = ws.children[c - 'a'];
        }
        return ws.isWord;
    }
    public boolean startsWith(String prefix) {
        _TrieNode ws = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (ws.children[c - 'a'] == null) {
                return false;
            }
            ws = ws.children[c - 'a'];
        }
        return true;
    }
```

### add and search word

注意search使用dfs。
```
class WordDictionary2 {

    class Node {
        char val;
        boolean isWord;
        Node[] children = new Node[26];

        public Node(char val) {
            this.val = val;
        }
    }

    Node root;

    public WordDictionary2() {
        root = new Node(' ');
    }

    public void addWord(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        char[] chars = word.toCharArray();
        Node node = root;
        for (char c : chars) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new Node(c);
            }
            node = node.children[c - 'a'];
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        char[] chars = word.toCharArray();
        return dfs(root, chars, 0);
    }

    public boolean dfs(Node node, char[] chars, int index) {
        if (node == null) return false;
        if (index == chars.length) return node.isWord;
        char c = chars[index];
        if (c == '.') {
            for (int i = 0; i < 26; i++) {
                if (dfs(node.children[i], chars, index + 1)) {
                    return true;
                }
            }
            return false;
        } else {
            return dfs(node, chars, index + 1);
        }
    }
}
```

## Greedy

### [Task Scheduler](https://leetcode.com/problems/task-scheduler/description/)

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
和358. Rearrange String k Distance Apart类似。
```
public String rearrangeString(String str, int k) {
    Map<Character, Integer> char2Freq = new HashMap<>();
    for (char ch : str.toCharArray()) {
        char2Freq.put(ch, char2Freq.getOrDefault(ch, 0) + 1);
    }
    PriorityQueue<Map.Entry<Character, Integer>> queue =
            new PriorityQueue<>((o1, o2) -> o2.getValue() - o1.getValue());
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
}
```

扩展，如果要求保证任务顺序呢？没那么难，直接遍历数组，维护一个task上次最近执行时间的map，不够就加idle即可。
```
Map<Character, Integer> task2LastRunTime = new HashMap<>(tasks.length * 4 / 3);
StringBuilder sb = new StringBuilder();
int index = 0;
for (char task : tasks) {
    // new task
    if (!task2LastRunTime.containsKey(task)) {
        sb.append(task);
    } else {
        while (index - task2LastRunTime.get(task) <= n) {
            sb.append("_");
            index++;
        }
        sb.append(task);
    }
    task2LastRunTime.put(task, index);
    index++;
}
return sb.toString();
```

## Math

### Pow

```
     * 6^2 = 6^1 * 6^1 = 6 * 6 = 36
     * 6^1 = 6 * 6^0 * 6^0 = 6 * 1 * 1 = 6
     * 6^0 = 1
     *
     * 6^3 = 6 * 6^2 * 6^2
     *
     * 6 ^-3 = ( 6^-1 * 6^-1) / 6
```
```
public double myPow(double x, int n) {
    if (n == 0) {
        return 1;
    }
    // 下面的可加可不加
    if (x == 0d) {
        return 0;
    }
    if (n == 1) {
        return x;
    }
    if (n == -1) {
        return 1 / x;
    }
    //
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
}

非递归
public double myPow(double x, int n) {
    if (n == 0 || x == 1)     return 1;
    if (x == -1)    return n % 2 == 0 ? 1 : -1;
    if (n == Integer.MIN_VALUE) return 0;
    if (n < 0) {
        n = -n;
        x = 1 / x;
    }
    double res = 1;
    while (n > 0) {
        if ((n & 1) == 1)  res *= x; // execude only when odd: n, 1; even: 1
        x *= x; // x to the power of 2
        n >>= 1; // divide the power by 2
    }
    return res;
}
```

### sqrt
```
/**
 * 按照23为例，
 * <pre>
 *     left   right   mid  mid^2         ans
 *       1     23     11    121  >  23    -
 *       1     10     5      25  >  23    -
 *       1     4      2      4   <= 23    4
 *       5     4                        quit
 * </pre>
 * <p>
 * 按照25为例，
 * <pre>
 *     left   right   mid  mid^2         ans
 *       1     25     13    169  >  25    -
 *       1     12     6      36  >  25    -
 *       1     5      3      9   <= 25    3
 *       4     5      4      16  <= 25    4
 *       5     5      5      25  <= 25    5
 *       6     5                         quit
 * </pre>
 */
public int mySqrtBinarySearch(int x) {
    if (num < 1) {
        return num;
    }
    long left = 1, right = num;// long type to avoid 2147483647 case
    int ans = -1;
    while (left <= right) {
        long mid = (left + right) / 2;
        long t = mid * mid;
        if (t > num) {
            right = mid - 1;
        } else if (t <= num) {
            left = mid + 1;
            ans = (int) mid;
        }
    }
    return ans;
}
```

valid perfect square，16，25，36一样的模板
```
public boolean isPerfectSquare(int num) {
    if (num < 1) {
        return false;
    }
    long left = 1, right = num;// long type to avoid 2147483647 case

    while (left <= right) {
        long mid = (left + right) / 2;
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
}
```

### Excel Sheet Column Number

```
 * A -> 1
 * B -> 2
 * C -> 3
 * ...
 * Z -> 26
 * AA -> 27
 * AB -> 28
int len = s.length();
int res = 0;
for (int i = 0; i < len; i++)
    res = res * 26 + (s.charAt(i) - 'A') + 1;
return res;
```

### Excel Sheet Column Title

思路和60-permutation-sequence 的一样，其实就是根据base做进制的转换，参考通用的模板，一般都是用低位处理（取低位用%），然后处理高位（用除法或者>>>）
```
StringBuilder sb = new StringBuilder();
do {
    n--;
    sb.insert(0, (char) ('A' + n % 26));
    n = n / 26;
} while (n != 0);
return sb.toString();
等价于
StringBuilder sb = new StringBuilder();
while (n != 0) {
    n--;
    sb.insert(0, (char) ('A' + n % 26));
    n = n / 26;
};
return sb.toString();
```

扩展base进制转换
```
static final String BASE36 = "0123456789abcdefghijklmnopqrstuvwxyz";

public static String base(int value, int base) {
    final StringBuilder sb = new StringBuilder();
    do {
        sb.insert(0, BASE36.charAt(value % base));
        value /= base;
    } while (value != 0);
    return sb.toString();
}

public static int base(String value, int base) {
    int result = 0;
    for (int i = 0; i < value.length(); i++) {
        char digit = value.charAt(i);
        result = result * base + BASE36.indexOf(digit);
    }
    return result;
}


public String ten2binary(int n) {
    return base(n, 1);
}

public String ten2Octal(int n) {
    return base(n, 8);
}

public String ten2hex(int n) {
    return base(n, 16);
}
```

## Bit manipulation

### Total Hamming Distance
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

Total hamming distance
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

## Linked list

### Remove Nth Node From End of List
提前设置好步长，end在start前面n+1位置，然后一块跑，直到end到头了，这时候start的下一个就是倒数第N个，直接设置next为next.next即可。
使用dummy作为开头。
```
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode header = new ListNode(0);
    header.next = head;
    ListNode start = header;
    ListNode end = header;
    for (int i = 1; i <= n + 1; i++) {
        end = end.next;
    }
    while (end != null) {
        start = start.next;
        end = end.next;
    }
    start.next = start.next.next;
    return header.next;
}
```

### Reverse linked list
```
ListNode newHead = null;
while (head != null) {
    ListNode next = head.next;
    head.next = newHead;
    newHead = head;
    head = next;
}
return newHead;
```

递归：
```
public static ListNode reverseListRecursiveLy(ListNode head) {
    return doReverseListRecursiveLy(head, null);
}
public static ListNode doReverseListRecursiveLy(ListNode head, ListNode newHead) {
    if (head == null) {
        return newHead;
    ListNode next = head.next;
    head.next = newHead;
    return doReverseListRecursiveLy(next, head);
```

### Linked List Cycle

第一问，是否有环？

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

第二问，如果有环，返回相遇点。如果没有返回null。
快（走两步）、慢指针（走一步）的方式，总会相遇。

X是runner（快指针）和walker（慢指针）相遇的点，设

- 1）从head到cycle起点的距离是A
- 2）cycle起点到X的举例是B，
- 3）环的长度是N 那么根据快慢指针的追的特性可以得出：

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
o->o->o->o->o->o->o       o
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

### Delete Node in a Linked List
```
public static void deleteNode(ListNode node) {
    node.val = node.next.val;
    node.next = node.next.next;
}
```

### Remove Duplicates from Sorted List

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
另外一种递归的方法，从后往前倒，保留最后一个重复的节点，很巧妙！要理解深刻递归！

public ListNode deleteDuplicates2(ListNode head) {
    if (head == null || head.next == null) return head;
    head.next = deleteDuplicates(head.next);
    return head.val == head.next.val ? head.next : head;
}
```

### Remove Duplicates from Sorted List II

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

### LRUCache

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
            if (target == null) {
                return -1;
            }
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
                size++;  // size++在这里
                if (head == null && tail == null) {  //为空时候特殊处理
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
                    if (size > capacity) {
                        deleteLRU();
                    }
                }
            }
        }

        void moveAhead(Node target) {
            if (target == head) {  //处理在对头和队尾两种特殊情况
                return;
            }
            if (tail == target) {
                tail = target.prev;
                head = target;
                return;
            }
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

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
}
```