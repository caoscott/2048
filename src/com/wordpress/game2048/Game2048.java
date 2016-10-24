/**
 * Game2048.java
 *
 * @author Scott Cao
 * @author Eric Chiu
 * @author Kevin Zhang
 * @version 2.00 2014/5/28
 */


package com.wordpress.game2048;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Game2048 extends JFrame {

	public static Game2048 game;
	public static final int HOMEPAGE = 0;
	public static final int WINGAME = 1;
	public static final int GAMEOVER = 2;
	public static final int PLAYERONEWINS = 3; // multi-player: player one wins
	public static final int PLAYERTWOWINS = 4; // multi-player: player two wins

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

