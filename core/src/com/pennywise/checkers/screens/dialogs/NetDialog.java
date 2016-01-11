package com.pennywise.checkers.screens.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pennywise.Assets;
import com.pennywise.checkers.core.Constants;

import sun.nio.ch.Net;

/**
 * Created by Joshua.Nabongo on 1/7/2016.
 */
public class NetDialog extends Dialog {

    public NetDialog(String title) {
        super(title, Assets.getSkin());
        initialize();
    }

    private void initialize() {
        getButtonTable().defaults().height(60);
        setModal(true);
        setMovable(false);
        setResizable(false);
        padTop(40); // set padding on top of the dialog title
        padLeft(40);
        padRight(40);
        padBottom(20);
    }


    @Override
    public NetDialog text(String text) {
        super.text(new Label(text, Assets.getSkin()));
        return this;
    }

    public NetDialog title(String text){
        Label label = new Label(text,Assets.getSkin());
        label.setWrap(true);
        label.setFontScale(.8f);
        label.setAlignment(Align.center);
        getContentTable().add(label).expandX().fillX().top();
        getContentTable().row();
        getButtonTable().padTop(10);
        return  this;

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
        getContentTable().add(button).height(60).bottom();
        getContentTable().row();
        return this;
    }

    public NetDialog selectBox(String buttonText, ChangeListener listener) {
        SelectBox selectBox = new SelectBox(Assets.getSkin());
        selectBox.addListener(listener);
        selectBox.setItems("Host Game", "Connect to host");
        getContentTable().add(selectBox).height(50).expandX().fillX().center();
        getContentTable().row();
        return this;
    }

    public NetDialog list(String buttonText, ClickListener listener) {

        Object[] listEntries = {"This is a list entry1", "And another one1", "The meaning of life1"};

        List list = new List(Assets.getSkin());
        list.setItems(listEntries);
        list.getSelection().setMultiple(false);
        list.getSelection().setRequired(false);
        list.addListener(listener);
        getContentTable().add(list).expand().fill().top();
        getContentTable().row();
        return this;
    }

    @Override
    public float getPrefWidth() {
        // force dialog width
        return (Constants.GAME_WIDTH * 0.90f);
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return (Constants.GAME_HEIGHT * 0.60f);
    }
}
