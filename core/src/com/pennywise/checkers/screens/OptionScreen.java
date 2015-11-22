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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Assets;
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
public class OptionScreen extends AbstractScreen {
    final float BUTTON_WIDTH = 250f;
    final float BUTTON_HEIGHT = 60f;
    Stage stage;
    Skin skin;

    public OptionScreen(
            Checkers game) {
        super(game);
    }

    @Override
    public void show() {

        skin = Assets.getSkin();
        Button humanButton = new TextButton("Human vs Human", skin);
        Button computerButton = new TextButton("Human vs Computer", skin);


        humanButton.setWidth(BUTTON_WIDTH);
        computerButton.setWidth(BUTTON_WIDTH);

        humanButton.setHeight(BUTTON_HEIGHT);
        computerButton.setHeight(BUTTON_HEIGHT);

        humanButton.setPosition(Constants.GAME_WIDTH / 2 - 125f, Constants.GAME_HEIGHT / 2);
        computerButton.setPosition(Constants.GAME_WIDTH / 2 - 125f, Constants.GAME_HEIGHT / 2 - 70f);


        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
        stage.addActor(humanButton);
        stage.addActor(computerButton);
        Gdx.input.setInputProcessor(stage);

        humanButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        skin.dispose();
        stage.dispose();
    }
}