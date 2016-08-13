## Leetcode records

### [64. Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/)

M, Array, Dynamic Programming
从左上到右下，最少要多少步。动态规划，定义状态和状态转移方程，状态就是到达每一个点最少的步骤，这是一个最优子结构和具有子问题重叠的问题。无后效性。

```
s[i][j] = Min{s[i - 1][j] + s[i][j - 1]} + grid[i][j]
```

技巧是搞一个ListNode在head的前面

### [19. Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/)

E，linked list，two pointers

技巧是搞一个ListNode在head的前面