package com.pennywise.multiplayer;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.pennywise.checkers.core.engine.CBMove;
import com.pennywise.checkers.core.persistence.Player;

import java.io.Serializable;

public class TransmissionPackage implements Serializable, Poolable {
    private static final long serialVersionUID = -1790798304239358707L;
    public static final String LOG = TransmissionPackage.class.getSimpleName();
    /*board state*/
    private int[][] gameboard = null;
    private CBMove move = null;
    private Player player;

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

    public CBMove getMove() {
        return move;
    }

    public void setMove(CBMove move) {
        this.move = move;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void reset() {
        move = null;
        gameboard = null;
    }
}
