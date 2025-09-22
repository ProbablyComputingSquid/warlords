/**
 * Player.java - logic for Player class
 */

import java.util.ArrayList;
import java.util.Collections;

public class Player {
    public enum PlayerStatus {
        UNASSIGNED("unassigned, shouldnt see this"),
        PLAYING("Proletariat"),
        SCUMBAG("\uD83D\uDDD1\uFE0F Scumbag"),
        WARLORD("\uD83D\uDC51 Warlord");

        private final String descriptor;
        PlayerStatus(String s) {
            descriptor = s;
        }
        @Override
        public String toString() {
            return descriptor;
        }

    }
    private PlayerStatus playerStatus = PlayerStatus.PLAYING;
    private ArrayList<Card> hand;
    private final int id;
    private static int nextId = 1;
    private boolean playing = true;
    private String name;
    public String viewHand() {
        sortHand();
        String output = "";
        for (Card card : hand) {
            output += card.toString() + ", ";
        }
        return output;
    }
    public void printFancyHand() {
        sortHand();
        Deck.printCards(hand, Main.Color.WHITE_BACKGROUND);
    }
    public String getFancyHand() {
        sortHand();
        return Deck.renderCards(hand);
    }
    public boolean handContainsCard(Card card) {
        for (Card _card : hand) {
            if (card.compareTo(_card) == 0 ) return true;
        }
        return false;
    }
    public void sortHand() {
        Collections.sort(hand);
    }
    public void setName(String name) {this.name = name;}
    public String getName() { return name; }
    public void recieveHand(ArrayList<Card> hand) {this.hand = hand;}
    public void setPlaying(boolean playing) { this.playing = playing;     }
    public boolean isPlaying() { return playing;}
    public int getId() {return id;}
    public PlayerStatus getPlayerStatus() {return playerStatus;}
    public void setPlayerStatus(PlayerStatus p) {playerStatus = p;}
    public int handSize() {return hand.size();}
    public Player() {
        id = nextId++;
    }
    public Player(ArrayList<Card> dealtHand) {
        hand = dealtHand;
        id = nextId++;
    }
    public Card getCardFromHandByIndex(int index) {
        Card card = hand.get(index);
        hand.remove(index);
        return card;
    }
    public ArrayList<Card> getCardsFromHandByIndices(ArrayList<Integer> indices) {
        ArrayList<Card> cards = new ArrayList<>();
        int offset = 0;
        for (int i : indices) {
            cards.add(hand.get(i - offset));
            hand.remove(i - offset);
            offset++;
        }
        return cards;
    }
    public Card getCardFromHandByName(String name) {
        for (Card card : hand) {
            String adjustedName = "";
            switch (name.substring(name.length()-1)) { // fix for tens because those are two characters
                case "D" -> adjustedName = "♦";
                case "H" -> adjustedName = "♥";
                case "C" -> adjustedName = "♣";
                case "S" -> adjustedName = "♠";
            }
            adjustedName = name.substring(0,name.length()-1) + adjustedName;
            if (name.equals("JOKER") && card.getRank() == Card.Rank.JOKER) {
                return card;
            }
            if (adjustedName.equals(card.toString())) {
                return card;
            }
        }
        return null; // not sure what to do here except return null cause no card found
    }
    public ArrayList<Card> getCardsFromHandByNames(String[] names) {
        ArrayList<Card> cardsToReturn = new ArrayList<>();
        for (String name : names) {
            cardsToReturn.add(getCardFromHandByName(name));
            System.out.println(name);
        }
        return cardsToReturn;
    }
    public void removeCard (Card card) {
        hand.remove(card);
    }
    public void receiveCard (Card card) {
        hand.add(card);
        sortHand();
    }
    public Card getHighestCard() {
        Card card = hand.getLast();
        hand.remove(card);
        return card;
    }
    @Override
    public String toString() {
        return String.format("Player '%s' (id #%d) %s", name, id, isPlaying() ? "is PLAYING" : "has PASSED");
    }
}
