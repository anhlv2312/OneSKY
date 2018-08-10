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
public class BoundedCube<T> implements Cube<T> {

	private final static int MAX_X = 5321;
	private final static int MAX_Y = 3428;
	private final static int MAX_Z = 32;

	int length, breadth, height;

	Object[] space = new Object[20000];

	/**
	 * 
	 * @param length  Maximum size in the 'x' dimension.
	 * @param breadth Maximum size in the 'y' dimension.
	 * @param height  Maximum size in the 'z' dimension.
	 * @throws IllegalArgumentException If provided dimension sizes are not
	 *                                  positive.
	 */
	public BoundedCube(int length, int breadth, int height) throws IllegalArgumentException {
		if (length <= 0 || breadth <= 0 || height <= 0 
				|| length > MAX_X || breadth > MAX_Y || height > MAX_Z) {
			throw new IllegalArgumentException();
		} else {
			this.length = length;
			this.breadth = breadth;
			this.height = height;
		}
	}

	@Override
	public void add(int x, int y, int z, T element) throws IndexOutOfBoundsException {
		space[0] = element;
	}

	@Override
	public T get(int x, int y, int z) throws IndexOutOfBoundsException {
		return null;
	}

	@Override
	public IterableQueue<T> getAll(int x, int y, int z) throws IndexOutOfBoundsException {
		return null;
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
		// TODO Auto-generated method stub

	}

}


class LinkedCellList<T> {
	
	
}


/* [1 pp. 277] */
class LinkedPositionalList<E> {

	// ---------------- nested Node class ----------------
	private static class Node<E> {

		private E element;
		private Node<E> prev;
		private Node<E> next;

		public Node(E e, Node<E> p, Node<E> n) {
			element = e; // reference to the element stored at this node
			prev = p; // reference to the previous node in the list
			next = n; // reference to the subsequent node in the list
		}

		public E getElement() throws IllegalStateException {
			if (next == null) // convention for defunct node
				throw new IllegalStateException("Position no longer valid");
			return element;
		}

		public Node<E> getPrev() {
			return prev;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setElement(E e) {
			element = e;
		}

		public void setPrev(Node<E> p) {
			prev = p;
		}

		public void setNext(Node<E> n) {
			next = n;
		}
	} // ----------- end of nested Node class -----------

	// instance variables of the LinkedPositionalList
	private Node<E> header;
	private Node<E> trailer;
	private int size = 0;

	public LinkedPositionalList() {
		header = new Node<>(null, null, null);
		trailer = new Node<>(null, header, null);
		header.setNext(trailer);
	}

	/** Validates the position and returns it as a node. */
	private Node<E> validate(Node<E> p) throws IllegalArgumentException {
		if (!(p instanceof Node))
			throw new IllegalArgumentException("Invalid p");
		Node<E> node = (Node<E>) p; // safe cast
		if (node.getNext() == null) // convention for defunct node
			throw new IllegalArgumentException("p is no longer in the list");
		return node;
	}

	/** Returns the given node as a Position (or null, if it is a sentinel). */
	private Node<E> position(Node<E> node) {
		if (node == header || node == trailer)
			return null; // do not expose user to the sentinels
		return node;
	}

	// public accessor methods
	/** Returns the number of elements in the linked list. */
	public int size() {
		return size;
	}

	/**
	 * Tests whether the linked list is empty.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the first Position in the linked list (or null, if empty).
	 */
	public Node<E> first() {
		return position(header.getNext());
	}

	/**
	 * Returns the last Position in the linked list (or null, if empty).
	 */
	public Node<E> last() {
		return position(trailer.getPrev());
	}

	/**
	 * Returns the Position immediately before Position p (or null, if p is first).
	 */
	public Node<E> before(Node<E> p) throws IllegalArgumentException {
		Node<E> node = validate(p);
		return position(node.getPrev());
	}

	/**
	 * Returns the Position immediately after Position p (or null, if p is last).
	 */
	public Node<E> after(Node<E> p) throws IllegalArgumentException {
		Node<E> node = validate(p);
		return position(node.getNext());
	}

	/** Adds element e to the linked list between the given nodes. */
	private Node<E> addBetween(E e, Node<E> pred, Node<E> succ) {
		Node<E> newest = new Node<>(e, pred, succ); // create and link a new node
		pred.setNext(newest);
		succ.setPrev(newest);
		size++;
		return newest;
	}

	/**
	 * Inserts element e at the front of the linked list and returns its new
	 * Position.
	 */
	public Node<E> addFirst(E e) {
		return addBetween(e, header, header.getNext()); // just after the header
	}

	/**
	 * Inserts element e at the back of the linked list and returns its new
	 * Position.
	 */
	public Node<E> addLast(E e) {
		return addBetween(e, trailer.getPrev(), trailer); // just before the trailer
	}

	/**
	 * Inserts element e immediately before Position p, and returns its new
	 * Position.
	 */
	public Node<E> addBefore(Node<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		return addBetween(e, node.getPrev(), node);
	}

	/**
	 * Inserts element e immediately after Position p, and returns its new Position.
	 */
	public Node<E> addAfter(Node<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		return addBetween(e, node, node.getNext());
	}

	/**
	 * Replaces the element stored at Position p and returns the replaced element.
	 */
	public E set(Node<E> p, E e) throws IllegalArgumentException {
		Node<E> node = validate(p);
		E answer = node.getElement();
		node.setElement(e);
		return answer;
	}

	/** Removes the element stored at Position p and returns it (invalidating p). */
	public E remove(Node<E> p) throws IllegalArgumentException {
		Node<E> node = validate(p);
		Node<E> predecessor = node.getPrev();
		Node<E> successor = node.getNext();
		predecessor.setNext(successor);
		successor.setPrev(predecessor);
		size--;
		E answer = node.getElement();
		node.setElement(null); // help with garbage collection
		node.setNext(null); // and convention for defunct node
		node.setPrev(null);
		return answer;
	}

}


/* REFERENCE 
 * [1]	M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, 
 * 		Data structures and algorithms in Java. John Wiley & Sons, 2014.
 */
