package comp3506.assn1.adts;

/**
 * A three-dimensional data structure that holds items in a positional
 * relationship to each other. Each cell in the data structure can hold multiple
 * items. A bounded cube has a specified maximum size in each dimension. The
 * root of each dimension is indexed from zero.
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
		if ((length <= 0 || breadth <= 0 || height <= 0) || (length > MAX_X || breadth > MAX_Y || height > MAX_Z)) {
			throw new IllegalArgumentException();
		}
		
		this.lenght = length;
		this.breadth = breadth;
		this.height = height;

		for (int z = 0; z <= height; z++) {
			layers[z] = new OrderedLinkedList<TraversableQueue<T>>(length, breadth);
		}
		
	}

	/**
	 * Add an element at a fixed position.
	 * 
	 * @param element The element to be added at the indicated position.
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 * Time-complexity O(n)
	 * 
	 */
	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		TraversableQueue<T> queue = ((OrderedLinkedList<TraversableQueue<T>>)layers[z]).getElement(position);		
		if (queue == null) {
			TraversableQueue<T> newQueue = new TraversableQueue<T>();
			newQueue.enqueue(element);
			((OrderedLinkedList<TraversableQueue<T>>)layers[z]).setElement(position, newQueue);
		} else {
			queue.enqueue(element);
		}
	}

	/**
	 * Return the 'oldest' element at the indicated position.
	 * 
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @return 'Oldest' element at this position or null if no elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 * Time-complexity O(n)
	 * 
	 */
	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		TraversableQueue<T> queue = ((OrderedLinkedList<TraversableQueue<T>>)layers[z]).getElement(position);
		if (queue == null) {
			return null;
		}
		return queue.dequeue();
	}

	/**
	 * Return all the elements at the indicated position.
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return An IterableQueue of all elements at this position or null if no elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 * Time-complexity O(n)
	 *  
	 */
	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		return ((OrderedLinkedList<TraversableQueue<T>>)layers[z]).getElement(position);
	}

	/**
	 * Indicates whether there are more than one elements at the indicated position.
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return true if there are more than one elements at the indicated position, false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 * Time-complexity O(n)
	 * 
	 */
	@Override
	public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		TraversableQueue<T> cell = ((OrderedLinkedList<TraversableQueue<T>>)layers[z]).getElement(position);
		if (cell == null) {
			return false;
		} else { 
			return (cell.size() > 1);
		}
	}

	/**
	 * Removes the specified element at the indicated position.
	 * 
	 * @param element The element to be removed from the indicated position.
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @return true if the element was removed from the indicated position, false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 * Time-complexity O(n)
	 * 
	 */
	@Override
	public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		TraversableQueue<T> queue = ((OrderedLinkedList<TraversableQueue<T>>)layers[z]).getElement(position);
		if (queue == null) {
			return false;
		}
		TraversableQueue<T> newQueue = new TraversableQueue<T>();
		for (T e: queue) {
			if (!e.equals(element)) {
				newQueue.enqueue(e);
			}
		}
		if (newQueue.size() == 0) {
			((OrderedLinkedList<TraversableQueue<T>>)layers[z]).removeNode(position);
		} else {
			((OrderedLinkedList<TraversableQueue<T>>)layers[z]).setElement(position, newQueue);
		}
		return (newQueue.size() < queue.size());
	}

	/**
	 * Removes all elements at the indicated position.
	 * 
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 * Time-complexity O(n)
	 * 
	 */
	@Override
	public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
		Position position = convertPosition(x, y, z);
		((OrderedLinkedList<TraversableQueue<T>>)layers[z]).setElement(position, null);
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
			((OrderedLinkedList<TraversableQueue<T>>)layers[z]).clear();
		}
	}
	
	private Position convertPosition(int x, int y, int z) throws IndexOutOfBoundsException {
		if ((x < 0 || y < 0 || z < 0) || (x > this.lenght || y > this.breadth || z > this.height)) {
			throw new IndexOutOfBoundsException();
		}
		return new Position(x, y);
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
		
		public Node<E> getPrev() {
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
		
		public OrderedLinkedList(int x, int y) {
			/* [1 pp. 277] */
			header = new Node<> (new Position(0,0), null, null, null);
			trailer = new Node<> (new Position(x,y), null, null, null);
			header.setNext(trailer);
			trailer.setPrevious(header);
		}
		
		private Node<E> findNode(Position position) {
			Node<E> currentNode = header;
			while (currentNode.getNext() != null) {
				if (position.compareTo(currentNode.getPosition()) == 0) {
					return currentNode;
				}
				currentNode = currentNode.getNext();
			} 
			return null;			
		}
		
		public Node<E> findPreviousNode(Position position) {
			Node<E> currentNode = header;
			Node<E> previousNode = header;
			while (currentNode.getNext() != null) {
				if (position.compareTo(currentNode.getPosition()) < 0) {
					previousNode = currentNode;
				}
				currentNode = currentNode.getNext();
			} 
			return previousNode;			
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
				Node<E> previousNode = this.findPreviousNode(position);
				currentNode = this.addNodeAfter(position, previousNode, element);
				previousNode.setNext(currentNode);
			} else {
				currentNode.setElement(element);
			}
		}
		
		public void removeNode(Position position) {
			Node<E> currentNode = this.findNode(position);
			currentNode.getPrev().setNext(currentNode.getNext());
		}
		
		public Node<E> addNodeAfter(Position position, Node<E> previousNode, E element) {
			Node<E> newNode = new Node<E>(position, element, null, null);
			newNode.setPrevious(previousNode);
			newNode.setNext(previousNode.getNext());
			previousNode.setNext(newNode);
			return newNode;
			
		}
		
		public void clear() {
			header.setElement(null);
			trailer.setElement(null);
			header.setNext(trailer);
		}
		
	}

}

/**
 * Design choices justification
 * 
 * Because the air space is very big, and the number of the aircraft is limited by 20000, 
 * therefore, we need a data structure that can only hold the information of the cells 
 * that contains at least one aircraft, instead of predefined the array for the whole air space.
 *
 * The approach that I used is to implement a Ordered Linked List and use linear search for 
 * finding an item
 * 
 * One other approach is use a 3D array to store all the cells of the air space, however, it is 
 * very memory consuming as we have to pre-allocate memory for every single cell.
 * 
 * REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
