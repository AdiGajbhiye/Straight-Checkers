package com.pennywise.checkers.core.engine;

/**
 * Created by Joshua.Nabongo on 3/29/2016.
 */
public class CBmove {
    int ismove;          /* kind of superfluous: is 0 if the move is not a valid move */
    int newpiece;        /* what type of piece appears on to */
    int oldpiece;        /* what disappears on from */
    Point from, to; /* coordinates of the piece - in 8x8 notation!*/
    Point[] path = new Point[12]; /* intermediate path coordinates of the moving piece */
    Point[] del = new Point[12]; /* squares whose pieces are deleted after the move */
    int[] delpiece = new int[12];    /* what is on these squares */
}
