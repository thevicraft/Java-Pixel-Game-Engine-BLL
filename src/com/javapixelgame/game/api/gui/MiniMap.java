package com.javapixelgame.game.api.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.handling.GameHandler;
import com.javapixelgame.game.util.ImageUtil;

@SuppressWarnings("serial")
public class MiniMap extends JPanel implements Tickable {

//	private World world;
//	private BufferedImage map;

//	private BufferedImage mapQualityStore;

	private int playerDot;

	private int mapXLength;
	private int mapYLength;
	private int border = 10;
	private JPanel mapdisplay;
	private Color[] c;
	private Color borderColor = Color.black;
	private int pxMapMeter;

	private BufferedImage map_image;
	private BufferedImage toDraw;
	private int scaled_width;
	private int scaled_height;
	private double percent;
	private int min_size;

	public MiniMap(int x, int y, int pxMapMeter, int width, int height) {

		setLocation(x, y);
		setSize(width, height);

		setLayout(null);
		setOpaque(false);

		playerDot = pxMapMeter;
		this.pxMapMeter = pxMapMeter;

		map_image = ImageUtil.getFormatBufferedImage(GameHandler.getSize().width, GameHandler.getSize().height,
				Transparency.OPAQUE);
		percent = (double) MiniMap.this.pxMapMeter / (double) GameHandler.getWorld().getPixelLength(1);

		scaled_width = (int) (GameHandler.getSize().width * percent);
		scaled_height = (int) (GameHandler.getSize().height * percent);

		toDraw = ImageUtil.getFormatBufferedImage(scaled_width, scaled_height, Transparency.OPAQUE);

		toDraw.createGraphics().drawImage(map_image, getWidth() / 2 - scaled_width / 2,
				getHeight() / 2 - scaled_height / 2, scaled_width, scaled_height, this);

		min_size = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.gui_scale)) * 2 + 4;

		mapdisplay = new JPanel() {
			@Override
			public boolean isOpaque() {
				return false;
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D graphics = (Graphics2D) g;

				graphics.setClip(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));

				graphics.drawImage(toDraw, 0, 0, this);

				graphics.setColor(Color.red);

				Triangle_Shape triangleShape = null;

				switch (GameHandler.getWorld().getPlayer().getSkinDirection()) {
				case Skin.UP:
					triangleShape = new Triangle_Shape(
							new Point2D.Double(getWidth() / 2 + playerDot / 2, getHeight() / 2 + playerDot / 2),
							new Point2D.Double(getWidth() / 2 - playerDot / 2, getHeight() / 2 + playerDot / 2),
							new Point2D.Double(getWidth() / 2, getHeight() / 2 - playerDot / 2));

					break;
				case Skin.DOWN:
					triangleShape = new Triangle_Shape(
							new Point2D.Double(getWidth() / 2 + playerDot / 2, getHeight() / 2 - playerDot / 2),
							new Point2D.Double(getWidth() / 2 - playerDot / 2, getHeight() / 2 - playerDot / 2),
							new Point2D.Double(getWidth() / 2, getHeight() / 2 + playerDot / 2));

					break;
				case Skin.LEFT:
					triangleShape = new Triangle_Shape(
							new Point2D.Double(getWidth() / 2 + playerDot / 2, getHeight() / 2 + playerDot / 2),
							new Point2D.Double(getWidth() / 2 + playerDot / 2, getHeight() / 2 - playerDot / 2),
							new Point2D.Double(getWidth() / 2 - playerDot / 2, getHeight() / 2));

					break;
				case Skin.RIGHT:
					triangleShape = new Triangle_Shape(
							new Point2D.Double(getWidth() / 2 - playerDot / 2, getHeight() / 2 + playerDot / 2),
							new Point2D.Double(getWidth() / 2 - playerDot / 2, getHeight() / 2 - playerDot / 2),
							new Point2D.Double(getWidth() / 2 + playerDot / 2, getHeight() / 2));

					break;
				}

				graphics.fill(triangleShape);

			}
		};

		mapdisplay.setSize(getWidth() - border * 2, getHeight() - border * 2);
		mapdisplay.setLocation(border, border);
		add(mapdisplay);

		List<Color> colors = new ArrayList<Color>();
		for (int r = 0; r < 100; r++)
			colors.add(new Color(r * 255 / 100, 255, 0));
		for (int g = 100; g > 0; g--)
			colors.add(new Color(255, g * 255 / 100, 0));
		for (int b = 0; b < 100; b++)
			colors.add(new Color(255, 0, b * 255 / 100));
		for (int r = 100; r > 0; r--)
			colors.add(new Color(r * 255 / 100, 0, 255));
		for (int g = 0; g < 100; g++)
			colors.add(new Color(0, g * 255 / 100, 255));
		for (int b = 100; b > 0; b--)
			colors.add(new Color(0, 255, b * 255 / 100));
		colors.add(new Color(0, 255, 0));

		c = colors.toArray(new Color[colors.size()]);

		zoom(this.pxMapMeter);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;

		graphics.setClip(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
		graphics.setColor(borderColor);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}

	private int rgbcycle = 0;

	@Override
	public void onWorldTick(int tick) {
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
//				if (tick % 2 == 0) {
//					GameHandler.getWorld().paint(map_image.createGraphics());					
//					updateMap();					
//				}
//
//			}
//		});
//		rgbcycle++;
//		if (rgbcycle >= c.length)
//			rgbcycle = 0;
//		borderColor = c[rgbcycle];
//		
//		mapdisplay.repaint();
		if (tick % 2 != 0)
			return;

		new Thread(new Runnable() {

			@Override
			public void run() {

				SwingUtilities.invokeLater(() -> GameHandler.getWorld().paint(map_image.createGraphics()));

//				GameHandler.getWorld().paint(map_image.createGraphics());
				
				updateMap();
				rgbcycle++;
				if (rgbcycle >= c.length)
					rgbcycle = 0;
				borderColor = c[rgbcycle];

				mapdisplay.repaint();

			}

		}).start();

	}

	public void updateMap() {
		toDraw.createGraphics().drawImage(map_image, mapdisplay.getWidth() / 2 - scaled_width / 2,
				mapdisplay.getHeight() / 2 - scaled_height / 2, scaled_width, scaled_height, this);
	}

	public void zoomIn(int value) {
		this.pxMapMeter += Math.abs(value);
		if (pxMapMeter > 40)
			pxMapMeter = 40;

		resize();
	}

	public void zoomOut(int value) {
		this.pxMapMeter -= Math.abs(value);
		if (pxMapMeter < min_size)
			pxMapMeter = min_size;
		resize();

	}

	private void resize() {
		percent = (double) MiniMap.this.pxMapMeter / (double) GameHandler.getWorld().getPixelLength(1);
		scaled_width = (int) (GameHandler.getSize().width * percent);
		scaled_height = (int) (GameHandler.getSize().height * percent);

		toDraw = ImageUtil.getFormatBufferedImage(scaled_width, scaled_height, Transparency.OPAQUE);
		updateMap();
	}

	public void zoom(int value) {
		if (value > 0)
			zoomIn(value);
		else if (value < 0) {
			zoomOut(value);
		}
	}

//	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
//		int w = img.getWidth();
//		int h = img.getHeight();
//		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
//		Graphics2D g = dimg.createGraphics();
//		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
//		g.dispose();
//		return dimg;
//	}

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub

	}

	static class Triangle_Shape extends Path2D.Double {
		public Triangle_Shape(Point2D... points) {
			moveTo(points[0].getX(), points[0].getY());
			lineTo(points[1].getX(), points[1].getY());
			lineTo(points[2].getX(), points[2].getY());
			closePath();
		}
	}
}
