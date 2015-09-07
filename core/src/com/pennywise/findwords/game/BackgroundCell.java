package com.pennywise.findwords.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pennywise.FindWords;

class BackgroundCell extends Actor {

    FindWords game;

    private Sprite img;

    public BackgroundCell(FindWords game) {
        this.game = game;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        img.draw(batch);
    }

    public void update() {
        setImg(game.skin.getSprite("0"));
    }

    public void setImg(Sprite image) {
        image.setScale(getScaleX(), getScaleY());
        image.setSize(getWidth(), getHeight());
        image.setPosition(getX(), getY());
        image.setOrigin(getWidth() / 2, getHeight() / 2);
        image.setAlpha(getColor().a);
        image.setRotation(getRotation());
        this.img = image;
    }
}