package com.pennywise.checkers.core.engine;

import java.io.Serializable;

/**
 * Created by Joshua.Nabongo on 3/29/2016.
 */

/* coordinate class for board coordinates */
public class Point implements Serializable {
    public int x = -1;
    public int y = -1;

    public Point() {
    }

    public Point(Point p) {
        set(p);
    }

    public Point cpy() {
        return new Point(this);
    }

    public Point set(Point p) {
        x = p.x;
        y = p.y;
        return this;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
