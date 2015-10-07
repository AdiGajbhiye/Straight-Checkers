package com.pennywise.checkers.core.logic;

import java.util.Vector;

import com.pennywise.checkers.core.logic.enums.Player;
import com.pennywise.checkers.core.logic.enums.CellEntry;

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

    protected Vector<Step> makeNextWhiteMoves() {

        Vector<Step> resultantStepSeq = new Vector<Step>();

        alphaBeta(board, player, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantStepSeq);

        //Apply the move to the game board.
        for (Step m : resultantStepSeq) {
            board.genericMakeWhiteMove(m);
        }

        System.out.print("Robot's Step was ");
        UserInteractions.DisplayMoveSeq(resultantStepSeq);
        System.out.println();

        return resultantStepSeq;
    }


    public Vector<Step> makeNextBlackMoves() {

        Vector<Step> resultantStepSeq = new Vector<Step>();

        alphaBeta(board, player, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, resultantStepSeq);

        //Apply the move to the game board.
        for (Step m : resultantStepSeq) {
            board.genericMakeBlackMove(m);
        }

        System.out.print("Robot's Step was ");
        UserInteractions.DisplayMoveSeq(resultantStepSeq);
        System.out.println();

        return resultantStepSeq;
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
    private int alphaBeta(Board board, Player player, int depth, int alpha, int beta, Vector<Step> resultStepSeq) {

        if (!canExploreFurther(board, player, depth)) {
            int value = oracle.evaluateBoard(board, player);
            return value;
        }

        Vector<Vector<Step>> possibleMoveSeq = expandMoves(board, player);
        Vector<Board> possibleBoardConf = getPossibleBoardConf(board, possibleMoveSeq, player);

        Vector<Step> bestStepSeq = null;

        if (player == Player.white) {

            for (int i = 0; i < possibleBoardConf.size(); i++) {

                Board b = possibleBoardConf.get(i);
                Vector<Step> stepSeq = possibleMoveSeq.get(i);

                int value = alphaBeta(b, Player.black, depth + 1, alpha, beta, resultStepSeq);

                if (value > alpha) {
                    alpha = value;
                    bestStepSeq = stepSeq;
                }
                if (alpha > beta) {
                    break;
                }
            }

            //If the depth is 0, copy the bestStepSeq in the result move seq.
            if (depth == 0 && bestStepSeq != null) {
                resultStepSeq.addAll(bestStepSeq);
            }

            return alpha;

        } else {
            assert (player == Player.black);

            for (int i = 0; i < possibleBoardConf.size(); i++) {

                Board b = possibleBoardConf.get(i);
                Vector<Step> stepSeq = possibleMoveSeq.get(i);

                int value = alphaBeta(b, Player.white, depth + 1, alpha, beta, resultStepSeq);
                if (value < beta) {
                    bestStepSeq = stepSeq;
                    beta = value;
                }
                if (alpha > beta) {
                    break;
                }
            }
            //If the depth is 0, copy the bestStepSeq in the result move seq.
            if (depth == 0 && bestStepSeq != null) {
                resultStepSeq.addAll(bestStepSeq);
            }

            return beta;
        }
    }

    public boolean CheckGameDraw(Player turn) {

        Vector<Vector<Step>> possibleMoveSeq = expandMoves(board.duplicate(), turn);

        if (possibleMoveSeq.isEmpty()) {
            return true;

        } else {
            return false;
        }
    }

    public Vector<Vector<Step>> expandMoves(Board board, Player player) {

        Vector<Vector<Step>> outerVector = new Vector<Vector<Step>>();

        if (player == Player.black) {

            Vector<Step> steps = null;
            steps = Black.CalculateAllForcedMovesForBlack(board, playerPawn, playerKing, opponentPawn, opponentKing);

            if (steps.isEmpty()) {
                steps = Black.CalculateAllNonForcedMovesForBlack(board, playerPawn, playerKing, opponentPawn, opponentKing);

                for (Step m : steps) {
                    Vector<Step> innerVector = new Vector<Step>();
                    innerVector.add(m);
                    outerVector.add(innerVector);
                }

            } else {
                for (Step m : steps) {

                    int r = m.finalRow;
                    int c = m.finalCol;
                    Vector<Step> innerVector = new Vector<Step>();

                    innerVector.add(m);

                    Board boardCopy = board.duplicate();
                    boardCopy.genericMakeBlackMove(m);
                    expandMoveRecursivelyForBlack(boardCopy, outerVector, innerVector, r, c);

                    innerVector.remove(m);

                }
            }

        } else if (player == Player.white) {

            Vector<Step> steps = null;

            steps = White.CalculateAllForcedMovesForWhite(board, playerPawn, playerKing, opponentPawn, opponentKing);
            if (steps.isEmpty()) {
                steps = White.CalculateAllNonForcedMovesForWhite(board, playerPawn, playerKing, opponentPawn, opponentKing);
                for (Step m : steps) {
                    Vector<Step> innerVector = new Vector<Step>();
                    innerVector.add(m);
                    outerVector.add(innerVector);
                }
            } else {
                for (Step m : steps) {

                    int r = m.finalRow;
                    int c = m.finalCol;
                    Vector<Step> innerVector = new Vector<Step>();

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

    private void expandMoveRecursivelyForWhite(Board board, Vector<Vector<Step>> outerVector, Vector<Step> innerVector, int r, int c) {

        Vector<Step> forcedSteps = White.ObtainForcedMovesForWhite(r, c, board, playerPawn, playerKing, opponentPawn, opponentKing);

        if (forcedSteps.isEmpty()) {
            Vector<Step> innerCopy = (Vector<Step>) innerVector.clone();
            outerVector.add(innerCopy);
            return;

        } else {
            for (Step m : forcedSteps) {

                com.pennywise.checkers.core.logic.Board boardCopy = board.duplicate();
                boardCopy.genericMakeWhiteMove(m);

                innerVector.add(m);
                expandMoveRecursivelyForWhite(boardCopy, outerVector, innerVector, m.finalRow, m.finalCol);
                innerVector.remove(m);

            }
        }


    }

    private void expandMoveRecursivelyForBlack(com.pennywise.checkers.core.logic.Board board, Vector<Vector<Step>> outerVector, Vector<Step> innerVector, int r, int c) {

        Vector<Step> forcedSteps = Black.ObtainForcedMovesForBlack(r, c, board, playerPawn, playerKing, opponentPawn, opponentKing);

        if (forcedSteps.isEmpty()) {
            Vector<Step> innerCopy = (Vector<Step>) innerVector.clone();
            outerVector.add(innerCopy);
            return;

        } else {
            for (Step m : forcedSteps) {

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
        if (board.CheckGameComplete() || CheckGameDraw(player)) {
            res = false;
        }
        if (depth == max_depth) {
            res = false;
        }
        return res;
    }


    public Vector<Board> getPossibleBoardConf(Board board, Vector<Vector<Step>> possibleMoveSeq, Player player) {
        Vector<Board> possibleBoardConf = new Vector<Board>();

        for (Vector<Step> stepSeq : possibleMoveSeq) {
            Board boardCopy = board.duplicate();
            for (Step step : stepSeq) {
                if (player == Player.black) {
                    boardCopy.genericMakeBlackMove(step);

                } else {
                    boardCopy.genericMakeWhiteMove(step);
                }
            }
            possibleBoardConf.add(boardCopy);
            //System.out.println();
        }

        return possibleBoardConf;
    }
}