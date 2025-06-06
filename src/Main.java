import org.junit.experimental.theories.Theories;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // clear screen code taken from stack overflow
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
        while (round.getRoundState() != Round.ROUND_STATE.WON) {
            for (Player player : players) {
                if (!round.isPlayerInPlay(player)) {
                    System.out.printf("Player %S has passed %n", player.getName());
                    continue;
                }
                System.out.printf("It is time for player %S's turn:%n (press enter to continue)", player.getName());
                scanner.nextLine();
                System.out.println("The cards currently played are: " + round.getPlayedCards() + "\n And the current hand type is " + round.getHandType());
                System.out.print("Player's hand: \n");
                player.printFancyHand();
                Round.PLAY_RESULT play_result = Round.PLAY_RESULT.HAS_NOT_PLAYED;
                while (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                    System.out.println("What cards do you want to play? (e.g. 3 of hearts is 3H, joker is JOK) - to pass type 'pass'");
                    String cards = scanner.nextLine();
                    if (cards.strip().equalsIgnoreCase("pass")) {
                        round.passTurn(player);
                        play_result = Round.PLAY_RESULT.TURN_PASSED;
                        System.out.println("Turn Passed!");
                        break;
                    } else {
                        ArrayList<Card> cardsPlayed = player.getCardsFromHandByNames(cards.split(" "));
                        play_result = round.submitCards(player, cardsPlayed);

                        if (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                            System.out.println("ERROR: The cards played returned status: " + play_result);
                        } else {
                            System.out.println("Well Played! Cards returned " + play_result);
                        }
                    }

                }
                System.out.println("Turn over, Press Enter to Continue:");
                scanner.nextLine();
                clearScreen();
            }

        }
    }
}
