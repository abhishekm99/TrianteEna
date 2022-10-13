package Movement;

import Players.Piece;

public class Move {
    int x;
    int y;
    Piece pieces;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Piece getPieces() {
        return pieces;
    }

    public void setPieces(Piece pieces) {
        this.pieces = pieces;
    }

}
