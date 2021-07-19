package com.meta1203.taskmaster.collections;

import java.util.Collection;
import java.util.ListIterator;

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
