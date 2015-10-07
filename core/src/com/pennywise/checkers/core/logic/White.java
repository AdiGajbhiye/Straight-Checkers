/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Owner;

import java.util.Vector;

/**
 * @author ASHISH
 */
public class White implements Move {


    public static Vector<Step> ObtainForcedMovesForWhite(int r, int c, Board board, CellEntry playerPawn, CellEntry playerKing,
                                                         CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Step> furtherCaptures = new Vector<Step>();

        if (board.cell[r][c].equals(playerPawn) || board.cell[r][c].equals(playerKing)) {
            if (ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing));
            if (ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing));
        }

        if (board.cell[r][c].equals(playerKing)) {
            if (BackwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(BackwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing));
            if (BackwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                furtherCaptures.add(BackwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing));
        }

        return furtherCaptures;
    }

    public static Vector<Step> CalculateAllForcedMovesForWhite(Board board, CellEntry playerPawn, CellEntry playerKing,
                                                               CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Step> forcedMovesForWhite = new Vector<Step>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Capture
                if (board.cell[r][c].equals(playerPawn)) {
                    // Boundary Condition for forward capture
                    if (r < Board.rows - 2) {
                        // Forward Left Capture
                        if (ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForWhite.add(ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing));

                        // Forward Right Capture
                        if (ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForWhite.add(ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing));
                    }
                }
                // Backward Capture
                if (board.cell[r][c].equals(CellEntry.whiteKing)) {
                    // Boundary Condition for backward capture
                    if (r >= 2) {
                        // Backward Left Capture
                        if (BackwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForWhite.add(BackwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing));

                        // Backward Right Capture
                        if (BackwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing) != null)
                            forcedMovesForWhite.add(BackwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing));
                    }
                }
            }
        }

        return forcedMovesForWhite;
    }

    /**
     * Returns a vector of all possible steps which White can make at the state of the game given by board.
     * Should only be called if no forced steps exist.
     *
     * @param board
     * @return
     */
    public static Vector<Step> CalculateAllNonForcedMovesForWhite(Board board, CellEntry playerPawn, CellEntry playerKing,
                                                                  CellEntry opponentPawn, CellEntry opponentKing) {

        Vector<Step> allNonForcedMovesForWhite = new Vector<Step>();
        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Step for normal white piece.
                if (board.cell[r][c].equals(playerPawn)) {

                    Step step = null;
                    step = ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);

                    step = ForwardLeftForWhite(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForWhite.add(step);
                    }

                    step = ForwardRightForWhite(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForWhite.add(step);
                    }
                }

                //Forward and Backward Step for black king piece.
                if (board.cell[r][c] == playerKing) {
                    Step step = null;
                    step = ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);

                    step = BackwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);
                    step = BackwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (step == null);

                    step = ForwardLeftForWhite(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForWhite.add(step);
                    }

                    step = ForwardRightForWhite(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForWhite.add(step);
                    }

                    step = BackwardLeftForWhite(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForWhite.add(step);
                    }

                    step = BackwardRightForWhite(r, c, board);
                    if (step != null) {
                        allNonForcedMovesForWhite.add(step);
                    }

                }


            }
        }

        return allNonForcedMovesForWhite;
    }

    private static Step ForwardLeftForWhite(int r, int c, Board board) {
        Step forwardLeft = null;
        if (r < Board.rows - 1 && c >= 1 &&
                board.cell[r + 1][c - 1] == CellEntry.empty
                ) {
            forwardLeft = new Step(r, c, r + 1, c - 1);
        }
        return forwardLeft;
    }

    // Forward Left Capture for White
    private static Step ForwardLeftCaptureForWhite(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {
        Step forwardLeftCapture = null;

        if (r < Board.rows - 2 && c >= 2 &&
                (
                        board.cell[r + 1][c - 1].equals(opponentPawn)
                                || board.cell[r + 1][c - 1].equals(opponentKing)
                )
                && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                ) {
            forwardLeftCapture = new Step(r, c, r + 2, c - 2);
            //System.out.println("Forward Left Capture");
        }

        return forwardLeftCapture;
    }

    private static Step ForwardRightForWhite(int r, int c, Board board) {
        Step forwardRight = null;
        if (r < Board.rows - 1 && c < Board.cols - 1 &&
                board.cell[r + 1][c + 1] == CellEntry.empty
                ) {
            forwardRight = new Step(r, c, r + 1, c + 1);
        }
        return forwardRight;
    }

    // Forward Right Capture for White
    private static Step ForwardRightCaptureForWhite(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {
        Step forwardRightCapture = null;

        if (r < Board.rows - 2 && c < Board.cols - 2 &&
                (
                        board.cell[r + 1][c + 1].equals(opponentPawn)
                                || board.cell[r + 1][c + 1].equals(opponentKing)
                )
                && board.cell[r + 2][c + 2].equals(CellEntry.empty)
                ) {
            forwardRightCapture = new Step(r, c, r + 2, c + 2);
            //System.out.println("Forward Right Capture");
        }

        return forwardRightCapture;
    }

    private static Step BackwardLeftForWhite(int r, int c, Board board) {
        Step backwardLeft = null;
        if (r >= 1 && c >= 1 &&
                board.cell[r - 1][c - 1] == CellEntry.empty
                ) {
            backwardLeft = new Step(r, c, r - 1, c - 1);
        }
        return backwardLeft;
    }

    // Backward Left Capture for White
    private static Step BackwardLeftCaptureForWhite(int r, int c, Board board,
                                                    CellEntry opponentPawn, CellEntry opponentKing) {

        Step backwardLeftCapture = null;

        if (r >= 2 && c >= 2 && (
                board.cell[r - 1][c - 1].equals(opponentPawn)
                        || board.cell[r - 1][c - 1].equals(opponentKing)
        )
                && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                ) {
            backwardLeftCapture = new Step(r, c, r - 2, c - 2);
            //System.out.println("Backward Left Capture");
        }

        return backwardLeftCapture;
    }

    private static Step BackwardRightForWhite(int r, int c, Board board) {
        Step backwardRight = null;
        if (r >= 1 && c < Board.cols - 1 &&
                board.cell[r - 1][c + 1] == CellEntry.empty
                ) {
            backwardRight = new Step(r, c, r - 1, c + 1);
        }
        return backwardRight;
    }

    // Backward Right Capture for White
    private static Step BackwardRightCaptureForWhite(int r, int c, Board board,
                                                     CellEntry opponentPawn, CellEntry opponentKing) {
        Step backwardRightCapture = null;

        if (r >= 2 && c < Board.cols - 2 && (
                board.cell[r - 1][c + 1].equals(opponentPawn) ||
                        board.cell[r - 1][c + 1].equals(opponentKing)
        )
                && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                ) {
            backwardRightCapture = new Step(r, c, r - 2, c + 2);
            //System.out.println("Backward Right Capture");
        }

        return backwardRightCapture;
    }
}