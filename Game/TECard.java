package Game;

import Players.Piece;

public class TECard extends Piece{

    boolean faceUp;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";

    public char getCardSymbol(String cardType){
        char symbol = '0';
        switch(cardType){
            case "Spades":
            symbol = 'S';
            break;
            case "Diamonds":
            symbol = 'D';
            break;
            case "Clubs":
            symbol = 'C';
            break;
            case "Hearts":
            symbol = 'H';
            break;
        }
        return symbol;
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public String getSymbol() {
        return super.getSymbol();
    }

    @Override
    public void setSymbol(String symbol) {
        super.setSymbol(symbol);
    }

    @Override
    public int getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(int value) {
        super.setValue(value);
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    public void printCard(){
        if(super.getType() == "Clubs" || super.getType() == "Spades"){
            System.out.println("----------");
            System.out.println("|"+ ANSI_BLACK + getCardSymbol(super.getType()) + ANSI_RESET +"       |");
//            if(String.valueOf(super.getValue()).length() == 1){
//                System.out.println("|   "+super.getValue()+"    |");
//            }else{
//                System.out.println("|   "+super.getValue()+"   |");
//            }
            System.out.println("|   "+super.getSymbol()+"    |");
            System.out.println("|      "+ANSI_BLACK + getCardSymbol(super.getType()) + ANSI_RESET+" |");
            System.out.println("----------");
            System.out.println("Card Value: "+super.getValue());
        }else{
            System.out.println("----------");
            System.out.println("|"+ ANSI_RED + getCardSymbol(super.getType()) + ANSI_RESET +"       |");
//            if(String.valueOf(super.getValue()).length() == 1){
//                System.out.println("|   "+super.getValue()+"    |");
//            }else{
//                System.out.println("|   "+super.getValue()+"   |");
//            }
            System.out.println("|   "+super.getSymbol()+"    |");
            System.out.println("|      "+ANSI_RED + getCardSymbol(super.getType()) + ANSI_RESET+" |");
            System.out.println("----------");
            System.out.println("Card Value: "+super.getValue());
        }

    }

    public void printBackOfCard(){
        System.out.println("-----------");
        System.out.println("|---------|");
        System.out.println("|---------|");
        System.out.println("|---------|");
        System.out.println("-----------");
    }
}
