package net.neoremind.mycode.argorithm.tree;

/**
 * 线段树的一个经典应用
 * <p>
 * 一个宽度已知的光束打下来，然后中间插几个木板，求若干阴影的宽度和
 * <p>
 * <pre>
 *              光束
 *               |
 *              \/
 *
 *
 *                 -------
 *     --------        --       ------
 *  -----                   -------
 *
 *  ======================================地面
 * </pre>
 * <p>
 * http://www.tuicool.com/articles/jaINruq
 *
 * @author zhangxu
 */
public class SegamentTree {

    public static void main(String[] args) {
        SegamentTree segamentTree = new SegamentTree(10);
        segamentTree.insertBoard(1, 3);
        segamentTree.insertBoard(2, 4);
        segamentTree.insertBoard(5, 5);
        System.out.println(segamentTree.count());
    }

    SegmentTreeNode root = null;

    public SegamentTree(int size) {
        root = buildTree(0, size);
    }

    private SegmentTreeNode buildTree(int start, int end) {
        if (start > end) {
            return null;
        } else {
            SegmentTreeNode ret = new SegmentTreeNode(start, end);
            if (start == end) {
                // do nothing
            } else {
                int mid = start + (end - start) / 2;
                ret.left = buildTree(start, mid);
                ret.right = buildTree(mid + 1, end);
            }
            return ret;
        }
    }

    void insertBoard(int left, int right) {
        insertBoard(root, left, right);
    }

    void insertBoard(SegmentTreeNode root, int left, int right) {
        if (root.start == root.end) {
            root.isFullCover = true;
        } else if (root.left.start == left && root.right.end == right) {
            root.isFullCover = true;
        } else {
            int mid = root.start + (root.end - root.start) / 2;
            if (right <= mid) {
                insertBoard(root.left, left, right);
            } else if (left >= mid + 1) {
                insertBoard(root.right, left, right);
            } else {
                insertBoard(root.left, left, mid);
                insertBoard(root.right, mid + 1, right);
            }
        }
    }

    public int count() {
        return count(root);
    }

    public int count(SegmentTreeNode root) {
        if (root.isFullCover == true) {
            return root.end - root.start + 1;
        } else if (root.left == root.right) {
            if (root.isFullCover) {
                return 1;
            }
            return 0;
        }
        return count(root.left) + count(root.right);
    }

    class SegmentTreeNode {
        int start, end;
        SegmentTreeNode left, right;
        boolean isFullCover;

        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.left = null;
            this.right = null;
            this.isFullCover = false;
        }

        @Override
        public String toString() {
            return "[" + start + ", " + end + ",cover=" + isFullCover + "]";
        }
    }
}
