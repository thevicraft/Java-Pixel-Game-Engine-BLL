package com.javapixelgame.game.api.graphics;

import java.awt.Image;
import java.awt.Point;
import java.util.Random;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.GameHandler;

public class Particle extends Objective {
	private static final long serialVersionUID = 1L;
//	private BufferedImage texture;
	private Texture texture;
	private CPoint position;

	private short duration;
	private int randomoffset = 20;
	private Random rand = new Random();

	private Point drawCorner = new Point();

	public static final int STAY = -1;
	public static final int RANDOM_DIRECTION = 4;
	public static final int UP_DIRECTION = Skin.UP;
	public static final int DOWN_DIRECTION = Skin.DOWN;
	public static final int LEFT_DIRECTION = Skin.LEFT;
	public static final int RIGHT_DIRECTION = Skin.RIGHT;

	private int behaviour;

//	private Graphics2D graphics;
//	private int drawwidth;
//	private int drawheight;

	public Particle(Texture t, int pxSize, short duration, CPoint position, int randomPixelOffset,
			final int behaviour) {
		int px_optimized = (pxSize > 0 ? pxSize : 1);

		this.width = t.getWidthOnRatio(px_optimized);
		this.height = px_optimized;

//		texture = new BufferedImage(drawwidth, drawheight, BufferedImage.TYPE_INT_ARGB);
//		graphics = texture.createGraphics();
//		graphics.drawImage(t.getImage(), 0, 0, t.getWidthOnRatio(px_optimized), px_optimized, null);
		this.texture = t;
		if (texture.getWidth() != width || texture.getHeight() != height) {
			texture.optimize(width, height);
		}

		this.duration = duration > 0 ? duration : 1;

		this.position = position;

		this.behaviour = behaviour;
		this.randomoffset = randomPixelOffset;
		updateBounds();
	}

	public Image returnTexture() {
		return texture.getImage();
	}

	public CPoint getPosition() {
		return position;
	}

	public void setPosition(CPoint position) {
		this.position = position;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public short getDuration() {
		return duration;
	}

	public void setDuration(short duration) {
		this.duration = duration;
	}

	private Point bound;

	public void spread() {

		duration -= 1;

		if (behaviour == STAY)
			return;

		int xOffset = 0;
		int yOffset = 0;
		switch (behaviour) {
		case RANDOM_DIRECTION:
			xOffset = (rand.nextBoolean() ? 1 : -1) * rand.nextInt(randomoffset + 1);
			yOffset = (rand.nextBoolean() ? 1 : -1) * rand.nextInt(randomoffset + 1);
			break;
		case UP_DIRECTION:
			xOffset = (rand.nextBoolean() ? 1 : -1) * rand.nextInt(randomoffset + 1);
			yOffset = rand.nextInt(randomoffset + 1);
			break;
		case DOWN_DIRECTION:
			xOffset = (rand.nextBoolean() ? 1 : -1) * rand.nextInt(randomoffset + 1);
			yOffset = -rand.nextInt(randomoffset + 1);
			break;
		case LEFT_DIRECTION:
			xOffset = -rand.nextInt(randomoffset + 1);
			yOffset = (rand.nextBoolean() ? 1 : -1) * rand.nextInt(randomoffset + 1);
			break;
		case RIGHT_DIRECTION:
			xOffset = rand.nextInt(randomoffset + 1);
			yOffset = (rand.nextBoolean() ? 1 : -1) * rand.nextInt(randomoffset + 1);
			break;
		}
		position.x = position.x + xOffset;
		position.y = position.y + yOffset;

		updateBounds();
	}

	private void updateBounds() {
		bound = getWorld().getCoords()
				.getAbsolutePoint(new CPoint(getPosition().x - getWidth() / 2, getPosition().y - getHeight() / 2));

		drawCorner = new Point(bound.x - getWorld().getCamera().getRenderArea().x,
				(int) bound.y - getWorld().getCamera().getRenderArea().y);
	}

	@Override
	public Point getRenderCorner() {
		return drawCorner;
	}

	@Override
	public World getWorld() {
		return GameHandler.getWorld();
	}

}
