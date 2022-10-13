package Game;

import Misc.ClearScreen;
import Rules.TrianteEnaRules;
import Store.Bank;
import Misc.TakeInput;
import Players.Player;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TrianteEna{
    TrianteEnaGame game;

    TrianteEnaRules trianteEnaRules;

    Bank bank;

    TECards teCards;

    int totalPlayers;

    int extraAmountToBanker;

    HashMap<Integer, Player> activePlayers = new HashMap();
    HashMap<Integer, Player> idlePlayers = new HashMap();

    HashMap<Integer, Player> bustedPlayers = new HashMap<>();

    HashMap<Integer, Player> standingPlayers = new HashMap<>();

    HashMap<Integer, Player> cashedOutPlayers = new HashMap<>();


    public TrianteEna(){
        game = new TrianteEnaGame();
        trianteEnaRules = new TrianteEnaRules();
        bank = new Bank();
        game.setType("card");
        game.setMaximumNumberOfPlayers(9);
        teCards = new TECards();
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public void registerPlayers(int numberOfPlayers, int amount){
        HashMap<Integer, Integer> bankData = bank.getAmountStored();
        for(int i=0; i<numberOfPlayers;i++){
            Player player = new Player();
            System.out.println("Please enter Player "+(i+1)+" name");
            System.out.print("Input: ");
            player.setName(TakeInput.getInput());
            player.setState(i+1);
            player.setGameScores(0);
            activePlayers.put(player.getState(), player);
            bankData.put(player.getState(), amount);
        }
        bank.setAmountStored(bankData);
    }

    public void selectBanker(int amount){
        System.out.println("Thank you for registering players, please select the first banker");
        System.out.println("Available players:- ");
        activePlayers.forEach((state, player) -> {
            System.out.println((player.getState()) + ": "+player.getName());
        });
        int playerState = Integer.parseInt(TakeInput.getInput());
        bank.setBanker(activePlayers.get(playerState));
        HashMap<Integer, Integer> amounts = bank.getAmountStored();
        amounts.remove(playerState);
        amounts.put(playerState, amount*3);
        setExtraAmountToBanker(amount*2);
        bank.setAmountStored(amounts);
    }

    public static HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> playerAmounts)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Integer> > list = new LinkedList<Map.Entry<Integer, Integer> >(playerAmounts.entrySet());

        // Sort the list using lambda expression
        Collections.sort(list,(i1,i2) -> i1.getValue().compareTo(i2.getValue()));

        // put data from sorted list to hashmap
        HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public void rotateBanker(){
        System.out.println("Round has come to an end");
        activePlayers.forEach((playerState, player) -> {
        });
    }

    public void displayCards(Player player){
        ClearScreen.ClearConsole();
        System.out.println(player.getName()+", your cards are: ");
        AtomicInteger totalValueOfHand = new AtomicInteger();
        teCards.getPlayerCards().get(player.getState()).forEach(teCard -> {
            teCard.printCard();
            totalValueOfHand.addAndGet(teCard.getValue());
        });
        System.out.println("Total Value of your hand is: "+totalValueOfHand.get());
        System.out.println("-----------------------------------------------------------");
    }

    public void displayCardsToOthers(Player player){
        if(!bank.getBanker().equals(player)) {
            System.out.println("Player " + player.getName() + "'s cards are");
            teCards.getPlayerCards().get(player.getState()).forEach(teCard -> {
                if (teCard.faceUp) {
                    teCard.printCard();
                } else {
                    teCard.printBackOfCard();
                }
            });
        }
    }

    public void giveFirstCardsAndCollectBets(){
        // Now everyone needs to start placing their bets
        activePlayers.forEach((state, player) -> {
//            System.out.println("Hello "+player.getName()+", your cards are: ");
            if(!bank.getBanker().equals(player)) {
                teCards.hit(player, false); //Orientation is false if the card is face down and true if its up
            }else{
                teCards.hit(player, true);
            }
            displayCards(player);

            if(!bank.getBanker().equals(player)) {
                System.out.println("Would you like to bet or fold, please note if you choose to fold then you are out of the round");
                System.out.println("Enter B to place bet or F to fold");
                System.out.print("Input: ");
                char bettingChoice = TakeInput.getInput().toString().charAt(0);
                if (bettingChoice == 'B' || bettingChoice == 'b') {
                    boolean betAmountStoredFlag = false;
                    while (betAmountStoredFlag == false) {
                        System.out.println("Please enter your bet amount, it should be lesser than " + bank.getAmountStored().get(player.getState()));
                        System.out.print("Input: ");
                        int betAmount = Integer.parseInt(TakeInput.getInput());
                        HashMap<Integer, Integer> amountStored = bank.getAmountStored();
                        int newAmount = bank.getAmountStored().get(player.getState())-betAmount;
                        betAmountStoredFlag = bank.setBetAmounts(player.getState(), betAmount);
                        amountStored.remove(player.getState());
                        amountStored.put(player.getState(), newAmount);
                        bank.setAmountStored(amountStored);
                    }
                } else {
                    idlePlayers.put(player.getState(), player);
                }
            }
        });
        idlePlayers.forEach((integer, player) -> {
            activePlayers.remove(integer);
        });
    }

    public void giveTwoMoreCards(){
        activePlayers.forEach((state,player)-> {
            System.out.println(player.getName() + ",You are now going to be given two more cards");
            if(state != bank.getBanker().getState()){
                teCards.hit(player, true);
                teCards.hit(player, true);
                displayCards(player);
                //put a check here to catch busting at this point
                System.out.println("Please see your cards, next screen will appear in 10 seconds");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ClearScreen.ClearConsole();
            }
        });
    }

    public void hitOrStand(){
        for (Map.Entry<Integer, Player> entry : activePlayers.entrySet()) {
            Integer state = entry.getKey();
            Player player = entry.getValue();
            if (state != bank.getBanker().getState()) {
//                System.out.println("Your Cards are: ");
                displayCards(player);
                System.out.println("Would you like to see the cards of other players? Press y to see or n to continue");
                char seeOthersCards = TakeInput.getInput().toString().charAt(0);
                if (seeOthersCards == 'y' || seeOthersCards == 'Y') {
                    activePlayers.forEach((innerState, innerPlayer) -> {
                        if (innerState != state && innerState != bank.getBanker().getState()) {
                            System.out.println("Player " + innerPlayer.getName() + "'s cards: ");
                            displayCardsToOthers(innerPlayer);
                        }
                        System.out.println("---------------------------------------------------------");
                    });
                    System.out.println("Bankers first card was: ");
                    teCards.getPlayerCards().get(bank.getBanker().getState()).get(0).printCard();
                }
                if (teCards.checkExistenceOfAce(teCards.getPlayerCards().get(player.getState()))) {
                    System.out.println("Would you like to take another card? Type h to take hit or s to stand. To change value of your ace between 1 or 11 please enter a");

                } else {
                    System.out.println("Would you like to take another card? Type h to take hit or s to stand.");
                }
                System.out.print("Input: ");
                char hitOrStand = TakeInput.getInput().toString().charAt(0);
                if (hitOrStand == 'a' || hitOrStand == 'A') {
                    System.out.println("Changing value of ace");
                    teCards.changeValueOfAce(player.getState());
                    displayCards(player);
                    System.out.println("Would you like to take another card? Type h to take hit or s to stand.");
                    System.out.print("Input:");
                    hitOrStand = TakeInput.getInput().toString().charAt(0);
                }
                while (hitOrStand == 'h' || hitOrStand == 'H') {
                    teCards.hit(player, true);
                    displayCards(player);
                    boolean playerBust = trianteEnaRules.checkIfBust(player, teCards, bank);
                    if (playerBust) {
                        System.out.println("Sorry, you went bust this round");
//                        activePlayers.remove(player.getState());
                        bustedPlayers.put(player.getState(), player);
                        hitOrStand = 'f';
                        break;
                    }
                    if (teCards.checkExistenceOfAce(teCards.getPlayerCards().get(player.getState()))) {
                        System.out.println("Would you like to take another card? Type h to take hit or s to stand. To change value of your ace between 1 or 11 please enter a");
                        hitOrStand = TakeInput.getInput().toString().charAt(0);
                        if (hitOrStand == 'a' || hitOrStand == 'A') {
                            System.out.println("Changing value of ace");
                            teCards.changeValueOfAce(player.getState());
                            displayCards(player);
                            System.out.println("Would you like to take another card? Type h to take hit or s to stand.");
                            System.out.print("Input:");
                            hitOrStand = TakeInput.getInput().toString().charAt(0);
                        }
                    } else {
                        System.out.println("Would you like to take another card? Type h to take hit or s to stand.");
                        hitOrStand = TakeInput.getInput().toString().charAt(0);
                    }
                }
                if (hitOrStand == 's' || hitOrStand == 'S') {
//                    activePlayers.remove(player.getState());
                    standingPlayers.put(player.getState(), player);
                }
            }
        }
        bustedPlayers.forEach((state, player) -> {
            activePlayers.remove(state);
        });
        standingPlayers.forEach((state, player)-> {
            activePlayers.remove(state);
        });
    }

    public void bankerTakesHit(){
        int bankerValueOfHand = teCards.playerCardsTotalValue(bank.getBanker());
        while (bankerValueOfHand<27){
            teCards.hit(bank.getBanker(), true);
            displayCards(bank.getBanker());
            bankerValueOfHand = teCards.playerCardsTotalValue(bank.getBanker());
        }
        trianteEnaRules.checkIfBust(bank.getBanker(), teCards, bank);
    }

    public void initializeGame(){
        System.out.println("----------Welcome to TrianteEna----------");
        System.out.println("The game is best played with 7-9 players. Please choose the number of players at-least 2");
        System.out.print("Input: ");
        int numberOfPlayers = Integer.parseInt(TakeInput.getInput());
        setTotalPlayers(numberOfPlayers);
        System.out.println("Before we can continue, please enter the maximum amount for each player. Number must be an integer");
        System.out.print("Input: ");
        int amount = Integer.parseInt(TakeInput.getInput());
        registerPlayers(numberOfPlayers, amount);
        selectBanker(amount);
        boolean gameOver = false;
        while (!gameOver){
            activePlayers.forEach((integer, player) -> {
                System.out.println(player.getName());
            });
            boolean roundOver = false;
            while (!roundOver){
                giveFirstCardsAndCollectBets();
                giveTwoMoreCards();
                hitOrStand();
                bankerTakesHit();
                roundOver = trianteEnaRules.checkRoundState(standingPlayers, teCards, bank);
            }
            standingPlayers.forEach((integer, player) -> {
                System.out.println("Amount Taken by "+player.getName()+": "+bank.getAmountStored().get(integer));
            });
            System.out.println("Last banker amount: "+bank.getAmountStored().get(bank.getBanker().getState()));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            standingPlayers.forEach((integer, player) -> {
                activePlayers.put(integer, player);
            });
            standingPlayers.clear();
            idlePlayers.forEach((integer, player) -> {
                activePlayers.put(integer, player);
            });
            idlePlayers.clear();
            bustedPlayers.forEach((integer, player) -> {
                activePlayers.put(integer, player);
            });
            bustedPlayers.clear();
            teCards.generateCards();
            gameOver = trianteEnaRules.checkState(teCards, bank, cashedOutPlayers, getTotalPlayers());
        }
    }

    public int getExtraAmountToBanker() {
        return extraAmountToBanker;
    }

    public void setExtraAmountToBanker(int extraAmountToBanker) {
        this.extraAmountToBanker = extraAmountToBanker;
    }
}
