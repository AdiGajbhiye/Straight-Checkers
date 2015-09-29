package com.pennywise.findwords.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.pennywise.findwords.core.Util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

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