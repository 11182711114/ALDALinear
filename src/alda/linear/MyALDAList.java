package alda.linear;
//Fredrik Larsson frla9839 flarsson93@gmail.com

import java.util.Iterator;
import java.util.NoSuchElementException;

import alda.linear.MyALDAList;

public class MyALDAList<T> implements ALDAList<T>{
	private Node<T> head;
	private Node<T> tail;
	private int size;
	
	public MyALDAList(){
		size = 0;
		tail = new Node<>(null,null);
		head = new Node<>(tail,null);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new MyALDAListIterator();
	}

	@Override
	public void add(T element) {
		Node<T> newTail = new Node<>(null,null);
		tail.next = newTail;
		tail.ref = element;
		tail = newTail;
		size++;
	}

	@Override
	public void add(int index, T element) {
		if(index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		
		Node<T> current = head;
		
		for(int i = -1; i<index;i++){
			if(i == index-1){
				Node<T> before = current;
				Node<T> after = current.next;
				before.next = new Node<T>(after,element);
				break;
			}
			current = current.next;
		}
		size++;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index > size-1)
			throw new IndexOutOfBoundsException();
		
		Node<T> current = head;
		
		for(int i = -1; i<index;i++){
			if(i == index-1){
				Node<T> before = current;
				Node<T> after = current.next.next;
				current = current.next;
				before.next = after;
				break;
			}
			current = current.next;
		}
		
		size--;
		return current.ref;
	}

	@Override
	public boolean remove(T element) {
// 		simple solution from a programmers perspective, however it iterates the list twice -> not optimal
		
//		int i = indexOf(element);
//		if(i < 0)
//			return false;
//		remove(i);
//		return true;
		
		Node<T> current = head;
		for(int i = -1; i < size-1 ; i++){
			if(current.next.ref == element){
				Node<T> before = current;
				Node<T> after = current.next.next;
				current = current.next;
				before.next = after;
				size--;
				return true;
			}
			current = current.next;
		}
		return false;
	}
	
	@Override
	public T get(int index) {
		if(index < 0 || index > size-1)
			throw new IndexOutOfBoundsException();
		
		Node<T> current = head;
		
		for(int i = -1; i<=index;i++){
			if(i == index){
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
		
		for(int i = -1; i<=size;i++){
			if(current.ref == element){
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

		//FIXME special case, can I make it general?
		for(Iterator<T> iter = iterator(); iter.hasNext(); ){
			T item = iter.next();
			if(iter.hasNext())
				output+= item + ", ";
			else
				output+= item;	
		}
		
		return output + "]";
	}

	private static class Node<T>{
		T ref;
		Node<T> next;
		
		Node(Node<T> next, T ref) {
			this.next = next;
			this.ref = ref;
		}
	}
	private class MyALDAListIterator implements Iterator<T>{
		Node<T> current = head;
		boolean removeActive = false;
		
		@Override
		public boolean hasNext() {
			return current.next != tail;
		}

		@Override
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			
			current = current.next;
			removeActive = true;
			
			return current.ref;
		}
		@Override
		public void remove(){
			if(!removeActive)
				throw new IllegalStateException();
			
			MyALDAList.this.remove(current.ref);
			removeActive = false;
		}
	}
}
