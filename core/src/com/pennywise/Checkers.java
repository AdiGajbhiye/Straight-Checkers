package com.pennywise;

import com.badlogic.gdx.Game;
import com.pennywise.checkers.screens.GameScreen;

public class Checkers extends Game {

    @Override
    public void create() {
        Assets.loadAll();
        setScreen(new GameScreen(this));
    }

}
