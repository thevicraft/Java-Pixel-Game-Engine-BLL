package com.javapixelgame.game.api;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.entity.Collision;
import com.javapixelgame.game.api.graphics.Light;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.registry.UID;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.ConfigHandler;

/**
 * Plain {@link Game} object that supplies with basic game attributes, it is
 * abstract, so in order to make a game object you must inherit this class
 * 
 * @author thevicraft
 * @see {@link Entity} {@link Obstacle}
 *
 */
public abstract class Objective implements java.io.Serializable {
	private static final long serialVersionUID = 4563039796804525177L;
	protected Collision collision;
	private Rectangle bounds = new Rectangle();
	private Point renderCorner = new Point();
	transient protected Graphics2D graphics;
	transient protected BufferedImage sprite;
	transient protected CPoint spawn;
	transient protected CPoint position;
	transient protected int width;
	transient protected int height;
	protected double aspect_ratio;
	transient protected World world;
	transient public int xBorder;
	transient public int yBorder;
	protected double sizeInMeter;
//	protected String name = "unknown";
	protected double borderFactor;
	private UID uid = new UID("unknown");
	private double weight = 100;
	private boolean hitbox = false;
	private boolean light;
	private double lightRange;
	private float luminocity;
	private String lightProperty = Light.CIRCLE;
	private int lightDirection = Skin.NULL;
	
	private int upLightDistance = 0;
	private int downLightDistance = 0;
	private int leftLightDistance = 0;
	private int rightLightDistance = 0;
	
	private boolean visible = true;
	private boolean rendered = false;

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		double px = Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale));
		oos.writeObject((double) getPosition().x / px);
		oos.writeObject((double) getPosition().y / px);

		oos.writeObject((double) getSpawn().x / px);
		oos.writeObject((double) getSpawn().y / px);
		writeMyself(oos);
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		double px = Double.parseDouble(ois.readObject().toString());
		double py = Double.parseDouble(ois.readObject().toString());

		double sx = Double.parseDouble(ois.readObject().toString());
		double sy = Double.parseDouble(ois.readObject().toString());
		position = new CPoint((int)(px * Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale))),
				(int)(py * Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale))));
		spawn = new CPoint((int)(sx * Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale))),
				(int)(sy * Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale))));

		readMyself(ois);
	}

	protected void readMyself(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		readSpriteBufferedImage(ois);
	}

	protected void writeMyself(ObjectOutputStream oos) throws IOException {
		writeSpriteBufferedImage(oos);
	}

	private void writeSpriteBufferedImage(ObjectOutputStream oos) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(sprite, "png", baos);
		byte[] bytes = baos.toByteArray();
		oos.writeObject(bytes);
	}

	private void readSpriteBufferedImage(ObjectInputStream ois) throws ClassNotFoundException, IOException {

		byte[] graphicBuffer = (byte[]) ois.readObject();
		InputStream is = new ByteArrayInputStream(graphicBuffer);
		sprite = ImageIO.read(is);

	}

	/**
	 * Resets the current Graphics of the Entity so the new ones do not overlap the
	 * old ones.
	 * 
	 * @apiNote Do not override this method! Otherwise the graphics will not work
	 *          properly!
	 */
	public void resetSprite() {
		sprite = new BufferedImage(getBounds().width, getBounds().height, BufferedImage.TYPE_INT_ARGB);
		graphics = sprite.createGraphics();
	}

	/**
	 * <strong>Returns</strong> the <strong>graphics</strong> that are ready to be
	 * painted on the current {@link World} surface
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getSprite() {
		return sprite;
	}

	/**
	 * <strong>Returns</strong> the current {@link Collision} attribute of the
	 * {@link Objective}
	 * 
	 * @return Collision
	 */
	public Collision getCollision() {
		return collision;
	}

	/**
	 * <strong>Returns</strong> the painting <strong>bounds</strong> of the
	 * {@link Objective}, it is only necessary for <strong>painting</strong> not for
	 * the game function
	 * 
	 * @return Rectangle
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * <strong>Sets</strong> the <strong>painting bounds</strong> of the
	 * {@link Objective}, they have <strong>no</strong> effect on the
	 * <strong>Objective's</strong> abilities
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds(int x, int y, int width, int height) {
		this.bounds = new Rectangle(x, y, width, height);
	}

//	/**
//	 * Sets the object's display name
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	/**
//	 * Returns the object's display name
//	 */
//	public String getName() {
//		return this.name;
//	}

	/**
	 * <strong>Returns</strong> the {@link World} where the object is applied to
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Returns the object's Spawn Point
	 * 
	 * @return CPoint
	 */
	public CPoint getSpawn() {
		return spawn;
	}

	/**
	 * Sets the object's Spawn Point when it is applied to the world
	 * 
	 * @param spawn - CPoint with spawn coordinates
	 */
	public void setSpawn(CPoint spawn) {
		this.spawn = spawn;
	}

	/**
	 * <strong>Returns</strong> the size of the {@link Objective} in real
	 * <strong>meters</strong>
	 * 
	 * @return double
	 */
	public double getSizeInMeter() {
		return sizeInMeter;
	}

	/**
	 * <strong>Returns</strong> the <strong>registry</strong> {@link UID} of the
	 * {@link Objective}, it is <strong>unique</strong> for every game object
	 * 
	 * @return
	 */
	public UID getRegistry() {
		return uid;
	}

	public CPoint getPosition() {
		return position;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isHitboxShown() {
		return hitbox;
	}

	public void showHitbox(boolean hitbox) {
		this.hitbox = hitbox;
	}

	public double getLightRange() {
		return lightRange;
	}

	public void setLightRange(double lightRange) {
		this.lightRange = lightRange;
	}

	public boolean isLightEmitting() {
		return light;
	}

	public void setLightEmitting(boolean light) {
		this.light = light;
	}

	public float getLuminocity() {
		return luminocity;
	}

	public void setLuminocity(float luminocity) {
		this.luminocity = luminocity;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Point getRenderCorner() {
		return renderCorner;
	}

	public void updateRenderCorner() {
		try {
		this.renderCorner = new Point(getBounds().x - getWorld().getCamera().getRenderArea().x,
				(int) getBounds().y - getWorld().getCamera().getRenderArea().y);
		} catch (Exception e) {}
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public String getLightProperty() {
		return lightProperty;
	}

	public void setLightProperty(String lightProperty) {
		this.lightProperty = lightProperty;
	}

	public int getLightDirection() {
		return lightDirection;
	}

	public void setLightDirection(int lightDirection) {
		this.lightDirection = lightDirection;
	}
	
	/**
	 * light distance in pixel!
	 */
	public int getLeftLightDistance() {
		return leftLightDistance;
	}

	/**
	 * 
	 * light distance in meter!!
	 */
	public void setLeftLightDistance(double meterdistance) {
		this.leftLightDistance = getWorld().getPixelLength(meterdistance);
	}

	/**
	 * light distance in pixel!
	 */
	public int getUpLightDistance() {
		return upLightDistance;
	}

	/**
	 * 
	 * light distance in meter!!
	 */
	public void setUpLightDistance(double meterdistance) {
		this.upLightDistance = getWorld().getPixelLength(meterdistance);
	}

	/**
	 * light distance in pixel!
	 */
	public int getDownLightDistance() {
		return downLightDistance;
	}

	/**
	 * 
	 * light distance in meter!!
	 */
	public void setDownLightDistance(double meterdistance) {
		this.downLightDistance = getWorld().getPixelLength(meterdistance);
	}

	/**
	 * light distance in pixel!
	 */
	public int getRightLightDistance() {
		return rightLightDistance;
	}

	/**
	 * 
	 * light distance in meter!!
	 */
	public void setRightLightDistance(double meterdistance) {
		this.rightLightDistance = getWorld().getPixelLength(meterdistance);
	}
}
