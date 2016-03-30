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
import com.pennywise.checkers.core.engine.Point;
import com.pennywise.checkers.core.engine.Simple;
import com.pennywise.checkers.core.persistence.GameObject;

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

    public void save(GameObject game) {

    }

    private static int coortonumber(Point point) {
        // given board coordinates x and y, this function returns the board number in
        // standard checkers notation
        int number = 0;
        number += 4 * (point.y + 1);
        number -= (point.x / 2);
        return number;
    }

    public static int coorstonumber(int x, int y) {
        // takes coordinates x and y, gametype, and returns the associated board number
        Point c = new Point();
        c.x = x;
        c.y = y;
        return (coortonumber(c));
    }

    public static Point numbertocoors(int number) {
        // given a board number this function returns the coordinates
        Point c = new Point();
        number--;
        c.y = number / 4;
        c.x = 2 * (3 - number % 4);
        if (c.y % 2 != 0)
            c.x++;

        return c;
    }

    public static Point coorstocoors(int x, int y, boolean invert, boolean mirror) {
        // given coordinates x and y on the screen, this function converts them to internal
        // representation of the board based on whether the board is inverted or mirrored
        Point c = new Point();

        if (invert) {
            c.x = 7 - x;
            c.y = 7 - y;
        }
        if (mirror)
            c.x = 7 - x;

        return c;
    }

    public static Point getIndex(float x, float y, float cellsize) {
        Point point = new Point();

        point.x = ((int) (x / cellsize));
        point.y = ((int) (y / cellsize));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (((i * cellsize) < x)
                        && (((i * cellsize) + cellsize) > x)
                        && ((j * cellsize) < y)
                        && (((j * cellsize) + cellsize) > y)) {
                    point.x = i;
                    point.y = j;
                    return point;
                }
            }
        }
        return point;
    }

    public static int[][] bitboardtoboard8(int[] board) {

        int[][] b = new int[8][8];

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                b[i][j] = Simple.FREE;
            }
        }

        for (int i = 5; i <= 40; i++)
            if (board[i] == Simple.FREE)
                board[i] = 0;

        b[0][0] = board[5];
        b[2][0] = board[6];
        b[4][0] = board[7];
        b[6][0] = board[8];
        b[1][1] = board[10];
        b[3][1] = board[11];
        b[5][1] = board[12];
        b[7][1] = board[13];
        b[0][2] = board[14];
        b[2][2] = board[15];
        b[4][2] = board[16];
        b[6][2] = board[17];
        b[1][3] = board[19];
        b[3][3] = board[20];
        b[5][3] = board[21];
        b[7][3] = board[22];
        b[0][4] = board[23];
        b[2][4] = board[24];
        b[4][4] = board[25];
        b[6][4] = board[26];
        b[1][5] = board[28];
        b[3][5] = board[29];
        b[5][5] = board[30];
        b[7][5] = board[31];
        b[0][6] = board[32];
        b[2][6] = board[33];
        b[4][6] = board[34];
        b[6][6] = board[35];
        b[1][7] = board[37];
        b[3][7] = board[38];
        b[5][7] = board[39];
        b[7][7] = board[40];

        return b;
    }
}
