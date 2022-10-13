package Store;

import Players.Player;

import java.util.HashMap;

public class Bank {
    Player banker; // Game should have the banker
    HashMap<Integer, Integer> amountStored = new HashMap(); //Hashmap to store state integer of player and the amount they have at the moment

    HashMap<Integer, Integer> betAmounts = new HashMap<>();

    public Player getBanker() {
        return banker;
    }

    public void setBanker(Player banker) {
        this.banker = banker;
    }

    public HashMap<Integer, Integer> getAmountStored() {
        return amountStored;
    }

    public void setAmountStored(HashMap<Integer, Integer> amountStored) {
        this.amountStored = amountStored;
    }

    public HashMap<Integer, Integer> getBetAmounts() {
        return betAmounts;
    }

    public void setBetAmounts(HashMap<Integer, Integer> betAmounts) {
        this.betAmounts = betAmounts;
    }

    public boolean setBetAmounts(int playerState, int betAmount) {
        System.out.println(betAmount);
        System.out.println(getAmountStored().get(playerState));
        if(betAmount>getAmountStored().get(playerState)){
            System.out.println("Please enter a correct amount lesser than the money you have");
            return false;
        }else{
            betAmounts.put(playerState, betAmount);
        }
        return true;
    }
}
