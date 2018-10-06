package com.pennywise.checkers.core.persistence;

/**
 * Created by Joshua.Nabongo on 4/5/2016.
 */
public class Player {
    private String name;
    private int color;
    private boolean host;

    public Player(String name, int color, boolean host) {
        this.name = name;
        this.color = color;
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }
}
