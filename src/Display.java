import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class Display {
    public static class NumberField extends JTextField {
        public NumberField(int cols) {
            super(cols);
        }

        protected Document createDefaultModel() {
            return new NumberDocument();
        }

        static class NumberDocument extends PlainDocument {

            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (str == null) {return;}
                char[] chars = str.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] > '9' || chars[i] < '0') {
                        return;
                    }
                }
                super.insertString(offs, new String(chars), a);
            }
        }
    }
    static class Game implements ActionListener {

        int count = 0;
        JLabel label, test, titleLabel, playerAmountLabel;
        JButton button, submitPlayers;
        JFrame frame;
        JPanel panel;
        NumberField playerAmountField;

        public static void runGame() throws Exception {
            Scanner scanner = new Scanner(System.in);
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
                        } else {
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

                }

            }
        }
        public Game() {



            JFrame frame = new JFrame();

            button = new JButton("play hand");
            button.addActionListener(this);
            label = new JLabel("Number of cards: 0");


            titleLabel = new JLabel("---WARLORDS---");
            playerAmountLabel = new JLabel("How many players?");
            playerAmountField = new NumberField(3);
            submitPlayers = new JButton("Start Game");
            submitPlayers.addActionListener(this);
            test = new JLabel("");




            panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
            panel.setLayout(new GridLayout(3,2));

            panel.add(titleLabel);
            panel.add(playerAmountLabel);
            panel.add(playerAmountField);
            panel.add(submitPlayers);


            panel.add(button);
            panel.add(label);
            panel.add(test);

            frame.add(panel, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Warlords");
            frame.pack();
            frame.setVisible(true);


        }
        public void play(Integer players) {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button) {
                count++;
                label.setText("Times clicked " + count);
            } else if (e.getSource() == submitPlayers) {
                test.setText(playerAmountField.getText());
                play(Integer.getInteger(playerAmountField.getText()));
            }
        }


    }

    public static void main(String[] args) {

        Game game = new Game();

    }
    public static void printHelp() {
        System.out.print("""
                What is Warlords?
                It's like multiplayer war, except instead of being completely deterministic, the player has a choice! That makes it such a better game.\s
               The game is played in sets, which make up a round. The person with the 3 of clubs starts, and the winner of each set starts the next one.
               The cards are ranked 3->King, Ace, 2, Joker. Be the first one to play all of your cards to win!
               \s""");
    }

}

