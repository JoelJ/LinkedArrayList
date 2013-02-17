package com.joelj.collections;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
	public void testNext() {
		List<String> list = LinkedArrayList.create();
		for(int i = 0; i < 30; i++) {
			list.add(String.valueOf(i));
		}
		Iterator<String> iterator = list.listIterator(10);
		int index = 10; //The first value returned, according to the javadocs for listIterator(int), should be the "10"
		while(iterator.hasNext()) {
			assertEquals(iterator.next(), String.valueOf(index));
			index++;
		}

		try {
			iterator.next();
			fail("Iterators next() implementation, by definition, needs to throw NoSuckElementException if called more times than there are elements.");
		} catch(NoSuchElementException e) {
		}
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
			list.subList(0, 1);
			fail("subList(int, int) appears to have an implementation now. Remove this when there are tests that test it.");
		} catch (UnsupportedOperationException ignore) {}
	}
}
