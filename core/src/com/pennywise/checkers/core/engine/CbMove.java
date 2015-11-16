package com.pennywise.checkers.core.engine;

public class CbMove            	/* all the information you need about a move */ {
    int ismove;          /* kind of superfluous: is 0 if the move is not a valid move */
    int newpiece;        /* what type of piece appears on to */
    int oldpiece;        /* what disappears on from */
    Coord from, to; /* coordinates of the piece - in 8x8 notation!*/
    Coord[] path = new Coord[12]; /* intermediate path coordinates of the moving piece */
    Coord[] del = new Coord[12]; /* squares whose pieces are deleted after the move */
    int delpiece[] = new int[12];    /* what is on these squares */
}
