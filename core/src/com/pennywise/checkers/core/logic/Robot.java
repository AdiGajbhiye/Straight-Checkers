package com.pennywise.checkers.core.logic;

import java.util.Vector;

import com.pennywise.checkers.core.logic.enums.Player;
import com.pennywise.checkers.core.logic.enums.CellEntry;
import com.pennywise.checkers.core.logic.enums.ReturnCode;

/**
 * @author apurv
 */
public class Robot {

    private Oracle oracle = new Oracle();
    private final int max_depth;

    final CellEntry playerPawn;
    final CellEntry playerKing;
    final CellEntry opponentPawn;
    final CellEntry opponentKing;
    final Player player;
    final Board board;

    public Robot(Player player, Board board, int max_depth) {

        this.player = player;
        this.board = board;
        //player pieces
        playerPawn = player.equals(Player.black) ? CellEntry.black : CellEntry.white;
        playerKing = player.equals(Player.black) ? CellEntry.blackKing : CellEntry.whiteKing;
        //opponent pieces
        opponentPawn = player.equals(Player.black) ? CellEntry.white : CellEntry.black;
        opponentKing = player.equals(Player.black) ? CellEntry.whiteKing : CellEntry.blackKing;

        this.max_depth = max_depth;
    }

    public void Move() {

        UserInteractions.PrintSeparator('-');
        System.out.println("\t\t" + player.name() + "'s TURN");
        UserInteractions.PrintSeparator('-');


        if (player == Player.black)
            makeNextBlackMoves();
        else
            makeNextWhiteMoves();

    }

    protected Vector<Move> makeNextWhiteMoves() {

        Vector<Move> resultantMoveSeq = new Vector<Move>();

        alphaBeta(board, player, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);

        //Apply the move to the game board.
        for (Move m : resultantMoveSeq) {
            board.genericMakeWhiteMove(m);
        }

        System.out.print("Robot's Move was ");
        UserInteractions.DisplayMoveSeq(resultantMoveSeq);
        System.out.println();

        return resultantMoveSeq;
    }


    public Vector<Move> makeNextBlackMoves() {

        Vector<Move> resultantMoveSeq = new Vector<Move>();

        alphaBeta(board, player, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);

        //Apply the move to the game board.
        for (Move m : resultantMoveSeq) {
            board.genericMakeBlackMove(m);
        }

        System.out.print("Robot's Move was ");
        UserInteractions.DisplayMoveSeq(resultantMoveSeq);
        System.out.println();

        return resultantMoveSeq;
    }

    /**
     * White represents the maximizing player, he/she wants to maximize the value of the board.
     * Black is the minimizing player, he/she wants to minimize the value of the board.
     * <p/>
     * alpha represents the maximum value that the max player is assured of, initially -inf.
     * beta represents the minimum value that the min player is assured of, initially +inf.
     * <p/>
     * if(alpha>beta) break
     */
    private int alphaBeta(Board board, Player player, int depth, int alpha, int beta, Vector<Move> resultMoveSeq) {

        if (!canExploreFurther(board, player, depth)) {
            int value = oracle.evaluateBoard(board, player);
            return value;
        }

        Vector<Vector<Move>> possibleMoveSeq = expandMoves(board, player);
        Vector<Board> possibleBoardConf = getPossibleBoardConf(board, possibleMoveSeq, player);

        Vector<Move> bestMoveSeq = null;

        if (player == Player.white) {

            for (int i = 0; i < possibleBoardConf.size(); i++) {

                Board b = possibleBoardConf.get(i);
                Vector<Move> moveSeq = possibleMoveSeq.get(i);

                int value = alphaBeta(b, Player.black, depth + 1, alpha, beta, resultMoveSeq);

                if (value > alpha) {
                    alpha = value;
                    bestMoveSeq = moveSeq;
                }
                if (alpha > beta) {
                    break;
                }
            }

            //If the depth is 0, copy the bestMoveSeq in the result move seq.
            if (depth == 0 && bestMoveSeq != null) {
                resultMoveSeq.addAll(bestMoveSeq);
            }

            return alpha;

        } else {
            assert (player == Player.black);

            for (int i = 0; i < possibleBoardConf.size(); i++) {

                Board b = possibleBoardConf.get(i);
                Vector<Move> moveSeq = possibleMoveSeq.get(i);

                int value = alphaBeta(b, Player.white, depth + 1, alpha, beta, resultMoveSeq);
                if (value < beta) {
                    bestMoveSeq = moveSeq;
                    beta = value;
                }
                if (alpha > beta) {
                    break;
                }
            }
            //If the depth is 0, copy the bestMoveSeq in the result move seq.
            if (depth == 0 && bestMoveSeq != null) {
                resultMoveSeq.addAll(bestMoveSeq);
            }

            return beta;
        }
    }

    public Vector<Vector<Move>> expandMoves(Board board, Player player) {

        Vector<Vector<Move>> outerVector = new Vector<Vector<Move>>();

        if (player == Player.black) {

            Vector<Move> moves = null;
            moves = Black.CalculateAllForcedMovesForBlack(board, playerPawn, playerKing, opponentPawn, opponentKing);

            if (moves.isEmpty()) {
                moves = Black.CalculateAllNonForcedMovesForBlack(board, playerPawn, playerKing, opponentPawn, opponentKing);

                for (Move m : moves) {
                    Vector<Move> innerVector = new Vector<Move>();
                    innerVector.add(m);
                    outerVector.add(innerVector);
                }

            } else {
                for (Move m : moves) {

                    int r = m.finalRow;
                    int c = m.finalCol;
                    Vector<Move> innerVector = new Vector<Move>();

                    innerVector.add(m);

                    Board boardCopy = board.duplicate();
                    boardCopy.genericMakeBlackMove(m);
                    expandMoveRecursivelyForBlack(boardCopy, outerVector, innerVector, r, c);

                    innerVector.remove(m);

                }
            }

        } else if (player == Player.white) {

            Vector<Move> moves = null;

            moves = White.CalculateAllForcedMovesForWhite(board, playerPawn, playerKing, opponentPawn, opponentKing);
            if (moves.isEmpty()) {
                moves = White.CalculateAllNonForcedMovesForWhite(board, playerPawn, playerKing, opponentPawn, opponentKing);
                for (Move m : moves) {
                    Vector<Move> innerVector = new Vector<Move>();
                    innerVector.add(m);
                    outerVector.add(innerVector);
                }
            } else {
                for (Move m : moves) {

                    int r = m.finalRow;
                    int c = m.finalCol;
                    Vector<Move> innerVector = new Vector<Move>();

                    innerVector.add(m);

                    Board boardCopy = board.duplicate();
                    boardCopy.genericMakeWhiteMove(m);
                    expandMoveRecursivelyForWhite(boardCopy, outerVector, innerVector, r, c);

                    innerVector.remove(m);

                }

            }
        }
        return outerVector;
    }

    private void expandMoveRecursivelyForWhite(Board board, Vector<Vector<Move>> outerVector, Vector<Move> innerVector, int r, int c) {

        Vector<Move> forcedMoves = White.ObtainForcedMovesForWhite(r, c, board, playerPawn, playerKing, opponentPawn, opponentKing);

        if (forcedMoves.isEmpty()) {
            Vector<Move> innerCopy = (Vector<Move>) innerVector.clone();
            outerVector.add(innerCopy);
            return;

        } else {
            for (Move m : forcedMoves) {

                com.pennywise.checkers.core.logic.Board boardCopy = board.duplicate();
                boardCopy.genericMakeWhiteMove(m);

                innerVector.add(m);
                expandMoveRecursivelyForWhite(boardCopy, outerVector, innerVector, m.finalRow, m.finalCol);
                innerVector.remove(m);

            }
        }


    }

    private void expandMoveRecursivelyForBlack(com.pennywise.checkers.core.logic.Board board, Vector<Vector<Move>> outerVector, Vector<Move> innerVector, int r, int c) {

        Vector<Move> forcedMoves = Black.ObtainForcedMovesForBlack(r, c, board, playerPawn, playerKing, opponentPawn, opponentKing);

        if (forcedMoves.isEmpty()) {
            Vector<Move> innerCopy = (Vector<Move>) innerVector.clone();
            outerVector.add(innerCopy);
            return;

        } else {
            for (Move m : forcedMoves) {

                Board boardCopy = board.duplicate();
                boardCopy.genericMakeBlackMove(m);

                innerVector.add(m);
                expandMoveRecursivelyForBlack(boardCopy, outerVector, innerVector, m.finalRow, m.finalCol);
                innerVector.remove(m);

            }
        }
    }


    private boolean canExploreFurther(Board board, Player player, int depth) {
        boolean res = true;
        if (board.CheckGameComplete() || board.CheckGameDraw(player)) {
            res = false;
        }
        if (depth == max_depth) {
            res = false;
        }
        return res;
    }


    public Vector<Board> getPossibleBoardConf(Board board, Vector<Vector<Move>> possibleMoveSeq, Player player) {
        Vector<Board> possibleBoardConf = new Vector<Board>();

        for (Vector<Move> moveSeq : possibleMoveSeq) {
            Board boardCopy = board.duplicate();
            for (Move move : moveSeq) {
                if (player == Player.black) {
                    boardCopy.genericMakeBlackMove(move);

                } else {
                    boardCopy.genericMakeWhiteMove(move);
                }
            }
            possibleBoardConf.add(boardCopy);
            //System.out.println();
        }

        return possibleBoardConf;
    }
}