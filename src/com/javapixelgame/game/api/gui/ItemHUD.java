package com.javapixelgame.game.api.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.tileentity.Inventory;

@SuppressWarnings("serial")
public class ItemHUD extends JPanel implements Tickable {

	private int width;
	private int height;

	private int xBorder;
	private int yBorder;

	private int maxIconWidth;
	private int maxIconHeight;
	private Color border = new Color(97, 97, 97);
	private Color borderSelected = Color.cyan;
	private Color center = new Color(168, 168, 168);
	private int slot;
	private Inventory inventory;
	private boolean selected;
	private boolean backgroundVisible = true;

	// the shape is squared!
	public ItemHUD(Inventory inventory, int size, int slot) {
		this.width = size;
		this.height = size;
		this.slot = slot;
		this.inventory = inventory;

		setSize(size, size);
		setPreferredSize(new Dimension(size, size));
		setOpaque(false);
		setBackground(new Color(0, 0, 0, 0));
//		setLayout(null);
		xBorder = (int) (width * 0.1);
		yBorder = (int) (height * 0.1);

		maxIconWidth = this.width - 2 * xBorder;
		maxIconHeight = this.height - 2 * yBorder;
	}

	@Override
	protected final void paintComponent(Graphics g) {
		Graphics2D pane = (Graphics2D) g;

		if (isBackgroundVisible()) {
			pane.setColor(isSelected() ? getSelectedBorderColor() : border);

			pane.fillRoundRect(0, 0, width, height, xBorder * 2, yBorder * 2);
			pane.setColor(center);
			pane.fillRect(xBorder, yBorder, width - 2 * xBorder, height - 2 * yBorder);
		}

		Item draw = getCorrespondingItem();

		if (draw == null)
			return;

		pane.rotate(Math.toRadians(45), width / 2, height / 2);

		drawItem(draw, pane);
	}

	protected Item getCorrespondingItem() {
		return getInventory().getItem(getSlotNumber());
	}

	protected void drawItem(Item draw, Graphics2D graphics) {
		if (draw == null)
			return;

		ImageIcon icon = draw.getTexture().getDisplayIcon();

		double aspect = draw.getTexture().getAspect();

		if (aspect > 1) {
			// width is longer than height
			graphics.drawImage(icon.getImage(), xBorder, (int) (height / 2 - (maxIconWidth / aspect) / 2), maxIconWidth,
					(int) (maxIconWidth / aspect), this);
		} else if (aspect < 1) {
			// height is longer than width
			graphics.drawImage(icon.getImage(), (int) (width / 2 - (maxIconHeight * aspect) / 2), yBorder,
					(int) (maxIconHeight * aspect), maxIconHeight, this);
		} else {
			graphics.drawImage(icon.getImage(), xBorder, yBorder, maxIconWidth, maxIconHeight, this);
		}

	}

	@Override
	public void onWorldTick(int tick) {

		repaint();

	}

	@Override
	public void onRandomTick() {
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getSlotNumber() {
		return slot;
	}

	public Color getSelectedBorderColor() {
		return borderSelected;
	}

	public void setSelectedBorderColor(Color borderSelected) {
		this.borderSelected = borderSelected;
	}

	public boolean isBackgroundVisible() {
		return backgroundVisible;
	}

	public void setBackgroundVisible(boolean backgroundVisible) {
		this.backgroundVisible = backgroundVisible;
	}

	public Inventory getInventory() {
		return inventory;
	}

}
