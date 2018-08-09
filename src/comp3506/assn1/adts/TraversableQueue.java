package comp3506.assn1.adts;

import java.util.Iterator;

public class TraversableQueue<T> implements IterableQueue<T> {

	int size;
	
	
	
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enqueue(T element) throws IllegalStateException {
		this.size +=1;
		
	}

	@Override
	public T dequeue() throws IndexOutOfBoundsException {
		if (this.size > 0) {
			this.size -=1;

		} else {
			return null;	
		}

	}

	@Override
	public int size() {
		return this.size;
	}

}
