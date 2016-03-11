package com.pennywise.checkers.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Assets;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.core.engine.Checker;
import com.pennywise.checkers.core.engine.GameEngine;
import com.pennywise.checkers.core.persistence.GameObject;
import com.pennywise.checkers.core.persistence.SaveUtil;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;
import com.pennywise.checkers.screens.dialogs.GameDialog;
import com.pennywise.checkers.screens.dialogs.GameOver;
import com.pennywise.managers.MultiplayerDirector;
import com.pennywise.multiplayer.TransmissionPackage;
import com.pennywise.multiplayer.TransmissionPackagePool;

import java.util.Date;
import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen implements InputProcessor, MultiplayerDirector {

    private final Stage stage;
    private final Stage boardStage;
    private final Stage dialogStage;
    private final OrthographicCamera camera;
    private OrthographicCamera hudCam;
    private Label infoLabel;
    protected TransmissionPackagePool transmissionPackagePool;

    SpriteBatch batch;
    private float cellsize = 0;
    private float gridHeight = 0;
    private ImageButton pauseGame, undoMove;
    private int width, height;
    private Tile[] backgroundTiles;
    private Panel panel;
    private Piece humanPiece = null;
    private int humanPlayer;
    private Piece cpuPiece = null;
    private Tile fromTile;
    private Tile toTile;

    private boolean gameOver = false;
    private boolean completed = true;
    private boolean humanTurn = true;

    String strTime = "";

    private Checker engine;
    private Group gameBoard;
    private long startTime = 0;
    private boolean timer = false;
    private long secondsTime = 0L;
    private BitmapFont hudFont;
    private boolean opponentMove = false;
    int toMove;
    int level = Constants.EASY;

    int[][] board = new int[8][8];
    int[][] preBoard1 = new int[8][8];                 //for undo
    int preToMove1;
    int[][] preBoard2 = new int[8][8];
    int preToMove2;
    int[][] preBoard3 = new int[8][8];
    int preToMove3;
    private int undoCount = 0;
    private int[] pieceCount = new int[2];  //pieceCount[0] = black, pieceCount[1]  = red
    private GameDialog gameDialog;
    private int saveCounter = 0;

    Vector moves;
    int moveCounter = 0;
    private boolean invert = false;
    private boolean multiplayer = false;
    private float[] boardPosition = null;
    private boolean ready = false;
    private float xx, yy;
    private int playerTurn = Checker.WHITENORMAL;
    private Label nameLabel;
    private int round = 0;
    private boolean acknowledged = false;
    private TextButton pushMove;

    public void newGame() {                            //creates a new game

        for (int i = 0; i < 8; i++)                                  //applies values to the board
        {
            for (int j = 0; j < 8; j++)
                board[i][j] = Checker.EMPTY;

            for (int j = 0; j < 3; j++)
                if (isPossibleSquare(i, j))
                    board[i][j] = Checker.BLACKNORMAL;

            for (int j = 5; j < 8; j++)
                if (isPossibleSquare(i, j))
                    board[i][j] = Checker.WHITENORMAL;
        }

        moves = new Vector();

        copyBoard(board, preBoard1);
        copyBoard(board, preBoard2);
        copyBoard(board, preBoard3);

        preToMove3 = preToMove2 = preToMove1 = toMove;

    }

    private void clearBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = Checker.EMPTY;
    }

    private void copyBoard(int[][] src, int[][] dst) {
        for (int i = 0; i < 8; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, 8);                       //for undo
        }
    }

    private boolean isPossibleSquare(int i, int j) {
        return (i + j) % 2 == 1;
    }

    public void undo() {            //undo function
        undoCount = 1;

        for (int i = 0; i < 8; i++) {
            System.arraycopy(preBoard3[i], 0, board[i], 0, 8);              //copies previous board
        }

        toMove = preToMove3;
    }

    public GameScreen(Checkers game, int difficulty) {
        super(game);

        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        camera.setToOrtho(true, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); //  flip y-axis
        camera.update();

        hudCam = new OrthographicCamera();
        hudCam.position.set(0, 0, 0);
        hudCam.setToOrtho(true, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // flip y-axis
        hudCam.update();

        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        dialogStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        boardStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));

        this.level = difficulty;

        transmissionPackagePool = new TransmissionPackagePool();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(this, dialogStage));

        hudFont = Assets.font;

        width = 8;
        height = 8;
        gridHeight = ((Constants.GAME_HEIGHT * 3) / 4);
        cellsize = ((Constants.GAME_WIDTH - width) / (width));

        batch = new SpriteBatch();

        initGame();
    }

    protected void initGame() {

        invert = false;
        multiplayer = game.isMultiplayer();
        humanPlayer = playerTurn;
        newGame();

        setupScreen();

        timer = true;
    }

    private final Vector2 stageCoords = new Vector2();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (timer)
            if (System.nanoTime() - startTime >= 1000000000) {
                secondsTime++;
                startTime = System.nanoTime();
            }

        if (opponentMove && !multiplayer) {
            opponentMove = false;
            new Thread("Opponent") {
                public void run() {
                    moveOpponentPiece();
                }
            }.start();
        }

        if (Gdx.input.isTouched() && humanTurn) {

            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);

            if (completed) {
                Actor actor2 = boardStage.hit(stageCoords.x, stageCoords.y, true);

                if (actor2 != null && actor2 instanceof Piece) {
                    humanPiece = (Piece) actor2;
                    if (humanPiece.getPlayer() == humanPlayer) {
                        humanPiece.setSelected(true);
                        fromTile = (Tile) actor;
                        if (actor instanceof Tile) {
                            Tile tile = (((Tile) actor));
                        }
                    } else
                        humanPiece = null;
                } else {

                    if (humanPiece != null && actor != null) {
                        if (actor instanceof Tile) {

                            toTile = ((Tile) actor);

                            if (toTile.getCellEntry() == Checker.BLACKNORMAL) {
                                movePiece();
                            }
                        }
                    }
                }
            } else { //move not complete multi capture
                //Gdx.app.log("FFF", "INCOMPLETE");
                Gdx.app.log("Tiles", "FROM => " + fromTile.getName() + " TO => " + toTile.getName());

                if (actor != null && toTile != null) {
                    if (!(actor.getName().equals(toTile.getName()))) {
                        if (humanPiece != null && actor != null) {
                            if (actor instanceof Tile) {
                                toTile = ((Tile) actor);
                                if (toTile.getCellEntry() == Checker.BLACKNORMAL) {
                                    movePiece();
                                }
                            }
                        }
                    } else {
                        Gdx.app.log("Tiles", "ILLEGAL MOVE");
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

        renderHud(batch);

        save();

        drawRect();
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
        stage.clear();
        boardStage.clear();
        //will hold pieces
        gameBoard = new Group();
        drawPieces(height, width);

        Table layerBoard = drawBoard();


        Stack stack = new Stack();
        stack.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        stack.add(backGround());
        stack.add(hud());
        stack.add(opponentHud());
        stack.add(layerBoard);
        stage.addActor(stack);

        boardStage.addActor(gameBoard);
    }

    private Table backGround() {
        Table layer = new Table();
        Image bg = new Image(getSkin().getDrawable("wooden"));
        layer.add(bg).height(Constants.GAME_HEIGHT).width(Constants.GAME_WIDTH).expandX().expandY();
        return layer;
    }

    private Table hud() {
        Table layer = new Table();
        layer.bottom();
        //layer.setWidth(Constants.GAME_WIDTH);
        //layer.setDebug(true);

        pushMove = new TextButton("Push", getSkin(), "green");
        pushMove.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(multiplayer) {
                    Vector v = new Vector();
                    v.add(new int[]{0, 0, 0, 0});
                    updatePeer(v);
                }
            }
        });

        nameLabel = new Label("Player name:", getSkin(), "white-text");
        nameLabel.setAlignment(Align.center);

        layer.add(nameLabel).size(420, 60).center().padBottom(5);
        layer.row();
        //undoMove = new ImageButton(Assets.img_undo);
        layer.add(pushMove).size(120, 60).right().padRight(5).padBottom(20);
        return layer;
    }

    private Table opponentHud() {
        Table layer = new Table();
        layer.top();
        layer.padTop(80);
        layer.setWidth(Constants.GAME_WIDTH);

        nameLabel = new Label("Player name:", getSkin(), "white-text");
        nameLabel.setAlignment(Align.center);

        layer.add(nameLabel).size(420, 60).center().padTop(10);
        return layer;
    }

    private Table drawBoard() {
        Table layer = new Table();
        Group g = board(height, width);
        boardPosition = new float[2];
        boardPosition[0] = g.getOriginX();
        boardPosition[1] = g.getOriginY();
        layer.addActor(g);
        return layer;
    }

    private Group board(int rows, int cols) {

        panel = new Panel(Assets.img_board_bg);
        panel.setTouchable(Touchable.childrenOnly);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = hudFont;
        style.background = Assets.img_dark_outline;

        Vector2[] position = new Vector2[rows * cols];

        backgroundTiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index = 0;

        float posY = (float) (0.20 * Constants.GAME_HEIGHT);

        panel.setOrigin(0, posY);
        panel.setWidth(Constants.GAME_WIDTH);
        float bHeight = (cellsize * rows) + (padding * 2);
        panel.setHeight(bHeight);

        int text = 0;
        int count = 1;


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((row * cellsize) + padding,
                        padding + ((col * (cellsize)) + (Constants.GAME_HEIGHT * 0.20f)));

                if (isPossibleSquare(row, col)) {
                    text = (count += 2) / 2;
                    style.background = Assets.img_cell_dark;
                    backgroundTiles[index] = new Tile(Checker.BLACKNORMAL, new Label.LabelStyle(style));

                } else {
                    text = 0;
                    style.background = Assets.img_cell_light;
                    backgroundTiles[index] = new Tile(Checker.WHITENORMAL, new Label.LabelStyle(style));
                }

                backgroundTiles[index].setSize(cellsize, cellsize);
                backgroundTiles[index].setAlignment(Align.center);
                backgroundTiles[index].setPosition(position[index].x, position[index].y);

                if (text != 0) {
                    backgroundTiles[index].setName(text + "");
                }

                panel.addActor(backgroundTiles[index]);
            }
        }
        return panel;
    }

    protected void move() {

        float posX = toTile.getX() + (toTile.getWidth() / 2);
        float posY = toTile.getY() + (toTile.getHeight() / 2);


        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(posX, posY, Align.center);
        moveAction.setDuration(0.35f);
        humanPiece.toFront();
        humanPiece.addAction(sequence(moveAction, run(new Runnable() {
            public void run() {
                if (completed) {
                    if (humanPiece != null) {
                        humanPiece.toBack();
                        humanPiece.setSelected(false);
                    }
                    drawPieces(8, 8);
                    gameOver();
                    humanTurn = false;
                    opponentMove = true;
                }
            }
        })));
    }

    protected void gameOver() {
        if (Checker.noMovesLeft(board, getPlayer())) {
            gameOver("Black");
            timer = false;
        }
    }

    protected void movePiece() {

        int[] from = Checker.getIndex(fromTile.getX() - boardPosition[0], fromTile.getY() - boardPosition[1], cellsize);
        int[] dest = Checker.getIndex(toTile.getX() - boardPosition[0], toTile.getY() - boardPosition[1], cellsize);

        Gdx.app.log("BOARD", "FROM=> " + from[0] + "," + from[1] + " TO " + dest[0] + "," + dest[1]);

        int result = Checker.ApplyMove(board, from[0], from[1], dest[0], dest[1]);


        switch (result) {
            case Checker.ILLEGALMOVE:
                humanPiece = null;
                fromTile = null;
                toTile = null;
                completed = true;
                humanTurn = true;
                break;
            case Checker.LEGALMOVE:
                humanPiece.setName(toTile.getName());
                completed = true;
                move();
                if (multiplayer) {
                    int[] move = new int[]{from[0], from[1], dest[0], dest[1]};
                    moves.add(move);
                    updatePeer(moves);
                    moves.clear();
                }
                playerTurn = getPlayer();
                break;
            case Checker.INCOMLETEMOVE:
                move();
                fromTile = toTile;
                if (multiplayer) {
                    int[] move = new int[]{from[0], from[1], dest[0], dest[1]};
                    moves.add(move);
                }
                completed = false;
                break;
        }
    }

    protected void drawRect() {
        if (ready) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.RED);

            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(xx, yy, (cellsize / 2), (cellsize / 2));
            shapeRenderer.end();
        }
    }

    protected Piece getPiece(int x, int y) {

        Piece selected = null;
        float[] position = new float[2];
        position[0] = (cellsize * x);
        position[1] = (cellsize * y);

        Group pp = null;

        Actor a = boardStage.getActors().first();

        if (a instanceof Group) {
            pp = (Group) a;
        }

        Rectangle rect1 = new Rectangle(position[0] + (cellsize / 4),
                position[1] + boardPosition[1] + (cellsize / 4),
                cellsize / 2,
                cellsize / 2);

        xx = rect1.x;
        yy = rect1.y;

        SnapshotArray<Actor> actors = pp.getChildren();

        for (int i = 0; i < actors.size; i++) {
            if (actors.get(i) instanceof Piece) {
                selected = (Piece) actors.get(i);
                Rectangle rect2 = Util.getRectangleOfActor(selected);
                if (Intersector.overlaps(rect2, rect1)) {
                    break;
                } else
                    selected = null;
            }
        }

        return selected;
    }

    protected void moveOpponentPiece() {
        SequenceAction sequenceAction = new SequenceAction();
        int loser = Checker.EMPTY;
        int[] move = new int[4];
        int[] counter = new int[1];
        counter[0] = 0;

        GameEngine.MinMax(board, 0, level, move, playerTurn, counter);

        if (move[0] == 0 && move[1] == 0)
            loser = playerTurn;
        else {
            Checker.moveComputer(board, move);

            if (loser != Checker.EMPTY) {
                return;
            }

            this.toMove = playerTurn;

            if (Checker.noMovesLeft(board, toMove)) {
                gameOver("White");
                timer = false;
            }
        }
        ///////
        int startx = move[0];
        int starty = move[1];
        int endx = move[2];
        int endy = move[3];

        cpuPiece = getPiece(startx, starty);

        if (cpuPiece == null)
            return;
        else
            cpuPiece.setSelected(true);

        while (endx > 0 || endy > 0) {

            float posX = ((endx % 10) * cellsize) + 4;
            float posY = ((endy % 10) * cellsize + boardPosition[1]) + 4;

            xx = posX;
            yy = posY;

            ready = true;

            sequenceAction.addAction(delay(0.35f));
            sequenceAction.addAction(moveTo(posX, posY, 0.35f));

            endx /= 10;
            endy /= 10;
        }

        ready = false;

        sequenceAction.addAction(

                run(new Runnable() {
                        public void run() {

                            Gdx.app.log("PLAYER", "CPU FINISHED " + round++);

                            cpuPiece.setSelected(false);

                            cpuPiece.toBack();

                            drawPieces(8, 8);

                            playerTurn = getPlayer();

                            gameOver();

                            humanTurn = true;

                        }
                    }
                ));

        //update name
        if (cpuPiece != null) {
            cpuPiece.toFront();
            cpuPiece.addAction(sequenceAction);
        }

    }

    protected void drawPieces(int rows, int cols) {

        gameBoard.clear();
        gameBoard.setTouchable(Touchable.childrenOnly);

        Vector2[] position = new Vector2[rows * cols];

        Piece[] pieces = new Piece[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));

        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;

        int index, text = 0;
        int count = 1;

        float posY = (float) (0.20 * Constants.GAME_HEIGHT);

        gameBoard.setOrigin(0, posY);
        gameBoard.setWidth(Constants.GAME_WIDTH);
        float bHeight = (cellsize * rows) + (padding * 2);
        gameBoard.setHeight(bHeight);


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((row * cellsize) + padding,
                        padding + ((col * (cellsize)) + (Constants.GAME_HEIGHT * 0.20f)));

                if (board[row][col] == Checker.BLACKNORMAL)
                    pieces[index] = new Piece(Assets.img_pawn_black, Checker.BLACKNORMAL);
                if (board[row][col] == Checker.BLACKKING)
                    pieces[index] = new Piece(Assets.img_king_black, Checker.BLACKNORMAL);
                if (board[row][col] == Checker.WHITEKING)
                    pieces[index] = new Piece(Assets.img_king_white, Checker.WHITENORMAL);
                if (board[row][col] == Checker.WHITENORMAL)
                    pieces[index] = new Piece(Assets.img_pawn_white, Checker.WHITENORMAL);
                if (board[row][col] == Checker.EMPTY)
                    continue;

                text = (count += 2) / 2;

                pieces[index].setSize((cellsize - 2), (cellsize - 2));
                pieces[index].setPosition(position[index].x, position[index].y);
                pieces[index].setName(text + "");
                gameBoard.addActor(pieces[index]);
            }
        }
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

    public void save() {

        int minutes = (int) ((secondsTime / 60) % 60);

        //save after every 1 minutes
        if ((minutes - saveCounter) == 1) {

            GameObject obj = new GameObject();
            obj.setName("test");
            obj.setBoard(board);
            obj.setDate(new Date());
            obj.setMultiplayer(multiplayer);
            obj.setTurn(playerTurn);
            SaveUtil.save(obj);

            saveCounter++;
        }
    }

    private void renderHud(SpriteBatch batch) {

        float y = Constants.GAME_HEIGHT * 0.05f;

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

    public void gameOver(String text) {

        timer = false;

        final GameOver gameOver = new GameOver(text + " WIN!", getSkin()); // this is the dialog title
        gameOver.text("Game Over");
        gameOver.button("Yes", new InputListener() { // button to exit app
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameOver.hide();
                game.setScreen(new LevelScreen(game));
                return true;
            }
        });
        gameOver.button("No", new InputListener() { // button to exit app
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return false;
            }
        });
        gameOver.show(dialogStage); // actually show the dialog
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(Label infoLabel) {
        this.infoLabel = infoLabel;
    }

    @Override
    public void updatePeer(Vector move) {

        //Gdx.app.log("TRX", "OUT=> " + Checker.printboard(board));

        TransmissionPackage transmissionPackage = new TransmissionPackage();
        transmissionPackage.reset();
        transmissionPackage.setGameboard(board);
        transmissionPackage.setMove(move);
        transmissionPackage.setName("");
        game.getBluetoothInterface().transmitPackage(transmissionPackage);

    }

    @Override
    public void notify_PeerDataReceived(TransmissionPackage transmissionPackage) {
        updateBoard(transmissionPackage);
    }

    private void updateBoard(TransmissionPackage transmissionPackage) {

        copyBoard(transmissionPackage.getGameboard(), board);

        //Gdx.app.log("TRX", "IN => " + Checker.printboard(board));

        Vector mv = transmissionPackage.getMove();

        SequenceAction sequenceAction = new SequenceAction();
        int[] arr = (int[]) mv.get(0);
        int startx = arr[0];
        int starty = arr[1];
        int endx = arr[2];
        int endy = arr[3];

        cpuPiece = getPiece(startx, starty);

        if (cpuPiece == null) {
            Gdx.app.log("TRX", "PIECE IS NULL");
            drawPieces(8, 8);
            playerTurn = getPlayer();
            humanTurn = true;
            return;
        } else
            cpuPiece.setSelected(true);

        float posX = ((endx) * cellsize) + 4;
        float posY = ((endy) * cellsize + boardPosition[1]) + 4;

        sequenceAction.addAction(delay(0.35f));
        sequenceAction.addAction(moveTo(posX, posY, 0.35f));

        for (int i = 1; i < mv.size(); i++) {

            if (mv.get(i) == null)
                continue;

            arr = (int[]) mv.get(i);
            endx = arr[2];
            endy = arr[3];

            posX = ((endx) * cellsize) + 4;
            posY = ((endy) * cellsize + boardPosition[1]) + 4;

            sequenceAction.addAction(delay(0.35f));
            sequenceAction.addAction(moveTo(posX, posY, 0.35f));
        }

        sequenceAction.addAction(
                run(new Runnable() {
                        public void run() {
                            cpuPiece.setSelected(false);
                            cpuPiece.toBack();
                            drawPieces(8, 8);
                            playerTurn = getPlayer();
                            humanTurn = true;
                        }
                    }
                ));

        //update
        if (cpuPiece != null) {
            cpuPiece.toFront();
            cpuPiece.addAction(sequenceAction);
        }
    }

    public int getPlayer() {
        //Gdx.app.log("PLAYER", "CHANGING TURN =>" + round);
        return playerTurn == Checker.BLACKNORMAL ? Checker.WHITENORMAL : Checker.BLACKNORMAL;
    }
}
