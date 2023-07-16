package com.javapixelgame.game.api.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.resourcehandling.GameFont;

@SuppressWarnings("serial")
public class InfoBox extends JTextArea implements Tickable {

//	private SimpleAttributeSet attributeSet = new SimpleAttributeSet();

	private static final Color textColor = new Color(255, 215, 0);

//	public static final int columns = 6;

	public InfoBox(int width, int height, int xcenter, int ycenter, int columns) {
		setSize(width, height);
		setPreferredSize(getSize());
		setOpaque(false);
		setLayout(null);
		setLocation(xcenter - getWidth() / 2, ycenter - getHeight() / 2);

		setBorder(null);
		setFocusable(false);
		setEditable(false);
		getCaret().deinstall(this);
		setFont(GameFont.getRainyHearts(Font.PLAIN, getHeight() / columns));

		setOpaque(false);
		setBackground(new Color(0, 0, 0, 100));
		setForeground(textColor);
		setColumns(columns);
//		Console.output(columns);
		setLineWrap(true);
		setWrapStyleWord(true);

		setText("");
		setVisible(false);
	}

	public void display(String message, int ticks) {
		SwingUtilities.invokeLater(() -> {
			setText(message);
			time = ticks;
			setVisible(true);
		});
	}

//	private void extend(String text) {
//		if(getColumnWidth() <= getFontMetrics(getFont()).stringWidth(text)) {
//			setColumns(columns);
//		}
//	}

	private int time = 0;

//	public void display(ChatMessage message, int ticks) {
//		time = ticks;
////		setCharacterAttributes(attributeSet, true);
//		
//		Document doc = getDocument();
//		try {
//			doc.remove(0, doc.getLength());
//		} catch (BadLocationException e1) {
//		}
//		StyleConstants.setBold(attributeSet, true);
//		StyleConstants.setForeground(attributeSet, Color.red);
//		StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
//
//		if (message.getSender() != "") {
//			try {
//				doc.insertString(doc.getLength(), message.getSender(), attributeSet);
//			} catch (BadLocationException e) {
//			}
//		}
//		StyleConstants.setForeground(attributeSet, getForeground());
//		try {
//			doc.insertString(doc.getLength(),
//					(message.getSender() != "" ? ": " : "") + message.getMessage(), attributeSet);
//		} catch (BadLocationException e) {
//		}
//		setVisible(true);
//
//		
//	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		Rectangle r = g.getClipBounds();
		g.fillRect(r.x, r.y, r.width, r.height);
		super.paintComponent(g);
	}

	@Override
	public void onWorldTick(int tick) {
		// TODO Auto-generated method stub
		time--;
		if (time <= 0) {

			time = 0;
			setText("");
			setVisible(false);

		}
	}

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub

	}

}
