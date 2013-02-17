package com.joelj.collections;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.List;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 6:44 PM
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public class LinkedArrayListImplTest {
	@Test
	public void testBasicGet() throws Exception {
		List<String> strings = LinkedArrayList.create();
		for(int i = 0; i < 30; i++) {
			strings.add(String.valueOf(i));
		}

		for(int i = 0; i < strings.size(); i++) {
			String s = strings.get(i);
			assertEquals(s, String.valueOf(i));
		}
	}

	@Test
	public void testBasicRemove() throws Exception {
		List<String> strings = LinkedArrayList.create();
		for(int i = 0; i < 30; i++) {
			strings.add(String.valueOf(i));
		}
		strings.remove(15);
		assertEquals(strings.get(14), "14");
		assertEquals(strings.get(15), "16");
	}
}
