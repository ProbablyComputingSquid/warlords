import java.util.ArrayList;

public class Round {
    public enum ROUND_STATE {
        NEW(),
        IN_PROGRESS(),
        WON(),
    }
    public enum PLAY_RESULT {
        FAILED_CARD_TOO_LOW(),
        FAILED_NOT_ENOUGH_CARDS(),
        FAILED_NOT_ALL_SAME_RANK(),
        SUCCESS(),
        JOKER_SUCCESS(),
        ROUND_OVER(),
        FAILED_TOO_MANY_CARDS(),
    }
    public enum HAND_TYPE {
        NONE_PLAYED,
        SINGLE,
        PAIR,
        THREE_OF_A_KIND,
        FOUR_OF_A_KIND,
        JOKER,
    }

    private ArrayList<Card> playedSetCards = new ArrayList<>();
    private ArrayList<Card> discardedCards = new ArrayList<>();
    private int lastPlayerToPlayCardId;
    private HAND_TYPE handType = HAND_TYPE.NONE_PLAYED;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> playersInPlay = new ArrayList<>();
    private ROUND_STATE roundState;
    private ROUND_STATE setState;
    private Deck deck = new Deck();
    private Player winner;
    public Round(ArrayList<Player> players) {
        this.players = players;
        for (Player player : players) {
            playersInPlay.add(player);
            player.recieveHand(deck.dealHand(players.size()));
        }
        roundState = ROUND_STATE.NEW;
    }
    public ROUND_STATE getRoundState() {
        return roundState;
    }
    public ROUND_STATE getSetState() {
        return setState;
    }
    public HAND_TYPE getHandType() {
        return handType;
    }
    public Player getWinner() {
        return winner;
    }
    private void newSet() {
        playersInPlay = new ArrayList<>();
        playersInPlay.addAll(players); // reset players in play
        discardedCards.addAll(playedSetCards); // discard the played set cards
        playedSetCards = new ArrayList<>(); // purge played set cards
        handType = HAND_TYPE.NONE_PLAYED;
    }
    public String getPlayedCards() {
        String cards = "";
        for (Card card : playedSetCards) {
            cards += card + ", ";
        }
        return cards;
    }
    /**
     *
     * @param player player object that is playing the card
     * @param cards the cards the player is playing
     * @return PLAY_RESULT enum
     */
    public PLAY_RESULT submitCards(Player player, ArrayList<Card> cards) throws Exception {
        if (cards == null || cards.isEmpty()) { // null card means you don't play anything, pass turn
            passTurn(player);
            return PLAY_RESULT.FAILED_NOT_ENOUGH_CARDS;
        }
        if (roundState == ROUND_STATE.WON) {
            return PLAY_RESULT.ROUND_OVER;
        }
        Card card = cards.getFirst();
        if (card.getRank() == Card.Rank.JOKER) { // joker trumps everything, end the round
            setState = ROUND_STATE.WON;
            newSet();
            return PLAY_RESULT.JOKER_SUCCESS;
        }
        for (Card _card : cards) {
            if (_card.getRank() != card.getRank()) {
                return PLAY_RESULT.FAILED_NOT_ALL_SAME_RANK;
            }
        }
        if (handType == HAND_TYPE.NONE_PLAYED) { // no cards have been played yet, establish the baseline
            switch (cards.size()) {
                case 1 -> handType = HAND_TYPE.SINGLE;
                case 2 -> handType = HAND_TYPE.PAIR;
                case 3 -> handType = HAND_TYPE.THREE_OF_A_KIND;
                case 4 -> handType = HAND_TYPE.FOUR_OF_A_KIND;
            }
        }
        if (cards.size() > handType.ordinal()) {
            return PLAY_RESULT.FAILED_TOO_MANY_CARDS;
        } else if (cards.size() < handType.ordinal()) {
            return PLAY_RESULT.FAILED_NOT_ENOUGH_CARDS;
        }

        if (playedSetCards.isEmpty() || card.getValue() > playedSetCards.getLast().getValue()) { // successful play!
            playedSetCards.add(card);
            if (player.handSize() == 0) {
                roundState = ROUND_STATE.WON;
                winner = player;
            }
            return PLAY_RESULT.SUCCESS;
        }
        return PLAY_RESULT.FAILED_CARD_TOO_LOW;
    }
    // the player doesn't play anything
    public void passTurn(Player player) {
        playersInPlay.remove(player);
        if (playersInPlay.size() == 1) { // nobody else can play anymore
            newSet();
        }
    }
}
