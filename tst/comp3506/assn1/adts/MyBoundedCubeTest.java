package comp3506.assn1.adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

public class MyBoundedCubeTest {
	
	@Test
	public void testGetWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		testCube.add(1, 1, 1, element);
		assertThat("Only element at a position was not returned.", testCube.get(1, 1, 1), is(equalTo(element)));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testPosition() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		testCube.add(5, 6, 5, new Object());
	}
	
	@Test
	public void testNegativePosition() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		try {
			testCube.add(-1, -1, -1, new Object());
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			testCube.get(-1, -1, -1);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			testCube.getAll(-1, -1, -1);
			fail();
		} catch (IndexOutOfBoundsException e) {}
		try {
			testCube.remove(-1, -1, -1, new Object());
			fail();
		} catch (IndexOutOfBoundsException e) {}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testArgumentException() {
		Cube<Object> testCube = new BoundedCube<>(6000, 6000, 36);
		testCube.add(1, 1, 1, new Object());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeArgument() {
		Cube<Object> testCube = new BoundedCube<>(-1, -1, -1);
		testCube.add(1, 1, 1, new Object());
	}

	@Test
	public void testGetAll() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		Object element = new Object(); 
		testCube.add(1, 1, 1, element);
		for (int i = 0; i <= 100; i++) {
			testCube.add(1, 1, 1, new Object());
		}
		assertEquals(element, testCube.getAll(1, 1, 1).dequeue());
		testCube.removeAll(1, 1, 1);
		assertEquals(null, testCube.get(1, 1, 1));
	}
	
	@Test
	public void testIsMultipleElementAt() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		Object element = new Object(); 
		assertEquals(false, testCube.isMultipleElementsAt(1, 1, 1));
		testCube.add(1, 1, 1, element);
		assertEquals(false, testCube.isMultipleElementsAt(1, 1, 1));
		for (int i = 0; i <= 100; i++) {
			testCube.add(1, 1, 1, new Object());
		}
		assertEquals(true, testCube.isMultipleElementsAt(1, 1, 1));
	}
	
	@Test
	public void testRemoveElementAt() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		Object element = new Object();
		assertFalse(testCube.remove(1, 1, 1, element));
		testCube.add(1, 1, 1, element);
		for (int i = 0; i <= 100; i++) {
			testCube.add(1, 1, 1, new Object());
		}
		testCube.add(1, 1, 1, element);
		assertFalse(testCube.remove(1, 1, 1, new Object()));
		assertTrue(testCube.remove(1, 1, 1, element));
		assertFalse(testCube.remove(1, 1, 1, element));
	}
	
	@Test
	public void testRemoveAndAdd() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		Object element = new Object();
		assertFalse(testCube.remove(1, 1, 1, element));
		testCube.add(1, 1, 1, element);
		assertTrue(testCube.remove(1, 1, 1, element));
		testCube.add(1, 1, 1, element);
		assertTrue(testCube.remove(1, 1, 1, element));
		assertFalse(testCube.remove(1, 1, 1, element));
	}
	
	@Test
	public void testAddNodeInTheMiddle() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		testCube.add(1, 1, 1, new Object());
		testCube.add(3, 3, 1, new Object());
		testCube.add(2, 2, 1, new Object());
	}
	
	
	@Test
	public void testPerformanceOneCell() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		for (int i = 0; i < 20000; i++) {
			testCube.add(1, 1, 1, new Object());
		}
		assertEquals(20000, testCube.getAll(1, 1, 1).size());
		testCube.removeAll(1, 1, 1);
		assertEquals(null, testCube.get(1, 1, 1));
	}
	
	@Test
	public void testPerformanceMultiCell() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		for (int i = 0; i <= 35; i++) {
			for (int j = 0; j <= 3428; j++) {
				testCube.add(j, j, i, new Object());
			}
		}
	}
	
	@Test
	public void testClear() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		for (int i = 0; i <= 35; i++) {
			for (int j = 0; j <= 100; j++) {
				testCube.add(j, j, i, new Object());
			}
		}
		testCube.clear();
		for (int i = 0; i <= 35; i++) {
			for (int j = 0; j <= 100; j++) {
				assertEquals(null, testCube.get(j, j, i));
			}
		}
		
	}
}
