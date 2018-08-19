package comp3506.assn1.adts;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class MyBoundedCubeTest {
	
	@Test
	public void testGetWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(10, 10, 5);
		Object element = new Object();
		testCube.add(1, 1, 1, element);
		assertEquals(testCube.get(1, 1, 1), element);
		testCube.add(10, 9, 1, element);
		assertEquals(testCube.get(10, 9, 1), element);
		testCube.add(2, 9, 1, element);
		assertEquals(testCube.get(2, 9, 1), element);
	}
	
	@Test
	public void testGetWithMultipleElement() {
		Cube<Object> testCube = new BoundedCube<>(10, 10, 1);
		Object element1 = new Object();
		Object element2 = new Object();
		Object element3 = new Object();
		Object element4 = new Object();
		
		testCube.add(1, 1, 1, element1);
		testCube.add(1, 1, 1, element2);
		testCube.add(1, 1, 1, element3);
		testCube.add(1, 1, 1, element4);

		assertEquals(testCube.get(1, 1, 1), element1);
		testCube.remove(1, 1, 1, element1);
		testCube.remove(1, 1, 1, element3);
		assertEquals(testCube.get(1, 1, 1), element2);
		testCube.remove(1, 1, 1, element2);
		assertEquals(testCube.get(1, 1, 1), element4);
		testCube.remove(1, 1, 1, element4);
		assertNull(testCube.get(1, 1, 1));
		
	}

	@Test
	public void testMultipleRandomPosition() {
		Random r = new Random();
		Object[] objects = new Object[100];
		for (int i = 0; i <= 99; i++) {
			objects[i] = new Object();
		}
		Cube<Object> testCube = new BoundedCube<>(100, 100, 35);
		for (int i = 0; i <= 99; i++) {
			int random = r.nextInt(99);
			testCube.add(random, random, 1, objects[random]);
		}
		for (int i = 0; i <= 99; i++) {
			int random = r.nextInt(99);
			Object compare = testCube.get(random, random, 1);
			if (compare != null) {
				assertEquals(compare, objects[random]);
			}
		}
	}

		
	
	
	@Test
	public void testEmptyHeader() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		testCube.add(0, 0, 0, element);
		testCube.remove(0, 0, 0, element);
		testCube.add(5, 5, 5, element);
		testCube.remove(5, 5, 5, element);
	}
	
	@Test
	public void testCursorPosition() {

		Random r = new Random();
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		Object element = new Object();
		
		for (int i = 0; i <= 1000; i++) {
			testCube.add(r.nextInt(5321), r.nextInt(3428), r.nextInt(35), new Object());
		}
		
		testCube.add(22, 33, 4, element);
		
		for (int i = 0; i <= 1000; i++) {
			testCube.add(r.nextInt(5321), r.nextInt(3428), r.nextInt(35), new Object());
		}
		assertEquals(testCube.get(22, 33, 4), element);
	}
	
	@Test
	public void testCursorPosition2() {

		Cube<Object> testCube = new BoundedCube<>(10, 10, 1);
		testCube.add(10, 10, 1, new Object());
		testCube.add(10, 10, 1, new Object());
	}
	
	@Test
	public void testSkipSearchingElement() {
		Cube<Object> testCube = new BoundedCube<>(10, 10, 1);
		testCube.add(1, 9, 1, new Object());
		testCube.add(2, 9, 1, new Object());
		testCube.add(1, 8, 1, new Object());
		testCube.add(8, 9, 1, new Object());
		assertEquals(testCube.get(3, 9, 1), null);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testOutOfBoundPosition() {
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
	public void testGetRemoveAll() {
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
		assertEquals(1, testCube.getAll(1, 1, 1).size());
		assertTrue(testCube.remove(1, 1, 1, element));
		testCube.add(1, 1, 1, element);
		assertTrue(testCube.remove(1, 1, 1, element));
		assertFalse(testCube.remove(1, 1, 1, element));
	}
	
	@Test
	public void testEmptyNodeAndQueue() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		Object element = new Object();
		assertFalse(testCube.remove(1, 1, 1, element));
		testCube.add(1, 1, 1, element);
		assertTrue(testCube.remove(1, 1, 1, element));
		assertNull(testCube.get(1, 1, 1));
		assertNull(testCube.getAll(1, 1, 1));
	}
	
	@Test
	public void testAddNodeInTheMiddle() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		testCube.add(1, 1, 1, new Object());
		testCube.add(3, 3, 1, new Object());
		testCube.add(1, 1, 1, new Object());
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
	public void testPerformanceContinuousCell() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		for (int i = 0; i <= 6; i++) {
			for (int j = 0; j <= 3428; j++) {
				testCube.add(j, j, i, new Object());
			}
		}
	}
	
	@Test
	public void testPerformanceRandomAccess() {
		Random r = new Random();
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 35);
		for (int i = 0; i <= 20000; i++) {
			testCube.add(r.nextInt(5321), r.nextInt(3428), r.nextInt(35), new Object());
		}
		testCube.clear();
	}
	
	@Test
	public void testClearAllLayers() {
		Random r = new Random();
		Cube<Object> testCube = new BoundedCube<>(100, 100, 35);
		for (int i = 0; i <= 2000; i++) {
			testCube.add(r.nextInt(100), r.nextInt(100), r.nextInt(35), new Object());
		}
		testCube.clear();
		for (int i = 0; i <= 35; i++) {
			for (int j = 0; j <= 100; j++) {
				assertEquals(null, testCube.get(j, j, i));
			}
		}
		

		
	}

}
