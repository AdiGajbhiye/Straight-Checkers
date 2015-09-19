package com.pennywise.findwords.core.logic;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PuzzleGenerator {

    protected final Grid grid;

    protected List<String> _wordList;

    private Random random = new Random();

    public PuzzleGenerator(int width, int height, List<String> wordList) {

        this._wordList = wordList;

        grid = new Grid(new char[width][height]);

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                grid.set(x, y, '-');
            }
        }
    }

    public Grid generate() {
        addWords();
        fillPuzzle();
        //savePuzzle("Test");
        return grid;
    }

    public Grid getPuzzle() {
        return grid;
    }

    private char getCharAt(int x, int y) {
        char result = grid.at(x, y);
        return result;
    }

    private int getWidth() {
        return grid.width();
    }

    private int getHeight() {
        return grid.height();
    }

    private void addWords() {

        int triecount = 0;
        Direction direction;
        int dirLen = Direction.values().length;

        while (_wordList.size() != 0) {
            //put the first word from the list into a string
            String aWord = (String) _wordList.get(0).toUpperCase();
            //remove the word from the list
            _wordList.remove(0);
            boolean numTries = true;

            int count = Direction.values().length;

            while (numTries) {
                //Choose a random location on the puzzle to start placement of word
                int startRow = random.nextInt(getWidth());
                int startCol = random.nextInt(getHeight());
                int index = random.nextInt(count);
                direction = Direction.values()[index];

                if (!validMove(startRow, startCol, aWord.length()))
                    continue;

                switch (direction) {
                    case NORTH_SOUTH:
                        numTries = move(aWord, startRow, startCol, direction, 0, 1);
                        break;
                    case NORTHEAST_SOUTHWEST:
                        numTries = move(aWord, startRow, startCol, direction, 1, -1);
                        break;
                    case EAST_WEST:
                        numTries = move(aWord, startRow, startCol, direction, -1, 0);
                        break;
                    case SOUTHEAST_NORTHWEST:
                        numTries = move(aWord, startRow, startCol, direction, -1, 1);
                        break;
                    case SOUTH_NORTH:
                        numTries = move(aWord, startRow, startCol, direction, -1, 0);
                        break;
                    case SOUTHWEST_NORTHEAST:
                        numTries = move(aWord, startRow, startCol, direction, -1, -1);
                        break;
                    case WEST_EAST:
                        numTries = move(aWord, startRow, startCol, direction, 1, 0);
                        break;
                    case NORTHWEST_SOUTHEAST:
                        numTries = move(aWord, startRow, startCol, direction, 1, 1);
                        break;
                }

            }
        }
    }

    private boolean validMove(int startX, int startY, int wordlen) {

        if (startX < 0 || startX >= getWidth() || startY < 0 || startY >= getHeight())
            return false;

        if ((getWidth() - (startX)) < wordlen && (getHeight() - (startY)) < wordlen)
            return false;

        return true;
    }

    private boolean move(String theWord, int startX, int startY, Direction direction, int dx, int dy) {
        boolean result = false;
        //the initial char in question for placement
        int p = 0;
        int x = startX;
        int y = startY;

        while (p < theWord.length()) {

            if (grid.at(startX, startY) == '-' || grid.at(startX, startY) == theWord.charAt(p)) {
                startX += dx;
                startY += dy;

                if (startX < 0 || startX >= getWidth()) {
                    result = true;
                    break;
                }

                if (startY < 0 || startY >= getHeight()) {
                    result = true;
                    break;
                }
                p++;
            } else {
                result = true;
                break;
            }
        }

        if (!result) {
            place(theWord, x, y, dx, dy);
        }
        return result;
    }

    private void place(String theWord, int startX, int startY, int dx, int dy) {
        //the initial char in question for placement
        int p = 0;

        while (p < theWord.length()) {

            //********COMMENTED OUT DUE TO HANGING ISSUES**************
            grid.set(startX, startY, (char) theWord.charAt(p));

            //This is the wrapping logic
            startX = ((startX + dx + getWidth()) % getWidth());
            startY = ((startY + dy + getHeight()) % getHeight());
            p++;
        }
    }

    protected void fillPuzzle() {
        for (int h = 0; h < getHeight(); h++) {
            for (int w = 0; w < getWidth(); w++) {
                if (grid.at(w, h) == '-') {
                    //randomly generate a capital char (65-90) for fill
                    Character aChar;
                    do {
                        aChar = (char) (random.nextInt(90) + 65);
                    } while ((int) aChar < 65 || (int) aChar > 90);

                    grid.set(w, h, aChar);
                }
            }
        }
    }

    protected void savePuzzle(String puzzleName) {
        try {
            Grid.savePuzzle(puzzleName, grid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}