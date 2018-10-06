package com.pennywise.checkers.core;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by joshua.nabongo on 4/24/2015.
 */
public class Cam extends OrthographicCamera {

    public static Cam instance = new Cam();

    private Cam() {
        position.set(0, 0, 0);
        setToOrtho(true, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // flip y-axis
        update();
    }
}
