package com.pennywise.multiplayer;

import com.badlogic.gdx.utils.Pool.Poolable;

import java.io.Serializable;
import java.util.Vector;

public class TransmissionPackage implements Serializable, Poolable {
    private static final long serialVersionUID = -1790798304239358707L;
    public static final String LOG = TransmissionPackage.class.getSimpleName();
    /* Data */
    private String name;
    /*board state*/
    private int[][] gameboard = null;
    private Vector move = null;

    public TransmissionPackage() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int[][] getGameboard() {
        return gameboard;
    }

    public void setGameboard(int[][] gameboard) {
        this.gameboard = gameboard;
    }

    public Vector getMove() {
        return move;
    }

    public void setMove(Vector move) {
        this.move = move;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void reset() {
        move = null;
        name = null;
        gameboard = null;
    }

    public String printMove() {
        String retValue = "";
        return retValue;
    }
}
