package com.pennywise.checkers.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.core.engine.CBMove;
import com.pennywise.checkers.core.engine.Point;
import com.pennywise.checkers.core.engine.Simple;
import com.pennywise.checkers.core.persistence.GameObject;
import com.pennywise.checkers.core.persistence.Player;
import com.pennywise.checkers.core.persistence.SaveUtil;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;
import com.pennywise.checkers.screens.dialogs.GameOver;
import com.pennywise.managers.MultiplayerDirector;
import com.pennywise.multiplayer.BluetoothInterface;
import com.pennywise.multiplayer.TransmissionPackage;
import com.pennywise.multiplayer.TransmissionPackagePool;

import java.util.Date;

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


    private Group gameBoard;
    private long startTime = 0;
    private boolean timer = false;
    private long secondsTime = 0L;
    private BitmapFont hudFont;
    private boolean opponentMove = false;
    double level = Constants.EASY;

    int[][] board = new int[8][8];
    private int undoCount = 0;
    private int[] pieceCount = new int[2];
    private int saveCounter = 0;

    private boolean multiplayer = false;
    private float[] boardPosition = null;
    private int playerTurn = Simple.BLACK;
    private int round = 0;
    private Image blackTurn;
    private Image whiteTurn;
    private int winner;
    private boolean multicapture = false;
    private Point from = null, dest = null;
    private Player player;
    private Label blackName, whiteName;
    protected BluetoothInterface bluetoothInterface;
    private boolean firstTransmission = true;
    private boolean firstReception = true;

    public void newGame() {                            //creates a new game

        initBoard();

    }

    void initBoard() {
        // initialize board to starting position

        int i, j;

        for (i = 0; i <= 7; i++) {
            for (j = 0; j <= 7; j++) {
                board[i][j] = Simple.FREE;
            }
        }

        board[0][0] = Simple.BLACKPAWN;
        board[2][0] = Simple.BLACKPAWN;
        board[4][0] = Simple.BLACKPAWN;
        board[6][0] = Simple.BLACKPAWN;
        board[1][1] = Simple.BLACKPAWN;
        board[3][1] = Simple.BLACKPAWN;
        board[5][1] = Simple.BLACKPAWN;
        board[7][1] = Simple.BLACKPAWN;
        board[0][2] = Simple.BLACKPAWN;
        board[2][2] = Simple.BLACKPAWN;
        board[4][2] = Simple.BLACKPAWN;
        board[6][2] = Simple.BLACKPAWN;

        board[1][7] = Simple.WHITEPAWN;
        board[3][7] = Simple.WHITEPAWN;
        board[5][7] = Simple.WHITEPAWN;
        board[7][7] = Simple.WHITEPAWN;
        board[0][6] = Simple.WHITEPAWN;
        board[2][6] = Simple.WHITEPAWN;
        board[4][6] = Simple.WHITEPAWN;
        board[6][6] = Simple.WHITEPAWN;
        board[1][5] = Simple.WHITEPAWN;
        board[3][5] = Simple.WHITEPAWN;
        board[5][5] = Simple.WHITEPAWN;
        board[7][5] = Simple.WHITEPAWN;
    }


    private void clearBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = Simple.FREE;
    }

    private void copyBoard(int[][] src, int[][] dst) {
        for (int i = 0; i < 8; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, 8);                       //for undo
        }
    }

    private boolean isPossibleSquare(int i, int j) {
        return ((i % 2) == (j % 2));
    }

    public GameScreen(Checkers game, double difficulty) {
        super(game);

        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); //  flip y-axis
        camera.update();

        hudCam = new OrthographicCamera();
        hudCam.position.set(0, 0, 0);
        hudCam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT); // flip y-axis
        hudCam.update();

        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        dialogStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        boardStage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));

        this.level = difficulty;

        transmissionPackagePool = new TransmissionPackagePool();
        bluetoothInterface = game.getBluetoothInterface();

        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(this, dialogStage));

        hudFont = Util.loadFont("fonts/Roboto-Regular.ttf", 32, Color.BLACK);

        blackTurn = new Image(getSkin().getDrawable("red_dot"));
        whiteTurn = new Image(getSkin().getDrawable("grey_dot"));

        width = 8;
        height = 8;
        //gridHeight = ((Constants.GAME_HEIGHT * 3) / 4);
        cellsize = ((Constants.GAME_WIDTH - width) / (width));

        batch = new SpriteBatch();

        initGame();
    }

    protected void initGame() {

        multiplayer = game.isMultiplayer();
        player = SaveUtil.loadUserData(Constants.USER_FILE);

        whiteName = new Label("Player name:", getSkin(), "black-text");
        blackName = new Label("Player name:", getSkin(), "black-text");

        if (multiplayer) {
            if (player.isHost()) {
                if (player.getColor() == Simple.BLACK) {
                    blackName.setText(player.getName());
                    playerTurn = humanPlayer = Simple.BLACK;
                    humanTurn = true;
                } else {
                    humanTurn = false;
                    playerTurn = Simple.BLACK;
                    humanPlayer = Simple.WHITE;
                    whiteName.setText(player.getName());
                }

            } else {
                playerTurn = Simple.BLACK;
                humanPlayer = Simple.WHITE;
                whiteName.setText(player.getName());
                humanTurn = false;
            }
        } else {
            humanPlayer = playerTurn;
            whiteName.setText("Droid");
            blackName.setText(player.getName().isEmpty() ? "Human" : player.getName());
        }

        newGame();

        setupScreen();

        timer = true;
    }

    private final Vector2 stageCoords = new Vector2();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched() && humanTurn) {
            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));

            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);

            if (completed) {
                Actor actor2 = boardStage.hit(stageCoords.x, stageCoords.y, true);

                if (actor2 != null && actor2 instanceof Piece) {
                    humanPiece = (Piece) actor2;
                    if ((humanPiece.getPlayer() & humanPlayer) != 0) {
                        humanPiece.setSelected(true);
                        if (actor instanceof Tile) {
                            fromTile = (Tile) actor;
                        }
                    } else
                        humanPiece = null;
                } else {
                    if (humanPiece != null && actor != null) {
                        if (actor instanceof Tile) {

                            toTile = ((Tile) actor);

                            if (toTile.getCellEntry() == Simple.BLACK) {
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
                        if (actor instanceof Tile) {
                            toTile = ((Tile) actor);
                            if (toTile.getCellEntry() == Simple.BLACK) {
                                movePiece();
                            }
                        }
                    } else {
                        Gdx.app.log("Tiles", "ILLEGAL MOVE");
                    }
                }
            }
        }

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

        if (gameOver) {
            gameOver = false;
            showGameOver();
        }

        //switch lights
        if (playerTurn == Simple.WHITE) {
            whiteTurn.setDrawable(getSkin(), "red_dot");
            blackTurn.setDrawable(getSkin(), "grey_dot");
        } else {
            whiteTurn.setDrawable(getSkin(), "grey_dot");
            blackTurn.setDrawable(getSkin(), "red_dot");
        }

        stage.act();
        stage.draw();

        boardStage.act();
        boardStage.draw();

        dialogStage.act();
        dialogStage.draw();

        renderHud(batch);

        save();

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
        super.dispose();
        transmissionPackagePool.dispose();
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

        blackName.setAlignment(Align.center);
        layer.add(blackTurn).size(30, 30).center().padBottom(80);
        layer.add(blackName).size(320, 60).left().padBottom(80);
        layer.row();
        return layer;
    }

    private Table opponentHud() {
        Table layer = new Table();
        //layer.setDebug(true);
        layer.top();
        layer.padTop(75);
        layer.setWidth(Constants.GAME_WIDTH);


        whiteName.setAlignment(Align.center);
        layer.add(whiteTurn).size(30, 30).center().padTop(5);
        layer.add(whiteName).size(320, 60).left().padTop(5);
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

        panel = new Panel(getSkin().getPatch("panel_brown"));
        panel.setTouchable(Touchable.childrenOnly);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = hudFont;
        style.background = getSkin().getDrawable("line_dark");

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
                    style.background = getSkin().getDrawable("darkcell");//Assets.img_cell_dark;
                    backgroundTiles[index] = new Tile(Simple.BLACK, new Label.LabelStyle(style));

                } else {
                    text = 0;
                    style.background = getSkin().getDrawable("litecell");//Assets.img_cell_light;
                    backgroundTiles[index] = new Tile(Simple.WHITE, new Label.LabelStyle(style));
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
                    humanTurn = false;
                    playerTurn = getPlayer();
                    opponentMove = true;

                }
            }
        })));
    }

    protected void movePiece() {

        if (!multicapture)
            from = Util.getIndex(fromTile.getX() - boardPosition[0], fromTile.getY() - boardPosition[1], cellsize);

        dest = Util.getIndex(toTile.getX() - boardPosition[0], toTile.getY() - boardPosition[1], cellsize);

        CBMove move = new CBMove();

        int result = Simple.isLegal(board, playerTurn, Util.coorstonumber(from.x, from.y), Util.coorstonumber(dest.x, dest.y), move);

        Gdx.app.log("RESULT Dump", "" + result);

        if (result == Simple.LEGAL) {
            humanPiece.setName(toTile.getName());
            completed = true;
            multicapture = false;
            Util.dumpMove(move);
            move();

            if (multiplayer) {
                updatePeer(move);
            }

        } else if (result == Simple.INCOMLETEMOVE) {
            System.out.println("INCOMPLETE");
            multicapture = true;
            move();
            completed = false;
        } else {
            humanPiece = null;
            fromTile = null;
            toTile = null;
            completed = true;
            humanTurn = true;
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
        int[] counter = new int[1];
        counter[0] = 0;
        String str = "";

        CBMove cbMove = new CBMove();

        final int result = Simple.getmove(board, playerTurn, level, str, cbMove);

        if ((playerTurn == Simple.BLACK && result == -200000)
                || (playerTurn == Simple.BLACK && result == Simple.NOLEGALMOVE)) {
            winner = Simple.WHITE;
            gameOver = true;
            timer = false;
        }

        if (playerTurn == Simple.WHITE && result == -100000
                || (playerTurn == Simple.WHITE && result == Simple.NOLEGALMOVE)) {
            winner = Simple.BLACK;
            gameOver = true;
            timer = false;
        }

        if (cbMove.from == null || cbMove.to == null)
            return;

        int startx = cbMove.from.x;
        int starty = cbMove.from.y;

        cpuPiece = getPiece(startx, starty);

        if (cpuPiece == null)
            return;
        else
            cpuPiece.setSelected(true);

        Util.dumpMove(cbMove);

        for (int i = 0; i < cbMove.path.length; i++) {

            if (cbMove.path[i] == null)
                continue;

            //float posX = toTile.getX() + (toTile.getWidth() / 2) ;
            //float posY = toTile.getY() + (toTile.getHeight() / 2);

            float posX = (cbMove.path[i].x * cellsize) + 4;
            float posY = (cbMove.path[i].y * cellsize + boardPosition[1]) + 4;

            sequenceAction.addAction(delay(0.35f));
            sequenceAction.addAction(moveTo(posX, posY, 0.35f));
        }

        sequenceAction.addAction(

                run(new Runnable() {
                        public void run() {

                            Gdx.app.log("PLAYER", "CPU FINISHED " + round++);

                            cpuPiece.setSelected(false);

                            cpuPiece.toBack();

                            drawPieces(8, 8);

                            if (((playerTurn == Simple.BLACK) && (result == 200000))) {
                                winner = Simple.BLACK;
                                gameOver = true;
                                timer = false;
                            }

                            if (((playerTurn == Simple.WHITE) && (result == 100000))) {
                                winner = Simple.WHITE;
                                gameOver = true;
                                timer = false;
                            }

                            playerTurn = getPlayer();

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


                if (board[row][col] == (Simple.BLACKPAWN))
                    pieces[index] = new Piece(getSkin().getDrawable("blackPawn"), (Simple.BLACKPAWN));
                if (board[row][col] == (Simple.BLACKKING))
                    pieces[index] = new Piece(getSkin().getDrawable("blackKing"), (Simple.BLACKKING));
                if (board[row][col] == (Simple.WHITEKING))
                    pieces[index] = new Piece(getSkin().getDrawable("redKing"), (Simple.WHITEKING));
                if (board[row][col] == (Simple.WHITEPAWN))
                    pieces[index] = new Piece(getSkin().getDrawable("redPawn"), (Simple.WHITEPAWN));
                if (board[row][col] == Simple.FREE)
                    continue;

                text = (count += 2) / 2;

                if (pieces[index] == null)
                    continue;

                pieces[index].setSize((cellsize - 4), (cellsize - 4));
                pieces[index].setPosition(position[index].x + 2, position[index].y + 2);
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

        float y = Constants.GAME_HEIGHT * 0.95f;

        strTime = getScreenTime();

        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();
        hudFont.draw(batch, strTime, 15, y);
        batch.end();
    }

    public void showGameOver() {

        String text = "";

        if (humanPlayer == winner)
            text = "You Win!";
        else
            text = "Droid Wins!";


        final GameOver gameOver = new GameOver(text, getSkin()); // this is the dialog title
        gameOver.text("Game Over, Play again?");

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
    public void updatePeer(CBMove move) {

        TransmissionPackage transmissionPackage = transmissionPackagePool.obtain();
        transmissionPackage.setGameboard(board);

        if (firstTransmission) {
            transmissionPackage.setName(game.getBluetoothInterface().getName());
            transmissionPackage.setColor(player.getColor());
            firstTransmission = false;
        }

        transmissionPackage.setMove(move);
        bluetoothInterface.transmitPackage(transmissionPackage);
        transmissionPackagePool.free(transmissionPackage);
    }

    @Override
    public void notify_PeerDataReceived(TransmissionPackage transmissionPackage) {
        updateBoard(transmissionPackage);
    }

    private void updateBoard(TransmissionPackage transmissionPackage) {

        if (firstReception) {
            if (transmissionPackage.getColor() == Simple.BLACK) {
                blackName.setText(transmissionPackage.getName());
                whiteName.setText(player.getName());
            } else {
                whiteName.setText(transmissionPackage.getName());
                blackName.setText(player.getName());
            }
            firstReception = false;
        }

        copyBoard(transmissionPackage.getGameboard(), board);

        CBMove cbMove = transmissionPackage.getMove();

        SequenceAction sequenceAction = new SequenceAction();
        if (cbMove.from == null || cbMove.to == null)
            return;

        int startX = cbMove.from.x;
        int startY = cbMove.from.y;

        cpuPiece = getPiece(startX, startY);

        if (cpuPiece == null) {
            drawPieces(8, 8);
            playerTurn = getPlayer();
            humanTurn = true;
            return;
        } else
            cpuPiece.setSelected(true);

        for (
                int i = 0;
                i < cbMove.path.length; i++)

        {

            if (cbMove.path[i] == null)
                continue;

            float posX = (cbMove.path[i].x * cellsize) + 4;
            float posY = (cbMove.path[i].y * cellsize + boardPosition[1]) + 4;

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
        if (cpuPiece != null)

        {
            cpuPiece.toFront();
            cpuPiece.addAction(sequenceAction);
        }

    }

    public int getPlayer() {
        return (playerTurn == Simple.BLACK) ? Simple.WHITE : Simple.BLACK;
    }

    @Override
    public void keyBackPressed() {
        super.keyBackPressed();
        game.setScreen(new LevelScreen(game));
    }
}
