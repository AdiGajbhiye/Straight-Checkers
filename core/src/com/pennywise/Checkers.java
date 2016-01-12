package com.pennywise;

import com.badlogic.gdx.Game;
import com.pennywise.checkers.screens.GameScreen;
import com.pennywise.checkers.screens.LevelScreen;
import com.pennywise.managers.GameManager;

public class Checkers extends Game {

    private GameManager mManager;

    public Checkers(GameManager manager) {
        this.mManager = manager;
    }

    @Override
    public void create() {
        Assets.loadAll();
        setScreen(new LevelScreen(this));
    }

    public GameManager getGameManager() {
        return mManager;
    }

}
