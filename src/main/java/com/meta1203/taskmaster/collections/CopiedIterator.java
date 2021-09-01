package com.meta1203.taskmaster.collections;

import java.util.Collection;
import java.util.Iterator;

/**
 * Creates a snapshot of the Collection to iterate over. Useful for concurrent access to the entirety of a Collection.
 * <p>
 * Caveats: <p>
 * Creates a copy of the Collection as an array, so uses more memory
 * (<a href="https://www.baeldung.com/java-size-of-object#1-objects-references-and-wrapper-classes">but not 100% more, just for object references</a>)
 * <p>
 * Any changes to the objects in the Iterator will be reflected in the originating Collection.
 *   
 * @author Hunter Hancock 
 */
public class CopiedIterator<E> implements Iterator<E> {
	private Object[] internal;
	private int pointer;
	
	public CopiedIterator(Collection<? extends E> c) {
		this.pointer = 0;
		this.internal = c.toArray();
	}

	@Override
	public boolean hasNext() {
		return internal.length > pointer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		return (E) this.internal[this.pointer++];
	}
}
