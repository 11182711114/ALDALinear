package alda.linear;
//Fredrik Larsson frla9839 flarsson93@gmail.com

import java.util.Iterator;
import java.util.NoSuchElementException;

import alda.linear.MyALDAList;

// Should be MyAldaList according to Google Java Style guide. https://google.github.io/styleguide/javaguide.html#s5.3-camel-case
/** @author fredrik */
public class MyALDAList<T> implements ALDAList<T> {
	private Node<T> head;
	private Node<T> tail;
	private int size;

	public MyALDAList() {
		size = 0;
		tail = new Node<>(null, null);
		head = new Node<>(tail, null);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new MyALDAListIterator();
	}

	@Override
	public void add(T element) {
		Node<T> newTail = new Node<>(null, null);
		tail.next = newTail;
		tail.ref = element;
		tail = newTail;
		size++;
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();

		Node<T> current = head;
		for (int i = -1; i < index; i++) {
			if (i == index - 1) {
				Node<T> before = current;
				Node<T> after = current.next;
				before.next = new Node<T>(after, element);
				break;
			}
			current = current.next;
		}
		
		size++;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException();

		Node<T> current = head;
		for (int i = -1; i < index; i++) {
			if (i == index - 1) {
				T toReturn = current.next.ref;
				removeNextNode(current);
				return toReturn;
			}
			current = current.next;
		}
		
		return null;
	}

	@Override
	public boolean remove(T element) {
		Node<T> nodeBefore = findNodeBeforeElement(element);
		if (nodeBefore != null) {
			removeNextNode(nodeBefore);
			return true;
		}
		
		return false;
	}

	private void removeNextNode(Node<T> beforeRefNode) {
		Node<T> after = beforeRefNode.next.next;
		beforeRefNode.next = after;
		size--;
	}

	private Node<T> findNodeBeforeElement(T element) {
		Node<T> current = head;
		for (int i = -1; i < size - 1; i++) {
			if (current.next.ref == element)
				return current;
			current = current.next;
		}
		
		return null;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException();

		Node<T> current = head;
		for (int i = -1; i <= index; i++) {
			if (i == index) {
				break;
			}
			current = current.next;
		}
		return current.ref;
	}

	@Override
	public boolean contains(T element) {
		return indexOf(element) > -1;
	}

	@Override
	public int indexOf(T element) {
		
		Node<T> current = head;
		for (int i = -1; i <= size; i++) {
			if (current.ref == element) {
				return i;
			}
			current = current.next;
		}
		
		return -1;
	}

	@Override
	public void clear() {
		head.next = tail;
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		String output = "[";

		for (Iterator<T> iter = iterator(); iter.hasNext();) {
			T item = iter.next();
			if (iter.hasNext())
				output += item + ", ";
			else
				output += item;
		}

		return output + "]";
	}

	private static class Node<T> {
		T ref;
		Node<T> next;

		Node(Node<T> next, T ref) {
			this.next = next;
			this.ref = ref;
		}
	}

	private class MyALDAListIterator implements Iterator<T> {
		Node<T> prev = null;
		Node<T> current = head;
		boolean removeActive = false;

		@Override
		public boolean hasNext() {
			return current.next != tail;
		}

		@Override
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			prev = current;
			current = current.next;
			removeActive = true;

			return current.ref;
		}

		@Override
		public void remove() {
			if (!removeActive)
				throw new IllegalStateException();

			MyALDAList.this.removeNextNode(prev);
			removeActive = false;
		}
	}
}
