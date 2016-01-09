package com.pennywise.checkers.screens.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.pennywise.Assets;
import com.pennywise.checkers.core.Constants;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public class NetDialog extends Dialog {

    public NetDialog(String title) {
        super(title, Assets.getSkin());
        initialize();
    }

    private void initialize() {
        padTop(60); // set padding on top of the dialog title
        setModal(true);
        setMovable(false);
        setResizable(false);

        getContentTable().defaults().expandX();
        //getContentTable().debugAll();
    }


    @Override
    public NetDialog text(String text) {
        super.text(new Label(text, Assets.getSkin()));
        return this;
    }

    /**
     * Adds a text button to the button table.
     *
     * @param listener the input listener that will be attached to the button.
     */
    public NetDialog button(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, Assets.getSkin());
        button.addListener(listener);
        button(button);
        return this;
    }

    /**
     * Adds a text button to the button table.
     *
     * @param listener the input listener that will be attached to the button.
     */
    public NetDialog content(String buttonText, InputListener listener) {

        TextButton button = new TextButton(buttonText, Assets.getSkin());
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
        return (Constants.GAME_HEIGHT * 0.50f);
    }
}
