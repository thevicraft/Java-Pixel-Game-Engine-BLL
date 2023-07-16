package com.javapixelgame.game.keyboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.component.Button;
import com.javapixelgame.game.gui.component.ConfigComponent;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.keyboard.KeyEventHandler.Keys;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.resourcehandling.GameFont;

public class KeyConfig {

	private static int returnValue;

	public static int showInputDialogue(Component c, Keys key) {

		returnValue = KeyEventHandler.getKeyCode(key);

		JDialog d = new JDialog();
		d.getContentPane().setBackground(Color.DARK_GRAY);
		d.setTitle("Key Input Dialog");

		d.setLayout(new BoxLayout(d.getContentPane(), BoxLayout.Y_AXIS));

		JLabel title = new JLabel("Press favorite key for " + key.toString());
		title.setFont(GameFont.getRainyHearts(Font.BOLD, 36));
		d.add(title);

		JLabel subtitle = new JLabel("Current value: " + KeyEventHandler.getKeyName(key));
		subtitle.setFont(GameFont.getRainyHearts(Font.PLAIN, 36));
		d.add(subtitle);

		Button cancel = new Button("Cancel", 200);
		cancel.addActionListener(a -> d.dispose());
		d.add(cancel);

		for (Component comp : d.getContentPane().getComponents()) {
			comp.setForeground(Color.white);
		}

		d.setLocationRelativeTo(c);

		d.setModal(true);
		d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		d.pack();
		d.setMinimumSize(d.getSize());

		d.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				returnValue = e.getKeyCode();
				if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
					Console.output(key.toString() + " -> " + KeyEvent.getKeyText(e.getKeyCode()) + " (" + e.getKeyCode()
							+ ")");
				d.dispose();
			}
		});

		d.setVisible(true);

		return returnValue;
	}

	public static class KeyButton extends ConfigComponent.ActionButton {

		private Keys variant;

		public KeyButton(Keys key, int width) {
			super("", width, null, null);
			variant = key;
			getFont().deriveFont(Font.BOLD);
			setToolTipText("Click to change key value.");
			addActionListener(a -> {
				KeyEventHandler.setKeyCode(key, KeyConfig.showInputDialogue(Main.game, key));
				setCorrectedText();
			});
			setCorrectedText();
		}

		public void setCorrectedText() {
			setText(variant.toString()+": "+KeyEventHandler.getKeyName(variant));
		}

	}

	public static KeyButton[] getConfigButtons(int width) {
		KeyButton[] buttons = new KeyButton[Keys.values().length];
		Keys[] values = Keys.values();

		for (int i = 0; i < values.length; i++) {
			buttons[i] = new KeyButton(values[i], width);
		}

		return buttons;
	}

	public static KeyButton getConfigButton(Keys key, int width) {
		return new KeyButton(key, width);
	}

}
