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

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class DeathMenu extends AbstractGUI {

	public static final int com = 6;

	public JComponent[] c = new JComponent[com];

	public DeathMenu(int width, int height) {
		super(width, height);
	}

	@Override
	protected void init() {
		c = new JComponent[com];
//		int width = (int) (getWidth() * 0.5);
		int width = getColumnScaledWidth(getWidth(),2);
		c[0] = new JLabel("You died");
		c[0].setOpaque(false);
		c[0].setPreferredSize(new Dimension(width, width / 10));
		c[0].setForeground(Color.white);
		((JLabel) c[0]).setHorizontalAlignment(SwingUtilities.CENTER);
//		c[0].setFont(new Font("System", Font.BOLD, width/10));
		c[0].setFont(GameFont.getKarmaticArcade(Font.BOLD, width / 10));
		c[0].setForeground(Color.red);

		c[1] = new Button("Respawn", width);
//		c[2] = new Button("adfasd", width);
//		c[3] = new Button("asdfadsf", width);
//		c[4] = new Button("asdfadsf", width);
		c[5] = new Button("To Main Menu", width);

		((Button) c[1]).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Main.game.gp.respawn();
			}

		});

		((Button) c[5]).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				GamePanel.get().closeGUI();
				Main.game.setState(Game.main_menu);
			}

		});
		int row = 1;
		for (int i = 0; i < com; i++) {
			if (c[i] != null) {
				addComponent(c[i], 1, row);
				row++;
			}
		}

	}

	@Override
	protected void paintOverlay(Graphics2D g) {
		// TODO Auto-generated method stub

	}
}
