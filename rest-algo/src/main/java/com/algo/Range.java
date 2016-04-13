package com.algo;

/**
 * Class that represents a range of numbers
 * 
 * @author Keith
 *
 */
public class Range {

	private final int to;
	private final int from;

	public Range(int from, int to) {
		this.from = from;
		this.to = to;

	}

	public int getTo() {
		return to;
	}

	public int getFrom() {
		return from;
	}

}
