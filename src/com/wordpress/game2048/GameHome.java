package com.wordpress.game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by scott on 10/23/16.
 */
class GameHome extends JPanel {

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
