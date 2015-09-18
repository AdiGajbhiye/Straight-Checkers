package com.pennywise.findwords.core.logic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {


    private static Random random = new Random(); // the shared random.


    public  void Test() {
        List<String> words = new LinkedList<String>();
        words.add("Yellow");
        words.add("Purple");
        words.add("Green");
        words.add("Beautiful");
        words.add("Chair");
        words.add("Table");
        words.add("Company");
        words.add("Tablet");
        words.add("Tower");

        PuzzleGenerator generator = new PuzzleGenerator(16, 14, words);

        words.add("Yellow");
        words.add("Purple");
        words.add("Green");
        words.add("Beautiful");
        words.add("Chair");
        words.add("Table");
        words.add("Company");
        words.add("Tablet");
        words.add("Tower");

        try {

            String[] ws = words.toArray(new String[words.size()]);
            Grid g = null;
            g = Grid.loadPuzzle("Test.grd");
            Solver solver = new Solver(g.getGrid(), ws);
            solver.solve();
            //System.out.println(solver.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
