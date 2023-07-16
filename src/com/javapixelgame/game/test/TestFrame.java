package com.javapixelgame.game.test;

import javax.swing.JFrame;

public class TestFrame {
	
	public static void mainTest() {
		
		JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setResizable(false);
		
		window.add(new DemoPanel());
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
}
