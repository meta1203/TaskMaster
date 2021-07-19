package com.meta1203.taskmaster.collections;

import java.util.Collection;
import java.util.Iterator;

public class CopiedIterator<E> implements Iterator<E> {
	private Object[] internal;
	private int pointer;
	
	public CopiedIterator(Collection<? extends E> c) {
		this.pointer = 0;
		this.internal = c.toArray();
	}

	@Override
	public boolean hasNext() {
		return internal.length > pointer + 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		return (E) this.internal[this.pointer++];
	}
}
