package com.joelj.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joel Johnson
 * Date: 2/15/13
 * Time: 6:15 PM
 */
public class LinkedArrayList<T> implements Iterable<T> {
	public static final int DEFAULT_BLOCK_SIZE = 16;

	private int size;
	private int blockCount;
	private final int blockSize;
	private Block<T> head;
	private Block<T> tail;

	/**
	 * Creates an instance of {@link LinkedArrayList} with the given block size and wraps it in a {@link List}.
	 * Note: The returned instance fails the check: {@code returnedObject instanceOf LinkedArrayList} since only a wrapper is returned.
	 *
	 * @param blockSize The size of each underlying array block.
	 *                  The bigger this value, the better random access performs, but the worse remove operations perform.
	 * @param <T> The type of elements contained in the list.
	 * @return A {@link List} wrapper. See above notice about {@code instanceOf} checks.
	 */
	public static <T> List<T> createWithBlockSize(int blockSize) {
		LinkedArrayList<T> rawWithBlockSize = createRawWithBlockSize(blockSize);
		return LinkedArrayListImpl.wrap(rawWithBlockSize);
	}

	/**
	 * Same as {@link #createWithBlockSize(int)}, but uses DEFAULT_BLOCK_SIZE for the block size.
	 */
	public static <T> List<T> create() {
		return createWithBlockSize(DEFAULT_BLOCK_SIZE);
	}

	public static <T> LinkedArrayList<T> createRawWithBlockSize(int blockSize) {
		return new LinkedArrayList<T>(blockSize);
	}

	public static <T> LinkedArrayList<T> createRaw() {
		return createRawWithBlockSize(DEFAULT_BLOCK_SIZE);
	}

	private LinkedArrayList(int blockSize) {
		if(blockSize <= 0) {
			blockSize = DEFAULT_BLOCK_SIZE;
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

	/**
	 * Replaces the value at the given index with the given value.
	 * @param index The index to replace.
	 * @param toAdd The item to add at the given index.
	 * @return The value at give index BEFORE it is replaced.
	 */
	public T replace(int index, T toAdd) {
		if(toAdd == null) {
			throw new NullPointerException("cannot add null to LinkedArrayList");
		}

		Pair<Block<T>, Integer> blockIndex = getBlockIndex(index);
		Block<T> block = blockIndex.getFirst();
		Integer blockIndexValue = blockIndex.getSecond();
		T result = block.get(blockIndexValue);
		block.array[blockIndexValue] = toAdd;
		return result;
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
