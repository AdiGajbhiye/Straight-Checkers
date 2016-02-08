package com.pennywise;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.pennywise.checkers.core.Util;

/**
 * Created by Joshua.Nabongo on 10/22/2015.
 */
public class Assets {

    private final String FILE_IMAGE_ATLAS = "ui-pack.atlas";
    private TextureAtlas imageAtlas;
    private Skin skin;

    public static BitmapFont font;

    public static SpriteDrawable img_cell_dark;
    public static SpriteDrawable img_cell_light;
    public static SpriteDrawable img_pawn_white;
    public static SpriteDrawable img_king_white;
    public static SpriteDrawable img_pawn_black;
    public static SpriteDrawable img_king_black;
    public static SpriteDrawable img_undo;
    public static NinePatch img_background;
    public static NinePatch img_board_bg;
    public static SpriteDrawable img_selected_cell_dark;
    public static SpriteDrawable img_selected_cell_lite;
    public static SpriteDrawable img_dark_outline;
    public static SpriteDrawable img_btn_pause;

    public TextureAtlas getAtlas() {
        if (imageAtlas == null) {
            imageAtlas = new TextureAtlas(Gdx.files.internal(FILE_IMAGE_ATLAS));
        }
        return imageAtlas;
    }

    public void loadAll() {
        relaseResources();
        loadImages();
        loadButtons();
        loadFonts();
        loadAnimations();
        loadSoundsAndMusics();
    }

    private void relaseResources() {
        skin = null;
        imageAtlas = null;
    }

    private SpriteDrawable getDrawable(String name) {
        Sprite sprite = getAtlas().createSprite(name);
        sprite.flip(false, true);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

    public void loadImages() {
        img_background = getAtlas().createPatch("panelinset_beigelight");
        img_board_bg = getAtlas().createPatch("panel_brown");
        img_selected_cell_dark = getDrawable("selecteddarkcell");
        img_selected_cell_lite = getDrawable("selectedlitecell");
        img_cell_dark = getDrawable("dark-tile");
        img_cell_light = getDrawable("ligth-tile");
        img_pawn_black = getDrawable("blackpawn");
        img_pawn_white = getDrawable("whitepawn");
        img_king_black = getDrawable("blackking");
        img_king_white = getDrawable("whiteking");
        img_dark_outline = getDrawable("line_dark");
        img_undo = getDrawable("undo");

    }

    public void loadButtons() {
        img_btn_pause = getDrawable("pause_dark");
    }

    public void loadFonts() {
        font = Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.BLACK);
    }

    public void loadAnimations() {

    }

    public void loadSoundsAndMusics() {
    }
}
