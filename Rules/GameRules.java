package Rules;

import Game.TECards;
import Players.Player;
import Store.Bank;

import java.util.HashMap;

public interface GameRules { //Interface to define skeleton of Rules
//    boolean checkState(TicTacToeBoard board);
//
    boolean checkState(TECards teCards, Bank bank, HashMap<Integer, Player> activePlayers, int totalNumberOfPlayers);

    boolean checkRoundState(HashMap<Integer, Player> standingPlayers, TECards teCards, Bank bank);
}
