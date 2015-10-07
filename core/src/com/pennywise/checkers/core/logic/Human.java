/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;


import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.Owner;
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
        Move move = new Move(r1, c1, r2, c2);

        if (player == Player.black)
            returnCode = makeNextBlackMoves(move);
        else
            returnCode = makeNextWhiteMoves(move);

        return returnCode;
    }

    public ReturnCode makeNextWhiteMoves(Move move) {
        return CheckValidMoveForWhiteHuman(move.initialRow, move.initialCol,
                move.finalRow, move.finalCol);
    }

    public ReturnCode makeNextBlackMoves(Move move) {
        return CheckValidMoveForBlackHuman(move.initialRow, move.initialCol, move.finalRow, move.finalCol);
    }

    private ReturnCode CheckValidMoveForWhiteHuman(int r1, int c1, int r2, int c2) {
        // Select Right Piece and Right Move
        if (board.cell[r1][c1].equals(CellEntry.inValid)
                || !(board.cell[r1][c1].equals(CellEntry.white) ||
                board.cell[r1][c1].equals(CellEntry.whiteKing)
        ) || !board.cell[r2][c2].equals(CellEntry.empty)) {
            UserInteractions.PrintSeparator('-');
            System.out.println("Check !!! White/Invalid Piece Selected or Invalid Move..... Try Again.");
            UserInteractions.PrintSeparator('-');
            return ReturnCode.INVALID_MOVE;
        }

        // Caution: Calculate forced moves at (r1,c1)-------------------------------------------
        Vector<Move> forcedMovesAtR1C1 = White.ObtainForcedMovesForWhite(r1, c1, board, playerPawn, playerKing, opponentPawn, opponentKing);

        // If Forced Move exists at (r1, c1)
        if (!forcedMovesAtR1C1.isEmpty()) {
            Move move = new Move(r1, c1, r2, c2);

            // Caution: if the current move is a forced move -------------------------------------------
            if (move.ExistsInVector(forcedMovesAtR1C1)) {
                // Capture Black Piece
                board.CaptureBlackPiece(r1, c1, r2, c2);
                // Update r1 to r2 and c1 to c2
                r1 = r2;
                c1 = c2;
                // Calculate further capture at (r1,c1)
                Vector<Move> furtherCapture = White.ObtainForcedMovesForWhite(r1, c1, board, playerPawn,
                        playerKing, opponentPawn, opponentKing);
                // No further capture
                if (furtherCapture.isEmpty()) {
                    return ReturnCode.VALID_MOVE;
                } else
                    return ReturnCode.MULTIPLE_CAPTURE;
            } else {
                UserInteractions.PrintSeparator('-');
                System.out.println("Check!!!Wrong Move....Try Again.");
                System.out.println("You have the following moves at: (r1: " + r1 + ", c1: " + c1 + ")");
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
            // Caution: Calculate all forced moves for white at this state of the board-------------------
            Vector<Move> forcedMoves = White.CalculateAllForcedMovesForWhite(board, playerPawn, playerKing, opponentPawn, opponentKing);

            // No forced move exists at this state of the board for white
            if (forcedMoves.isEmpty()) {
                // Forward Move
                if (r2 - r1 == 1 && Math.abs(c2 - c1) == 1) {
                    board.MakeMove(r1, c1, r2, c2);
                    return ReturnCode.VALID_MOVE;
                }

                // Backward Move For WhiteKing
                else if (board.cell[r1][c1].equals(CellEntry.whiteKing)) {
                    if (r2 - r1 == -1 && Math.abs(c2 - c1) == 1) {
                        board.MakeMove(r1, c1, r2, c2);
                        return ReturnCode.VALID_MOVE;
                    }
                } else {
                    UserInteractions.PrintSeparator('-');
                    System.out.println("Check!!!Only Unit Step Move Allowed.......Try Again.\n");
                    UserInteractions.PrintSeparator('-');
                    return ReturnCode.INVALID_MOVE;
                }
            } else {
                UserInteractions.PrintSeparator('-');

                System.out.println("Forced Move exists!!!!!!!!!!!");
                System.out.println("You have the following options.");
                for (int i = 0; i < forcedMoves.size(); i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.print("(r1: " + forcedMoves.elementAt(i).initialRow + ", ");
                    System.out.print("c1: " + forcedMoves.elementAt(i).initialCol + ")");
                    System.out.print("------> (r2: " + forcedMoves.elementAt(i).finalRow + ", ");
                    System.out.println("c2: " + forcedMoves.elementAt(i).finalCol + ")");
                }

                UserInteractions.PrintSeparator('-');
                return ReturnCode.FORCED_MOVES;
            }
        }

        return ReturnCode.INVALID_MOVE;
    }

    private ReturnCode CheckValidMoveForBlackHuman(int r1, int c1, int r2, int c2) {
        // Select Right Piece and Right Move
        if (
                board.cell[r1][c1].equals(CellEntry.inValid)
                        || !(board.cell[r1][c1].equals(CellEntry.black) ||
                        board.cell[r1][c1].equals(CellEntry.blackKing))
                        || !board.cell[r2][c2].equals(CellEntry.empty)
                ) {
            UserInteractions.PrintSeparator('-');
            System.out.println("Check !!! White/Invalid Piece Selected or Invalid Move..... Try Again.");
            UserInteractions.PrintSeparator('-');
            return ReturnCode.INVALID_MOVE;
        }

        // Caution: Calculate forced moves at (r1,c1)-------------------------------------------
        Vector<Move> forcedMovesAtR1C1 = Black.ObtainForcedMovesForBlack(r1, c1, board, playerPawn
                , playerKing, opponentPawn, opponentKing);

        // If Forced Move exists at (r1, c1)
        if (!forcedMovesAtR1C1.isEmpty()) {
            Move move = new Move(r1, c1, r2, c2);

            // Caution: if the current move is a forced move -------------------------------------------
            if (move.ExistsInVector(forcedMovesAtR1C1)) {
                // Check if  further capture is possible

                // Capture White Piece
                board.CaptureWhitePiece(r1, c1, r2, c2);

                // Update r1 to r2 and c1 to c2
                r1 = r2;
                c1 = c2;

                // Calculate further capture at (r1,c1)
                Vector<Move> furtherCapture = Black.ObtainForcedMovesForBlack(r1, c1, board, playerPawn
                        , playerKing, opponentPawn, opponentKing);

                // Caution: No further capture--------------------------------------------
                if (furtherCapture.isEmpty()) {
                    return ReturnCode.VALID_MOVE;
                } else
                    return ReturnCode.MULTIPLE_CAPTURE;
            } else {
                UserInteractions.PrintSeparator('-');

                System.out.println("Forced Move exists!!!!!!!!!!!");
                System.out.println("You have the following moves at: (r1: " + r1 + ", c1: " + c1 + ")");
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
            // Caution: Calculate all forced moves for black at this state of the board-------------------
            Vector<Move> forcedMoves = Black.CalculateAllForcedMovesForBlack(board, playerPawn,
                    playerKing, opponentPawn, opponentKing);

            // No forced move exists at this state of the board for black
            if (forcedMoves.isEmpty()) {
                // Forward Move for Black
                if (r2 - r1 == -1 && Math.abs(c2 - c1) == 1) {
                    board.MakeMove(r1, c1, r2, c2);
                    return ReturnCode.VALID_MOVE;
                }

                // Backward Move For BlackKing
                else if (board.cell[r1][c1].equals(CellEntry.blackKing)) {
                    if (r2 - r1 == 1 && Math.abs(c2 - c1) == 1) {
                        board.MakeMove(r1, c1, r2, c2);
                        return ReturnCode.VALID_MOVE;
                    }
                } else {
                    UserInteractions.PrintSeparator('-');
                    System.out.println("Check!!!Only Unit Step Move Allowed.......Try Again.");
                    UserInteractions.PrintSeparator('-');
                    return ReturnCode.INVALID_MOVE;
                }
            } else {
                UserInteractions.PrintSeparator('-');
                System.out.println("Forced Move exists!!!!!!!!!!!");
                System.out.println("You have the following options.");
                for (int i = 0; i < forcedMoves.size(); i++) {
                    System.out.print((i + 1) + ". ");
                    System.out.print("(r1: " + forcedMoves.elementAt(i).initialRow + ", ");
                    System.out.print("c1: " + forcedMoves.elementAt(i).initialCol + ")");
                    System.out.print("------> (r2: " + forcedMoves.elementAt(i).finalRow + ", ");
                    System.out.println("c2: " + forcedMoves.elementAt(i).finalCol + ")");
                }

                UserInteractions.PrintSeparator('-');
                return ReturnCode.FORCED_MOVES;
            }
        }

        return ReturnCode.INVALID_MOVE;
    }
}
