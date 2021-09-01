package com.meta1203.taskmaster.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author Hunter
 *
 * @param <E> the type of elements in this list
 */
public class ConcurrentArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -6104897338204207229L;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private boolean snapshot = false;
	
	/**
	 * Creates a new, empty ConcurrentArrayList
	 * The Collection interface iterator functions return iterators synchronized to the underlying list 
	 */
	public ConcurrentArrayList() {
		super();
	}
	
	/**
	 * Creates a new ConcurrentArrayList containing the contents of the given collection
	 * The Collection interface iterator functions return iterators synchronized to the underlying list 
	 * @param arg0 the collection to copy into the new list
	 */
	public ConcurrentArrayList(Collection<? extends E> arg0) {
		super(arg0);
	}
	
	/**
	 * Creates a new, empty ConcurrentArrayList with the given starting capacity
	 * The Collection interface iterator functions return iterators synchronized to the underlying list
	 * @param capacity the starting capacity of the list 
	 */
	public ConcurrentArrayList(int capacity) {
		super(capacity);
	}
	
	/**
	 * Creates a new, empty ConcurrentArrayList
	 * @param snapshot should the Collection interface iterator functions return CopiedIterators/CopiedListIterators
	 */
	public ConcurrentArrayList(boolean snapshot) {
		this();
		this.snapshot = snapshot;
	}
	
	/**
	 * Creates a new ConcurrentArrayList containing the contents of the given collection
	 * @param arg0 the collection to copy into the new list
	 * @param snapshot should the Collection interface iterator functions return CopiedIterators/CopiedListIterators
	 */
	public ConcurrentArrayList(Collection<? extends E> arg0, boolean snapshot) {
		this(arg0);
		this.snapshot = snapshot;
	}
	
	/**
	 * Creates a new, empty ConcurrentArrayList with the given starting capacity
	 * @param capacity the starting capacity of the list 
	 * @param snapshot should the Collection interface iterator functions return CopiedIterators/CopiedListIterators
	 */
	public ConcurrentArrayList(int capacity, boolean snapshot) {
		this(capacity);
		this.snapshot = snapshot;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		lock.readLock().lock();
		try {
			return super.containsAll(c);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public boolean contains(Object o) {
		lock.readLock().lock();
		try {
			return super.contains(o);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public boolean equals(Object o) {
		lock.readLock().lock();
		try {
			return super.equals(o);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public void forEach(Consumer<? super E> action) {
		lock.readLock().lock();
		try {
			super.forEach(action);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public E get(int index) {
		lock.readLock().lock();
		try {
			return super.get(index);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public int hashCode() {
		lock.readLock().lock();
		try {
			return super.hashCode();
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public int indexOf(Object o) {
		lock.readLock().lock();
		try {
			return super.indexOf(o);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty(); // Immediate return doesn't require a read lock
	}
	
	@Override
	public int lastIndexOf(Object o) {
		lock.readLock().lock();
		try {
			return super.lastIndexOf(o);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return super.size();
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public Object[] toArray() {
		lock.readLock().lock();
		try {
			return super.toArray();
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public <T> T[] toArray(IntFunction<T[]> generator) {
		lock.readLock().lock();
		try {
			return super.toArray(generator);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		lock.readLock().lock();
		try {
			return super.toArray(a);
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public String toString() {
		lock.readLock().lock();
		try {
			return super.toString();
		} finally {
			lock.readLock().unlock();			
		}
	}
	
	@Override
	public boolean add(E e) {
		lock.writeLock().lock();
		try {
			return super.add(e);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void add(int index, E element) {
		lock.writeLock().lock();
		try {
			super.add(index, element);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		lock.writeLock().lock();
		try {
			return super.addAll(c);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		lock.writeLock().lock();
		try {
			return super.addAll(index, c);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			super.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public Object clone() {
		lock.readLock().lock();
		try {
			return super.clone();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	public void ensureCapacity(int minCapacity) {
		lock.writeLock().lock();
		try {
			super.ensureCapacity(minCapacity);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public E remove(int index) {
		lock.writeLock().lock();
		try {
			return super.remove(index);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void trimToSize() {
		lock.writeLock().lock();
		try {
			super.trimToSize();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean remove(Object o) {
		lock.writeLock().lock();
		try {
			return super.remove(o);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		lock.writeLock().lock();
		try {
			return super.removeAll(c);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		lock.writeLock().lock();
		try {
			return super.removeIf(filter);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		lock.writeLock().lock();
		try {
			super.removeRange(fromIndex, toIndex);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		lock.writeLock().lock();
		try {
			super.replaceAll(operator);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		lock.writeLock().lock();
		try {
			return super.retainAll(c);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public E set(int index, E element) {
		lock.writeLock().lock();
		try {
			return super.set(index, element);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void sort(Comparator<? super E> c) {
		lock.writeLock().lock();
		try {
			super.sort(c);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public Iterator<E> iterator() {
		return snapshot ? new CopiedIterator<>(this) : new Itr();
	}
	
	public CopiedIterator<E> copiedIterator() {
		return new CopiedIterator<>(this);
	}
	
	public Iterator<E> syncedIterator() {
		return new Itr();
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return snapshot ? new CopiedListIterator<>(this) : new ListItr();
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return snapshot ? new CopiedListIterator<>(this, index) : new ListItr(index);
	}
	
	public ListIterator<E> copiedListIterator() {
		return new CopiedListIterator<>(this);
	}
	
	public ListIterator<E> copiedListIterator(int index) {
		return new CopiedListIterator<>(this, index);
	}
	
	public ListIterator<E> syncedListIterator() {
		return new ListItr();
	}
	
	public ListIterator<E> syncedListIterator(int index) {
		return new ListItr(index);
	}
	
	private class Itr implements Iterator<E> {
		private int pointer = 0;
		
		@Override
		public boolean hasNext() {
			return pointer < size();
		}

		@Override
		public E next() {
			return get(pointer++);
		}
	}
	
	private class ListItr implements ListIterator<E> {
		private int pointer;
		
		public ListItr() {
			pointer = 0;
		}
		
		public ListItr(int index) {
			pointer = index;
		}
		
		@Override
		public boolean hasNext() {
			return pointer < size();
		}

		@Override
		public E next() {
			return get(pointer++);
		}

		@Override
		public boolean hasPrevious() {
			return pointer > 0 && pointer <= size();
		}

		@Override
		public E previous() {
			return get(pointer--);
		}

		@Override
		public int nextIndex() {
			return hasNext() ? pointer + 1 : size();
		}

		@Override
		public int previousIndex() {
			return hasPrevious() ? pointer - 1: -1;
		}

		@Override
		public void remove() {
			ConcurrentArrayList.this.remove(pointer);
		}

		@Override
		public void set(E e) {
			ConcurrentArrayList.this.set(pointer, e);
		}

		@Override
		public void add(E e) {
			ConcurrentArrayList.this.add(pointer, e);
		}
	}
}
