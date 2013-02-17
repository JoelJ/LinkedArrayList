package com.joelj.collections;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 4:33 PM
 */
public class LinkedArrayBlockIteratorTest {
	@Test
	public void testForEach() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createWithBlockSize(5);
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
	public void testRemove() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createWithBlockSize(5);
		try {
			linkedArrayList.iterator().remove();
		} catch (UnsupportedOperationException e) {
			assertEquals(e.getMessage(), "remove isn't implemented yet");
		}
	}
}
