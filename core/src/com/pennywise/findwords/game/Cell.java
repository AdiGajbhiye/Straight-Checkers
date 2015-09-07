package com.pennywise.findwords.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pennywise.FindWords;

public class Cell extends Actor {

    FindWords game;

    private Sprite img;
    private int value;

    public Vector2 privPosition;

    private boolean appear = false;
    public boolean same = false;
    public boolean delete = false;
    public boolean combined = false;

    public Cell(FindWords game) {
        this.game = game;
        appear = true;
        setScale(0f);
        privPosition = new Vector2(Gdx.graphics.getWidth() / 2, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        img.draw(batch);
    }

    @Override
    public void act(float delta) {
        if (delete) toBack();
        if (appear) runAppearAnimation();
        if (same) runSameAnimation();
        if (!isTruePosition()) runTravelAnimation();
        setImg(game.skin.getSprite(String.valueOf(getValue())));
    }


    private float incAppear = 0.1f;

    public void runAppearAnimation() {
        setScale(getScaleX() + incAppear);
        if (getScaleX() >= 1) {
            appear = false;
        }
    }

    private float incSame = 0.05f;

    public void runSameAnimation() {
        setScale(getScaleX() + incSame);
        if (getScaleX() >= 1.15f) incSame = -incSame;
        if (getScaleX() <= 1) {
            setScale(1);
            incSame = -incSame;
            same = false;
        }
    }

    public void runTravelAnimation() {
        if (appear) privPosition.set(getX(), getY());
        privPosition.lerp(new Vector2(getX(), getY()), 0.2f);
    }

    public boolean isTruePosition() {
        return Math.abs(getX() - privPosition.x) < 1f
                && Math.abs(getY() - privPosition.y) < 1f;
    }

    public void update(int value) {
        setValue(value);
        setImg(game.skin.getSprite(String.valueOf(getValue())));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value > 8192) return;
        this.value = value;
        setImg(game.skin.getSprite(String.valueOf(value)));
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public void setImg(Sprite image) {
        image.setScale(getScaleX(), getScaleY());
        image.setSize(getWidth(), getHeight());
        image.setPosition(privPosition.x, privPosition.y);
        image.setOrigin(getWidth() / 2, getHeight() / 2);
        image.setAlpha(getColor().a);
        image.setRotation(getRotation());
        this.img = image;
    }
}
