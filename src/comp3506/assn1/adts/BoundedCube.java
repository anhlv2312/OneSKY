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
	OrderedPositionalList<OrderedPositionalList<OrderedPositionalList<TraversableQueue<T>>>> airSpace;
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
		// Validate the arguments
		if ((length <= 0 || breadth <= 0 || height <= 0) 
				|| (length > MAX_X || breadth > MAX_Y || height > MAX_Z)) {
			throw new IllegalArgumentException();
		}
		
		this.lenght = length;
		this.breadth = breadth;
		this.height = height;

		airSpace = new OrderedPositionalList<>();
		
		
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
		airSpace.getElement(z).getElement(y).getElement(x).enqueue(element);	
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
		TraversableQueue<T> queue = airSpace.getElement(z).getElement(y).getElement(x);
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
		return airSpace.getElement(z).getElement(y).getElement(x);
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
		TraversableQueue<T> queue = airSpace.getElement(z).getElement(y).getElement(x);
		// Return false if null or queue size <= 1, otherwise return true
		if (queue == null) {
			return false;
		} else { 
			return (queue.size() > 1);
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
		// Get the queue in the position
		TraversableQueue<T> queue = airSpace.getElement(z).getElement(y).getElement(x);
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
		airSpace.getElement(z).getElement(y).setElement(x, newQueue);
		

		
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
		
		// Set the element of the node in that position to null
		airSpace.getElement(z).getElement(y).setElement(x, null);
	}

	/**
	 * Removes all elements stored in the cube.
	 * 
	 * Time-complexity O(1)
	 * 
	 */
	@Override
	public void clear() {
		airSpace.clear();
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
		private int position;
		
		public Node(int position, E element, Node<E> previousNode, Node<E> nextNode) {
			this.element = element; 
			this.nextNode = nextNode;
			this.position = position;
		}

		public E getElement() {
			return element;
		}

		public void setElement(E element) {
			this.element = element;
		}
		
		public Node<E> getNext() {
			return nextNode;
		}
		
		public Node<E> getPrevious() {
			return previousNode;
		}
		
		public void setNext(Node<E> nextNode) {
			this.nextNode = nextNode;
		}

		public void setPrevious(Node<E> previousNode) {
			this.previousNode = previousNode;
		}
		
		public int getPosition() {
			return position;
		}
		
	}
	
	/**
	 * Singly Linked List data structure that provides access to its nodes using position
	 * This class is used to represent a layers of the air space
	 *
	 * @param <E> The type of element held in the List.
	 *
	 */
	private class OrderedPositionalList<E> {
	
		private Node<E> header;
		private Node<E> trailer;
		private Node<E> cursor;
		
		public OrderedPositionalList() {
			/* [1 pp. 277] */
			header = new Node<> (0, null, null, null);
			trailer = new Node<> (-1, null, null, null);
			header.setNext(trailer);
			trailer.setPrevious(header);
			cursor = header;
		}
		
		public E getElement(int position) {
			Node<E> node = this.get(position);
			if (node != null) {
				return node.getElement();
			} else {
				return null;
			}
		}
		
		public void setElement(int position, E element) {
			Node<E> node = this.get(position);
			if (node != null) {
				node.setElement(element);
			}
		}
		
		public Node<E> get(int position) {
			if (position == cursor.getPosition()) {
				return cursor;
			} else if (position < cursor.getPosition()) {
				return getRight(position);
			} else {
				return getLeft(position);
			}
		}
		
		private Node<E> getRight(int position) {
			Node<E> node = cursor.getNext();
			while (node != null) {
				if (position == cursor.getPosition()) {
					cursor = node;
					return cursor;
				}
				node = node.getNext();
			} 
			return null;		
		}

		private Node<E> getLeft(int position) {
			Node<E> node = cursor.getPrevious();
			while (node != null) {
				if (position == cursor.getPosition()) {
					cursor = node;
					return cursor;
				}
				node = node.getPrevious();
			} 
			return null;		
		}
		
		public void remove(int position) {
			Node<E> node = this.get(position);
			if (node == header || node == trailer) {
				return;
			}
			if (node != null) {
				node.getPrevious().setNext(node.getNext());
				node.getNext().setPrevious(node.getPrevious());
			}
		}
		
		// Add a new node after a particular node
		public Node<E> add(int position, E element) {
			if (position == cursor.getPosition()) {
				return cursor;
			} else if (position > cursor.getPosition()) {
				return addRight(position, element);
			} else {
				return addLeft(position, element);
			}
		}
		
		// Add a new node after a particular node
		private Node<E> addRight(int position, E element) {
			Node<E> node = cursor.getNext();
			while (node != null) {
				if (node.getPosition() > position) {
					Node<E> newNode = new Node<E>(position, element, null, null);
					node.getPrevious().setNext(newNode);
					newNode.setNext(node);
					newNode.setPrevious(node.getPrevious());
					node.setPrevious(newNode);
					cursor = newNode;
					return cursor;
				}
				node = node.getNext();
			}
			return null;
		}
		
		private Node<E> addLeft(int position, E element) {
			Node<E> node = cursor.getPrevious();
			while (node != null) {
				if (node.getPosition() < position) {
					Node<E> newNode = new Node<E>(position, element, null, null);
					node.getNext().setPrevious(newNode);
					newNode.setPrevious(node);
					newNode.setNext(node.getNext());
					node.setNext(newNode);
					cursor = newNode;
					return cursor;
				}
				node = node.getPrevious();
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
 * Because the air space is very big, and the number of the aircraft is limited by 20000, and it's
 * likely that the emulator will work on adjacency positions, therefore, we need a data structure 
 * that can only hold the information of the cells that contains at least one aircraft with a cursor
 * to keep track of the current position that the emulator is working on. 
 *
 * The approach that I used is to implement a Ordered Doubly Linked List [1] and linear search to
 * find the item, I also use an cursor to store a current working node in order to reduce the 
 * seek time (this method give a significant improvement in Continuous Access Test, the run time
 * to add 20.000 objects reduced from ~2000ms to ~500ms) This approach will give the run-time 
 * efficiency of O(n) and memory space efficiency of O(n)
 * 
 * I also noticed that the air space is only 35km height, then I decided to use an Array of the list
 * to represent the layers of the air space to reduce the maximum length of the List. This method 
 * give a significant improvement in Random Access Test, (it reduce the time of inserting 20.000 
 * objects into arbitrary locations from 4000ms to ~300ms) This method does not require much more 
 * memory, because it's only store the maximum of 36 references of 36 layers. 
 * 
 * One other approach is use a 3D array to store all the cells of the air space, however, it is 
 * very memory consuming as we have to pre-allocate memory for every single cell, it also takes time
 * to initialize the system, especially to test a very single case while performing the unit tests.
 * 
 * There may be a solution using tree data structure that is much more efficient, however, it would 
 * take time to research and implement.
 * 
 * REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
