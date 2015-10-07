/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

import java.util.Vector;

import com.pennywise.checkers.core.logic.enums.Owner;
import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Player;

/**
 * @author ASHISH
 */
public class Black {

    public static Vector<Move> ObtainForcedMovesForBlack(int r, int c, Board board, CellEntry playerPawn, CellEntry playerKing,
                                                         CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Move> furtherCaptures = new Vector<Move>();

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

    public static Vector<Move> CalculateAllForcedMovesForBlack(Board board, CellEntry playerPawn, CellEntry playerKing,
                                                               CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Move> forcedMovesForBlack = new Vector<Move>();

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
     * Returns a vector of all possible moves which Black can make at the state of the game given by board.
     * <p/>
     * Should only be called if no forced moves exist.
     *
     * @param board
     * @return
     */
    public static Vector<Move> CalculateAllNonForcedMovesForBlack(Board board, CellEntry playerPawn, CellEntry playerKing
            , CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Move> allNonForcedMovesForBlack = new Vector<Move>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Move for normal black piece.
                if (board.cell[r][c].equals(playerPawn)) {

                    Move move = null;
                    move = blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = blackForwardRightCapture(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = blackForwardLeft(r, c, board, playerPawn, playerKing);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackForwardRight(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }
                }

                //Forward and Backward Move for black king piece.
                if (board.cell[r][c] == playerKing) {
                    Move move = null;
                    move = blackForwardLeftCapture(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = blackForwardRightCapture(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);

                    move = blackBackwardLeftCapture(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = blackBackwardRightCapture(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);

                    move = blackForwardLeft(r, c, board, playerPawn, playerKing);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackForwardRight(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackBackwardLeft(r, c, board, playerPawn);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackBackwardRight(r, c, board, playerKing);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                }


            }
        }

        return allNonForcedMovesForBlack;
    }


    private static Move blackForwardLeft(int r, int c, Board board, CellEntry playerPawn, CellEntry playerKing) {
        Move forwardLeft = null;

        assert (board.cell[r][c] == playerPawn || board.cell[r][c] == playerKing);

        if (r >= 1 && c < Board.cols - 1 &&
                board.cell[r - 1][c + 1] == CellEntry.empty

                ) {
            forwardLeft = new Move(r, c, r - 1, c + 1);
        }
        return forwardLeft;
    }

    // Forward Left Capture for Black
    private static Move blackForwardLeftCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {
        Move forwardLeftCapture = null;

        if (r >= 2 && c < Board.cols - 2 &&
                (
                        board.cell[r - 1][c + 1].equals(opponentPawn)
                                || board.cell[r - 1][c + 1].equals(opponentKing)
                )
                && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                ) {
            forwardLeftCapture = new Move(r, c, r - 2, c + 2);
        }

        return forwardLeftCapture;
    }


    //Forward Right for Black
    private static Move blackForwardRight(int r, int c, Board board) {
        Move forwardRight = null;
        if (r >= 1 && c >= 1 &&
                board.cell[r - 1][c - 1] == CellEntry.empty
                ) {
            forwardRight = new Move(r, c, r - 1, c - 1);
        }
        return forwardRight;
    }

    // Forward Right Capture for White
    private static Move blackForwardRightCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {

        Move forwardRightCapture = null;

        if (r >= 2 && c >= 2 && (
                board.cell[r - 1][c - 1].equals(opponentPawn)
                        || board.cell[r - 1][c - 1].equals(opponentKing)
        )
                && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                ) {
            forwardRightCapture = new Move(r, c, r - 2, c - 2);

        }

        return forwardRightCapture;
    }

    private static Move blackBackwardLeft(int r, int c, Board board, CellEntry playerPawn) {
        Move backwardLeft = null;

        assert (board.cell[r][c].equals(playerPawn));
        if (r < Board.rows - 1 && c < Board.cols - 1 &&
                board.cell[r + 1][c + 1] == CellEntry.empty
                ) {
            backwardLeft = new Move(r, c, r + 1, c + 1);
        }

        return backwardLeft;
    }

    // Backward Left Capture for Black
    private static Move blackBackwardLeftCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {

        Move backwardLeftCapture = null;

        if (r < Board.rows - 2 && c < Board.cols - 2 && (
                board.cell[r + 1][c + 1].equals(opponentPawn)
                        || board.cell[r + 1][c + 1].equals(opponentKing)
        )
                && board.cell[r + 2][c + 2].equals(CellEntry.empty)
                ) {
            backwardLeftCapture = new Move(r, c, r + 2, c + 2);

        }

        return backwardLeftCapture;
    }


    private static Move blackBackwardRight(int r, int c, Board board, CellEntry playerKing) {
        Move backwardRight = null;


        if (r < Board.rows - 1 && c >= 1 &&
                board.cell[r + 1][c - 1].equals(CellEntry.empty)
                ) {
            backwardRight = new Move(r, c, r + 1, c - 1);
        }
        return backwardRight;
    }


    // Backward Right Capture for Black
    private static Move blackBackwardRightCapture(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {

        Move backwardRightCapture = null;

        if (r < Board.rows - 2 && c >= 2 && (
                board.cell[r + 1][c - 1].equals(opponentPawn) ||
                        board.cell[r + 1][c - 1].equals(opponentKing)
        )
                && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                ) {
            backwardRightCapture = new Move(r, c, r + 2, c - 2);
        }

        return backwardRightCapture;
    }
}