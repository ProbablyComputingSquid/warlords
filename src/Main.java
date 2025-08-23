/**
 * Main.java - contains the driver for the project
 *
 * */

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // clear screen code taken from stack overflow
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void printHelp() {
        System.out.print("""
                What is Warlords?
                It's like multiplayer war, except instead of being completely deterministic, the player has a choice! That makes it such a better game.\s
               The game is played in sets, which make up a round. The person with the 3 of clubs starts, and the winner of each set starts the next one.
               The cards are ranked 3->King, Ace, 2, Joker. Be the first one to play all of your cards to win!
               \s""");
    }
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---WARLORDS---");
        System.out.println("How many players?");
        int playerAmount = scanner.nextInt();
        scanner.nextLine();
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < playerAmount; i++) {
            Player player = new Player();
            System.out.printf("What name for the player %d? ", i);
            player.setName(scanner.nextLine());
            players.add(player);
        }
        Round round = new Round(players);
        while (round.getRoundState() != Round.ROUND_STATE.WON && round.getRoundState() != Round.ROUND_STATE.ABORTED) {
            for (Player player : round.getPlayers()) {
                if (!round.isPlayerInPlay(player)) {
                    System.out.printf("Player %S has previously passed %n", player.getName());
                    continue;
                }
                System.out.printf("It is time for player %S's turn:%n (press enter to continue)", player.getName());
                scanner.nextLine();
                for (Player p : players) {
                    System.out.println(p);
                }
                System.out.println("The cards currently played are: " + round.getPlayedCards() + "\n And the current hand type is " + round.getHandType());
                System.out.printf("Player %s's hand: \n", player.getName());
                player.printFancyHand();
                Round.PLAY_RESULT play_result = Round.PLAY_RESULT.HAS_NOT_PLAYED;
                while (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                    System.out.println("What cards do you want to play? (e.g. 3 of hearts is 3H, 4 of clubs and spades is 4C 4S, joker is JOKER) - to pass type 'pass'");
                    String cards = scanner.nextLine();
                    if (cards.strip().equalsIgnoreCase("pass")) {
                        System.out.println("Turn Passed!");
                        round.passTurn(player);
                        play_result = Round.PLAY_RESULT.TURN_PASSED;
                        break;
                    } else if (cards.strip().equalsIgnoreCase("quit")) {
                        System.out.println("ABORTING...");
                        round.abort();
                        break;
                    }else {
                        ArrayList<Card> cardsPlayed = player.getCardsFromHandByNames(cards.split(" "));
                        play_result = round.submitCards(player, cardsPlayed);

                        if (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                            System.out.println("ERROR: The cards played returned status: " + play_result);
                        } else {
                            System.out.println("Well Played! Cards returned " + play_result);
                            //System.out.print("␇"); //. this should make a bell noise but jetbrains terminal is bad
                        }
                    }

                }
                if (round.getRoundState() == Round.ROUND_STATE.ABORTED) break;
                System.out.println("Turn over, Press Enter to Continue:");
                scanner.nextLine();
                clearScreen();
            }

        }
    }
}
