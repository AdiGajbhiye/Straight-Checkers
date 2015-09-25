package com.pennywise;

import com.badlogic.gdx.Game;
import com.pennywise.findwords.screens.GameScreen;

public class  FindWords extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }

}
