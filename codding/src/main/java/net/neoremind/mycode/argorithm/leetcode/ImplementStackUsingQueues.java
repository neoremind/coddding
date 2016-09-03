package net.neoremind.mycode.argorithm.leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implement the following operations of a stack using queues.
 * <p>
 * push(x) -- Push element x onto stack.
 * pop() -- Removes the element on top of the stack.
 * top() -- Get the top element.
 * empty() -- Return whether the stack is empty.
 * Notes:
 * You must use only standard operations of a queue -- which means only push to back, peek/pop from front, size, and
 * is empty operations are valid.
 * Depending on your language, queue may not be supported natively. You may simulate a queue by using a list or deque
 * (double-ended queue), as long as you use only standard operations of a queue.
 * You may assume that all operations are valid (for example, no pop or top operations will be called on an empty
 * stack).
 * <p>
 * There are two solutions cost O(n) and O(1) for different operations:
 * <p>
 * push: O(n), pop/top: O(1)
 * push: O(1), pop/top: O(n)
 * Time efficiency depends on operation frequency of push, pop, top:
 * if push>pop+top, second solution is better.
 * if push<pop+top, first solution is better.
 * <p>
 * And I feel, in most cases, push<pop+top.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/implement-stack-using-queues/
 */
public class ImplementStackUsingQueues {

    class MyStack1 {
        Queue<Integer> q = new LinkedList<Integer>();

        // Push element x onto stack.
        public void push(int x) {
            q.add(x);
        }

        // Removes the element on top of the stack.
        public void pop() {
            int size = q.size();
            for (int i = 1; i < size; i++) {
                q.add(q.remove());
            }
            q.remove();
        }

        // Get the top element.
        public int top() {
            int size = q.size();
            for (int i = 1; i < size; i++) {
                q.add(q.remove());
            }
            int ret = q.remove();
            q.add(ret);
            return ret;
        }

        // Return whether the stack is empty.
        public boolean empty() {
            return q.isEmpty();
        }
    }

    class MyStack2 {
        Queue<Integer> queue = new LinkedList<Integer>();

        // Push element x onto stack.
        public void push(int x) {
            queue.add(x);
            for (int i = 0; i < queue.size() - 1; i++) {
                queue.add(queue.poll());
            }
        }

        // Removes the element on top of the stack.
        public void pop() {
            queue.poll();
        }

        // Get the top element.
        public int top() {
            return queue.peek();
        }

        // Return whether the stack is empty.
        public boolean empty() {
            return queue.isEmpty();
        }
    }

}
