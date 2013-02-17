package com.joelj.collections;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.Collections;
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

	@Test
	public void unsupportedMethods() {
		List<Object> list = LinkedArrayList.create();

		try {
			list.addAll(0, Collections.emptyList());
			fail("addAll(int, Collection) appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}

		try {
			list.retainAll(Collections.emptyList());
			fail("retainAll(Collection) appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}

		try {
			list.add(0, new Object());
			fail("add(int, E) appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}

		try {
			list.listIterator();
			fail("listIterator() appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}

		try {
			list.listIterator(0);
			fail("listIterator(int) appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}

		try {
			list.subList(0, 1);
			fail("subList(int, int) appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}
	}
}
