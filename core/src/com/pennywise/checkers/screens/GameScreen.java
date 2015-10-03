package com.pennywise.checkers.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Cam;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.core.logic.Black;
import com.pennywise.checkers.core.logic.Board;
import com.pennywise.checkers.core.logic.Human;
import com.pennywise.checkers.core.logic.Move;
import com.pennywise.checkers.core.logic.UserInteractions;
import com.pennywise.checkers.core.logic.White;
import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Owner;
import com.pennywise.checkers.core.logic.enums.Player;
import com.pennywise.checkers.core.logic.enums.ReturnCode;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen implements InputProcessor {


    private final Stage stage;
    private final Stage boardStage;
    private final Cam camera;
    private OrthographicCamera hudCam;
    private final BitmapFont hudFont;
    SpriteBatch batch;
    private float cellsize = 0;
    private float gridHeight = 0;
    private TextureAtlas gameUI;
    private Button pause;
    private boolean isBusy = false;
    private int width, height;
    private Tile[] backgroundTiles;
    private Panel board;
    private Piece selectedPiece;
    private List<Tile> selectedTiles;
    private static Board logicBoard;
    private ReturnCode retCode;
    private Executor executor;
    private boolean gameOver = false;
    String strTime = "";

    private final SpriteDrawable validBlackCell;
    private final SpriteDrawable validCell;
    private final SpriteDrawable blackCell;
    private final SpriteDrawable selectedBlackCell;
    private Image pauseButton;


    public GameScreen(Checkers game) {
        super(game);
        camera = Cam.instance;

        hudCam = new OrthographicCamera();
        hudCam.position.set(0, 0, 0);
        hudCam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // don't flip y-axis
        hudCam.update();

        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));

        Gdx.input.setInputProcessor(this);
        //load fonts
        hudFont = Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.BLACK);

        width = 8;
        height = 8;
        gridHeight = ((com.pennywise.checkers.core.Constants.GAME_HEIGHT * 3) / 4);

        batch = new SpriteBatch();
        gameUI = new TextureAtlas("images/ui-pack.atlas");
        executor = Executors.newSingleThreadExecutor();
        boardStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        selectedTiles = new LinkedList<Tile>();

        selectedBlackCell = tileTexture("selectedBlackCell");
        blackCell = tileTexture("blackCell");
        validBlackCell = tileTexture("validDarkCell");
        validCell = tileTexture("validCell");

    }

    private void initialize() {
        logicBoard = new Board();
        White.owner = Owner.HUMAN;
        Black.owner = Owner.ROBOT;

    }

    @Override
    public void show() {
        setupScreen();
        initialize();
    }

    private final Vector2 stageCoords = new Vector2();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderHud(batch, delta);
        renderFPS(batch);

        stage.act();
        stage.draw();

        boardStage.act();
        boardStage.draw();

        if (Gdx.input.isTouched()) {
            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);
            Actor actor2 = boardStage.hit(stageCoords.x, stageCoords.y, true);

            if (actor2 != null && actor2 instanceof Piece) {
                selectedPiece = (Piece) actor2;
                if (actor instanceof Tile) {
                    Tile tile = (((Tile) actor));
                    if (tile.getCellEntry() == CellEntry.black)
                        tile.getStyle().background = validBlackCell;
                    else
                        tile.getStyle().background = validCell;
                }
            } else {

                if (selectedPiece != null) {
                    if (actor instanceof Tile) {
                        selectedTiles.add(((Tile) actor));
                        int position = Integer.parseInt(selectedTiles.get(0).getName());
                        int dstRow = (width - 1) - (position / width);
                        int dstCol = position % width;

                        position = Integer.parseInt(selectedPiece.getName());
                        int curRow = (width - 1) - (position / width);
                        int curCol = position % width;
                        if (selectedTiles.get(0).getCellEntry() == CellEntry.black) {
                            movePiece(new Move(curRow, curCol, dstRow, dstCol));
                        } else {
                            selectedPiece = null;
                            selectedTiles.clear();
                        }
                    }
                }
            }

            if (retCode == ReturnCode.VALID_MOVE) {
                playGame();
                retCode = ReturnCode.INVALID_MOVE;
            }

            updateUI();

        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
        boardStage.getViewport().setScreenSize(width, height);
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
        stack.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        stack.add(hud());
        stack.add(layerPuzzle);
        stage.addActor(stack);
        boardStage.addActor(drawPieces(height, width));
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
        pauseButton = new Image(gameUI.createSprite("pause_dark"));
        layer.add(pauseButton).height(40).width(40).bottom().right().expandX().padRight(20).padBottom(30);
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
        style.font = hudFont;
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
                        padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.15f)));

                x = col * 22;
                y = row * 22;

                if ((row % 2) == (col % 2)) {
                    style.background = tileTexture("beigeCell");
                    backgroundTiles[index] = new Tile(CellEntry.white, new Label.LabelStyle(style));
                } else {
                    style.background = tileTexture("blackCell");
                    backgroundTiles[index] = new Tile(CellEntry.black, new Label.LabelStyle(style));
                }

                backgroundTiles[index].setSize(cellsize, cellsize);
                backgroundTiles[index].setAlignment(Align.center);
                backgroundTiles[index].setPosition(position[index].x, position[index].y);
                backgroundTiles[index].setName(index + "");
                board.addActor(backgroundTiles[index]);
            }
        }

        return board;


    }


    protected void movePiece(Move move) {

        retCode = Human.makeNextWhiteMoves(move, logicBoard);

        if (retCode == ReturnCode.VALID_MOVE) {

            float posX = selectedTiles.get(0).getX() + (selectedTiles.get(0).getWidth() / 2);
            float posY = selectedTiles.get(0).getY() + (selectedTiles.get(0).getHeight() / 2);

            MoveToAction moveAction = new MoveToAction();
            moveAction.setPosition(posX, posY, Align.center);
            moveAction.setDuration(0.5f);
            selectedPiece.addAction(moveAction);

            selectedPiece.setName(selectedTiles.get(0).getName());// = null;
            selectedTiles.get(0).getStyle().background = blackCell;
            selectedTiles.clear();
            selectedPiece = null;
        }

        if (retCode == ReturnCode.MULTIPLE_CAPTURE) {

            float posX = selectedTiles.get(0).getX() + (selectedTiles.get(0).getWidth() / 2);
            float posY = selectedTiles.get(0).getY() + (selectedTiles.get(0).getHeight() / 2);

            MoveToAction moveAction = new MoveToAction();
            moveAction.setPosition(posX, posY, Align.center);
            moveAction.setDuration(0.5f);
            selectedPiece.addAction(moveAction);

            selectedPiece.setName(selectedTiles.get(0).getName());// = null;
            selectedTiles.get(0).getStyle().background = blackCell;

            if (selectedTiles.size() != 0)
                selectedTiles.remove(0);
        }

        if (retCode == ReturnCode.INVALID_MOVE) {
            selectedTiles.clear();
            selectedPiece = null;
            return;
        }

        if (retCode == ReturnCode.FORCED_MOVES) {
            selectedTiles.clear();
            selectedPiece = null;
            return;
        }
    }

    protected Tile getTile(String name) {

        for (Tile t : backgroundTiles) {
            if (t.getName().equalsIgnoreCase(name))
                return t;
        }

        return null;
    }

    protected void moveOpponentPiece(Vector<Move> moves) {

        for (Move m : moves) {

            int iR = m.getInitialRow();
            int iC = m.getInitialCol();

            int fR = m.getFinalRow();
            int fC = m.getFinalCol();

            int index = iC + (((width - 1) - iR) * width);

            Tile srcTile = getTile(index + "");
            String srcName = srcTile.getName();

            index = fC + (((width - 1) - fR) * width);

            Tile destTile = getTile(index + "");
            String destName = destTile.getName();


            if (srcTile != null && destTile != null) {
                srcTile.getStyle().background = selectedBlackCell;
                destTile.getStyle().background = selectedBlackCell;
            }

            //find the piece
            for (Actor a : boardStage.getActors()) {
                if (a instanceof Group) {
                    for (Actor actor : ((Group) a).getChildren())
                        if (actor instanceof Piece) {
                            if (actor.getName().equalsIgnoreCase(srcName)) {
                                Piece p = (Piece) actor;
                                //update name
                                actor.setName(destName);
                                float posX = destTile.getX() + (destTile.getWidth() / 2);
                                float posY = destTile.getY() + (destTile.getHeight() / 2);

                                MoveToAction moveAction = new MoveToAction();
                                moveAction.setPosition(posX, posY, Align.center);
                                moveAction.setDuration(0.5f);
                                p.addAction(moveAction);
                                break;
                            }
                        }
                }
            }

            srcTile.getStyle().background = blackCell;
            destTile.getStyle().background = blackCell;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean playGame() {

        if (!logicBoard.CheckGameComplete()) {
            if (logicBoard.CheckGameDraw(Player.white)) {
                return false;
            }

            logicBoard.CheckGameDraw(Player.black);
            logicBoard.Display();

            Vector<Move> moves = Black.Move(logicBoard);

            moveOpponentPiece(moves);

            logicBoard.Display();


            if (logicBoard.CheckGameComplete()) {
                gameOver = true;
                UserInteractions.DisplayGreetings(Player.black, logicBoard);
                logicBoard.Display();
                return false;
            }


        } else {
            gameOver = true;
            return true;
        }

        return false;
    }

    protected void updateUI() {

        for (int r = 0; r < height; r++) {
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < width; c += 2) {

                updatePieces(r, c);

            }
        }
    }

    protected void updatePieces(int row, int col) {

        int index = col + (((width - 1) - row) * width);
        String targetName = index + "";

        //find the piece
        for (Actor a : boardStage.getActors()) {
            if (a instanceof Group) {
                for (Actor actor : ((Group) a).getChildren())
                    if (actor instanceof Piece) {
                        if (actor.getName().equalsIgnoreCase(targetName)) {
                            if (logicBoard.getCell()[row][col].equals(CellEntry.empty)) {
                                ((Group) a).removeActor(actor);
                            }
                            if (logicBoard.getCell()[row][col].equals(CellEntry.blackKing)) {
                                ((Piece) actor).setDrawable(tileTexture("blackKing"));
                            }
                            if (logicBoard.getCell()[row][col].equals(CellEntry.whiteKing)) {
                                ((Piece) actor).setDrawable(tileTexture("whiteKing"));
                            }

                        }
                    }
            }
        }
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
                        pieces[index] = new Piece(tileTexture("blackPiece"));
                    else if (row >= 5)
                        pieces[index] = new Piece(tileTexture("whitePiece"));
                    else
                        continue;

                }

                pieces[index].setSize(cellsize, cellsize);
                pieces[index].setPosition(position[index].x, position[index].y);
                pieces[index].setName(index + "");
                board.addActor(pieces[index]);
            }
        }


        return board;
    }

    public SpriteDrawable tileTexture(String name) {
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

    private void renderGui(SpriteBatch batch, float runTime) {
        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();

        batch.end();
    }


    private void renderHud(SpriteBatch batch, float gameTime) {

        float y = hudCam.viewportHeight * 0.95f;

        float minutes = (float) Math.floor(gameTime / 60.0f);
        float seconds = (float) Math.floor(gameTime - minutes * 60.0f);

        strTime = String.format("%.0fm%.0fs", minutes, seconds);

        /*
        //show time
        strTime = String.format("%s:%s", StringUtils.leftPad(new DecimalFormat("##").format(minutes), 2, "0"),
                StringUtils.leftPad(new DecimalFormat("##").format(seconds), 2, "0"));*/

        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();
        hudFont.draw(batch, strTime, 15, y);
        batch.end();
    }

    private void renderFPS(SpriteBatch batch) {

        float x = hudCam.viewportWidth - 150;
        float y = 30;

        int fps = Gdx.graphics.getFramesPerSecond();

        BitmapFont fpsFont = hudFont;

        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        batch.end();
    }

}
