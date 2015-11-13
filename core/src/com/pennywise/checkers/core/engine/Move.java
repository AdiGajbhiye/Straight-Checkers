package com.pennywise.checkers.core.engine;

public class Move {
    public int n;
    public int[] m = new int[32];

    public Move() {
    }

    public Move set(Move mv) {
        this.n = mv.n;
        this.m = mv.m;
        return this;
    }
}
