package com.pennywise.findwords.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pennywise.FindWords;

/**
 * Created by Joshua.Nabongo on 6/8/2015.
 */
public abstract class AbstractScreen implements Screen {

    protected FindWords game;
    protected SpriteBatch batch;

    public AbstractScreen(FindWords game) {
        this.game = game;
        batch = new SpriteBatch();
    }


}
