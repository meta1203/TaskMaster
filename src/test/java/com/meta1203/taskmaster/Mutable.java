package com.meta1203.taskmaster;

public class Mutable {
	private int one;
	private long two;
	private String three;
	
	public Mutable(int one, long two, String three) {
		this.one = one;
		this.two = two;
		this.three = three;
	}
	
	public int getOne() {
		return one;
	}
	public void setOne(int one) {
		this.one = one;
	}
	public long getTwo() {
		return two;
	}
	public void setTwo(long two) {
		this.two = two;
	}
	public String getThree() {
		return three;
	}
	public void setThree(String three) {
		this.three = three;
	}
	
	@Override
	public String toString() {
		return "Mutable [one=" + one + ", two=" + two + ", three=" + three + "]";
	}
}
