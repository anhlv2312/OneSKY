package comp3506.assn1.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TraversableQueue implement IterableQueue with ability to iterate over all the
 * elements in the queue.
 * 
 * Memory Usage: O(n) (n is the number of aircraft)
 * 
 * @author Vu Anh LE <s4490763@student.uq.edu.au>
 *
 * @param <T> Type of the elements held in the queue.
 * 
 */
public class TraversableQueue<T> implements IterableQueue<T> {

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
	 * Time-complexity O(1) (n is the number of aircraft)
	 * 
	 * @param element The element to be added to the queue.
	 * @throws IllegalStateException Queue cannot accept a new element (e.g. queue
	 *                               space is full).
	 * 
	 */
	@Override
	public void enqueue(T element) throws IllegalStateException {
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
	 * Time-complexity O(1)
	 * 
	 * @return Element at that was at the head of the queue.
	 * @throws IndexOutOfBoundsException Queue is empty and nothing can be dequeued
	 * 
	 */
	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (size == 0)
			throw new IndexOutOfBoundsException();
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
	 *         Time-complexity O(1)
	 * 
	 */
	@Override
	public int size() {
		return size;
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

		TraversableQueueIterator(Node<E> firstNode) {
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

	/**
	 * Nested Node class that stores the reference to its element and to the next
	 * node
	 *
	 * @param <E> The type of element held in the Node.
	 *
	 */
	private static class Node<E> {
		private E element;
		private Node<E> nextNode;

		Node(E element, Node<E> nextNode) {
			this.element = element;
			this.setNext(nextNode);
		}

		E getElement() {
			return element;
		}

		Node<E> getNext() {
			return nextNode;
		}

		void setNext(Node<E> nextNode) {
			this.nextNode = nextNode;
		}
	}

}

/**
 * Design choices justification:
 * 
 * Since it may have only a few aircraft in one cell, and we need to iterate
 * over the queue then I choose to implement a queue use a Singly Linked List
 * [1] as the data structure. They Queue only need to be singly because we only
 * have to iterate one way, from first to last. With this design choice, we can
 * access enqueue and dequeue with the time complexity of O(1).
 * 
 * This data structure has the memory efficiency of O(n) with n is the number of
 * element stored in the data structure.
 * 
 * There is another approach it that using an array to store information.
 * However it would be more memory consuming and more complicated to implement
 * Iterator.
 * 
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
