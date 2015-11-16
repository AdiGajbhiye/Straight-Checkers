package com.pennywise.checkers.core.engine;

/**
 * Created by Joshua.Nabongo on 10/13/2015.
 */
public class Simplech {
    public static final int OCCUPIED = 0;

    public static final int WHITE = 1;
    public static final int BLACK = 2;
    public static final int PAWN = 4;
    public static final int KING = 8;
    public static final int FREE = 16;


    public static final int WHITEPAWN = (WHITE | PAWN);
    public static final int BLACKPAWN = (BLACK | PAWN);
    public static final int WHITEKING = (WHITE | KING);
    public static final int BLACKKING = (BLACK | KING);


    public static final int CHANGECOLOR = 3;
    public static final int MAXDEPTH = 99;
    public static final int MAXMOVES = 20;
    public static final double TICKS = 1000;


    public static final int VALID = 10;
    public static final int INVALID = 11;
    public static final int MULTIPLE_CAPTURE = 12;


    public static final int DRAW = 20;
    public static final int WIN = 21;
    public static final int LOSS = 22;

    public static final int WHITELOSS = (WHITE | LOSS);
    public static final int BLACKLOSS = (BLACK | LOSS);
    public static final int WHITEWIN = (WHITE | WIN);
    public static final int BLACKWIN = (BLACK | WIN);


    public boolean STATISTICS = false;
    public boolean MUTE = false;

    //stats variables
    private int alphabetas = 0;
    private int generatemovelists = 0;
    private int evaluations = 0;
    private int generatecapturelists = 0;
    private int testcaptures = 0;

    private Move bestMove;

    int[] value = new int[]{0, 0, 0, 0, 0, 1, 256, 0, 0, 16, 4096, 0, 0, 0, 0, 0, 0};
    int[] edge = new int[]{5, 6, 7, 8, 13, 14, 22, 23, 31, 32, 37, 38, 39, 40};
    int[] center = new int[]{15, 16, 20, 21, 24, 25, 29, 30};
    int[] row = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2,
            0, 3, 3, 3, 3, 4, 4, 4, 4, 0, 5, 5, 5, 5, 6, 6, 6, 6, 0, 7, 7, 7, 7};
    int[] safeedge = new int[]{8, 13, 32, 37};

    private boolean play = false;

    ///////////////////////////////////


    public void initCheckers(int[] b)
/*----------> purpose: initialize the checkerboard
  ----------> version: 1.0
  ----------> date: 24th october 97 */ {
        int i;

        for (i = 0; i < 46; i++)
            b[i] = OCCUPIED;

        for (i = 5; i <= 40; i++)
            b[i] = FREE;

        for (i = 5; i <= 17; i++)
            b[i] = (BLACK | PAWN);

        for (i = 28; i <= 40; i++)
            b[i] = (WHITE | PAWN);

        for (i = 9; i <= 36; i += 9)
            b[i] = OCCUPIED;
    }

    public void printBoard(int[] board) {

        int[][] b = new int[8][8];

        b[0][0] = board[5];
        b[2][0] = board[6];
        b[4][0] = board[7];
        b[6][0] = board[8];
        b[1][1] = board[10];
        b[3][1] = board[11];
        b[5][1] = board[12];
        b[7][1] = board[13];
        b[0][2] = board[14];
        b[2][2] = board[15];
        b[4][2] = board[16];
        b[6][2] = board[17];
        b[1][3] = board[19];
        b[3][3] = board[20];
        b[5][3] = board[21];
        b[7][3] = board[22];
        b[0][4] = board[23];
        b[2][4] = board[24];
        b[4][4] = board[25];
        b[6][4] = board[26];
        b[1][5] = board[28];
        b[3][5] = board[29];
        b[5][5] = board[30];
        b[7][5] = board[31];
        b[0][6] = board[32];
        b[2][6] = board[33];
        b[4][6] = board[34];
        b[6][6] = board[35];
        b[1][7] = board[37];
        b[3][7] = board[38];
        b[5][7] = board[39];
        b[7][7] = board[40];

        for (int r = 7; r >= 0; r--) {
            for (int c = 0; c < 8; c++) {
                if (b[c][r] == OCCUPIED)
                    System.out.print("- ");
                else if (b[c][r] == WHITEPAWN)
                    System.out.print("W ");
                else if (b[c][r] == BLACKPAWN)
                    System.out.print("B ");
                else if (b[c][r] == FREE)
                    System.out.print("F ");
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

    ////////////////////////////////////

    public CbMove isLegal(int[] board, int color, int from, int to) {
    /* islegal tells CheckerBoard if a move the user wants to make is legal or not */
    /* to check this, we generate a movelist and compare the moves in the movelist to
       the move the user wants to make with from&to */
        int n = 0;
        Move[] movelist = new Move[MAXMOVES];
        Move move = null;
        CbMove cbMove = new CbMove();


        /* array initialized */
        for (int j = 0; j < MAXMOVES; j++)
            movelist[j] = new Move();

        n = generatecapturelist(board, movelist, color);

        if (n == 0)
            n = generatemovelist(board, movelist, color);
        if (n == 0)
            return null;

	/* now we have a movelist - check if from and to are the same */
        for (int i = 0; i < n; i++) {
            int[] iMove = moveNotation(movelist[i]);

            if (from == iMove[0] && to == iMove[1]) {
                move = movelist[i];
                break;
            }
        }

        //move

        doMove(board, move);

        cbMove = setMove(move);

        return cbMove;
    }

    public int getMove(int[] board, int color, long maxtime, boolean playNow, CbMove move) {
   /* getmove is what checkerboard calls. you get 6 parameters:
   board 	is the current position. the values in the array are determined by
   			the #defined values of BLACK, WHITE, KING, PAWN. a black king for
            instance is represented by BLACK|KING.
   color		is the side to make a move. BLACK or WHITE.
   maxtime	is the time your program should use to make a move. this is
   		   what you specify as level in checkerboard. so if you exceed
            this time it's not too bad - just don't exceed it too much...
   playnow	is a pointer to the playnow variable of checkerboard. if the user
   			would like your engine to play immediately, this value is nonzero,
            else zero. you should respond to a nonzero value of *playnow by
            interrupting your search IMMEDIATELY.
            */

        int i;
        int value;

        for (i = 5; i <= 40; i++)
            if (board[i] == 0)
                board[i] = FREE;

        for (i = 9; i <= 36; i += 9)
            board[i] = OCCUPIED;

        this.play = playNow;

        value = computeMove(board, color, maxtime, move);

        if (color == BLACK) {
            if (value > 4000) return WIN;
            if (value < -4000) return LOSS;
        }
        if (color == WHITE) {
            if (value > 4000) return LOSS;
            if (value < -4000) return WIN;
        }
        return VALID;
    }

    public int[] moveNotation(Move move) {
        int j, from, to;

        from = move.m[0] % 256;
        to = move.m[1] % 256;
        from = from - (from / 9);
        to = to - (to / 9);
        from -= 5;
        to -= 5;
        j = from % 4;
        from -= j;
        j = 3 - j;
        from += j;
        j = to % 4;
        to -= j;
        j = 3 - j;
        to += j;
        from++;
        to++;
        int[] mv = new int[]{from, to};
        return mv;
    }

    public int[] moveNotation2(Move move) {
        int j = 0, from = 0, to = 0;
        int[] mv = new int[move.n];

        from = move.m[0] % 256;
        to = move.m[1] % 256;
        from = from - (from / 9);
        to = to - (to / 9);
        from -= 5;
        to -= 5;
        j = from % 4;
        from -= j;
        j = 3 - j;
        from += j;
        j = to % 4;
        to -= j;
        j = 3 - j;
        to += j;
        from++;
        to++;
        mv[0] = from;
        mv[1] = to;

        return mv;
    }


    String movetonotation(Move move) {
        int j, from, to;
        char c;

        from = move.m[0] % 256;
        to = move.m[1] % 256;
        from = from - (from / 9);
        to = to - (to / 9);
        from -= 5;
        to -= 5;
        j = from % 4;
        from -= j;
        j = 3 - j;
        from += j;
        j = to % 4;
        to -= j;
        j = 3 - j;
        to += j;
        from++;
        to++;
        c = '-';
        if (move.n > 2) c = 'x';
        String out = String.format("%d%c%d", from, c, to);
        return out;
    }


/*-------------- PART II: SEARCH ---------------------------------------------*/


    int computeMove(int[] b, int color, double maxtime, CbMove move)
/*----------> purpose: entry point to checkers. find a move on board b for color
  ---------->          in the time specified by maxtime, write the best move in
  ---------->          board, returns information on the search in str
  ----------> returns 1 if a move is found & executed, 0, if there is no legal
  ----------> move in this position.
  ----------> version: 1.1
  ----------> date: 9th october 98 */ {
        int i, numberofmoves;
        long start;
        int eval;
        Move lastbest = null;
        Move[] movelist = new Move[MAXMOVES];
        String str2 = "";
        String str = "";

        for (int j = 0; j < MAXMOVES; j++)
            movelist[j] = new Move();

        if (STATISTICS) {
            alphabetas = 0;
            generatemovelists = 0;
            generatecapturelists = 0;
            evaluations = 0;
        }

/*--------> check if there is only one move */
        numberofmoves = generatecapturelist(b, movelist, color);

        if (numberofmoves == 1) {
            doMove(b, movelist[0]);
            str = "forced capture";
            move = setMove(movelist[0]);
            return (1);
        } else {
            numberofmoves = generatemovelist(b, movelist, color);
            if (numberofmoves == 1) {
                doMove(b, movelist[0]);
                str = "only move";
                move = setMove(movelist[0]);
                return (1);
            }
            if (numberofmoves == 0) {
                str = "no legal moves in this position";
                return (0);
            }
        }

        start = System.currentTimeMillis();

        eval = firstalphabeta(b, 1, -10000, 10000, color);

        for (i = 2; (i <= MAXDEPTH) && ((System.currentTimeMillis() - start) / TICKS < maxtime); i++) {
            lastbest = bestMove;
            eval = firstalphabeta(b, i, -10000, 10000, color);
            str2 = movetonotation(bestMove);
            if (MUTE)
                str = String.format("best:%s time %2.2fs, depth %d, value %d", str2, (System.currentTimeMillis() - start) / TICKS, i, eval);
            if (STATISTICS) {
                str2 = String.format("  nodes %d, gms %d, gcs %d, evals %d",
                        alphabetas, generatemovelists, generatecapturelists,
                        evaluations);
                str += str2;

                System.out.println(str);
            }
            if (play)
                break;
            if (eval == 5000) break;
            if (eval == -5000) break;
        }
        i--;
        if (play)
            str2 = movetonotation(lastbest);
        else
            str2 = movetonotation(bestMove);

        str = String.format("best:%s time %2.2f, depth %2d, value %4d  nodes %d, gms %d, gcs %d, evals %d", str2, (System.currentTimeMillis() - start) / TICKS, i, eval, alphabetas, generatemovelists, generatecapturelists, evaluations);

        System.out.println(str);

        if (play)
            bestMove = lastbest;
        doMove(b, bestMove);
        move = setMove(bestMove);
        return eval;
    }

    int firstalphabeta(int[] b, int depth, int alpha, int beta, int color)
/*----------> purpose: search the game tree and find the best move.
  ----------> version: 1.0
  ----------> date: 25th october 97 */ {
        int i;
        int value;
        int numberofmoves;
        boolean capture;
        Move[] movelist = new Move[MAXMOVES];

        for (int j = 0; j < MAXMOVES; j++)
            movelist[j] = new Move();

        if (STATISTICS)
            alphabetas++;

        if (play) return 0;
/*----------> test if captures are possible */
        capture = testcapture(b, color);

/*----------> recursion termination if no captures and depth=0*/
        if (depth == 0) {
            if (!capture)
                return (evaluation(b, color));
            else
                depth = 1;
        }

/*----------> generate all possible moves in the position */
        if (!capture) {
            numberofmoves = generatemovelist(b, movelist, color);
/*----------> if there are no possible moves, we lose: */
            if (numberofmoves == 0) {
                if (color == BLACK) return (-5000);
                else return (5000);
            }
        } else
            numberofmoves = generatecapturelist(b, movelist, color);

/*----------> for all moves: execute the move, search tree, undo move. */
        for (i = 0; i < numberofmoves; i++) {
            doMove(b, movelist[i]);

            value = alphabeta(b, depth - 1, alpha, beta, (color ^ CHANGECOLOR));

            undomove(b, movelist[i]);
            if (color == BLACK) {
                if (value >= beta) return (value);
                if (value > alpha) {
                    alpha = value;
                    bestMove = movelist[i];
                }
            }
            if (color == WHITE) {
                if (value <= alpha) return (value);
                if (value < beta) {
                    beta = value;
                    bestMove = movelist[i];
                }
            }
        }
        if (color == BLACK)
            return (alpha);
        return (beta);
    }

    int alphabeta(int[] b, int depth, int alpha, int beta, int color)
/*----------> purpose: search the game tree and find the best move.
  ----------> version: 1.0
  ----------> date: 24th october 97 */ {
        int i;
        int value;
        boolean capture;
        int numberofmoves;
        Move[] movelist = new Move[MAXMOVES];

        for (int j = 0; j < MAXMOVES; j++)
            movelist[j] = new Move();

        if (STATISTICS)
            alphabetas++;

        if (play) return 0;
/*----------> test if captures are possible */
        capture = testcapture(b, color);

/*----------> recursion termination if no captures and depth=0*/
        if (depth == 0) {
            if (!capture)
                return (evaluation(b, color));
            else
                depth = 1;
        }

/*----------> generate all possible moves in the position */
        if (!capture) {
            numberofmoves = generatemovelist(b, movelist, color);
/*----------> if there are no possible moves, we lose: */
            if (numberofmoves == 0) {
                if (color == BLACK) return (-5000);
                else return (5000);
            }
        } else
            numberofmoves = generatecapturelist(b, movelist, color);

/*----------> for all moves: execute the move, search tree, undo move. */
        for (i = 0; i < numberofmoves; i++) {
            doMove(b, movelist[i]);

            value = alphabeta(b, depth - 1, alpha, beta, color ^ CHANGECOLOR);

            undomove(b, movelist[i]);

            if (color == BLACK) {
                if (value >= beta) return (value);
                if (value > alpha) alpha = value;
            }
            if (color == WHITE) {
                if (value <= alpha) return (value);
                if (value < beta) beta = value;
            }
        }
        if (color == BLACK)
            return (alpha);
        return (beta);
    }

    public void doMove(int[] b, Move move)
/*----------> purpose: execute move on board
  ----------> version: 1.1
  ----------> date: 25th october 97 */ {
        int square, after;
        int i;

        for (i = 0; i < move.n; i++) {
            square = (move.m[i] % 256);
            after = ((move.m[i] >> 16) % 256);
            b[square] = after;
        }

    }

    void undomove(int[] b, Move move)
/*----------> purpose:
  ----------> version: 1.1
  ----------> date: 25th october 97 */ {
        int square, before;
        int i;

        for (i = 0; i < move.n; i++) {
            square = (move.m[i] % 256);
            before = ((move.m[i] >> 8) % 256);
            b[square] = before;
        }
    }

    int evaluation(int[] b, int color)
/*----------> purpose:
  ----------> version: 1.1
  ----------> date: 18th april 98 */ {
        int i, j;
        int eval;
        int v1, v2;
        int nbm, nbk, nwm, nwk;
        int nbmc = 0, nbkc = 0, nwmc = 0, nwkc = 0;
        int nbme = 0, nbke = 0, nwme = 0, nwke = 0;
        int code = 0;


        int tempo = 0;
        int nm, nk;

        int turn = 2;   //color to move gets +turn
        int brv = 3;    //multiplier for back rank
        int kcv = 5;    //multiplier for kings in center
        int mcv = 1;    //multiplier for men in center

        int mev = 1;    //multiplier for men on edge
        int kev = 5;    //multiplier for kings on edge
        int cramp = 5;  //multiplier for cramp

        int opening = -2; // multipliers for tempo
        int midgame = -1;
        int endgame = 2;
        int intactdoublecorner = 3;


        int backrank;

        int stonesinsystem = 0;

        if (STATISTICS)
            evaluations++;


        for (i = 5; i <= 40; i++)
            code += value[b[i]];

        nwm = code % 16;
        nwk = (code >> 4) % 16;
        nbm = (code >> 8) % 16;
        nbk = (code >> 12) % 16;


        v1 = 100 * nbm + 130 * nbk;
        v2 = 100 * nwm + 130 * nwk;

        eval = v1 - v2;                       /*material values*/
        eval += (250 * (v1 - v2)) / (v1 + v2);      /*favor exchanges if in material plus*/

        nm = nbm + nwm;
        nk = nbk + nwk;
   /*--------- fine evaluation below -------------*/

        if (color == BLACK) eval += turn;
        else eval -= turn;
   /*    (white)
                    37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */
   /* cramp */
        if (b[23] == (BLACK | PAWN) && b[28] == (WHITE | PAWN)) eval += cramp;
        if (b[22] == (WHITE | PAWN) && b[17] == (BLACK | PAWN)) eval -= cramp;

   /* back rank guard */

        code = 0;
        if (b[5] == PAWN) code++;
        if (b[6] == PAWN) code += 2;
        if (b[7] == PAWN) code += 4;
        if (b[8] == PAWN) code += 8;
        switch (code) {
            case 0:
                code = 0;
                break;
            case 1:
                code = -1;
                break;
            case 2:
                code = 1;
                break;
            case 3:
                code = 0;
                break;
            case 4:
                code = 1;
                break;
            case 5:
                code = 1;
                break;
            case 6:
                code = 2;
                break;
            case 7:
                code = 1;
                break;
            case 8:
                code = 1;
                break;
            case 9:
                code = 0;
                break;
            case 10:
                code = 7;
                break;
            case 11:
                code = 4;
                break;
            case 12:
                code = 2;
                break;
            case 13:
                code = 2;
                break;
            case 14:
                code = 9;
                break;
            case 15:
                code = 8;
                break;
        }
        backrank = code;


        code = 0;
        if (b[37] == PAWN) code += 8;
        if (b[38] == PAWN) code += 4;
        if (b[39] == PAWN) code += 2;
        if (b[40] == PAWN) code++;
        switch (code) {
            case 0:
                code = 0;
                break;
            case 1:
                code = -1;
                break;
            case 2:
                code = 1;
                break;
            case 3:
                code = 0;
                break;
            case 4:
                code = 1;
                break;
            case 5:
                code = 1;
                break;
            case 6:
                code = 2;
                break;
            case 7:
                code = 1;
                break;
            case 8:
                code = 1;
                break;
            case 9:
                code = 0;
                break;
            case 10:
                code = 7;
                break;
            case 11:
                code = 4;
                break;
            case 12:
                code = 2;
                break;
            case 13:
                code = 2;
                break;
            case 14:
                code = 9;
                break;
            case 15:
                code = 8;
                break;
        }
        backrank -= code;
        eval += brv * backrank;


   /* intact double corner */
        if (b[8] == (BLACK | PAWN)) {
            if (b[12] == (BLACK | PAWN) || b[13] == (BLACK | PAWN))
                eval += intactdoublecorner;
        }

        if (b[37] == (WHITE | PAWN)) {
            if (b[32] == (WHITE | PAWN) || b[33] == (WHITE | PAWN))
                eval -= intactdoublecorner;
        }
   /*    (white)
                    37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */

    /* center control */
        for (i = 0; i < 8; i++) {
            if (b[center[i]] != FREE) {
                if (b[center[i]] == (BLACK | PAWN)) nbmc++;
                if (b[center[i]] == (BLACK | KING)) nbkc++;
                if (b[center[i]] == (WHITE | PAWN)) nwmc++;
                if (b[center[i]] == (WHITE | KING)) nwkc++;
            }
        }
        eval += (nbmc - nwmc) * mcv;
        eval += (nbkc - nwkc) * kcv;

  	/*edge*/
        for (i = 0; i < 14; i++) {
            if (b[edge[i]] != FREE) {
                if (b[edge[i]] == (BLACK | PAWN)) nbme++;
                if (b[edge[i]] == (BLACK | KING)) nbke++;
                if (b[edge[i]] == (WHITE | PAWN)) nwme++;
                if (b[edge[i]] == (WHITE | KING)) nwke++;
            }
        }
        eval -= (nbme - nwme) * mev;
        eval -= (nbke - nwke) * kev;



   /* tempo */
        for (i = 5; i < 41; i++) {
            if (b[i] == (BLACK | PAWN))
                tempo += row[i];
            if (b[i] == (WHITE | PAWN))
                tempo -= 7 - row[i];
        }

        if (nm >= 16) eval += opening * tempo;
        if ((nm <= 15) && (nm >= 12)) eval += midgame * tempo;
        if (nm < 9) eval += endgame * tempo;


        for (i = 0; i < 4; i++) {
            if (nbk + nbm > nwk + nwm && nwk < 3) {
                if (b[safeedge[i]] == (WHITE | KING))
                    eval -= 15;
            }
            if (nwk + nwm > nbk + nbm && nbk < 3) {
                if (b[safeedge[i]] == (BLACK | KING))
                    eval += 15;
            }
        }





   /* the move */
        if (nwm + nwk - nbk - nbm == 0) {
            if (color == BLACK) {
                for (i = 5; i <= 8; i++) {
                    for (j = 0; j < 4; j++) {
                        if (b[i + 9 * j] != FREE) stonesinsystem++;
                    }
                }
                if ((stonesinsystem % 2) != 0) {
                    if (nm + nk <= 12) eval++;
                    if (nm + nk <= 10) eval++;
                    if (nm + nk <= 8) eval += 2;
                    if (nm + nk <= 6) eval += 2;
                } else {
                    if (nm + nk <= 12) eval--;
                    if (nm + nk <= 10) eval--;
                    if (nm + nk <= 8) eval -= 2;
                    if (nm + nk <= 6) eval -= 2;
                }
            } else {
                for (i = 10; i <= 13; i++) {
                    for (j = 0; j < 4; j++) {
                        if (b[i + 9 * j] != FREE) stonesinsystem++;
                    }
                }
                if ((stonesinsystem % 2) == 0) {
                    if (nm + nk <= 12) eval++;
                    if (nm + nk <= 10) eval++;
                    if (nm + nk <= 8) eval += 2;
                    if (nm + nk <= 6) eval += 2;
                } else {
                    if (nm + nk <= 12) eval--;
                    if (nm + nk <= 10) eval--;
                    if (nm + nk <= 8) eval -= 2;
                    if (nm + nk <= 6) eval -= 2;
                }
            }
        }


        return (eval);
    }



/*-------------- PART III: MOVE GENERATION -----------------------------------*/

    int generatemovelist(int[] b, Move[] movelist, int color)
/*----------> purpose:generates all moves. no captures. returns number of moves
  ----------> version: 1.0
  ----------> date: 25th october 97 */ {
        int n = 0, m;
        int i;

        if (STATISTICS)
            generatemovelists++;


        if (color == BLACK) {
            for (i = 5; i <= 40; i++) {
                if ((b[i] & BLACK) != 0) {
                    if ((b[i] & PAWN) != 0) {
                        if ((b[i + 4] & FREE) != 0) {
                            movelist[n].n = 2;
                            if (i >= 32) m = (BLACK | KING);
                            else m = (BLACK | PAWN);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i + 4;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (BLACK | PAWN);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i + 5] & FREE) != 0) {
                            movelist[n].n = 2;
                            if (i >= 32) m = (BLACK | KING);
                            else m = (BLACK | PAWN);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i + 5;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (BLACK | PAWN);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                    }
                    if ((b[i] & KING) != 0) {
                        if ((b[i + 4] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (BLACK | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i + 4;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (BLACK | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i + 5] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (BLACK | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i + 5;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (BLACK | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i - 4] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (BLACK | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i - 4;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (BLACK | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i - 5] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (BLACK | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i - 5;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (BLACK | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                    }
                }
            }
        } else    /* color = WHITE */ {
            for (i = 5; i <= 40; i++) {
                if ((b[i] & WHITE) != 0) {
                    if ((b[i] & PAWN) != 0) {
                        if ((b[i - 4] & FREE) != 0) {
                            movelist[n].n = 2;
                            if (i <= 13) m = (WHITE | KING);
                            else m = (WHITE | PAWN);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i - 4;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (WHITE | PAWN);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i - 5] & FREE) != 0) {
                            movelist[n].n = 2;
                            if (i <= 13) m = (WHITE | KING);
                            else m = (WHITE | PAWN);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i - 5;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (WHITE | PAWN);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                    }
                    if ((b[i] & KING) != 0)  /* or else */ {
                        if ((b[i + 4] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (WHITE | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i + 4;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (WHITE | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i + 5] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (WHITE | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i + 5;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (WHITE | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i - 4] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (WHITE | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i - 4;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (WHITE | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                        if ((b[i - 5] & FREE) != 0) {
                            movelist[n].n = 2;
                            m = (WHITE | KING);
                            m = m << 8;
                            m += FREE;
                            m = m << 8;
                            m += i - 5;
                            movelist[n].m[1] = m;
                            m = FREE;
                            m = m << 8;
                            m += (WHITE | KING);
                            m = m << 8;
                            m += i;
                            movelist[n].m[0] = m;
                            n++;
                        }
                    }
                }
            }
        }
        return (n);
    }

    int generatecapturelist(int[] b, Move[] movelist, int color)
/*----------> purpose: generate all possible captures
  ----------> version: 1.0
  ----------> date: 25th october 97 */ {
        int n = 0;
        int m;
        int i;
        int tmp;

        if (STATISTICS)
            generatecapturelists++;


        if (color == BLACK) {
            for (i = 5; i <= 40; i++) {
                if ((b[i] & BLACK) != 0) {
                    if ((b[i] & PAWN) != 0) {
                        if ((b[i + 4] & WHITE) != 0) {
                            if ((b[i + 8] & FREE) != 0) {
                                movelist[n].n = 3;
                                if (i >= 28) m = (BLACK | KING);
                                else m = (BLACK | PAWN);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i + 8;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (BLACK | PAWN);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i + 4];
                                m = m << 8;
                                m += i + 4;
                                movelist[n].m[2] = m;
                                n = blackPAWNcapture(b, n, movelist, i + 8);
                            }
                        }
                        if ((b[i + 5] & WHITE) != 0) {
                            if ((b[i + 10] & FREE) != 0) {
                                movelist[n].n = 3;
                                if (i >= 28) m = (BLACK | KING);
                                else m = (BLACK | PAWN);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i + 10;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (BLACK | PAWN);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i + 5];
                                m = m << 8;
                                m += i + 5;
                                movelist[n].m[2] = m;
                                n = blackPAWNcapture(b, n, movelist, i + 10);
                            }
                        }
                    } else /* b[i] is a KING */ {
                        if ((b[i + 4] & WHITE) != 0) {
                            if ((b[i + 8] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (BLACK | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i + 8;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (BLACK | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i + 4];
                                m = m << 8;
                                m += i + 4;
                                movelist[n].m[2] = m;
                                tmp = b[i + 4];
                                b[i + 4] = FREE;
                                n = blackkingcapture(b, n, movelist, i + 8);
                                b[i + 4] = tmp;
                            }
                        }
                        if ((b[i + 5] & WHITE) != 0) {
                            if ((b[i + 10] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (BLACK | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i + 10;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (BLACK | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i + 5];
                                m = m << 8;
                                m += i + 5;
                                movelist[n].m[2] = m;
                                tmp = b[i + 5];
                                b[i + 5] = FREE;
                                n = blackkingcapture(b, n, movelist, i + 10);
                                b[i + 5] = tmp;
                            }
                        }
                        if ((b[i - 4] & WHITE) != 0) {
                            if ((b[i - 8] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (BLACK | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i - 8;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (BLACK | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i - 4];
                                m = m << 8;
                                m += i - 4;
                                movelist[n].m[2] = m;
                                tmp = b[i - 4];
                                b[i - 4] = FREE;
                                n = blackkingcapture(b, n, movelist, i - 8);
                                b[i - 4] = tmp;
                            }
                        }
                        if ((b[i - 5] & WHITE) != 0) {
                            if ((b[i - 10] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (BLACK | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i - 10;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (BLACK | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i - 5];
                                m = m << 8;
                                m += i - 5;
                                movelist[n].m[2] = m;
                                tmp = b[i - 5];
                                b[i - 5] = FREE;
                                n = blackkingcapture(b, n, movelist, i - 10);
                                b[i - 5] = tmp;
                            }
                        }
                    }
                }
            }
        } else /* color is WHITE */ {
            for (i = 5; i <= 40; i++) {
                if ((b[i] & WHITE) != 0) {
                    if ((b[i] & PAWN) != 0) {
                        if ((b[i - 4] & BLACK) != 0) {
                            if ((b[i - 8] & FREE) != 0) {
                                movelist[n].n = 3;
                                if (i <= 17) m = (WHITE | KING);
                                else m = (WHITE | PAWN);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i - 8;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (WHITE | PAWN);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i - 4];
                                m = m << 8;
                                m += i - 4;
                                movelist[n].m[2] = m;
                                n = whitePAWNcapture(b, n, movelist, i - 8);
                            }
                        }
                        if ((b[i - 5] & BLACK) != 0) {
                            if ((b[i - 10] & FREE) != 0) {
                                movelist[n].n = 3;
                                if (i <= 17) m = (WHITE | KING);
                                else m = (WHITE | PAWN);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i - 10;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (WHITE | PAWN);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i - 5];
                                m = m << 8;
                                m += i - 5;
                                movelist[n].m[2] = m;
                                n = whitePAWNcapture(b, n, movelist, i - 10);
                            }
                        }
                    } else /* b[i] is a KING */ {
                        if ((b[i + 4] & BLACK) != 0) {
                            if ((b[i + 8] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (WHITE | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i + 8;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (WHITE | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i + 4];
                                m = m << 8;
                                m += i + 4;
                                movelist[n].m[2] = m;
                                tmp = b[i + 4];
                                b[i + 4] = FREE;
                                n = whitekingcapture(b, n, movelist, i + 8);
                                b[i + 4] = tmp;
                            }
                        }
                        if ((b[i + 5] & BLACK) != 0) {
                            if ((b[i + 10] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (WHITE | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i + 10;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (WHITE | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i + 5];
                                m = m << 8;
                                m += i + 5;
                                movelist[n].m[2] = m;
                                tmp = b[i + 5];
                                b[i + 5] = FREE;
                                n = whitekingcapture(b, n, movelist, i + 10);
                                b[i + 5] = tmp;
                            }
                        }
                        if ((b[i - 4] & BLACK) != 0) {
                            if ((b[i - 8] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (WHITE | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i - 8;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (WHITE | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i - 4];
                                m = m << 8;
                                m += i - 4;
                                movelist[n].m[2] = m;
                                tmp = b[i - 4];
                                b[i - 4] = FREE;
                                n = whitekingcapture(b, n, movelist, i - 8);
                                b[i - 4] = tmp;
                            }
                        }
                        if ((b[i - 5] & BLACK) != 0) {
                            if ((b[i - 10] & FREE) != 0) {
                                movelist[n].n = 3;
                                m = (WHITE | KING);
                                m = m << 8;
                                m += FREE;
                                m = m << 8;
                                m += i - 10;
                                movelist[n].m[1] = m;
                                m = FREE;
                                m = m << 8;
                                m += (WHITE | KING);
                                m = m << 8;
                                m += i;
                                movelist[n].m[0] = m;
                                m = FREE;
                                m = m << 8;
                                m += b[i - 5];
                                m = m << 8;
                                m += i - 5;
                                movelist[n].m[2] = m;
                                tmp = b[i - 5];
                                b[i - 5] = FREE;
                                n = whitekingcapture(b, n, movelist, i - 10);
                                b[i - 5] = tmp;
                            }
                        }
                    }
                }
            }
        }
        return (n);
    }

    int blackPAWNcapture(int[] b, int n, Move[] movelist, int i) {
        int m;
        boolean found = false;
        Move move, orgmove;

        orgmove = movelist[n];
        move = orgmove;

        if ((b[i + 4] & WHITE) != 0) {
            if ((b[i + 8] & FREE) != 0) {
                move.n++;
                if (i >= 28) m = (BLACK | KING);
                else m = (BLACK | PAWN);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += (i + 8);
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i + 4];
                m = m << 8;
                m += (i + 4);
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                blackPAWNcapture(b, n, movelist, i + 8);
            }
        }
        move = orgmove;
        if ((b[i + 5] & WHITE) != 0) {
            if ((b[i + 10] & FREE) != 0) {
                move.n++;
                if (i >= 28) m = (BLACK | KING);
                else m = (BLACK | PAWN);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += (i + 10);
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i + 5];
                m = m << 8;
                m += (i + 5);
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                blackPAWNcapture(b, n, movelist, i + 10);
            }
        }
        if (!found)
            n++;

        return n;
    }

    int blackkingcapture(int[] b, int n, Move[] movelist, int i) {
        int m;
        int tmp;
        boolean found = false;
        Move move, orgmove;

        orgmove = movelist[n];
        move = orgmove;

        if ((b[i - 4] & WHITE) != 0) {
            if ((b[i - 8] & FREE) != 0) {
                move.n++;
                m = (BLACK | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i - 8;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i - 4];
                m = m << 8;
                m += i - 4;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i - 4];
                b[i - 4] = FREE;
                blackkingcapture(b, n, movelist, i - 8);
                b[i - 4] = tmp;
            }
        }
        move = orgmove;
        if ((b[i - 5] & WHITE) != 0) {
            if ((b[i - 10] & FREE) != 0) {
                move.n++;
                m = (BLACK | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i - 10;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i - 5];
                m = m << 8;
                m += i - 5;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i - 5];
                b[i - 5] = FREE;
                blackkingcapture(b, n, movelist, i - 10);
                b[i - 5] = tmp;
            }
        }
        move = orgmove;
        if ((b[i + 4] & WHITE) != 0) {
            if ((b[i + 8] & FREE) != 0) {
                move.n++;
                m = (BLACK | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i + 8;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i + 4];
                m = m << 8;
                m += i + 4;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i + 4];
                b[i + 4] = FREE;
                blackkingcapture(b, n, movelist, i + 8);
                b[i + 4] = tmp;
            }
        }
        move = orgmove;
        if ((b[i + 5] & WHITE) != 0) {
            if ((b[i + 10] & FREE) != 0) {
                move.n++;
                m = (BLACK | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i + 10;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i + 5];
                m = m << 8;
                m += i + 5;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i + 5];
                b[i + 5] = FREE;
                blackkingcapture(b, n, movelist, i + 10);
                b[i + 5] = tmp;
            }
        }
        if (!found) n++;

        return n;
    }

    int whitePAWNcapture(int[] b, int n, Move[] movelist, int i) {
        int m;
        boolean found = false;
        Move move, orgmove;

        orgmove = movelist[n];
        move = orgmove;

        if ((b[i - 4] & BLACK) != 0) {
            if ((b[i - 8] & FREE) != 0) {
                move.n++;
                if (i <= 17) m = (WHITE | KING);
                else m = (WHITE | PAWN);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i - 8;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i - 4];
                m = m << 8;
                m += i - 4;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                whitePAWNcapture(b, n, movelist, i - 8);
            }
        }
        move = orgmove;
        if ((b[i - 5] & BLACK) != 0) {
            if ((b[i - 10] & FREE) != 0) {
                move.n++;
                if (i <= 17) m = (WHITE | KING);
                else m = (WHITE | PAWN);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i - 10;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i - 5];
                m = m << 8;
                m += i - 5;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                whitePAWNcapture(b, n, movelist, i - 10);
            }
        }
        if (!found) n++;

        return n;
    }

    int whitekingcapture(int[] b, int n, Move[] movelist, int i) {
        int m;
        int tmp;
        boolean found = false;
        Move move, orgmove;

        orgmove = movelist[n];
        move = orgmove;

        if ((b[i - 4] & BLACK) != 0) {
            if ((b[i - 8] & FREE) != 0) {
                move.n++;
                m = (WHITE | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i - 8;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i - 4];
                m = m << 8;
                m += i - 4;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i - 4];
                b[i - 4] = FREE;
                whitekingcapture(b, n, movelist, i - 8);
                b[i - 4] = tmp;
            }
        }
        move = orgmove;
        if ((b[i - 5] & BLACK) != 0) {
            if ((b[i - 10] & FREE) != 0) {
                move.n++;
                m = (WHITE | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i - 10;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i - 5];
                m = m << 8;
                m += i - 5;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i - 5];
                b[i - 5] = FREE;
                whitekingcapture(b, n, movelist, i - 10);
                b[i - 5] = tmp;
            }
        }
        move = orgmove;
        if ((b[i + 4] & BLACK) != 0) {
            if ((b[i + 8] & FREE) != 0) {
                move.n++;
                m = (WHITE | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i + 8;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i + 4];
                m = m << 8;
                m += i + 4;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i + 4];
                b[i + 4] = FREE;
                whitekingcapture(b, n, movelist, i + 8);
                b[i + 4] = tmp;
            }
        }
        move = orgmove;
        if ((b[i + 5] & BLACK) != 0) {
            if ((b[i + 10] & FREE) != 0) {
                move.n++;
                m = (WHITE | KING);
                m = m << 8;
                m += FREE;
                m = m << 8;
                m += i + 10;
                move.m[1] = m;
                m = FREE;
                m = m << 8;
                m += b[i + 5];
                m = m << 8;
                m += i + 5;
                move.m[move.n - 1] = m;
                found = true;
                movelist[n] = move;
                tmp = b[i + 5];
                b[i + 5] = FREE;
                whitekingcapture(b, n, movelist, i + 10);
                b[i + 5] = tmp;
            }
        }
        if (!found) n++;

        return n;
    }

    boolean testcapture(int[] b, int color)
/*----------> purpose: test if color has a capture on b
  ----------> version: 1.0
  ----------> date: 25th october 97 */ {
        int i = 0;

        if (STATISTICS)
            testcaptures++;

        if (color == BLACK) {
            for (i = 5; i <= 40; i++) {
                if ((b[i] & BLACK) != 0) {
                    if ((b[i] & PAWN) != 0) {
                        if ((b[i + 4] & WHITE) != 0) {
                            if ((b[i + 8] & FREE) != 0)
                                return true;
                        }
                        if ((b[i + 5] & WHITE) != 0) {
                            if ((b[i + 10] & FREE) != 0)
                                return true;
                        }
                    } else /* b[i] is a KING */ {
                        if ((b[i + 4] & WHITE) != 0) {
                            if ((b[i + 8] & FREE) != 0)
                                return true;
                        }
                        if ((b[i + 5] & WHITE) != 0) {
                            if ((b[i + 10] & FREE) != 0)
                                return true;
                        }
                        if ((b[i - 4] & WHITE) != 0) {
                            if ((b[i - 8] & FREE) != 0)
                                return true;
                        }
                        if ((b[i - 5] & WHITE) != 0) {
                            if ((b[i - 10] & FREE) != 0)
                                return true;
                        }
                    }
                }
            }
        } else /* color is WHITE */ {
            for (i = 5; i <= 40; i++) {
                if ((b[i] & WHITE) != 0) {
                    if ((b[i] & PAWN) != 0) {
                        if ((b[i - 4] & BLACK) != 0) {
                            if ((b[i - 8] & FREE) != 0)
                                return true;
                        }
                        if ((b[i - 5] & BLACK) != 0) {
                            if ((b[i - 10] & FREE) != 0)
                                return true;
                        }
                    } else /* b[i] is a KING */ {
                        if ((b[i + 4] & BLACK) != 0) {
                            if ((b[i + 8] & FREE) != 0)
                                return true;
                        }
                        if ((b[i + 5] & BLACK) != 0) {
                            if ((b[i + 10] & FREE) != 0)
                                return true;
                        }
                        if ((b[i - 4] & BLACK) != 0) {
                            if ((b[i - 8] & FREE) != 0)
                                return true;
                        }
                        if ((b[i - 5] & BLACK) != 0) {
                            if ((b[i - 10] & FREE) != 0)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    CbMove setMove(Move move) {
        int i;
        int jumps;
        int from, to;
        Coord c1;
        Coord c2;
        CbMove cbMove = new CbMove();
        jumps = move.n - 2;

        from = move.m[0] % 256;
        to = move.m[1] % 256;

        cbMove.from = from;
        cbMove.to = to;
        cbMove.cdFrom = toCoord(from);
        cbMove.cdTo = toCoord(to);
        cbMove.jumps = jumps;
        cbMove.newpiece = ((move.m[1] >> 16) % 256);
        cbMove.oldpiece = ((move.m[0] >> 8) % 256);

        for (i = 2; i < move.n; i++) {
            cbMove.delpiece[i - 2] = ((move.m[i] >> 8) % 256);
            cbMove.del[i - 2] = toCoord(move.m[i] % 256);
        }

        if (jumps > 1)
        /* more than one jump - need to calculate intermediate squares*/ {
        /* set square where we start to c1 */
            c1 = toCoord(from);
            for (i = 2; i < move.n; i++) {
                c2 = toCoord(move.m[i] % 256);
            /* c2 is the piece we jump */
            /* => we land on the next square?! */
                if (c2.x > c1.x)
                    c2.x++;
                else
                    c2.x--;

                if (c2.y > c1.y)
                    c2.y++;
                else
                    c2.y--;
            /* now c2 holds the square after the jumped piece - this is our path square */
                cbMove.path[i - 1] = c2;
                c1 = c2;
            }
        } else {
            cbMove.path[1] = toCoord(to);
        }

        return cbMove;
    }


    protected Coord toCoord(int n) {
    /* turns square number n into a coordinate for checkerboard */
   /*    (white)
   				 37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */
        Coord c = new Coord();
        switch (n) {
            case 5:
                c.x = 0;
                c.y = 0;
                break;
            case 6:
                c.x = 2;
                c.y = 0;
                break;
            case 7:
                c.x = 4;
                c.y = 0;
                break;
            case 8:
                c.x = 6;
                c.y = 0;
                break;
            case 10:
                c.x = 1;
                c.y = 1;
                break;
            case 11:
                c.x = 3;
                c.y = 1;
                break;
            case 12:
                c.x = 5;
                c.y = 1;
                break;
            case 13:
                c.x = 7;
                c.y = 1;
                break;
		   /*    (white)
   				 37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */
            case 14:
                c.x = 0;
                c.y = 2;
                break;
            case 15:
                c.x = 2;
                c.y = 2;
                break;
            case 16:
                c.x = 4;
                c.y = 2;
                break;
            case 17:
                c.x = 6;
                c.y = 2;
                break;
            case 19:
                c.x = 1;
                c.y = 3;
                break;
            case 20:
                c.x = 3;
                c.y = 3;
                break;
            case 21:
                c.x = 5;
                c.y = 3;
                break;
            case 22:
                c.x = 7;
                c.y = 3;
                break;
		   /*    (white)
   				 37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */
            case 23:
                c.x = 0;
                c.y = 4;
                break;
            case 24:
                c.x = 2;
                c.y = 4;
                break;
            case 25:
                c.x = 4;
                c.y = 4;
                break;
            case 26:
                c.x = 6;
                c.y = 4;
                break;
            case 28:
                c.x = 1;
                c.y = 5;
                break;
            case 29:
                c.x = 3;
                c.y = 5;
                break;
            case 30:
                c.x = 5;
                c.y = 5;
                break;
            case 31:
                c.x = 7;
                c.y = 5;
                break;
		   /*    (white)
   				 37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */
            case 32:
                c.x = 0;
                c.y = 6;
                break;
            case 33:
                c.x = 2;
                c.y = 6;
                break;
            case 34:
                c.x = 4;
                c.y = 6;
                break;
            case 35:
                c.x = 6;
                c.y = 6;
                break;
            case 37:
                c.x = 1;
                c.y = 7;
                break;
            case 38:
                c.x = 3;
                c.y = 7;
                break;
            case 39:
                c.x = 5;
                c.y = 7;
                break;
            case 40:
                c.x = 7;
                c.y = 7;
                break;
        }
		   /*    (white)
   				 37  38  39  40
              32  33  34  35
                28  29  30  31
              23  24  25  26
                19  20  21  22
              14  15  16  17
                10  11  12  13
               5   6   7   8
         (black)   */

        return c;
    }

    public static int toNumber(Coord c) {
        // board coordinates are [y][x]!
        // ENGLISH
        int[][] en = new int[][]{
                {
                        4, 0, 3, 0, 2, 0, 1, 0
                },
                {
                        0, 8, 0, 7, 0, 6, 0, 5
                },
                {
                        12, 0, 11, 0, 10, 0, 9, 0
                },
                {
                        0, 16, 0, 15, 0, 14, 0, 13
                },
                {
                        20, 0, 19, 0, 18, 0, 17, 0
                },
                {
                        0, 24, 0, 23, 0, 22, 0, 21
                },
                {
                        28, 0, 27, 0, 26, 0, 25, 0
                },
                {
                        0, 32, 0, 31, 0, 30, 0, 29
                },};

        return en[c.y][c.x];
    }

}


