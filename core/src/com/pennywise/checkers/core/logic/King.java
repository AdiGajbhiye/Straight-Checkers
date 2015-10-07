package com.pennywise.checkers.core.logic;

import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Owner;
import com.pennywise.checkers.core.logic.enums.Player;

import java.util.Vector;

/**
 * Created by Joshua.Nabongo on 10/7/2015.
 */
public class King extends Move {

    public King(Player player, Owner owner, boolean allowBackstep, boolean allowKingjump) {
        super(player, owner, allowBackstep, allowKingjump);
    }

    @Override
    public Vector<Step> getForcedMoves(int r, int c, Board board) {
        Vector<Step> furtherCaptures = new Vector<Step>();

        if (forwardLeftCapture(r, c, board) != null)
            furtherCaptures.add(forwardLeftCapture(r, c, board));
        if (forwardRightCapture(r, c, board) != null)
            furtherCaptures.add(forwardRightCapture(r, c, board));

        if (backwardLeftCapture(r, c, board) != null)
            furtherCaptures.add(backwardLeftCapture(r, c, board));
        if (backwardRightCapture(r, c, board) != null)
            furtherCaptures.add(backwardRightCapture(r, c, board));


        return furtherCaptures;
    }

    @Override
    public Vector<Step> calculateAllForcedMoves(Board board) {

        Vector<Step> forcedMovesForBlack = new Vector<Step>();

        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Capture
                if (board.cell[r][c].equals(playerPiece)
                        ) {
                    // Boundary Condition for forward capture for black
                    if (r >= 2) {
                        // Forward Left Capture for black
                        if (forwardLeftCapture(r, c, board) != null)
                            forcedMovesForBlack.add(forwardLeftCapture(r, c, board));

                        // Forward Right Capture for black
                        if (forwardRightCapture(r, c, board) != null)
                            forcedMovesForBlack.add(forwardRightCapture(r, c, board));
                    }

                    // backward capture
                    if (r < Board.rows - 2) {
                        // Backward Left Capture for
                        if (backwardLeftCapture(r, c, board) != null)
                            forcedMovesForBlack.add(backwardLeftCapture(r, c, board));

                        // Backward Right Capture for black king
                        if (backwardRightCapture(r, c, board) != null)
                            forcedMovesForBlack.add(backwardRightCapture(r, c, board));
                    }
                }
            }
        }

        return forcedMovesForBlack;
    }

    @Override
    public Vector<Step> calculateAllNonForcedMoves(Board board) {
        Vector<Step> allNonForcedMoves = new Vector<Step>();

        if (owner == Owner.ROBOT) {
            // Scan across the board
            for (int r = 0; r < Board.rows; r++) {
                // Check only valid cols
                int c = (r % 2 == 0) ? 0 : 1;
                for (; c < Board.cols; c += 2) {
                    assert (!board.cell[r][c].equals(CellEntry.inValid));

                    if (board.cell[r][c].equals(playerPiece)) {

                        //Forward and Backward Step for black king piece.

                        Step step = null;
                        step = forwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = forwardRightCapture(r, c, board);
                        assert (step == null);

                        step = backwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = backwardRightCapture(r, c, board);
                        assert (step == null);

                        step = forwardLeft(r, c, board);
                        if (step != null)
                            allNonForcedMoves.add(step);


                        step = forwardRight(r, c, board);
                        if (step != null)
                            allNonForcedMoves.add(step);


                        step = backwardLeft(r, c, board);
                        if (step != null)
                            allNonForcedMoves.add(step);


                        step = backwardRight(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }
                    }
                }
            }
        } else {

            for (int r = 0; r < Board.rows; r++) {
                // Check only valid cols
                int c = (r % 2 == 0) ? 0 : 1;
                for (; c < Board.cols; c += 2) {
                    assert (!board.cell[r][c].equals(CellEntry.inValid));

                    //Forward and Backward Step for black king piece.
                    if (board.cell[r][c].equals(playerPiece)) {
                        Step step = null;
                        step = forwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = forwardRightCapture(r, c, board);
                        assert (step == null);

                        step = backwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = backwardRightCapture(r, c, board);
                        assert (step == null);

                        step = forwardLeft(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }

                        step = forwardRight(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }

                        step = backwardLeft(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }

                        step = backwardRight(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }
                    }
                }
            }
        }

        return allNonForcedMoves;
    }


}
