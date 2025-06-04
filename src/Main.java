import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---WARLORDS---");
        System.out.println("How many players?");
        int playerAmount = scanner.nextInt();
        scanner.nextLine();
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < playerAmount; i++) {
            Player player = new Player();
            System.out.println("What name for the player? ");
            player.setName(scanner.nextLine());
            players.add(player);
        }
        Round round = new Round(players);
        while (round.getRoundState() != Round.ROUND_STATE.WON) {
            for (Player player : players) {
                System.out.printf("It is time for player %S's turn:%n (press enter to continue)", player.getName());
                scanner.nextLine();
                System.out.printf("Player's hand: %S%n", player.viewHand());
                System.out.println("What cards do you want to play?");
                String cards = scanner.nextLine();

                System.out.println("Press Enter to Continue:");
                scanner.nextLine();
            }

        }
    }
}
