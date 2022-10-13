package Rules;

import Game.TECard;
import Game.TECards;
import Players.Player;
import Store.Bank;

import java.util.HashMap;
import java.util.Map;

public class TrianteEnaRules implements GameRules{
    @Override
    public boolean checkState(TECards teCards, Bank bank, HashMap<Integer, Player> standingPlayers, int totalNumberOfPlayers) {
        if(standingPlayers.size()==totalNumberOfPlayers-1){
            System.out.println("Game is over, all players cashed out at the following");
            standingPlayers.forEach((integer, player) -> {
                System.out.println("Amount Taken by "+player.getName()+": "+bank.getAmountStored().get(integer));
            });
            System.out.println("Last banker amount: "+bank.getAmountStored().get(bank.getBanker().getState()));
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRoundState(HashMap<Integer, Player> standingPlayers, TECards teCards, Bank bank) {
        standingPlayers.forEach((playerState, player) -> {
            if(teCards.playerCardsTotalValue(player)>teCards.playerCardsTotalValue(bank.getBanker())){
                System.out.println(player.getName() + " has a hand value higher than the banker, they win the round");
                int betMadeByPlayer = bank.getBetAmounts().get(playerState);
                int playerStash = bank.getAmountStored().get(playerState);
                playerStash += 2*betMadeByPlayer;
                bank.getAmountStored().remove(playerState);
                bank.getAmountStored().put(playerState, playerStash);
                int bankerStash = bank.getAmountStored().get(bank.getBanker().getState());
                bankerStash-=betMadeByPlayer;
                bank.getAmountStored().remove(bank.getBanker().getState());
                bank.getAmountStored().put(bank.getBanker().getState(), bankerStash);
                bank.getBetAmounts().remove(playerState);
            }
        });
        return true;
    }

    public boolean checkIfBust(Player player, TECards teCards, Bank bank){
        boolean bustState = false;
        int totalHand = 0;
        for (TECard card : teCards.getPlayerCards().get(player.getState())) {
            totalHand += card.getValue();
        }
        if(totalHand>31){
            System.out.println("You went bust!");
            if(player.getState()!=bank.getBanker().getState()){
                bank.getBetAmounts().remove(player.getState());
            }else{
                System.out.println("Dealer Bust!");
                HashMap<Integer, Integer> playerModifiedAmounts = bank.getAmountStored();
                for (Map.Entry<Integer, Integer> entry : playerModifiedAmounts.entrySet()) {
                    Integer playerState = entry.getKey();
                    Integer playerAmount = entry.getValue();
                    int bankerAmount = bank.getAmountStored().get(bank.getBanker().getState());
                    playerAmount += bank.getBetAmounts().get(playerState);
                    bankerAmount -= bank.getBetAmounts().get(playerState);
                    bank.getAmountStored().remove(bank.getBanker().getState());
                    bank.getAmountStored().put(bank.getBanker().getState(), bankerAmount);
                    playerModifiedAmounts.remove(playerState);
                    playerModifiedAmounts.put(playerState, playerAmount);
                    bank.getBetAmounts().remove(playerState);
                }
                bank.setAmountStored(playerModifiedAmounts);
            }
            bustState = true;
        }
//        else if(totalHand == 31){
//            System.out.println("You win!");
//        }
        bank.getAmountStored().forEach((state,amount)-> {
            System.out.println(amount);
        });
        return bustState;
    }
}
