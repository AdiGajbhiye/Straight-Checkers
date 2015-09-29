/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

import java.util.Vector;

/**
 *
 * @author ASHISH
 */
public class Black {
    
    static Owner owner;
    
    public static void Move(){
        
        UserInteractions.PrintSeparator('-');
        System.out.println("\t\tBLACK's TURN");
        UserInteractions.PrintSeparator('-');
        
        if (owner.equals(com.pennywise.checkers.core.logic.Owner.HUMAN)){
            
            Human.makeNextBlackMoves();
            
        }
        else{
            
            assert(owner.equals(com.pennywise.checkers.core.logic.Owner.ROBOT));
            com.pennywise.checkers.core.logic.Robot.makeNextBlackMoves();
            
        }
    }
    
    
    
    public static Vector<com.pennywise.checkers.core.logic.Move> ObtainForcedMovesForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board)
    {        
        Vector<com.pennywise.checkers.core.logic.Move> furtherCaptures = new Vector<com.pennywise.checkers.core.logic.Move>();
        
        if (board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.black) || board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.blackKing))
        {
            if (ForwardLeftCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(ForwardLeftCaptureForBlack(r,c,board));
            if (ForwardRightCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(ForwardRightCaptureForBlack(r,c,board));
        }        
        
        if(board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.blackKing))
        {
            if (BackwardLeftCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(BackwardLeftCaptureForBlack(r,c,board));
            if (BackwardRightCaptureForBlack(r,c,board)!=null)
                furtherCaptures.add(BackwardRightCaptureForBlack(r,c,board));
        }
        
        return furtherCaptures;
    }

    public static Vector<com.pennywise.checkers.core.logic.Move> CalculateAllForcedMovesForBlack(com.pennywise.checkers.core.logic.Board board)
    {        
        Vector<com.pennywise.checkers.core.logic.Move> forcedMovesForBlack = new Vector<com.pennywise.checkers.core.logic.Move>();
        
        // Scan across the board
        for(int r = 0; r< com.pennywise.checkers.core.logic.Board.rows; r++)
        {            
            // Check only valid cols
            int c = (r%2==0)?0:1;
            for(; c< com.pennywise.checkers.core.logic.Board.cols; c+=2)
            {       
                assert(!board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.inValid));
                
                // Forward Capture
                if(
                        board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.black) ||
                        board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.blackKing)
                  )
                {       
                    // Boundary Condition for forward capture for black
                    if (r>=2)
                    {    
                        // Forward Left Capture for black
                        if (ForwardLeftCaptureForBlack(r,c, board)!=null)
                            forcedMovesForBlack.add(ForwardLeftCaptureForBlack(r,c, board));                        
                        
                        // Forward Right Capture for black
                        if (ForwardRightCaptureForBlack(r,c, board)!=null)
                            forcedMovesForBlack.add(ForwardRightCaptureForBlack(r,c, board));
                    }                   
                }
                // Backward Capture for Black King
                if(board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.blackKing))
                {
                    // Boundary Condition for backward capture
                    if (r< com.pennywise.checkers.core.logic.Board.rows-2)
                    {          
                        // Backward Left Capture for black king
                        if (BackwardLeftCaptureForBlack(r,c,board)!=null)
                            forcedMovesForBlack.add(BackwardLeftCaptureForBlack(r,c, board));
                        
                        // Backward Right Capture for black king
                        if (BackwardRightCaptureForBlack(r,c,board)!=null)
                            forcedMovesForBlack.add(BackwardRightCaptureForBlack(r,c,board));                        
                    }
                }
            }    
        }
        
        return forcedMovesForBlack;
    }
    
    /**
     * Returns a vector of all possible moves which Black can make at the state of the game given by board.
     * 
     * Should only be called if no forced moves exist.
     * 
     * @param board
     * @return 
     */
    public static Vector<com.pennywise.checkers.core.logic.Move> CalculateAllNonForcedMovesForBlack(com.pennywise.checkers.core.logic.Board board){
        Vector<com.pennywise.checkers.core.logic.Move> allNonForcedMovesForBlack = new Vector<com.pennywise.checkers.core.logic.Move>();

        // Scan across the board
        for(int r = 0; r< com.pennywise.checkers.core.logic.Board.rows; r++)
        {            
            // Check only valid cols
            int c = (r%2==0)?0:1;
            for(; c< com.pennywise.checkers.core.logic.Board.cols; c+=2)
            {       
                assert(!board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.inValid));
                
                // Forward Move for normal black piece.
                if( board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.black) ){

                    com.pennywise.checkers.core.logic.Move move = null;
                    move = ForwardLeftCaptureForBlack(r, c, board);
                    assert(move == null);                    
                    move = ForwardRightCaptureForBlack(r, c, board);
                    assert(move == null);
                    
                    move = ForwardLeftForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);   
                    }
                    
                    move = ForwardRightForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                }
                
                //Forward and Backward Move for black king piece.
                if(board.cell[r][c] == com.pennywise.checkers.core.logic.CellEntry.blackKing){
                    com.pennywise.checkers.core.logic.Move move = null;
                    move = ForwardLeftCaptureForBlack(r, c, board);
                    assert(move == null);                    
                    move = ForwardRightCaptureForBlack(r, c, board);
                    assert(move == null);

                    move = BackwardLeftCaptureForBlack(r, c, board);
                    assert(move == null);
                    move = BackwardRightCaptureForBlack(r, c, board);
                    assert(move == null);
                    
                    move = ForwardLeftForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                    move = ForwardRightForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                    move = BackwardLeftForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                    move = BackwardRightForBlack(r, c, board);
                    if(move!=null){
                        allNonForcedMovesForBlack.add(move);
                    }
                    
                }

                
            }
        }

        return allNonForcedMovesForBlack;
    }

    
    
    
    
    private static com.pennywise.checkers.core.logic.Move ForwardLeftForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        com.pennywise.checkers.core.logic.Move forwardLeft = null;
        
        assert(board.cell[r][c] == com.pennywise.checkers.core.logic.CellEntry.black || board.cell[r][c] == com.pennywise.checkers.core.logic.CellEntry.blackKing);
        
        if( r>=1 && c< com.pennywise.checkers.core.logic.Board.cols-1 &&
                board.cell[r-1][c+1] == com.pennywise.checkers.core.logic.CellEntry.empty
                
          )
        {
            forwardLeft = new com.pennywise.checkers.core.logic.Move(r,c,r-1, c+1);
        }
        return forwardLeft;
    }
    
    // Forward Left Capture for Black
    private static com.pennywise.checkers.core.logic.Move ForwardLeftCaptureForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board)
    {        
        com.pennywise.checkers.core.logic.Move forwardLeftCapture = null;
        
        if(r>=2 && c< com.pennywise.checkers.core.logic.Board.cols-2 &&
                (
                board.cell[r-1][c+1].equals(com.pennywise.checkers.core.logic.CellEntry.white)
                || board.cell[r-1][c+1].equals(com.pennywise.checkers.core.logic.CellEntry.whiteKing)
                )
                && board.cell[r-2][c+2].equals(com.pennywise.checkers.core.logic.CellEntry.empty)
                )
        {
             forwardLeftCapture = new com.pennywise.checkers.core.logic.Move(r,c,r-2,c+2);
             //System.out.println("Forward Left Capture for Black");
        }        
        
        return forwardLeftCapture;
    }
    
    
    
    //Forward Right for Black
    private static com.pennywise.checkers.core.logic.Move ForwardRightForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        com.pennywise.checkers.core.logic.Move forwardRight = null;
        if( r>=1 && c>=1 &&
                board.cell[r-1][c-1] == com.pennywise.checkers.core.logic.CellEntry.empty
          )
        {
            forwardRight = new com.pennywise.checkers.core.logic.Move(r,c, r-1, c-1);
        }
        return forwardRight;
    }
    
    // Forward Right Capture for White
    private static com.pennywise.checkers.core.logic.Move ForwardRightCaptureForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        
        com.pennywise.checkers.core.logic.Move forwardRightCapture = null;
        
        if(r>=2 && c>=2 && (
                board.cell[r-1][c-1].equals(com.pennywise.checkers.core.logic.CellEntry.white)
                || board.cell[r-1][c-1].equals(com.pennywise.checkers.core.logic.CellEntry.whiteKing)
                )
                && board.cell[r-2][c-2].equals(com.pennywise.checkers.core.logic.CellEntry.empty)
                )
        {
            forwardRightCapture = new com.pennywise.checkers.core.logic.Move(r,c,r-2,c-2);
            //System.out.println("Forward Right Capture for Black");
        }
        
        return forwardRightCapture;
    }
    
    private static com.pennywise.checkers.core.logic.Move BackwardLeftForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        com.pennywise.checkers.core.logic.Move backwardLeft = null;
        
        assert(board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.blackKing));
        if(r< com.pennywise.checkers.core.logic.Board.rows-1 && c< com.pennywise.checkers.core.logic.Board.cols-1 &&
           board.cell[r+1][c+1] == com.pennywise.checkers.core.logic.CellEntry.empty
          )
        {
            backwardLeft = new com.pennywise.checkers.core.logic.Move(r,c, r+1, c+1);
        }
        
        return backwardLeft;
    }
    
     // Backward Left Capture for Black
    private static com.pennywise.checkers.core.logic.Move BackwardLeftCaptureForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        
        com.pennywise.checkers.core.logic.Move backwardLeftCapture = null;
        
        if(r< com.pennywise.checkers.core.logic.Board.rows-2 && c< com.pennywise.checkers.core.logic.Board.cols-2 && (
                board.cell[r+1][c+1].equals(com.pennywise.checkers.core.logic.CellEntry.white)
                || board.cell[r+1][c+1].equals(com.pennywise.checkers.core.logic.CellEntry.whiteKing)
                )
                && board.cell[r+2][c+2].equals(com.pennywise.checkers.core.logic.CellEntry.empty)
                )
        {
             backwardLeftCapture = new com.pennywise.checkers.core.logic.Move(r,c,r+2,c+2);
             //System.out.println("Backward Left Capture for Black");
        }
        
        return backwardLeftCapture;
    }
    
    
    private static com.pennywise.checkers.core.logic.Move BackwardRightForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        com.pennywise.checkers.core.logic.Move backwardRight = null;
        
        assert(board.cell[r][c].equals(com.pennywise.checkers.core.logic.CellEntry.blackKing));
        
        if(r< com.pennywise.checkers.core.logic.Board.rows-1 && c>=1 &&
           board.cell[r+1][c-1].equals(com.pennywise.checkers.core.logic.CellEntry.empty)
          )
        {
            backwardRight = new com.pennywise.checkers.core.logic.Move(r,c, r+1, c-1);
        }
        return backwardRight;
    }
    
    
    // Backward Right Capture for Black
    private static com.pennywise.checkers.core.logic.Move BackwardRightCaptureForBlack(int r, int c, com.pennywise.checkers.core.logic.Board board){
        
        com.pennywise.checkers.core.logic.Move backwardRightCapture = null;
        
        if(r< com.pennywise.checkers.core.logic.Board.rows-2 && c>=2 && (
                board.cell[r+1][c-1].equals(com.pennywise.checkers.core.logic.CellEntry.white) ||
                board.cell[r+1][c-1].equals(com.pennywise.checkers.core.logic.CellEntry.whiteKing)
                )
                && board.cell[r+2][c-2].equals(com.pennywise.checkers.core.logic.CellEntry.empty)
                )
        {
            backwardRightCapture = new com.pennywise.checkers.core.logic.Move(r,c,r+2,c-2);
            //System.out.println("Backward Right Capture for Black");
        }
        
        return backwardRightCapture;
    }
}