package com.pennywise.checkers.core.engine;

public class CbMove            	/* all the information you need about a move */ {

    public int status;
    public int eval;
    public int jumps;          /* jump moves */
    public int newpiece;        /* what type of piece appears on to */
    public int oldpiece;        /* what disappears on from */
    public Coord from, to; /* coordinates of the piece - in 8x8 notation!*/
    public Coord[] path = new Coord[12]; /* intermediate path coordinates of the moving piece */
    public Coord[] del = new Coord[12]; /* squares whose pieces are deleted after the move */
    public int delpiece[] = new int[12];    /* what is on these squares */

}
