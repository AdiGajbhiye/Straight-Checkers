package com.pennywise.checkers.screens.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pennywise.checkers.core.Constants;

/**
 * Created by CHOXXY on 11/26/2015.
 */
public class GameDialog extends Dialog {

    Skin skin;
    public GameDialog(String title,Skin skin) {
        super(title,skin);
        this.skin = skin;
        initialize();
    }

    private void initialize() {
        padTop(30); // set padding on top of the dialog title
        padLeft(30);
        padRight(30);
        padBottom(30);

        setModal(true);
        setMovable(false);
        setResizable(false);
        getContentTable().defaults().expandX().pad(5);
    }


    @Override
    public GameDialog text(String text) {
        super.text(new Label(text, skin));
        return this;
    }

    /**
     * Adds a text button to the button table.
     *
     * @param listener the input listener that will be attached to the button.
     */
    public GameDialog button(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, skin);
        button.addListener(listener);
        button(button);
        return this;
    }

    /**
     * Adds a text button to the button table.
     *
     * @param listener the input listener that will be attached to the button.
     */
    public GameDialog content(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, skin);
        button.addListener(listener);
        getContentTable().add(button).height(60).fill().left().top().center();
        getContentTable().row();
        return this;
    }



    @Override
    public float getPrefWidth() {
        // force dialog width
        return (Constants.GAME_WIDTH * 0.85f);
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return (Constants.GAME_HEIGHT * 0.55f);
    }


}
