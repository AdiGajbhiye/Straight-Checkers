package com.company;

public class CBMove            	/* all the information you need about a move */ {
    int ismove;          /* kind of superfluous: is 0 if the move is not a valid move */
    int newpiece;        /* what type of piece appears on to */
    int oldpiece;        /* what disappears on from */
    Coord from, to; /* coordinates of the piece - in 8x8 notation!*/
    Coord[] path = new Coord[12]; /* intermediate path coordinates of the moving piece */
    Coord[] del = new Coord[12]; /* squares whose pieces are deleted after the move */
    int[] delpiece = new int[12];    /* what is on these squares */

    public CBMove() {
    }

    public int getIsmove() {
        return ismove;
    }

    public void setIsmove(int ismove) {
        this.ismove = ismove;
    }

    public int getNewpiece() {
        return newpiece;
    }

    public void setNewpiece(int newpiece) {
        this.newpiece = newpiece;
    }

    public int getOldpiece() {
        return oldpiece;
    }

    public void setOldpiece(int oldpiece) {
        this.oldpiece = oldpiece;
    }

    public Coord getFrom() {
        return from;
    }

    public void setFrom(Coord from) {
        this.from = from;
    }

    public Coord getTo() {
        return to;
    }

    public void setTo(Coord to) {
        this.to = to;
    }

    public Coord[] getPath() {
        return path;
    }

    public void setPath(Coord[] path) {
        this.path = path;
    }

    public Coord[] getDel() {
        return del;
    }

    public void setDel(Coord[] del) {
        this.del = del;
    }

    public int[] getDelpiece() {
        return delpiece;
    }

    public void setDelpiece(int[] delpiece) {
        this.delpiece = delpiece;
    }
}
