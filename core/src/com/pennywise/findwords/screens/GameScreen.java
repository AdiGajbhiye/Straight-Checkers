package com.pennywise.findwords.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen {


    private final Stage stage;
    private final GameCam camera;
    private final BitmapFont uiFont;
    private final BitmapFont strikeThrough;
    SpriteBatch batch;
    private final BitmapFont gridFont;
    private final BitmapFont listFont;
    private float cellsize = 0;
    private float gridHeight = 0;
    protected Tile tile;
    protected String foundWord;
    protected StringBuilder sb;
    private Random random = new Random();
    private List<String> words;
    private int color = 0;
    private TextureAtlas gameUI;
    private Image time;
    private Button pause;
    private boolean isBusy = false;
    private int longestWordLen;
    private int width, height;

    public GameScreen(FindWords game) {
        super(game);
        camera = GameCam.instance;
        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(new InputMultiplexer(stage));

        //load fonts
        gridFont = Util.loadFont("fonts/Roboto-Regular.ttf", 16, Color.WHITE);
        listFont = Util.loadFont("fonts/Roboto-Bold.ttf", 24, Color.BLUE);
        uiFont = Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.BLUE);
        uiFont.getData().setScale(1, -1);
        strikeThrough = Util.loadFont("fonts/BPtypewriteStrikethrough.ttf", 24, Color.BLUE);

        width = 11;
        height = 13;
        //2/3 of the height
        gridHeight = ((Constants.GAME_HEIGHT * 3) / 4);

        batch = new SpriteBatch();
        sb = new StringBuilder();
        words = new LinkedList<String>();
        color = random.nextInt(8) + 1;
        gameUI = new TextureAtlas("images/ui-pack.atlas");
        setupScreen();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.59f, 0.44f, 0.29f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();

        stage.act();
        stage.draw();

        batch.begin();
        renderScore(batch, delta);
        batch.end();

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


    private void setupScreen() {
        // build all layers
        Table layerPuzzle = buildBoard();
        Table layerWordlist = buildWordList();
        stage.clear();

        Stack stack = new Stack();
        stack.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        stack.add(backGround());
        stack.add(hud());
        stack.add(layerPuzzle);
        stack.add(layerWordlist);
        stage.addActor(stack);
    }

    private Table backGround() {
        Table layer = new Table();
        Image bg = new Image(gameUI.createPatch("panelInset_beigeLight"));
        layer.add(bg).height(Constants.GAME_HEIGHT).width(Constants.GAME_WIDTH).expandX().expandY();
        return layer;
    }

    private Table hud() {
        Table layer = new Table();
        layer.bottom();
        time = new Image(gameUI.createPatch("pause"));
        layer.add(time).height(40).width(40).bottom().right().expandX().padBottom(10);
        return layer;
    }

    private Table buildBoard() {
        Table layer = new Table();
        layer.addActor(board(height, width));
        return layer;
    }

    private Table buildWordList() {
        Table layer = new Table();
        layer.addActor(wordList());
        return layer;
    }

    private Group board(int rows, int cols) {

        Group board = new Group();

        //fill grid with letters
        words.add("everton");
        words.add("watford");
        words.add("swansea");
        words.add("leeds");
        words.add("stoke");
        words.add("arsenal");
        words.add("chelsea");
        words.add("fulham");
        words.add("city");

        Collections.sort(words, new LengthComparator());
        longestWordLen = words.get(0).length();

        PuzzleGenerator pg = new PuzzleGenerator(rows, cols, words);
        Grid grid = pg.generate();

        words.add("everton");
        words.add("watford");
        words.add("swansea");
        words.add("leeds");
        words.add("stoke");
        words.add("arsenal");
        words.add("chelsea");
        words.add("fulham");
        words.add("city");

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = gridFont;
        style.background = tileTexture("brown_tile");

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        //float cellHeight = ((gridHeight - rows) / rows);
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + padding,
                        padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.10f)));

                String val = String.valueOf(grid.at(col, row));
                val = val.toUpperCase();
                tiles[index] = new Tile(val, new Label.LabelStyle(style));
                tiles[index].setSize(cellsize, cellsize);
                tiles[index].setAlignment(Align.center);
                tiles[index].setPosition(position[index].x, position[index].y);
                tiles[index].addListener(tileListener);
                tiles[index].setName(index + "");
                board.addActor(tiles[index]);
            }
        }

        return board;
    }

    private Group wordList() {
        Group wordList = new Group();
        int count = words.size();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = listFont;
        style.background = tileTexture("clear_cell");


        int rows = 4;
        int cols = (count / rows) + 1;

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        float width = ((Constants.GAME_WIDTH - cols) / (cols));
        float height = listFont.getData().capHeight + 10;
        float padding = 10;
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                if (index >= count)
                    break;
                position[index] = new Vector2((col * width) + padding,
                        ((row * (height)) + (Constants.GAME_HEIGHT * 0.85f)));
                String word = words.get(index);
                word = WordUtils.capitalize(word);
                tiles[index] = new Tile(word, new Label.LabelStyle(style));
                tiles[index].setSize(width, height);
                tiles[index].setAlignment(Align.left);
                tiles[index].setPosition(position[index].x, position[index].y);
                tiles[index].setName(index + "");
                wordList.addActor(tiles[index]);
            }
        }

        return wordList;
    }

    public SpriteDrawable tileTexture(String name) {
        Gdx.app.log("Color", name);
        Sprite sprite = gameUI.createSprite(name);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

    protected void updateColor() {

        if (words.contains(sb.toString().toLowerCase())) {
            sb.setLength(0);
            tileList.clear();
            color = random.nextInt(8) + 1;
        }
    }

    protected boolean validInput() {

        isBusy = true;

        if (sb.length() > longestWordLen) {
            sb.setLength(0);
            isBusy = false;
            return false;
        }

        for (String word : words) {

            String s = sb.toString().toLowerCase();

            if (word.startsWith(s)) {
                Gdx.app.log("WordTest", sb.toString());
                isBusy = false;
                return true;
            }
        }

        rewind();


        isBusy = false;

        return false;
    }

    private void rewind() {
        sb.setLength(0);

        for (Tile t : tileList) {
            t.getStyle().background = Util.loadTexture("background/brown_tile.png");
        }

        tileList.clear();
    }

    protected Tile startTile, endTile;
    protected List<Tile> tileList = new LinkedList<Tile>();

    private InputListener tileListener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Tile t = (Tile) event.getListenerActor();
            tileList.add(t);

            if (tile != null) {
                if (tile.getName().equals(t.getName())) {
                    sb.deleteCharAt(sb.length() - 1);
                    t.getStyle().background = Util.loadTexture("background/brown_tile.png");
                    tile = null;
                    return true;
                }
            }

            String v = t.getText().toString();
            sb.append(v);

            if (isBusy)
                return true;

            if (validInput())
                t.getStyle().background = tileTexture("tile" + color);

            updateColor();

            return true; //or false
        }

        /*   @Override
           public void touchDragged(InputEvent event, float x, float y, int pointer) {
               super.touchDragged(event, x, y, pointer);
               Tile b = (Tile) event.getListenerActor();
               Gdx.app.log("Actor",b.getName() + " ");
           }

           @Override
           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               super.touchUp(event, x, y, pointer, button);
           }


        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            super.enter(event, x, y, pointer, fromActor);
            Tile t = (Tile) event.getListenerActor();
            tileList.add(t);
            Gdx.app.log("Enter", t.getName());
        }


        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            super.exit(event, x, y, pointer, toActor);

            Tile t = (Tile) event.getListenerActor();

            Gdx.app.log("Exit", t.getName());

            t.getStyle().background = tileTexture("tile" + color);


            /*
            if (tile != null) {
                if (tile.getName().equals(t.getName())) {
                    if (sb.length() > 1)
                        sb.deleteCharAt(sb.length() - 1);
                    t.getStyle().background = Util.loadTexture("background/brown_tile.png");
                    tile = null;
                    return;
                }
            }

            tile = t;

            String v = t.getText().toString();
            sb.append(v);

            if (isBusy)
                return;

            if (validInput())
                t.getStyle().background = tileTexture("tile" + color);

            updateColor();

        }*/
    };

    class LengthComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str2.length() - str1.length();
        }
    }

    private void renderScore(SpriteBatch batch, float gameTime) {

        float x = Constants.GAME_WIDTH * 0.80f;
        float y = Constants.GAME_HEIGHT * 0.95f;

        //show score
        String score = "0";
        uiFont.draw(batch, score, x, y);

        float minutes = (float) Math.floor(gameTime / 60.0f);
        float seconds = (float) Math.floor(gameTime - minutes * 60.0f);

        //show time
        String time = String.format("%s:%s", StringUtils.leftPad(new DecimalFormat("##").format(minutes), 2, "0"),
                StringUtils.leftPad(new DecimalFormat("##").format(seconds), 2, "0"));
        uiFont.draw(batch, time, 15, y);
    }


}
