package net.neoremind.mycode.argorithm.tree;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class BST<T extends Comparable<T>> {

    private Node root;

    private int elementsCount;

    public int getElementsCount() {
        return elementsCount;
    }

    public int depth() {
        return depth(root);
    }

    public int depth(Node node) {
        int depthLeft = 0;
        int depthRight = 0;

        // 左子树的深度
        if (node.getLeft() != null) {
            depthLeft = depth(node.getLeft()) + 1;
        }

        // 右子树的深度
        if (node.getRight() != null) {
            depthRight = depth(node.getRight()) + 1;
        }
        return Math.max(depthLeft, depthRight);
    }

    public void insert(T[] arr) {
        if (arr == null) {
            return;
        }
        for (T t : arr) {
            insertByLoop(root, t);
        }
        elementsCount = elementsCount + arr.length;
    }

    public Node insertByLoop(Node node, T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            root = new Node(t);
            return root;
        }

        if (node == null) {
            node = new Node(t);
        } else {
            if (t.compareTo(node.getValue()) <= 0) {
                node.setLeft(insertByLoop(node.getLeft(), t));
            } else {
                node.setRight(insertByLoop(node.getRight(), t));
            }
        }

        return node;
    }

    public Node insertInteratively(T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            root = new Node(t);
            return root;
        }

        Node curr = root;
        Node ret = new Node(t);
        for (Node tmp = curr; tmp != null; curr = tmp) {
            if (t.compareTo(curr.getValue()) <= 0) {
                tmp = curr.getLeft();
                if (tmp == null) {
                    curr.setLeft(ret);
                }
            } else {
                tmp = curr.getRight();
                if (tmp == null) {
                    curr.setRight(ret);
                }
            }
        }
        return ret;
    }

    public void preOrder(Node root) {
        if (root != null) {
            System.out.print(root.getValue() + "-");
            preOrder(root.getLeft());
            preOrder(root.getRight());
        }
    }

    public void inOrder(Node root) {
        if (root != null) {
            inOrder(root.getLeft());
            System.out.print(root.getValue() + "-");
            inOrder(root.getRight());
        }
    }

    public void postOrder(Node root) {
        if (root != null) {
            postOrder(root.getLeft());
            postOrder(root.getRight());
            System.out.print(root.getValue() + "-");
        }
    }

    public static void main(String[] args) throws IOException {
        BST<Integer> bst = new BST<Integer>();
        bst.insert(new Integer[] {12, 76, 35, 22, 16, 48, 90, 46, 9, 40});
        bst.preOrder(bst.getRoot());
        System.out.println();
        bst.inOrder(bst.getRoot());
        System.out.println();
        bst.postOrder(bst.getRoot());
        System.out.println();
        System.out.println(bst.depth());
        System.out.println(bst.getElementsCount());
        OutputStreamWriter osr = new OutputStreamWriter(System.out);
        bst.getRoot().printTree(osr);
        osr.flush();
        osr.close();
    }

    public Node getRoot() {
        return root;
    }

    /**
     * 双向链表Node节点
     */
    class Node {

        private T value;

        private Node left;

        private Node right;

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return value.toString();
        }

        /**
         * Creates a new instance of Node.
         *
         * @param value
         */
        public Node(T value) {
            this.value = value;
        }

        // getter and setter

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public void printTree(OutputStreamWriter out) throws IOException {
            if (right != null) {
                right.printTree(out, true, "");
            }
            printNodeValue(out);
            if (left != null) {
                left.printTree(out, false, "");
            }
        }

        private void printNodeValue(OutputStreamWriter out) throws IOException {
            out.write(value.toString());
            out.write('\n');
        }

        // use string and not stringbuffer on purpose as we need to change the indent at each recursion
        private void printTree(OutputStreamWriter out, boolean isRight, String indent) throws IOException {
            if (right != null) {
                right.printTree(out, true, indent + (isRight ? "        " : " |      "));
            }
            out.write(indent);
            if (isRight) {
                out.write(" /");
            } else {
                out.write(" \\");
            }
            out.write("----- ");
            printNodeValue(out);
            if (left != null) {
                left.printTree(out, false, indent + (isRight ? " |      " : "        "));
            }
        }

    }

}
