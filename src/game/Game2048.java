/**
 * Game2048.java
 *
 * @author Scott Cao
 * @author Eric Chiu
 * @author Kevin Zhang
 * @version 2.00 2014/5/28
 */


package game;

import javax.swing.*;
import java.awt.event.KeyListener;

public class Game2048 extends JFrame {

    public static final int HOMEPAGE = 0;
    public static final int WINGAME = 1;
    public static final int GAMEOVER = 2;
    public static final int PLAYERONEWINS = 3; // multi-player: player one wins
    public static final int PLAYERTWOWINS = 4; // multi-player: player two wins
    public static Game2048 game;
    public int gameMode;
    public JFrame controller;

    public Game2048() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("2048");

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws Exception {
        game = new Game2048();
        game.showController(Game2048.HOMEPAGE);
    }

    public void showGameBoard(int mode) {
        gameMode = mode;
        if (mode == 1) {
            GameBoard board = new GameBoard(5);
            setSize(750, 750);

            JTextField typingArea = new JTextField();
            typingArea.addKeyListener(board);
            typingArea.setEditable(false);

            getContentPane().removeAll();
            getContentPane().add(typingArea);
            getContentPane().add(board);

        } else if (mode == 2) {
            GameBoard board = new GameBoard(4);
            setSize(600, 600);

            JTextField typingArea = new JTextField();
            typingArea.addKeyListener(board);
            typingArea.setEditable(false);

            getContentPane().removeAll();
            getContentPane().add(typingArea);
            getContentPane().add(board);

        } else {
            Game512 game512 = new Game512(4);
            setSize(1250, 600);

            JTextField typingArea = new JTextField();
            typingArea.addKeyListener((KeyListener) game512.getGameBoard1());
            typingArea.addKeyListener((KeyListener) game512.getGameBoard2());
            typingArea.setEditable(false);

            getContentPane().removeAll();
            getContentPane().add(typingArea);
            getContentPane().add(game512);

        }

        setLocationRelativeTo(null);
        controller.setVisible(false);
        setVisible(true);
    }

    public void showController(int mode) throws Exception {
        if (controller == null) {
            controller = new JFrame();
            controller.setSize(700, 490);
            controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            controller.setLocationRelativeTo(null);
        }

        controller.getContentPane().removeAll();
        if (mode == HOMEPAGE) { // game home
            controller.getContentPane().add(new GameHome());
        } else if (mode == GAMEOVER) { // game over
            controller.getContentPane().add(new GameOver());
        } else if (mode == WINGAME) { // win game
            controller.getContentPane().add(new WinGame());
            controller.setResizable(false);
        } else if (mode == PLAYERONEWINS) {
            controller.getContentPane().add(new GameOver512(true));
        } else {
            controller.getContentPane().add(new GameOver512(false));
        }

        controller.setVisible(true);
        setVisible(false);
    }
}

