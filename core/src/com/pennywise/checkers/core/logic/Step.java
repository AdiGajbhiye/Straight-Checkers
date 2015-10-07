/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

import java.util.Vector;

/**
 * @author ASHISH
 */
public class Step {
    int initialRow;
    int initialCol;
    int finalRow;
    int finalCol;

    public Step(int r1, int c1, int r2, int c2) {
        this.initialRow = r1;
        this.initialCol = c1;
        this.finalRow = r2;
        this.finalCol = c2;
    }

    public boolean Equals(Step step) {
        return (this.initialRow == step.initialRow
                && this.initialCol == step.initialCol
                && this.finalRow == step.finalRow
                && this.finalCol == step.finalCol) ? true : false;
    }

    public boolean ExistsInVector(Vector<Step> steps) {
        for (int i = 0; i < steps.size(); i++) {
            if (this.Equals(steps.elementAt(i))) {
                return true;
            }
        }
        return false;
    }

    public void display() {
        System.out.print("(" + this.initialRow + "," + this.initialCol + ") -->" + " (" + this.finalRow + ", " + this.finalCol + ")");
    }

    public int getInitialRow() {
        return initialRow;
    }

    public int getInitialCol() {
        return initialCol;
    }

    public int getFinalRow() {
        return finalRow;
    }

    public int getFinalCol() {
        return finalCol;
    }
}
