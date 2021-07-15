package com.meta1203.taskmaster;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MultiException extends Exception {
	private static final long serialVersionUID = -2561231291236270017L;
	
	private List<Throwable> causes;
	private boolean finalized;
	
	public MultiException() {
		causes = new ArrayList<>();
	}
	
	public MultiException(List<Throwable> causes) {
		this.causes = causes;
		this.finalized = true;
	}
	
	public void addCause(Throwable cause) {
		if (!finalized) this.causes.add(cause);
		else throw new UnsupportedOperationException("MultiException is already finalized.");
	}
	
	public List<Throwable> getCauses() {
		return new ArrayList<>(this.causes); // clone the cause list to prevent any meddling
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		for (Throwable ex : causes) ex.printStackTrace();
	}
	
	@Override
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		for (Throwable ex : causes) ex.printStackTrace(s);
	}
	
	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		for (Throwable ex : causes) ex.printStackTrace(s);
	}
	
	/**
	 * Throws an Exception if any exist.
	 * <p>
	 * Will throw the single exception if there is only one, or
	 * a MultiException if there are many.
	 * @throws Throwable
	 */
	public void throwMe() throws Throwable {
		this.finalized = true;
		if (causes.size() == 0) return;
		if (causes.size() == 1) throw causes.get(0);
		throw this;
	}
	
	/**
	 * Throws a RuntimeException if any exist.
	 * <p>
	 * Will throw the single exception if there is only one, or
	 * a MultiException if there are many.
	 * @throws RuntimeException
	 */
	public void throwRuntimeMe() throws RuntimeException {
		this.finalized = true;
		if (causes.size() == 0) return;
		if (causes.size() == 1) {
			Throwable ex = causes.get(0);
			if (ex instanceof RuntimeException) throw (RuntimeException)ex;
			throw new RuntimeException(ex);
		}
		throw new RuntimeException(this);
	}
}
