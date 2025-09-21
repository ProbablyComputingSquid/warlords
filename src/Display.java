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
                for (char aChar : chars) {
                    if (aChar > '9' || aChar < '0') {
                        return;
                    }
                }
                super.insertString(offs, new String(chars), a);
            }
        }
    }
    static class Game implements ActionListener {


        public JLabel test, titleLabel, playerAmountPromptLabel;
        public JButton submitPlayers;
        JFrame frame = new JFrame();
        JPanel panel;
        public NumberField playerAmountField;

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



            //JFrame frame


            titleLabel = new JLabel("---WARLORDS---");
            playerAmountPromptLabel = new JLabel("How many players?");
            playerAmountField = new NumberField(3);
            submitPlayers = new JButton("Start Game");
            submitPlayers.addActionListener(this);
            test = new JLabel("");




            panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
            panel.setLayout(new GridLayout(3,2));

            panel.add(titleLabel);
            panel.add(playerAmountPromptLabel);
            panel.add(playerAmountField);
            panel.add(submitPlayers);


            panel.add(test);

            frame.add(panel, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Warlords");
            frame.pack();
            frame.setVisible(true);


        }
        JFrame gameFrame;
        JPanel cardsPanel;
        JLabel playerLabel, playersListLabel, statusLabel;
        JTextField handField;
        public void play(Integer playerAmount) {
            gameFrame = new JFrame();
            cardsPanel = new JPanel();
            playerLabel = new JLabel("Player amount: " + playerAmount);
            handField = new JTextField();
            statusLabel = new JLabel();
            playersListLabel = new JLabel();

            /* game logic n stuff idk (also me when i forget how my own code works */
            ArrayList<Player> players = new ArrayList<>();
            String playerNames = "";
            for (int i = 0; i < playerAmount; i++) {
                Player player = new Player();
                System.out.printf("What name for the player %d? ", i);
                String name = JOptionPane.showInputDialog(
                        null,                       // parent component (null = center on screen)
                        "Enter Player " + i + " name:",         // prompt message
                        "Input Required",           // title of dialog
                        JOptionPane.QUESTION_MESSAGE
                );

                if (name != null) {
                    System.out.println("You entered: " + name);
                } else {
                    System.out.println("User canceled the input.");
                    name = "Player " + i;
                }
                playerNames += name + ", ";
                player.setName(name);
                players.add(player);
            }
            playerNames = playerNames.substring(0,playerNames.length() - 2);
            playersListLabel.setText(playerNames);
            cardsPanel.add(handField);
            cardsPanel.add(statusLabel);
            cardsPanel.add(playerLabel);
            cardsPanel.add(playersListLabel);
            cardsPanel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
            cardsPanel.setLayout(new GridLayout(3,2));

            /* pack everything in and display */
            gameFrame.add(cardsPanel, BorderLayout.CENTER);
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setTitle("Warlords");
            gameFrame.pack();
            gameFrame.setVisible(true);
            Round round = new Round(players);
            while (round.getRoundState() != Round.ROUND_STATE.WON && round.getRoundState() != Round.ROUND_STATE.ABORTED) {
                for (Player player : round.getPlayers()) {
                    if (!round.isPlayerInPlay(player)) {
                        System.out.printf("Player %S has previously passed %n", player.getName());
                        continue;
                    }
                    JOptionPane.showMessageDialog(null, "It is time for player " + player.getName() + "'s turn:%n (press enter to continue)");


                    System.out.println("The cards currently played are: " + round.getPlayedCards() + "\n And the current hand type is " + round.getHandType());
                    System.out.printf("Player %s's hand: \n", player.getName());
                    handField.setText(player.getFancyHand());
                    player.printFancyHand();
                    Round.PLAY_RESULT play_result = Round.PLAY_RESULT.HAS_NOT_PLAYED;
                    while (play_result != Round.PLAY_RESULT.SUCCESS && play_result != Round.PLAY_RESULT.JOKER_SUCCESS) {
                        System.out.println("What cards do you want to play? (e.g. 3 of hearts is 3H, 4 of clubs and spades is 4C 4S, joker is JOKER) - to pass type 'pass'");
                        String cards = JOptionPane.showInputDialog(
                                handField,                       // parent component (null = center on screen)
                                "What cards will you play?",         // prompt message
                                "Play cards",           // title of dialog
                                JOptionPane.QUESTION_MESSAGE
                        );
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
                    JOptionPane.showMessageDialog(null, "Kindly affirm for next player");
                }

            }




        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submitPlayers) {
                System.out.println();
                int players = Integer.parseInt(this.playerAmountField.getText());
                frame.dispose();
                play(players);
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

