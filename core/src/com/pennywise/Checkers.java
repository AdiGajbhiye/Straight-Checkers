package com.pennywise;

import com.badlogic.gdx.Game;
import com.pennywise.checkers.screens.GameScreen;

public class Checkers extends Game {

    private AdsController mController;

    public Checkers(AdsController controller) {
        this.mController = controller;
    }

    @Override
    public void create() {
        Assets.loadAll();
        setScreen(new GameScreen(this));
    }

    public AdsController getAdController() {
        return mController;
    }

}
