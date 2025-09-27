/**
 * Round.java - round logic
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Round {
    public enum ROUND_STATE {
        NEW(),
        IN_PROGRESS(),
        WON(),
        NEW_SET(),
        FINISHED(),
        ABORTED(),
    }
    public enum PLAY_RESULT {
        FAILED_CARD_TOO_LOW(),
        FAILED_NOT_ENOUGH_CARDS(),
        FAILED_NOT_ALL_SAME_RANK(),
        SUCCESS(),
        JOKER_SUCCESS(),
        ROUND_OVER(),
        FAILED_TOO_MANY_CARDS(),
        HAS_NOT_PLAYED(),
        TURN_PASSED(),
        FAILED_NOT_A_VALID_CARD(),
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
    // this keeps track of the players playing in the set
    private ArrayList<Player> playersInPlay = new ArrayList<>();
    // this keeps track of the players who are not yet out
    private ArrayList<Player> unfinishedPlayers = new ArrayList<>();
    private ROUND_STATE roundState;
    private ROUND_STATE setState;
    private Deck deck = new Deck();
    public Player warlord;
    public Player scumbag;
    public Player winner; // winner of the set

    public Round(ArrayList<Player> players) {
        // bool scumbag used to see if there is a scumbag and warlord
        this.players = players;
        int i = 0;
        int starting_player_id = 0;

        for (Player player : players) {
            playersInPlay.add(player);
            unfinishedPlayers.add(player);
            player.recieveHand(deck.dealHand(players.size()));
            if (player.handContainsCard(new Card(Card.Rank.THREE, Card.Suit.CLUBS))) {
                starting_player_id = i;
                //System.out.println("Detected three of clubs!");
            }
            i++;
        }
        Collections.rotate(players, -starting_player_id);

        roundState = ROUND_STATE.NEW;
    }
    private void restartRound() {
        playersInPlay = new ArrayList<>();
        playersInPlay.addAll(players); // reset players in play
        unfinishedPlayers.addAll(players);
        discardedCards = new ArrayList<>(); // discard the played set cards
        playedSetCards = new ArrayList<>(); // purge played set cards
        handType = HAND_TYPE.NONE_PLAYED;
        deck = new Deck();
        int starting_index = 0;
        int i = 0;
        Card scumbags_card = new Card(3,1); // default placeholders
        Card warlords_card = new Card(3,1);
        for (Player player : players) {
            player.recieveHand(deck.dealHand(players.size()));
            player.setPlaying(true);
            if (player.getId() == scumbag.getId()) { // if player is the scumbag, enact tribute
                starting_index = i;
                scumbags_card = player.getHighestCard();
            } else if (player.getId() == warlord.getId()) {
                System.out.print("Choose a card to give to the scumbag: ");
                player.printFancyHand();
                Scanner scanner = new Scanner(System.in);
                System.out.print("enter card name: ");
                String s = scanner.nextLine();
                // trust the user that its correct ion wanna implement new card sorting shi
                warlords_card = player.getCardFromHandByName(s);
            }
            i++;
        }
        scumbag.receiveCard(warlords_card);
        warlord.receiveCard(scumbags_card);


        Collections.rotate(players, -starting_index);
        roundState = ROUND_STATE.IN_PROGRESS;
    }
    private void newSet() {
        playersInPlay = new ArrayList<>();
        playersInPlay.addAll(players); // reset players in play
        discardedCards.addAll(playedSetCards); // discard the played set cards
        playedSetCards = new ArrayList<>(); // purge played set cards
        handType = HAND_TYPE.NONE_PLAYED;
        roundState = ROUND_STATE.NEW_SET;
        int winnerIndex = 0;

        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlaying(true);
            if (players.get(i).equals(winner)) {
                winnerIndex = i;
                //System.out.println("Found winner at index " + i);
            }
        }
        //System.out.println(players);
        Collections.rotate(players, players.size() - winnerIndex);
        //System.out.println(players);
    }
    public ROUND_STATE getRoundState() {
        return roundState;
    }
    public ArrayList<Player> getPlayers() {return players;}
    public ArrayList<Player> getPlayersInPlay() {return playersInPlay;}
    @Deprecated
    public ROUND_STATE getSetState() {
        return setState;
    }
    public void setRoundStateInProgress() {
        roundState = ROUND_STATE.IN_PROGRESS;
    }
    public void abort() {
        roundState = ROUND_STATE.ABORTED;
    }
    public HAND_TYPE getHandType() {
        return handType;
    }
    public String getCurrentRank() {
        if (handType == HAND_TYPE.NONE_PLAYED) {
            return "nothing!";
        } else {
            return "" + playedSetCards.getLast().getRank(); // force to string lol
        }
    }
    public Player getWarlord() {
        return warlord;
    }
    public boolean isPlayerInPlay(Player player) {
        for (Player p : playersInPlay) {
            if (p.equals(player)) return true;
        }
        return false;
    }

    public String getPlayedCards() {
        String cards = "";
        for (Card card : playedSetCards) {
            cards += card + ", ";
        }
        return cards;
    }
    public void displayFancyPlayedCards() {
        if (playedSetCards.isEmpty()) {
            System.out.println("Nothing has been played yet - play any valid hand");
        } else { // okay i know this else is redundant but it makes sense for readibility
            Deck.printCards(playedSetCards, Main.Color.FELT_GREEN_BG);
        }

    }
    /**
     *
     * @param player player object that is playing the card
     * @param cards the cards the player is playing
     * @return PLAY_RESULT enum
     */
    public PLAY_RESULT submitCards(Player player, ArrayList<Card> cards)  {
        if (cards == null || cards.isEmpty()) { // null cards list means you don't play anything, pass turn
            passTurn(player);
            return PLAY_RESULT.FAILED_NOT_ENOUGH_CARDS;
        }
        // cant play anything if the rounds over
        if (roundState == ROUND_STATE.WON) {
            return PLAY_RESULT.ROUND_OVER;
        }
        Card firstCard = cards.getFirst();
        for (Card _card : cards) {
            if (_card == null) {
                return PLAY_RESULT.FAILED_NOT_A_VALID_CARD;
            }
            if (_card.getRank() != firstCard.getRank()) {
                return PLAY_RESULT.FAILED_NOT_ALL_SAME_RANK;
            }
        }
        if (firstCard.getRank() == Card.Rank.JOKER) { // joker trumps everything, end the round
            if (cards.size() > 1) {
                System.out.println("You can only play one joker!");
                return PLAY_RESULT.FAILED_TOO_MANY_CARDS;
            }
            Deck.printCards(cards, Main.Color.FELT_GREEN_BG);
            player.removeCard(firstCard);
            if (player.handSize() == 0) {
                handleDone(player);
            }
            newSet();
            return PLAY_RESULT.JOKER_SUCCESS;
        }
        // loop through the cards and check if they´ re all the same suit cause thats how pairs work
        // no cards have been played yet, establish the baseline of single/pair/etc.
        if (handType == HAND_TYPE.NONE_PLAYED) {
            switch (cards.size()) {
                case 1 -> handType = HAND_TYPE.SINGLE;
                case 2 -> handType = HAND_TYPE.PAIR;
                case 3 -> handType = HAND_TYPE.THREE_OF_A_KIND;
                case 4 -> handType = HAND_TYPE.FOUR_OF_A_KIND;
            }
        }
        // why would bro try to play too many cards :sob:
        if (cards.size() > handType.ordinal()) {
            return PLAY_RESULT.FAILED_TOO_MANY_CARDS;
        } else if (cards.size() < handType.ordinal()) {
            return PLAY_RESULT.FAILED_NOT_ENOUGH_CARDS;
        }

        // successful play!
        if (playedSetCards.isEmpty() || firstCard.getValue() > playedSetCards.getLast().getValue()) {
            for (Card playedCard : cards) {
                playedSetCards.add(playedCard);
                player.removeCard(playedCard);
            }
            Deck.printCards(cards, Main.Color.FELT_GREEN_BG); // print the cards played to show
            if (player.handSize() == 0) {
               handleDone(player);
            }
            return PLAY_RESULT.SUCCESS;
        }
        return PLAY_RESULT.FAILED_CARD_TOO_LOW;
    }
    public void handleDone(Player player) {
        if (warlord == null) {
            roundState = ROUND_STATE.WON;
            System.out.printf("Player %s has won the round! they are now the ", player.getName());
            Main.printColor("WARLORD", Main.Color.GOLD);
            warlord = player;
            player.setPlayerStatus(Player.PlayerStatus.WARLORD);
        }
        unfinishedPlayers.remove(player);
        if (unfinishedPlayers.size() == 1) { // set last player to scumbag
            Player scumbag = unfinishedPlayers.getFirst();
            scumbag.setPlayerStatus(Player.PlayerStatus.SCUMBAG);
            System.out.printf("Player %s lost! they are now the ", scumbag);
            Main.printColor("SCUMBAG", Main.Color.RED);
            scumbag = player;
            restartRound();
        }


    }
    // the player doesn't play anything, passes the turn
    public void passTurn(Player player) {
        playersInPlay.remove(player);
        player.setPlaying(false);
        if (playersInPlay.size() == 1) { // nobody else can play anymore
            System.out.printf("Player %s won the set! It is now their turn.", playersInPlay.getFirst().getName());
            winner = playersInPlay.getFirst();
            newSet();
        }
    }

}
