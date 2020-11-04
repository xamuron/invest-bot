package easter_eggs.tic_tac_toe.controller;

import easter_eggs.tic_tac_toe.model.TicTacToeGame;

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
        if (game.getUserFigure().equalsIgnoreCase("X")) {
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
//            GameView.printGameView(this);
            printGameView();
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
            doUserTurnByConsole();
        }

        AITurn = !AITurn;
        game.increaseTurnNumber();
    }

    public void doTurn (int userI, int userJ) {
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
            doUserTurn(userI, userJ);
        }

        AITurn = !AITurn;
        game.increaseTurnNumber();
    }

    public void doUserTurnByConsole() {
        System.out.print("Input a number: \n");
        int numi = in.nextInt();
        int numj = in.nextInt();
        game.setFigureOnField(game.getUserFigure(), numi, numj);
    }

    public void doUserTurn(int i, int j) {
        game.setFigureOnField(game.getUserFigure(), i, j);
    }

    public boolean winCondition() {

        if (game.getGameField()[0][0].equals(game.getGameField()[1][1]) && game.getGameField()[0][0].equals(game.getGameField()[2][2]) && (game.getGameField()[0][0].equals(AIFigure) || game.getGameField()[0][0].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[0][2].equals(game.getGameField()[1][1]) && game.getGameField()[0][2].equals(game.getGameField()[2][0]) && (game.getGameField()[0][2].equals(AIFigure) || game.getGameField()[0][2].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[0][0].equals(game.getGameField()[1][0]) && game.getGameField()[0][0].equals(game.getGameField()[2][0]) && (game.getGameField()[0][0].equals(AIFigure) || game.getGameField()[0][0].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[0][2].equals(game.getGameField()[1][2]) && game.getGameField()[0][2].equals(game.getGameField()[2][2]) && (game.getGameField()[0][2].equals(AIFigure) || game.getGameField()[0][2].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[0][0].equals(game.getGameField()[0][1]) && game.getGameField()[0][0].equals(game.getGameField()[0][2]) && (game.getGameField()[0][0].equals(AIFigure) || game.getGameField()[0][0].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[1][0].equals(game.getGameField()[1][1]) && game.getGameField()[1][0].equals(game.getGameField()[1][2]) && (game.getGameField()[1][0].equals(AIFigure) || game.getGameField()[1][0].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[0][1].equals(game.getGameField()[1][1]) && game.getGameField()[0][1].equals(game.getGameField()[2][1]) && (game.getGameField()[0][1].equals(AIFigure) || game.getGameField()[0][1].equals(game.getUserFigure()))){
            return true;
        } else if(game.getGameField()[2][0].equals(game.getGameField()[2][1]) && game.getGameField()[2][0].equals(game.getGameField()[2][2]) && (game.getGameField()[2][0].equals(AIFigure) || game.getGameField()[2][0].equals(game.getUserFigure()))) {
            return true;
        }
        return false;
    }

    public String getWinner() {
        if (!AITurn)
            return "AI";
        else
            return game.getUserName();
    }

    public void printGameView() {
        System.out.println("Turn number: " + getGame().getTurn());

        String currentUser = isAITurn() ? getGame().getUserName() : "AI";
        System.out.println(currentUser + "'s turn: ");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String figure = getGame().getGameField()[i][j];
                if (!figure.isEmpty())
                    stringBuilder.append(" " + figure + " ");
                else
                    stringBuilder.append("   ");
                if (j != 2)
                    stringBuilder.append("|");
                if (j == 2)
                    stringBuilder.append("\n");
            }
        }
        System.out.println(stringBuilder.toString());
        System.out.println("***************");
    }

    public String getGameView() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Turn number: " + getGame().getTurn());
        stringBuilder.append("\n");

        String currentUser = isAITurn() ? getGame().getUserName() : "AI";
        stringBuilder.append(currentUser + "'s turn:\n");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String figure = getGame().getGameField()[i][j];
                if (!figure.isEmpty())
                    stringBuilder.append(" " + figure + " ");
                else
                    stringBuilder.append("     ");
                if (j != 2)
                    stringBuilder.append("|");
                if (j == 2)
                    stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
