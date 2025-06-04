import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    public Deck() {
        // add all the normal cards
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 13; rank++) {
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
}
