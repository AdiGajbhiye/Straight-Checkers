package com.pennywise.checkers.core.logic;

import java.util.Vector;

import com.pennywise.checkers.core.logic.enums.Player;
import com.pennywise.checkers.core.logic.enums.CellEntry;

/**
 * @author apurv
 */
public class Robot {

    static Oracle oracle = new Oracle();
    static int MAX_DEPTH = 2;

    public static void makeNextWhiteMoves(Board board) {

        Vector<Move> resultantMoveSeq = new Vector<Move>();

        alphaBeta(board, Player.white, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);

        //Apply the move to the game board.
        for (Move m : resultantMoveSeq) {
            board.genericMakeWhiteMove(m);
        }

        System.out.print("Robot's Move was ");
        UserInteractions.DisplayMoveSeq(resultantMoveSeq);
        System.out.println();
    }


    public static Vector<Move> makeNextBlackMoves(Board board) {

        Vector<Move> resultantMoveSeq = new Vector<Move>();

        alphaBeta(board, Player.black, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantMoveSeq);

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
    private static int alphaBeta(Board board, Player player, int depth, int alpha, int beta, Vector<Move> resultMoveSeq) {

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

    public static Vector<Vector<Move>> expandMoves(Board board, Player player) {

        Vector<Vector<Move>> outerVector = new Vector<Vector<Move>>();

        if (player == Player.black) {

            Vector<Move> moves = null;
            moves = Black.CalculateAllForcedMovesForBlack(board);

            if (moves.isEmpty()) {
                moves = Black.CalculateAllNonForcedMovesForBlack(board);

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

            moves = White.CalculateAllForcedMovesForWhite(board);
            if (moves.isEmpty()) {
                moves = White.CalculateAllNonForcedMovesForWhite(board);
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

    private static void expandMoveRecursivelyForWhite(Board board, Vector<Vector<Move>> outerVector, Vector<Move> innerVector, int r, int c) {

        Vector<Move> forcedMoves = White.ObtainForcedMovesForWhite(r, c, board);

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

    private static void expandMoveRecursivelyForBlack(Board board, Vector<Vector<Move>> outerVector, Vector<Move> innerVector, int r, int c) {

        Vector<Move> forcedMoves = Black.ObtainForcedMovesForBlack(r, c, board);

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


    private static boolean canExploreFurther(com.pennywise.checkers.core.logic.Board board, Player player, int depth) {
        boolean res = true;
        if (board.CheckGameComplete() || board.CheckGameDraw(player)) {
            res = false;
        }
        if (depth == MAX_DEPTH) {
            res = false;
        }
        return res;
    }


    public static Vector<Board> getPossibleBoardConf(Board board, Vector<Vector<Move>> possibleMoveSeq, Player player) {
        Vector<Board> possibleBoardConf = new Vector<com.pennywise.checkers.core.logic.Board>();

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