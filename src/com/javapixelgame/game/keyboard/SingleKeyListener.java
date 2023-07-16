package com.javapixelgame.game.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Abstract {@link KeyListener} for handling <b>single</b> key events.
 * 
 * @author thevicraft
 *
 */
public abstract class SingleKeyListener implements KeyListener {

	private int singleKey;

	/**
	 * Sets a default key. This key will be saved and used as condition on
	 * <i>pressed(), released(), typed()</i> events.
	 * 
	 * @param key
	 */
	public SingleKeyListener(int key) {
		setSingleKey(key);
	}

	@Override
	public final void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == getSingleKey())
			pressed(e);
	}

	@Override
	public final void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == getSingleKey())
			released(e);
	}

	@Override
	public final void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == getSingleKey())
			typed(e);
	}

	/**
	 * Executed when the single key in the <b>constructor</b> is pressed.
	 * 
	 * @param e - the single key of the <b>constructor</b>
	 */
	public abstract void pressed(KeyEvent e);

	/**
	 * Executed when the single key in the <b>constructor</b> is released.
	 * 
	 * @param e - the single key of the <b>constructor</b>
	 */
	public abstract void released(KeyEvent e);

	/**
	 * Executed when the single key in the <b>constructor</b> is typed.
	 * 
	 * @param e - the single key of the <b>constructor</b>
	 */
	public abstract void typed(KeyEvent e);

	public final int getSingleKey() {
		return singleKey;
	}

	public final void setSingleKey(int singleKey) {
		this.singleKey = singleKey;
	}

	/**
	 * Abstract {@link KeyListener} for handling <b>single</b> key events.
	 * </p>
	 * It implements <b>only</b> the <i>released()</i> method!
	 */
	public static abstract class Released extends SingleKeyListener {
		/**
		 * Abstract {@link KeyListener} for handling <b>single</b> key events.
		 * </p>
		 * It implements <b>only</b> the <i>released()</i> method!
		 */
		public Released(int code) {
			super(code);
		}

		@Override
		public void pressed(KeyEvent e) {
		}

		@Override
		public void typed(KeyEvent e) {
		}

	}

	/**
	 * Abstract {@link KeyListener} for handling <b>single</b> key events.
	 * </p>
	 * It implements <b>only</b> the <i>pressed()</i> method!
	 */
	public static abstract class Pressed extends SingleKeyListener {
		/**
		 * Abstract {@link KeyListener} for handling <b>single</b> key events.
		 * </p>
		 * It implements <b>only</b> the <i>pressed()</i> method!
		 */
		public Pressed(int code) {
			super(code);
		}

		@Override
		public void typed(KeyEvent e) {
		}

		@Override
		public void released(KeyEvent e) {
		}

	}

	/**
	 * Abstract {@link KeyListener} for handling <b>single</b> key events.
	 * </p>
	 * It implements <b>only</b> the <i>typed()</i> method!
	 */
	public static abstract class Typed extends SingleKeyListener {
		/**
		 * Abstract {@link KeyListener} for handling <b>single</b> key events.
		 * </p>
		 * It implements <b>only</b> the <i>typed()</i> method!
		 */
		public Typed(int code) {
			super(code);
		}

		@Override
		public void pressed(KeyEvent e) {
		}

		@Override
		public void released(KeyEvent e) {
		}

	}

}
