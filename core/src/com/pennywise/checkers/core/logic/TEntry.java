package com.pennywise.checkers.core.logic;

/**
 * Created by CHOXXY on 10/9/2015.
 */
public class TEntry {

    long m_checksum;
    int m_depth, m_failtype;
    int m_eval;
    int m_bestmove;

    int[] read(long checkSum, short alpha, short beta, int depth) {

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

        return new int[]{value, bestmove};
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


}
