package com.pennywise.checkers.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Joshua on 4/16/2015.
 */
public class Tile extends Label {

    private int cellEntry;

    public Tile(int cellEntry, LabelStyle skin) {
        super("", skin);
        this.cellEntry = cellEntry;
    }

    public int getCellEntry() {
        return cellEntry;
    }

}