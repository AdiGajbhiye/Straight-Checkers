package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pennywise.Checkers;

public class MenuScreen extends AbstractScreen {

    public static final String LOG = MenuScreen.class.getSimpleName();
    private Label welcomeLabel;

    public MenuScreen(Checkers game) {
        super(game);
    }

    @Override
    public void show() {
        getTable().row();
        welcomeLabel = new Label("Straight Checkers", getSkin(),"title-text");
        getTable().add(welcomeLabel).spaceBottom(20);
        getTable().row();

        final TextButton game1Button = new TextButton("Singleplayer", getSkin(), "orange");
        getTable().add(game1Button).size(360, 70).uniform().spaceBottom(30);
        game1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelScreen(game));
            }
        });

        getTable().row();

        final TextButton game2Button = new TextButton("Multiplayer", getSkin(), "green");
        getTable().add(game2Button).size(360, 70).uniform().spaceBottom(30);
        game2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MultiplayerScreen(game));
            }
        });

        getTable().row();

        final TextButton help = new TextButton("Help", getSkin());
        getTable().add(help).size(360, 70).uniform().spaceBottom(30);
        help.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        getTable().row();

        final TextButton quit = new TextButton("Quit", getSkin());
        getTable().add(quit).size(360, 70).uniform();
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }


    public void dispose() {
        super.dispose();
    }

}
