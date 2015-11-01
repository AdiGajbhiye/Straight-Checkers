package com.pennywise.checkers.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Assets;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;

import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.core.engine.Move;
import com.pennywise.checkers.core.engine.Point;
import com.pennywise.checkers.core.engine.Simplech;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;

import java.util.Comparator;
import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;


/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen implements InputProcessor {


    private final Stage stage;
    private final Stage boardStage;
    private final Stage dialogStage;
    private final OrthographicCamera camera;
    private OrthographicCamera hudCam;

    SpriteBatch batch;
    private float cellsize = 0;
    private float gridHeight = 0;
    private Button pause;
    private boolean isBusy = false;
    private int width, height;
    private Tile[] backgroundTiles;
    private Panel panel;
    private Piece selectedPiece;
    private Tile selectedTile;
    private int[] board = new int[46];
    private boolean gameOver = false;
    String strTime = "";

    private Image pauseButton;
    private Simplech engine;
    // Screen second counter (1 second tick)
    private long startTime = System.nanoTime();
    private long secondsTime = 0L;
    private BitmapFont hudFont;
    private Move move = new Move();
    private boolean opponentMove = false;

    public GameScreen(Checkers game) {
        super(game);


        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // don't flip y-axis
        camera.update();

        hudCam = new OrthographicCamera();
        hudCam.position.set(0, 0, 0);
        hudCam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // don't flip y-axis
        hudCam.update();


        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        dialogStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        boardStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));


        Gdx.input.setInputProcessor(this);

        hudFont = Assets.font;

        width = 8;
        height = 8;
        gridHeight = ((Constants.GAME_HEIGHT * 3) / 4);

        batch = new SpriteBatch();

        engine = new Simplech();
        engine.initCheckers(board);
    }


    @Override
    public void show() {
        setupScreen();

    }

    private final Vector2 stageCoords = new Vector2();

    private int getRow(int index, int boardCol) {
        return (int) Math.floor((((index - 2) - boardCol) / boardCol));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (System.nanoTime() - startTime >= 1000000000) {
            secondsTime++;
            startTime = System.nanoTime();
        }

        if(opponentMove){

            opponentMove = false;
            isBusy = true;
            new Thread("Opponent"){
                public void run(){
                    int opponentPlayer = selectedPiece.getPlayer() == Simplech.BLACK ? Simplech.WHITE : Simplech.BLACK;
                    engine.getMove(board, opponentPlayer, 5, false, move);
                    moveOpponentPiece(move);
                    engine.printBoard(board);
                    isBusy = false;
                    selectedPiece = null;
                }
            }.start();




        }


        if (Gdx.input.isTouched() && !isBusy) {
            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);
            Actor actor2 = boardStage.hit(stageCoords.x, stageCoords.y, true);

            if (actor2 != null && actor2 instanceof Piece) {
                selectedPiece = (Piece) actor2;
                if (actor instanceof Tile) {
                    Tile tile = (((Tile) actor));
                    if (tile.getCellEntry() == Simplech.BLACK)
                        tile.getStyle().background = Assets.img_selected_cell_dark;
                    else
                        tile.getStyle().background = Assets.img_selected_cell_lite;
                }
            } else {

                if (selectedPiece != null) {
                    if (actor instanceof Tile) {
                        selectedTile = ((Tile) actor);

                        if (selectedTile.getCellEntry() == Simplech.BLACK) {
                            movePiece();
                        } else {
                            selectedPiece = null;
                            selectedTile = null;
                        }
                    }
                }
            }
        }

        stage.act();
        stage.draw();

        boardStage.act();
        boardStage.draw();

        dialogStage.act();
        dialogStage.draw();

        renderHud(batch, delta);
        renderFPS(batch);
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
        //stack.add(backGround());
        stack.add(hud());
        stack.add(layerPuzzle);
        stage.addActor(stack);
        boardStage.addActor(drawPieces(height, width, false));
    }

    private Table backGround() {
        Table layer = new Table();
        Image bg = new Image(Assets.img_background);
        layer.add(bg).height(Constants.GAME_HEIGHT).width(Constants.GAME_WIDTH).expandX().expandY();
        return layer;
    }

    private Table hud() {
        Table layer = new Table();
        layer.top();
        pauseButton = new Image(Assets.img_btn_pause);
        layer.add(pauseButton).height(40).width(40).top().right().expandX().padRight(20).padTop(30);
        return layer;
    }

    private Table buildBoard() {
        Table layer = new Table();
        layer.addActor(board(height, width, false));
        return layer;
    }

    private Group board(int rows, int cols, boolean inverted) {

        panel = new Panel(Assets.img_board_bg);
        panel.setTouchable(Touchable.childrenOnly);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = hudFont;
        style.background = Assets.img_dark_outline;

        Vector2[] position = new Vector2[rows * cols];

        backgroundTiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index, y, x = 0;


        float posY = (float) (0.25 * Constants.GAME_HEIGHT);

        panel.setOrigin(0, posY);
        panel.setWidth(Constants.GAME_WIDTH);
        float bHeight = (cellsize * rows) + (padding * 2);
        panel.setHeight(bHeight);

        int text = 0;
        int count = 1;

        if (inverted) {
            for (int row = 7; row >= 0; row--) {
                for (int col = 0; col < cols; col++) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        style.background = Assets.img_cell_dark;
                        backgroundTiles[index] = new Tile(Simplech.BLACK, new Label.LabelStyle(style));

                    } else {
                        text = 0;
                        style.background = Assets.img_cell_light;
                        backgroundTiles[index] = new Tile(Simplech.WHITE, new Label.LabelStyle(style));
                    }

                    backgroundTiles[index].setSize(cellsize, cellsize);
                    backgroundTiles[index].setAlignment(Align.center);
                    backgroundTiles[index].setPosition(position[index].x, position[index].y);
                    backgroundTiles[index].setName(text + "");
                    panel.addActor(backgroundTiles[index]);
                }
            }
        } else {
            for (int row = 0; row < rows; row++) {
                for (int col = 7; col >= 0; col--) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        style.background = Assets.img_cell_dark;
                        backgroundTiles[index] = new Tile(Simplech.BLACK, new Label.LabelStyle(style));

                    } else {
                        text = 0;
                        style.background = Assets.img_cell_light;
                        backgroundTiles[index] = new Tile(Simplech.WHITE, new Label.LabelStyle(style));
                    }

                    backgroundTiles[index].setSize(cellsize, cellsize);
                    backgroundTiles[index].setAlignment(Align.center);
                    backgroundTiles[index].setPosition(position[index].x, position[index].y);
                    backgroundTiles[index].setName(text + "");
                    panel.addActor(backgroundTiles[index]);
                }
            }
        }
        return panel;


    }

    protected void move() {

        float posX = selectedTile.getX() + (selectedTile.getWidth() / 2);
        float posY = selectedTile.getY() + (selectedTile.getHeight() / 2);

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(posX, posY, Align.center);
        moveAction.setDuration(0.5f);
        selectedPiece.toFront();
        selectedPiece.addAction(moveAction);

        selectedPiece.setName(selectedTile.getName());
        selectedPiece.toBack();

    }

    protected void movePiece() {

        int from = Integer.parseInt(selectedPiece.getName());
        int to = Integer.parseInt(selectedTile.getName());

        Move move = engine.isLegal(board, selectedPiece.getPlayer(), from, to);

        if (move != null) {
            engine.doMove(board, move);
            move();
            engine.printBoard(board);
            opponentMove = true;
        } else
            return;
    }

    protected Tile getTile(String name) {

        for (Tile t : backgroundTiles) {
            if (t.getName().equalsIgnoreCase(name))
                return t;
        }

        return null;
    }

    protected void moveOpponentPiece(Move move) {
        Piece piece = null;
        Tile srcTile;
        String srcName = "";
        Tile destTile = null;
        String destName = "";
        SequenceAction sequenceAction = new SequenceAction();

        for (int i = 0; i < move.n; i += 2) {

            int[] steps = engine.moveNotation(move);

            srcTile = getTile(steps[0] + "");
            srcName = srcTile.getName();

            destTile = getTile(steps[1] + "");
            destName = destTile.getName();


            float posX = destTile.getX();
            float posY = destTile.getY();


            sequenceAction.addAction(delay(0.5f));
            sequenceAction.addAction(moveTo(posX, posY, 0.5f));
        }

        sequenceAction.addAction(run(new Runnable() {
            public void run() {
                //updateUI();
                ;
            }
        }));

        //find the piece
        for (Actor a : boardStage.getActors()) {
            if (a instanceof Group) {
                for (Actor actor : ((Group) a).getChildren())
                    if (actor instanceof Piece) {
                        if (actor.getName().equalsIgnoreCase(srcName)) {
                            piece = (Piece) actor;
                            //update name
                            piece.toFront();
                            piece.setName(destName);
                            piece.addAction(sequenceAction);
                            break;
                        }
                    }
            }
        }
    }

    /*
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
                        gameOverDialog("Black");
                        logicBoard.Display();
                        return false;
                    }


                } else {
                    gameOverDialog("Black");
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

                int index = col + (row * width);
                String targetName = index + "";

                //find the piece
                for (Actor a : boardStage.getActors()) {
                    if (a instanceof Group) {
                        for (Actor actor : ((Group) a).getChildren())
                            if (actor instanceof Piece) {
                                if (actor.getName().equalsIgnoreCase(targetName)) {
                                    if (logicBoard.getCell()[row][col].equals(CellEntry.empty)) {
                                        actor.addAction(sequence(fadeOut(0.15f), removeActor()));
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

        */
    protected Group drawPieces(int rows, int cols, boolean inverted) {

        Group board = new Group();
        board.setTouchable(Touchable.childrenOnly);

        Vector2[] position = new Vector2[rows * cols];

        Piece[] pieces = new Piece[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));

        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;

        int index, text = 0;
        int count = 1;

        float posY = (float) (0.25 * Constants.GAME_HEIGHT);

        board.setOrigin(0, posY);
        board.setWidth(Constants.GAME_WIDTH);
        float bHeight = (cellsize * rows) + (padding * 2);
        board.setHeight(bHeight);

        if (inverted) {
            for (int row = 7; row >= 0; row--) {
                for (int col = 0; col < cols; col++) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        if (row >= 5)
                            pieces[index] = new Piece(Assets.img_pawn_black, Simplech.BLACK);
                        else if (row < 3)
                            pieces[index] = new Piece(Assets.img_pawn_white, Simplech.WHITE);
                        Gdx.app.error("Text", text + "");
                        Gdx.app.error("Index", index + "");

                        if (pieces[index] == null)
                            continue;

                        pieces[index].setSize(cellsize, cellsize);
                        pieces[index].setPosition(position[index].x, position[index].y);
                        pieces[index].setName(index + "");
                        board.addActor(pieces[index]);
                    }
                }
            }
        } else {
            for (int row = 0; row < rows; row++) {
                for (int col = 7; col >= 0; col--) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        if (row >= 5)
                            pieces[index] = new Piece(Assets.img_pawn_white, Simplech.WHITE);
                        else if (row < 3)
                            pieces[index] = new Piece(Assets.img_pawn_black, Simplech.BLACK);

                        if (pieces[index] == null)
                            continue;
                        pieces[index].setSize(cellsize, cellsize);
                        pieces[index].setPosition(position[index].x, position[index].y);
                        pieces[index].setName(text + "");
                        board.addActor(pieces[index]);
                    }
                }
            }
        }


        return board;
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


    private void renderGui(SpriteBatch batch, float runTime) {
        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();

        batch.end();
    }

    /**
     * Get screen time from start in format of HH:MM:SS. It is calculated from
     * "secondsTime" parameter, reset that to get resetted time.
     */
    public String getScreenTime() {
        int seconds = (int) (secondsTime % 60);
        int minutes = (int) ((secondsTime / 60) % 60);
        int hours = (int) ((secondsTime / 3600) % 24);
        String secondsStr = (seconds < 10 ? "0" : "") + seconds;
        String minutesStr = (minutes < 10 ? "0" : "") + minutes;
        String hoursStr = (hours < 10 ? "0" : "") + hours;
        return new String(hoursStr + ":" + minutesStr + ":" + secondsStr);
    }


    private void renderHud(SpriteBatch batch, float gameTime) {

        float y = Constants.GAME_HEIGHT * 0.95f;

        float minutes = (float) Math.floor(gameTime / 60.0f);
        float seconds = (float) Math.floor(gameTime - minutes * 60.0f);

        strTime = getScreenTime();

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


    public void gameOverDialog(String winner) {

        Skin skin = new Skin(Gdx.files.internal("images/ui-pack.json"), Assets.getAtlas());

        Label label = new Label(winner + "wins!", skin);
        label.setWrap(true);
        label.setFontScale(.8f);
        label.setAlignment(Align.center);

        Dialog dialog =
                new Dialog("Game Over", skin) {
                    protected void result(Object object) {
                        System.out.println("Chosen: " + object);
                    }
                };

        dialog.padTop(50).padBottom(50);
        dialog.getContentTable().add(label).width((Constants.GAME_WIDTH * 0.65f)).row();
        dialog.getButtonTable().padTop(50);

        TextButton dbutton = new TextButton("Yes", skin);
        dialog.button(dbutton, true);

        dbutton = new TextButton("No", skin);
        dialog.button(dbutton, false);
        dialog.invalidateHierarchy();
        dialog.invalidate();
        dialog.layout();
        dialog.show(dialogStage);
    }

}
