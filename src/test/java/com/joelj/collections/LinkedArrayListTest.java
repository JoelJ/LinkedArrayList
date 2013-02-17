package com.joelj.collections;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 3:49 PM
 */
public class LinkedArrayListTest {
	@Test
	public void testInitialize() {
		LinkedArrayList<String> linkedArrayList = LinkedArrayList.createRaw();
		assertEquals(linkedArrayList.getHead(), linkedArrayList.getTail(), "After creating a new instance and before allocating a block, head and tail should be the same.");
		assertEquals(linkedArrayList.getBlockCount(), 1);
	}

	@Test
	public void testAllocateFirstNewBlock() {
		LinkedArrayList<String> linkedArrayList = LinkedArrayList.createRaw();
		linkedArrayList.allocateBlock();
		assertNotEquals(linkedArrayList.getHead(), linkedArrayList.getTail(), "After allocating a new block, head and tail should be different.");
		assertEquals(linkedArrayList.getBlockCount(), 2);
	}

	@Test
	public void testAdd() {
		LinkedArrayList<String> linkedArrayList = LinkedArrayList.createRawWithBlockSize(4);
		assertEquals(linkedArrayList.getSize(), 0);
		linkedArrayList.add("1");
		assertEquals(linkedArrayList.getSize(), 1);
		linkedArrayList.add("2");
		assertEquals(linkedArrayList.getSize(), 2);
		linkedArrayList.add("3");
		assertEquals(linkedArrayList.getSize(), 3);
		linkedArrayList.add("4");
		assertEquals(linkedArrayList.getSize(), 4);
		linkedArrayList.add("5");
		assertEquals(linkedArrayList.getSize(), 5);
	}

	@Test
	public void testAddNull() {
		LinkedArrayList<String> linkedArrayList = LinkedArrayList.createRawWithBlockSize(4);
		assertEquals(linkedArrayList.getSize(), 0);
		try {
			linkedArrayList.add(null);
			fail("Should not be able to add null to list");
		} catch (NullPointerException e) {
			assertEquals("cannot add null to LinkedArrayList", e.getMessage());
		}
	}

	@Test
	public void testAutoExpand() {
		LinkedArrayList<String> linkedArrayList = LinkedArrayList.createRawWithBlockSize(4);
		linkedArrayList.add("1");
		linkedArrayList.add("2");
		linkedArrayList.add("3");
		linkedArrayList.add("4");
		assertEquals(linkedArrayList.getHead(), linkedArrayList.getTail(), "Auto-expanded too soon");
		linkedArrayList.add("This should cause it to auto-expand");
		assertNotEquals(linkedArrayList.getHead(), linkedArrayList.getTail(), "Didn't auto-expand");
	}

	@Test
	public void testGet() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(2);
		int numberToAddAndCheck = 20;
		for (int i = 0; i < numberToAddAndCheck; i++) {
			linkedArrayList.add(i);
		}
		for (int i = 0; i < numberToAddAndCheck; i++) {
			assertEquals(linkedArrayList.get(i).intValue(), i);
		}
	}

	@Test
	public void testGetOutOfBounds() {
		int numberToAdd = 5;
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(2);
		for (int i = 0; i < numberToAdd; i++) {
			linkedArrayList.add(i);
		}
		try {
			linkedArrayList.get(-1);
			fail("-1 should always throw index out of bounds");
		} catch (IndexOutOfBoundsException ignore) {
		}

		try {
			linkedArrayList.get(Integer.MIN_VALUE);
			fail("int min value should throw index out of bounds");
		} catch (IndexOutOfBoundsException ignore) {
		}

		try {
			linkedArrayList.get(numberToAdd);
			fail("Using the size as an index should throw index out of bounds");
		} catch (IndexOutOfBoundsException ignore) {
		}

		try {
			linkedArrayList.get(Integer.MAX_VALUE);
			fail("int max value should throw index out of bounds");
		} catch (IndexOutOfBoundsException ignore) {
		}

		for(int i = 0; i < numberToAdd; i++) {
			//We should be able to index every value added
			linkedArrayList.get(i);
		}
	}

	@Test
	public void testRemoveOneBlock() {
		LinkedArrayList<String> linkedArrayList = LinkedArrayList.createRawWithBlockSize(10);
		linkedArrayList.add("1");
		linkedArrayList.add("2");
		linkedArrayList.add("3");
		linkedArrayList.add("4");

		assertEquals(linkedArrayList.getSize(), 4);

		String removed = linkedArrayList.remove(1);
		assertEquals(removed, "2");

		assertEquals(linkedArrayList.getSize(), 3);

		assertEquals(linkedArrayList.get(0), "1");
		assertEquals(linkedArrayList.get(1), "3");
		assertEquals(linkedArrayList.get(2), "4");
	}

	@Test
	public void testRemoveMiddleBlock() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(10);
		for(int i = 0; i < 30; i++) {
			linkedArrayList.add(i);
		}

		assertEquals(linkedArrayList.getSize(), 30);

		int removed = linkedArrayList.remove(linkedArrayList.getSize() / 2);
		assertEquals(removed, 15);

		assertEquals(linkedArrayList.getSize(), 29);

		//double check the values around the one removed are what we expect
		for(int i = 0; i < 15; i++) {
			assertEquals(linkedArrayList.get(i).intValue(), i);
		}
		for(int i = 15; i < 29; i++) {
			assertEquals(linkedArrayList.get(i).intValue(), i+1);
		}
	}

	@Test
	public void testRemoveEntireFirstBlock() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(10);
		for(int i = 0; i < 30; i++) {
			linkedArrayList.add(i);
		}

		assertEquals(linkedArrayList.getSize(), 30);

		for(int i = 0; i < 10; i++) {
			int removed = linkedArrayList.remove(0);
			assertEquals(removed, i);
		}

		for(int i = 0; i < 20; i++) {
			assertEquals(linkedArrayList.get(i).intValue(), i+10);
		}
	}

	@Test
	public void testRemoveEntireSecondBlock() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(10);
		for(int i = 0; i < 30; i++) {
			linkedArrayList.add(i);
		}

		assertEquals(linkedArrayList.getSize(), 30);

		for(int i = 0; i < 10; i++) {
			int removed = linkedArrayList.remove(10);
			assertEquals(removed, i+10);
		}

		for(int i = 0; i < 10; i++) {
			assertEquals(linkedArrayList.get(i).intValue(), i);
		}

		for(int i = 10; i < 20; i++) {
			assertEquals(linkedArrayList.get(i).intValue(), i+10);
		}
	}

	@Test
	public void testClear() {
		LinkedArrayList<Integer> linkedArrayList = LinkedArrayList.createRawWithBlockSize(10);
		for(int i = 0; i < 30; i++) {
			linkedArrayList.add(i);
		}
		assertEquals(linkedArrayList.getSize(), 30);
		assertEquals(linkedArrayList.getBlockCount(), 3);
		assertNotEquals(linkedArrayList.getHead(), linkedArrayList.getTail());

		linkedArrayList.clear();

		assertEquals(linkedArrayList.getSize(), 0);
		assertEquals(linkedArrayList.getBlockCount(), 1);
		assertEquals(linkedArrayList.getHead(), linkedArrayList.getTail(), "After clearing, head and tail should be the same.");
	}
}
