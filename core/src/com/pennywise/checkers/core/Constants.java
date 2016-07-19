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
    public static final double NONE = 0.01;
    public static final double EASY = 0.05;
    public static final double NORMAL = 0.10;
    public static final double HARD = 0.35;

    //GAME TYPE
    public static final int VSHUMAN = 11;
    public static final int VSCPU = 12;

    public static final int TCPPORT = 54555;
    public static final int UDPPORT = 54777;

    public static final String NAME = "name";
    public static final String COLOR = "color";
    public static final java.lang.String HOST = "host";

    public static final String USER_FILE = "user_preference";

    //Message Type
    public static final int MSG_UPDATE = 21;
    public static final int MSG_RESIGN = 22;
    public static final int MSG_DRAW = 23;
    public static final int MSG_POKE = 24;

}
