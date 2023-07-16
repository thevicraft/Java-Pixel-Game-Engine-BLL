package com.javapixelgame.game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.javapixelgame.game.GameConstants;
import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.gui.component.ConfigComponent;
import com.javapixelgame.game.gui.component.ConfigComponent.ActionButton;
import com.javapixelgame.game.resourcehandling.GameFont;
import com.javapixelgame.game.resourcehandling.TextureID;
import com.javapixelgame.game.test.GLTest;
import com.javapixelgame.game.test.TestFrame;

@SuppressWarnings("serial")
public class MainMenu extends AbstractGUI {

	public static final int com = 6;

	public JComponent[] c;

	public MainMenu(int width, int height, Texture texture) {
		super(width, height);
		setBackgroundImage(texture);
	}

	@Override
	protected void paintOverlay(Graphics2D g) {
	}

	@Override
	protected void init() {
//		int width = (int) (getWidth() * 0.35);
		int width = getColumnScaledWidth(getWidth(),3);
		
		c = new JComponent[com];
		c[0] = new JLabel("The Game");
		c[0].setOpaque(false);
		c[0].setPreferredSize(new Dimension(width, width / 10));
		c[0].setForeground(Color.white);
		((JLabel) c[0]).setHorizontalAlignment(SwingUtilities.CENTER);
		c[0].setFont(GameFont.getKarmaticArcade(Font.BOLD, width / 10));
		c[0].setForeground(Color.red);

		c[1] = new Button("New Game", width);
		
		c[2] = new Button("Load Game", width);
		
		c[3] = new Button("Settings", width);

		c[5] = new Button("Quit Game", width);

		((Button) c[1]).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.game.setState(Game.world_create_menu);
			}

		});
		
		((Button) c[2]).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.game.setState(Game.world_load_selection);
			}

		});
		((Button) c[3]).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.game.setState(Game.settings);
			}

		});

		((Button) c[5]).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.close(true);
			}

		});

		
		int row = 1;
		for (int i = 0; i < com; i++) {
			if(c[i]!= null) {
				addComponent(c[i], 2, row);
				row++;
			}
		}
		
		JLabel left = new JLabel(" "+"¢"+GameConstants.GameProducer);
		left.setOpaque(false);
		left.setPreferredSize(new Dimension(width/2, width / 10));
		left.setForeground(Color.red);
		left.setHorizontalAlignment(SwingUtilities.LEFT);
		left.setFont(GameFont.getRainyHearts(Font.BOLD, width / 20));
		
		Texture t = new Texture(TextureID.AUTHOR);
		t.optimize(width/10-2, width/10-2);
		left.setIcon(t.getIcon());
		
		JLabel right = new JLabel((GameConstants.SNAPSHOT ? "Snapshot: " : "Version: ")+GameConstants.VERSION+" ");
		right.setOpaque(false);
		right.setPreferredSize(new Dimension(width/2, width / 10));
		right.setForeground(Color.blue);
		right.setHorizontalAlignment(SwingUtilities.RIGHT);
		right.setFont(GameFont.getRainyHearts(Font.BOLD, width / 20));
		
		addComponent(left,2,row);
		addComponent(right, 2, row);
		
		row++;
		
		ActionButton ai_pathfinder = new ActionButton("Pathfinder Test",width/2,a ->{
			TestFrame.mainTest();
		},null);
		
		ActionButton opengl = new ActionButton("OpenGL Test",width/2,a ->{
			GLTest.mainTest();
		},null);
		
		addComponent(ai_pathfinder, 1, row);
		addComponent(opengl, 3, row);
	}
	
	}
