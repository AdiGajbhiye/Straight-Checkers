/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pennywise.checkers.core.logic;

/**
 * @author ASHISH
 */
public class Game {

    static com.pennywise.checkers.core.logic.Board board;

    Game() {
        this.Initialize(UserInteractions.GameChoice().charAt(0));
    }

    public void PlayGame() {
        while (!Game.board.CheckGameComplete()) {
            if (Game.board.CheckGameDraw(com.pennywise.checkers.core.logic.Player.white)) {
                break;
            }

            com.pennywise.checkers.core.logic.White.Move();
            if (Game.board.CheckGameComplete()) {
                UserInteractions.DisplayGreetings(com.pennywise.checkers.core.logic.Player.white);
                Game.board.Display();
                break;
            }

            if (Game.board.CheckGameDraw(com.pennywise.checkers.core.logic.Player.black)) {
                break;
            }

            Game.board.Display();

            Black.Move();
            if (Game.board.CheckGameComplete()) {
                UserInteractions.DisplayGreetings(com.pennywise.checkers.core.logic.Player.black);
                Game.board.Display();
                break;
            }


        }
    }


    private void Initialize(char human) {
        assert (human == 'w' || human == 'b' || human == 'a' || human == 'n');

        board = new com.pennywise.checkers.core.logic.Board();

        switch (human) {
            case 'w':
                com.pennywise.checkers.core.logic.White.owner = com.pennywise.checkers.core.logic.Owner.HUMAN;
                Black.owner = com.pennywise.checkers.core.logic.Owner.ROBOT;
                break;
            case 'b':
                com.pennywise.checkers.core.logic.White.owner = com.pennywise.checkers.core.logic.Owner.ROBOT;
                Black.owner = com.pennywise.checkers.core.logic.Owner.HUMAN;
                break;
            case 'a':
                com.pennywise.checkers.core.logic.White.owner = com.pennywise.checkers.core.logic.Owner.HUMAN;
                Black.owner = com.pennywise.checkers.core.logic.Owner.HUMAN;
                break;
            case 'n':
                com.pennywise.checkers.core.logic.White.owner = com.pennywise.checkers.core.logic.Owner.ROBOT;
                Black.owner = com.pennywise.checkers.core.logic.Owner.ROBOT;
                break;
        }
    }
}