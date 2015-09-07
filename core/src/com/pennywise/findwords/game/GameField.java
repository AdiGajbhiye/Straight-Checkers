package com.pennywise.findwords.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.FindWords;

import java.util.Random;

public class GameField {

    public FindWords game;

    public boolean gameOver = false;

    private Stage stage;
    private Stage backgroundStage;

    int[][] g = new int[3][3];

    public Vector2[][] field = new Vector2[4][4];
    public SaveCell[][] saveCellArray = new SaveCell[4][4];
    public Cell[][] cellArray = new Cell[4][4];
    public Cell[][] privCellArray = new Cell[4][4];
    public BackgroundCell[][] background = new BackgroundCell[4][4];
    private final float cellSize;
    private final float MARGIN;

    private Random random;

    public GameField(FindWords game) {
        this.game = game;

        random = new Random();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        backgroundStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        cellSize = Gdx.graphics.getWidth() / (field.length) * 400 / 480;
        MARGIN = (Gdx.graphics.getWidth() - (field.length) * cellSize) / (field.length + 1);

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = new Vector2(i * cellSize + (i + 1) * MARGIN, (j * cellSize + (j + 1) * MARGIN) + (Gdx.graphics.getHeight() - Gdx.graphics.getWidth()));
                background[i][j] = new BackgroundCell(game);
                background[i][j].setSize(cellSize, cellSize);
                background[i][j].setPosition(field[i][j].x, field[i][j].y);
                background[i][j].update();
                backgroundStage.addActor(background[i][j]);
            }
        }
    }

    public void newGame() {
        createCell();
        createCell();
    }

    public void continueGame() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (saveCellArray[i][j] == null) {
                    cellArray[i][j] = null;
                    continue;
                }
                cellArray[i][j] = new Cell(game);
                cellArray[i][j].setSize(cellSize, cellSize);
                cellArray[i][j].setPosition(saveCellArray[i][j].position.x, saveCellArray[i][j].position.y);
                cellArray[i][j].setValue(saveCellArray[i][j].value);
                stage.addActor(cellArray[i][j]);
            }
        }
    }

    public void save() {
        game.save();
    }

    public boolean isBusy(int i, int j) {
        if (cellArray[i][j] != null)
            return !cellArray[i][j].delete;
        return false;
    }

    public void createCell() {
        if (cellsIsBusy()) {
            return;
        }
        int i, j;
        do {
            i = random.nextInt(cellArray.length);
            j = random.nextInt(cellArray.length);
        } while (isBusy(i, j));
        cellArray[i][j] = new Cell(game);
        cellArray[i][j].setSize(cellSize, cellSize);
        cellArray[i][j].setPosition(field[i][j].x, field[i][j].y);
        cellArray[i][j].update(Math.random() < 0.9 ? 2 : 4);
        stage.addActor(cellArray[i][j]);
    }

    public boolean isSame(int i, int j, int di, int dj) {
        return cellArray[i][j].getValue() == cellArray[di][dj].getValue();
    }

    public boolean cellsIsBusy() {
        boolean notNull = true;
        for (int i = cellArray.length - 1; i >= 0; i--)
            for (int j = cellArray.length - 1; j >= 0; j--) {
                if (cellArray[i][j] != null)
                    notNull &= !cellArray[i][j].delete;
                else return false;
            }
        return notNull;
    }

    public boolean canMove() {
        if (!cellsIsBusy()) return true;
        boolean can = false;
        for (int i = cellArray.length - 1; i >= 0; i--)
            for (int j = cellArray.length - 1; j >= 0; j--) {
                if (i - 1 >= 0)
                    can |= cellArray[i][j].getValue() == cellArray[i - 1][j].getValue();
                if (i + 1 <= cellArray.length - 1)
                    can |= cellArray[i][j].getValue() == cellArray[i + 1][j].getValue();
                if (j - 1 >= 0)
                    can |= cellArray[i][j].getValue() == cellArray[i][j - 1].getValue();
                if (j + 1 <= cellArray.length - 1)
                    can |= cellArray[i][j].getValue() == cellArray[i][j + 1].getValue();
            }
        return can;
    }

    public void moveUp() {
        boolean flag = false;
        boolean flagMusic = false;
        backup();
        for (int i = cellArray.length - 1; i >= 0; i--) {
            for (int j = cellArray.length - 1; j >= 0; j--) {
                if (!isBusy(i, j)) continue;
                if (j >= cellArray.length - 1) continue;
                int j1 = j + 1;
                while (j1 <= cellArray.length - 1) {
                    if (isBusy(i, j1)) {
                        if (cellArray[i][j1].combined) break;
                        if (isSame(i, j1 - 1, i, j1)) {
                            cellArray[i][j1].same = true;
                            cellArray[i][j1].update(cellArray[i][j1].getValue() * 2);
                            cellArray[i][j1].combined = true;
                            cellArray[i][j1 - 1].setPosition(cellArray[i][j1].getX(), cellArray[i][j1].getY());
                            cellArray[i][j1 - 1].delete = true;
                            game.score += cellArray[i][j1].getValue();
                            flagMusic = true;
                            flag = true;
                        }
                        break;
                    }
                    cellArray[i][j1 - 1].setPosition(field[i][j].x, field[i][j1].y);
                    cellArray[i][j1 - 1].update(cellArray[i][j1 - 1].getValue());
                    cellArray[i][j1] = cellArray[i][j1 - 1];
                    cellArray[i][j1 - 1] = null;
                    j1 += 1;
                    flag = true;
                }
            }
        }
        if (flag) {
            if (flagMusic) game.playSame();
            else game.playSwipe();
            createCell();
        }
        reBuild();
    }

    public void moveDown() {
        boolean flag = false;
        boolean flagMusic = false;
        backup();
        for (int j = 0; j < cellArray.length; j++) {
            for (int i = 0; i < cellArray.length; i++) {
                if (!isBusy(i, j)) continue;
                if (j <= 0) continue;
                int j1 = j - 1;
                while (j1 >= 0) {
                    if (isBusy(i, j1)) {
                        if (cellArray[i][j1].combined) break;
                        if (isSame(i, j1 + 1, i, j1)) {
                            cellArray[i][j1].same = true;
                            cellArray[i][j1].update(cellArray[i][j1].getValue() * 2);
                            cellArray[i][j1].combined = true;
                            cellArray[i][j1 + 1].setPosition(cellArray[i][j1].getX(), cellArray[i][j1].getY());
                            cellArray[i][j1 + 1].delete = true;
                            game.score += cellArray[i][j1].getValue();
                            flagMusic = true;
                            flag = true;
                        }
                        break;
                    }
                    cellArray[i][j1 + 1].setPosition(field[i][j1 + 1].x, field[i][j1].y);
                    cellArray[i][j1 + 1].update(cellArray[i][j1 + 1].getValue());
                    cellArray[i][j1] = cellArray[i][j1 + 1];
                    cellArray[i][j1 + 1] = null;
                    j1 -= 1;
                    flag = true;
                }
            }
        }
        if (flag) {
            if (flagMusic) game.playSame();
            else game.playSwipe();
            createCell();
        }
        reBuild();
    }

    public void moveRight() {
        boolean flag = false;
        boolean flagMusic = false;
        backup();
        for (int i = cellArray.length - 1; i >= 0; i--) {
            for (int j = cellArray.length - 1; j >= 0; j--) {
                if (!isBusy(i, j)) continue;
                if (i >= cellArray.length - 1) continue;
                int i1 = i + 1;
                while (i1 <= cellArray.length - 1) {
                    if (isBusy(i1, j)) {
                        if (cellArray[i1][j].combined) break;
                        if (isSame(i1 - 1, j, i1, j)) {
                            cellArray[i1][j].same = true;
                            cellArray[i1][j].update(cellArray[i1][j].getValue() * 2);
                            cellArray[i1][j].combined = true;
                            cellArray[i1 - 1][j].setPosition(cellArray[i1][j].getX(), cellArray[i1][j].getY());
                            cellArray[i1 - 1][j].delete = true;
                            game.score += cellArray[i1][j].getValue();
                            flagMusic = true;
                            flag = true;
                        }
                        break;
                    }
                    cellArray[i1 - 1][j].setPosition(field[i1][j].x, field[i1 - 1][j].y);
                    cellArray[i1 - 1][j].update(cellArray[i1 - 1][j].getValue());
                    cellArray[i1][j] = cellArray[i1 - 1][j];
                    cellArray[i1 - 1][j] = null;
                    i1 += 1;
                    flag = true;
                }
            }
        }
        if (flag) {
            if (flagMusic) game.playSame();
            else game.playSwipe();
            createCell();
        }
        reBuild();
    }

    public void moveLeft() {
        boolean flag = false;
        boolean flagMusic = false;
        backup();
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray.length; j++) {
                if (!isBusy(i, j)) continue;
                if (i <= 0) continue;
                int i1 = i - 1;
                while (i1 >= 0) {
                    if (isBusy(i1, j)) {
                        if (cellArray[i1][j].combined) break;
                        if (isSame(i1 + 1, j, i1, j)) {
                            cellArray[i1][j].same = true;
                            cellArray[i1][j].update(cellArray[i1][j].getValue() * 2);
                            cellArray[i1][j].combined = true;
                            cellArray[i1 + 1][j].setPosition(cellArray[i1][j].getX(), cellArray[i1][j].getY());
                            cellArray[i1 + 1][j].delete = true;
                            game.score += cellArray[i1][j].getValue();
                            flagMusic = true;
                            flag = true;
                        }
                        break;
                    }
                    cellArray[i1 + 1][j].setPosition(field[i1][j].x, field[i1 + 1][j].y);
                    cellArray[i1 + 1][j].update(cellArray[i1 + 1][j].getValue());
                    cellArray[i1][j] = cellArray[i1 + 1][j];
                    cellArray[i1 + 1][j] = null;
                    i1 -= 1;
                    flag = true;
                }
            }
        }
        if (flag) {
            if (flagMusic) game.playSame();
            else game.playSwipe();
            createCell();
        }
        reBuild();
    }

    public void reBuild() {
        stage.clear();
        for (int i = 0; i < cellArray.length; i++)
            for (int j = 0; j < cellArray.length; j++) {
                saveCellArray[i][j] = null;
                if (cellArray[i][j] != null) {
                    if (cellArray[i][j].delete && cellArray[i][j].isTruePosition()) {
                        cellArray[i][j] = null;
                        continue;
                    }
                    cellArray[i][j].combined = false;
                    stage.addActor(cellArray[i][j]);
                    if (!cellArray[i][j].delete)
                        saveCellArray[i][j] = new SaveCell(cellArray[i][j].getPosition(), cellArray[i][j].getValue());
                }
            }
    }

    public void backup() {
        for (int i = 0; i < cellArray.length; i++)
            for (int j = 0; j < cellArray.length; j++) {
                privCellArray[i][j] = cellArray[i][j];
            }
    }

    public void back() {
        for (int i = 0; i < cellArray.length; i++)
            for (int j = 0; j < cellArray.length; j++) {
                cellArray[i][j] = privCellArray[i][j];
            }
        reBuild();
    }

    private final Vector2 finalAlpha = new Vector2(0.1f, 0);
    private Vector2 currentAlpha = new Vector2(1f, 0);

    public void fadeOut() {
        currentAlpha.lerp(finalAlpha, 0.1f);
        backgroundStage.clear();
        for (int i = 0; i < cellArray.length; i++)
            for (int j = 0; j < cellArray.length; j++) {
                if (cellArray[i][j] != null)
                    cellArray[i][j].getColor().a = currentAlpha.x;
            }
    }

    public void render(SpriteBatch batch) {
        reBuild();
        if (!canMove()) {
            gameOver = true;
        }
        if (gameOver) {
            fadeOut();
        }
        stage.act();
        backgroundStage.draw();
        stage.draw();
    }

}
