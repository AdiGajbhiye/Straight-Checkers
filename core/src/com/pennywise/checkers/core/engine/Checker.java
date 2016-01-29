package com.pennywise.checkers.core.engine;

import java.util.Vector;

/**
 * Created by CHOXXY on 11/17/2015.
 */
public class Checker {
    public final static int LEGALMOVE = 1;
    public final static int ILLEGALMOVE = 2;
    public final static int INCOMLETEMOVE = 3;

    public static final int BLACKNORMAL = 1;
    public static final int WHITENORMAL = 2;
    public static final int BLACKKING = 3;
    public static final int WHITEKING = 4;
    public static final int EMPTY = 0;

    public static int[] getIndex(float x, float  y, float cellsize) {
        int[] index = new int[2];

        index[0] = ((int)(x / cellsize));
        index[1] = ((int)(y / cellsize));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (((i * cellsize) < x)
                        && (((i * cellsize) + cellsize) > x)
                        && ((j * cellsize) < y)
                        && (((j * cellsize) + cellsize) > y)) {
                    index[0] = i;
                    index[1] = j;
                    return index;
                }
            }
        }
        return index;
    }

    /*
    noMovesLeft return true if no more movents are left for the next player
    else it returns false
    */
    public static boolean noMovesLeft(int[][] board, int toMove) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((float) (i + j) / 2 != (i + j) / 2) {
                    if (toMove == BLACKNORMAL &&
                            (board[i][j] == BLACKNORMAL ||
                                    board[i][j] == BLACKKING)) {
                        if (canWalk(board, i, j)) return false;
                        else if (canCapture(board, i, j)) return false;
                    } else if (toMove == WHITENORMAL &&
                            (board[i][j] == WHITENORMAL ||
                                    board[i][j] == WHITEKING)) {
                        if (canWalk(board, i, j)) return false;
                        else if (canCapture(board, i, j)) return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     ApplyMove checks if the move entered is legal, illegal or incomplete.
     If IsMoveLegal returns INCOMLETEMOVE, this means a capture has just been made.
     Call canCapture() to see     if a further capture is possible. clears old position and
     Move checker to a new position
    */

    public static int ApplyMove(int[][] board, int srtI, int srtJ, int endI, int endJ) {
        int result = isMoveLegal(board, srtI, srtJ, endI, endJ, colour(board[srtI][srtJ]));

        if (result != ILLEGALMOVE) {
            if (Math.abs(endI - srtI) == 1) {
                board[endI][endJ] = board[srtI][srtJ];          //declare a checker there
                board[srtI][srtJ] = EMPTY;             //clear the previous cell.
            } else // capture
            {
                board[(srtI + endI) / 2][(srtJ + endJ) / 2] = EMPTY;
                board[endI][endJ] = board[srtI][srtJ];
                board[srtI][srtJ] = EMPTY;
            }

            if (result == INCOMLETEMOVE) {
                // if there are no further captures
                if (!(canCapture(board, endI, endJ)))
                    result = LEGALMOVE;
            }

            // check for new king
            if (board[endI][endJ] == BLACKNORMAL && endJ == 7) {
                board[endI][endJ] = BLACKKING;
            } else if (board[endI][endJ] == WHITENORMAL && endJ == 0) {
                board[endI][endJ] = WHITEKING;
            }


        }
        return result;
    }

    /*
     IsMoveLegal checks if the move entered is legal.
     Returns ILLEGALMOVE or legalMove;
     have to check with canCapture(int[][],int,int) to see
      if there is another capture possible after the first capture
     Returns INCOMLETEMOVE if a capture has taken place.
     Note: it does not check if a 2nd capture is possible!
    */
    static int isMoveLegal(int[][] board, int srtI, int srtJ, int endI, int endJ, int turn) {
        if (!(inRange(srtI, srtJ) && inRange(endI, endJ)))       //if try to move out of the board,
            return ILLEGALMOVE;                                           //returns illegal move
        if (board[endI][endJ] != EMPTY)                     //if try to move to a occupied square
            return ILLEGALMOVE;                                           //returns illegal move

        int piece = board[srtI][srtJ];
        if (Math.abs(srtI - endI) == 1) {
            // first see if any captures are possible
            switch (piece) {
                case BLACKNORMAL:
                case BLACKKING:
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if ((board[i][j] == BLACKNORMAL ||
                                    board[i][j] == BLACKKING)
                                    && canCapture(board, i, j))
                                return ILLEGALMOVE;
                        }
                    }
                    break;
                case WHITENORMAL:
                case WHITEKING:
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if ((board[i][j] == WHITENORMAL ||
                                    board[i][j] == WHITEKING)
                                    && canCapture(board, i, j))
                                return ILLEGALMOVE;
                        }
                    }
                    break;
            }

            switch (piece) {
                case BLACKNORMAL:
                    if (endJ - srtJ == 1)
                        return LEGALMOVE;       //Normal checkers only can go forward
                    break;
                case WHITENORMAL:
                    if (endJ - srtJ == -1) return LEGALMOVE;
                    break;
                case BLACKKING:
                case WHITEKING:
                    if (Math.abs(endJ - srtJ) == 1)
                        return LEGALMOVE;    //Kings can go in any direction
                    break;
            }
            return ILLEGALMOVE;

        } else if (Math.abs(srtI - endI) == 2) {
            int cap_i = (srtI + endI) / 2;
            int cap_j = (srtJ + endJ) / 2;
            int cap_piece = board[cap_i][cap_j];

            if (turn == BLACKNORMAL)         //when you try to cut a piece it should be a peice of opposite side.
            //otherwise it is an illegal move.
            {
                if (!(cap_piece == WHITENORMAL ||
                        cap_piece == WHITEKING))
                    return ILLEGALMOVE;
            } else if (!(cap_piece == BLACKNORMAL ||       //same for yellowers
                    cap_piece == BLACKKING))
                return ILLEGALMOVE;

            switch (piece)              //if u are going to cut a piece the move must have the length two
            {
                case BLACKNORMAL:
                    if (endJ - srtJ != 2)
                        return ILLEGALMOVE;
                    break;
                case WHITENORMAL:
                    if (endJ - srtJ != -2)
                        return ILLEGALMOVE;
                    break;
                case BLACKKING:
                case WHITEKING:
                    if (Math.abs(endJ - srtJ) != 2)
                        return ILLEGALMOVE;
            }

            return INCOMLETEMOVE;   //if the situation is something else it should be an incomplete move
        }
        return ILLEGALMOVE;     //if the situation is not one of these it should be illegal.
    }

    static int isWalkLegal(int[][] board, int srtI, int srtJ, int endI, int endJ) {
        if (!(inRange(srtI, srtJ) && inRange(endI, endJ)))
            return ILLEGALMOVE;
        if (board[endI][endJ] != EMPTY)
            return ILLEGALMOVE;

        int piece = board[srtI][srtJ];

        if (Math.abs(srtI - endI) == 1) {
            switch (piece) {
                case BLACKNORMAL:
                    if (endJ - srtJ == 1)
                        return LEGALMOVE;
                    break;
                case WHITENORMAL:
                    if (endJ - srtJ == -1)
                        return LEGALMOVE;
                    break;
                case BLACKKING:
                case WHITEKING:
                    if (Math.abs(endJ - srtJ) == 1)
                        return LEGALMOVE;
                    break;
            }
            return ILLEGALMOVE;
        }
        return ILLEGALMOVE;
    }

    static boolean canCapture(int[][] board, int toMove) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (colour(board[i][j]) == toMove && canCapture(board, i, j))
                    return true;
            }
        return false;
    }

    // examines a board board to see if the piece indicated at (x,y)
    // can make a(nother) capture
    static boolean canCapture(int[][] board, int i, int j) {
        switch (board[i][j]) {
            case BLACKNORMAL:
                //check if (red)checkers go out of the board
                if (i + 2 < 8 && j + 2 < 8)
                    if ((board[i + 1][j + 1] == WHITENORMAL ||
                            board[i + 1][j + 1] == WHITEKING)
                            &&
                            (board[i + 2][j + 2] == EMPTY))
                        return true;
                // other posiible move direction
                if (i - 2 > -1 && j + 2 < 8)
                    if ((board[i - 1][j + 1] == WHITENORMAL ||
                            board[i - 1][j + 1] == WHITEKING)
                            &&
                            board[i - 2][j + 2] == EMPTY)
                        return true;
                break;
            case WHITENORMAL:
                if (i + 2 < 8 && j - 2 > -1)
                    if ((board[i + 1][j - 1] == BLACKNORMAL ||
                            board[i + 1][j - 1] == BLACKKING)
                            &&
                            board[i + 2][j - 2] == EMPTY)
                        return true;
                if (i - 2 > -1 && j - 2 > -1)
                    if ((board[i - 1][j - 1] == BLACKNORMAL ||
                            board[i - 1][j - 1] == BLACKKING)
                            &&
                            board[i - 2][j - 2] == EMPTY)
                        return true;
                break;
            case BLACKKING:   //should check all four directions
                if (i + 2 < 8) {
                    if (j + 2 < 8)
                        if ((board[i + 1][j + 1] == WHITENORMAL ||
                                board[i + 1][j + 1] == WHITEKING)
                                &&
                                board[i + 2][j + 2] == EMPTY)
                            return true;
                    if (j - 2 > -1)
                        if ((board[i + 1][j - 1] == WHITENORMAL ||
                                board[i + 1][j - 1] == WHITEKING)
                                &&
                                board[i + 2][j - 2] == EMPTY)
                            return true;
                }
                if (i - 2 > -1) {
                    if (j + 2 < 8)
                        if ((board[i - 1][j + 1] == WHITENORMAL ||
                                board[i - 1][j + 1] == WHITEKING)
                                &&
                                board[i - 2][j + 2] == EMPTY)
                            return true;
                    if (j - 2 > -1)
                        if ((board[i - 1][j - 1] == WHITENORMAL ||
                                board[i - 1][j - 1] == WHITEKING)
                                &&
                                board[i - 2][j - 2] == EMPTY)
                            return true;
                }
                break;
            case WHITEKING:
                if (i + 2 < 8) {
                    if (j + 2 < 8)
                        if ((board[i + 1][j + 1] == BLACKNORMAL ||
                                board[i + 1][j + 1] == BLACKKING)
                                &&
                                board[i + 2][j + 2] == EMPTY)
                            return true;
                    if (j - 2 > -1)
                        if ((board[i + 1][j - 1] == BLACKNORMAL ||
                                board[i + 1][j - 1] == BLACKKING)
                                &&
                                board[i + 2][j - 2] == EMPTY)
                            return true;
                }
                if (i - 2 > -1) {
                    if (j + 2 < 8)
                        if ((board[i - 1][j + 1] == BLACKNORMAL ||
                                board[i - 1][j + 1] == BLACKKING)
                                &&
                                board[i - 2][j + 2] == EMPTY)
                            return true;
                    if (j - 2 > -1)
                        if ((board[i - 1][j - 1] == BLACKNORMAL ||
                                board[i - 1][j - 1] == BLACKKING)
                                &&
                                board[i - 2][j - 2] == EMPTY)
                            return true;
                }
                break;
        }
        return false;
    }

    // canWalk() returns true if the piece on (i,j) can make a
    // legal non-capturing move
    // Imporatant to see if the game is over
    static boolean canWalk(int[][] board, int i, int j) {
        switch (board[i][j]) {
            case BLACKNORMAL:
                if (isEmpty(board, i + 1, j + 1) || isEmpty(board, i - 1, j + 1))
                    return true;
                break;
            case WHITENORMAL:
                if (isEmpty(board, i + 1, j - 1) || isEmpty(board, i - 1, j - 1))
                    return true;
                break;
            case BLACKKING:
            case WHITEKING:
                if (isEmpty(board, i + 1, j + 1) || isEmpty(board, i + 1, j - 1)
                        || isEmpty(board, i - 1, j + 1) || isEmpty(board, i - 1, j - 1))
                    return true;
        }
        // board[i][j] cannot walk anywhere right now
        return false;
    }

    private static boolean isEmpty(int[][] board, int i, int j) {
        if (i > -1 && i < 8 && j > -1 && j < 8)
            if (board[i][j] == EMPTY)
                return true;
        return false;
    }

    // returns the color of a piece
    static int colour(int piece) {
        switch (piece) {
            case BLACKNORMAL:
            case BLACKKING:
                return BLACKNORMAL;
            case WHITENORMAL:
            case WHITEKING:
                return WHITENORMAL;
        }
        return EMPTY;
    }

    // checkers that i and j are between 0 and 7 inclusive
    private static boolean inRange(int i, int j) {
        return (i > -1 && i < 8 && j > -1 && j < 8);
    }


    //given a board, generates all the possible moves depending on whose turn
    static Vector generateMoves(int[][] board, int turn) {
        Vector moves_list = new Vector();
        int move;

        for (int i = 7; i >= 0; i--)
            for (int j = 0; j < 8; j++)
                if (turn == colour(board[i][j])) {
                    if (canCapture(board, turn)) {
                        for (int k = -2; k <= 2; k += 4)     //check all directions
                            for (int l = -2; l <= 2; l += 4) {
                                move = isMoveLegal(board, i, j, i + k, j + l, turn);
                                if (move == INCOMLETEMOVE) {
                                    int[] int_array = new int[4];   //stores old coorinates and new coordinates
                                    int_array[0] = i;
                                    int_array[1] = j;
                                    int_array[2] = i + k;
                                    int_array[3] = j + l;
                                    int[][] temp_board = GameEngine.copyBoard(board);
                                    move = Checker.ApplyMove(temp_board, i, j, i + k, j + l);
                                    if (move == INCOMLETEMOVE)/*(canCapture(temp_board,i+k,j+l))*/ {
                                        forceCaptures(temp_board, int_array, moves_list, 10);
                                    } else {
                                        moves_list.addElement(int_array);
                                    }

                                }
                            }
                    } else {
                        for (int k = -1; k <= 2; k += 2)
                            for (int l = -1; l <= 2; l += 2) {
                                if (inRange(i + k, j + l)) {
                                    move = isWalkLegal(board, i, j, i + k, j + l);
                                    if (move == LEGALMOVE) {
                                        int[] int_array = new int[4];
                                        int_array[0] = i;
                                        int_array[1] = j;
                                        int_array[2] = i + k;
                                        int_array[3] = j + l;
                                        //a walk has taken place
                                        //add the new array to the Vector moves_list
                                        moves_list.addElement(int_array);
                                    }
                                }
                            }
                    }
                }
        return moves_list;

    }

    //"apply move" in the Minimax.  simply moves the board give moves
    public static void moveComputer(int[][] board, int[] move) {
        int startx = move[0];
        int starty = move[1];
        int endx = move[2];
        int endy = move[3];
        while (endx > 0 || endy > 0) {
            ApplyMove(board, startx, starty, endx % 10, endy % 10);
            startx = endx % 10;
            starty = endy % 10;
            endx /= 10;
            endy /= 10;
        }
    }

    //for an initial capture represented by move, sees if there are more captures
//until there is none.  If there are multiple capture configurations,
//add all of them to moves_list
    private static void forceCaptures(int[][] board, int[] move, Vector moves_list, int inc) {
        int newx = move[2], newy = move[3];

        while (newx > 7 || newy > 7) {
            newx /= 10;
            newy /= 10;
        }//end while
        for (int i = -2; i <= 2; i += 4)
            for (int j = -2; j <= 2; j += 4)
                if (inRange(newx + i, newy + j)) {
                    int[][] tempPosition = GameEngine.copyBoard(board);
                    int moveResult = ApplyMove(tempPosition, newx, newy, newx + i, newy + j);
                    if (moveResult == LEGALMOVE) {      // an ordinary move without additionals
                        int[] new_move = new int[4];
                        new_move[0] = move[0];
                        new_move[1] = move[1];
                        new_move[2] = move[2] + (newx + i) * inc;
                        new_move[3] = move[3] + (newy + j) * inc;
                        moves_list.addElement(new_move);
                    } else if (moveResult == INCOMLETEMOVE)      //There are multiple captures
                    {
                        int[] new_move = new int[4];
                        new_move[0] = move[0];
                        new_move[1] = move[1];
                        new_move[2] = move[2] + (newx + i) * inc;
                        new_move[3] = move[3] + (newy + j) * inc;

                        forceCaptures(tempPosition, new_move, moves_list, inc * 10);  //do until there are no more captures
                    }
                }
    }


    public static void printboard(int[][] board) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i] == BLACKNORMAL)
                    System.out.print("B ");
                else if (board[j][i] == WHITENORMAL)
                    System.out.print("W ");
                else if (board[j][i] == BLACKKING)
                    System.out.print("B+");
                else if (board[j][i] == WHITEKING)
                    System.out.print("W+");
                else
                    System.out.print("  ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void printBoard(int[][] b) {

        for (int r = 7; r >= 0; r--) {
            for (int c = 0; c < 8; c++) {
                if (b[c][r] == EMPTY)
                    System.out.print("- ");
                else if (b[c][r] == WHITENORMAL)
                    System.out.print("W ");
                else if (b[c][r] == BLACKNORMAL)
                    System.out.print("B ");
                else if (b[c][r] == EMPTY)
                    System.out.print("- ");
                else if (b[c][r] == BLACKKING)
                    System.out.print("B+");
                else if (b[c][r] == WHITEKING)
                    System.out.print("W+");
                else
                    System.out.print(b[c][r] + " ");

            }
            System.out.println("");
        }

        System.out.println("");
    }
}
