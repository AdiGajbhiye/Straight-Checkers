package com.pennywise.checkers.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by CHOXXY on 9/29/2015.
 */
public class Piece extends Image {

    private boolean captured;
    private int player;
    private boolean selected;
    private boolean isKing;

    public Piece(SpriteDrawable img, int player) {
        super(img);
        captured = false;
        selected = false;
        isKing = false;
        this.player = player;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }


    public boolean isKing() {
        return isKing;
    }

    public void Knight(boolean b) {
        isKing =  true;
    }


}
