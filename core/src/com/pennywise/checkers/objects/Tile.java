package com.pennywise.checkers.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Joshua on 4/16/2015.
 */
public class Tile extends Label {

    private boolean touched;

    public Tile(CharSequence tileValue, LabelStyle skin) {
        super(tileValue, skin);
        touched = false;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}