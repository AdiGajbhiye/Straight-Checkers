package com.pennywise.checkers.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by CHOXXY on 10/4/2015.
 */
public class GameDialog extends Dialog {


    public static GameDialog instance(String title, NinePatch panePatch, BitmapFont font) {
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.background = new NinePatchDrawable(panePatch);
        GameDialog dialog = new GameDialog(title, windowStyle);
        return dialog;
    }

    public GameDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        initialize();
    }

    private void initialize() {
        padTop(60); // set padding on top of the dialog title
        getButtonTable().defaults().height(60); // set buttons height
        setModal(true);
        setMovable(false);
        setResizable(false);
    }

    @Override
    public GameDialog text(String text) {
        super.text(new Label(text, Assets.skin, "medium-green"));
        return this;
    }

    /**
     * Adds a text button to the button table.
     *
     * @param listener the input listener that will be attached to the button.
     */
    public GameDialog button(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, Assets.skin);
        button.addListener(listener);
        button(button);
        return this;
    }

    @Override
    public float getPrefWidth() {
        // force dialog width
        return 480f;
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return 240f;
    }
}
