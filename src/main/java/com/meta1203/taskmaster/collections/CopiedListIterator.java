package com.meta1203.taskmaster.collections;

import java.util.Collection;
import java.util.ListIterator;

/**
 * Creates a snapshot of the Collection to iterate over. Useful for concurrent access to the entirety of a Collection.
 * <p>
 * Caveats: <p>
 * Creates a copy of the Collection as a {@link ConcurrentArrayList}, so uses more memory
 * (<a href="https://www.baeldung.com/java-size-of-object#1-objects-references-and-wrapper-classes">but not 100% more, just for object references</a>)
 * <p>
 * Any changes to the objects in the Iterator will be reflected in the originating Collection.
 *   
 * @author hunterh 
 */
public class CopiedListIterator<E> implements ListIterator<E> {
	private ConcurrentArrayList<E> backing;
	private int pointer;
	
	public CopiedListIterator(Collection<? extends E> data) {
		backing = new ConcurrentArrayList<>(data);
		pointer = 0;
	}
	
	public CopiedListIterator(Collection<? extends E> data, int start) {
		backing = new ConcurrentArrayList<>(data);
		pointer = start;
	}

	@Override
	public boolean hasNext() {
		return backing.size() > pointer;
	}

	@Override
	public E next() {
		return backing.get(pointer++);
	}

	@Override
	public boolean hasPrevious() {
		return pointer > 0;
	}

	@Override
	public E previous() {
		return backing.get(pointer--);
	}

	@Override
	public int nextIndex() {
		return hasNext() ? pointer + 1 : backing.size();
	}

	@Override
	public int previousIndex() {
		return hasPrevious() ? pointer - 1: -1;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("CopiedListIterator does not support remove()");
	}

	@Override
	public void set(E e) {
		throw new UnsupportedOperationException("CopiedListIterator does not support set()");
	}

	@Override
	public void add(E e) {
		throw new UnsupportedOperationException("CopiedListIterator does not support add()");
	}
}
