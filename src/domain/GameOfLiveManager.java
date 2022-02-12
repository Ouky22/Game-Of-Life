package domain;

public class GameOfLiveManager {
    private GameOfLiveField gameOfLiveField;

    public GameOfLiveManager(int fieldHeight, int fieldWidth) {
        gameOfLiveField = new GameOfLiveField(fieldHeight, fieldWidth);
    }
}
