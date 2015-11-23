package com.pennywise.checkers.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.Assets;
import com.pennywise.Checkers;
import com.pennywise.checkers.core.Constants;
import com.pennywise.checkers.core.Util;
import com.pennywise.checkers.core.engine.Checker;
import com.pennywise.checkers.core.engine.Coord;
import com.pennywise.checkers.core.engine.GameEngine;
import com.pennywise.checkers.objects.Panel;
import com.pennywise.checkers.objects.Piece;
import com.pennywise.checkers.objects.Tile;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


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
    private Piece humanPiece = null;
    private Piece cpuPiece = null;
    private Tile fromTile;
    private Tile toTile;

    private boolean gameOver = false;
    String strTime = "";
    int count = 0;

    private Image pauseButton;
    private Checker engine;
    private long startTime = System.nanoTime();
    private long secondsTime = 0L;
    private BitmapFont hudFont;
    private boolean opponentMove = false;
    int toMove;

    int[][] board = new int[8][8];
    int[][] preBoard1 = new int[8][8];                 //for undo
    int preToMove1;
    int[][] preBoard2 = new int[8][8];
    int preToMove2;
    int[][] preBoard3 = new int[8][8];
    int preToMove3;
    private int undoCount = 0;

    public void newGame() {                            //creates a new game

        int[] b = new int[46];

        for (int i = 0; i < 46; i++)
            b[i] = Checker.OCCUPIED;

        for (int i = 5; i <= 40; i++)
            b[i] = Checker.EMPTY;

        for (int i = 5; i <= 17; i++)
            b[i] = Checker.BLACKPAWN;

        for (int i = 28; i <= 40; i++)
            b[i] = Checker.WHITEPAWN;

        for (int i = 9; i <= 36; i += 9)
            b[i] = Checker.OCCUPIED;

        board[0][0] = b[5];
        board[2][0] = b[6];
        board[4][0] = b[7];
        board[6][0] = b[8];
        board[1][1] = b[10];
        board[3][1] = b[11];
        board[5][1] = b[12];
        board[7][1] = b[13];
        board[0][2] = b[14];
        board[2][2] = b[15];
        board[4][2] = b[16];
        board[6][2] = b[17];
        board[1][3] = b[19];
        board[3][3] = b[20];
        board[5][3] = b[21];
        board[7][3] = b[22];
        board[0][4] = b[23];
        board[2][4] = b[24];
        board[4][4] = b[25];
        board[6][4] = b[26];
        board[1][5] = b[28];
        board[3][5] = b[29];
        board[5][5] = b[30];
        board[7][5] = b[31];
        board[0][6] = b[32];
        board[2][6] = b[33];
        board[4][6] = b[34];
        board[6][6] = b[35];
        board[1][7] = b[37];
        board[3][7] = b[38];
        board[5][7] = b[39];
        board[7][7] = b[40];

        Checker.printboard(board);



        for (int i = 0; i < 8; i++)                                  //applies values to the board
        {
            for (int j = 0; j < 8; j++)
                board[i][j] = Checker.EMPTY;

            for (int j = 0; j < 3; j++)
                if (isPossibleSquare(i, j))
                    board[i][j] = Checker.BLACKPAWN;

            for (int j = 5; j < 8; j++)
                if (isPossibleSquare(i, j))
                    board[i][j] = Checker.WHITEPAWN;
        }

        Checker.printboard(board);

        /*
        for (int i = 0; i < 8; i++)                                  //applies values to the board
        {
            for (int j = 0; j < 8; j++)
                board[i][j] = Checker.EMPTY;

            for (int j = 0; j < 3; j++)
   //             if (isPossibleSquare(i, j))
                    board[i][j] = Checker.BLACKPAWN;

            for (int j = 5; j < 8; j++)
   //             if (isPossibleSquare(i, j))
                    board[i][j] = Checker.WHITEPAWN;
        }

*/

        toMove = Checker.WHITEPAWN;

        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, preBoard1[i], 0, 8);                       //for undo
            System.arraycopy(preBoard1[i], 0, preBoard2[i], 0, 8);
            System.arraycopy(preBoard2[i], 0, preBoard3[i], 0, 8);
            preToMove3 = preToMove2 = preToMove1 = toMove;
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

        newGame();
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

        if (opponentMove) {
            opponentMove = false;
            new Thread("Opponent") {
                public void run() {


                    moveOpponentPiece();

                }
            }.start();
        }

        if (Gdx.input.isTouched() && !isBusy) {
            stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
            Actor actor = stage.hit(stageCoords.x, stageCoords.y, true);
            Actor actor2 = boardStage.hit(stageCoords.x, stageCoords.y, true);

            if (actor2 != null && actor2 instanceof Piece) {
                humanPiece = (Piece) actor2;
                humanPiece.setSelected(true);
                fromTile = (Tile) actor;
                if (actor instanceof Tile) {
                    Tile tile = (((Tile) actor));
                    if (tile.getCellEntry() == Checker.BLACK)
                        tile.getStyle().background = Assets.img_selected_cell_dark;
                    else
                        tile.getStyle().background = Assets.img_selected_cell_lite;
                }
            } else {

                if (humanPiece != null) {
                    if (actor instanceof Tile) {
                        toTile = ((Tile) actor);

                        if (toTile.getCellEntry() == Checker.BLACK) {
                            movePiece();
                        }
                    }
                }
            }
        }

        if (humanPiece != null)
            checkBlackPieceCollision(humanPiece);

        if (cpuPiece != null)
            checkWhitePieceCollision(cpuPiece);


        stage.act();
        stage.draw();

        boardStage.act();
        boardStage.draw();

        dialogStage.act();
        dialogStage.draw();

        renderHud(batch, delta);
        renderFPS(batch);

        removeCapturedPieces();
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
        boardStage.addActor(drawPieces(height, width, true));
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
        layer.addActor(board(height, width, true));
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

            for (int row = 0; row < rows; row++) {
                for (int col = 7; col >= 0; col--) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        style.background = Assets.img_cell_dark;
                        backgroundTiles[index] = new Tile(Checker.BLACK, new Label.LabelStyle(style));

                    } else {
                        text = 0;
                        style.background = Assets.img_cell_light;
                        backgroundTiles[index] = new Tile(Checker.WHITE, new Label.LabelStyle(style));
                    }

                    backgroundTiles[index].setSize(cellsize, cellsize);
                    backgroundTiles[index].setAlignment(Align.center);
                    backgroundTiles[index].setPosition(position[index].x, position[index].y);
                    backgroundTiles[index].setName(text + "");
                    backgroundTiles[index].setText(text + "");
                    panel.addActor(backgroundTiles[index]);
                }
            }
        } else {


            for (int row = 7; row >= 0; row--) {
                for (int col = 0; col < cols; col++) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        style.background = Assets.img_cell_dark;
                        backgroundTiles[index] = new Tile(Checker.BLACK, new Label.LabelStyle(style));

                    } else {
                        text = 0;
                        style.background = Assets.img_cell_light;
                        backgroundTiles[index] = new Tile(Checker.WHITE, new Label.LabelStyle(style));
                    }

                    backgroundTiles[index].setSize(cellsize, cellsize);
                    backgroundTiles[index].setAlignment(Align.center);
                    backgroundTiles[index].setPosition(position[index].x, position[index].y);
                    backgroundTiles[index].setText(text + "");
                    backgroundTiles[index].setName(text + "");
                    panel.addActor(backgroundTiles[index]);
                }
            }
        }
        return panel;


    }

    protected void move() {

        float posX = toTile.getX() + (toTile.getWidth() / 2);
        float posY = toTile.getY() + (toTile.getHeight() / 2);

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(posX, posY, Align.center);
        moveAction.setDuration(0.5f);
        humanPiece.toFront();
        humanPiece.addAction(sequence(moveAction, run(new Runnable() {
            public void run() {
                humanPiece.toBack();
                humanPiece.setSelected(false);
                if (isKingTile(toTile, humanPiece.getPlayer()) &&
                        !humanPiece.isKing()) {
                    crownPiece(humanPiece);
                }
                isBusy = false;
                opponentMove = true;
            }
        })));
    }

    protected boolean isKingTile(Tile tile, int color) {

        if (color == Checker.BLACK) {
            //32  31  30  29
            if (tile.getName().equals("32")
                    || tile.getName().equals("31")
                    || tile.getName().equals("30")
                    || tile.getName().equals("29")) {
                return true;
            }
        } else {
            //4   3   2   1
            if (tile.getName().equals("1")
                    || tile.getName().equals("2")
                    || tile.getName().equals("3")
                    || tile.getName().equals("4")) {
                return true;
            }
        }

        return false;
    }

    private boolean isMoveLegal(Piece piece, int from, int to) {
        if (piece.isKing())
            return true;

        if (((to - from) < 0))
            return false;

        return true;
    }

    private void checkBlackPieceCollision(Piece piece) {


        if (piece.isSelected())
            //find the piece
            for (Actor a : boardStage.getActors()) {
                if (a instanceof Group) {
                    for (Actor actor : ((Group) a).getChildren()) {
                        if (actor instanceof Piece) {
                            Piece p = (Piece) actor;
                            if (p.isSelected())
                                continue;
                            if ((p.getPlayer() == Checker.WHITE)) {
                                if (Util.isActorCollide(piece, p) && !p.isCaptured()) {
                                    p.setCaptured(true);
                                }
                            }
                        }
                    }
                }
            }

    }

    private void checkWhitePieceCollision(Piece piece) {

        if (piece.isSelected()) {

            for (Actor a : boardStage.getActors()) {
                if (a instanceof Group) {
                    for (Actor actor : ((Group) a).getChildren()) {
                        if (actor instanceof Piece) {
                            Piece p = (Piece) actor;
                            if (p.isSelected())
                                continue;
                            if ((p.getPlayer() == Checker.BLACK)) {
                                if (Util.isActorCollide(piece, p) && !p.isCaptured()) {
                                    p.setCaptured(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected synchronized void removeCapturedPieces() {
        for (Actor a : boardStage.getActors()) {
            if (a instanceof Group) {
                for (Actor actor : ((Group) a).getChildren()) {
                    if (actor instanceof Piece) {
                        Piece p = (Piece) actor;
                        if (p.isCaptured()) {
                            p.remove();
                        }
                    }
                }
            }
        }
    }

    protected void movePiece() {

        isBusy = true;

        int from = Integer.parseInt(fromTile.getName());
        int to = Integer.parseInt(toTile.getName());

        Coord src = Util.numbertocoors(from);
        Coord dest = Util.numbertocoors(to);

        int result = Checker.ApplyMove(board, src.x, src.y, dest.x, dest.y);

        switch (result) {
            case Checker.ILLEGALMOVE:
                break;
            case Checker.LEGALMOVE:
                move();
                break;
            case Checker.INCOMLETEMOVE:
                break;
        }
    }

    protected void crownPiece(Piece piece) {

        if (piece.getPlayer() == Checker.BLACK)
            piece.setDrawable(Assets.img_king_black);
        else
            piece.setDrawable(Assets.img_king_white);

        piece.Knight(true);
    }

    protected Tile getTile(String name) {

        for (Tile t : backgroundTiles) {
            if (t.getName().equalsIgnoreCase(name))
                return t;
        }

        return null;
    }

    protected Piece getPiece(int n) {

        Actor a = boardStage.getRoot();
        Actor actor = null;
        if (a instanceof Group) {
            Group g = ((Group) a);
            actor = g.findActor(String.valueOf(n));
        }

        return ((Piece) actor);
    }

    protected void moveOpponentPiece() {
        Tile srcTile;
        String srcName = "";
        Tile destTile = null;
        SequenceAction sequenceAction = new SequenceAction();


        int tempScore, loser = Checker.EMPTY;
        int[] move = new int[4];
        int[] counter = new int[1];
        counter[0] = 0;

        tempScore = GameEngine.MinMax(board, 0, 4, move, Checker.BLACK, counter);

        if (move[0] == 0 && move[1] == 0)
            loser = Checker.BLACK;
        else {
            Checker.moveComputer(board, move);
            if (loser == Checker.EMPTY) {
                return;
            }
            this.toMove = Checker.BLACK;
        }

        ///////

        int startx = move[0];
        int starty = move[1];
        int endx = move[2];
        int endy = move[3];

        int from = Util.coordstonumber(startx, starty);
        cpuPiece = getPiece(from);

        if (cpuPiece == null)
            return;
        else
            cpuPiece.setSelected(true);

        while (endx > 0 || endy > 0) {

            int to = Util.coordstonumber(endx % 10, endy % 10);

            destTile = getTile(to + "");
            cpuPiece.setName(to + "");

            float posX = destTile.getX();
            float posY = destTile.getY();

            sequenceAction.addAction(delay(0.5f));
            sequenceAction.addAction(moveTo(posX, posY, 0.5f));

            startx = endx % 10;
            starty = endy % 10;
            endx /= 10;
            endy /= 10;
        }

        final Tile end = destTile;

        sequenceAction.addAction(

                run(new Runnable() {
                        public void run() {
                            cpuPiece.setSelected(false);
                            cpuPiece.toBack();
                            if (isKingTile(end, cpuPiece.getPlayer()) &&
                                    !cpuPiece.isKing()) {
                                crownPiece(cpuPiece);
                            }
                        }
                    }

                ));

        //update name
        if (cpuPiece != null)

        {
            cpuPiece.toFront();
            cpuPiece.addAction(sequenceAction);
        }

    }

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


            for (int row = 0; row < rows; row++) {
                for (int col = 7; col >= 0; col--) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        if (row >= 5)
                            pieces[index] = new Piece(Assets.img_pawn_white, Checker.WHITE);
                        else if (row < 3)
                            pieces[index] = new Piece(Assets.img_pawn_black, Checker.BLACK);

                        if (pieces[index] == null)
                            continue;
                        pieces[index].setSize(cellsize, cellsize);
                        pieces[index].setPosition(position[index].x, position[index].y);
                        pieces[index].setName(text + "");
                        board.addActor(pieces[index]);
                    }
                }
            }

        } else {
            for (int row = 7; row >= 0; row--) {
                for (int col = 0; col < cols; col++) {
                    index = col + (row * cols);
                    position[index] = new Vector2((col * cellsize) + padding,
                            padding + ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.25f)));

                    if ((row % 2) == (col % 2)) {
                        text = (count += 2) / 2;
                        if (row >= 5)
                            pieces[index] = new Piece(Assets.img_pawn_black, Checker.BLACK);
                        else if (row < 3)
                            pieces[index] = new Piece(Assets.img_pawn_white, Checker.WHITE);

                        if (pieces[index] == null)
                            continue;

                        pieces[index].setSize(cellsize, cellsize);
                        pieces[index].setPosition(position[index].x, position[index].y);
                        pieces[index].setName(index + "");
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
