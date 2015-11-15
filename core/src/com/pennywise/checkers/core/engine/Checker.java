package com.pennywise.checkers.core.engine;


/*          (white)
            32  31  30  29
        28  27  26  25
            24  23  22  21
        20  19  18  17
            16  15  14  13
        12  11  10   9
            8   7   6   5
        4   3   2   1
            (black)
*/

/**
 * Created by CHOXXY on 11/13/2015.
 */
public class Checker {
    // Global constants
    public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;

    public static final int SIZE = 8;


    // You can ignore these constants
    public static final int MARK = 3;

    public static final int RANDOM = 1;
    public static final int OFFENSIVE = 2;
    public static final int DEFENSIVE = 3;
    public static final int CUSTOM = 4;


    // *** SOLUTION: EXTRA FUNCTIONS ***
    private static final int DISCS = 12;
    private static final int PLIES = 9;

    private static boolean ownSquare(int[][] board, int player, int row, int col) {
        return board[row][col] * player > 0;
    }

    private static boolean opponentSquare(int[][] board, int player, int row, int col) {
        return board[row][col] * player < 0;
    }

    private static boolean emptySquare(int[][] board, int row, int col) {
        return board[row][col] == EMPTY;
    }

    private static int[][] trimArray(int[][] array, int size) {
        int[][] newArray = new int[size][];
        for (int i = 0; i < size; ++i)
            newArray[i] = array[i];

        return newArray;
    }

    private static void appendArray(int[][] array, int curSize, int[][] extraArray) {
        for (int i = 0; i < extraArray.length; ++i)
            array[curSize + i] = extraArray[i];
    }

    private static int sign(int number) {
        return Math.min(1, Math.max(-1, number));
    }

    private static int random(int exclusiveMax) {
        return (int) (Math.random() * exclusiveMax);
    }

    private static int[] randomMove(int[][] moves) {
        return moves[random(moves.length)];
    }

    private static boolean isLegalPosition(int row, int col) {
        return 0 <= row && row < SIZE
                && 0 <= col && col < SIZE;
    }

    private static boolean isLegalDirection(int[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        int indicator = sign(toRow - fromRow) * board[fromRow][fromCol];
        return indicator >= 1 || indicator == -2;
    }

    private static boolean isJumpingMove(int fromRow, int toRow) {
        return Math.abs(toRow - fromRow) == 2;
    }

    private static int[][] allMoves(int[][] board, int player) {
        return canJump(board, player) ?
                allJumpingMoves(board, player)
                : getSimpleMoves(board, player);
    }

    private static boolean canJump(int[][] board, int player, int fromRow, int fromCol) {
        return discJumpingMoves(board, player, fromRow, fromCol).length > 0;
    }

    private static int discsSum(int[][] board, int player) {
        int[][] discs = playerDiscs(board, player);
        int sum = 0;

        for (int i = 0; i < discs.length; ++i)
            sum += board[discs[i][0]][discs[i][1]];

        return Math.abs(sum);
    }

    private static String moveToString(int[] move) {
        return "(" + move[0] + "," + move[1] + ") -> (" + move[2] + "," + move[3] + ")";
    }
    // *** SOLUTION: EXTRA FUNCTIONS ***

    /* -------------------------------------------------------------------------------- *
     * Task 1                                                                           *
     * -------------------------------------------------------------------------------- */
    public static int[][] createBoard() {
        int[][] board = null;         // YOU SHOULD CHANGE IT !!

        // *** SOLUTION ***
        board = new int[SIZE][SIZE];

        for (int row = 0; row < SIZE; ++row)
            for (int col = 0; col < SIZE; ++col)
                board[row][col] = EMPTY;

        for (int col = 0; col < SIZE; col += 2) {
            board[0][col] = board[2][col] = board[1][col + 1] = WHITE;
            board[SIZE - 1][SIZE - 1 - col] = board[SIZE - 3][SIZE - 1 - col] = board[SIZE - 2][SIZE - 2 - col] = BLACK;
        }
        // *** SOLUTION ***

        return board;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 2                                                                           *
     * -------------------------------------------------------------------------------- */
    public static int[][] playerDiscs(int[][] board, int player) {
        // fill pieces positions
        int[][] positions = null;      // YOU SHOULD CHANGE IT !!

        // *** SOLUTION ***
        positions = new int[DISCS][];
        int posIndex = 0;
        for (int row = 0; row < SIZE; ++row)
            for (int col = 0; col < SIZE; ++col)
                if (ownSquare(board, player, row, col))
                    positions[posIndex++] = new int[]{row, col};

        positions = trimArray(positions, posIndex);
        // *** SOLUTION ***

        return positions;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 3                                                                           *
     * -------------------------------------------------------------------------------- */
    public static boolean isValidSimpleMove(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
        boolean ans = false;         // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = isLegalPosition(fromRow, fromCol)
                && isLegalPosition(toRow, toCol)
                && isLegalDirection(board, fromRow, fromCol, toRow, toCol)
                && Math.abs(fromRow - toRow) == 1
                && Math.abs(fromCol - toCol) == 1
                && ownSquare(board, player, fromRow, fromCol)
                && emptySquare(board, toRow, toCol);
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 4                                                                           *
     * -------------------------------------------------------------------------------- */
    public static int[][] getSimpleMoves(int[][] board, int player) {
        int[][] moves = null;         // YOU SHOULD CHANGE IT !!

        // *** SOLUTION ***
        moves = new int[DISCS * 4][];
        int movesCount = 0;

        int[][] discs = playerDiscs(board, player);
        for (int i = 0; i < discs.length; ++i) {
            int row = discs[i][0];
            int col = discs[i][1];

            for (int newRow = row - 1; newRow <= row + 1; newRow += 2)
                for (int newCol = col - 1; newCol <= col + 1; newCol += 2)
                    if (isValidSimpleMove(board, player, row, col, newRow, newCol))
                        moves[movesCount++] = new int[]{row, col, newRow, newCol};
        }

        moves = trimArray(moves, movesCount);
        // *** SOLUTION ***

        return moves;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 5                                                                           *
     * -------------------------------------------------------------------------------- */
    public static boolean isValidJumping(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
        boolean ans = false;         // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = isLegalPosition(fromRow, fromCol)
                && isLegalPosition(toRow, toCol)
                && isLegalDirection(board, fromRow, fromCol, toRow, toCol)
                && Math.abs(fromRow - toRow) == 2
                && Math.abs(fromCol - toCol) == 2
                && ownSquare(board, player, fromRow, fromCol)
                && opponentSquare(board, player, (fromRow + toRow) / 2, (fromCol + toCol) / 2)
                && emptySquare(board, toRow, toCol);
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 6                                                                           *
     * -------------------------------------------------------------------------------- */
    public static int[][] discJumpingMoves(int[][] board, int player, int row, int col) {
        int[][] moves = null;         // YOU SHOULD CHANGE IT !!

        // *** SOLUTION ***
        moves = new int[4][];
        int movesCount = 0;

        for (int newRow = row - 2; newRow <= row + 2; newRow += 4)
            for (int newCol = col - 2; newCol <= col + 2; newCol += 4)
                if (isValidJumping(board, player, row, col, newRow, newCol))
                    moves[movesCount++] = new int[]{row, col, newRow, newCol};

        moves = trimArray(moves, movesCount);
        // *** SOLUTION ***

        return moves;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 7                                                                           *
     * -------------------------------------------------------------------------------- */
    public static int[][] allJumpingMoves(int[][] board, int player) {
        int[][] moves = null;         // YOU SHOULD CHANGE IT !!

        // *** SOLUTION ***
        moves = new int[DISCS * 4][];
        int movesCount = 0;

        int[][] discs = playerDiscs(board, player);
        for (int i = 0; i < discs.length; ++i) {
            int[][] discMoves = discJumpingMoves(board, player, discs[i][0], discs[i][1]);
            appendArray(moves, movesCount, discMoves);
            movesCount += discMoves.length;
        }

        moves = trimArray(moves, movesCount);
        // *** SOLUTION ***

        return moves;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 8                                                                           *
     * -------------------------------------------------------------------------------- */
    public static boolean canJump(int[][] board, int player) {
        boolean ans = false;         // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = allJumpingMoves(board, player).length > 0;
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 9                                                                           *
     * -------------------------------------------------------------------------------- */
    public static boolean isValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
        boolean ans = false;         // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = (isValidSimpleMove(board, player, fromRow, fromCol, toRow, toCol) && !canJump(board, player))
                || isValidJumping(board, player, fromRow, fromCol, toRow, toCol);
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 10                                                                           *
     * -------------------------------------------------------------------------------- */
    public static boolean hasValidMoves(int[][] board, int player) {
        boolean ans = false;         // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = canJump(board, player)
                || getSimpleMoves(board, player).length > 0;
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 11                                                                          *
     * -------------------------------------------------------------------------------- */
    public static int[][] applyMove(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
        // *** SOLUTION ***
        if (toRow == 0 || toRow == SIZE - 1)
            board[toRow][toCol] = player * 2;
        else
            board[toRow][toCol] = board[fromRow][fromCol];

        board[fromRow][fromCol] = EMPTY;

        if (isJumpingMove(fromRow, toRow))
            board[(fromRow + toRow) / 2][(fromCol + toCol) / 2] = EMPTY;
        // *** SOLUTION ***

        return board;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 12                                                                          *
     * -------------------------------------------------------------------------------- */
    public static boolean gameOver(int[][] board, int player) {
        boolean ans = false;         // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = playerDiscs(board, player).length == 0
                || playerDiscs(board, -player).length == 0
                || !hasValidMoves(board, player);
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 13                                                                          *
     * -------------------------------------------------------------------------------- */
    public static int getCurrentWinner(int[][] board) {
        int ans = 0;               // YOU CAN CHANGE IT !!

        // *** SOLUTION ***
        ans = sign(discsSum(board, WHITE) - discsSum(board, BLACK));
        // *** SOLUTION ***

        return ans;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 14                                                                          *
     * -------------------------------------------------------------------------------- */
    public static int[][] randomPlayer(int[][] board, int player) {
        // *** SOLUTION ***
        if (hasValidMoves(board, player)) {
            int[] move = randomMove(allMoves(board, player));
            board = applyMove(board, player, move[0], move[1], move[2], move[3]);
            board = finishJumpingSequence(board, player, move);

            System.out.println("Played " + moveToString(move));
        }
        // *** SOLUTION ***

        return board;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 15                                                                          *
     * -------------------------------------------------------------------------------- */
    public static int[][] offensivePlayer(int[][] board, int player) {
        // *** SOLUTION ***
        if (hasValidMoves(board, player)) {
            if (canJump(board, player))
                board = randomPlayer(board, player);
            else {
                int[][] moves = getSimpleMoves(board, player);
                int[][] farthestMoves = new int[moves.length][];

                int farthestRow = -1;
                int count = -1;

                for (int i = 0; i < moves.length; ++i) {
                    int newRow = moves[i][2];

                    if (count == -1
                            || ((player == WHITE) && newRow > farthestRow)
                            || ((player == BLACK) && newRow < farthestRow)) {
                        farthestRow = newRow;
                        count = 0;
                    }

                    if (newRow == farthestRow)
                        farthestMoves[count++] = moves[i];
                }

                int[] move = randomMove(trimArray(farthestMoves, count));
                board = applyMove(board, player, move[0], move[1], move[2], move[3]);
            }
        }
        // *** SOLUTION ***

        return board;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 16                                                                          *
     * -------------------------------------------------------------------------------- */
    public static int[][] defensivePlayer(int[][] board, int player) {
        // *** SOLUTION ***
        if (hasValidMoves(board, player)) {
            if (canJump(board, player))
                board = randomPlayer(board, player);
            else {
                int[][] moves = getSimpleMoves(board, player);
                int[][] farthestMoves = new int[moves.length][];

                int farthestRow = -1;
                int count = -1;

                for (int i = 0; i < moves.length; ++i) {
                    int newRow = moves[i][2];

                    if (count == -1
                            || ((player == WHITE) && newRow < farthestRow)
                            || ((player == BLACK) && newRow > farthestRow)) {
                        farthestRow = newRow;
                        count = 0;
                    }

                    if (newRow == farthestRow)
                        farthestMoves[count++] = moves[i];
                }

                int[] move = randomMove(trimArray(farthestMoves, count));
                board = applyMove(board, player, move[0], move[1], move[2], move[3]);
            }
        }
        // *** SOLUTION ***

        return board;
    }


    /* -------------------------------------------------------------------------------- *
     * Task 17 (OPTIONAL)                                                               *
     *                                                                *
     * http://en.wikipedia.org/wiki/Negamax                                    *
     * http://en.wikipedia.org/wiki/Alpha-beta_pruning                           *
     * http://www.seanet.com/~brucemo/topics/alphabeta.htm                        *
     * -------------------------------------------------------------------------------- */
    public static int[][] myPlayer(int[][] board, int player) {
        // Code your superior checkers player here!
        if (hasValidMoves(board, player)) {
            int[][] moves = allMoves(board, player);
            double previous = fitness(board, player);
            double fitness;

            // If it's a forced move, don't spend time...
            if (moves.length == 1) {
                int[] move = moves[0];
                board = applyMove(board, player, move[0], move[1], move[2], move[3]);
                board = finishJumpingSequence(board, player, move);
                fitness = fitness(board, player);
            }
            // Otherwise, think very hard!
            else {
                int[][][] bestBoardBox = new int[1][][];
                fitness = negamaxAB(board, player, PLIES, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, bestBoardBox);
                board = bestBoardBox[0];
            }

            printFitness(previous, fitness);
        }

        // Leave the null return if you do not write this function
        return board;
    }

    private static double negamaxAB(int[][] board, int player, int plies, double alpha, double beta, int[][][] bestBoardBox) {
        if (plies == 0 || gameOver(board, player))
            return fitness(board, player);
        else {
            int[][] moves = randomize(allMoves(board, player));

            for (int i = 0; i < moves.length; ++i) {
                int[] move = moves[i];
                int[][] newBoard = applyMove(cloneBoard(board), player, move[0], move[1], move[2], move[3]);
                // don't bother with sequences
                newBoard = finishJumpingSequence(newBoard, player, move);

                double newFitness = -negamaxAB(newBoard, -player, plies - 1, -beta, -alpha, null);
                if (newFitness > alpha) {
                    alpha = newFitness;
                    if (bestBoardBox != null)
                        bestBoardBox[0] = newBoard;

                    if (alpha + 0.0000001 >= beta)
                        return beta;
                }
            }

            return alpha;
        }
    }

    private static double fitness(int[][] board, int player) {
        int playerSum = discsSum(board, player);
        int oppSum = discsSum(board, -player);

        double difference;

        if (oppSum == 0)                     // Win
            difference = Double.MAX_VALUE;
        else if (playerSum == 0)               // Loss
            difference = -Double.MAX_VALUE;
        else if (!hasValidMoves(board, player))      // Draw
            difference = 0.0;
        else
            difference = playerSum - oppSum;

        return difference;
    }

    private static int[][] finishJumpingSequence(int[][] board, int player, int[] move) {
        if (isJumpingMove(move[0], move[2]))
            while (canJump(board, player, move[2], move[3])) {
                move = randomMove(discJumpingMoves(board, player, move[2], move[3]));
                board = applyMove(board, player, move[0], move[1], move[2], move[3]);
            }

        return board;
    }

    private static int[][] cloneBoard(int[][] board) {
        int[][] newBoard = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; ++i)
            System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);

        return newBoard;
    }

    private static int[][] randomize(int[][] array) {
        for (int i = 1; i < array.length; ++i) {
            // pick j in [0 .. i]
            int j = (int) (Math.random() * (i + 1));

            // swap array[i], array[j]
            int[] tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }

        return array;
    }

    private static void printFitness(double previous, double fitness) {
        double change = fitness - previous;

        if (fitness > Double.MAX_VALUE / 2)
            System.out.println("*** PWND ***");
        else if (fitness < -Double.MAX_VALUE / 2)
            System.out.println("*** LAME ***");

        else if (change > 1.5)
            System.out.println("!!! Nobody expects the Spanish Inquisition !!!");
        else if (change < -1.5)
            System.out.println("!!! I, for one, welcome our new Human overlords !!!");

        else if (change > 0.5)
            System.out.println("+++ I RULE :))) +++");
        else if (change < -0.5)
            System.out.println("+++ U SUXX :((( +++");
    }


    /* ---------------------- *
     * Program entry point    *
     * ---------------------- */
    public static void main(String[] args) {
        // CREATE THE GRAPHICAL GRID

        interactivePlay();
        //twoPlayers();
    }


    /* ---------------------------------------------------------- *
     * Play an interactive game between the computer and you      *
     * ---------------------------------------------------------- */
    public static void interactivePlay() {
        int[][] board = createBoard();
        showBoard(board);

        System.out.println("Welcome to the interactive Checkers Game !");

        //int strategy = getStrategyChoice();
        int strategy = CUSTOM;
        System.out.println("You are the first player (WHITE discs)");

        boolean oppGameOver = false;
        while (!gameOver(board, WHITE) & !oppGameOver) {
            board = getPlayerFullMove(board, WHITE);

            oppGameOver = gameOver(board, BLACK);
            if (!oppGameOver) {
                // Uncomment just one of the two following lines: ENTER or SLEEP 500ms
                //Console.readString("May I play? (press ENTER for Yes)");
                //CheckersGrid.sleep(200);

                board = getStrategyFullMove(board, BLACK, strategy);
            }
        }

        int winner = 0;
        if (playerDiscs(board, WHITE).length == 0 | playerDiscs(board, BLACK).length == 0)
            winner = getCurrentWinner(board);

        if (winner == WHITE) {
            System.out.println();
            System.out.println("\t *************************");
            System.out.println("\t * You are the winner !! *");
            System.out.println("\t *************************");
        } else if (winner == BLACK) {
            System.out.println("\n======= You lost :( =======");
        } else
            System.out.println("\n======= DRAW =======");
    }


    /* --------------------------------------------------------- *
     * A game between two players                                *
     * --------------------------------------------------------- */
    public static void twoPlayers() {
        int[][] board = createBoard();
        showBoard(board);

        System.out.println("Welcome to the 2-player Checkers Game !");

        boolean oppGameOver = false;
        while (!gameOver(board, WHITE) & !oppGameOver) {
            System.out.println("\nWHITE's turn");
            board = getPlayerFullMove(board, WHITE);

            oppGameOver = gameOver(board, BLACK);
            if (!oppGameOver) {
                System.out.println("\nBLACK's turn");
                board = getPlayerFullMove(board, BLACK);
            }
        }

        int winner = 0;
        if (playerDiscs(board, WHITE).length == 0 | playerDiscs(board, BLACK).length == 0)
            winner = getCurrentWinner(board);

        System.out.println();
        System.out.println("\t ************************************");
        if (winner == WHITE)
            System.out.println("\t * The WHITE player is the winner !!  *");
        else if (winner == BLACK)
            System.out.println("\t * The BLACK player is the winner !! *");
        else
            System.out.println("\t *   FRIENDSHIP   is the winner !!  *");
        System.out.println("\t ************************************");
    }


    /* --------------------------------------------------------- *
     * Get a complete (possibly a sequence of jumps) move        *
     * from a human player.                                      *
     * --------------------------------------------------------- */
    public static int[][] getPlayerFullMove(int[][] board, int player) {
        // Get first move/jump
        int fromRow = -1, fromCol = -1, toRow = -1, toCol = -1;
        boolean jumpingMove = canJump(board, player);
        boolean badMove = true;
        while (badMove) {
            System.out.println("Please play:");
            //fromRow = Console.readInt("Origin row");
            //fromCol = Console.readInt("Origin column");

            int[][] moves = jumpingMove ? allJumpingMoves(board, player) : getSimpleMoves(board, player);
            markPossibleMoves(board, moves, fromRow, fromCol, MARK);
            //toRow = Console.readInt("Destination row");
            //toCol = Console.readInt("Destination column");
            markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

            badMove = !isValid(board, player, fromRow, fromCol, toRow, toCol);
            if (badMove)
                System.out.println("\nThis is an illegal move :(");
        }

        // Apply move/jump
        board = applyMove(board, player, fromRow, fromCol, toRow, toCol);
        showBoard(board);

        // Get extra jumps
        if (jumpingMove) {
            boolean longMove = (discJumpingMoves(board, player, toRow, toCol).length > 0);
            while (longMove) {
                fromRow = toRow;
                fromCol = toCol;

                int[][] moves = discJumpingMoves(board, player, fromRow, fromCol);

                boolean badExtraMove = true;
                while (badExtraMove) {
                    markPossibleMoves(board, moves, fromRow, fromCol, MARK);
                    System.out.println("Continue jump:");
                    // toRow = Console.readInt("Destination row");
                    // toCol = Console.readInt("Destination column");
                    markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

                    badExtraMove = !isValid(board, player, fromRow, fromCol, toRow, toCol);
                    if (badExtraMove)
                        System.out.println("\nThis is an illegal jump destination :(");
                }

                // Apply extra jump
                board = applyMove(board, player, fromRow, fromCol, toRow, toCol);
                showBoard(board);

                longMove = (discJumpingMoves(board, player, toRow, toCol).length > 0);
            }
        }

        return board;
    }


    /* --------------------------------------------------------- *
     * Get a complete (possibly a sequence of jumps) move        *
     * from a strategy.                                          *
     * --------------------------------------------------------- */
    public static int[][] getStrategyFullMove(int[][] board, int player, int strategy) {
        if (strategy == RANDOM)
            board = randomPlayer(board, player);
        else if (strategy == OFFENSIVE)
            board = offensivePlayer(board, player);
        else if (strategy == DEFENSIVE)
            board = defensivePlayer(board, player);
        else if (strategy == CUSTOM)
            board = myPlayer(board, player);

        showBoard(board);
        return board;
    }


    /* --------------------------------------------------------- *
     * Get a strategy choice before the game.                    *
     * --------------------------------------------------------- */
    public static int getStrategyChoice() {
        int strategy = -1;
        /*while (strategy != RANDOM & strategy != OFFENSIVE
                & strategy != DEFENSIVE & strategy != CUSTOM) {
            strategy = Console.readInt("Choose:\t(" + RANDOM + ") random strategy"
                    + "\n\t(" + OFFENSIVE + ") offensive strategy"
                    + "\n\t(" + DEFENSIVE + ") defensive strategy"
                    + "\n\t(" + CUSTOM + ") for myPlayer strategy\n");
        }*/

        return CUSTOM;
    }


    /* --------------------------------------- *
     * Print the possible moves                *
     * --------------------------------------- */
    public static void printMoves(int[][] possibleMoves) {
        for (int i = 0; i < 4; i = i + 1) {
            for (int j = 0; j < possibleMoves.length; j = j + 1)
                System.out.print(" " + possibleMoves[j][i]);
            System.out.println();
        }
    }


    /* --------------------------------------- *
     * Mark/unmark the possible moves          *
     * --------------------------------------- */
    public static void markPossibleMoves(int[][] board, int[][] moves, int fromRow, int fromColumn, int value) {
        for (int i = 0; i < moves.length; i = i + 1)
            if (moves[i][0] == fromRow & moves[i][1] == fromColumn)
                board[moves[i][2]][moves[i][3]] = value;

        showBoard(board);
    }


    /* --------------------------------------------------------------------------- *
     * Shows the board in a graphic window - you can use it                        *
     * without understanding how it works. :)                                      *
     * --------------------------------------------------------------------------- */
    public static void showBoard(int[][] board) {
        //grid.showBoard(board);
    }

    public static int[] getBoardPosition(int n) {

        int[] p = new int[2];
        switch (n) {
            case 4:
                p[0] = 0;
                p[1] = 0;
                break;
            case 3:
                p[0] = 2;
                p[1] = 0;
                break;
            case 2:
                p[0] = 4;
                p[1] = 0;
                break;
            case 1:
                p[0] = 6;
                p[1] = 0;
                break;
            case 8:
                p[0] = 1;
                p[1] = 1;
                break;
            case 7:
                p[0] = 3;
                p[1] = 1;
                break;
            case 6:
                p[0] = 5;
                p[1] = 1;
                break;
            case 5:
                p[0] = 7;
                p[1] = 1;
                break;
           case 12:
                p[0] = 0;
                p[1] = 2;
                break;
            case 11:
                p[0] = 2;
                p[1] = 2;
                break;
            case 10:
                p[0] = 4;
                p[1] = 2;
                break;
            case 9:
                p[0] = 6;
                p[1] = 2;
                break;
            case 16:
                p[0] = 1;
                p[1] = 3;
                break;
            case 15:
                p[0] = 3;
                p[1] = 3;
                break;
            case 14:
                p[0] = 5;
                p[1] = 3;
                break;
            case 13:
                p[0] = 7;
                p[1] = 3;
                break;
            case 20:
                p[0] = 0;
                p[1] = 4;
                break;
            case 19:
                p[0] = 2;
                p[1] = 4;
                break;
            case 18:
                p[0] = 4;
                p[1] = 4;
                break;
            case 17:
                p[0] = 6;
                p[1] = 4;
                break;
            case 24:
                p[0] = 1;
                p[1] = 5;
                break;
            case 23:
                p[0] = 3;
                p[1] = 5;
                break;
            case 22:
                p[0] = 5;
                p[1] = 5;
                break;
            case 21:
                p[0] = 7;
                p[1] = 5;
                break;
            case 28:
                p[0] = 0;
                p[1] = 6;
                break;
            case 27:
                p[0] = 2;
                p[1] = 6;
                break;
            case 26:
                p[0] = 4;
                p[1] = 6;
                break;
            case 25:
                p[0] = 6;
                p[1] = 6;
                break;
            case 32:
                p[0] = 1;
                p[1] = 7;
                break;
            case 31:
                p[0] = 3;
                p[1] = 7;
                break;
            case 30:
                p[0] = 5;
                p[1] = 7;
                break;
            case 29:
                p[0] = 7;
                p[1] = 7;
                break;
        }

        return p;
    }
}