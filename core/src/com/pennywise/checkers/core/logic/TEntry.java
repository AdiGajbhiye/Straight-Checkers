package com.pennywise.checkers.core.logic;

/**
 * Created by CHOXXY on 10/9/2015.
 */
public class TEntry {

    public static class Value{
        public int bestmove;
        public int value;
    }

    void read(long checkSum, short alpha, short beta, Value v, int depth) {
        if (m_checksum == checkSum) //To almost totally sure these are really the same position.
        {
            // Get the Value if the search was deep enough, and bounds usable
            if (m_depth >= depth) {
                if (m_failtype == 0)
                    v.value = m_eval; // True Value
                else if (m_failtype == 1 && m_eval <= alpha)
                    v.value = m_eval; //  Alpha Bound
                else if (m_failtype == 2 && m_eval >= beta)
                    v.value = m_eval; //  Beta Bound
            }
            // Otherwise take the best move from Transposition Table
            v.bestmove = m_bestmove;
        }
    }

    void write(unsigned long CheckSum, short alpha, short beta, int&bestmove, int&value, int depth) {
        m_checksum = CheckSum;
        m_eval = value;
        if (value <= alpha) m_failtype = 1;
        else if (value >= beta) m_failtype = 2;
        else m_failtype = 0;
        m_depth = depth;
        m_bestmove = bestmove;
    }

    // DATA
    private :
    unsigned
    long m_checksum;
    char m_depth, m_failtype;
    short m_eval;
    short m_bestmove;
}
