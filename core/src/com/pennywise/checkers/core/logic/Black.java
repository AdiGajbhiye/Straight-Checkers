/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

import java.util.Vector;

import com.pennywise.checkers.core.logic.enums.CellEntry;

/**
 * @author ASHISH
 */
public class Black implements Move{

    public static Vector<Step> ObtainForcedMovesForBlack(int r, int c, Board board, CellEntry playerPawn, CellEntry playerKing,
                                                         CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Step> furtherCaptures = new Vector<Step>();

        if (board.cell[r][c].equals(playerPawn) || board.cell[r][c].equals(playerKing)) {
            if (blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing));
            if (blackForwardRightCapture(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(blackForwardRightCapture(r, c, board, opponentPawn, opponentKing));
        }

        if (board.cell[r][c].equals(playerKing)) {
            if (blackBackwardLeftCapture(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(blackBackwardLeftCapture(r, c, board, opponentPawn, opponentKing));
            if (blackBackwardRightCapture(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(blackBackwardRightCapture(r, c, board, opponentPawn, opponentKing));
        }

        return furtherCaptures;
    }

    public static Vector<Step> CalculateAllForcedMovesForBlack(Board board, CellEntry playerPawn, CellEntry playerKing,
                                                               CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Step> forcedMovesForBlack = new Vector<Step>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Capture
                if (
                        board.cell[r][c].equals(playerPawn) ||
                                board.cell[r][c].equals(playerKing)
                        ) {
                    // Boundary Condition for forward capture for black
                    if (r >= 2) {
                        // Forward Left Capture for black
                        if (blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForBlack.add(blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing));

                        // Forward Right Capture for black
                        if (blackForwardRightCapture(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForBlack.add(blackForwardRightCapture(r, c, board, opponentPawn, opponentKing));
                    }
                }
                // Backward Capture for Black King
                if (board.cell[r][c].equals(playerKing)) {
                    // Boundary Condition for backward capture
                    if (r < Board.rows - 2) {
                        // Backward Left Capture for black king
                        if (blackBackwardLeftCapture(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForBlack.add(blackBackwardLeftCapture(r, c, board, opponentPawn, opponentKing));

                        // Backward Right Capture for black king
                        if (blackBackwardRightCapture(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForBlack.add(blackBackwardRightCapture(r, c, board, opponentPawn, opponentKing));
                    }
                }
            }
        }

        return forcedMovesForBlack;
    }

    /**
     * Returns a vector of all possible steps which Black can make at the state of the game given by board.
     * <p/>
     * Should only be called if no forced steps exist.
     *
     * @param board
     * @return
     */
    public static Vector<Step> CalculateAllNonForcedMovesForBlack(Board board, CellEntry playerPawn, CellEntry playerKing
            , CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Step> allNonForcedMovesForBlack = new Vector<Step>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Step for normal black piece.
                if (board.cell[r][c].equals(playerPawn)) {

                    Step step = null;
                    step = blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = blackForwardRightCapture(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = blackForwardLeft(r, c, board, playerPawn, playerKing);
                    if (step != null) {
                        allNonForcedMovesForBlack.add(step);
                    }

                    step = blackForwardRight(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForBlack.add(step);
                    }
                }

                //Forward and Backward Step for black king piece.
                if (board.cell[r][c] == playerKing) {
                    Step step = null;
                    step = blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = blackForwardRightCapture(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);

                    step = blackBackwardLeftCapture(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = blackBackwardRightCapture(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);

                    step = blackForwardLeft(r, c, board, playerPawn, playerKing);
                    if (step != null) {
                        allNonForcedMovesForBlack.add(step);
                    }

                    step = blackForwardRight(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForBlack.add(step);
                    }

                    step = blackBackwardLeft(r, c, board, playerPawn);
                    if (step != null) {
                        allNonForcedMovesForBlack.add(step);
                    }

                    step = blackBackwardRight(r, c, board, playerKing);
                    if (step != null) {
                        allNonForcedMovesForBlack.add(step);
                    }

                }


            }
        }

        return allNonForcedMovesForBlack;
    }


    private static Step blackForwardLeft(int r, int c, Board board, CellEntry playerPawn, CellEntry playerKing) {
        Step forwardLeft = null;

        assert (board.cell[r][c] == playerPawn || board.cell[r][c] == playerKing);

        if (r >= 1 && c < Board.cols - 1 &&
                board.cell[r - 1][c + 1] == CellEntry.empty

                ) {
            forwardLeft = new Step(r, c, r - 1, c + 1);
        }
        return forwardLeft;
    }

    // Forward Left Capture for Black
    private static Step blackForwardLeftCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {
        Step forwardLeftCapture = null;

        if (r >= 2 && c < Board.cols - 2 &&
                (
                        board.cell[r - 1][c + 1].equals(opponentPawn)
                                || board.cell[r - 1][c + 1].equals(opponentKing)
                )
                && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                ) {
            forwardLeftCapture = new Step(r, c, r - 2, c + 2);
        }

        return forwardLeftCapture;
    }


    //Forward Right for Black
    private static Step blackForwardRight(int r, int c, Board board) {
        Step forwardRight = null;
        if (r >= 1 && c >= 1 &&
                board.cell[r - 1][c - 1] == CellEntry.empty
                ) {
            forwardRight = new Step(r, c, r - 1, c - 1);
        }
        return forwardRight;
    }

    // Forward Right Capture for White
    private static Step blackForwardRightCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {

        Step forwardRightCapture = null;

        if (r >= 2 && c >= 2 && (
                board.cell[r - 1][c - 1].equals(opponentPawn)
                        || board.cell[r - 1][c - 1].equals(opponentKing)
        )
                && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                ) {
            forwardRightCapture = new Step(r, c, r - 2, c - 2);

        }

        return forwardRightCapture;
    }

    private static Step blackBackwardLeft(int r, int c, Board board, CellEntry playerPawn) {
        Step backwardLeft = null;

        assert (board.cell[r][c].equals(playerPawn));
        if (r < Board.rows - 1 && c < Board.cols - 1 &&
                board.cell[r + 1][c + 1] == CellEntry.empty
                ) {
            backwardLeft = new Step(r, c, r + 1, c + 1);
        }

        return backwardLeft;
    }

    // Backward Left Capture for Black
    private static Step blackBackwardLeftCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {

        Step backwardLeftCapture = null;

        if (r < Board.rows - 2 && c < Board.cols - 2 && (
                board.cell[r + 1][c + 1].equals(opponentPawn)
                        || board.cell[r + 1][c + 1].equals(opponentKing)
        )
                && board.cell[r + 2][c + 2].equals(CellEntry.empty)
                ) {
            backwardLeftCapture = new Step(r, c, r + 2, c + 2);

        }

        return backwardLeftCapture;
    }

    private static Step blackBackwardRight(int r, int c, Board board, CellEntry playerKing) {
        Step backwardRight = null;


        if (r < Board.rows - 1 && c >= 1 &&
                board.cell[r + 1][c - 1].equals(CellEntry.empty)
                ) {
            backwardRight = new Step(r, c, r + 1, c - 1);
        }
        return backwardRight;
    }
    // Backward Right Capture for Black
    private static Step blackBackwardRightCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {

        Step backwardRightCapture = null;

        if (r < Board.rows - 2 && c >= 2 && (
                board.cell[r + 1][c - 1].equals(opponentPawn) ||
                        board.cell[r + 1][c - 1].equals(opponentKing)
        )
                && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                ) {
            backwardRightCapture = new Step(r, c, r + 2, c - 2);
        }

        return backwardRightCapture;
    }
}