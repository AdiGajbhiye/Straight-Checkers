package com.pennywise.findwords.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.pennywise.findwords.objects.Tile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen implements InputProcessor {


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
    private List<String> foundWords;
    private int color = 0;
    private TextureAtlas gameUI;
    private Image time;
    private Button pause;
    private boolean isBusy = false;
    private int longestWordLen;
    private int width, height;
    protected List<Tile> tileList = new LinkedList<Tile>();
    protected List<String> validTileList = new LinkedList<String>();
    NinePatch pinkSelector;
    NinePatch purpleSelector;
    NinePatch orangeSelector;
    NinePatch maroonSelector;
    Texture purpleTexture;
    Texture orangeTexture;
    Texture maroonTexture;

    enum Orientation {UNKNOWN, VERTICAL, HORIZONTAL, DIAGONALDOWN, DIAGONALUP}

    protected Orientation orientation;


    public GameScreen(FindWords game) {
        super(game);
        camera = GameCam.instance;
        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(this);

        //load fonts
        gridFont = Util.loadFont("fonts/Roboto-Regular.ttf", 16, Color.BLACK);
        listFont = Util.loadFont("fonts/Roboto-Bold.ttf", 24, Color.BLACK);
        uiFont = Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.TEAL);
        strikeThrough = Util.loadFont("fonts/BPtypewriteStrikethrough.ttf", 24, Color.BLACK);

        width = 8;
        height = 8;
        gridHeight = ((Constants.GAME_HEIGHT * 3) / 4);

        batch = new SpriteBatch();
        sb = new StringBuilder();
        words = new LinkedList<String>();
        foundWords = new LinkedList<String>();
        color = random.nextInt(6) + 1;
        gameUI = new TextureAtlas("images/ui-pack.atlas");

    }

    @Override
    public void show() {

        //d = gameUI.createPatch("highlighter");
        pinkSelector = gameUI.createPatch("pinkselector");
        purpleTexture = gameUI.createPatch("purpleselector").getTexture();
        orangeTexture = gameUI.createPatch("orangeselector").getTexture();
        maroonTexture = gameUI.createPatch("maroonselector").getTexture();

        setupScreen();
    }

    private final Vector2 stageCoords = new Vector2();

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.59f, 0.44f, 0.29f, 1);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isTouched()) {
            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);
            if (actor instanceof Tile) {
                ((Tile) actor).getStyle().background = tileTexture("tile" + color);
                Gdx.app.log("Tile", ((Tile) actor).getName());
            }

        }


        batch.begin();
        //pinkSelector.draw(batch, 5, 5, 200, 40);
        renderScore(batch, delta);
        batch.end();

    }

    Vector2 v2 = new Vector2();
    Vector2 v1 = new Vector2();

    public float distance2d(float x1, float y1, float x2, float y2) {
        Vector2 vect = v2.set(x2, y2);
        return vect.dst(v1.set(x1, x2));
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
        time = new Image(gameUI.createSprite("pause_dark"));
        layer.add(time).height(40).width(40).bottom().right().expandX().padRight(20).padBottom(30);
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

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = gridFont;
        style.background = tileTexture("line_dark");

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index, y, x = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + padding,
                        padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.20f)));

                x = col * 22;
                y = row * 22;

                if ((row % 2) == (col % 2))
                    style.background = tileTexture("white_cell");
                else
                    style.background = tileTexture("beige_cell");

                tiles[index] = new Tile("", new Label.LabelStyle(style));
                tiles[index].setSize(cellsize, cellsize);
                tiles[index].setAlignment(Align.center);
                tiles[index].setPosition(position[index].x, position[index].y);
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
                        ((row * (height)) + (Constants.GAME_HEIGHT * 0.80f)));
                String word = words.get(index).toLowerCase();

                if (foundWords.contains(word))
                    style.font = strikeThrough;
                else
                    style.font = listFont;

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
            foundWords.add(sb.toString().toLowerCase());
            tileList.clear();
            color = random.nextInt(6) + 1;
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
            t.getStyle().background = Util.loadTexture("background/line_dark.png");
        }

        tileList.clear();
    }

    private InputListener tileListener = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Tile t = (Tile) event.getListenerActor();
            tileList.add(t);

            if (tile != null) {
                if (tile.getName().equals(t.getName())) {
                    sb.deleteCharAt(sb.length() - 1);
                    t.getStyle().background = Util.loadTexture("background/line_dark.png");
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
    };

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    class LengthComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str2.length() - str1.length();
        }
    }

    private void renderScore(SpriteBatch batch, float gameTime) {

        float x = Constants.GAME_WIDTH * 0.45f;
        float y = Constants.GAME_HEIGHT * 0.05f;

        //show score
        String score = "00/00";
        uiFont.draw(batch, score, x, y);

        float minutes = (float) Math.floor(gameTime / 60.0f);
        float seconds = (float) Math.floor(gameTime - minutes * 60.0f);

        //show time
        String time = String.format("%s:%s", StringUtils.leftPad(new DecimalFormat("##").format(minutes), 2, "0"),
                StringUtils.leftPad(new DecimalFormat("##").format(seconds), 2, "0"));
        uiFont.draw(batch, time, 15, y);
    }


}
