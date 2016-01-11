package com.pennywise.checkers.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pennywise.Assets;
import com.pennywise.Checkers;

/**
 * Created by CHOXXY on 1/10/2016.
 */
public class LevelScreen extends AbstractScreen {

    Table contentTable;
    TextButton easy;
    TextButton continew;
    TextButton normal;
    TextButton hard;
    TextButton multiplayer;
    Label title;
    private boolean pendingGame = false;

    public LevelScreen(Checkers game) {
        super(game);
    }

    @Override
    public void show() {
        contentTable = new Table();
        contentTable.setFillParent(true);

        contentTable.padTop(10); // set padding on top of the dialog title
        contentTable.padLeft(60);
        contentTable.padRight(60);
        contentTable.defaults().height(60);


        continew = new TextButton("Continue", Assets.getSkin());
        easy = new TextButton("Easy", Assets.getSkin());
        normal = new TextButton("Normal", Assets.getSkin());
        hard = new TextButton("Hard", Assets.getSkin());
        title = new Label("New Game", Assets.getSkin());
        multiplayer = new TextButton("2 Player", Assets.getSkin());

        contentTable.add(title).expandX().fillX().center();
        contentTable.row();

        if(pendingGame){
            contentTable.add(continew).expandX().fillX().center();
            contentTable.row();
        }

        contentTable.add(easy).expandX().fillX().center();
        contentTable.row();

        contentTable.add(normal).expandX().fillX().center();
        contentTable.row();

        contentTable.add(hard).expandX().fillX().center();
        contentTable.row();

        contentTable.add(multiplayer).expandX().fillX().center();
        contentTable.row();


    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
