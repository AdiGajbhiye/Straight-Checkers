package com.pennywise.checkers.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by CHOXXY on 9/29/2015.
 */
public class Piece extends Image {

    private  boolean captured;

    public Piece(SpriteDrawable img) {
        super(img);
        captured = false;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

}
