package com.joelj.collections;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 4:33 PM
 */
public class LinkedArrayBlockIteratorTest {
	@Test
	public void testForEach() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(5);
		int numberToAddAndCheck = 20;
		for (int i = 0; i < numberToAddAndCheck; i++) {
			linkedArrayList.add(i);
		}

		int currentIndex = 0;
		for (Integer integer : linkedArrayList) {
			assertEquals(integer.intValue(), currentIndex);
			currentIndex++;
		}
	}

	@Test
	public void testForEachWithEmptyBlock() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(5);
		int numberToAddAndCheck = 50;
		for (int i = 0; i < numberToAddAndCheck; i++) {
			linkedArrayList.add(i);
		}

		//Empty out a couple blocks
		for (int i = 0; i < 30; i++) {
			linkedArrayList.remove(0);
		}

		int currentIndex = 0;
		for (Integer integer : linkedArrayList) {
			assertEquals(integer.intValue(), currentIndex+30);
			currentIndex++;
		}
	}

	@Test
	public void testNext() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(5);
		linkedArrayList.add(1);
		Iterator<Integer> iterator = linkedArrayList.iterator();
		assertEquals(iterator.next().intValue(), 1);

		try {
			iterator.next();
			fail("Iterators next() implementation, by definition, need to throw NoSuckElementException if called more times than there are elements.");
		} catch(NoSuchElementException e) {
		}
	}

	@Test
	public void testPrevious() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRaw();
		for(int i = 0; i < 10; i++) {
			linkedArrayList.add(i*10);
		}
		LinkedArrayBlockIterator<Integer> iterator = linkedArrayList.iterator();
		for(int i = 0; i < 5; i++) {
			iterator.next();
		}

		for(int i = 4; i >= 0; i--) {
			int actual = iterator.previous();
			System.out.println(actual);
			assertEquals(actual, i * 10);
		}
	}

	@Test
	public void testSet() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRaw();
		for(int i = 0; i < 10; i++) {
			linkedArrayList.add(i*10);
		}
		LinkedArrayBlockIterator<Integer> iterator = linkedArrayList.iterator();
		for(int i = 0; i < 5; i++) {
			iterator.next();
		}

		iterator.set(5000);

		assertEquals(linkedArrayList.get(5).intValue(), 5000);
	}


	@Test
	public void testRemove() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(5);
		try {
			linkedArrayList.iterator().remove();
		} catch (UnsupportedOperationException e) {
			assertEquals(e.getMessage(), "remove isn't implemented yet");
		}
	}

	@Test
	public void testAdd() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(5);
		try {
			linkedArrayList.iterator().add(10);
		} catch (UnsupportedOperationException e) {
			assertEquals(e.getMessage(), "add isn't implemented yet");
		}
	}
}
