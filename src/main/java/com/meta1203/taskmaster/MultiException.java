package com.meta1203.taskmaster;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MultiException extends RuntimeException {
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
		System.err.println("MultiException caused by: ");
		int x = 0;
		for (Throwable ex : causes) {
			System.err.println("Cause " + x + ": ");
			ex.printStackTrace();
			x++;
		}
	}
	
	@Override
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		s.println("MultiException caused by: ");
		int x = 0;
		for (Throwable ex : causes) {
			s.println("Cause " + x + ": ");
			ex.printStackTrace(s);
			x++;
		}
	}
	
	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		s.println("MultiException caused by: ");
		int x = 0;
		for (Throwable ex : causes) {
			s.println("Cause " + x + ": ");
			ex.printStackTrace(s);
			x++;
		}
	}
	
	/**
	 * Throws a RuntimeException if any exist.
	 * <p>
	 * Will throw the single exception if there is only one, or
	 * a MultiException if there are many.
	 * @throws RuntimeException
	 */
	public void throwMe() throws RuntimeException {
		this.finalized = true;
		if (causes.size() == 0) return;
		if (causes.size() == 1) {
			Throwable ex = causes.get(0);
			if (ex instanceof RuntimeException) throw (RuntimeException)ex;
			throw new RuntimeException(ex);
		}
		throw this;
	}
}
