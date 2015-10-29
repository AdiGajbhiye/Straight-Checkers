package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by CHOXXY on 10/21/2015.
 */
public class OptionScreen  extends AbstractScreen
{
    private Label volumeValue;

    public OptionScreen(
            Checkers game )
    {
        super( game );
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /**
     * Updates the volume label next to the slider.
     */
    private void updateVolumeLabel()
    {

    }
}