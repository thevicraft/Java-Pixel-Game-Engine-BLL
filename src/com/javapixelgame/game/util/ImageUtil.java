package com.javapixelgame.game.util;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class ImageUtil {
	private ImageUtil() {
	}

	public static boolean valid(int width, int height) {
		return (width > 0) && (height > 0);
	}

	public static BufferedImage toBufferedImage(Image image, int imageType) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return img;
	}

	public static BufferedImage scale(Image image, int width, int height, int imageType) {

		if (!valid(width, height))
			return toBufferedImage(image, imageType);

		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return img;
	}

	public static BufferedImage scaleFactorial(Image image, double factor, int imageType) {
		int width = (int) (image.getWidth(null) * factor);
		int height = (int) (image.getHeight(null) * factor);
		if (!valid(width, height))
			return toBufferedImage(image, imageType);
		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return img;
	}

	public static BufferedImage scaleToWidth(Image image, int width, int imageType) {
		double aspect = image.getWidth(null) / (double) image.getHeight(null);

		int height = (int) (width / aspect);
		if (!valid(width, height))
			return toBufferedImage(image, imageType);

		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return img;
	}

	public static BufferedImage scaleToHeight(Image image, int height, int imageType) {
		double aspect = image.getWidth(null) / (double) image.getHeight(null);

		int width = (int) (height * aspect);

		if (!valid(width, height))
			return toBufferedImage(image, imageType);

		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return img;
	}

	public static BufferedImage getFormatBufferedImage(int width, int height, int variant) {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(width, height, variant);
	}

}
