package com.pennywise.findwords.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.FindWords;
import com.pennywise.findwords.game.GameField;
import com.pennywise.findwords.inputHandler.GestureHandler;

public class GameScreen implements Screen {

    FindWords game;

    public GameField gameField;

    private GestureHandler gestureHandler;

    private SpriteBatch batch;
    private Stage stage, stageScore;
    Button btn_score, btn_best, btn_gameOver, btn_restart;
    Button btn_back;

    public GameScreen(FindWords game) {
        this.game = game;

        gameField = new GameField(game);

        gestureHandler = new GestureHandler(gameField);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stageScore = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        createButtons();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new GestureDetector(gestureHandler)));
    }

    private final Vector2 finalAlpha = new Vector2(1f, 0);
    private Vector2 currentAlpha = new Vector2(0f, 0);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(game.background.r, game.background.g, game.background.b, game.background.a);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		game.save();

        if (game.score > game.best) {
            game.best = game.score;
           /* if (game.actionResolver.getSignedInGPGS()) {
                game.actionResolver.submitScoreGPGS(game.best);
            }*/
            game.save();
        }
        stageScore.clear();
        createScoreLabels();

        if (gameField.gameOver) {
            stage.addActor(btn_gameOver);
            currentAlpha.lerp(finalAlpha, 0.02f);
            btn_gameOver.getColor().a = currentAlpha.x;
        }

        batch.begin();
        gameField.render(batch);
        stage.draw();
        stageScore.draw();
        batch.end();
    }

    public void createButtons() {
        ButtonStyle styleBack = new ButtonStyle();
        styleBack.up = game.skinButton.getDrawable("btn_back_up");
        styleBack.down = game.skinButton.getDrawable("btn_back_down");

        btn_back = new Button(styleBack);
        btn_back.setHeight(Gdx.graphics.getHeight() / 10);
        btn_back.setWidth(btn_back.getHeight() * styleBack.up.getMinWidth() / styleBack.up.getMinHeight());
        btn_back.setX(Gdx.graphics.getWidth() * 0.95f - btn_back.getWidth());
        btn_back.setY(Gdx.graphics.getHeight() * 0.02f);

        btn_back.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.playClick();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                if (x > btn_back.getWidth() || y > btn_back.getHeight() || x < 0 || y < 0) return;
                game.setScreen(game.getMainMenuScreen());
            }
        });

        stage.addActor(btn_back);

        ButtonStyle styleGameOver = new ButtonStyle();
        styleGameOver.up = game.skinButton.getDrawable("game_over");

        btn_gameOver = new Button(styleGameOver);
        btn_gameOver.setWidth(Gdx.graphics.getWidth() * 0.8f);
        btn_gameOver.setHeight(btn_gameOver.getWidth() * styleGameOver.up.getMinHeight() / styleGameOver.up.getMinWidth());
        btn_gameOver.setX(Gdx.graphics.getWidth() / 2 - btn_gameOver.getWidth() / 2);
        btn_gameOver.setY(Gdx.graphics.getHeight() - btn_gameOver.getHeight() * 1.2f);

        ButtonStyle styleRestart = new ButtonStyle();
        styleRestart.up = game.skinButton.getDrawable("restart");

        btn_restart = new Button(styleRestart);
        btn_restart.setHeight(Gdx.graphics.getHeight() / 10);
        btn_restart.setWidth(btn_restart.getHeight() * styleRestart.up.getMinWidth() / styleRestart.up.getMinHeight());
        btn_restart.setX(Gdx.graphics.getWidth() * 0.05f);
        btn_restart.setY(Gdx.graphics.getHeight() * 0.02f);

        btn_restart.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                game.playClick();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                if (x > btn_restart.getWidth() || y > btn_restart.getHeight() || x < 0 || y < 0)
                    return;
                game.newGame();
            }
        });

        stage.addActor(btn_restart);
    }

    public void createScoreLabels() {
        ButtonStyle styleScore = new ButtonStyle();
        styleScore.up = game.skinNumber.getDrawable("score");

        btn_score = new Button(styleScore);
        btn_score.setHeight(Gdx.graphics.getHeight() / 20);
        btn_score.setWidth(btn_score.getHeight() * styleScore.up.getMinWidth() / styleScore.up.getMinHeight());
        btn_score.setX(Gdx.graphics.getWidth() * 1 / 20);
        btn_score.setY(Gdx.graphics.getHeight() - Gdx.graphics.getWidth() - btn_score.getHeight() * 1.5f);

        stageScore.addActor(btn_score);

        float w = btn_score.getX() + btn_score.getWidth() * 1.2f;
        String s = String.valueOf(game.score);
        for (int i = 0; i < s.length(); i++) {
            ButtonStyle style = new ButtonStyle();
            style.up = game.skinNumber.getDrawable("" + s.charAt(i));

            Button btn = new Button(style);
            btn.setHeight(Gdx.graphics.getHeight() / 20);
            btn.setWidth(btn.getHeight() * style.up.getMinWidth() / style.up.getMinHeight());
            btn.setX(w);
            btn.setY(Gdx.graphics.getHeight() - Gdx.graphics.getWidth() - btn.getHeight() * 1.5f);

            stageScore.addActor(btn);
            w += btn.getWidth();
        }

        ButtonStyle styleBest = new ButtonStyle();
        styleBest.up = game.skinNumber.getDrawable("best");

        btn_best = new Button(styleBest);
        btn_best.setHeight(Gdx.graphics.getHeight() / 20);
        btn_best.setWidth(btn_best.getHeight() * styleBest.up.getMinWidth() / styleBest.up.getMinHeight());
        btn_best.setX(Gdx.graphics.getWidth() * 1 / 20);
        btn_best.setY(Gdx.graphics.getHeight() - Gdx.graphics.getWidth() - btn_best.getHeight() * 2.8f);

        stageScore.addActor(btn_best);

        w = btn_best.getX() + btn_best.getWidth() * 1.2f;
        s = String.valueOf(game.best);
        for (int i = 0; i < s.length(); i++) {
            ButtonStyle style = new ButtonStyle();
            style.up = game.skinNumber.getDrawable("" + s.charAt(i));

            Button btn = new Button(style);
            btn.setHeight(Gdx.graphics.getHeight() / 20);
            btn.setWidth(btn.getHeight() * style.up.getMinWidth() / style.up.getMinHeight());
            btn.setX(w);
            btn.setY(Gdx.graphics.getHeight() - Gdx.graphics.getWidth() - btn.getHeight() * 2.8f);

            stageScore.addActor(btn);
            w += btn.getWidth();
        }
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
