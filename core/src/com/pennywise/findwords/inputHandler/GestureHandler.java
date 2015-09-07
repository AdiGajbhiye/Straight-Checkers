package com.pennywise.findwords.inputHandler;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.pennywise.findwords.game.GameField;

public class GestureHandler implements GestureListener {

    GameField gameField;

    public GestureHandler(GameField gameField) {
        this.gameField = gameField;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (Math.abs(velocityX) < Math.abs(velocityY)) {
            if (velocityY < 0) {
                gameField.moveUp();
                gameField.save();
                return false;
            }
            if (velocityY > 0) {
                gameField.moveDown();
                gameField.save();
                return false;
            }
        } else {
            if (velocityX < 0) {
                gameField.moveLeft();
                gameField.save();
                return false;
            }
            if (velocityX > 0) {
                gameField.moveRight();
                gameField.save();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        return false;
    }


}
