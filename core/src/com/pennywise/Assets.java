package com.pennywise;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.pennywise.checkers.core.Util;

/**
 * Created by Joshua.Nabongo on 10/22/2015.
 */
public class Assets {

    private final static String FILE_IMAGE_ATLAS = "images/ui-pack.atlas";
    private final static String FILE_UI_SKIN = "images/ui-pack.json";
    public static TextureAtlas imageAtlas;
    public static Skin skin;

    //
    public static BitmapFont font;

    //
    public static SpriteDrawable img_board_bg_1;
    public static SpriteDrawable img_board_bg_2;
    public static SpriteDrawable img_bg_1;
    public static SpriteDrawable img_bg_2;
    public static SpriteDrawable img_obj_btn_play;
    public static SpriteDrawable img_obj_btn_scores;
    public static SpriteDrawable img_obj_btn_settings;
    public static SpriteDrawable img_obj_circle;
    public static SpriteDrawable img_cell_dark;
    public static SpriteDrawable img_cell_light;
    public static SpriteDrawable img_pawn_white;
    public static SpriteDrawable img_king_white;
    public static SpriteDrawable img_pawn_black;
    public static SpriteDrawable img_king_black;
    public static SpriteDrawable img_obj_social_google;
    public static TextureRegion img_obj_social_twitter;
    public static TextureRegion img_obj_sound_off;
    public static TextureRegion img_obj_sound_on;
    public static TextureRegion img_obj_swipe_down_menu;
    public static TextureRegion img_obj_swipe_up_instructions;
    public static TextureRegion img_obj_text_junglegamemenu;
    private static SpriteDrawable img_selected_cell_dark;
    private static SpriteDrawable img_selected_cell_lite;

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static TextureAtlas getAtlas() {
        if (imageAtlas == null) {
            imageAtlas = new TextureAtlas(Gdx.files.internal(FILE_IMAGE_ATLAS));
        }
        return imageAtlas;
    }

    public static Skin getSkin() {
        if (skin == null) {
            FileHandle skinFile = Gdx.files.internal(FILE_UI_SKIN);
            skin = new Skin(skinFile);
        }
        return skin;
    }

    public static void loadAll() {
        relaseResources();
        loadImages();
        loadButtons();
        loadFonts();
        loadAnimations();
        loadSoundsAndMusics();
    }

    private static void relaseResources() {
        skin = null;
        imageAtlas = null;
    }

    public static SpriteDrawable getDrawable(String name) {
        Sprite sprite = getAtlas().createSprite(name);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

    public static void loadImages() {
        img_bg_1 = getDrawable("img_bg_1_");
        img_obj_btn_play = getDrawable("img_obj_btn_play");
        img_obj_btn_scores = getDrawable("img_obj_btn_scores");
        img_obj_btn_settings = getDrawable("img_obj_btn_settings");
        img_obj_circle = getDrawable("img_obj_circle");
        img_board_bg_1 = ;
        img_board_bg_2 = ;
        img_selected_cell_dark = getDrawable("selecteddarkcell");
        img_selected_cell_lite = getDrawable("selectedlitecell");
        img_cell_dark=getDrawable("darkcell");
        img_cell_light= getDrawable("litecell");
        img_pawn_black= getDrawable("blackpawn");
        img_pawn_white= getDrawable("whitepawn");
        img_king_black= getDrawable("blackking");
        img_king_white= getDrawable("whiteking");
    }

    public static void loadButtons() {

    }

    public static void loadFonts() {
        //load fonts
        font = Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.BLACK);
    }

    public static void loadAnimations() {

    }

    public static void loadSoundsAndMusics() {
    }
}
