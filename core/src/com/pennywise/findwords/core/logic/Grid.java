package com.pennywise.findwords.core.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Joshua.Nabongo on 9/3/2015.
 */
public class Grid {

    private char[][] grid;

    /**
     * Constructor which sets the grid.
     *
     * @param grid The grid
     */
    public Grid(char[][] grid) {
        this.grid = grid;
    }

    /**
     * Gives the height of the grid.
     *
     * @return height
     */
    public int height() {
        return grid.length;
    }

    /**
     * Gives the width of the grid.
     *
     * @return width
     */
    public int width() {
        return grid[0].length;
    }

    /**
     * Gives the character at the given position.
     *
     * @param x The column
     * @param y The row
     * @return character
     */
    public char at(int x, int y) {
        if (x >= 0 && x < width() && y >= 0 && y < height())
            return grid[y][x];

        return Character.UNASSIGNED;
    }

    public char set(int x, int y, char c) {
        if (x >= 0 && x < width() && y >= 0 && y < height()) {
            grid[y][x] = c;
            return c;
        }
        return Character.UNASSIGNED;
    }

    public char[][] getGrid() {
        return grid;
    }

    public static Grid loadPuzzle(String fileName) throws IOException {
        String oneLine;
        int rows;
        int columns;
        Grid grid;

        FileReader theFile;
        BufferedReader puzzleStream = null;
        do {
            try {
                if (fileName == null)
                    System.exit(0);
                theFile = new FileReader(fileName);
                puzzleStream = new BufferedReader(theFile);
            } catch (IOException e) {
                System.err.println("Cannot open " + fileName);
            }
        } while (puzzleStream == null);

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

        grid = new Grid(new char[rows][columns]);

        Iterator itr = puzzleLines.iterator();

        for (int r = 0; r < rows; r++) {
            String theLine = (String) itr.next();
            grid.getGrid()[r] = theLine.toCharArray();
        }

        return grid;
    }

    public static void savePuzzle(String filename, Grid grid) throws IOException {
        BufferedWriter out2 = null;
        try {
            //now add the array to the file created above
            out2 = new BufferedWriter(new FileWriter(filename + ".grd", false));
            for (int i = 0; i < grid.width(); i++) {
                for (int j = 0; j < grid.height(); j++) {
                    out2.write(grid.at(i, j));
                    System.out.print(grid.at(i, j) + " ");
                }
                out2.write("\n");
                System.out.println("");
            }
            out2.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
