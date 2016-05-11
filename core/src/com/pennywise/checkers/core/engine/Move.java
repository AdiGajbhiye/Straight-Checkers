package com.pennywise.checkers.core.engine;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Joshua.Nabongo on 3/29/2016.
 */
public class Move implements Serializable {
    int n;
    int[] m = new int[16];


    public Move() {
    }

    /**
     * Constructs a move from the given move
     *
     * @param m The move
     */
    public Move(Move m) {
        set(m);
    }

    /*In Java, objects are manipulated through reference variables, and there is no
      operator for copying an object—the assignment operator duplicates the reference,
      not the object.*/
    public Move cpy() {
        return new Move(this);
    }

    public Move set(Move mv) {
        n = mv.n;
        System.arraycopy(mv.m, 0, m, 0, m.length);
        return this;
    }


    @Override
    public String toString() {
        return "Move{" +
                "n=" + n +
                ", m=" + Arrays.toString(m) +
                '}';
    }
}
