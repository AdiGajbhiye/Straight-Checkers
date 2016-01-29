package com.pennywise.checkers.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.pennywise.checkers.core.engine.Coord;

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
        parameter.flip = true;
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
     */
    public static Rectangle getRectangleOfActor(Actor actor) {
        return new Rectangle(actor.getX(), actor.getY(), actor.getWidth(),
                actor.getHeight());
    }

    /**
     * Check collision from actor's rectangles
     */
    public static boolean isActorsCollide(Actor actor1, Actor actor2) {
        if (Intersector.overlaps(getRectangleOfActor(actor1),
                getRectangleOfActor(actor2))) {
            logCollision1(actor1, actor2);
            return true;
        } else {
            return false;
        }
    }


    public static boolean isActorCollide(Actor subject, Actor test) {
        Rectangle rect1 = getRectangleOfActor(subject);
        Rectangle rect2 = getRectangleOfActor(test);
        if (rect1.overlaps(rect2)) {
            logCollision(subject, test);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Very precise point detection in a box, think as virtual box in the actual
     * box with padding as precision amount
     */
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
     */
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

    private static void logCollision(Actor a1, Actor a2) {
        Gdx.app.log(logTag,
                "Collision detected: Actor (Name: " + a1.getName()
                        + ") overlaps Actor (Name: " + a2.getName() + ")");
    }

    public static int coordtonumber(Coord coord) {
        // given board coordinates col and row, this function returns the board number in
        // standard checkers notation
        int number;

        number = 0;
        number += 4 * (coord.row + 1);
        number -= (coord.col / 2);

        return number;
    }

    public static int coordstonumber(int col, int row) {
        // takes coordinates col and row, gametype, and returns the associated board number
        Coord c = new Coord();
        c.col = col;
        c.row = row;
        return (coordtonumber(c));
    }

    public static Coord toCoord(int n) {

        Coord c = new Coord();

        switch (n) {
            case 1:
                c.row = 0;
                c.col = 1;
                break;
            case 2:
                c.col = 3;
                c.row = 0;
                break;
            case 3:
                c.col = 5;
                c.row = 0;
                break;
            case 4:
                c.col = 7;
                c.row = 0;
                break;
            case 5:
                c.col = 0;
                c.row = 1;
                break;
            case 6:
                c.col = 2;
                c.row = 1;
                break;
            case 7:
                c.col = 4;
                c.row = 1;
                break;
            case 8:
                c.col = 6;
                c.row = 1;
                break;
            case 9:
                c.col = 1;
                c.row = 2;
                break;
            case 10:
                c.col = 3;
                c.row = 2;
                break;
            case 11:
                c.col = 5;
                c.row = 2;
                break;
            case 12:
                c.col = 7;
                c.row = 2;
                break;
            case 13:
                c.col = 0;
                c.row = 3;
                break;
            case 14:
                c.col = 2;
                c.row = 3;
                break;
            case 15:
                c.col = 4;
                c.row = 3;
                break;
            case 16:
                c.col = 6;
                c.row = 3;
                break;
            case 17:
                c.col = 1;
                c.row = 4;
                break;
            case 18:
                c.col = 3;
                c.row = 4;
                break;
            case 19:
                c.col = 5;
                c.row = 4;
                break;
            case 20:
                c.col = 7;
                c.row = 4;
                break;
            case 21:
                c.col = 0;
                c.row = 5;
                break;
            case 22:
                c.col = 2;
                c.row = 5;
                break;
            case 23:
                c.col = 4;
                c.row = 5;
                break;
            case 24:
                c.col = 6;
                c.row = 5;
                break;
            case 25:
                c.col = 1;
                c.row = 6;
                break;
            case 26:
                c.col = 3;
                c.row = 6;
                break;
            case 27:
                c.col = 5;
                c.row = 6;
                break;
            case 28:
                c.col = 7;
                c.row = 6;
                break;
            case 29:
                c.col = 0;
                c.row = 7;
                break;
            case 30:
                c.col = 2;
                c.row = 7;
                break;
            case 31:
                c.col = 4;
                c.row = 7;
                break;
            case 32:
                c.col = 6;
                c.row = 7;
                break;
        }

        return c;
    }

    // return board number for coordinates
    public static int toNumber(int row, int col) {
        // board coordinates are [row][col]!
        // ENGLISH
        int[][] en = new int[][]{
                {0, 1, 0, 2, 0, 3, 0, 4},
                {5, 0, 6, 0, 7, 0, 8, 0},
                {0, 9, 0, 10, 0, 11, 0, 12},
                {13, 0, 14, 0, 15, 0, 16, 0},
                {0, 17, 0, 18, 0, 19, 0, 20},
                {21, 0, 22, 0, 23, 0, 24, 0},
                {0, 25, 0, 26, 0, 27, 0, 28},
                {29, 0, 30, 0, 31, 0, 32, 0},
        };


        return en[col][row];

    }

    private SpriteDrawable getTexture(Skin skin, String id) {
        Sprite sprite = skin.getSprite(id);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

    private NinePatch getPatch(Skin skin, String id) {
        NinePatch patch = skin.getPatch(id);
        return patch;
    }

    public static Pixmap getPixmapRoundedRectangle(int width, int height, int radius, int color) {

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        // Pink rectangle
        pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight() - 2 * radius);

// Green rectangle
        pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2 * radius, pixmap.getHeight());


// Bottom-left circle
        pixmap.fillCircle(radius, radius, radius);

// Top-left circle
        pixmap.fillCircle(radius, pixmap.getHeight() - radius, radius);

// Bottom-right circle
        pixmap.fillCircle(pixmap.getWidth() - radius, radius, radius);

// Top-right circle
        pixmap.fillCircle(pixmap.getWidth() - radius, pixmap.getHeight() - radius, radius);
        return pixmap;
    }

}
