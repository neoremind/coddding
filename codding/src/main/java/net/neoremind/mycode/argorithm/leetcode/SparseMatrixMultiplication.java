package net.neoremind.mycode.argorithm.leetcode;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Given two sparse matrices A and B, return the result of AB.
 * <p>
 * You may assume that A’s column number is equal to B’s row number.
 * <p>
 * 这道题让我们实现稀疏矩阵相乘，稀疏矩阵的特点是矩阵中绝大多数的元素为0，而相乘的结果是还应该是稀疏矩阵，即还是大多数元素为0，那么我们使用传统的矩阵相乘的算法肯定会处理大量的0乘0
 * 的无用功，所以我们需要适当的优化算法，使其可以顺利通过OJ，我们知道一个 i x k 的矩阵A乘以一个 k x j 的矩阵B会得到一个 i x j
 * 大小的矩阵C，那么我们来看结果矩阵中的某个元素C[i][j]是怎么来的，起始是A[i][0]*B[0][j] + A[i][1]*B[1][j] + ... +
 * A[i][k]*B[k][j]，那么为了不重复计算0乘0，我们首先遍历A数组，要确保A[i][k]不为0，才继续计算，然后我们遍历B矩阵的第k行，如果B[K][J]不为0，我们累加结果矩阵res[i][j] += A[i][k]
 * * B[k][j]; 这样我们就能高效的算出稀疏矩阵的乘法，参见代码如下
 * <p>
 * 扩展问题，如果要做矩阵的dot product呢？
 *
 * @author xu.zhang
 */
public class SparseMatrixMultiplication {

    public int[][] multiply(int[][] A, int[][] B) {
        int m = A.length, n = A[0].length, nB = B[0].length;
        int[][] C = new int[m][nB];

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                if (A[i][k] != 0) {
                    for (int j = 0; j < nB; j++) {
                        if (B[k][j] != 0) C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }
        }
        return C;
    }

    //TODO???
    public int[][] multiply2(int[][] A, int[][] B) {
        int m = A.length, n = A[0].length, mB = B.length, nB = B[0].length;
        int[][] C = new int[m][nB];

        TreeMap<Integer, Integer> mapA = new TreeMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] != 0) {
                    mapA.put(i * n + j % n, A[i][j]);
                }
            }
        }

        TreeMap<Integer, Integer> mapB = new TreeMap<>();
        for (int i = 0; i < mB; i++) {
            for (int j = 0; j < nB; j++) {
                if (B[i][j] != 0) {
                    mapB.put(j * mB + i % mB, B[i][j]);
                }
            }
        }

        for (Map.Entry<Integer, Integer> e : mapB.entrySet()) {
            if (mapB.containsKey(e.getKey())) {
                C[e.getKey() / m + e.getKey() % m][e.getKey() / nB + e.getKey() % nB] += e.getValue() * mapB.get(e
                        .getKey());
            }
        }

        return C;
    }

    public int multiply3(int[][] A, int[][] B) {
        int m = A.length, n = A[0].length, mB = B.length, nB = B[0].length;
        int[][] C = new int[m][nB];

        TreeMap<Integer, Integer> mapA = new TreeMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] != 0) {
                    mapA.put(i * n + j % n, A[i][j]);
                }
            }
        }

        TreeMap<Integer, Integer> mapB = new TreeMap<>();
        for (int i = 0; i < mB; i++) {
            for (int j = 0; j < nB; j++) {
                if (B[i][j] != 0) {
                    mapB.put(j * mB + i % mB, B[i][j]);
                }
            }
        }
        int res = 0;
        for (Map.Entry<Integer, Integer> e : mapB.entrySet()) {
            if (mapA.containsKey(e.getKey())) {
                res += e.getValue() * mapA.get(e.getKey());
            }
        }

        return res;
    }

    @Test
    public void test() {
        int array1[][] = {{5, 6, 7}, {4, 8, 9}};
        int array2[][] = {{6, 4}, {5, 7}, {1, 1}};
        System.out.println("Matrix 1:");
        Arrays.asList(array1).stream().forEach(e -> System.out.println(ArrayUtils.toString(e)));
        System.out.println("Matrix 2:");
        Arrays.asList(array2).stream().forEach(e -> System.out.println(ArrayUtils.toString(e)));
        int result[][] = multiply(array1, array2);
        System.out.println("Result:");
        Arrays.asList(result).stream().forEach(e -> System.out.println(ArrayUtils.toString(e)));

        System.out.println(multiply3(array1, array2));
    }
}
