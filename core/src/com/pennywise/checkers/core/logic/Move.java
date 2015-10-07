package com.pennywise.checkers.core.logic;

import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Owner;
import com.pennywise.checkers.core.logic.enums.Player;

import java.util.Vector;

/**
 * Created by Joshua.Nabongo on 10/7/2015.
 */
public abstract class Move {

    protected final Player player;
    protected final Owner owner;
    protected final CellEntry playerPiece;
    protected final CellEntry opponentPawn;
    protected final CellEntry opponentKing;
    protected boolean allowBackstep;
    protected final boolean allowKingjump;

    public Move(Player player, Owner owner, boolean allowBackstep, boolean allowKingjump) {
        this.player = player;
        this.owner = owner;
        this.allowBackstep = allowBackstep;
        this.allowKingjump = allowKingjump;

        playerPiece = player.equals(Player.black) ? CellEntry.black : CellEntry.white;
        opponentPawn = player.equals(Player.black) ? CellEntry.white : CellEntry.black;
        opponentKing = player.equals(Player.black) ? CellEntry.whiteKing : CellEntry.blackKing;
    }

    public abstract Vector<Step> calculateAllNonForcedMoves(Board board);

    public abstract Vector<Step> calculateAllForcedMoves(Board board);

    public abstract Vector<Step> getForcedMoves(int r, int c, Board board);

    protected Step forwardLeft(int r, int c, Board board) {
        Step forwardLeft = null;

        if (owner == Owner.ROBOT) {

            if (r >= 1 && c < Board.cols - 1 &&
                    board.cell[r - 1][c + 1] == CellEntry.empty

                    ) {
                forwardLeft = new Step(r, c, r - 1, c + 1);
            }
        } else {
            if (r < Board.rows - 1 && c >= 1 &&
                    board.cell[r + 1][c - 1] == CellEntry.empty
                    ) {
                forwardLeft = new Step(r, c, r + 1, c - 1);
            }
        }
        return forwardLeft;
    }

    // Forward Left Capture
    protected Step forwardLeftCapture(int r, int c, Board board) {
        Step forwardLeftCapture = null;

        if (owner == Owner.ROBOT) {
            if (r >= 2 && c < Board.cols - 2 &&
                    (
                            board.cell[r - 1][c + 1].equals(opponentPawn)
                                    || board.cell[r - 1][c + 1].equals(opponentKing)
                    )
                    && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                    ) {
                forwardLeftCapture = new Step(r, c, r - 2, c + 2);
            }
        } else {
            if (r < Board.rows - 2 && c >= 2 &&
                    (
                            board.cell[r + 1][c - 1].equals(opponentPawn)
                                    || board.cell[r + 1][c - 1].equals(opponentKing)
                    )
                    && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                    ) {
                forwardLeftCapture = new Step(r, c, r + 2, c - 2);
            }
        }
        return forwardLeftCapture;
    }

    //Forward Right
    protected Step forwardRight(int r, int c, Board board) {
        Step forwardRight = null;

        if (owner == Owner.ROBOT) {
            if (r >= 1 && c >= 1 &&
                    board.cell[r - 1][c - 1] == CellEntry.empty
                    ) {
                forwardRight = new Step(r, c, r - 1, c - 1);
            }
        } else {
            if (r < Board.rows - 1 && c < Board.cols - 1 &&
                    board.cell[r + 1][c + 1] == CellEntry.empty
                    ) {
                forwardRight = new Step(r, c, r + 1, c + 1);
            }
        }
        return forwardRight;
    }

    // Forward Right Capture
    protected Step forwardRightCapture(int r, int c, Board board) {

        Step forwardRightCapture = null;

        if (owner == Owner.ROBOT) {
            if (r >= 2 && c >= 2 && (
                    board.cell[r - 1][c - 1].equals(opponentPawn)
                            || board.cell[r - 1][c - 1].equals(opponentKing)
            )
                    && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                    ) {
                forwardRightCapture = new Step(r, c, r - 2, c - 2);

            }
        } else {
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
        }
        return forwardRightCapture;
    }

    protected Step backwardLeft(int r, int c, Board board) {
        Step backwardLeft = null;

        if (owner == Owner.ROBOT) {
            if (r < Board.rows - 1 && c < Board.cols - 1 &&
                    board.cell[r + 1][c + 1] == CellEntry.empty
                    ) {
                backwardLeft = new Step(r, c, r + 1, c + 1);
            }
        } else {
            if (r >= 1 && c >= 1 &&
                    board.cell[r - 1][c - 1] == CellEntry.empty
                    ) {
                backwardLeft = new Step(r, c, r - 1, c - 1);
            }
        }

        return backwardLeft;
    }

    // Backward Left Capture
    protected Step backwardLeftCapture(int r, int c, Board board) {

        Step backwardLeftCapture = null;

        if (owner == Owner.ROBOT) {

            if (r < Board.rows - 2 && c < Board.cols - 2 && (
                    board.cell[r + 1][c + 1].equals(opponentPawn)
                            || board.cell[r + 1][c + 1].equals(opponentKing)
            )
                    && board.cell[r + 2][c + 2].equals(CellEntry.empty)
                    ) {
                backwardLeftCapture = new Step(r, c, r + 2, c + 2);

            }
        } else {

            if (r >= 2 && c >= 2 && (
                    board.cell[r - 1][c - 1].equals(opponentPawn)
                            || board.cell[r - 1][c - 1].equals(opponentKing)
            )
                    && board.cell[r - 2][c - 2].equals(CellEntry.empty)
                    ) {
                backwardLeftCapture = new Step(r, c, r - 2, c - 2);
            }
        }

        return backwardLeftCapture;
    }

    protected Step backwardRight(int r, int c, Board board) {
        Step backwardRight = null;


        if (owner == Owner.ROBOT) {
            if (r < Board.rows - 1 && c >= 1 &&
                    board.cell[r + 1][c - 1].equals(CellEntry.empty)
                    ) {
                backwardRight = new Step(r, c, r + 1, c - 1);
            }
        } else {

            if (r >= 1 && c < Board.cols - 1 &&
                    board.cell[r - 1][c + 1] == CellEntry.empty
                    ) {
                backwardRight = new Step(r, c, r - 1, c + 1);
            }
        }

        return backwardRight;
    }

    // Backward Right Capture
    protected Step backwardRightCapture(int r, int c, Board board) {

        Step backwardRightCapture = null;

        if (owner == Owner.ROBOT) {

            if (r < Board.rows - 2 && c >= 2 && (
                    board.cell[r + 1][c - 1].equals(opponentPawn) ||
                            board.cell[r + 1][c - 1].equals(opponentKing)
            )
                    && board.cell[r + 2][c - 2].equals(CellEntry.empty)
                    ) {
                backwardRightCapture = new Step(r, c, r + 2, c - 2);
            }
        } else {

            if (r >= 2 && c < Board.cols - 2 && (
                    board.cell[r - 1][c + 1].equals(opponentPawn) ||
                            board.cell[r - 1][c + 1].equals(opponentKing)
            )
                    && board.cell[r - 2][c + 2].equals(CellEntry.empty)
                    ) {
                backwardRightCapture = new Step(r, c, r - 2, c + 2);
            }
        }

        return backwardRightCapture;
    }

}
