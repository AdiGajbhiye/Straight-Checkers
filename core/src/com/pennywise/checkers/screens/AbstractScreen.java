package com.pennywise.checkers.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pennywise.Checkers;

/**
 * Created by Joshua.Nabongo on 6/8/2015.
 */
public abstract class AbstractScreen implements Screen {

    protected Checkers game;
    protected SpriteBatch batch;

    public AbstractScreen(Checkers game) {
        this.game = game;
        batch = new SpriteBatch();
    }


}
