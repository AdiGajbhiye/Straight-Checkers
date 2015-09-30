package com.pennywise.checkers.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by Joshua.Nabongo on 9/30/2015.
 */
public class Panel extends Group {

    private NinePatch image;

    public Panel(NinePatch image) {
        this.image = image;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //image.draw(batch, getOriginX(), getOriginY(), getWidth(), getHeight());
    }

}
