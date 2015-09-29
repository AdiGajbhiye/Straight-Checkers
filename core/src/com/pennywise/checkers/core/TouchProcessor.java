package com.pennywise.checkers.core;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Created by Joshua.Nabongo on 9/17/2015.
 */
public class TouchProcessor extends DragListener {

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        Actor actor = (Actor) event.getListenerActor();

        System.out.println("Actor = " + actor.getX() + "," + actor.getY());
        System.out.println("Delta = " + this.getDeltaX() + "," + this.getDeltaY());

        actor.setX(actor.getX() - this.getDeltaX());
        actor.setY(actor.getY() - this.getDeltaY());
    }
}