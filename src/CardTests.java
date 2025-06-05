public class CardTests {
    public static void main(String[] args) {
        Card card = new Card(13,4);
        System.out.println(card);
        Deck deck = new Deck();
        for (int i = 0; i < 54; i++) {
            deck.drawCard().printCard();
        }
    }
}
