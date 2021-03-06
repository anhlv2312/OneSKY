package comp3506.assn1.adts;

/**
 * A three-dimensional data structure that holds items in a positional
 * relationship to each other. Each cell in the data structure can hold multiple
 * items. A bounded cube has a specified maximum size in each dimension. The
 * root of each dimension is indexed from zero.
 * 
 * Memory Usage: O(n) (n is the number of aircraft)
 * 
 * @author Vu Anh LE <s4490763@student.uq.edu.au>
 *
 * @param <T> The type of element held in the data structure.
 *
 */
public class BoundedCube<T> implements Cube<T> {

	private SortedPositionalList<SortedPositionalList<SortedPositionalList<TraversableQueue<T>>>> airSpace;
	private int length;
    private int breadth;
    private int height;

	Object[] layers = new Object[36];

	/**
	 * BoundCube constructor
	 * 
	 * Time-complexity O(1)
	 * 
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not
	 *                                  positive.
	 */
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
		// Validate the arguments
		if (length <= 0 || breadth <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}

		this.length = length;
		this.breadth = breadth;
		this.height = height;

		airSpace = new SortedPositionalList<>();

	}

	/**
	 * Add an element at a fixed position.
	 * 
	 * Time-complexity O(n) (n is the number of aircraft)
	 * 
	 * @param element The element to be added at the indicated position.
	 * @param x       X Coordinate of the position of the element.
	 * @param y       Y Coordinate of the position of the element.
	 * @param z       Z Coordinate of the position of the element.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		checkOutOfBound(x, y, z);
		
		// If there is no list in the z coordinate then add new List to that layer 
		if (airSpace.get(z) == null) {
			airSpace.set(z, new SortedPositionalList<SortedPositionalList<TraversableQueue<T>>>());
		}
		// If there is no list in the y coordinate then add new List to that line
		if (airSpace.get(z).get(y) == null) {
			airSpace.get(z).set(y, new SortedPositionalList<TraversableQueue<T>>());
		}
		// If there is no queue in the x coordinate then add new queue to that cell 
		if (airSpace.get(z).get(y).get(x) == null) {
			airSpace.get(z).get(y).set(x, new TraversableQueue<T>());
		}
		
		// Enqueue the element to the cell
		airSpace.get(z).get(y).get(x).enqueue(element);
		
	}

	/**
	 * Return the 'oldest' element at the indicated position.
	 * 
	 * Time-complexity O(n) (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position of the element.
	 * @param y Y Coordinate of the position of the element.
	 * @param z Z Coordinate of the position of the element.
	 * @return 'Oldest' element at this position or null if no elements at the
	 *         indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		checkOutOfBound(x, y, z);
		IterableQueue<T> queue = this.getAll(x, y, z);
		// If there is no queue there, return null
		if (queue == null) {
			return null;
		}
		// else, return the first item from the iterator
		return queue.iterator().next();
	}

	/**
	 * Return all the elements at the indicated position.
	 * 
	 * Time-complexity O(n) (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return An IterableQueue of all elements at this position or null if no
	 *         elements at the indicated position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		checkOutOfBound(x, y, z);
		if (airSpace.get(z) == null) {
			return null;
		}
		if (airSpace.get(z).get(y) == null) {
			return null;
		}
		return airSpace.get(z).get(y).get(x);
	}

	/**
	 * Indicates whether there are more than one elements at the indicated position.
	 * 
	 * Time-complexity O(n) (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position of the element(s).
	 * @param y Y Coordinate of the position of the element(s).
	 * @param z Z Coordinate of the position of the element(s).
	 * @return true if there are more than one elements at the indicated position,
	 *         false otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
		checkOutOfBound(x, y, z);
		IterableQueue<T> queue = this.getAll(x, y, z);
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
	 * Time-complexity O(n) (n is the number of aircraft)
	 * 
	 * @param element The element to be removed from the indicated position.
	 * @param x       X Coordinate of the position.
	 * @param y       Y Coordinate of the position.
	 * @param z       Z Coordinate of the position.
	 * @return true if the element was removed from the indicated position, false
	 *         otherwise.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		checkOutOfBound(x, y, z);
		// Get the queue in the position
		IterableQueue<T> queue = this.getAll(x, y, z);
		if (queue == null) {
			return false;
		}

		// Initialize a new queue
		TraversableQueue<T> newQueue = new TraversableQueue<T>();
		for (T e : queue) {
			// enqueue all the items from old queue to the newQueue except the given element
			if (!e.equals(element)) {
				newQueue.enqueue(e);
			}
		}

		// If the newQueue is empty
		if (newQueue.size() == 0) {
			// Then remove the node from x axis
			airSpace.get(z).get(y).remove(x);
			// If the x axis is empty, remove it from y axis
			if (airSpace.get(z).get(y).isEmpty()) {
				airSpace.get(z).remove(y);
			} // Keep the z axis because it's only 35 layers
		} else {
			// Replace newQueue to that cell
			airSpace.get(z).get(y).set(x, newQueue);
		}

		// Return true if the size is reduced
		return (newQueue.size() < queue.size());
	}

	/**
	 * Removes all elements at the indicated position.
	 * 
	 * Time-complexity O(n) (n is the number of aircraft)
	 * 
	 * @param x X Coordinate of the position.
	 * @param y Y Coordinate of the position.
	 * @param z Z Coordinate of the position.
	 * @throws IndexOutOfBoundsException If x, y or z coordinates are out of bounds.
	 * 
	 */
	@Override
	public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
		checkOutOfBound(x, y, z);
		// Set the element of the node in that position to null
		airSpace.get(z).get(y).set(x, null);
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

	// Check the given coordinate is valid or not
	private void checkOutOfBound(int x, int y, int z) {
		if ((x < 0 || y < 0 || z < 0) || (x > this.length || y > this.breadth || z > this.height)) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * A Sorted Linked List data structure that provides access to its nodes using
	 * position, the nodes are kept sorted by their positions when added into the list
	 *
	 * @param <E> The type of element held in the List.
	 *
	 */
	private class SortedPositionalList<E> {
		// Inspired by Double Linked List and Sorted Priority Queue in [1]

		private Node<E> header;
		private Node<E> trailer;
		private Node<E> cursor;

		SortedPositionalList() {
			/* [1 pp. 277] */
			header = new Node<>(-1, null, null, null);
			trailer = new Node<>(999999, null, null, null);
			header.setNext(trailer);
			trailer.setPrevious(header);
			cursor = header;
		}

		// Set the element at the given position
        void set(int position, E element) {
			Node<E> node = this.getNode(position);
			node.setElement(element);
		}

		// Get the element at the given position
		private E get(int position) {
			Node<E> node = this.getNode(position);
			if (node != null) {
				return node.getElement();
			} else {
				return null;
			}
		}

		// Find the node in position, return a new node if not found
		private Node<E> getNode(int position) {
			if (cursor.getPosition() == position) {
				// if the cursor there, return it
				return cursor;
			}
			// cursor is on the left of the position,
			if (cursor.getPosition() < position) {
				while (cursor.getNext() != null) {
					// move the cursor to the right
					cursor = cursor.getNext();
					if (cursor.getPosition() == position) {
						// If the position is exist, return it
						return cursor;
					}
					// If the cursor is move over the position,
					// That mean the position is not existed
					// then create a new node to the left of the cursor
					if (cursor.getPosition() > position) {
						return addPrevious(cursor, position);
					}
				}
			} else if (cursor.getPosition() > position) {
				// The same idea with move the cursor to the right
				// but move the cursor to the left instead
				while (cursor.getPrevious() != null) {
					cursor = cursor.getPrevious();
					if (cursor.getPosition() == position) {
						return cursor;
					}
					if (cursor.getPosition() < position) {
						return addNext(cursor, position);
					}
				}
			}
			return null;
		}

		// Add a new Node before the given node
		private Node<E> addPrevious(Node<E> node, int position) {
			Node<E> newNode = new Node<E>(position, null, null, null);
			node.getPrevious().setNext(newNode);
			newNode.setNext(node);
			newNode.setPrevious(node.getPrevious());
			node.setPrevious(newNode);
			return newNode;
		}

		// Add a new Node after the given node
		private Node<E> addNext(Node<E> node, int position) {
			Node<E> newNode = new Node<E>(position, null, null, null);
			node.getNext().setPrevious(newNode);
			newNode.setPrevious(node);
			newNode.setNext(node.getNext());
			node.setNext(newNode);
			return newNode;
		}

		// Remove the node at the given position
        void remove(int position) {
			Node<E> node = this.getNode(position);
			cursor = node.getPrevious();
			if (node == header || node == trailer) {
				return;
			}
			if (node != null) {
				node.getPrevious().setNext(node.getNext());
				node.getNext().setPrevious(node.getPrevious());
			}
		}

		// Clear all the List
        void clear() {
			// Link header and trailer directly to each other,
			header.setNext(trailer);
			trailer.setPrevious(header);
			// Reset the cursor
			cursor = header;
			// Empty header and trailer node
			header.setElement(null);
			trailer.setElement(null);
		}

		// Check if the List if empty
        boolean isEmpty() {
			return (header.getNext() == trailer);
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
		private int position;

		Node(int position, E element, Node<E> previousNode, Node<E> nextNode) {
			this.element = element;
			this.nextNode = nextNode;
			this.position = position;
		}

		E getElement() {
			return element;
		}

		void setElement(E element) {
			this.element = element;
		}

		Node<E> getNext() {
			return nextNode;
		}

		Node<E> getPrevious() {
			return previousNode;
		}

		void setNext(Node<E> nextNode) {
			this.nextNode = nextNode;
		}

		void setPrevious(Node<E> previousNode) {
			this.previousNode = previousNode;
		}

		int getPosition() {
			return position;
		}

	}
	
}

/**
 * Design choices justification:
 * 
 * Because the air space is very big, and the number of the aircraft is limited
 * by 20000, therefore, we need a data structure that can only hold the
 * information of the cells that contains at least one aircraft. And since the
 * emulator is likely to work on adjacency cells, then we need a method to keep
 * track of the working cell in order to improve the performance of the data 
 * structure.
 *
 * The approach that I used is to create a 3D Positional Linked List that
 * implements a Positional Doubly Linked List [1 pp. 276] and modified the data
 * structure to I keep it sorted by the position of the node, so that I can use
 * a cursor to keep track of the current working node in order to reduce the
 * seek time (By using the cursor, the ADT performed a significant improvement
 * in Continuous Access Test, the time of adding 20500 aircrafts into continuous
 * cells is reduced from ~1800ms to ~20ms).
 * 
 * When the last aircraft is removed from the cell, the empty node will be
 * removed from the data structure to make it more memory efficient. This data 
 * structure has the memory efficiency of O(n) where n is the number of the
 * aircraft.
 * 
 * I tried to implement the Sorted Priority Queue [1 pp. 368] with the key is the
 * 3D point and the value is the TraversableQueue, the insert method of this data 
 * structure takes O(n) time, the min() and removeMin() method takes only O(1) time
 * [1 pp. 368], however, it not add much value in OneSky application because and it 
 * still takes 0(n) time to to access a random item.
 * 
 * There is another approach that uses a 3D array to store all the cells of the 
 * air space, however, it is very memory consuming as we have to pre-allocate memory 
 * for every single cell, it also takes time to initialize the system.
 *  
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 * 
 */
