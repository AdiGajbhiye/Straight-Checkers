package com.pennywise.checkers.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pennywise.checkers.core.logic.Player;

/**
 * Created by CHOXXY on 9/29/2015.
 */
public class Piece extends Actor {

    private Sprite img;
    private Player player;
    public Vector2 prevPosition;
    public boolean delete = false;

    public Piece(Sprite img, Player player) {
        delete = false;
        this.img = img;
        this.player = player;
        setScale(0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        img.draw(batch);
    }

    @Override
    public void act(float delta) {
        if (delete) toBack();
        setImg(img);
    }

    public void runTravelAnimation() {

        prevPosition.lerp(new Vector2(getX(), getY()), 0.2f);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public void setImg(Sprite image) {
        image.setScale(getScaleX(), getScaleY());
        image.setSize(getWidth(), getHeight());
        image.setPosition(prevPosition.x, prevPosition.y);
        image.setOrigin(getWidth() / 2, getHeight() / 2);
        image.setAlpha(getColor().a);
        image.setRotation(getRotation());
        this.img = image;
    }
}
