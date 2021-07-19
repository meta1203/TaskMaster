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

public class ConcurrentArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -6104897338204207229L;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public ConcurrentArrayList() {
		super();
	}
	
	public ConcurrentArrayList(Collection<? extends E> arg0) {
		super(arg0);
	}
	
	public ConcurrentArrayList(int capacity) {
		super(capacity);
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
		return new CopiedIterator<>(this);
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return new CopiedListIterator<>(this);
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return new CopiedListIterator<>(this, index);
	}
}