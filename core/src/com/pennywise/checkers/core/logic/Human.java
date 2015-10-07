/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;


import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Player;
import com.pennywise.checkers.core.logic.enums.ReturnCode;

import java.util.Vector;

/**
 * @author apurv
 */
public class Human {

    final CellEntry playerPawn;
    final CellEntry playerKing;
    final CellEntry opponentPawn;
    final CellEntry opponentKing;
    final Player player;
    final Board board;

    public Human(Player player, Board board) {

        this.player = player;
        this.board = board;
        //player pieces
        playerPawn = player.equals(Player.black) ? CellEntry.black : CellEntry.white;
        playerKing = player.equals(Player.black) ? CellEntry.blackKing : CellEntry.whiteKing;
        //opponent pieces
        opponentPawn = player.equals(Player.black) ? CellEntry.white : CellEntry.black;
        opponentKing = player.equals(Player.black) ? CellEntry.whiteKing : CellEntry.blackKing;

    }

    public ReturnCode Move(int r1, int c1, int r2, int c2, Board board) {

        UserInteractions.PrintSeparator('-');
        System.out.println("\t\t" + player.name() + "'s TURN");
        UserInteractions.PrintSeparator('-');

        ReturnCode returnCode = ReturnCode.INVALID_MOVE;
        Step step = new Step(r1, c1, r2, c2);

        if (player == Player.black)
            returnCode = makeNextBlackMoves(step);
        else
            returnCode = makeNextWhiteMoves(step);

        return returnCode;
    }

    public ReturnCode makeNextWhiteMoves(Step step) {
        return CheckValidMoveForWhiteHuman(step.initialRow, step.initialCol,
                step.finalRow, step.finalCol);
    }

    public ReturnCode makeNextBlackMoves(Step step) {
        return CheckValidMoveForBlackHuman(step.initialRow, step.initialCol, step.finalRow, step.finalCol);
    }

    private ReturnCode CheckValidMoveForWhiteHuman(int r1, int c1, int r2, int c2) {
        // Select Right Piece and Right Step
        if (board.cell[r1][c1].equals(CellEntry.inValid)
                || !(board.cell[r1][c1].equals(CellEntry.white) ||
                board.cell[r1][c1].equals(CellEntry.whiteKing)
        ) || !board.cell[r2][c2].equals(CellEntry.empty)) {
            UserInteractions.PrintSeparator('-');
            System.out.println("Check !!! White/Invalid Piece Selected or Invalid Step..... Try Again.");
            UserInteractions.PrintSeparator('-');
            return ReturnCode.INVALID_MOVE;
        }

        // Caution: Calculate forced steps at (r1,c1)-------------------------------------------
        Vector<Step> forcedMovesAtR1C1 = White.ObtainForcedMovesForWhite(r1, c1, board, playerPawn, playerKing, opponentPawn, opponentKing);

        // If Forced Step exists at (r1, c1)
        if (!forcedMovesAtR1C1.isEmpty()) {
            Step step = new Step(r1, c1, r2, c2);

            // Caution: if the current step is a forced step -------------------------------------------
            if (step.ExistsInVector(forcedMovesAtR1C1)) {
                // Capture Black Piece
                board.CaptureBlackPiece(r1, c1, r2, c2);
                // Update r1 to r2 and c1 to c2
                r1 = r2;
                c1 = c2;
                // Calculate further capture at (r1,c1)
                Vector<Step> furtherCapture = White.ObtainForcedMovesForWhite(r1, c1, board, playerPawn,
                        playerKing, opponentPawn, opponentKing);
                // No further capture
                if (furtherCapture.isEmpty()) {
                    return ReturnCode.VALID_MOVE;
                } else
                    return ReturnCode.MULTIPLE_CAPTURE;
            } else {
                UserInteractions.PrintSeparator('-');
                System.out.println("Check!!!Wrong Step....Try Again.");
                System.out.println("You have the following steps at: (r1: " + r1 + ", c1: " + c1 + ")");
                for (int i = 0; i < forcedMovesAtR1C1.size(); i++) {
                    System.out.print("Option " + (i + 1) + " : ");
                    System.out.print("------>(r2: " + forcedMovesAtR1C1.elementAt(i).finalRow + ", ");
                    System.out.println("c2: " + forcedMovesAtR1C1.elementAt(i).finalCol + ")");
                }

                UserInteractions.PrintSeparator('-');
                return ReturnCode.FORCED_MOVES;
            }
        }

        // If no forced move exists at (r1,c1)
        if (forcedMovesAtR1C1.isEmpty()) {
            // Caution: Calculate all forced steps for white at this state of the board-------------------
            Vector<Step> forcedSteps = White.CalculateAllForcedMovesForWhite(board, playerPawn, playerKing, opponentPawn, opponentKing);

            // No forced move exists at this state of the board for white
            if (forcedSteps.isEmpty()) {
                // Forward Step
                if (r2 - r1 == 1 && Math.abs(c2 - c1) == 1) {
                    board.MakeMove(r1, c1, r2, c2);
                    return ReturnCode.VALID_MOVE;
                }

                // Backward Step For WhiteKing
                else if (board.cell[r1][c1].equals(CellEntry.whiteKing)) {
                    if (r2 - r1 == -1 && Math.abs(c2 - c1) == 1) {
                        board.MakeMove(r1, c1, r2, c2);
                        return ReturnCode.VALID_MOVE;
                    }
                } else {
                    UserInteractions.PrintSeparator('-');
                    System.out.println("Check!!!Only Unit Step Step Allowed.......Try Again.\n");
                    UserInteractions.PrintSeparator('-');
                    return ReturnCode.INVALID_MOVE;
                }
            } else {
                UserInteractions.PrintSeparator('-');

                System.out.println("Forced Step exists!!!!!!!!!!!");
                System.out.println("You have the following options.");
                for (int i = 0; i < forcedSteps.size(); i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.print("(r1: " + forcedSteps.elementAt(i).initialRow + ", ");
                    System.out.print("c1: " + forcedSteps.elementAt(i).initialCol + ")");
                    System.out.print("------> (r2: " + forcedSteps.elementAt(i).finalRow + ", ");
                    System.out.println("c2: " + forcedSteps.elementAt(i).finalCol + ")");
                }

                UserInteractions.PrintSeparator('-');
                return ReturnCode.FORCED_MOVES;
            }
        }

        return ReturnCode.INVALID_MOVE;
    }

    private ReturnCode CheckValidMoveForBlackHuman(int r1, int c1, int r2, int c2) {
        // Select Right Piece and Right Step
        if (
                board.cell[r1][c1].equals(CellEntry.inValid)
                        || !(board.cell[r1][c1].equals(CellEntry.black) ||
                        board.cell[r1][c1].equals(CellEntry.blackKing))
                        || !board.cell[r2][c2].equals(CellEntry.empty)
                ) {
            UserInteractions.PrintSeparator('-');
            System.out.println("Check !!! White/Invalid Piece Selected or Invalid Step..... Try Again.");
            UserInteractions.PrintSeparator('-');
            return ReturnCode.INVALID_MOVE;
        }

        // Caution: Calculate forced steps at (r1,c1)-------------------------------------------
        Vector<Step> forcedMovesAtR1C1 = Black.ObtainForcedMovesForBlack(r1, c1, board, playerPawn
                , playerKing, opponentPawn, opponentKing);

        // If Forced Step exists at (r1, c1)
        if (!forcedMovesAtR1C1.isEmpty()) {
            Step step = new Step(r1, c1, r2, c2);

            // Caution: if the current step is a forced step -------------------------------------------
            if (step.ExistsInVector(forcedMovesAtR1C1)) {
                // Check if  further capture is possible

                // Capture White Piece
                board.CaptureWhitePiece(r1, c1, r2, c2);

                // Update r1 to r2 and c1 to c2
                r1 = r2;
                c1 = c2;

                // Calculate further capture at (r1,c1)
                Vector<Step> furtherCapture = Black.ObtainForcedMovesForBlack(r1, c1, board, playerPawn
                        , playerKing, opponentPawn, opponentKing);

                // Caution: No further capture--------------------------------------------
                if (furtherCapture.isEmpty()) {
                    return ReturnCode.VALID_MOVE;
                } else
                    return ReturnCode.MULTIPLE_CAPTURE;
            } else {
                UserInteractions.PrintSeparator('-');

                System.out.println("Forced Step exists!!!!!!!!!!!");
                System.out.println("You have the following steps at: (r1: " + r1 + ", c1: " + c1 + ")");
                for (int i = 0; i < forcedMovesAtR1C1.size(); i++) {
                    System.out.print("Option " + (i + 1) + " : ");
                    System.out.print("------>(r2: " + forcedMovesAtR1C1.elementAt(i).finalRow + ", ");
                    System.out.println("c2: " + forcedMovesAtR1C1.elementAt(i).finalCol + ")");
                }

                UserInteractions.PrintSeparator('-');
                return ReturnCode.FORCED_MOVES;
            }
        }

        // If no forced move exists at (r1,c1)
        if (forcedMovesAtR1C1.isEmpty()) {
            // Caution: Calculate all forced steps for black at this state of the board-------------------
            Vector<Step> forcedSteps = Black.CalculateAllForcedMovesForBlack(board, playerPawn,
                    playerKing, opponentPawn, opponentKing);

            // No forced move exists at this state of the board for black
            if (forcedSteps.isEmpty()) {
                // Forward Step for Black
                if (r2 - r1 == -1 && Math.abs(c2 - c1) == 1) {
                    board.MakeMove(r1, c1, r2, c2);
                    return ReturnCode.VALID_MOVE;
                }

                // Backward Step For BlackKing
                else if (board.cell[r1][c1].equals(CellEntry.blackKing)) {
                    if (r2 - r1 == 1 && Math.abs(c2 - c1) == 1) {
                        board.MakeMove(r1, c1, r2, c2);
                        return ReturnCode.VALID_MOVE;
                    }
                } else {
                    UserInteractions.PrintSeparator('-');
                    System.out.println("Check!!!Only Unit Step Step Allowed.......Try Again.");
                    UserInteractions.PrintSeparator('-');
                    return ReturnCode.INVALID_MOVE;
                }
            } else {
                UserInteractions.PrintSeparator('-');
                System.out.println("Forced Step exists!!!!!!!!!!!");
                System.out.println("You have the following options.");
                for (int i = 0; i < forcedSteps.size(); i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.print("(r1: " + forcedSteps.elementAt(i).initialRow + ", ");
                    System.out.print("c1: " + forcedSteps.elementAt(i).initialCol + ")");
                    System.out.print("------> (r2: " + forcedSteps.elementAt(i).finalRow + ", ");
                    System.out.println("c2: " + forcedSteps.elementAt(i).finalCol + ")");
                }

                UserInteractions.PrintSeparator('-');
                return ReturnCode.FORCED_MOVES;
            }
        }

        return ReturnCode.INVALID_MOVE;
    }
}
