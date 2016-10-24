package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by scott on 10/23/16.
 */
class GameBoard extends JPanel implements KeyListener {

    private GameButton[][] buttons;
    private ArrayList<ArrayList<Integer>> matrix;
    private boolean moveable;
    private boolean keyListening;
    private Font font;

    public GameBoard(int side) {
        setSize(side * 200, side * 200);
        setLayout(new GridLayout(side, side, 4, 4));
        initiateCells(side);
        keyListening = true;

        if (side == 4)
            font = new Font("Times New Roman", Font.BOLD, 50);
        if (side == 5)
            font = new Font("Times New Roman", Font.BOLD, 40);
    }

    private static void playSound(String fileName) throws Exception {
        File url = new File(fileName);
        Clip clip = AudioSystem.getClip();

        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        clip.open(ais);
        clip.start();
    }

    ArrayList<ArrayList<Integer>> getMatrix() {
        return matrix;
    }

    void setKeyListenerMode(boolean k) {
        keyListening = k;
    }

    boolean getKeyListeningMode() {
        return keyListening;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            boolean rightKey = move(e.getKeyCode());
            check(rightKey);
        } catch (Exception ex) {
            System.out.println("ERROR: NO IMAGE FOUND");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private boolean move(int key) {

        if (keyListening) {
            switch (key) {
                case KeyEvent.VK_LEFT:
                    crunch();
                    return true;
                case KeyEvent.VK_RIGHT:
                    shiftRight();
                    crunch();
                    shiftRight();
                    return true;
                case KeyEvent.VK_UP:
                    shiftUp();
                    crunch();
                    shiftUp();
                    return true;
                case KeyEvent.VK_DOWN:
                    shiftDown();
                    crunch();
                    shiftRight();
                    shiftUp();
                    return true;
            }
        } else {
            switch (key) {
                case KeyEvent.VK_A:
                    crunch();
                    return true;
                case KeyEvent.VK_D:
                    shiftRight();
                    crunch();
                    shiftRight();
                    return true;
                case KeyEvent.VK_W:
                    shiftUp();
                    crunch();
                    shiftUp();
                    return true;
                case KeyEvent.VK_S:
                    shiftDown();
                    crunch();
                    shiftRight();
                    shiftUp();
                    return true;
            }
        }

        return false;
    }

    private void check(boolean rightKey) {

        try {
            if (checkWin())
                winGame();

            else if (checkLose())
                endGame();

            else if (moveable && rightKey) {
                display();
                playSound("coin.wav");
                addNum(randomNumberGenerate());
                display();
            }
        } catch (Exception ex) {
            System.out.println("ERROR");
        }

    }

    private void initiateCells(int side) {

        buttons = new GameButton[side][side];
        for (int r = 0; r < buttons.length; r++)
            for (int c = 0; c < buttons[r].length; c++) {
                buttons[r][c] = new GameButton();
                add(buttons[r][c]);
            }

        matrix = new ArrayList<ArrayList<Integer>>(buttons.length);
        for (int r = 0; r < buttons.length; r++) {
            ArrayList<Integer> list = new ArrayList<Integer>(buttons[0].length);
            for (int c = 0; c < buttons[0].length; c++)
                list.add(0);
            matrix.add(list);
        }

        addNum(randomNumberGenerate());
        addNum(randomNumberGenerate());
        display();

    }

    private int randomNumberGenerate() {
        if (Math.random() > 0.2)
            return 2;
        return 4;
    }

    private void addNum(int num) {

        int r, c;
        do {
            r = (int) (Math.random() * buttons.length);
            c = (int) (Math.random() * buttons[0].length);
        } while (matrix.get(r).get(c) != 0);

        matrix.get(r).set(c, num);

    }

    private void shiftRight() {
        for (int r = 0; r < matrix.size(); r++) {
            ArrayList<Integer> temp = matrix.get(r);
            for (int c = temp.size() - 2; c >= 0; c--)
                temp.add(temp.remove(c));
        }
    }

    private void shiftUp() {
        int size = matrix.get(0).size();
        for (int c = 0; c < size; c++)
            for (int r = 0; r < matrix.size(); r++)
                matrix.get(c).add(matrix.get(r).remove(0));
    }

    private void shiftDown() {
        int size = matrix.get(0).size();
        for (int c = 0; c < size; c++)
            for (int r = matrix.size() - 1; r >= 0; r--)
                matrix.get(c).add(matrix.get(r).remove(0));
    }

    private void crunch() {

        int size = matrix.get(0).size();
        moveable = false;

        for (ArrayList<Integer> list : matrix) {

            int spot = list.size() - 1;
            while (spot >= 0 && list.get(spot) == 0)
                spot--;

            for (; spot >= 0; spot--)
                if (list.get(spot) == 0)
                    list.remove(spot);

            for (spot = 0; spot < list.size() - 1; spot++)
                if ((int) (list.get(spot)) == (int) (list.get(spot + 1))
                        && list.get(spot) != 0) {
                    list.set(spot, list.get(spot) * 2);
                    list.remove(spot + 1);
                }

            for (spot = list.size(); spot < size; spot++) {
                moveable = true;
                list.add(0);
            }
        }
    }

    private boolean checkGameOver() {
        boolean full = true;
        for (int r = 0; r < matrix.size(); r++)
            for (int c = 0; c < matrix.size() - 1; c++) {
                if ((int) matrix.get(r).get(c) == (int) (matrix.get(r)
                        .get(c + 1)) || (int) (matrix.get(r).get(c)) == 0)
                    full = false;
            }
        return full;
    }

    boolean checkWin() {
        for (ArrayList<Integer> list : matrix)
            for (int i : list)
                if (i == 2048)
                    return true;
        return false;
    }

    boolean checkLose() {
        boolean one = checkGameOver();
        shiftDown();
        boolean two = checkGameOver();
        shiftRight();
        shiftUp();
        return one && two;
    }

    void winGame() {
        try {
            Game2048.game.showController(Game2048.WINGAME);
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
    }

    void endGame() {
        try {
            Game2048.game.showController(Game2048.GAMEOVER);
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
    }

    private void display() {
        for (int r = 0; r < buttons.length; r++) {
            ArrayList<Integer> temp = matrix.get(r);
            for (int c = 0; c < buttons[r].length; c++) {
                buttons[r][c]
                        .setFont(new Font("Times New Roman", Font.BOLD, 50));
                if (temp.get(c) == 0)
                    buttons[r][c].setText("");
                else
                    buttons[r][c].setText("" + temp.get(c));
            }
        }
    }

}
