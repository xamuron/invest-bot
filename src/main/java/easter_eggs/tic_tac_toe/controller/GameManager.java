package easter_eggs.tic_tac_toe.controller;

import easter_eggs.tic_tac_toe.model.TicTacToeGame;
import easter_eggs.tic_tac_toe.view.GameView;

import java.util.Scanner;

public class GameManager {
    private String level;
    private String AIFigure;
    private boolean AITurn;
    private TicTacToeGame game;

    private Scanner in;

    public TicTacToeGame getGame() {
        return game;
    }

    public boolean isAITurn() {
        return AITurn;
    }

    public GameManager(TicTacToeGame game) {
        this.level = "easy";
        if (game.getUserFigure().equalsIgnoreCase("x")) {
            AIFigure = "0";
            AITurn = false;
        }
        else {
            AIFigure = "X";
            AITurn = true;
        }

        this.game = game;

        in = new Scanner(System.in);
    }

    public void run() {

        while (!winCondition()) {
            doTurn();
            GameView.printGameView(this);
        }

        in.close();

        String winner = getWinner();
        System.out.println("Winner: " + winner);
    }

    private void doTurn () {
        boolean turnIsDone = false;

        if (AITurn) {
            for (int i = 0; i < 3; i++) {
                if (turnIsDone)
                    break;
                for (int j = 0; j < 3; j++) {
                    if (game.getGameField()[i][j].isEmpty()) {
                        game.setFigureOnField(AIFigure, i, j);
                        turnIsDone = true;
                        break;
                    }
                }
            }
        }
        else {
            System.out.print("Input a number: \n");
            int numi = in.nextInt();
            int numj = in.nextInt();
            game.setFigureOnField(game.getUserFigure(), numi, numj);
        }

        AITurn = !AITurn;
        game.increaseTurnNumber();
    }

    public boolean winCondition() {

        //todo определить условия завершения игры
        return false;
    }

    public String getWinner() {
        if (!AITurn)
            return "AI";
        else
            return game.getUserName();
    }
}
