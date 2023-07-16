package com.javapixelgame.game.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.javapixelgame.game.gui.AbstractGUI;

@SuppressWarnings("serial")
public class ScrollPane extends JScrollPane {

	private AbstractGUI contentPane;

	public ScrollPane(int width, int height, int contentHeight) {
		setSize(width, height);
		setPreferredSize(getSize());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setOpaque(false);
		setBackground(Color.black);
		getVerticalScrollBar().setBackground(Color.BLACK);
		getVerticalScrollBar().setForeground(Color.white);

		setBorder(null);
		setViewportBorder(null);

		getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			private JButton createZeroButton() {
				JButton jbutton = new JButton();
				jbutton.setPreferredSize(new Dimension(0, 0));
				jbutton.setMinimumSize(new Dimension(0, 0));
				jbutton.setMaximumSize(new Dimension(0, 0));
				return jbutton;
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				g.setColor(Color.WHITE);
				g.fillRect(r.x, r.y, r.width, r.height);
				g.drawRoundRect(r.x, r.y, r.width, r.height, r.width, r.width);
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
				g.setColor(Color.DARK_GRAY);
				g.fillRect(r.x + r.width / 4, r.y + r.width / 2, r.width / 2, r.height - r.width / 2);
			}
		});

		contentPane = new AbstractGUI(getViewportBorderBounds().width, contentHeight) {

			@Override
			protected void paintOverlay(Graphics2D g) {
				return;
			}

			@Override
			protected void init() {
				return;
			}
		};
		contentPane.setSize(getViewportBorderBounds().width, contentHeight);
		contentPane.setPreferredSize(contentPane.getSize());
		contentPane.setBackground(Color.black);
		contentPane.setOpaque(true);
		setViewportView(contentPane);
		getVerticalScrollBar().setUnitIncrement(contentPane.getHeight() / 100);

	}

	/**
	 * Gets the JPanel that is within the ScrollPane. Here you can add thinks and
	 * change the Layout etc.
	 * 
	 * @return JPanel - contentPane
	 */
	public AbstractGUI getContentPane() {
		return contentPane;
	}
}
