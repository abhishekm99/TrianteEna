package Players;

public class Player {
    String name;
    int gameScores;

    int state; //State is an integer which decides whether player plays as X or O. Can be further extended if more pieces are added

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameScores() {
        return gameScores;
    }

    public void setGameScores(int gameScores) {
        this.gameScores = gameScores;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
