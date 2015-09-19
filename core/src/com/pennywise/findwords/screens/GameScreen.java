package com.pennywise.findwords.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.FindWords;
import com.pennywise.findwords.core.Constants;
import com.pennywise.findwords.core.GameCam;
import com.pennywise.findwords.core.Util;
import com.pennywise.findwords.core.logic.Grid;
import com.pennywise.findwords.core.logic.PuzzleGenerator;
import com.pennywise.findwords.objects.Tile;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen {


    private final Stage stage;
    private final GameCam camera;
    private ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    private final BitmapFont font;
    private Vector2[] dragTracker;
    private float cellsize = 0;
    protected Tile tile;
    protected String foundWord;
    protected StringBuilder sb;
    private Random random = new Random();
    private List<String> words;
    private int color = 0;

    public GameScreen(FindWords game) {
        super(game);
        camera = GameCam.instance;
        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
        font = Util.loadFont("fonts/Roboto-Regular.ttf", Color.BLACK);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        dragTracker = new Vector2[2];
        dragTracker[0] = new Vector2();
        sb = new StringBuilder();
        words = new LinkedList<String>();
        color = random.nextInt(7);
        setupPuzzle();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();

        // shapeRenderer.begin();
        if (dragTracker[1] != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            drawLineForSelected(shapeRenderer);
            //Gdx.gl.glDisable(GL20.GL_BLEND);
            // shapeRenderer.end();
        }

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


    private void setupPuzzle() {
        // build all layers
        Table layerPuzzle = buildBoard();
        Table layerWordlist = buildWordList();
        stage.clear();

        Stack stack = new Stack();
        stack.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        stack.add(layerWordlist);
        stack.add(layerPuzzle);
        stage.addActor(stack);
    }

    private Table buildBoard() {
        Table layer = new Table();
        layer.addActor(board(10, 8));
        return layer;
    }

    private Table buildWordList() {
        Table layer = new Table();
        layer.addActor(wordList());
        return layer;
    }

    private Group board(int rows, int cols) {

        Group board = new Group();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.background = tileTexture("background/brown_tile.png");

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + padding,
                        ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.10f)));
                tiles[index] = new Tile("A", new Label.LabelStyle(style));
                tiles[index].setSize(cellsize, cellsize);
                tiles[index].setAlignment(Align.center);
                tiles[index].setPosition(position[index].x, position[index].y);
                tiles[index].addListener(tileListener);
                tiles[index].setName(index + "");
                board.addActor(tiles[index]);
            }
        }

        //fill grid with letters
        words.add("everton");
        words.add("watford");
        words.add("liverpool");
        words.add("swansea");
        words.add("astonvilla");
        words.add("manchester");
        words.add("arsenal");
        words.add("chelsea");
        words.add("fulham");
        words.add("newcastle");

        PuzzleGenerator pg = new PuzzleGenerator(rows, cols, words);
        Grid grid = pg.generate();


        words.add("everton");
        words.add("watford");
        words.add("liverpool");
        words.add("swansea");
        words.add("astonvilla");
        words.add("manchester");
        words.add("arsenal");
        words.add("chelsea");
        words.add("fulham");
        words.add("newcastle");

        for (int i = 0; i < grid.width(); i++) {
            for (int j = 0; j < grid.height(); j++) {
                index = i + (j * cols);
                String val = String.valueOf(grid.at(i, j));
                tiles[index].setText(val);
            }
        }

        return board;
    }

    private Group wordList() {
        Group wordList = new Group();
        int count = words.size();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.background = tileTexture("background/clear_cell.png");


        int rows = 4;
        int cols = (count / rows) + 1;

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        float width = ((Constants.GAME_WIDTH - cols) / (cols));
        float height = font.getData().capHeight + 10;
        float padding = (Constants.GAME_WIDTH - (width * cols)) / 2;
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                if (index >= count)
                    break;
                position[index] = new Vector2((col * width) + padding,
                        ((row * (height)) + (Constants.GAME_HEIGHT * 0.80f)));
                tiles[index] = new Tile(words.get(index), new Label.LabelStyle(style));
                tiles[index].setSize(width, height);
                tiles[index].setAlignment(Align.center);
                tiles[index].setPosition(position[index].x, position[index].y);
                tiles[index].setName(index + "");
                wordList.addActor(tiles[index]);
            }
        }

        return wordList;
    }

    public SpriteDrawable tileTexture(String name) {
        final Texture t = new Texture(Gdx.files.internal(name));
        Sprite sprite = new Sprite(t);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

    private void drawLineForSelected(ShapeRenderer sr) {


        int startX = (int) ((dragTracker[0].x + cellsize) / 2);
        int startY = (int) ((dragTracker[0].y + cellsize) / 2);
        int endX = (int) dragTracker[1].x;
        int endY = (int) dragTracker[1].y;
        int size = (int) (cellsize - 2);


        sr.setColor(new Color().set(255.0f, 108.0f, 0.0f, 0.5f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        sr.rectLine(startX, startY, endX, endY, size);
        sr.arc(startX, startY, (size / 2), 0, 180);
        sr.arc(endX, endY, (size / 2), 0, 180);
        shapeRenderer.end();
    }

    protected void updateColor() {

        if (words.contains(sb.toString().toLowerCase())) {
            sb.setLength(0);
            color = random.nextInt(7);
        }

    }

    private InputListener tileListener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Tile b = (Tile) event.getListenerActor();

            if (tile != null) {
                if (tile.getName().equals(b.getName())) {
                    sb.deleteCharAt(sb.length() - 1);
                    b.getStyle().background = Util.loadTexture("background/brown_tile.png");
                    tile = null;
                    return true;
                }
            }

            tile = b;

            String v = b.getText().toString();
            sb.append(v);

            if (color == 1)
                b.getStyle().background = Util.loadTexture("background/beige_tile_yellow_glow.png");
            else if (color == 2)
                b.getStyle().background = Util.loadTexture("background/beige_tile_green_glow.png");
            else if (color == 3)
                b.getStyle().background = Util.loadTexture("background/beige_tile_red_glow.png");
            else if (color == 4)
                b.getStyle().background = Util.loadTexture("background/beige_tile_orange_glow.png");
            else if (color == 5)
                b.getStyle().background = Util.loadTexture("background/beige_tile_pink_glow.png");
            else if (color == 6)
                b.getStyle().background = Util.loadTexture("background/beige_tile_purple_glow.png");
            else
                b.getStyle().background = Util.loadTexture("background/beige_tile_blue_glow.png");

            updateColor();

            return true; //or false
        }
    };
}
