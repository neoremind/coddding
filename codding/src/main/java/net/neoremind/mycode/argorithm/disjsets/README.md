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


