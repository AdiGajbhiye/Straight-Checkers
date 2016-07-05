package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;

/**
 * Created by Joshua.Nabongo on 6/8/2015.
 */
public abstract class AbstractScreen implements Screen {

    public static final String LOG = AbstractScreen.class.getSimpleName();
    private final static String FILE_UI_SKIN = "uiskin.json";

    protected Checkers game;
    private Table table;
    private Skin skin;
    private OrthographicCamera uiCam;
    private Stage uiStage;
    private boolean isBackButtonActive = true;


    public AbstractScreen(Checkers game) {
        this.game = game;
    }

    public Stage getUIStage() {
        if (uiStage == null) {
            uiStage = new Stage();
            uiCam = new OrthographicCamera();
            uiCam.position.set(0, 0, 0);
            uiCam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // don't flip y-axis
            uiCam.update();

            uiStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, uiCam));
            Gdx.input.setInputProcessor(uiStage);
            setBackButton();
            uiStage.addActor(backGround());
        }
        return uiStage;
    }

    private Table backGround() {
        Table layer = new Table();
        Image bg = new Image(getSkin().getDrawable("background"));
        layer.add(bg).height(Constants.GAME_HEIGHT).width(Constants.GAME_WIDTH).expandX().expandY();
        layer.setFillParent(true);
        return layer;
    }

    public Skin getSkin() {
        if (skin == null) {
            FileHandle skinFile = Gdx.files.internal(FILE_UI_SKIN);
            skin = new Skin(skinFile);
        }
        return skin;
    }


    public Table getTable() {
        if (table == null) {
            table = new Table(getSkin());
            table.setFillParent(true);
            table.center().top().padTop(120);
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

        skin = null;
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
        //dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    /**
     * Override this method to do some function when back button pressed, only
     * works if setBackButtonActive(true)
     */
    public void keyBackPressed() {

    }

    /**
     * Set the back button active for the screen. Sets
     * "Gdx.input.setCatchBackKey(true)" and override the method
     * "keyBackPressed" to add desired functionality to back button
     *
     * @param isBackButtonActive to use or not to use the back button
     * @see keyBackPressed
     */
    public void setBackButtonActive(boolean isBackButtonActive) {
        Gdx.input.setCatchBackKey(isBackButtonActive);
        this.isBackButtonActive = isBackButtonActive;

    }

    /**
     * Back button
     */
    private void setBackButton() {
        uiStage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                    if (isBackButtonActive) {
                        keyBackPressed();
                    }
                }
                return false;
            }
        });
    }
}
