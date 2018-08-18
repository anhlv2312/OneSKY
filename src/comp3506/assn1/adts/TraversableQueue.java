package comp3506.assn1.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TraversableQueue implement IterableQueue with ability to 
 * iterate over all the elements in the queue.
 * 
 * @author Vu Anh LE <s4490763@student.uq.edu.au>
 *
 * @param <T> Type of the elements held in the queue.
 *
 */
public class TraversableQueue<T> implements IterableQueue<T> {
	
	private static final int MAX_LENGTH = 20000;
	private Node<T> head = null;
	private Node<T> tail = null;
	private int size = 0;

	/**
	 * Return new Iterator object each time it is called.
	 */
	@Override
	public Iterator<T> iterator() {
		return new TraversableQueueIterator<T>(head);
	}

	/**
	 * Add a new element to the end of the queue.
	 * 
	 * @param element The element to be added to the queue.
	 * @throws IllegalStateException Queue cannot accept a new element (e.g. queue space is full).
	 * 
	 * Time-complexity O(1)
	 * 
	 */
	@Override
	public void enqueue(T element) throws IllegalStateException {
		if (size() == MAX_LENGTH) throw new IllegalStateException();
		Node<T> newNode = new Node<>(element, null); 
		if (size == 0)
			head = newNode;
		else
			tail.setNext(newNode);
		tail = newNode;
		size++;
	}

	/**
	 * Remove and return the element at the head of the queue.
	 * 
	 * @return Element at that was at the head of the queue.
	 * @throws IndexOutOfBoundsException Queue is empty and nothing can be dequeued.
	 * 
	 * Time-complexity O(1)
	 * 
	 */
	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (size == 0) throw new IndexOutOfBoundsException();
		T element = head.getElement();
		head = head.getNext();
		size--;
		if (size == 0)
			tail = null;
		return element;
	}
	
	/**
	 * @return Number of elements in the queue.
	 * 
	 * Time-complexity O(1)
	 * 
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Nested Node class that stores the reference to its element and to the next node
	 *
	 * @param <E> The type of element held in the Node.
	 *
	 */
	private static class Node<E> { 
		private E element;
		private Node<E> nextNode;

		public Node(E element, Node<E> nextNode) {
			this.element = element;
			this.setNext(nextNode);
		}

		public E getElement() {
			return element;
		}

		public Node<E> getNext() {
			return nextNode;
		}

		public void setNext(Node<E> nextNode) {
			this.nextNode = nextNode;
		}
	}
	
	/**
	 * Nested Iterator class for TraversableQueue
	 *
	 * @param <E> The type of element held in the Node.
	 *
	 */
	private class TraversableQueueIterator<E> implements Iterator<E> {
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
			nextNode = nextNode.getNext();
			return element;
		}
	}
}


/**
 * Design choices justification
 *  
 * Implement the Queue with a Singly Linked List
 * 
 * REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
