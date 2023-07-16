package com.javapixelgame.game.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainEvent implements KeyListener {

	public String state = "idle";
	public int keyCode;
	public boolean pressed;
	private boolean typed;
	private boolean holdKey;
	private String ID;
	public String keyname;
	
	
	public MainEvent(int keyCode, boolean holdKey, String id) {
		this.keyCode = keyCode;
		this.holdKey = holdKey;
		this.ID = id;
		keyname = KeyEvent.getKeyText(keyCode);
	}
	
	public void update(int keyCode) {
		this.keyCode = keyCode;
		keyname = KeyEvent.getKeyText(keyCode);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == keyCode) {
			if (holdKey) {
				state = "pressed";
				pressed = true;
			} else {
				typed = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == keyCode) {
			if (holdKey) {
				state = "idle";
				pressed = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public boolean isTyped() {
		boolean store = typed;
		typed = false;
		return store;
	}

	public String getID() {
		return ID;
	}

}