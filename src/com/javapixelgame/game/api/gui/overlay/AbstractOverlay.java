package com.javapixelgame.game.api.gui.overlay;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.gui.GamePanel;
import com.javapixelgame.game.handling.ConfigHandler;

@SuppressWarnings("serial")
public abstract class AbstractOverlay extends JPanel implements Tickable {
	private GridBagConstraints cgb;

	private boolean disableKeyInputWhenOpen = true;
	private boolean pauseGameWhenOpen = false;
	private boolean focusWhenOpen = true;
	private boolean closeOnKey = true;
	private List<Integer> closeKey = new ArrayList<>();
	private int guiAlignment = SwingConstants.CENTER;

	public AbstractOverlay(int width, int height) {

		setSize(width, height);

		setPreferredSize(getSize());

		setOpaque(false);

		setBackground(Color.black);

		cgb = new GridBagConstraints();

		cgb.fill = GridBagConstraints.HORIZONTAL;

		setLayout(new GridBagLayout());

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(!isCloseOnKey())return;
				for (int code : closeKey) {
					if (code == e.getKeyCode()) {
						GamePanel.get().closeGUI();
						return;
					}
				}
			}
		});

		init();
	}

	public void addCloseKey(int keyCode) {
		closeKey.add(keyCode);
	}

	public void removeCloseKey(int keyCode) {
		closeKey.remove(keyCode);
	}

	protected int borderThickness = 5;

	private boolean open = false;

	public boolean isOpen() {
		return open;
	}

	public void open() {
		if (isOpen())
			return;
		setVisible(true);
		open = true;
		if (focusWhenOpen)
			requestFocusInWindow();
		if (disableKeyInputWhenOpen)
			Main.game.disableKeyInput();
		if (pauseGameWhenOpen)
			GamePanel.get().pauseGame();
		onOpen();
	}

	public void close() {
		if (!isOpen())
			return;
		setVisible(false);
		open = false;
		onClose();
		Main.game.requestFocusInWindow();
		Main.game.enableKeyInput();
		GamePanel.get().continueGame();
	}

	public abstract void onOpen();

	public abstract void onClose();

	public void setOpen(boolean b) {
		if (b) {
			open();
			return;
		}
		close();
	}

	@Override
	protected final void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		Rectangle bounds = graphics.getClipBounds();

		// black transparent background
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
//		graphics.setColor(new Color(0, 0, 0, 120));
		graphics.setColor(Color.black);
		graphics.fillRoundRect(bounds.x + borderThickness / 2, bounds.y + borderThickness / 2,
				bounds.width - borderThickness, bounds.height - borderThickness, borderThickness * 4,
				borderThickness * 4);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// border black line filling
		graphics.setStroke(new BasicStroke(borderThickness));
		graphics.setColor(Color.black);
		graphics.drawRoundRect(bounds.x + borderThickness / 2, bounds.y + borderThickness / 2,
				bounds.width - borderThickness, bounds.height - borderThickness, borderThickness * 4,
				borderThickness * 4);

		// border white line filling
		graphics.setStroke(new BasicStroke(borderThickness - 2));
		graphics.setColor(Color.white);
		graphics.drawRoundRect(bounds.x + borderThickness / 2, bounds.y + borderThickness / 2,
				bounds.width - borderThickness, bounds.height - borderThickness, borderThickness * 4,
				borderThickness * 4);

		paintOverlay((Graphics2D) g);
	}

	public static int getColumnScaledWidth(int width, int columns) {
		float scale_percentage = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.gui_scale)) / 6f;

		return (int) (width * (1d / (double) columns) * Math.sqrt(scale_percentage));
	}

	public void addComponent(JComponent c, int gridx, int gridy) {
		cgb.gridx = gridx;
		cgb.gridy = gridy;
		add(c, cgb);
	}

	public void setComponentAlignment(int variant) {
		cgb.fill = variant;
	}

	public static JLabel getSeparator(int width, int height) {
		JLabel space = new JLabel();
		space.setSize(width, height);
		space.setPreferredSize(space.getSize());
		return space;
	}

	protected abstract void paintOverlay(Graphics2D g);

	protected abstract void init();

	@Override
	public void onRandomTick() {
	}

	public boolean isPauseGameWhenOpen() {
		return pauseGameWhenOpen;
	}

	public void setPauseGameWhenOpen(boolean pauseGameWhenOpen) {
		this.pauseGameWhenOpen = pauseGameWhenOpen;
	}

	public boolean isDisableKeyInputWhenOpen() {
		return disableKeyInputWhenOpen;
	}

	public void setDisableKeyInputWhenOpen(boolean disableKeyInputWhenOpen) {
		this.disableKeyInputWhenOpen = disableKeyInputWhenOpen;
	}

	public int getGuiAlignment() {
		return guiAlignment;
	}

	public void setGuiAlignment(int guiAlignment) {
		this.guiAlignment = guiAlignment;
	}

	public boolean isFocusWhenOpen() {
		return focusWhenOpen;
	}

	public void setFocusWhenOpen(boolean focusWhenOpen) {
		this.focusWhenOpen = focusWhenOpen;
	}

	public boolean isCloseOnKey() {
		return closeOnKey;
	}

	public void setCloseOnKey(boolean closeOnKey) {
		this.closeOnKey = closeOnKey;
	}

}
