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

//multi-player
class Game512 extends JPanel {

	private GameBoard512 panel1;
	private GameBoard512 panel2;

	public Game512(int side) {
		initiate(side);
	}

	private void initiate(int side) {

		this.setLayout(new GridLayout(1, 2, 50, 50));
		panel1 = new GameBoard512(side, this);
		panel2 = new GameBoard512(side, this);
		this.add(panel1);
		this.add(panel2);

		panel1.setKeyListenerMode(false);
		panel2.setKeyListenerMode(true);

	}

	public GameBoard getGameBoard1() {
		return panel1;
	}

	public GameBoard getGameBoard2() {
		return panel2;
	}

	void gameOver(GameBoard512 gb, boolean lose) throws Exception {
		if (lose && panel1 == gb || !lose && panel2 == gb)
			Game2048.game.showController(Game2048.PLAYERTWOWINS);
		else
			Game2048.game.showController(Game2048.PLAYERONEWINS);
	}

}

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
	
	ArrayList<ArrayList<Integer>> getMatrix(){
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
	
	private boolean move(int key){
		
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
		}

		else {
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
	
	private void check(boolean rightKey){
		
		try{
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
		} catch (Exception ex){
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
	
	boolean checkLose(){
		boolean one = checkGameOver();
		shiftDown();
		boolean two = checkGameOver();
		shiftRight();
		shiftUp();
		return one && two;
	}

	void winGame() {
		try{
			Game2048.game.showController(Game2048.WINGAME);
		} catch (Exception ex) {
			System.out.println("ERROR");
		}
	}

	void endGame() {
		try{
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

	private static void playSound(String fileName) throws Exception {
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();
	}

}

class GameBoard512 extends GameBoard {

	private Game512 panel;

	GameBoard512(int side, Game512 p) {
		super(side);
		panel = p;
	}
	
	@Override
	boolean checkWin() {
		for (ArrayList<Integer> list : getMatrix())
			for (int i : list)
				if (i == 512)
					return true;
		return false;
	}

	@Override
	void endGame() {
		try{
			panel.gameOver(this, true);
		} catch (Exception ex) {
			System.out.println("ERROR");
		}
	}

	@Override
	void winGame() {
		try{
			panel.gameOver(this, false);
		} catch (Exception ex) {
			System.out.println("ERROR");
		}
	}

}

class GameButton extends JButton {

	@Override
	public void setText(String str) {
		super.setText(str);
		if (str.equals(""))
			setBackground(null);
		else
			switch (Integer.parseInt(str)) {
			case 2:
				setBackground(new Color(238, 228, 218));
				break;
			case 4:
				setBackground(new Color(237, 224, 200));
				break;
			case 8:
				setBackground(new Color(242, 177, 121));
				break;
			case 16:
				setBackground(new Color(245, 149, 99));
				break;
			case 32:
				setBackground(new Color(246, 124, 95));
				break;
			case 64:
				setBackground(new Color(246, 94, 59));
				break;
			case 128:
				setBackground(new Color(237, 207, 114));
				break;
			case 256:
				setBackground(new Color(237, 204, 97));
				break;
			case 512:
				setBackground(new Color(237, 200, 80));
				break;
			case 1024:
				setBackground(new Color(237, 197, 63));
				break;
			case 2048:
				setBackground(new Color(255, 255, 0));
				break;
			case 4096:
				setBackground(new Color(94, 218, 146));
				break;
			default:
				setBackground(new Color(255, 255, 0));
			}
	}
}

class GameHome extends JPanel{

  	private JButton easy = new JButton("Beginners");
    private JButton normal = new JButton("Normal");
   	private JButton multi = new JButton("Multi-Player");
   	
  	public GameHome() {
 		
    	BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);	
    	setLayout(layout);
    	setBackground(new Color(236, 196, 0));
    	
    	JLabel title = new JLabel("2048");
    	title.setFont(new Font("Serif", Font.BOLD, 200));
   		title.setForeground(Color.WHITE);
   		
  		easy.setFont(new Font("Arial", Font.PLAIN, 20));
  		normal.setFont(new Font("Arial", Font.PLAIN, 20));
  		multi.setFont(new Font("Arial", Font.PLAIN, 20));
  		
   		add(title);
   		add(Box.createRigidArea(new Dimension(0,20)));
   		add(easy);
    	add(Box.createRigidArea(new Dimension(0,20)));
    	add(normal);
    	add(Box.createRigidArea(new Dimension(0,20)));
    	add(multi);
   
    	title.setAlignmentX(Component.CENTER_ALIGNMENT);
   		easy.setAlignmentX(Component.CENTER_ALIGNMENT);
   		normal.setAlignmentX(Component.CENTER_ALIGNMENT);
   		multi.setAlignmentX(Component.CENTER_ALIGNMENT);
   		
    	easy.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e){
    			Game2048.game.showGameBoard(1);
    		}
    			
    	});
    	
  		normal.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e){
    			Game2048.game.showGameBoard(2);
    		}
    			
    	});
    	
    	multi.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e){
    			Game2048.game.showGameBoard(3);
    		}
    			
    	});
    }
}

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

class WinGame extends JPanel {

	private JButton playAgain = new JButton("Try Again");
	private JButton homePage = new JButton("Back to Home");
	private Image img = new ImageIcon("YOU WIN.jpg").getImage();

	private JPanel p1 = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, null);
		}
	};
	private JPanel p2 = new JPanel();

	public WinGame() {
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
