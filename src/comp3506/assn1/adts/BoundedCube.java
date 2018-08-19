package comp3506.assn1.adts;

import java.util.Iterator;

/**
 * A three-dimensional data structure that holds items in a positional
 * relationship to each other. Each cell in the data structure can hold multiple
 * items. A bounded cube has a specified maximum size in each dimension. The
 * root of each dimension is indexed from zero.
 * 
 * Memory Usage Efficiency: O(n)
 * (n is the number of aircraft)
 * 
 * @author Vu Anh LE <s4490763@student.uq.edu.au>
 *
 * @param <T> The type of element held in the data structure.
 *
 */
public class BoundedCube<T> implements Cube<T> {

	private final static int MAX_X = 5321;
	private final static int MAX_Y = 3428;
	private final static int MAX_Z = 35;
	
	int lenght, breadth, height;
	
	Object[] layers = new Object[36];
	
	/**
	 * 
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not
	 *  	positive.
	 */
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
		// Validate the aguments
		if ((length <= 0 || breadth <= 0 || height <= 0) 
				|| (length > MAX_X || breadth > MAX_Y || height > MAX_Z)) {
			throw new IllegalArgumentException();
		}
		
		this.lenght = length;
		this.breadth = breadth;
		this.height = height;

		for (int z = 0; z <= height; z++) {
			// Initialize layers of the air space, each layer is 1km height
			layers[z] = new OrderedLinkedList<TraversableQueue<T>>(length, breadth);
		}
		
	}

	/**
	 * Add an element at a fixed position.
	 * 
	 * Time-complexity O(n)
	 * (n is the number of aircraft)
	 * 
	 * @param element The element to be added at the indicated position.
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		// Get the queue form that position
		TraversableQueue<T> queue = getLayer(z).getElement(position);	
		if (queue == null) {
			// If there is no queue in that position, initialize a new Queue
			TraversableQueue<T> newQueue = new TraversableQueue<T>();
			// Enqueue the element to the created queue
			newQueue.enqueue(element);
			// Add the queue to that position
			getLayer(z).setElement(position, newQueue);
		} else {
			// Enqueue the element
			queue.enqueue(element);
		}
	}

	/**
	 * Return the 'oldest' element at the indicated position.
	 * 
	 * Time-complexity O(n)
	 * (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @return 'Oldest' element at this position or null if no elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		
		TraversableQueue<T> queue = getLayer(z).getElement(position);
		// If there is no queue there, return null
		if (queue == null) {
			return null;
		}
		// Initialize Iterator 
		// (It's quite over-complicated but, we don't have to create additional methods) 
		Iterator<T> iterator = queue.iterator();
		// Return the first item.
		return (T)iterator.next();
		
	}

	/**
	 * Return all the elements at the indicated position.
	 * 
	 * Time-complexity O(n)
	 * (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return An IterableQueue of all elements at this position or null if no elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		// Return the whole queue in that position
		return getLayer(z).getElement(position);
	}

	/**
	 * Indicates whether there are more than one elements at the indicated position.
	 * 
	 * Time-complexity O(n)
	 * (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return true if there are more than one elements at the indicated position, false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		TraversableQueue<T> cell = getLayer(z).getElement(position);
		// Return false if null or queue size <= 1, otherwise return true
		if (cell == null) {
			return false;
		} else { 
			return (cell.size() > 1);
		}
	}

	/**
	 * Removes the specified element at the indicated position.
	 * 
	 * Time-complexity O(n)
	 * (n is the number of aircraft)
	 * 
	 * @param element The element to be removed from the indicated position.
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @return true if the element was removed from the indicated position, false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		
		// Get the queue in the position
		TraversableQueue<T> queue = getLayer(z).getElement(position);
		if (queue == null) {
			return false;
		}
		
		// Initialize a new queue
		TraversableQueue<T> newQueue = new TraversableQueue<T>();
		for (T e: queue) {
			// enqueue all the items from old queue to the newQueue except the given element
			if (!e.equals(element)) {
				newQueue.enqueue(e);
			}
		}
		
		// Replace newQueue to that position
		getLayer(z).setElement(position, newQueue);
		
		if (newQueue.size() == 0) {
			getLayer(z).removeNode(position);
		}
		
		// Return true if the size is reduced
		return (newQueue.size() < queue.size());
	}

	/**
	 * Removes all elements at the indicated position.
	 * 
	 * Time-complexity O(n)
	 * (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		// Set the element of the node in that position to null
		getLayer(z).setElement(position, null);
	}

	/**
	 * Removes all elements stored in the cube.
	 * 
	 * Time-complexity O(1)
	 * 
	 */
	@Override
	public void clear() {
		for (int z = 0; z <= this.height; z++) {
			getLayer(z).clear();
		}
	}
	
	
	private Position convertPosition(int x, int y, int z) throws IndexOutOfBoundsException {
		// Validate the x,y,z input and return the position object
		if ((x < 0 || y < 0 || z < 0) || (x > this.lenght || y > this.breadth || z > this.height)) {
			throw new IndexOutOfBoundsException();
		}
		return new Position(x, y);
	}
	
	private OrderedLinkedList<TraversableQueue<T>> getLayer(int layerIndex) {
		return (OrderedLinkedList<TraversableQueue<T>>)layers[layerIndex];

	}
	
	/**
	 * The Position class holds the coordinates information based on x, y, z axis, 
	 *
	 */
	private static class Position implements Comparable<Position> {
		/* [1 pp. 227] */
		private int x, y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		// Compare with other position, z axis has the highest weight, then y and then x,
		@Override
		public int compareTo(Position position) {
			if (this.y != position.y) {
				return this.y - position.y;
			} 
			return this.x - position.x;
		}
		
		public String toString() {
			return "(" + x + "," + y + ")";
		}
	
		
	}
	
	/**
	 * Nested Node class that holds the element and its position information
	 *
	 * @param <E> The type of element held in the Node.
	 *
	 */
	private static class Node<E> {

		private E element;
		private Node<E> nextNode;
		private Node<E> previousNode;
		private Position position;
		
		public Node(Position position, E element, Node<E> previousNode, Node<E> nextNode) {
			this.element = element; 
			this.nextNode = nextNode;
			this.position = position;
		}

		public E getElement() {
			return element;
		}

		public Node<E> getNext() {
			return nextNode;
		}
		
		public Node<E> getPrevious() {
			return previousNode;
		}

		public void setElement(E element) {
			this.element = element;
		}
		
		public void setNext(Node<E> nextNode) {
			this.nextNode = nextNode;
		}

		public void setPrevious(Node<E> previousNode) {
			this.previousNode = previousNode;
		}
		
		public Position getPosition() {
			return position;
		}

		public String toString() {
			String pre, nex;
			if (previousNode == null) {
				pre = "(null)";
			} else { 
				pre = previousNode.getPosition().toString();
			}
			if (nextNode == null) {
				nex = "(null)";
			} else { 
				nex = nextNode.getPosition().toString();
			}
			return "[" + pre + this.getPosition().toString() + nex +"]";
		}
		
	}
	
	/**
	 * Singly Linked List data structure that provides access to its nodes using position
	 * This class is used to represent a layers of the air space
	 *
	 * @param <E> The type of element held in the List.
	 *
	 */
	private class OrderedLinkedList<E> {
	
		private Node<E> header;
		private Node<E> trailer;
		private Node<E> cursor;
		
		public OrderedLinkedList(int x, int y) {
			/* [1 pp. 277] */
			header = new Node<> (new Position(0,0), null, null, null);
			trailer = new Node<> (new Position(x,y), null, null, null);
			header.setNext(trailer);
			trailer.setPrevious(header);
			cursor = header;
		}
		
		public void show() {
			Node<E> currentNode = header;
			while (currentNode != null) {
				System.out.print(currentNode.toString());
				currentNode = currentNode.getNext();
			}
			System.out.println("");
		}
		
		private Node<E> findNode(Position position) {
			if (position.compareTo(cursor.getPosition()) == 0) {
				return cursor;
			} else if (position.compareTo(cursor.getPosition()) < 0) {
				return findNodeLeft(position);
			} else {
				return findNodeRight(position);
			}
		}
		
		// Find a Node by iterating to the beginning
		private Node<E> findNodeRight(Position position) {
			Node<E> currentNode = cursor;
			while (currentNode.getNext() != null) {
				if (position.compareTo(currentNode.getPosition()) == 0) {
					return currentNode;
				}
				currentNode = currentNode.getNext();
			} 
			return null;		
		}
		
		// Find a Node by iterating to the end
		private Node<E> findNodeLeft(Position position) {
			Node<E> currentNode = cursor;
			while (currentNode.getPrevious() != null) {
				if (position.compareTo(currentNode.getPosition()) == 0) {
					return currentNode;
				}
				currentNode = currentNode.getPrevious();
			} 
			return null;		
		}
		
		// Remove the Node from linked list
		public void removeNode(Position position) {
			Node<E> currentNode = this.findNode(position);
			currentNode.getPrevious().setNext(currentNode.getNext());
			currentNode.getNext().setPrevious(currentNode.getPrevious());
		}
		
		public E getElement(Position position) {
			Node<E> node = this.findNode(position);
			if (node != null) {
				return node.getElement();
			}
			return null;
		}
		
		public void setElement(Position position, E element) {
			Node<E> currentNode = this.findNode(position);
			if (currentNode == null) {
				currentNode = this.addNode(position, element);
			} else {
				currentNode.setElement(element);
			}
		}
		
		// Add a new node after a particular node
		public Node<E> addNode(Position position, E element) {
			if (position.compareTo(cursor.getPosition()) < 0) {
				return addNodeRight(position, element);
			} else {
				return addNodeLeft(position, element);
			}
		}
		
		// Add a new node after a particular node
		public Node<E> addNodeRight(Position position, E element) {
			while (cursor != null) {
				if (cursor.getPosition().compareTo(position) > 0) {
					Node<E> newNode = new Node<E>(position, element, null, null);
					cursor.getPrevious().setNext(newNode);
					newNode.setNext(cursor);
					newNode.setPrevious(cursor.getPrevious());
					cursor.setPrevious(newNode);
					return newNode;
				}
				cursor = cursor.getNext();
			}
			return null;
		}
		
		public Node<E> addNodeLeft(Position position, E element) {
			while (cursor != null) {
				if (cursor.getPosition().compareTo(position) < 0) {
					Node<E> newNode = new Node<E>(position, element, null, null);
					cursor.getNext().setPrevious(newNode);
					newNode.setPrevious(cursor);
					newNode.setNext(cursor.getNext());
					cursor.setNext(newNode);
					return newNode;
				}
				cursor = cursor.getPrevious();
			}
			return null;
		}
		
		// Clear all the List 
		public void clear() {
			// link head and tail directly to each other,
			header.setNext(trailer);
			trailer.setPrevious(header);
			cursor = header;
			header.setElement(null);
			trailer.setElement(null);
		}
		
	}

}

/**
 * Design choices justification:
 * 
 * Because the air space is very big, and the number of the aircraft is limited by 20000, 
 * therefore, we need a data structure that can only hold the information of the cells 
 * that contains at least one aircraft, instead of predefined the array for the whole air space.
 * 
 * The approach that I used is to implement a Ordered Doubly Linked List [1] and linear search to
 * find the item, This approach will give the run-time efficiency of O(n) and memory space efficiency of O(n)
 * 
 * I also noticed that the air space is only 35km height, then I decided to use an Array of the list
 * to represent the layers of the air space to reduce the maximum length of the List. This method 
 * does not require much more memory, because it's only store the maximum of 36 references 
 * of every layer.
 * 
 * One other approach is use a 3D array to store all the cells of the air space, however, it is 
 * very memory consuming as we have to pre-allocate memory for every single cell, it also takes time
 * to initialize the system, especially to test a very single case while performing the unit tests.
 * 
 * There may be a solution using tree data structure, however, it would take time to research 
 * and implement
 * 
 * REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
