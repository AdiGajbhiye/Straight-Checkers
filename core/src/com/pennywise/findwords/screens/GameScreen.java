package com.pennywise.findwords.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.FindWords;
import com.pennywise.findwords.core.Constants;
import com.pennywise.findwords.core.GameCam;
import com.pennywise.findwords.objects.Tile;

/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen {


    private final Stage stage;
    private final BitmapFont font;
    private float runTime = 0;

    public GameScreen(FindWords game) {
        super(game);
        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, GameCam.instance));
        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
        font = loadFont("fonts/Roboto-Bold.ttf");
        buildBoard();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        runTime += delta;
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }

    //fonts/Roboto-Regular.ttf
    public BitmapFont loadFont(String path) {
        // how much bigger is the real device screen, compared to the defined viewport
        float SCALE = 1.0f * Gdx.graphics.getWidth() / Constants.GAME_WIDTH;
        // prevents unwanted downscale on devices with resolution SMALLER than 320x480
        if (SCALE < 1)
            SCALE = 1;
        //set the font parameters
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (12 * SCALE);
        parameter.flip = true;
        parameter.color = Color.WHITE;
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


    private void buildBoard() {
        // build all layers

        Table layerGrid = buildGrid();

        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        stack.add(layerGrid);
    }

    private Table buildGrid() {
        Table layer = new Table();
        layer.addActor(grid(12, 12));
        return layer;
    }


    private Group grid(int rows, int cols) {

        Group grid = new Group();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.background = tileTexture();

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        float cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        int margin = 1;

        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + margin,
                        ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.80f)));
                tiles[index] = new Tile("A", new Label.LabelStyle(style));
                tiles[index].setSize(cellsize, cellsize);
                tiles[index].setPosition(position[index].x, position[index].y);
                grid.addActor(tiles[index]);
            }
        }

        return grid;
    }

    public SpriteDrawable tileTexture() {
        final Texture t = new Texture(Gdx.files.internal("background/cell.png"));
        Sprite sprite = new Sprite(t);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }
}
