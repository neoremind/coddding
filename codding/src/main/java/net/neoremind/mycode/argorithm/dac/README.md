# [分治算法 Divide and conquer algorithms](https://en.wikipedia.org/wiki/Divide_and_conquer_algorithms)

In computer science, divide and conquer (D&C) is an algorithm design paradigm based on multi-branched recursion.
A divide and conquer algorithm works by recursively breaking down a problem into two or more sub-problems of the same or related type,
until these become simple enough to be solved directly. The solutions to the sub-problems are then combined to give a solution to the original problem.

要点：
* 分治算法是基于多分枝递归的一种算法设计模式。

* 分治算法递归地把一个大问题分解为多个类型相同的子问题，直到这些子问题足够的简单能被直接解决。

* 最后把这些子问题的解结合起来就能得到原始问题的解。

根据这个定义，我们可以很明显的知道，对于D&C问题，我们解决它需要三步：

* 1、Divide。即分，将问题拆分成几个子问题，也就是找到递归式

* 2、Conquer。即治，通过递归的方法分别解决第一步中子问题

* 3、Combine。即合，将各个子问题的结果合并起来便得到整个问题的解决方案

This divide and conquer technique is the basis of efficient algorithms for all kinds of problems, such as sorting (e.g., quicksort, merge sort), multiplying large numbers (e.g. the Karatsuba algorithm), finding the closest pair of points, syntactic analysis (e.g., top-down parsers), and computing the discrete Fourier transform (FFTs).

* a^n
* quick sort & merge sort
* binary search
* Fibonacci
* 矩阵乘法 Strassen//暂时未研究
* Median of Two Sorted Arrays【leetcode 4】
* Merge k Sorted Lists【leetcode 23】
* Maximum Subarray 【leetcode 53】
* Kth Largest Element in an Array 【leetcode 215】
* Search a 2D Matrix II 【leetcode 240】

有几张分析时间复杂度的图：
http://blog.csdn.net/xyd0512/article/details/8220506

例如merge sort的时间复杂度：
T(N) = 2T(N/2) + O(N) = O(NlogN)
2表示subproblems
T(N/2)表示subproblems size
O(N)表示work dividing and combine

binary search的时间复杂度：
T(N) = 2T(N/2) + O(1) = O(logN)
