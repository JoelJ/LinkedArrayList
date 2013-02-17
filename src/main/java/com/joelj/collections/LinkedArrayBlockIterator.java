package com.joelj.collections;

import java.util.*;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 4:25 PM
 */
class LinkedArrayBlockIterator<T> implements ListIterator<T> {
	private LinkedArrayList.Block<T> current;
	private int index;
	private int globalIndex;
	private Deque<LinkedArrayList.Block<T>> previousBlocks;

	public LinkedArrayBlockIterator(LinkedArrayList.Block<T> block) {
		assert block != null : "block can't be null";
		current = block;
		index = 0;
		globalIndex = 0;
		previousBlocks = new LinkedList<LinkedArrayList.Block<T>>();
	}

	@Override
	public boolean hasNext() {
		return current != null && ((index < current.nextIndex && current.get(index) != null) || current.next != null);
	}

	@Override
	public T next() {
		if(!hasNext()) {
			throw new NoSuchElementException("There are no more elements left to iterate.");
		}

		if(index >= current.nextIndex) {
			previousBlocks.push(current);
			current = current.next;
			while(current != null && current.getNextIndex() <= 0) {
				current = current.next;
			}
			index = 0;
		}
		if(current == null) {
			throw new NullPointerException("current is null. There is a bug in the hasNext method.");
		}

		T result = current.get(index);
		globalIndex++;
		index++;
		return result;
	}

	@Override
	public boolean hasPrevious() {
		return globalIndex > 0;
	}

	@Override
	public T previous() {
		if(globalIndex <= 0) {
			throw new NoSuchElementException("Already at the beginning.");
		}
		if(index > 0) {
			index--;
			globalIndex--;
			return current.get(index);
		} else {
			LinkedArrayList.Block<T> pop = previousBlocks.pop();
			current = pop;
			index = current.getNextIndex() - 1;
			globalIndex--;
			return current.get(current.getNextIndex());
		}
	}

	@Override
	public int nextIndex() {
		return globalIndex + 1;
	}

	@Override
	public int previousIndex() {
		return globalIndex - 1;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove isn't implemented yet");
	}

	@Override
	public void set(T t) {
		current.array[index] = t;
	}

	@Override
	public void add(T t) {
		throw new UnsupportedOperationException("add isn't implemented yet");
	}
}
