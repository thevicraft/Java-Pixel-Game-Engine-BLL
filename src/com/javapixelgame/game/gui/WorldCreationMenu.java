package com.javapixelgame.game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.component.ConfigComponent;
import com.javapixelgame.game.gui.component.TextField;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.keyboard.SingleKeyListener;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class WorldCreationMenu extends AbstractGUI {

	private TextField inputField;
	
	private ConfigComponent.ActionButton create_world;

	public WorldCreationMenu(int width, int height) {
		super(width, height);
	}

	@Override
	protected void paintOverlay(Graphics2D g) {}

	@Override
	protected void init() {
		int width = getColumnScaledWidth(getWidth(), 3);

		JLabel title = new JLabel("Create new World");
		title.setOpaque(false);
		title.setPreferredSize(new Dimension(width, width / 10));
		title.setForeground(Color.white);
		title.setHorizontalAlignment(SwingUtilities.CENTER);
		title.setFont(GameFont.getRainyHearts(Font.BOLD, width / 10));
		title.setForeground(Color.red);

		ConfigComponent.ActionButton back = new ConfigComponent.ActionButton("back", width, ar -> {
			Main.game.setState(Game.main_menu);
		}, null);

		create_world = new ConfigComponent.ActionButton("Create World", width, e -> {

			GameHandler.loadTestMap(inputField.getText().trim().replaceAll(" ", "_"));
			Main.game.gp.updateGUI();
			Main.game.setState(Game.in_game);
		}, null);
		
		// ----------------------------------
		allowCreate(false);
		// ----------------------------------
		
		inputField = new TextField(width, back.getHeight());

		inputField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				allowCreate(inputField.getText().length() > 0);
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		inputField.addKeyListener(new SingleKeyListener.Released(KeyEvent.VK_ESCAPE) {
			
			@Override
			public void released(KeyEvent e) {
				Main.game.setState(Game.main_menu);				
			}
		});
		
		inputField.setLinedBorder(true);

		addComponent(title, 2, 1);

		addComponent(getSeparator(width, back.getHeight() * 2), 2, 2);
		addComponent(inputField, 2, 3);
		addComponent(getSeparator(width, back.getHeight() * 4), 2, 4);

		addComponent(back, 1, 8);
		addComponent(create_world, 3, 8);
	}
	
	private void allowCreate(boolean b) {
		create_world.setEnabled(b);
		if(b) {
			create_world.setToolTipText("");
		} else {
			create_world.setToolTipText("Type in valid world name to continue.");
		}
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			SwingUtilities.invokeLater(() -> {
				inputField.setText("");
				inputField.setVisible(true);
				inputField.setEnabled(true);
				inputField.requestFocusInWindow();
			});
		} else {
			SwingUtilities.invokeLater(() -> {
				inputField.setText("");
				inputField.setVisible(false);
				inputField.setEnabled(false);
				if (Main.game != null)
					Main.game.requestFocusInWindow();
			});
		}

	}

}
