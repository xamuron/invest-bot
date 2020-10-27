package easter_eggs.tic_tac_toe.view;

import easter_eggs.tic_tac_toe.controller.GameManager;

public class GameView {

    public static void printGameView(GameManager manager) {
        System.out.println("Turn number: " + manager.getGame().getTurn());

        String currentUser = manager.isAITurn() ? manager.getGame().getUserName() : "AI";
        System.out.println(currentUser + "'s turn: ");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String figure = manager.getGame().getGameField()[i][j];
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
}
