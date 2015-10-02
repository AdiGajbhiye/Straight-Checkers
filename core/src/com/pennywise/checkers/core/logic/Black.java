/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

import java.util.Vector;

import com.pennywise.checkers.core.logic.enums.Owner;
import com.pennywise.checkers.core.logic.enums.CellEntry;

/**
 * @author ASHISH
 */
public class Black {

    public static Owner owner;

    public static Vector<Move> Move(Board board) {

        UserInteractions.PrintSeparator('-');
        System.out.println("\t\tBLACK's TURN");
        UserInteractions.PrintSeparator('-');

        if (owner.equals(Owner.HUMAN)) {
            Human.makeNextBlackMoves(board);
        } else {
            assert (owner.equals(Owner.ROBOT));
            return Robot.makeNextBlackMoves(board);
        }

        return null;
    }

    public static Vector<Move> Move(int r1, int c1, int r2, int c2, Board board) {

        UserInteractions.PrintSeparator('-');
        System.out.println("\t\tBLACK's TURN");
        UserInteractions.PrintSeparator('-');

        if (owner.equals(Owner.HUMAN)) {
            Human.makeNextBlackMoves(r1, c1, r2, c2, board);
        } else {
            assert (owner.equals(Owner.ROBOT));
            return Robot.makeNextBlackMoves(board);
        }

        return null;
    }


    public static Vector<Move> ObtainForcedMovesForBlack(int r, int c, Board board) {
        Vector<Move> furtherCaptures = new Vector<Move>();

        if (board.cell[r][c].equals(CellEntry.black) || board.cell[r][c].equals(CellEntry.blackKing)) {
            if (blackForwardLeftCapture(r, c, board) != null)
                furtherCaptures.add(blackForwardLeftCapture(r, c, board));
            if (blackForwardRightCapture(r, c, board) != null)
                furtherCaptures.add(blackForwardRightCapture(r, c, board));
        }

        if (board.cell[r][c].equals(CellEntry.blackKing)) {
            if (blackBackwardLeftCapture(r, c, board) != null)
                furtherCaptures.add(blackBackwardLeftCapture(r, c, board));
            if (blackBackwardRightCapture(r, c, board) != null)
                furtherCaptures.add(blackBackwardRightCapture(r, c, board));
        }

        return furtherCaptures;
    }

    public static Vector<Move> CalculateAllForcedMovesForBlack(Board board) {
        Vector<Move> forcedMovesForBlack = new Vector<Move>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Capture
                if (
                        board.cell[r][c].equals(CellEntry.black) ||
                                board.cell[r][c].equals(CellEntry.blackKing)
                        ) {
                    // Boundary Condition for forward capture for black
                    if (r >= 2) {
                        // Forward Left Capture for black
                        if (blackForwardLeftCapture(r, c, board) != null)
                            forcedMovesForBlack.add(blackForwardLeftCapture(r, c, board));

                        // Forward Right Capture for black
                        if (blackForwardRightCapture(r, c, board) != null)
                            forcedMovesForBlack.add(blackForwardRightCapture(r, c, board));
                    }
                }
                // Backward Capture for Black King
                if (board.cell[r][c].equals(CellEntry.blackKing)) {
                    // Boundary Condition for backward capture
                    if (r < Board.rows - 2) {
                        // Backward Left Capture for black king
                        if (blackBackwardLeftCapture(r, c, board) != null)
                            forcedMovesForBlack.add(blackBackwardLeftCapture(r, c, board));

                        // Backward Right Capture for black king
                        if (blackBackwardRightCapture(r, c, board) != null)
                            forcedMovesForBlack.add(blackBackwardRightCapture(r, c, board));
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
    public static Vector<Move> CalculateAllNonForcedMovesForBlack(Board board) {
        Vector<Move> allNonForcedMovesForBlack = new Vector<Move>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Move for normal black piece.
                if (board.cell[r][c].equals(CellEntry.black)) {

                    Move move = null;
                    move = blackForwardLeftCapture(r, c, board);
                    assert (move == null);
                    move = blackForwardRightCapture(r, c, board);
                    assert (move == null);

                    move = blackForwardLeft(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackForwardRight(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }
                }

                //Forward and Backward Move for black king piece.
                if (board.cell[r][c] == CellEntry.blackKing) {
                    Move move = null;
                    move = blackForwardLeftCapture(r, c, board);
                    assert (move == null);
                    move = blackForwardRightCapture(r, c, board);
                    assert (move == null);

                    move = blackBackwardLeftCapture(r, c, board);
                    assert (move == null);
                    move = blackBackwardRightCapture(r, c, board);
                    assert (move == null);

                    move = blackForwardLeft(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackForwardRight(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackBackwardLeft(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                    move = blackBackwardRight(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForBlack.add(move);
                    }

                }


            }
        }

        return allNonForcedMovesForBlack;
    }


    private static Move blackForwardLeft(int r, int c, Board board) {
        Move forwardLeft = null;

        assert (board.cell[r][c] == CellEntry.black || board.cell[r][c] == CellEntry.blackKing);

        if (r >= 1 && c < Board.cols - 1 &&
                board.cell[r - 1][c + 1] == CellEntry.empty

                ) {
            forwardLeft = new Move(r, c, r - 1, c + 1);
        }
        return forwardLeft;
    }

    // Forward Left Capture for Black
    private static Move blackForwardLeftCapture(int r, int c, Board board) {
        Move forwardLeftCapture = null;

        if (r >= 2 && c < Board.cols - 2 &&
                (
                        board.cell[r - 1][c + 1].equals(CellEntry.white)
                                || board.cell[r - 1][c + 1].equals(CellEntry.whiteKing)
                )
                && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                ) {
            forwardLeftCapture = new Move(r, c, r - 2, c + 2);
            //System.out.println("Forward Left Capture for Black");
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
    private static Move blackForwardRightCapture(int r, int c, Board board) {

        Move forwardRightCapture = null;

        if (r >= 2 && c >= 2 && (
                board.cell[r - 1][c - 1].equals(CellEntry.white)
                        || board.cell[r - 1][c - 1].equals(CellEntry.whiteKing)
        )
                && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                ) {
            forwardRightCapture = new Move(r, c, r - 2, c - 2);

        }

        return forwardRightCapture;
    }

    private static Move blackBackwardLeft(int r, int c, Board board) {
        Move backwardLeft = null;

        assert (board.cell[r][c].equals(CellEntry.blackKing));
        if (r < Board.rows - 1 && c < Board.cols - 1 &&
                board.cell[r + 1][c + 1] == CellEntry.empty
                ) {
            backwardLeft = new Move(r, c, r + 1, c + 1);
        }

        return backwardLeft;
    }

    // Backward Left Capture for Black
    private static Move blackBackwardLeftCapture(int r, int c, Board board) {

        Move backwardLeftCapture = null;

        if (r < Board.rows - 2 && c < Board.cols - 2 && (
                board.cell[r + 1][c + 1].equals(CellEntry.white)
                        || board.cell[r + 1][c + 1].equals(CellEntry.whiteKing)
        )
                && board.cell[r + 2][c + 2].equals(CellEntry.empty)
                ) {
            backwardLeftCapture = new Move(r, c, r + 2, c + 2);

        }

        return backwardLeftCapture;
    }


    private static Move blackBackwardRight(int r, int c, Board board) {
        Move backwardRight = null;

        assert (board.cell[r][c].equals(CellEntry.blackKing));

        if (r < Board.rows - 1 && c >= 1 &&
                board.cell[r + 1][c - 1].equals(CellEntry.empty)
                ) {
            backwardRight = new Move(r, c, r + 1, c - 1);
        }
        return backwardRight;
    }


    // Backward Right Capture for Black
    private static Move blackBackwardRightCapture(int r, int c, Board board) {

        Move backwardRightCapture = null;

        if (r < Board.rows - 2 && c >= 2 && (
                board.cell[r + 1][c - 1].equals(CellEntry.white) ||
                        board.cell[r + 1][c - 1].equals(CellEntry.whiteKing)
        )
                && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                ) {
            backwardRightCapture = new Move(r, c, r + 2, c - 2);
        }

        return backwardRightCapture;
    }
}