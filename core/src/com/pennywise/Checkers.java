package com.pennywise;

import com.badlogic.gdx.Game;
import com.pennywise.checkers.screens.GameScreen;
import com.pennywise.checkers.screens.IntroScreen;

public class Checkers extends Game {

    @Override
    public void create() {
        Assets.loadAll();
        setScreen(new IntroScreen(this));
    }

}
