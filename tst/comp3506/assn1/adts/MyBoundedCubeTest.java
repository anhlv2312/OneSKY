package comp3506.assn1.adts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

public class MyBoundedCubeTest {
	
	@Test(timeout=500)
	public void testGetWithOneElement() {
		Cube<Object> testCube = new BoundedCube<>(5, 5, 5);
		Object element = new Object();
		testCube.add(1, 1, 1, element);
		assertThat("Only element at a position was not returned.", testCube.get(1, 1, 1), is(equalTo(element)));
	}
	
	@Test(timeout=500, expected = IllegalArgumentException.class)
	public void testArgumentException() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 33);
	}

	
	@Test(timeout=500)
	public void testPerformanceOneCell() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 32);
		for (int i = 1; i < 20000; i++) {
			testCube.add(1, 1, 1, new Object());
		}
	}
	
	@Test(timeout=500)
	public void testPerformanceMultiCell() {
		Cube<Object> testCube = new BoundedCube<>(5321, 3428, 32);
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 3428; j++) {
				testCube.add(j, j, i, new Object());
			}
		}
	}
	
}
