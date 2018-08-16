package comp3506.assn1.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 */

public class TraversableQueue<T> implements IterableQueue<T> {

	static class Node<E> {
		/* [1 pp. 126] */
		private E element; // reference to the element stored at this node
		private Node<E> nextNode; // reference to the subsequent node in the list

		public Node(E element, Node<E> nextNode) {
			this.element = element;
			this.setNextNode(nextNode);
		}

		public E getElement() {
			return element;
		}

		public Node<E> getNextNode() {
			return nextNode;
		}

		public void setNextNode(Node<E> nextNode) {
			this.nextNode = nextNode;
		}
	}
	
	class TraversableQueueIterator<E> implements Iterator<E> {
		private Node<E> currentNode = null;
		private Node<E> nextNode = null;
		
		public TraversableQueueIterator(Node<E> firstNode) {
			this.nextNode = firstNode;
		}

		@Override
		public boolean hasNext() {
			return (nextNode != null);
		}

		@Override
		public E next() {
			if (nextNode == null) 
				throw new NoSuchElementException();
			currentNode = nextNode;
			E element = currentNode.getElement();
			nextNode = nextNode.getNextNode();
			return element;
		}
	}
	
	private static final int MAX_LENGTH = 20000;
	private Node<T> head = null;
	private Node<T> tail = null;
	private int size = 0;

	@Override
	public Iterator<T> iterator() {
		return new TraversableQueueIterator<T>(head);
	}

	@Override
	public void enqueue(T element) throws IllegalStateException {
		if (size() == MAX_LENGTH) throw new IllegalStateException();

		/* [1 pp. 126] */
		Node<T> newNode = new Node<>(element, null); // node will eventually be the tail
		if (size == 0)
			head = newNode; // special case: previously empty list
		else
			tail.setNextNode(newNode); // new node after existing tail
		tail = newNode; // new node becomes the tail
		size++;
	}

	@Override
	public T dequeue() throws IndexOutOfBoundsException {

		if (size == 0) throw new IndexOutOfBoundsException();

		/* [1 pp. 126] */
		T element = head.getElement();
		head = head.getNextNode(); // will become null if list had only one node
		size--;
		if (size == 0)
			tail = null; // special case as list is now empty
		return element;
	}
	
	@Override
	public int size() {
		return size;
	}
}



