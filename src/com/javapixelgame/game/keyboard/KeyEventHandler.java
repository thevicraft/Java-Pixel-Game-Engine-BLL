package com.javapixelgame.game.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.gui.Game;
import com.javapixelgame.game.gui.GamePanel;

public class KeyEventHandler {
//	public static int KEY_UP = KeyEvent.VK_W;
//	public static int KEY_DOWN = KeyEvent.VK_S;
//	public static int KEY_LEFT = KeyEvent.VK_A;
//	public static int KEY_RIGHT = KeyEvent.VK_D;
//	public static int KEY_ATTACK = KeyEvent.VK_SPACE;
//	public static int KEY_SWAP_ITEM = KeyEvent.VK_Q;

	public static MainEvent[] keys;

	public static final KeyListener game_pause = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE && Main.game.state == Game.in_game) {
				if (GamePanel.get().isOpenGUI() && GamePanel.get().getCurrentGUI().isCloseOnKey())
					GamePanel.get().closeGUI();
				else
					Main.game.gp.toggleGameState();
			}
		}
	};

	public static final SingleKeyListener tab_switch = new SingleKeyListener(KeyEvent.VK_CONTROL) {

		@Override
		public void typed(KeyEvent e) {
		}

		@Override
		public void released(KeyEvent e) {
			if (!Main.game.gp.isRunning())
				return;
			Main.game.gp.tab_switch = false;
		}

		@Override
		public void pressed(KeyEvent e) {
			if (!Main.game.gp.isRunning())
				return;
			Main.game.gp.tab_switch = true;
		}
	};

	public enum Keys {
		up, down, left, right, attack, swap_item, throw_item, minimap_zoom_in, minimap_zoom_out, open_chat, interact, inventory
	}

	public static String[][] config = {

			{ "up", Integer.toString(KeyEvent.VK_W), "p" }, 
			{ "down", Integer.toString(KeyEvent.VK_S), "p" },
			{ "left", Integer.toString(KeyEvent.VK_A), "p" }, 
			{ "right", Integer.toString(KeyEvent.VK_D), "p" },
			{ "attack", Integer.toString(KeyEvent.VK_SPACE), "p" },
			{ "swap_item", Integer.toString(KeyEvent.VK_R), "t" },
			{ "throw_item", Integer.toString(KeyEvent.VK_Q), "t" },
			{ "minimap_zoom_in", Integer.toString(KeyEvent.VK_Z), "t" },
			{ "minimap_zoom_out", Integer.toString(KeyEvent.VK_U), "t" },
			{ "open_chat", Integer.toString(KeyEvent.VK_ENTER), "t" },
			{ "interact", Integer.toString(KeyEvent.VK_F), "t" },
			{ "inventory", Integer.toString(KeyEvent.VK_E), "t" },
	};

	private KeyEventHandler() {
	}

	public static void init(Game game) {

		keys = new MainEvent[config.length];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new MainEvent(Integer.parseInt(config[i][1]), config[i][2].equals("p"), config[i][0]);
		}

		for (MainEvent e : keys) {
			game.addKeyListener(e);
		}

		game.addKeyListener(tab_switch);

	}

	public static void updateKeyEvents() {
		for (int i = 0; i < keys.length; i++) {
			keys[i].update(Integer.parseInt(config[i][1]));
		}
	}

	public static void updateKeyEvent(Keys key) {
		int i = getConfigIndex(key);
		keys[i].update(Integer.parseInt(config[i][1]));
	}

	public static final void loadConfig() {
		if (!new File("key.txt").exists()) {
			saveConfig();
			return;
		}
		try {
			BufferedReader buff = new BufferedReader(new FileReader("key.txt"));
			for (int i = 0; i < config.length; i++) {
				applySetting(buff.readLine());
			}
			buff.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void applySetting(String s) {
		String[] str = s.split("=");
		String category = str[0].trim();
		String value = str[1].trim();
		for (int i = 0; i < config.length; i++) {
			if (config[i][0].equals(category)) {
				config[i][1] = value;
				break;
			}
		}
	}

	public static final void saveConfig() {

		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter("key.txt"));

			for (int i = 0; i < config.length; i++) {
				buff.write(config[i][0] + "=" + config[i][1]);
				buff.newLine();
			}

			buff.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPressed(Keys key) {
		return getKeyEvent(key).pressed;
	}

	public static boolean isTyped(Keys key) {
		return getKeyEvent(key).isTyped();
	}

	public static String getState(Keys key) {
		return getKeyEvent(key).state;
	}

	public static String getKeyName(Keys key) {
		return getKeyEvent(key).keyname;
	}

	public static int getKeyCode(Keys key) {
		return getKeyEvent(key).keyCode;
	}

	public static void setKeyCode(Keys key, int code) {
		config[getConfigIndex(key)][1] = Integer.toString(code);
		updateKeyEvent(key);
	}

	private static int getConfigIndex(Keys key) {
		for (int i = 0; i < config.length; i++) {
			if (key.toString().equals(keys[i].getID())) {
				return i;
			}
		}
		return -1;
	}

	private static MainEvent getKeyEvent(Keys key) {
		return keys[getConfigIndex(key)];
	}
}
