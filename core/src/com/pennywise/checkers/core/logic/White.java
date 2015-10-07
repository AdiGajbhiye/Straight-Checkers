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
public class White {
    public static Owner owner;


    public static Vector<Move> ObtainForcedMovesForWhite(int r, int c, Board board, CellEntry playerPawn, CellEntry playerKing,
                                                         CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Move> furtherCaptures = new Vector<Move>();

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

    public static Vector<Move> CalculateAllForcedMovesForWhite(Board board, CellEntry playerPawn, CellEntry playerKing,
                                                               CellEntry opponentPawn, CellEntry opponentKing) {
        Vector<Move> forcedMovesForWhite = new Vector<Move>();

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
     * Returns a vector of all possible moves which White can make at the state of the game given by board.
     * Should only be called if no forced moves exist.
     *
     * @param board
     * @return
     */
    public static Vector<Move> CalculateAllNonForcedMovesForWhite(Board board, CellEntry playerPawn, CellEntry playerKing,
                                                                  CellEntry opponentPawn, CellEntry opponentKing) {

        Vector<Move> allNonForcedMovesForWhite = new Vector<Move>();
        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Move for normal white piece.
                if (board.cell[r][c].equals(playerPawn)) {

                    Move move = null;
                    move = ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);

                    move = ForwardLeftForWhite(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForWhite.add(move);
                    }

                    move = ForwardRightForWhite(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForWhite.add(move);
                    }
                }

                //Forward and Backward Move for black king piece.
                if (board.cell[r][c] == playerKing) {
                    Move move = null;
                    move = ForwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = ForwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);

                    move = BackwardLeftCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);
                    move = BackwardRightCaptureForWhite(r, c, board, opponentPawn, opponentKing);
                    assert (move == null);

                    move = ForwardLeftForWhite(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForWhite.add(move);
                    }

                    move = ForwardRightForWhite(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForWhite.add(move);
                    }

                    move = BackwardLeftForWhite(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForWhite.add(move);
                    }

                    move = BackwardRightForWhite(r, c, board);
                    if (move != null) {
                        allNonForcedMovesForWhite.add(move);
                    }

                }


            }
        }

        return allNonForcedMovesForWhite;
    }

    /**
     * Returns a vector of all possible moves which a White piece can make at the state of the game given by board.
     * Should only be called if no forced moves exist.
     *
     * @param row
     * @param col
     * @param board
     * @return Vector of moves
     */

    public static Vector<Move> CalculateAllNonForcedMovesForWhite(int row, int col, Board board, CellEntry playerPawn, CellEntry playerKing,
                                                                  CellEntry opponentPawn, CellEntry opponentKing) {

        Vector<Move> allNonForcedMovesForWhite = new Vector<Move>();

        // Check only valid cols
        col = (row % 2 == 0) ? 0 : 1;

        assert (!board.cell[row][col].equals(CellEntry.inValid));

        // Forward Move for normal white piece.
        if (board.cell[row][col].equals(CellEntry.white)) {
            Move move = null;
            move = ForwardLeftCaptureForWhite(row, col, board, opponentPawn, opponentKing);
            assert (move == null);
            move = ForwardRightCaptureForWhite(row, col, board, opponentPawn, opponentKing);
            assert (move == null);
            move = ForwardLeftForWhite(row, col, board);
            if (move != null) {
                allNonForcedMovesForWhite.add(move);
            }

            move = ForwardRightForWhite(row, col, board);
            if (move != null) {
                allNonForcedMovesForWhite.add(move);
            }
        }

        //Forward and Backward Move for black king piece.
        if (board.cell[row][col] == CellEntry.whiteKing) {
            Move move = null;
            move = ForwardLeftCaptureForWhite(row, col, board, opponentPawn, opponentKing);
            assert (move == null);
            move = ForwardRightCaptureForWhite(row, col, board, opponentPawn, opponentKing);
            assert (move == null);

            move = BackwardLeftCaptureForWhite(row, col, board, opponentPawn, opponentKing);
            assert (move == null);
            move = BackwardRightCaptureForWhite(row, col, board, opponentPawn, opponentKing);
            assert (move == null);

            move = ForwardLeftForWhite(row, col, board);
            if (move != null) {
                allNonForcedMovesForWhite.add(move);
            }

            move = ForwardRightForWhite(row, col, board);
            if (move != null) {
                allNonForcedMovesForWhite.add(move);
            }

            move = BackwardLeftForWhite(row, col, board);
            if (move != null) {
                allNonForcedMovesForWhite.add(move);
            }

            move = BackwardRightForWhite(row, col, board);
            if (move != null) {
                allNonForcedMovesForWhite.add(move);
            }

        }


        return allNonForcedMovesForWhite;
    }


    private static Move ForwardLeftForWhite(int r, int c, Board board) {
        Move forwardLeft = null;
        if (r < Board.rows - 1 && c >= 1 &&
                board.cell[r + 1][c - 1] == CellEntry.empty
                ) {
            forwardLeft = new Move(r, c, r + 1, c - 1);
        }
        return forwardLeft;
    }

    // Forward Left Capture for White
    private static Move ForwardLeftCaptureForWhite(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {
        Move forwardLeftCapture = null;

        if (r < Board.rows - 2 && c >= 2 &&
                (
                        board.cell[r + 1][c - 1].equals(opponentPawn)
                                || board.cell[r + 1][c - 1].equals(opponentKing)
                )
                && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                ) {
            forwardLeftCapture = new Move(r, c, r + 2, c - 2);
            //System.out.println("Forward Left Capture");
        }

        return forwardLeftCapture;
    }

    private static Move ForwardRightForWhite(int r, int c, Board board) {
        Move forwardRight = null;
        if (r < Board.rows - 1 && c < Board.cols - 1 &&
                board.cell[r + 1][c + 1] == CellEntry.empty
                ) {
            forwardRight = new Move(r, c, r + 1, c + 1);
        }
        return forwardRight;
    }

    // Forward Right Capture for White
    private static Move ForwardRightCaptureForWhite(int r, int c, Board board, CellEntry opponentPawn, CellEntry opponentKing) {
        Move forwardRightCapture = null;

        if (r < Board.rows - 2 && c < Board.cols - 2 &&
                (
                        board.cell[r + 1][c + 1].equals(opponentPawn)
                                || board.cell[r + 1][c + 1].equals(opponentKing)
                )
                && board.cell[r + 2][c + 2].equals(CellEntry.empty)
                ) {
            forwardRightCapture = new Move(r, c, r + 2, c + 2);
            //System.out.println("Forward Right Capture");
        }

        return forwardRightCapture;
    }

    private static Move BackwardLeftForWhite(int r, int c, Board board) {
        Move backwardLeft = null;
        if (r >= 1 && c >= 1 &&
                board.cell[r - 1][c - 1] == CellEntry.empty
                ) {
            backwardLeft = new Move(r, c, r - 1, c - 1);
        }
        return backwardLeft;
    }

    // Backward Left Capture for White
    private static Move BackwardLeftCaptureForWhite(int r, int c, Board board,
                                                    CellEntry opponentPawn, CellEntry opponentKing) {

        Move backwardLeftCapture = null;

        if (r >= 2 && c >= 2 && (
                board.cell[r - 1][c - 1].equals(opponentPawn)
                        || board.cell[r - 1][c - 1].equals(opponentKing)
        )
                && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                ) {
            backwardLeftCapture = new Move(r, c, r - 2, c - 2);
            //System.out.println("Backward Left Capture");
        }

        return backwardLeftCapture;
    }

    private static Move BackwardRightForWhite(int r, int c, Board board) {
        Move backwardRight = null;
        if (r >= 1 && c < Board.cols - 1 &&
                board.cell[r - 1][c + 1] == CellEntry.empty
                ) {
            backwardRight = new Move(r, c, r - 1, c + 1);
        }
        return backwardRight;
    }

    // Backward Right Capture for White
    private static Move BackwardRightCaptureForWhite(int r, int c, Board board,
                                                     CellEntry opponentPawn, CellEntry opponentKing) {
        Move backwardRightCapture = null;

        if (r >= 2 && c < Board.cols - 2 && (
                board.cell[r - 1][c + 1].equals(opponentPawn) ||
                        board.cell[r - 1][c + 1].equals(opponentKing)
        )
                && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                ) {
            backwardRightCapture = new Move(r, c, r - 2, c + 2);
            //System.out.println("Backward Right Capture");
        }

        return backwardRightCapture;
    }
}