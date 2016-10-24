package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by scott on 10/23/16.
 */
class GameOver extends JPanel {

    private JButton playAgain = new JButton("Try Again");
    private JButton homePage = new JButton("Back to Home");
    private Image img = new ImageIcon("GAME OVER.jpg").getImage();

    private JPanel p1 = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    };
    private JPanel p2 = new JPanel();

    public GameOver() {
        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p2, BorderLayout.SOUTH);

        playAgain.setFont(new Font("Arial", Font.PLAIN, 20));
        homePage.setFont(new Font("Arial", Font.PLAIN, 20));
        playAgain.setPreferredSize(new Dimension(170, (int) playAgain
                .getPreferredSize().getHeight()));
        homePage.setPreferredSize(new Dimension(170, (int) homePage
                .getPreferredSize().getHeight()));

        p2.add(playAgain);
        p2.add(homePage);

        playAgain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game2048.game.showGameBoard(Game2048.game.gameMode);
            }

        });

        homePage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Game2048.game.showController(Game2048.HOMEPAGE);
                } catch (Exception ex) {
                    System.out.println("ERROR");
                }
            }

        });
    }
}
