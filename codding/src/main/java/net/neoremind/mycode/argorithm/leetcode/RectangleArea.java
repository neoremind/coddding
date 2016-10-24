package net.neoremind.mycode.argorithm.leetcode;

/**
 * Find the total area covered by two rectilinear rectangles in a 2D plane.
 * <p>
 * Each rectangle is defined by its bottom left corner and top right corner as shown in the figure.
 * <p>
 * <pre>
 *      ----------------- C
 *     |                 |
 *     |                 |
 *      -----------------
 *    A               ----------------------    -> X axis
 *                   |                      |
 *                  E ---------------------- G
 * </pre>
 * <p>
 * <pre>
 *      ----------------- D
 *     |                 |
 *     |                 |
 *      -----------------                     ^ Y axis
 *    B               ---------------------- H
 *                   |                      |
 *                  F ----------------------
 * </pre>
 * <p>
 * 关键判断是否overlap：
 * <p>
 * 1）left=左“边”最大，right=右“边”最小，如果overlap，那么left<right。
 * <p>
 * 同理,
 * 2）top=上“边”最小，bottom=下“边”最大，如果overlap，那么top>bottom。
 * <p>
 * 然后重合的大小就(right - left) * (top - bottom)
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/rectangle-area/
 */
public class RectangleArea {

    public int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
        int area1 = (C - A) * (D - B);
        int area2 = (G - E) * (H - F);
        int left = Math.max(A, E);
        int right = Math.min(C, G);
        int top = Math.min(D, H);
        int bottom = Math.max(B, F);
        int overlap = 0;
        if (right > left && top > bottom) {
            overlap = (right - left) * (top - bottom);
        }
        return area1 + area2 - overlap;
    }

}
