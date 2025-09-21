/**
 * Card.java - Card logic
 */


public class Card implements Comparable<Card> {
    public enum Rank  {
        THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),  TEN("10"),
        JACK("J"), QUEEN("Q"), KING("K"), ACE("A"), TWO("2"), JOKER("$");
        public final String label;
        private Rank(String label) {
            this.label = label;
        }
    }


    public enum Suit {
        HEARTS("♥"), DIAMONDS("♦"), CLUBS("♣"), SPADES("♠"), JOKER("\uD83C\uDCBF");
        public final String label;
        private Suit(String label) {
            this.label = label;
        }
    }

    private final Rank rank;
    private final Suit suit;
    private final int value;
    public Card (int rank, int suit) {
        this.rank = Rank.values()[rank-3];
        this.suit = Suit.values()[suit];
        value = this.rank.ordinal() + 3;
    }
    public Card (Rank _rank, Suit _suit) {
        this.rank = _rank;
        this.suit = _suit;
        value = this.rank.ordinal() + 3;
    }
    public Rank getRank() {return rank;}
    public Suit getSuit() {return suit;}
    public int getValue() {return value;}
    public String getFancyCard() {
        StringBuilder card = new StringBuilder();
        Main.Color card_color = Main.Color.BLACK;
        if (getSuit().equals(Suit.HEARTS) || getSuit().equals(Suit.DIAMONDS)) {
            card_color = Main.Color.RED;
        }
        Main.Color wb = Main.Color.WHITE_BACKGROUND;
        String formatted_middle = String.format(wb + "┓\n┃ " + card_color + "%S" + Main.Color.RESET + wb + " ┃\n", suit.label);
        if (getRank().label.equals("10")) {
            card.append(String.format(wb + "┏" + card_color + "%s" +  Main.Color.RESET + wb + "━", rank.label));
            card.append(formatted_middle);
            card.append(String.format(wb + "┗━" + card_color + "%s"+ Main.Color.RESET + wb + "┛\n", rank.label));
        } else {
            card.append(String.format(wb +"┏" + card_color + "%s" + Main.Color.RESET + wb + "━━", rank.label));
            card.append(formatted_middle);
            card.append(String.format(wb + "┗━━" + card_color + "%s" + Main.Color.RESET + wb + "┛\n", rank.label));
        }
        return card.toString();
    }

    @Override
    public String toString() {
        return rank.label + suit.label;
    }

    @Override
    public int compareTo(Card card) {
        return (this.rank.ordinal() - card.rank.ordinal()) * 10 + (this.suit.ordinal() - card.suit.ordinal());
    }
}
