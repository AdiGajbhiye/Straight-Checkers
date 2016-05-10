package com.pennywise.checkers.core.engine;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Joshua.Nabongo on 3/29/2016.
 */
public class Move implements Serializable {
    int n;
    int[] m = new int[16];


    @Override
    public String toString() {
        return "Move{" +
                "n=" + n +
                ", m=" + Arrays.toString(m) +
                '}';
    }
}
