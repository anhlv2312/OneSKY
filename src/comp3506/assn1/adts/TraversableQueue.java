package comp3506.assn1.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TraversableQueue<T> implements IterableQueue<T> {

	private static final int MAX_LENGTH = 20000;
	private IterableSinglyLinkedList<T> list = new IterableSinglyLinkedList<>();

	@Override
	public Iterator<T> iterator() {
		return list.getIterator();
	}

	@Override
	public void enqueue(T element) throws IllegalStateException {
		if (list.size() == MAX_LENGTH)
			throw new IllegalStateException();
		else
			list.addLast(element);
	}

	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		T element = list.removeFirst();
		if (element == null)
			throw new IndexOutOfBoundsException();
		else
			return element;
	}

	@Override
	public int size() {
		return list.size();
	}
}

class IterableSinglyLinkedList<T> extends SinglyLinkedList<T> {

	class SinglyLinkedListIterator implements Iterator<T> {

		Node<T> currentNode = null;
		Node<T> nextNode;

		public SinglyLinkedListIterator(Node<T> head) {
			currentNode = null;
			nextNode = head;
		}

		@Override
		public boolean hasNext() {
			return (nextNode != null);
		}

		@Override
		public T next() {
			if (nextNode == null) 
				throw new NoSuchElementException();
			currentNode = nextNode;
			T element = currentNode.getElement();
			nextNode = nextNode.getNext();
			return element;
		}
	}

	public Iterator<T> getIterator() {
		return new SinglyLinkedListIterator(head);
	}

}

/* [1 pp. 126] */
class SinglyLinkedList<E> {

	// ---------------- nested Node class ----------------
	static class Node<E> {

		private E element; // reference to the element stored at this node
		private Node<E> next; // reference to the subsequent node in the list

		public Node(E e, Node<E> n) {
			element = e;
			next = n;
		}

		public E getElement() {
			return element;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> n) {
			next = n;
		}
	}
	// ---------- end of nested Node class --------------

	// instance variables of the SinglyLinkedList
	protected Node<E> head = null; // head node of the list (or null if empty)
	protected Node<E> tail = null; // last node of the list (or null if empty)
	private int size = 0; // number of nodes in the list

	public SinglyLinkedList() {
	} // constructs an initially empty list

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

//	// returns (but does not remove) the first element
//	public E first() {
//		if (isEmpty())
//			return null;
//		return head.getElement();
//	}
//
//	// returns (but does not remove) the last element
//	public E last() {
//		if (isEmpty())
//			return null;
//		return tail.getElement();
//	}
//
//	// adds element e to the front of the list
//	public void addFirst(E e) {
//		head = new Node<>(e, head); // create and link a new node
//		if (size == 0)
//			tail = head; // special case: new node becomes tail also
//		size++;
//	}

	// adds element e to the end of the list
	public void addLast(E e) {
		Node<E> newest = new Node<>(e, null); // node will eventually be the tail
		if (isEmpty())
			head = newest; // special case: previously empty list
		else
			tail.setNext(newest); // new node after existing tail
		tail = newest; // new node becomes the tail
		size++;
	}

	// removes and returns the first element
	public E removeFirst() {
		if (isEmpty())
			return null; // nothing to remove
		E answer = head.getElement();
		head = head.getNext(); // will become null if list had only one node
		size--;
		if (size == 0)
			tail = null; // special case as list is now empty
		return answer;
	}
}

/* REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 */



