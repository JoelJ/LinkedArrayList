package com.joelj.collections;

/**
 * Simple Pair class.
 * Has two values: first and second that are individually typed.
 *
 * User: Joel Johnson
 * Date: 2/16/13
 * Time: 4:58 PM
 */
public class Pair<FIRST, SECOND> {
	private final FIRST first;
	private final SECOND second;

	public static <FIRST, SECOND> Pair of(FIRST first, SECOND second) {
		return new Pair<FIRST, SECOND>(first, second);
	}

	private Pair(FIRST first, SECOND second) {
		this.first = first;
		this.second = second;
	}

	public FIRST getFirst() {
		return first;
	}

	public SECOND getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pair)) return false;

		Pair pair = (Pair) o;
		return !(first != null ? !first.equals(pair.first) : pair.first != null) && !(second != null ? !second.equals(pair.second) : pair.second != null);
	}

	@Override
	public int hashCode() {
		int result = first != null ? first.hashCode() : 0;
		result = 31 * result + (second != null ? second.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "{" + first + "," + second + '}';
	}
}
