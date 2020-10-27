package easter_eggs.tic_tac_toe.model;

public class TicTacToeGame {
    private String userName;
    private String userFigure;
    private int turn;
    private String[][] gameField = {{"","",""}, {"", "", ""}, {"", "", ""}};

    public TicTacToeGame(String userName, String userFigure) {
        this.userName = userName;
        this.userFigure = userFigure;
        turn = 0;
    }

    public String[][] getGameField() {
        return gameField;
    }

    public void setFigureOnField(String figure, int i, int j) {
        gameField[i][j] = figure;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFigure() {
        return userFigure;
    }

    public int getTurn() {
        return turn;
    }

    public void increaseTurnNumber() {
        turn++;
    }
}
