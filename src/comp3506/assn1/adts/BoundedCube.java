package comp3506.assn1.adts;

/**
 * A three-dimensional data structure that holds items in a positional
 * relationship to each other. Each cell in the data structure can hold multiple
 * items. A bounded cube has a specified maximum size in each dimension. The
 * root of each dimension is indexed from zero.
 * 
 * @author
 *
 * @param <T> The type of element held in the data structure.
 */

/* REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 */

public class BoundedCube<T> implements Cube<T> {

	static class Position implements Comparable<Position> {
		/* [1 pp. 227] */
		int x, y, z;
		public Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public int compareTo(Position position) {
			if (this.z != position.z) {
				return this.z - position.z;
			} else if (this.y != position.z) {
				return this.y - position.y;
			} else {
				return this.x - position.x;
			}
		}
	}
	
	static class Node<E> {

		private E element;
		private Node<E> previousNode;
		private Node<E> nextNode;
		private Position position;
		
		public Node(Position position, E element, Node<E> previousNode, Node<E> nextNode) {
			this.element = element; 
			this.previousNode = previousNode;
			this.nextNode = nextNode;
			this.position = position;
		}

		public E getElement() throws IllegalStateException {
			if (nextNode == null)
				throw new IllegalStateException();
			return element;
		}

		public Node<E> getPrev() {
			return previousNode;
		}

		public Node<E> getNext() {
			return nextNode;
		}

		public void setElement(E e) {
			element = e;
		}

		public void setPrev(Node<E> p) {
			previousNode = p;
		}

		public void setNext(Node<E> n) {
			nextNode = n;
		}
		
		public Position getPosition() {
			return position;
		}
	}
	

	class LinkedList<E> {
	
		private Node<E> header;
		private Node<E> trailer;
		
		public LinkedList(int x, int y, int z, E headerElement, E trailerElement) {
			header = new Node<> (new Position(0,0,0), headerElement, null, null);
			trailer = new Node<> (new Position(x,y,z), trailerElement, header, null);
			header.setNext(trailer);
		}
		
		public E find(int x, int y, int z, E emptyElement) {
			Position position = new Position(x, y, z);
			Node<E> currentNode = header;
			Node<E> previousNode = header;
			while (currentNode.getNext() != null) {
				if (position.compareTo(currentNode.getPosition()) < 0) {
					previousNode = currentNode;
				} else if (position.compareTo(currentNode.getPosition()) == 0) {
					return currentNode.getElement();
				}
				currentNode = currentNode.getNext();
			} 
			Node<E> newNode = new Node<>(position, emptyElement, previousNode, previousNode.getNext());
			previousNode.getNext().setPrev(newNode);
			previousNode.setNext(newNode);
			return newNode.getElement();
		}
		
		public E find(int x, int y, int z) {
			Position position = new Position(x, y, z);
			Node<E> currentNode = header;
			while (currentNode.getNext() != null) {
				if (position.compareTo(currentNode.getPosition()) == 0) {
					return currentNode.getElement();
				}
				currentNode = currentNode.getNext();
			} 
			return null;
		}
		
		public T newElement() {
			return new T();
		}
		
		public void clear() {
			Node<E> currentNode = header;
			while (currentNode.getNext() != null) {
				currentNode.setElement(null);
				currentNode = currentNode.getNext();
			} 
		}
		
	}
	
	private final static int MAX_X = 5321;
	private final static int MAX_Y = 3428;
	private final static int MAX_Z = 32;

	
	LinkedList<TraversableQueue<T>> cells;
	
	/**
	 * 
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not
	 *                                  positive.
	 */
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
		if (length <= 0 || breadth <= 0 || height <= 0 || length > MAX_X || breadth > MAX_Y || height > MAX_Z) {
			throw new IllegalArgumentException();
		} else {
			cells = new LinkedList<>(length, breadth, height, new TraversableQueue<>(), new TraversableQueue<>());
		}
	}

	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		TraversableQueue<T> cell = cells.find(x, y, z, new TraversableQueue<>());
		cell.enqueue(element);
	}

	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		TraversableQueue<T> cell = cells.find(x, y, z);
		if (cell == null) {
			return null;
		}
		return cell.dequeue();
	}

	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		return cells.find(x, y, z);
	}

	@Override
	public boolean isMultipleElementsAt(int x, int y, int z) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAll(int x, int y, int z) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		cells.clear();
	}

}


