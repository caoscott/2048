package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by scott on 10/23/16.
 */
class GameOver512 extends JPanel { // multi-player game over

    private JButton tryAgain = new JButton("Try Again");
    private JButton homePage = new JButton("Back to Home");

    GameOver512(boolean winner) {

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        setBackground(new Color(236, 196, 0));

        JLabel title = new JLabel("");
        if (winner)
            title = new JLabel("PLAYER ONE WINS");
        else
            title = new JLabel("PLAYER TWO WINS");
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setForeground(Color.RED);

        tryAgain.setFont(new Font("Arial", Font.PLAIN, 20));
        homePage.setFont(new Font("Arial", Font.PLAIN, 20));

        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(tryAgain);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(homePage);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        tryAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        homePage.setAlignmentX(Component.CENTER_ALIGNMENT);

        tryAgain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game2048.game.showGameBoard(Game2048.game.gameMode);
            }

        });

        homePage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Game2048.game.showController(Game2048.HOMEPAGE);
                } catch (Exception ex) {
                    System.out.println("TROLOLOLOLOL");
                }
            }

        });
    }
}
