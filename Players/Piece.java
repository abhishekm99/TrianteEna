package Players;

public class Piece {
    protected String type;
    protected String symbol;

    protected int value;
//    protected Movement movement; //Will allow to specify movements if required such as L shape for Knight in chess

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

//    public Movement getMovement() {
//        return movement;
//    }
//
//    public void setMovement(Movement movement) {
//        this.movement = movement;
//    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
