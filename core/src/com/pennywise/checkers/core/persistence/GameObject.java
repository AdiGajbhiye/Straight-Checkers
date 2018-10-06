package com.pennywise.checkers.core.persistence;

import java.util.Date;

/**
 * Created by Joshua.Nabongo on 2/4/2016.
 */
public class GameObject {

    int[][] board;
    boolean multiplayer;
    Date date;
    int turn;
    String name;

    public GameObject() {
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
