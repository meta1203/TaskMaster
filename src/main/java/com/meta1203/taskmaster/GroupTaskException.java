package com.meta1203.taskmaster;

public class GroupTaskException extends Exception {
	private static final long serialVersionUID = 8450921343029431161L;
	
	private Task<?> origin;
	
	public GroupTaskException(Task<?> origin, Throwable cause) {
		super("Exception in " + origin.toString(), cause);
		this.origin = origin;
	}
	
	public Task<?> getOrigin() {
		return origin;
	}
}
