import java.util.ArrayList;
import java.util.Collections;

public class Player {
    private ArrayList<Card> hand;
    private final int id;
    private static int nextId = 1;
    private boolean playing = false;
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
        Deck.printCards(hand);
    }
    public void sortHand() {
        Collections.sort(hand);
    }
    public void setName(String name) {this.name = name;}
    public String getName() { return name; }
    public void recieveHand(ArrayList<Card> hand) {this.hand = hand;}
    @Deprecated
    public void setPlaying(boolean playing) { this.playing = playing;     }
    @Deprecated
    public boolean isPlaying() { return playing;}
    public int getId() {return id;}
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
            switch (name.substring(1)) {
                case "D" -> adjustedName = "♦";
                case "H" -> adjustedName = "♥";
                case "C" -> adjustedName = "♣";
                case "S" -> adjustedName = "♠";
            }
            adjustedName = name.charAt(0) + adjustedName;
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
}
