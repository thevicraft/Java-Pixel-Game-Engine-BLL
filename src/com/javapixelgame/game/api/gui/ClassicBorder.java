package com.javapixelgame.game.api.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

@SuppressWarnings("serial")
public class ClassicBorder extends AbstractBorder {

	private int borderThickness = 5;
	private float backgroundOpacity = 0.5f;

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D graphics = (Graphics2D) g;
		Rectangle bounds = new Rectangle(x, y, width, height);

		if (backgroundOpacity >= 0 && backgroundOpacity < 1f) {
			// black transparent background
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getBackgroundOpacity()));
		}
//		graphics.setColor(new Color(0, 0, 0, 120));
		graphics.setColor(Color.black);
		graphics.fillRoundRect(bounds.x + getBorderThickness() / 2, bounds.y + getBorderThickness() / 2,
				bounds.width - getBorderThickness(), bounds.height - getBorderThickness(), getBorderThickness() * 4,
				getBorderThickness() * 4);
		if (backgroundOpacity >= 0 && backgroundOpacity < 1f) {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));			
		}

		// border black line filling
		graphics.setStroke(new BasicStroke(getBorderThickness()));
		graphics.setColor(Color.black);
		graphics.drawRoundRect(bounds.x + getBorderThickness() / 2, bounds.y + getBorderThickness() / 2,
				bounds.width - getBorderThickness(), bounds.height - getBorderThickness(), getBorderThickness() * 4,
				getBorderThickness() * 4);

		// border white line filling
		graphics.setStroke(new BasicStroke(getBorderThickness() - 2));
		graphics.setColor(Color.white);
		graphics.drawRoundRect(bounds.x + getBorderThickness() / 2, bounds.y + getBorderThickness() / 2,
				bounds.width - getBorderThickness(), bounds.height - getBorderThickness(), getBorderThickness() * 4,
				getBorderThickness() * 4);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	public int getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
	}

	public float getBackgroundOpacity() {
		return backgroundOpacity;
	}

	public void setBackgroundOpacity(float backgroundOpacity) {
		this.backgroundOpacity = backgroundOpacity;
	}
}
