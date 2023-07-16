package com.javapixelgame.game.api.obstacle;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.Player;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.Collision;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.ConfigHandler;

/**
 * Game Obstacle, the Class provides various methods for controlling the Entity,
 * it is <strong>not</strong> {@linkplain Tickable}
 * 
 * @author thevicraft
 * @see {@link World} {@link Game} {@link Player}
 */
public abstract class Obstacle extends Objective {

	private static final long serialVersionUID = 1L;
	protected double borderFactor = 0;

	protected Texture texture;

	private boolean showName;

	public Obstacle(double sizeInMeter, CPoint spawn, Texture texture, World world, String name, boolean showName) {
		init(sizeInMeter, spawn, texture, world, name, showName);
	}

	private void init(double sizeInMeter, CPoint spawn, Texture texture, World world, String name, boolean showName) {
		this.showName = showName;
		getRegistry().setName(name);
		this.setWorld(world);
		this.sizeInMeter = sizeInMeter;
		this.height = world.getPixelLength(sizeInMeter);
		this.texture = texture;
		this.width = texture.getWidthOnRatio(height);
		
		borderFactor = showName ? 0.1 : 0;
		xBorder = (int) (width * borderFactor);
		yBorder = (int) (height * borderFactor);
//		setSize(new Dimension(width + xBorder * 2, height + yBorder * 2));
		setBounds(0,0,width + xBorder * 2, height + yBorder * 2);
		
		this.spawn = spawn;

		collision = new Collision(this);
		setPosition(this.spawn);
		collision.update();
//		setLayout(null);
		this.texture.optimize(width, height);
		aspect_ratio = texture.getAspect();
//		System.out.println(width+","+height);
		resetSprite();
		updateGraphics();
		
		constructObstacle();
	}
	
	@Override
	protected void readMyself(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		super.readMyself(ois);
		this.texture = (Texture)ois.readObject();
		resizeLoad(sizeInMeter);
	}
	
	@Override
	protected void writeMyself(ObjectOutputStream oos) throws IOException {
		super.writeMyself(oos);
		oos.writeObject(texture);
	}
	
	private void resizeLoad(double sizeInMeter) {
		this.sizeInMeter = sizeInMeter;
		this.height = (int)(Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)) * sizeInMeter);
		this.width = texture.getWidthOnRatio(height);

		xBorder = (int) (width * borderFactor);
		yBorder = (int) (height * borderFactor);


		this.texture.optimize(width, height);
		
		getBounds().setSize(width + 2 * xBorder, height + 2 * yBorder);
		updateRenderCorner();
		resetSprite();
		updateGraphics();
		
	}

	public void setPosition(CPoint pos) {

		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos));

		position = getWorld().getCoords().check(pos);
		setBounds(exactPoint.x - (width + 2 * xBorder) / 2, exactPoint.y - (height + 2 * yBorder) / 2,
				width + 2 * xBorder, height + 2 * yBorder);
		
		collision.update();

	}

	public CPoint getPosition() {
		return position;
	}
	
	public void updateGraphics() {
		drawTexture();
	}
	

//	@Override
//	protected void paintComponent(Graphics g) {
//		Graphics2D panel = (Graphics2D) g;
//		panel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		if (showName) {
//			panel.setColor(new Color(0, 0, 0, 180));
//			panel.fillRect(0, 0, width, yBorder);
//			panel.setColor(Color.white);
//			panel.drawString(name, xBorder + 5, yBorder - 2);
//		}
//
//		drawTexture(panel);
//
//	}
	private byte lastTick = 0;
	
	public BufferedImage getSprite() {
		
		if(getWorld().isRendered(this) && texture.isAnimated() && lastTick != getWorld().getTick().getCurrentTick()) {
			resetSprite();
			updateGraphics();
		}
		lastTick = getWorld().getTick().getCurrentTick();
		return super.getSprite();
	}

	protected void drawTexture() {
		if (showName) {
			graphics.setColor(new Color(0, 0, 0, 180));
			graphics.fillRect(0, 0, width, yBorder);
			graphics.setColor(Color.white);
			graphics.drawString(getRegistry().getName(), xBorder + 5, yBorder - 2);
		}
		
		graphics.drawImage(texture.getImage(), xBorder, yBorder, world);
	}

	/**
	 * Makes the {@link Obstacle} to die and remove from the {@link World} and
	 * {@link Tickable}.
	 */
	public void die() {
		world.removeObjective(this);
	}

	/**
	 * Override this method to add special things to when the obstacle is
	 * constructed, this method is already executed in the constructor of the
	 * Obstacle Class, so there is no need of executing it again!
	 * 
	 * @see {@link Obstacle}
	 */
	public void constructObstacle() {

	}

	public int getObstacleWidth() {
		return this.width;
	}

	public int getObstacleHeight() {
		return this.height;
	}

	/**
	 * Sets collision properties for walking and blocking everything
	 * 
	 * @param walkable
	 * @param blockall
	 */
	public void setProperties(boolean walkable, boolean blockall) {
		getCollision().setWalkable(walkable);
		getCollision().setBlockAll(blockall);
	}


}
