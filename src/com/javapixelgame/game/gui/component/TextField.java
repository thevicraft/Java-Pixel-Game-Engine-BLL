package com.javapixelgame.game.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class TextField extends JTextField{
	
	private Color lineBackground = new Color(0, 0, 0, 100);
	private Color textColor = new Color(255, 215, 0);
	
	private boolean linedBorder = false;
	
	public TextField(int width, int height) {
		
		setSize(width, height);
		setPreferredSize(getSize());
		setFont(GameFont.getRainyHearts(Font.PLAIN, height));
		setOpaque(false);

		setBackground(lineBackground);
		setBorder(null);
		setForeground(textColor);
		setCaret(new ChatCaret());
		setCaretColor(Color.white);
	}
	
	@Override
	public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        Rectangle r = g.getClipBounds();
        g.fillRect(r.x, r.y, r.width, r.height);
        super.paintComponent(g);
    }
	
	
	public boolean isLinedBorder() {
		return linedBorder;
	}

	public void setLinedBorder(boolean linedBorder) {
		this.linedBorder = linedBorder;
		if(isLinedBorder())
			setBorder(BorderFactory.createLineBorder(Color.white, getHeight()/20, true));
		else
			setBorder(null);
	}

	private class ChatCaret extends DefaultCaret {

		private String mark = "_";
		private Font use = new Font("System", Font.BOLD, (int) (TextField.this.getHeight() * 0.8));
		// GameFont.getKarmaticArcade(Font.PLAIN, inputField.getHeight()*0.8f);

		public ChatCaret() {
			setBlinkRate(500);
		}

		@Override
		protected synchronized void damage(Rectangle r) {
			if (r == null) {
				return;
			}

			JTextComponent comp = getComponent();
//	            FontMetrics fm = comp.getFontMetrics(comp.getFont());
			FontMetrics fm = comp.getFontMetrics(use);
			int textWidth = fm.stringWidth(mark);
			int textHeight = fm.getHeight();
			x = r.x;
			y = r.y;
			width = textWidth;
			height = textHeight;
			repaint(); // calls getComponent().repaint(x, y, width, height)
		}

		@Override
		public void paint(Graphics g) {
			JTextComponent comp = getComponent();
			if (comp == null) {
				return;
			}

			int dot = getDot();
			Rectangle r = null;
			try {
				r = comp.modelToView(dot);
			} catch (BadLocationException e) {
				return;
			}
			if (r == null) {
				return;
			}

			if ((x != r.x) || (y != r.y)) {
				repaint(); // erase previous location of caret
				damage(r);
			}

			if (isVisible()) {
				FontMetrics fm = comp.getFontMetrics(use);
				g.setFont(use);

				g.setColor(comp.getCaretColor());
				g.drawString(mark, x, y + fm.getAscent());
			}
		}

	}
	
}
