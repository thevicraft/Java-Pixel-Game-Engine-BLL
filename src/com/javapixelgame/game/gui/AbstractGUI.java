package com.javapixelgame.game.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.handling.ConfigHandler;

@SuppressWarnings("serial")
public abstract class AbstractGUI extends JPanel {

	private GridBagConstraints cgb;

	private Texture texture;

	private Image background;

	private int width;
	private int height;

	public AbstractGUI(int width, int height) {
		this.width = width;
		this.height = height;

		setSize(width, height);

		setPreferredSize(getSize());

		setOpaque(false);

		cgb = new GridBagConstraints();

		cgb.fill = GridBagConstraints.HORIZONTAL;

		setLayout(new GridBagLayout());

		init();
	}
	
	public static int getColumnScaledWidth(int width, int columns) {
		float scale_percentage = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.gui_scale)) / 6f;

		return (int) (width * (1d/(double)columns)* Math.sqrt(scale_percentage));
	}
	
	public void addComponent(JComponent c, int gridx, int gridy) {
		cgb.gridx = gridx;
		cgb.gridy = gridy;
		add(c,cgb);
	}
	
	public void setComponentAlignment(int variant) {
		cgb.fill = variant;
	}

	public void setBackgroundImage(Texture texture) {
		this.texture = texture;

		if (width > height) {
			this.background = texture.getImage().getScaledInstance(texture.getWidthOnRatio(height), height,
					Image.SCALE_SMOOTH);

		}
		if (width < height) {
			this.background = texture.getImage().getScaledInstance(width, texture.getHeightOnRatio(width),
					Image.SCALE_SMOOTH);
		}
	}
	
	public static JLabel getSeparator(int width, int height) {
		JLabel space = new JLabel();
		space.setSize(width, height);
		space.setPreferredSize(space.getSize());
		return space;
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (texture != null)
			drawScaledBackground(width, height, background, (Graphics2D) g, this);

		super.paintComponent(g);
		paintOverlay((Graphics2D)g);
	}
	
	protected abstract void paintOverlay(Graphics2D g);

	protected abstract void init();

	public static void drawScaledBackground(int width, int height, Image image, Graphics2D g, ImageObserver obs) {
		int imgWidth = image.getWidth(null);
		int imgHeight = image.getHeight(null);

		double imgAspect = (double) imgHeight / imgWidth;

		int canvasWidth = width;
		int canvasHeight = height;

		double canvasAspect = (double) canvasHeight / canvasWidth;

		int x1 = 0; // top left X position
		int y1 = 0; // top left Y position
		int x2 = 0; // bottom right X position
		int y2 = 0; // bottom right Y position

		if (imgWidth < canvasWidth && imgHeight < canvasHeight) {
			// the image is smaller than the canvas
			x1 = (canvasWidth - imgWidth) / 2;
			y1 = (canvasHeight - imgHeight) / 2;
			x2 = imgWidth + x1;
			y2 = imgHeight + y1;

		} else {
			if (canvasAspect > imgAspect) {
				y1 = canvasHeight;
				// keep image aspect ratio
				canvasHeight = (int) (canvasWidth * imgAspect);
				y1 = (y1 - canvasHeight) / 2;
			} else {
				x1 = canvasWidth;
				// keep image aspect ratio
				canvasWidth = (int) (canvasHeight / imgAspect);
				x1 = (x1 - canvasWidth) / 2;
			}
			x2 = canvasWidth + x1;
			y2 = canvasHeight + y1;
		}

		g.drawImage(image, x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, obs);
	}
}
