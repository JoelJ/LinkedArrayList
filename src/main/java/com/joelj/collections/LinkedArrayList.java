package com.joelj.collections;

import java.util.Arrays;
import java.util.Iterator;

/**
 * User: Joel Johnson
 * Date: 2/15/13
 * Time: 6:15 PM
 */
public class LinkedArrayList<T> implements Iterable<T> {
	private int size;
	private int blockCount;
	private final int blockSize;
	private Block<T> head;
	private Block<T> tail;

	public static <T> LinkedArrayList<T> createRawWithBlockSize(int size) {
		return new LinkedArrayList<T>(size);
	}

	public static <T> LinkedArrayList<T> createRaw() {
		return new LinkedArrayList<T>(16);
	}

	private LinkedArrayList(int blockSize) {
		if(blockSize <= 0) {
			blockSize = 16;
		}
		this.blockSize = blockSize;
		init(blockSize);
	}

	private void init(int blockSize) {
		this.size = 0;
		this.tail = new Block<T>(blockSize);
		this.head = tail;
		this.blockCount = 1;
	}

	/**
	 * Allocates a block of memory for more entries.
	 */
	public void allocateBlock() {
		Block<T> newBlock = new Block<T>(blockSize);
		tail.next = newBlock;
		tail = newBlock;
		blockCount++;
	}

	/**
	 * Adds the given value to the list, allocating new memory if it needs to.
	 */
	public void add(T toAdd) {
		if(toAdd == null) {
			throw new NullPointerException("cannot add null to LinkedArrayList");
		}
		if(!tail.add(toAdd)) {
			allocateBlock();
			if(!tail.add(toAdd)) {
				throw new IllegalStateException("We just allocated a new block but couldn't add to it. This line wasn't expected to ever be possible to be it.");
			}
		}
		size++;
	}

	public T get(int index) {
		Pair<Block<T>, Integer> blockIndex = getBlockIndex(index);
		return blockIndex.getFirst().get(blockIndex.getSecond());
	}

	public T remove(int index) {
		Pair<Block<T>, Integer> blockIndex = getBlockIndex(index);
		T remove = blockIndex.getFirst().remove(blockIndex.getSecond());
		size--;
		return remove;
	}

	/**
	 * Gets the block and index of that block represented by the global index
	 * @param globalIndex The index in the overall list to get.
	 * @return A pair where the first element is the block that contains the element represented by the globalIndex and
	 * 	the second element represents the index in the block that contains the element represented by the globalIndex.
	 */
	private Pair<Block<T>, Integer> getBlockIndex(int globalIndex) {
		if(globalIndex < 0 || globalIndex >= getSize()) {
			throw new IndexOutOfBoundsException(""+globalIndex);
		}
		Block<T> current = getHead();
		int currentMaxIndex = 0;
		int blockIndex = globalIndex;
		while(current != null) {
			currentMaxIndex += current.getNextIndex();

			if(globalIndex < currentMaxIndex) {
				break;
			} else {
				blockIndex -= current.getNextIndex();
			}

			current = current.next;
		}
		assert current != null : "current should never become null";

		//I'm really not sure why IntelliJ things this line needs to be checked
		//noinspection unchecked
		return Pair.of(current, blockIndex);
	}

	public int getSize() {
		return size;
	}

	public int getBlockCount() {
		return blockCount;
	}

	/**
	 * @return The first Block in the list
	 */
	public Block<T> getHead() {
		return head;
	}

	/**
	 * @return The currently last Block in the list
	 */
	public Block<T> getTail() {
		return tail;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedArrayBlockIterator<T>(this.getHead());
	}

	public void clear() {
		init(this.blockSize);
	}

	static class Block<T> {
		final T[] array;
		Block<T> next;
		int nextIndex;

		@SuppressWarnings("unchecked")
		public Block(int size) {
			assert size > 0 : "size should be positive: " + size;
			this.array = (T[])new Object[size];
			this.nextIndex = 0;
		}

		/**
		 * The max size of the block.
		 * Or, in other words: the size of the underlying array.
		 */
		public int getMaxSize() {
			return array.length;
		}

		/**
		 * The next index of the array that will be added to.
		 * this.array[getNextIndex()] will always return null.
		 */
		public int getNextIndex() {
			return nextIndex;
		}

		public boolean add(T toAdd) {
			assert getNextIndex() <= getMaxSize() : "nextIndex should never pass the maxSize";
			if(getNextIndex() == getMaxSize()) {
				return false;
			}

			assert array[nextIndex] == null : "the index we're adding to should always be null";
			array[nextIndex++] = toAdd;
			return true;
		}

		public T get(int index) {
			assert index < nextIndex : "shouldn't be indexing a value not in this block";
			return array[index];
		}

		public T remove(int index) {
			assert index < nextIndex : "shouldn't be removing a value not in this block";
			T result = get(index);

			System.arraycopy(array, index + 1, array, index + 1 - 1, nextIndex - (index + 1));
			nextIndex--;
			array[nextIndex] = null;
			return result;
		}

		@Override
		public String toString() {
			return "Block{" +
					"array=" + Arrays.toString(array) +
					", next=" + next +
					", nextIndex=" + nextIndex +
					", maxSize=" + getMaxSize() +
					'}';
		}
	}
}
