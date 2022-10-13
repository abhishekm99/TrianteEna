package Game;

import Misc.TakeInput;
import Players.Player;
import Store.Cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TECards implements Cards { //Name it Deck Class
    ArrayList<String> cardTypes = new ArrayList<>();
    ArrayList<String> faceCardTypes = new ArrayList<>();

    ArrayList<TECard> cardsDeck = new ArrayList<>();

    ArrayList<TECard> usedCards = new ArrayList<>();

    HashMap<Integer, ArrayList<TECard>> playerCards = new HashMap<>();

    public TECards() {
        cardTypes.add("Diamonds");
        cardTypes.add("Clubs");
        cardTypes.add("Spades");
        cardTypes.add("Hearts");
        faceCardTypes.add("King");
        faceCardTypes.add("Queen");
        faceCardTypes.add("Jack");

        generateCards();
    }

    @Override
    public void generateCards() {
        ArrayList<TECard> pieces = new ArrayList<>();
        int decks = 2;
        for(int deck=0; deck<decks; deck++) {
            for (int i = 2; i <= 10; i++) {
                for (String type : cardTypes) {
                    TECard piece = new TECard();
                    piece.setSymbol(Integer.toString(i));
                    piece.setValue(i);
                    piece.setType(type);
                    pieces.add(piece);
                }
            }

            faceCardTypes.forEach(faceCard -> {
                cardTypes.forEach(type -> {
                    TECard piece = new TECard();
                    piece.setSymbol(String.valueOf(faceCard.charAt(0)));
                    piece.setValue(10);
                    piece.setType(type);
                    pieces.add(piece);
                });
            });

            cardTypes.forEach(type -> {
                TECard piece = new TECard();
                piece.setSymbol(String.valueOf('A'));
                piece.setValue(11);
                piece.setType(type);
                pieces.add(piece);
            });
        }
        setCardsDeck(pieces);
        setPlayerCards(new HashMap<>());
        setUsedCards(new ArrayList<TECard>());
    }

    public ArrayList<TECard> getCardsDeck() {
        return cardsDeck;
    }

    public void setCardsDeck(ArrayList<TECard> cards) {
        this.cardsDeck = cards;
    }

    public boolean checkExistenceOfAce(ArrayList<TECard> playerCards){
        boolean hasAce = false;
        for (TECard card : playerCards) {
            if (card.getSymbol().equals("A")) {
                hasAce = true;
                break;
            }
            else{
                hasAce = false;
            }
        }
        return hasAce;
    }

    public boolean changeValueOfAce(int playerState){
//        int countAces = 0;
        boolean valueChanged = false;
        ArrayList<TECard> playerCards = this.getPlayerCards().get(playerState);
        for(int i=0;i<playerCards.size(); i++){
            if(playerCards.get(i).getSymbol() == "A" && playerCards.get(i).getValue() == 1){
                playerCards.get(i).setValue(11);
                valueChanged = true;
                break;
            }
            if(playerCards.get(i).getSymbol() == "A" && playerCards.get(i).getValue() == 11){
                playerCards.get(i).setValue(1);
                valueChanged = true;
                break;
            }
        }
//        for(int i=0; i<playerCards.size(); i++){
//            if(playerCards.get(i).getSymbol() == "A" && playerCards.get(i).getValue() == 1){
//                playerCards.get(i).setValue(11);
//                valueChanged = true;
//                break;
//            }
//            else if(playerCards.get(i).getSymbol() == "A" && playerCards.get(i).getValue() == 11){
//                countAces++;
//            }
//        }
//        if(countAces>=1){
//            for(int i=0; i<playerCards.size(); i++){
//                if(playerCards.get(i).getSymbol() == "A" && playerCards.get(i).getValue() == 11){
//                    playerCards.get(i).setValue(1);
//                    valueChanged = true;
//                    break;
//                }
//            }
//        }
//        if(valueChanged == false){
//            for(int i=0; i<playerCards.size(); i++){
//                if(playerCards.get(i).getSymbol()=="A"){
//                    playerCards.get(i).setValue(1);
//                    valueChanged = true;
//                    break;
//                }
//            }
//        }
        return valueChanged;
    }

//    public boolean changeValueOfAce(ArrayList<TECard> playerCards, int valueOfAce, String typeOfAce){
//        boolean valueChanged = false;
//        if(valueOfAce == 1 || valueOfAce == 11){
//            TECard ace = new TECard();
//            ace.setValue(valueOfAce);
//            ace.setSymbol("A");
//            ace.setType(typeOfAce);
//            playerCards.add(ace);
//            valueChanged = true;
//        }
//        return valueChanged;
//    }

    public void hit(Player player, boolean cardOrientation){
        System.out.println("Current size of cards deck: "+cardsDeck.size());
        int randomIndex = ThreadLocalRandom.current().nextInt(0, cardsDeck.size());
        TECard cardHit = cardsDeck.get(randomIndex);
        cardHit.setFaceUp(cardOrientation);
        cardsDeck.remove(randomIndex);
        usedCards.add(cardHit);
        if(playerCards.containsKey(player.getState())){
            ArrayList<TECard> tempPlayerCards = playerCards.get(player.getState());
            tempPlayerCards.add(cardHit);
            playerCards.remove(player);
            playerCards.put(player.getState(), tempPlayerCards);
        }else{
            ArrayList<TECard> tempPlayerCards = new ArrayList<>();
            tempPlayerCards.add(cardHit);
            playerCards.put(player.getState(), tempPlayerCards);
        }
        System.out.println("Current size of hand: "+playerCards.get(player.getState()).size());
    }

    public int playerCardsTotalValue(Player player){
        ArrayList<TECard> allPlayerCards = playerCards.get(player.getState());
        AtomicInteger totalValue = new AtomicInteger();
        allPlayerCards.forEach(card -> {
            totalValue.addAndGet(card.getValue());
        });
        return totalValue.get();
    }

    public ArrayList<String> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(ArrayList<String> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public ArrayList<String> getFaceCardTypes() {
        return faceCardTypes;
    }

    public void setFaceCardTypes(ArrayList<String> faceCardTypes) {
        this.faceCardTypes = faceCardTypes;
    }

    public ArrayList<TECard> getUsedCards() {
        return usedCards;
    }

    public void setUsedCards(ArrayList<TECard> usedCards) {
        this.usedCards = usedCards;
    }

    public HashMap<Integer, ArrayList<TECard>> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(HashMap<Integer, ArrayList<TECard>> playerCards) {
        this.playerCards = playerCards;
    }
}
