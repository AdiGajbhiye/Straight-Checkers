package com.pennywise.multiplayer;

import com.badlogic.gdx.utils.Pool.Poolable;

import java.io.Serializable;

public class TransmissionPackage implements Serializable, Poolable {

    private static final long serialVersionUID = -1790798304239358707L;

    public static final String LOG = TransmissionPackage.class.getSimpleName();

    /* Data */
    private String message;
    private String name;
    private boolean gameOver = false;

    /*board state*/
    private int[][] gameboard = null;
    private int[][] move = null;
    private int color = 0;

    public TransmissionPackage() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int[][] getGameboard() {
        return gameboard;
    }

    public void setGameboard(int[][] gameboard) {
        this.gameboard = gameboard;
    }

    public int[][] getMove() {
        return move;
    }

    public void setMove(int[][] move) {
        this.move = move;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void reset() {
        // submarineX = -1;
        gameOver = false;
        color = 0;
        move = null;
        message = null;
        name = null;
        gameboard = null;
    }

    public String printMove() {
        String retValue = "";
        return retValue;
    }
}
