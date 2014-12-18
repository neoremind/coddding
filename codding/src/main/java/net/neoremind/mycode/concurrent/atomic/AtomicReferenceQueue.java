package net.neoremind.mycode.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 
 * ClassName: AtomicReferenceQueue <br/>
 * 
 * http://www.ibm.com/developerworks/cn/java/j-jtp04186/
 *
 * @author zhangxu
 */
public class AtomicReferenceQueue<E> {
	
	public static void main(String[] args) {
		AtomicReferenceQueue<String> queue = new AtomicReferenceQueue<String>();
		queue.put("123");
		queue.put("456");
		queue.put("789");
	}
	
	private static class Node<E> {
		@SuppressWarnings("unused")
		final E item;
		final AtomicReference<Node<E>> next;

		Node(E item, Node<E> next) {
			this.item = item;
			this.next = new AtomicReference<Node<E>>(next);
		}
	}

	private AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(new Node<E>(null, null));
	private AtomicReference<Node<E>> tail = head;

	public boolean put(E item) {
		Node<E> newNode = new Node<E>(item, null);
		while (true) {
			Node<E> curTail = tail.get();
			Node<E> residue = curTail.next.get();
			if (curTail == tail.get()) {
				if (residue == null) /* A */{
					if (curTail.next.compareAndSet(null, newNode)) /* C */{
						tail.compareAndSet(curTail, newNode) /* D */;
						return true;
					}
				} else {
					tail.compareAndSet(curTail, residue) /* B */;
				}
			}
		}
	}
}
