import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class Display {
    static class Game implements ActionListener {

        int count = 0;
        JLabel label;
        JButton button;
        JFrame frame;
        JPanel panel;

        public Game() {
            JFrame frame = new JFrame();

            button = new JButton("play hand");
            button.addActionListener(this);
            label = new JLabel("Number of cards: 0");


            panel = new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
            panel.setLayout(new GridLayout(3,2));
            panel.add(button);
            panel.add(label);

            frame.add(panel, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Warlords");
            frame.pack();
            frame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            count++;
            label.setText("Number of Cards " + count);
        }
    }
    public static void main(String[] args) {
        Game game = new Game();
        /*JButton play_hand = new JButton("Play Your Hand");
        play_hand.setBounds(150, 200, 220, 50);
        frame.add(play_hand);

        frame.setSize(600,600);
        frame.setLayout(null);
        frame.setVisible(true);*/

    }
}
