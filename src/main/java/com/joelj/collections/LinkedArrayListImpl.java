package com.joelj.collections;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 6:20 PM
 */
class LinkedArrayListImpl<E> implements List<E> {
	private LinkedArrayList<E> delegate;

	public static <E> List<E> wrap(LinkedArrayList<E> list) {
		return new LinkedArrayListImpl<E>(list);
	}

	private LinkedArrayListImpl(LinkedArrayList<E> delegate) {
		this.delegate = delegate;
	}

	@Override
	public int size() {
		return delegate.getSize();
	}

	@Override
	public boolean isEmpty() {
		return delegate.getSize() == 0;
	}

	@Override
	public boolean contains(Object objectToCheck) {
		for (E t : delegate) {
			if(t.equals(objectToCheck)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return delegate.iterator();
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[delegate.getSize()];
		int index = 0;
		for (E element : delegate) {
			array[index] = element;
			index++;
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A> A[] toArray(A[] array) {
		if(array.length < delegate.getSize()) {
			array = (A[]) Array.newInstance(array.getClass().getComponentType(), delegate.getSize());
		}
		int index = 0;
		for (E element : delegate) {
			array[index] = (A)element;
			index++;
		}
		return null;
	}

	@Override
	public boolean add(E toAdd) {
		delegate.add(toAdd);
		return true;
	}

	@Override
	public boolean remove(Object objectToRemove) {
		boolean found = false;
		int index = 0;
		for (E t : delegate) {
			if(t.equals(objectToRemove)) {
				found = true;
				break;
			}
			index++;
		}
		if(found) {
			delegate.remove(index);
		}
		return found;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for (Object o : collection) {
			if(!this.contains(o)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		for (E element : collection) {
			delegate.add(element);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean found = false;
		for (Object toRemove : collection){
			boolean currentlyFound = this.remove(toRemove);
			if(currentlyFound) {
				found = true;
			}
		}
		return found;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public E get(int index) {
		return delegate.get(index);
	}

	@Override
	public E set(int index, E element) {
		return delegate.replace(index, element);
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int index) {
		return delegate.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		int index = -1;

		int currentIndex = 0;
		for (E element : delegate) {
			if(element.equals(o)) {
				index = currentIndex;
				break;
			}
			currentIndex++;
		}
		return index;
	}

	@Override
	public int lastIndexOf(Object o) {
		int index = -1;

		int currentIndex = 0;
		for (E element : delegate) {
			if(element.equals(o)) {
				index = currentIndex;
				//notice there's no break
			}
			currentIndex++;
		}
		return index;
	}

	@Override
	public ListIterator<E> listIterator() {
		return delegate.iterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return LinkedArrayBlockIterator.create(delegate.getHead(), index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
}
