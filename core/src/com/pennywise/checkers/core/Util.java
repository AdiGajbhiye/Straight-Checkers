package com.pennywise.checkers.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Joshua.Nabongo on 9/18/2015.
 */
public class Util {

    private static final String logTag = Util.class.getSimpleName();

    public static SpriteDrawable loadTexture(String name) {
        final Texture t = new Texture(Gdx.files.internal(name));
        Sprite sprite = new Sprite(t);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

    //fonts/Roboto-Regular.ttf
    public static BitmapFont loadFont(String path, int fontSize, Color color) {
        // how much bigger is the real device screen, compared to the defined viewport
        float SCALE = 1.0f * Gdx.graphics.getWidth() / Constants.GAME_WIDTH;
        // prevents unwanted downscale on devices with resolution SMALLER than 320x480
        if (SCALE < 1)
            SCALE = 1;
        //set the font parameters
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (fontSize * SCALE);
        parameter.flip = false;
        parameter.color = color;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        // 12 is the size i want to give for the font on all devices
        // bigger font textures = better results
        BitmapFont font = generator.generateFont(parameter);
        // aplly the inverse scale of what Libgdx will do at runtime
        font.getData().setScale((float) (1.0 / SCALE));
        // the resulting font scale is: 1.0 / SCALE * SCALE = 1
        //Apply Linear filtering; best choice to keep everything looking sharp
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    /**
     * Get the rectangle of an actor from its current position and size
     * */
    public static Rectangle getRectangleOfActor(Actor actor) {
        return new Rectangle(actor.getX(), actor.getY(), actor.getWidth(),
                actor.getHeight());
    }

    /**
     * Check collision from actor's rectangles
     * */
    public static boolean isActorsCollide(Actor actor1, Actor actor2) {
        if (Intersector.overlaps(getRectangleOfActor(actor1),
                getRectangleOfActor(actor2))) {
            logCollision1(actor1, actor2);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Very precise point detection in a box, think as virtual box in the actual
     * box with padding as precision amount
     * */
    public static boolean isTouchPointCollide(float touchX, float touchY,
                                              float posX, float posY, float width, float height,
                                              float precisionAmount) {
        if (touchX > (posX + precisionAmount)
                && touchX < (posX + width - precisionAmount)
                && touchY > (posY + precisionAmount)
                && touchY < (posY + height - precisionAmount)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Very precise point detection in an actor, think as virtual box in the
     * actual box with margin as precision amount
     * */
    public static boolean isTouchPointCollide(float touchX, float touchY,
                                              Actor actor, float precisionAmount) {
        if (touchX > (actor.getX() + precisionAmount)
                && touchX < (actor.getX() + actor.getWidth() - precisionAmount)
                && touchY > (actor.getY() + precisionAmount)
                && touchY < (actor.getY() + actor.getHeight() - precisionAmount)) {
            logCollision2(actor);
            return true;
        } else {
            return false;
        }
    }

    private static void logCollision1(Actor a1, Actor a2) {
        Gdx.app.log(logTag,
                "Collision detected: Actor (Name: " + a1.getName()
                        + ") and Actor (Name: " + a2.getName() + ")");
    }

    private static void logCollision2(Actor a1) {
        Gdx.app.log(
                logTag,
                "Collision detected on touch point: Actor (Name: "
                        + a1.getName() + ")");
    }


}
