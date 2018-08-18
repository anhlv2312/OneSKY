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
	
	LinkedPositionalList<TraversableQueue<T>> cells;
	
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

		cells = new LinkedPositionalList<>(length, breadth, height);
		
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
		TraversableQueue<T> queue = cells.get(position);		
		if (queue == null) {
			TraversableQueue<T> newQueue = new TraversableQueue<T>();
			newQueue.enqueue(element);
			cells.set(position, newQueue);
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
		TraversableQueue<T> queue = cells.get(position);
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
		return cells.get(position);
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
		TraversableQueue<T> cell = cells.get(position);
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
		TraversableQueue<T> queue = cells.get(position);
		if (queue == null) {
			return false;
		}
		TraversableQueue<T> newQueue = new TraversableQueue<T>();
		for (T e: queue) {
			if (!e.equals(element)) {
				newQueue.enqueue(e);
			}
		}
		cells.set(position, newQueue);
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
		cells.set(position, null);
	}

	/**
	 * Removes all elements stored in the cube.
	 * 
	 * Time-complexity O(n)
	 * 
	 */
	@Override
	public void clear() {
		cells.clear();
	}
	
	private Position convertPosition(int x, int y, int z) throws IndexOutOfBoundsException {
		if ((x < 0 || y < 0 || z < 0) || (x > this.lenght || y > this.breadth || z > this.height)) {
			throw new IndexOutOfBoundsException();
		}
		return new Position(x, y, z);
	}
	
	/**
	 * The Position class holds the coordinates information based on x, y, z axis, 
	 *
	 */
	private static class Position implements Comparable<Position> {
		/* [1 pp. 227] */
		int x, y, z;
		
		public Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		// Compare with other position, z axis has the highest weight, then y and then x,
		@Override
		public int compareTo(Position position) {
			// Compare the 
			if (this.z != position.z) {
				return this.z - position.z;
			}
			
			if (this.y != position.z) {
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
		private Position position;
		
		public Node(Position position, E element, Node<E> nextNode) {
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

		public void setElement(E element) {
			this.element = element;
		}
		
		public void setNext(Node<E> nextNode) {
			this.nextNode = nextNode;
		}
		
		public Position getPosition() {
			return position;
		}
	}
	
	/**
	 * Singly Linked List data structure that provides access to its nodes using position
	 *
	 * @param <E> The type of element held in the List.
	 *
	 */
	private class LinkedPositionalList<E> {
	
		private Node<E> header;
		private Node<E> trailer;
		
		public LinkedPositionalList(int x, int y, int z) {
			/* [1 pp. 277] */
			header = new Node<> (new Position(0,0,0), null, null);
			trailer = new Node<> (new Position(x,y,z), null, null);
			header.setNext(trailer);
		}
		
		public Node<E> find(Position position) {
			Node<E> currentNode = header;
			while (currentNode.getNext() != null) {
				if (position.compareTo(currentNode.getPosition()) == 0) {
					return currentNode;
				}
				currentNode = currentNode.getNext();
			} 
			return null;			
		}
		
		public Node<E> findPrevious(Position position) {
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
		
		public E get(Position position) {
			Node<E> node = this.find(position);
			if (node != null) {
				return node.getElement();
			}
			return null;
		}
		
		public void set(Position position, E element) {
			Node<E> currentNode = this.find(position);
			if (currentNode == null) {
				Node<E> previousNode = this.findPrevious(position);
				currentNode = new Node<E>(position, null, null);
				currentNode.setNext(previousNode.getNext());
				previousNode.setNext(currentNode);
			}
			currentNode.setElement(element);
		}
		
		public void clear() {
			Node<E> currentNode = header;
			while (currentNode.getNext() != null) {
				currentNode.setElement(null);
				currentNode = currentNode.getNext();
			} 
		}
		
	}

}

/**
 * Design choices justification
 * 
 * REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
