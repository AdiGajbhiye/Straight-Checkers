package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.managers.AudioManager;

public class MultiplayerScreen extends AbstractScreen {

    public static final String LOG = MultiplayerScreen.class.getSimpleName();

    private Label infoLabel;

    public MultiplayerScreen(Checkers game) {
        super(game);
    }

    @Override
    public void show() {

        getTable().setBackground("dialog");
        getTable().pad(10);

        Label welcomeLabel = new Label("Bluetooth: Two Players", getSkin());
        welcomeLabel.setAlignment(Align.center);
        Table gameTitle = new Table(getSkin());
        gameTitle.setBackground("dialog");
        gameTitle.add(welcomeLabel).center().size(Constants.GAME_WIDTH * 0.90f, 80);
        getTable().top().add(gameTitle).fillX();
        getTable().row();

        Table menuContainer = new Table(getSkin());
        menuContainer.setBackground("dialog");
        getTable().add(menuContainer).fill();

        if (Checkers.BLUETOOTH_INTERFACE_EXISTS) {

            final TextButton game1Button = new TextButton("Host", getSkin());
            menuContainer.add(game1Button).size(360, 70).padBottom(25).padTop(100);
            game1Button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    AudioManager.playClick();
                    game.setScreen(new HostScreen(game));
                }
            });

            menuContainer.row();

            final TextButton game2Button = new TextButton("Join", getSkin());
            menuContainer.add(game2Button).size(360, 70).padTop(25).padBottom(25);
            game2Button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    AudioManager.playClick();
                    game.setScreen(new JoinScreen(game));
                }
            });

            menuContainer.row();
        } else {
            infoLabel = new Label(
                    "Can't play multiplayer.\nBluetooth not supported.",
                    getSkin());
            infoLabel.setAlignment(Align.center);
            menuContainer.center().add(infoLabel).padBottom(25).padTop(100);
            menuContainer.row();
        }

        final TextButton back = new TextButton("Back", getSkin());
        menuContainer.add(back).size(360, 70).padBottom(25).padTop(25);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.playClick();
                game.setScreen(new MenuScreen(game));
            }
        });

        menuContainer.row();
        menuContainer.add().prefHeight(200);


    }

    public void dispose() {
        super.dispose();
        Gdx.app.log(LOG, "Disposing MultiplayerScreen");
    }

    @Override
    public void keyBackPressed() {
        super.keyBackPressed();

        game.setScreen(new MenuScreen(game));
    }

}
