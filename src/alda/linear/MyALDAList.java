package alda.linear;
//Fredrik Larsson frla9839 flarsson93@gmail.com

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import alda.linear.MyALDAList;

/* 
 * Should be MyAldaList according to Google Java Style guide. https://google.github.io/styleguide/javaguide.html#s5.3-camel-case
 * It is also named MyAldaList in the instructions on iLearn but not in the actual code references.
 * https://ilearn2.dsv.su.se/mod/page/view.php?id=46589 "Namnet på er listklass ska vara MyAldaList ..."
*/
/** Unidirectonal implementation of ALDAList<T>
 * @author fredrik */
public class MyALDAList<T> implements ALDAList<T> {
	private Node<T> head;
	private Node<T> tail;
	private int size;

	public MyALDAList() {
		size = 0;
		tail = new Node<>(null, null);
		head = new Node<>(tail, null);
	}
	
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
//		v1, no iterator
//		Node<T> nodeBefore = findNodeBeforeElement(element);
//		if (nodeBefore != null) {
//			removeNextNode(nodeBefore);
//			return true;
//		}
//		v2, using iterator
		for (Iterator<T> iter = iterator(); iter.hasNext();){
			T item = iter.next();
			if (item == element){
				iter.remove();
				return true;
			}
		}
		
		return false;
	}

	private void removeNextNode(Node<T> beforeRefNode) {
		Node<T> after = beforeRefNode.next.next;
		beforeRefNode.next = after;
		size--;
	}
	
	@Deprecated 
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

	private class MyALDAListIterator implements ListIterator<T> {
		Node<T> prev = null;
		Node<T> current = head;
		boolean removeActive = false;
		int index = -1;

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
			index++;
			return current.ref;
		}

		@Override
		public void remove() {
			if (!removeActive)
				throw new IllegalStateException();

			MyALDAList.this.removeNextNode(prev);
			index--;
			removeActive = false;
		}

		@Override
		public void add(T element) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasPrevious() {
			return prev != null && prev != head;
		}

		@Override
		public int nextIndex() {
			return index + 1;
		}

		@Override
		public T previous() {
			if(!hasPrevious())
				throw new NoSuchElementException();
			current = prev;
			prev = null;
			return current.ref;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void set(T element) {
			if(!removeActive)
				throw new NoSuchElementException();
			current.ref = element;
		}
	}
}
