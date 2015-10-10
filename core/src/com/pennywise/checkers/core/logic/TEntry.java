package com.pennywise.checkers.core.logic;

/**
 * Created by CHOXXY on 10/9/2015.
 */
public class TEntry {

    int[][] hashFunction = new int[85][6];
    int[][] hashFunction2 = new int[85][6];

    protected long m_checksum;
    protected int m_depth;
    protected int m_failtype;
    protected int m_eval;
    protected int m_bestmove;

    public int[] read(long checkSum, short alpha, short beta, int depth) {

        int value = 0;
        int bestmove = 0;

        if (m_checksum == checkSum) //To almost totally sure these are really the same position.
        {
            // Get the Value if the search was deep enough, and bounds usable
            if (m_depth >= depth) {
                if (m_failtype == 0)
                    value = m_eval; // True Value
                else if (m_failtype == 1 && m_eval <= alpha)
                    value = m_eval; //  Alpha Bound
                else if (m_failtype == 2 && m_eval >= beta)
                    value = m_eval; //  Beta Bound
            }
            // Otherwise take the best move from Transposition Table
            bestmove = m_bestmove;
        }

        return new int[]{bestmove, value};
    }

    void write(long checkSum, short alpha, short beta, int bestmove, int value, int depth) {
        m_checksum = checkSum;
        m_eval = value;
        if (value <= alpha) m_failtype = 1;
        else if (value >= beta) m_failtype = 2;
        else m_failtype = 0;
        m_depth = depth;
        m_bestmove = bestmove;
    }

    // ------------------
//  Hash Board
//
// The Hash Value really should be updated incrementally with moves (much faster.) Instead Hash_Board is called each time
// to get the hash value of the board.
// ------------------
    public static void create_HashFunction() {
        for (int i = 0; i < 65; i++)
            for (int x = 0; x < 5; x++) {
                hashFunction[i][x] = rand() + (rand() * 256) + (rand() * 65536);
                hashFunction2[i][x] = rand() + (rand() * 65536) + (rand() * 256);
            }
    }

    public static long hashBoard(char board[], char color, unsigned long&CheckSum) {
        unsigned long sum = 0;
        CheckSum = 0;
        int x, y, index;

        for (y = 0; y < 8; y++) {
            if ((y & 1) == 0) x = 1;
            else x = 0;
            for (; x < 8; x += 2) {
                index = x + (y << 3);
                sum += HashFunction[index][board[index]];
                CheckSum += HashFunction2[index][board[index]];
            }
        }
        if (color == BLACK) {
            sum += HashFunction[64][3];
            CheckSum += HashFunction2[64][3];
        }
        return sum;
    }


}
