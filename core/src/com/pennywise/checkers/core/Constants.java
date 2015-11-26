package com.pennywise.checkers.core;

import com.badlogic.gdx.Gdx;

/**
 * Created by Joshua on 4/23/2015.
 */
public class Constants {
    public static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float SCREEN_WIDTH = Gdx.graphics.getWidth();

    // game will run on different Android devices out there. We need to handle screen resolution properly.
    // To do this, we are going to assume that the width of the game is always 148.
    // The height will be dynamically determined!
    // We will calculate our device's screen resolution and determine how tall the game should be.

    public static final float GAME_WIDTH = 480;
    public static final float GAME_HEIGHT = SCREEN_HEIGHT / (SCREEN_WIDTH / GAME_WIDTH);

    //GAME LEVEL
    public static final int EASY = 2;
    public static final int NORMAL = 4;
    public static final int HARD = 6;

    //GAME TYPE
    public static final int VSHUMAN = 11;
    public static final int VSCPU = 12;

}
