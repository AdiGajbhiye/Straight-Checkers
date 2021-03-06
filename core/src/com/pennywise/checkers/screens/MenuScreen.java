package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.managers.AudioManager;

public class MenuScreen extends AbstractScreen {

    public static final String LOG = MenuScreen.class.getSimpleName();
    private Label welcomeLabel;

    public MenuScreen(Checkers game) {
        super(game);
    }

    @Override
    public void show() {

        getTable().setBackground("dialog");
        getTable().pad(10);

        welcomeLabel = new Label("Straight Checkers", getSkin());
        welcomeLabel.setAlignment(Align.center);
        Table gameTitle = new Table(getSkin());
        gameTitle.setBackground("dialog");
        gameTitle.add(welcomeLabel).center().size(Constants.GAME_WIDTH * 0.90f,80);
        getTable().top().add(gameTitle).fillX();
        getTable().row();

        Table menuContainer = new Table(getSkin());
        menuContainer.setBackground("dialog");
        getTable().top().add(menuContainer).fill();

        final TextButton game1Button = new TextButton("Singleplayer", getSkin());
        menuContainer.add(game1Button).size(360, 70).padBottom(25).padTop(100);
        game1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                game.setScreen(new LevelScreen(game));
            }
        });

        menuContainer.row();

        final TextButton game2Button = new TextButton("Multiplayer", getSkin());
        menuContainer.add(game2Button).size(360, 70).padTop(25).padBottom(25);
        game2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                game.setScreen(new MultiplayerScreen(game));
            }
        });

        menuContainer.row();

        final TextButton quit = new TextButton("Quit", getSkin());
        menuContainer.add(quit).size(360, 70).padBottom(25).padTop(25);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        menuContainer.row();
        menuContainer.add().prefHeight(200);
    }


    public void dispose() {
        super.dispose();
    }

    @Override
    public void keyBackPressed() {
        super.keyBackPressed();
        Gdx.app.exit();
    }

}
