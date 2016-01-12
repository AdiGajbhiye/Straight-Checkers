package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Assets;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;

/**
 * Created by Joshua.Nabongo on 6/8/2015.
 */
public abstract class AbstractScreen implements Screen {

    public static final String LOG = AbstractScreen.class.getSimpleName();

    protected Checkers game;
    private Table table;
    private Skin skin;
    private Stage uiStage;

    public AbstractScreen(Checkers game) {
        this.game = game;
    }

    public Stage getUIStage() {
        if (uiStage == null) {
            uiStage = new Stage();
            Gdx.input.setInputProcessor(uiStage);
        }
        return uiStage;
    }

    public Skin getSkin() {
        if (skin == null) {
            skin = Assets.getSkin();
        }
        return skin;
    }

    public Table getTable() {
        if (table == null) {
            table = new Table(getSkin());
            table.setFillParent(true);
            table.center();
            getUIStage().addActor(getTable());
        }
        return table;
    }

    public void dispose() {
        Gdx.app.log(LOG, "Disposing AbstractScreen");
        if (uiStage != null)
            uiStage.dispose();
        if (skin != null)
            skin.dispose();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getUIStage().act(); // Nothing happens here
        getUIStage().draw();
        // Table.drawDebug(uiStage);
    }

    public void resize(int width, int height) {
        getUIStage().getViewport().setScreenSize(width, height);
    }


    public void hide() {
        Gdx.app.log(LOG, "Will call dispose on screen");
        // This call is necessary to dispose the screen's resources
        // after hiding it. Otherwise, the resources will reside in memory!
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }


}
