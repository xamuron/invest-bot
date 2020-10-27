package easter_eggs.tic_tac_toe;

import easter_eggs.tic_tac_toe.controller.GameManager;
import easter_eggs.tic_tac_toe.model.TicTacToeGame;

public class XOGame {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame("Jack", "x");

        GameManager gameManager = new GameManager(game);
        gameManager.run();
    }
}
