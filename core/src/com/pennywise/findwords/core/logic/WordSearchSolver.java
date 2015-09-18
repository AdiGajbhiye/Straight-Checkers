package com.pennywise.findwords.core.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


// WordSearch class interface: solve word search puzzle
//
// CONSTRUCTION: with no initializer
// ******************PUBLIC OPERATIONS******************
// int solvePuzzle( )   --> Print all words found in the
//                          puzzle; return number of matches


public class WordSearchSolver {
    /**
     * Constructor for WordSearch class.
     * Prompts for and reads puzzle and dictionary files.
     */
    public WordSearchSolver() throws IOException {
        puzzleStream = openFile("Test.grd");
        wordStream = openFile("words.txt");
        System.out.println("Reading files...");
        readPuzzle();
        readWords();
    }

    /**
     * Routine to solve the word search puzzle.
     * Performs checks in all eight directions.
     *
     * @return number of matches
     */
    public int solvePuzzle() {
        int matches = 0;

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                for (int rd = -1; rd <= 1; rd++)
                    for (int cd = -1; cd <= 1; cd++)
                        if (rd != 0 || cd != 0)
                            matches += solveDirection(r, c, rd, cd);

        return matches;
    }


    /**
     * Search the grid from a starting point and direction.
     *
     * @return number of matches
     */
    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta) {
        String charSequence = "";
        int numMatches = 0;
        int searchResult;

        charSequence += theBoard[baseRow][baseCol];

        for (int i = baseRow + rowDelta, j = baseCol + colDelta;
             i >= 0 && j >= 0 && i < rows && j < columns;
             i += rowDelta, j += colDelta) {
            charSequence += theBoard[i][j];
            searchResult = prefixSearch(theWords, charSequence);

            if (searchResult == theWords.length)
                break;
            if (!((String) theWords[searchResult]).startsWith(charSequence))
                break;

            if (theWords[searchResult].equals(charSequence)) {
                numMatches++;
                System.out.println("Found " + charSequence + " at " +
                        baseRow + " " + baseCol + " to " +
                        i + " " + j);
            }
        }

        return numMatches;
    }


    /**
     * Performs the binary search for word search.
     *
     * @param a the sorted array of strings.
     * @param x the string to search for.
     * @return last position examined;
     * this position either matches x, or x is
     * a prefix of the mismatch, or there is no
     * word for which x is a prefix.
     */
    private static int prefixSearch(Object[] a, String x) {
        int idx = Arrays.binarySearch(a, x);

        if (idx < 0)
            return -idx - 1;
        else
            return idx;
    }

    /**
     * Print a prompt and open a file.
     * Retry until open is successful.
     * Program exits if end of file is hit.
     */
    private BufferedReader openFile(String message) {
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;

        do {
            try {
                fileName = message;
                if (fileName == null)
                    System.exit(0);
                theFile = new FileReader(fileName);
                fileIn = new BufferedReader(theFile);
            } catch (IOException e) {
                System.err.println("Cannot open " + fileName);
            }
        } while (fileIn == null);

        System.out.println("Opened " + fileName);
        return fileIn;
    }

    /**
     * Routine to read the grid.
     * Checks to ensure that the grid is rectangular.
     * Checks to make sure that capacity is not exceeded is omitted.
     */
    private void readPuzzle() throws IOException {
        String oneLine;
        List puzzleLines = new ArrayList();

        if ((oneLine = puzzleStream.readLine()) == null)
            throw new IOException("No lines in puzzle file");

        columns = oneLine.length();
        puzzleLines.add(oneLine);

        while ((oneLine = puzzleStream.readLine()) != null) {
            if (oneLine.length() != columns)
                System.err.println("Puzzle is not rectangular; skipping row");
            else
                puzzleLines.add(oneLine);
        }

        rows = puzzleLines.size();
        theBoard = new char[rows][columns];
        Iterator itr = puzzleLines.iterator();
        for (int r = 0; r < rows; r++) {
            String theLine = (String) itr.next();
            theBoard[r] = theLine.toCharArray();
        }
    }

    /**
     * Routine to read the dictionary.
     * Error message is printed if dictionary is not sorted.
     */
    private void readWords() throws IOException {
        List words = new ArrayList();

        String lastWord = null;
        String thisWord;

        while ((thisWord = wordStream.readLine()) != null) {
            if (lastWord != null && thisWord.compareTo(lastWord) < 0) {
                System.err.println("Dictionary is not sorted... skipping");
                continue;
            }
            words.add(thisWord);
            lastWord = thisWord;
        }

        theWords = words.toArray();
    }




    private int rows;
    private int columns;
    private char[][] theBoard;
    private Object[] theWords;
    private BufferedReader puzzleStream;
    private BufferedReader wordStream;
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
}