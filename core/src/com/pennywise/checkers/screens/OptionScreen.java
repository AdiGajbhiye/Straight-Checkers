package com.pennywise.checkers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Assets;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;

/**
 * Created by CHOXXY on 10/21/2015.
 */
public class OptionScreen extends AbstractScreen {
    final float WIDTH = 250f;
    final float BUTTON_HEIGHT = 60f;
    Stage stage;
    Skin skin;
    private Label lblName;
    private TextField txtName;
    private CheckBox vsHuman;
    private Label vshuman;
    private CheckBox vsDroid;
    private Label vsdroid;
    private CheckBox playerBlack;
    private Label blackPiece;
    private CheckBox playerWhite;
    private Label redPiece;
    private Label lblGametype;
    private Label lblSide;

    public OptionScreen(
            Checkers game) {
        super(game);
    }

    @Override
    public void show() {

        Table layout = new Table();
        layout.setFillParent(true);
        layout.top();
        layout.defaults().space(6);
        //layout.debugAll();
        layout.pad(20f);

        skin = Assets.getSkin();

        lblName = new Label("Name", skin);
        lblGametype = new Label("Game", skin);
        lblSide = new Label("Piece Color", skin);
        txtName = new TextField("Player 1", skin);
        txtName.setWidth(WIDTH);
        txtName.setAlignment(Align.left | Align.center);
        vsHuman = new CheckBox(null, skin);
        vsDroid = new CheckBox(null, skin);
        playerBlack = new CheckBox(null, skin);
        playerWhite = new CheckBox(null, skin);

        vshuman = new Label("Human vs Human (LAN)", skin);
        vsdroid = new Label("Human vs  Droid", skin);
        blackPiece = new Label("Black Piece", skin);
        redPiece = new Label("Red Piece ", skin);

        layout.add(lblName).left().colspan(2).expandX();
        layout.row();
        layout.add(txtName).left().colspan(2).fill();
        layout.row();
        layout.row();
        layout.add(lblGametype).left().colspan(2);
        layout.row();
        layout.add(vshuman).left().height(40f);
        layout.add(vsHuman).right().height(40f);
        layout.row();
        layout.add(vsdroid).left().height(40f);
        layout.add(vsDroid).right().height(40f);
        layout.row();
        layout.row();
        layout.add(lblSide).left();
        layout.row();
        layout.add(blackPiece).left().height(40f);
        layout.add(playerBlack).right().height(40f);
        layout.row();
        layout.add(redPiece).left().height(40f);
        layout.add(playerWhite).right().height(40f);
        layout.row();

        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
        stage.addActor(layout);
        Gdx.input.setInputProcessor(stage);

       /* humanButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });*/

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        skin.dispose();
        stage.dispose();
    }
}