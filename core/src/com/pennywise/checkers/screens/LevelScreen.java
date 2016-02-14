package com.pennywise.checkers.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.persistence.SaveUtil;

/**
 * Created by Joshua.Nabongo on 1/14/2016.
 */
public class LevelScreen extends AbstractScreen {

    public static final String LOG = LevelScreen.class.getSimpleName();
    private Label welcomeLabel;

    public LevelScreen(Checkers game) {
        super(game);
        game.setMultiplayer(false);
        game.setHost(false);
    }

    @Override
    public void show() {
        getTable().row();
        welcomeLabel = new Label("Difficulty", getSkin());
        getTable().add(welcomeLabel).spaceBottom(30);
        getTable().row();


        if (SaveUtil.exists()) {
            final TextButton continew = new TextButton("Continue", getSkin(), "orange");
            getTable().add(continew).size(320, 60).uniform().spaceBottom(15);
            continew.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new GameScreen(game, Constants.HARD));
                }
            });
        }

        getTable().row();

        final TextButton easy = new TextButton("Easy", getSkin());
        getTable().add(easy).size(320, 60).uniform().spaceBottom(15);
        easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, Constants.EASY));
            }
        });

        getTable().row();

        final TextButton normal = new TextButton("Normal", getSkin());
        getTable().add(normal).size(320, 60).uniform().spaceBottom(15);
        normal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, Constants.NORMAL));
            }
        });

        getTable().row();

        final TextButton hard = new TextButton("Hard", getSkin());
        getTable().add(hard).size(320, 60).uniform().spaceBottom(15);
        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, Constants.HARD));
            }
        });

        getTable().row();


    }


    public void dispose() {
        super.dispose();
    }

}

