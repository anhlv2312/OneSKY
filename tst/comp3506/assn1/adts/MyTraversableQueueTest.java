package comp3506.assn1.adts;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.NoSuchElementException;

public class MyTraversableQueueTest {

	@Test(timeout=500)
	public void testNewQueueIsEmpty() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		assertEquals(0, testQueue.size());
	}

	@Test(timeout=500)
	public void testEnqueueDequeue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object object = new Object();
		testQueue.enqueue(object);
		assertEquals(1, testQueue.size());
		assertEquals(object, testQueue.dequeue());
	}
	
	@Test(timeout=500)
	public void testDequeueEmptyQueue() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object object = new Object();
		testQueue.enqueue(object);
		assertEquals(1, testQueue.size());
		assertEquals(object, testQueue.dequeue());
	}
	
	@Test(timeout=500, expected = IndexOutOfBoundsException.class)
	public void testIterator() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.dequeue();
	}	

	@Test(timeout=500, expected = NoSuchElementException.class)
	public void testEmptyQueueIteratorExceptions() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		testQueue.iterator().next();
	}
	
	@Test(timeout=500, expected = NoSuchElementException.class)
	public void testDequeueIteratorExceptions() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object object = new Object();
		testQueue.enqueue(object);
		assertTrue(testQueue.iterator().hasNext());
		testQueue.dequeue();
		assertEquals(object, testQueue.iterator().next());
	}
	
	@Test(timeout=500)
	public void testIteratorExceptions() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		Object object = new Object();
		testQueue.enqueue(new Object());
		testQueue.enqueue(object);
		assertTrue(testQueue.iterator().hasNext());
		assertEquals(object, testQueue.iterator().next());
		testQueue.iterator().next();
		assertEquals(object, testQueue.iterator().next());
	}
	
	
	@Test(timeout=500, expected = IllegalStateException.class)
	public void testMaxLengthExceptions() {
		IterableQueue<Object> testQueue = new TraversableQueue<>();
		for (int i=0; i < 20000; i++)
			testQueue.enqueue(new Object());
		testQueue.enqueue(new Object());
	}
	
	
}
