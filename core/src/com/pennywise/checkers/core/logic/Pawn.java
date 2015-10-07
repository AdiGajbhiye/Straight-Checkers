package com.pennywise.checkers.core.logic;

import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Owner;
import com.pennywise.checkers.core.logic.enums.Player;

import java.util.Vector;

/**
 * Created by Joshua.Nabongo on 10/7/2015.
 */
public class Pawn extends Move {


    public Pawn(Player player, Owner owner, boolean allowBackstep) {
        super(player, owner, allowBackstep, false);
    }

    @Override
    public Vector<Step> getForcedMoves(int r, int c, Board board) {
        Vector<Step> furtherCaptures = new Vector<Step>();

        if (board.cell[r][c].equals(playerPiece)) {
            if (forwardLeftCapture(r, c, board) != null)
                furtherCaptures.add(forwardLeftCapture(r, c, board));
            if (forwardRightCapture(r, c, board) != null)
                furtherCaptures.add(forwardRightCapture(r, c, board));
        }

        if (allowBackstep) {
            if (backwardLeftCapture(r, c, board) != null)
                furtherCaptures.add(backwardLeftCapture(r, c, board));
            if (backwardRightCapture(r, c, board) != null)
                furtherCaptures.add(backwardRightCapture(r, c, board));
        }

        return furtherCaptures;
    }

    @Override
    public Vector<Step> calculateAllForcedMoves(Board board) {
        Vector<Step> forcedMoves = new Vector<Step>();


        // Scan across the board
        for (int r = 0; r < Board.rows; r++) {
            // Check only valid cols
            int c = (r % 2 == 0) ? 0 : 1;
            for (; c < Board.cols; c += 2) {
                assert (!board.cell[r][c].equals(CellEntry.inValid));

                // Forward Capture
                if (board.cell[r][c].equals(playerPiece)) {
                    // Boundary Condition for forward capture for black
                    if (r >= 2) {
                        // Forward Left Capture
                        if (forwardLeftCapture(r, c, board) != null)
                            forcedMoves.add(forwardLeftCapture(r, c, board));

                        // Forward Right Capture for black
                        if (forwardRightCapture(r, c, board) != null)
                            forcedMoves.add(forwardRightCapture(r, c, board));
                    }
                }
                // Backward Capture for Black King
                if (allowBackstep) {
                    // Boundary Condition for backward capture
                    if (r < Board.rows - 2) {
                        // Backward Left Capture for black king
                        if (backwardLeftCapture(r, c, board) != null)
                            forcedMoves.add(backwardLeftCapture(r, c, board));

                        // Backward Right Capture for black king
                        if (backwardRightCapture(r, c, board) != null)
                            forcedMoves.add(backwardRightCapture(r, c, board));
                    }
                }
            }
        }

        return forcedMoves;
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

                    // Forward Step for normal.
                    if (board.cell[r][c].equals(playerPiece)) {

                        Step step = null;
                        step = forwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = forwardRightCapture(r, c, board);
                        assert (step == null);
                        step = forwardLeft(r, c, board);
                        if (step != null)
                            allNonForcedMoves.add(step);

                        step = forwardRight(r, c, board);

                        if (step != null)
                            allNonForcedMoves.add(step);

                    }

                    //Forward and Backward Step for black king piece.
                    if (allowBackstep) {
                        Step step = null;
                        step = backwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = backwardRightCapture(r, c, board);
                        assert (step == null);
                    }
                }
            }
        } else {

            for (int r = 0; r < Board.rows; r++) {
                // Check only valid cols
                int c = (r % 2 == 0) ? 0 : 1;
                for (; c < Board.cols; c += 2) {
                    assert (!board.cell[r][c].equals(CellEntry.inValid));

                    // Forward Step for normal white piece.
                    if (board.cell[r][c].equals(playerPiece)) {

                        Step step = null;
                        step = forwardLeftCapture(r, c, board);
                        assert (step == null);
                        step = forwardRightCapture(r, c, board);
                        assert (step == null);

                        step = forwardLeft(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }

                        step = forwardRight(r, c, board);
                        if (step != null) {
                            allNonForcedMoves.add(step);
                        }
                    }

                    //Forward and Backward Step for black king piece.
                    if (allowBackstep) {
                        Step step = null;

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
