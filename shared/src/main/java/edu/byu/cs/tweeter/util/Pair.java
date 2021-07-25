package edu.byu.cs.tweeter.util;

/**
 * Generic pair class that represents a pair of values.
 *
 * @param <F> type of the first pair member.
 * @param <S> type of the second pair member.
 */
public class Pair<F, S> {
    private F first; //first member of pair
    private S second; //second member of pair

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}