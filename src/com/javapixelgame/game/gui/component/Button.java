package com.javapixelgame.game.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.resourcehandling.GameFont;
import com.javapixelgame.game.resourcehandling.TextureID;

@SuppressWarnings("serial")
public class Button extends JButton {

	private ImageIcon focused;
	private ImageIcon unfocused;
	private ImageIcon overlay;

	public Button(String text, int width) {
		setText(text);
		prepare(width);
	}

//	public Button(String text) {
//		setText(text);
//		prepare();
//	}

	private void prepare(int width) {
		setForeground(Color.white);
		setBackground(new Color(0, 0, 0, 0));
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setOpaque(false);
		setFocusable(false);

		Texture f = new Texture(TextureID.BUTTON_FOCUSED);
		Texture uf = new Texture(TextureID.BUTTON);

		setSize(width, uf.getHeightOnRatio(width));
		setPreferredSize(getSize());

		focused = f.getIcon();
		unfocused = uf.getIcon();
		overlay = unfocused;
		setHorizontalTextPosition(SwingConstants.CENTER);

//		setFont(new Font("System", Font.BOLD, getHeight() / 2));

		setFont(GameFont.getArcadeClassic(Font.BOLD, getHeight() * 0.7f));

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				overlay = unfocused;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				overlay = focused;
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				Main.game.sound.play(SoundID.okeee, false);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {

//		setIcon(getMousePosition() != null? focused: unfocused);

//		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		graphics.drawImage(overlay.getImage(), 0, 0, getWidth(), getHeight(), this);

		super.paintComponent(graphics);
//		graphics.drawImage(unfocused.getImage(), 0, 0, getWidth() / 2, getHeight(), 0, 0, unfocused.getIconWidth() / 2,
//				unfocused.getIconHeight(), this);
//
//		graphics.drawImage(unfocused.getImage(), getWidth() / 2, 0, getWidth(), getHeight(),
//				unfocused.getIconWidth() / 2, 0, unfocused.getIconWidth(), unfocused.getIconHeight(), this);

//		graphics.drawImage(getMousePosition() != null ? focused.getImage() : unfocused.getImage(), 0, 0,
//				getMousePosition() != null ? focused.getIconWidth() : unfocused.getIconWidth(),
//				getMousePosition() != null ? focused.getIconHeight() : unfocused.getIconHeight(), this);

//		if(getMousePosition() != null) {
//			graphics.drawImage(focused.getImage(), 0, 0, getWidth(),getHeight(),this);
//		} else {
//			graphics.drawImage(unfocused.getImage(), 0, 0,  getWidth(),getHeight(),this);
//		}

	}

}
