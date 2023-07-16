package com.javapixelgame.game.api.gui.inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import com.javapixelgame.game.api.gui.ClassicBorder;
import com.javapixelgame.game.resourcehandling.GameFont;

public class ItemToolTip  extends JToolTip {

	private static final long serialVersionUID = 1L;
	
	private ClassicBorder border = new ClassicBorder();

	public ItemToolTip(JComponent component, Color background, Color foreground, float size) {
		super();
		setComponent(component);
		setBackground(background);
		setForeground(foreground);
		
		setOpaque(false);
		
		border.setBorderThickness(3);
		border.setBackgroundOpacity(1f);
		
		setBorder(border);
		setFont(GameFont.getRainyHearts(Font.ITALIC, size));
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		return;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintBorder(g);
		super.paintComponent(g);
	}

}
