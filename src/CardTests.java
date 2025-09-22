import java.util.ArrayList;

public class CardTests {
    public static void main(String[] args) {
        Card card = new Card(13,4);
        System.out.println(card);
        Deck deck = new Deck();
        for (int i = 0; i < 54; i++) {
            //System.out.println(deck.drawCard().getFancyCard());
        }
        Card joker = new Card(13,4);
        System.out.print(joker.getFancyCard(Main.Color.WHITE_BACKGROUND));
        Deck deck2 = new Deck();
        String[] cards = new String[3];
        for (int i = 0; i < 3; i++) {
            cards[i] = deck2.drawCard().getFancyCard(Main.Color.WHITE_BACKGROUND);
        }
        String[][] splitCards = new String[cards.length][3];
        for (int i = 0; i < 3; i++) {
            splitCards[i] = cards[i].split("\n");
        }
        for (int row = 0; row < splitCards.length; row++) {
            for (int col = 0; col < splitCards[0].length; col++) {
                System.out.print(splitCards[col][row]);
            }
            System.out.println();
        }

    }
}
