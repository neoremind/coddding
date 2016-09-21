# 并查集Union-find（不想交集合）

## 代码来源
所有的XXXUF的示例代码都来自于
[cs.princeton.edu](http://algs4.cs.princeton.edu/15uf/)

##《数据结构与算法分析》第8章部分

### 等价关系
自反性、对称性、传递性

<=不是等价关系，电气连通性和城市道路是关系。

那么动态的等价关系，或者说*Dynamic connectivity*是并查集要解决的问题。

### 几种并查集的实现方案

一般的数据结构如下：
```
class UF {
    int[] s

    public UF(int n) {
        ...
    }

    void union(int p, int q)

    int find(int x)

}
```

#### 1. Quick-find

Find：O(2)

Union：O(N)

每次union都把等价的元素id改成一样的，这种二次的quadratic的方案不是非常可行。下面的解法都稍微的好些。

#### 2. Quick-union

Find：O(tree height)

Union：O(tree height)

要连通就赋值parent组成一颗树。多个不相交的树组成了森林。

#### 3. Weighted quick-union

Find：O(logN)

Union：O(logN)

比上面的好处在于控制了树的深入，避免了最快的情况

#### 4. Weighted quick-union with path compression

Find：非常非常接近O(1)

Union：非常非常接近O(1)

但是O(1)同时是永远达不到的。

用了路径压缩，在find的同时把点挂到根节点上，避免过深入的find搜索。

### 示例
下面看示例，代码在`App`类里面，比较Quick-union和Weighted quick-union。

初始并查集0-N大小：
```
0  1  2  3  4  5  6  7  8  9
```

下面的步骤Quick-union和Weighted quick-union的结果都一样。
```
uf.union(1, 2);

0  1     3  4  5  6  7  8  9
    \
     2

uf.union(2, 3);

0  1     3  4  5  6  7  8  9
  / \
  2  3

uf.union(5, 6);

0  1     3  4  5     7  8  9
  / \           \
  2  3           6

uf.union(7, 8);

0  1     3  4  5     7     9
  / \           \     \
  2  3           6     8

uf.union(9, 0);

   1     3  4  5     7     9
  / \           \     \     \
  2  3           6     8    0

uf.union(8, 9);

   1     3  4  5     7
  / \           \    /\
  2  3           6  8  9
                        \
                         0
```

从下面开始区分下，如果是Weighted quick-union，可以看出树的高度是可控的，高度不会超过logN。
```
uf.union(5, 7);

   1     3  4        7
  / \              / /\
  2  3            5 8  9
                   \    \
                    6    0

uf.union(2, 8);

         3  4        7
                  /\  /\
                 1  5 8 9
                /\  /    \
               2  3 6     0
```

不按照树的重量来，那么可能会发展成一颗不可控的高树，进而影响find的效率。
```
uf.union(5, 7);

   1     3  4  5
  / \         / \
  2  3       6   7
                 /\
                8  9
                    \
                     0

uf.union(2, 8);

   1     3  4
  /\\
 2 3 5
    / \
   6   7
       /\
      8  9
          \
           0
```








