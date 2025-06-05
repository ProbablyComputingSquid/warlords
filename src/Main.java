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
                System.out.printf("It is time for player %S's turn:%n (press enter to continue)", player.getName());
                scanner.nextLine();
                System.out.println("The cards currently played are: " + round.getPlayedCards() + "\n And the current hand type is " + round.getHandType());
                System.out.printf("Player's hand: %S%n", player.viewHand());
                System.out.println("What cards do you want to play?");
                String cards = scanner.nextLine();
                ArrayList<Card> cardsPlayed = player.getCardsFromHandByNames(cards.split(" "));
                System.out.println("The cards played returned status: " + round.submitCards(player, cardsPlayed));
                System.out.println("Press Enter to Continue:");
                scanner.nextLine();
                clearScreen();
            }

        }
    }
}
