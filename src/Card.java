import javax.swing.*;
import java.util.*;

public class Card implements Comparable<Card> {
    public enum Rank  {
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("10"),
        JACK("J"),
        QUEEN("Q"),
        KING("K"),
        ACE("A"),
        TWO("2"),
        JOKER("JOKER");
        public final String label;
        private Rank(String label) {
            this.label = label;
        }
    }


    public enum Suit {
        HEARTS("H"),
        DIAMONDS("D"),
        CLUBS("C"),
        SPADES("S"),
        JOKER("");
        public final String label;
        private Suit(String label) {
            this.label = label;
        }
    }

    private final Rank rank;
    private final Suit suit;
    private final int value;
    public Card (int rank, int suit) {
        this.rank = Rank.values()[rank];
        this.suit = Suit.values()[suit];
        value = this.rank.ordinal() + 3;
    }
    public Rank getRank() {
        return rank;
    }
    public Suit getSuit() {
        return suit;
    }
    public int getValue() {
        return value;
    }
    @Override
    public String toString() {
        return rank.label + suit.label;
    }

    /**
     *
     * @param card
     * @returns rank * 10 + suit
     */
    @Override
    public int compareTo(Card card) {
        return (this.rank.ordinal() - card.rank.ordinal()) * 10 + (this.suit.ordinal() - card.suit.ordinal());
    }
}
