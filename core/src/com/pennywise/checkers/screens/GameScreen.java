package com.pennywise.checkers.screens;


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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.GameCam;
import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;

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
    private final Stage boardStage;
    private final GameCam camera;
    private final BitmapFont uiFont;
    private final BitmapFont strikeThrough;
    SpriteBatch batch;
    private final BitmapFont gridFont;
    private final BitmapFont listFont;
    private float cellsize = 0;
    private float gridHeight = 0;
    protected com.pennywise.checkers.objects.Tile tile;
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
    private int width, height;
    private Tile[] backgroundTiles;
    private Panel board;

    public GameScreen(Checkers game) {
        super(game);
        camera = com.pennywise.checkers.core.GameCam.instance;
        stage = new Stage(new FitViewport(com.pennywise.checkers.core.Constants.GAME_WIDTH, com.pennywise.checkers.core.Constants.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(this);

        //load fonts
        gridFont = com.pennywise.checkers.core.Util.loadFont("fonts/Roboto-Regular.ttf", 16, Color.BLACK);
        listFont = com.pennywise.checkers.core.Util.loadFont("fonts/Roboto-Bold.ttf", 24, Color.BLACK);
        uiFont = com.pennywise.checkers.core.Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.TEAL);
        strikeThrough = com.pennywise.checkers.core.Util.loadFont("fonts/BPtypewriteStrikethrough.ttf", 24, Color.BLACK);

        width = 8;
        height = 8;
        gridHeight = ((com.pennywise.checkers.core.Constants.GAME_HEIGHT * 3) / 4);

        batch = new SpriteBatch();
        sb = new StringBuilder();
        words = new LinkedList<String>();
        foundWords = new LinkedList<String>();
        color = random.nextInt(6) + 1;
        gameUI = new TextureAtlas("images/ui-pack.atlas");

        boardStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
    }

    @Override
    public void show() {

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

        boardStage.act();
        boardStage.draw();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isTouched()) {
            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);
            Actor actor2 = boardStage.hit(stageCoords.x, stageCoords.y, true);
            int y= 0;

            if (actor instanceof Piece) {
            //    board.stageToLocalCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            //    Actor bgTile = board.hit(stageCoords.x, stageCoords.y, true);
                ;
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
        stage.clear();

        Stack stack = new Stack();
        stack.setSize(com.pennywise.checkers.core.Constants.GAME_WIDTH, com.pennywise.checkers.core.Constants.GAME_HEIGHT);
        stack.add(hud());
        stack.add(layerPuzzle);
        stage.addActor(stack);
        boardStage.addActor(drawPieces(height, width));
    }

    private Table backGround() {
        Table layer = new Table();
        Image bg = new Image(gameUI.createPatch("panelInset_beigeLight"));
        layer.add(bg).height(com.pennywise.checkers.core.Constants.GAME_HEIGHT).width(com.pennywise.checkers.core.Constants.GAME_WIDTH).expandX().expandY();
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

    private Group board(int rows, int cols) {

        board = new Panel(gameUI.createPatch("panel_brown"));
        board.setTouchable(Touchable.childrenOnly);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = gridFont;
        style.background = tileTexture("line_dark");

        Vector2[] position = new Vector2[rows * cols];

        backgroundTiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index, y, x = 0;


        float posY = (float) (0.15 * Constants.GAME_HEIGHT);

        board.setOrigin(0, posY);
        board.setWidth(Constants.GAME_WIDTH);
        float bHeight = (cellsize * rows) + (padding * 2);
        board.setHeight(bHeight);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + padding,
                        padding + ((row * (cellsize)) + (com.pennywise.checkers.core.Constants.GAME_HEIGHT * 0.15f)));

                x = col * 22;
                y = row * 22;

                if ((row % 2) == (col % 2))
                    style.background = tileTexture("beigeCell");
                else
                    style.background = tileTexture("blackCell");

                backgroundTiles[index] = new com.pennywise.checkers.objects.Tile("", new Label.LabelStyle(style));
                backgroundTiles[index].setSize(cellsize, cellsize);
                backgroundTiles[index].setAlignment(Align.center);
                backgroundTiles[index].setPosition(position[index].x, position[index].y);
                backgroundTiles[index].setName(index + "");
                board.addActor(backgroundTiles[index]);
            }
        }

        return board;


    }

    protected Group drawPieces(int rows, int cols) {

        Group board = new Group();
        board.setTouchable(Touchable.childrenOnly);

        Vector2[] position = new Vector2[rows * cols];

        Piece[] pieces = new Piece[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index, y, x = 0;

        float posY = (float) (0.15 * Constants.GAME_HEIGHT);

        board.setOrigin(0, posY);
        board.setWidth(Constants.GAME_WIDTH);
        float bHeight = (cellsize * rows) + (padding * 2);
        board.setHeight(bHeight);

        //black pieces
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + padding,
                        padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.15f)));

                x = col * 22;
                y = row * 22;

                if ((row % 2) == (col % 2))
                    continue;
                else {
                    if (row < 3)
                        pieces[index] = new Piece(tileTexture("whitePiece"));
                    else if (row >= 5)
                        pieces[index] = new Piece(tileTexture("blackPiece"));
                    else
                        continue;

                }

                pieces[index].setSize(cellsize - 2, cellsize - 2);
                pieces[index].setPosition(position[index].x, position[index].y);
                pieces[index].setName(index + "");
                board.addActor(pieces[index]);
            }
        }


        return board;
    }

    public SpriteDrawable tileTexture(String name) {
        Gdx.app.log("Color", name);
        Sprite sprite = gameUI.createSprite(name);
        sprite.setFlip(false, true);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }

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
        float x = 0, y = 0;

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
