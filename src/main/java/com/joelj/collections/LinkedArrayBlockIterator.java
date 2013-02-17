package com.joelj.collections;

import java.util.Iterator;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 4:25 PM
 */
class LinkedArrayBlockIterator<T> implements Iterator<T> {
	private LinkedArrayList.Block<T> current;
	private int index;

	public LinkedArrayBlockIterator(LinkedArrayList.Block<T> block) {
		assert block != null : "block can't be null";
		current = block;
		index = 0;
	}

	@Override
	public boolean hasNext() {
		return current != null && ((index < current.nextIndex && current.get(index) != null) || current.next != null);
	}

	@Override
	public T next() {
		if(!hasNext()) {
			throw new IndexOutOfBoundsException("There are no more elements left to iterate.");
		}

		if(index >= current.nextIndex) {
			current = current.next;
			while(current != null && current.getNextIndex() <= 0) {
				current = current.next;
			}
			index = 0;
		}
		if(current == null) {
			throw new NullPointerException("current is null. There is a bug in the hasNext method.");
		}
		return current.get(index++);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove isn't implemented yet");
	}
}
