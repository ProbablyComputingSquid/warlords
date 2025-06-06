import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    public Deck() {
        // add all the normal cards
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 3; rank < 16; rank++) {
                cards.add(new Card(rank,suit));
            }
        }
        // the two jokers
        cards.add(new Card(13,4));
        cards.add(new Card(13,4));
        shuffleDeck();
    }
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }
    public Card drawCard() {
        Card card = cards.getFirst();
        cards.removeFirst();
        return card;
    }
    public ArrayList<Card> dealHand(int players) {
        int cards = 54 / players;
        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < cards; i++) {
            hand.add(drawCard());
        }
        return hand;
    }
    public static void printCards(ArrayList<Card> cards) {
        String[] cardArray = new String[cards.size()];


            for (int i = 0; i < cards.size(); i++) {
                cardArray[i] = cards.get(i).getFancyCard();
            }
            String[][] splitCards = new String[cardArray.length][3];
            for (int i = 0; i < cards.size(); i++) {
                splitCards[i] = cardArray[i].split("\n");
            }
            for (int row = 0; row < splitCards[0].length; row++) {
                for (int col = 0; col < splitCards.length; col++) {
                    System.out.print(splitCards[col][row]);
                }
                System.out.println();
            }
    }

}
