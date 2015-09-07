package com.pennywise.findwords.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.FindWords;

public class MainMenuScreen implements Screen {

    FindWords game;
    Stage stage;
    SpriteBatch batch;
    Button btn_name, btn_play, btn_sound, btn_best, btn_google, btn_leaderboard;

    private final float BTN_SIZE = 8f;

    public MainMenuScreen(FindWords game) {
        this.game = game;

        batch = new SpriteBatch();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
//		stage.clear();
//		createButtons();
    }

    @Override
    public void show() {
        stage.clear();
        createButtons();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(game.background.r, game.background.g, game.background.b, game.background.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);

        batch.begin();
        stage.draw();
        batch.end();
    }

    public void createButtons() {

        ButtonStyle styleName = new ButtonStyle();
        styleName.up = game.skinButton.getDrawable("power_of_two");

        btn_name = new Button(styleName);
        btn_name.setWidth(Gdx.graphics.getWidth());
        btn_name.setHeight(btn_name.getWidth() * styleName.up.getMinHeight() / styleName.up.getMinWidth());
        btn_name.setX(Gdx.graphics.getWidth() / 2 - btn_name.getWidth() / 2);
        btn_name.setY(Gdx.graphics.getHeight() - btn_name.getHeight());

        stage.addActor(btn_name);

        ButtonStyle style = new ButtonStyle();
        style.up = game.skinButton.getDrawable("btn_play_up");
        style.down = game.skinButton.getDrawable("btn_play_down");

        btn_play = new Button(style);
        btn_play.setHeight(Gdx.graphics.getHeight() / BTN_SIZE + 50);
        btn_play.setWidth(btn_play.getHeight() * style.up.getMinWidth() / style.up.getMinHeight());
        btn_play.setX(Gdx.graphics.getWidth() / 2 - btn_play.getWidth() / 2);
        btn_play.setY(Gdx.graphics.getHeight() * 0.5f);

        btn_play.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.playClick();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                game.setScreen(game.getGameScreen());
            }
        });

        stage.addActor(btn_play);

        ButtonStyle styleSound = new ButtonStyle();
        styleSound.up = game.skinButton.getDrawable("btn_sound_" + game.isSoundEnabled());

        btn_sound = new Button(styleSound);
        btn_sound.setHeight(Gdx.graphics.getHeight() / BTN_SIZE - 10);
        btn_sound.setWidth(btn_sound.getHeight() * styleSound.up.getMinWidth() / styleSound.up.getMinHeight());
        btn_sound.setX(Gdx.graphics.getWidth() / 2 - btn_sound.getWidth() / 2);
        btn_sound.setY(Gdx.graphics.getHeight() * 0.35f);

        btn_sound.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.playClick();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                game.setSoundEnabled(!game.isSoundEnabled());
                game.save();
                btn_sound.getStyle().up = game.skinButton.getDrawable("btn_sound_" + game.isSoundEnabled());
            }
        });

        stage.addActor(btn_sound);

        ButtonStyle styleGoolge = new ButtonStyle();
        styleGoolge.up = game.skinButton.getDrawable(/*"btn_sign_" + game.actionResolver.getSignedInGPGS()*/"btn_leaderboard");

        btn_google = new Button(styleGoolge);
        /*btn_google.setHeight(Gdx.graphics.getHeight() / BTN_SIZE - 20);
        btn_google.setWidth(btn_google.getHeight() * styleGoolge.up.getMinWidth() / styleGoolge.up.getMinHeight());
        btn_google.setX(Gdx.graphics.getWidth() - btn_google.getWidth());
        btn_google.setY(0);

        btn_google.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.playClick();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                game.isSigned = !game.isSigned;
                game.actionResolver.loginGPGS();
                btn_google.getStyle().up = game.skinButton.getDrawable("btn_sign_" + game.actionResolver.getSignedInGPGS());
            }
        });

        stage.addActor(btn_google);*/

        ButtonStyle styleLeaderboard = new ButtonStyle();
        styleLeaderboard.up = game.skinButton.getDrawable("btn_leaderboard");

        /*
        btn_leaderboard = new Button(styleLeaderboard);
        btn_leaderboard.setHeight(Gdx.graphics.getHeight() / BTN_SIZE - 20);
        btn_leaderboard.setWidth(btn_leaderboard.getHeight() * styleLeaderboard.up.getMinWidth() / styleLeaderboard.up.getMinHeight());
        btn_leaderboard.setX(btn_google.getX() - btn_leaderboard.getWidth() * 2);
        btn_leaderboard.setY(0);

        btn_leaderboard.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.playClick();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                if (game.actionResolver.getSignedInGPGS()) game.actionResolver.getLeaderboardGPGS();
            }
        });

        stage.addActor(btn_leaderboard);*/

        ButtonStyle styleBest = new ButtonStyle();
        styleBest.up = game.skinNumber.getDrawable("best");

        btn_best = new Button(styleBest);
        btn_best.setHeight(Gdx.graphics.getHeight() / 20);
        btn_best.setWidth(btn_best.getHeight() * styleBest.up.getMinWidth() / styleBest.up.getMinHeight());
        btn_best.setX(Gdx.graphics.getWidth() * 0);
        btn_best.setY(0 + btn_google.getHeight() * 1.5f);

        stage.addActor(btn_best);

        centralize();

        float w = btn_best.getX() + btn_best.getWidth();
        String s = String.valueOf(game.best);
        for (int i = 0; i < s.length(); i++) {
            ButtonStyle styleBestn = new ButtonStyle();
            styleBestn.up = game.skinNumber.getDrawable("" + s.charAt(i));

            Button btn = new Button(styleBestn);
            btn.setHeight(Gdx.graphics.getHeight() / 20);
            btn.setWidth(btn.getHeight() * styleBestn.up.getMinWidth() / styleBestn.up.getMinHeight());
            btn.setX(w);
            btn.setY(btn_best.getY());

            stage.addActor(btn);
            w += btn.getWidth();
        }
    }

    public void centralize() {
        String s = String.valueOf(game.best);
        float w = btn_best.getWidth();
        for (int i = 0; i < s.length(); i++) {
            w += Gdx.graphics.getHeight() / 20 * game.skinNumber.getDrawable("" + s.charAt(i)).getMinWidth() / game.skinNumber.getDrawable("" + s.charAt(i)).getMinHeight();
        }
        btn_best.setX(Gdx.graphics.getWidth() / 2 - w / 2);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}
