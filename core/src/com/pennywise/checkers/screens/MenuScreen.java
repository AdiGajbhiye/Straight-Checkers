package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
        welcomeLabel = new Label("== Welcome to Checkers ==", getSkin());
        getTable().add(welcomeLabel).spaceBottom(20);
        getTable().row();

        final TextButton game1Button = new TextButton("Singleplayer", getSkin());
        getTable().add(game1Button).size(320, 60).uniform().spaceBottom(10);
        game1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        getTable().row();

        final TextButton game2Button = new TextButton("Multiplayer", getSkin());
        getTable().add(game2Button).size(320, 60).uniform().spaceBottom(10);
        game2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MultiplayerScreen(game));
            }
        });

        getTable().row();

        final TextButton game3Button = new TextButton("Help", getSkin());
        getTable().add(game3Button).size(320, 60).uniform();
        /*game3Button.addListener(new ChangeListener() {
            @Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new HelpScreen(game));
			}
		});*/
    }


    public void dispose() {
        super.dispose();
    }

}
