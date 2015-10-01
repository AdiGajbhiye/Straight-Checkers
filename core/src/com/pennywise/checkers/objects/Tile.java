package com.pennywise.checkers.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.pennywise.checkers.core.logic.CellEntry;
import com.pennywise.checkers.core.logic.Player;

/**
 * Created by Joshua on 4/16/2015.
 */
public class Tile extends Label {

    private CellEntry cellEntry;

    public Tile(CellEntry cellEntry, LabelStyle skin) {
        super("", skin);
        this.cellEntry = cellEntry;
    }

    public CellEntry getCellEntry() {
        return cellEntry;
    }

    public void setCellEntry(CellEntry cellEntry) {
        this.cellEntry = cellEntry;
    }
}