package net.neoremind.mycode.concurrent.atomic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import net.neoremind.mycode.guava.vo.Person;

/**
 * 
 * ClassName: AtomicReferenceStack <br/>
 * Function: 无锁的，线程安全的堆栈
 *
 * @author zhangxu
 */
public class AtomicReferenceStack<T> {

	private AtomicReference<Node<T>> stacks = new AtomicReference<Node<T>>();

	@Test
	public void test() {
		AtomicReferenceStack<Person> stack = new AtomicReferenceStack<Person>();
		Person person = new Person("bob", 12);
		stack.push(person);
		Person person2 = stack.pop();
		assertThat(person, is(person2));
	}

	public T push(T e) {
		Node<T> oldNode, newNode;
		while (true) { // 这里的处理非常的特别，也是必须如此的。
			oldNode = stacks.get();
			newNode = new Node<T>(e, oldNode);
			if (stacks.compareAndSet(oldNode, newNode)) {
				return e;
			}
		}
	}

	public T pop() {
		Node<T> oldNode, newNode;
		while (true) {
			oldNode = stacks.get();
			newNode = oldNode.next;
			if (stacks.compareAndSet(oldNode, newNode)) {
				return oldNode.object;
			}
		}
	}

	private static final class Node<T> {
		private T object;

		private Node<T> next;

		private Node(T object, Node<T> next) {
			this.object = object;
			this.next = next;
		}
	}
}
