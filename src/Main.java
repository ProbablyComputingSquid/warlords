/**
 * Main.java - contains the driver for the project
 * */

import com.sun.jdi.event.ExceptionEvent;

import java.nio.channels.ScatteringByteChannel;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static String[] successful_play_dialogues = { // stolen from balatro teehee
            "You Aced it!",
            "You dealt with that pretty well!",
            "Looks like you weren't bluffing!",
            "Too bad these chips are all virtual...",
            "How the turn tables.",
            "Looks like I've taught you well!",
            "You made some heads up plays!",
            "Good thing I didn't bet against you!",
    };
    public enum Color {
        RESET("\033[0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[38;2;176;0;32m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),

        GOLD("\u001B[38;2;255;185;79m"),

        BOLD("\u001B[1m"),
        UNDERLINE("\u001B[4m"),
        BLINK_ON("\u001B[5m"),
        BLINK_OFF("\u001B[25m"),
        FELT_GREEN_BG("\u001B[48;2;11;102;35m"),
        ERROR_BG("\u001B[48;2;251;192;45m"),
        WHITE_BACKGROUND("\u001B[107m");
        public final String code;
        Color(String s) {
            code = s;
        }
        @Override
        public String toString() {
            return code;
        }
    }
    public static void printColor(String content, Color[] colors) {
        for (Color color : colors) {
            System.out.print(color);
        }
        System.out.print(content);
        System.out.print(Color.RESET);
        System.out.flush();
    }
    public static void printColor(String content, Color color) {
        printColor(content, new Color[]{color});
    }
    public static void printlnColor(String content, Color[] colors) {
        printColor(content, colors);
        System.out.println();
    }
    public static void printAlternatingColors(String content, Color[] colors, Color[] colors_always) {
        char[] contentArr = content.toCharArray();
        Color[] colorholder = new Color[colors_always.length + 1];
        System.arraycopy(colors_always, 0, colorholder, 0, colors_always.length);
        for (int i = 0; i<contentArr.length; i++) {
            colorholder[colorholder.length-1] = colors[i % colors.length];
            printColor(Character.toString(contentArr[i]),colorholder);
        }
    }
    // clear screen code taken from stack overflow
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void reset() {
        System.out.print(Color.RESET);
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

        printAlternatingColors("┏━━━━━━━━┓", new Color[]{Color.RED, Color.BLACK}, new Color[]{Color.WHITE_BACKGROUND});
        System.out.println();
        printColor("┃", new Color[]{Color.BLACK, Color.WHITE_BACKGROUND});
        printColor("WARLORDS", new Color[]{Color.GOLD,Color.BOLD, Color.WHITE_BACKGROUND});
        printColor("┃", new Color[]{Color.BLACK, Color.WHITE_BACKGROUND});
        System.out.println();
        printAlternatingColors("┗━━━━━━━━┛", new Color[]{Color.RED, Color.BLACK}, new Color[]{Color.WHITE_BACKGROUND});
        System.out.println("\nHow many players?");
        int playerAmount = -1;
        while (playerAmount <= 1) {
            playerAmount = scanner.nextInt();
            scanner.nextLine();
            if (playerAmount <= 1) {
                printColor("ERROR:", new Color[] {Color.RED});
                System.out.println("Invalid number of players, you can't play a card game alone (unless it's solitaire or balatro) ");
            }
        }


        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < playerAmount; i++) {
            Player player = new Player();
            System.out.printf("What name for the player %d? ", i);
            player.setName(scanner.nextLine());
            players.add(player);
        }

        boolean is_playing = true;
        boolean is_scumbag = false;
        Round round = new Round(players);
        while (is_playing) {
        System.out.println("New Round!");
        while (round.getRoundState() != Round.ROUND_STATE.FINISHED && round.getRoundState() != Round.ROUND_STATE.ABORTED) {
            for (Player player : round.getPlayers()) {
                clearScreen();
                if (!round.isPlayerInPlay(player)) {
                    System.out.printf("Player %S has previously passed. press enter to continue %n", player.getName());
                    scanner.nextLine();
                    continue;
                }
                System.out.printf("It is time for player " + Color.BOLD + "%S" + Color.RESET + "'s turn:%n (press enter to continue)", player.getName());
                scanner.nextLine();
                System.out.println("Players in play: ");
                for (Player p : round.getPlayersInPlay()) {
                    System.out.println("\uD83D\uDC64" + p);
                }
                System.out.println("The cards currently played are: ");
                round.displayFancyPlayedCards();
                reset();
                System.out.print("\n And the current hand type is ");
                printlnColor(String.valueOf(round.getHandType()), new Color[]{Color.BOLD, Color.UNDERLINE});
                System.out.printf("Player %s's hand: \n", player.getName());


                Round.PLAY_RESULT play_result = Round.PLAY_RESULT.HAS_NOT_PLAYED;
                label:
                while (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                    player.printFancyHand();
                    reset();
                    System.out.println("What cards do you want to play? (e.g. 3 of hearts is 3h, 4 of clubs and spades is 4C 4S, joker is JOKER) | to pass type 'pass' | for help 'help'");
                    String cards = scanner.nextLine().toUpperCase().strip();
                    switch (cards) {
                        case "PASS":
                            System.out.println("Turn Passed!");
                            round.passTurn(player);
                            play_result = Round.PLAY_RESULT.TURN_PASSED;
                            break label;
                        case "QUIT":
                            printlnColor("ABORTING...", new Color[]{Color.BOLD, Color.RED});
                            round.abort();
                            is_playing = false;
                            break label;
                        case "HELP":
                            printHelp();
                            break;
                        default:
                            ArrayList<Card> cardsPlayed;
                            try {
                                 cardsPlayed = player.getCardsFromHandByNames(cards.split(" "));
                            } catch (Exception e) {
                                System.out.println("error! thats probably not a card");
                                break ;
                            }
                            play_result = round.submitCards(player, cardsPlayed);

                            if (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                                if (play_result != Round.PLAY_RESULT.FAILED_NOT_A_VALID_CARD) { // dont try displaying an invalid card
                                    Deck.printCards(cardsPlayed, Color.ERROR_BG);
                                    reset();
                                }
                                printColor("ERROR:", Color.RED);
                                System.out.println("The cards played returned status: " + play_result);
                            } else {
                                reset();
                                System.out.print(successful_play_dialogues[(int) (Math.random() * successful_play_dialogues.length)]);
                                System.out.print(" Cards returned ");
                                printlnColor(String.valueOf(play_result), new Color[]{Color.GREEN, Color.BOLD});

                            }
                            break;
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
}
