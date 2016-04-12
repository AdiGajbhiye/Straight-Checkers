package com.pennywise.checkers.core.engine;

import java.io.Serializable;

/**
 * Created by Joshua.Nabongo on 3/29/2016.
 */
public class CBMove implements Serializable {
    public Point from, to; /* coordinates of the piece - in 8x8 notation!*/
    public Point[] path = new Point[12]; /* intermediate path coordinates of the moving piece */
    public Point[] del = new Point[12];  /* squares whose pieces are deleted after the move */
    public int[] delpiece = new int[12]; /* what is on these squares */
}
