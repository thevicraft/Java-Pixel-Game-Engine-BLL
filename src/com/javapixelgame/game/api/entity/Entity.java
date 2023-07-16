package com.javapixelgame.game.api.entity;

import java.awt.AlphaComposite;
import java.awt.Image;
import java.awt.Point;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.Player;
import com.javapixelgame.game.api.Tickable;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.registry.UID;
import com.javapixelgame.game.api.world.Tile;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;

/**
 * Basic Game Object, the Class provides various methods for controlling the
 * Entity, it is {@linkplain Tickable}
 * 
 * @author thevicraft
 * @see {@link World} {@link Game} {@link Player}
 */
public abstract class Entity extends Objective implements Tickable {
	private static final long serialVersionUID = 1L;
	transient private int speedPixelPerTick;
	private double speedMeterPerTick;

	protected int xVelocity = 0;
	protected int yVelocity = 0;
	
	transient protected Skin skin;
	transient protected Image toDraw;
	protected Texture texture;
	
	protected boolean hasSkin;
	protected int skinDirection = Skin.DOWN;
	protected boolean hasItem = false;

	protected String state = "idle";

	protected double borderFactor = 0.5;
	protected int healthPoints;
	private int maxHealthPoints;

	private boolean invulnerable = false;
	protected boolean colliding;
	protected boolean pushing = false;
	private boolean dead = false;
	protected boolean receivedDamage = false;


	public Entity(double sizeInMeter, CPoint spawn, Texture texture, World world, double speedMeterPerTick, String name,
			int lifePoints) {
		this.healthPoints = lifePoints;
		this.setMaxHealthPoints(lifePoints);
		hasSkin = false;
		getRegistry().setName(name);
		this.setWorld(world);
		this.sizeInMeter = sizeInMeter;
		this.height = world.getPixelLength(sizeInMeter);

		aspect_ratio = texture.getAspect();

		this.width = texture.getWidthOnRatio(this.height);
		xBorder = (int) (this.width * borderFactor);
		yBorder = (int) (this.height * borderFactor);

		setBounds(0, 0, this.width + xBorder * 2, this.height + yBorder * 2);
		texture.optimize(width, height);
		this.texture = texture;
		resetSprite();
		this.toDraw = texture.getImage();
		this.spawn = spawn;
		position = this.spawn;

		collision = new Collision(this);
		spawn(spawn);

		setSpeedMeterPerTick(speedMeterPerTick);
		constructEntity();
	}

	public Entity(double sizeInMeter, CPoint spawn, Skin skin, World world, double speedMeterPerTick, String name,
			int lifePoints) {
		this.healthPoints = lifePoints;
		this.setMaxHealthPoints(lifePoints);
		hasSkin = true;
		getRegistry().setName(name);
		this.setWorld(world);
		this.height = (int) (sizeInMeter * world.widthToMapMeterAspectRatio * world.map_width);
		this.sizeInMeter = sizeInMeter;
		this.spawn = spawn;
		this.skin = skin;

		aspect_ratio = this.skin.aspect_ratio;

		this.width = (int) (this.height * aspect_ratio);
		xBorder = (int) (this.width * borderFactor);
		yBorder = (int) (this.height * borderFactor);
		setBounds(0, 0, this.width + xBorder * 2, this.height + yBorder * 2);

		resetSprite();

		position = this.spawn;
		spawn(spawn);

		collision = new Collision(this);

		setSpeedMeterPerTick(speedMeterPerTick);

		this.skin.optimize(width, height);

		this.toDraw = this.skin.getSkinImage(getSkinDirection()); // default pose on startup
		constructEntity();
	}

	@Override
	protected void readMyself(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		super.readMyself(ois);
		double size = Double.parseDouble(ois.readObject().toString());
		this.sizeInMeter = size;
		double borderfactor = Double.parseDouble(ois.readObject().toString());
		borderFactor = borderfactor;
		boolean hasskin = (boolean) ois.readObject();
		if (hasskin /* checks if the entity has a skin model */) {
			String pack = (String) ois.readObject();
			skin = new Skin(pack);
			skinDirection = (int) ois.readObject();
			toDraw = this.skin.getSkinImage(getSkinDirection());
		} else {
			this.texture = (Texture)ois.readObject();
//			Console.output(texture);
		}
		
		resizeLoad(size,hasskin,borderfactor);
		
		setSpeedMeterPerTick(Double.parseDouble(ois.readObject().toString()));
	}

	@Override
	protected void writeMyself(ObjectOutputStream oos) throws IOException {
		super.writeMyself(oos);
		oos.writeObject(sizeInMeter);
		oos.writeObject(borderFactor);
		oos.writeObject(hasSkin);
		if (hasSkin) {
			oos.writeObject(skin.name);
			oos.writeObject(getSkinDirection());
		} else {
			oos.writeObject(texture);			
		}
		oos.writeObject(speedMeterPerTick);
	}
	
	protected void resizeLoad(double sizeInMeter, boolean skinyes, double borderfactor) {
		this.sizeInMeter = sizeInMeter;
		this.height = (int)(Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)) * sizeInMeter);
		if(!hasSkin)
			this.width = (int) (this.height * aspect_ratio);
		else
			this.width = (int) (this.height * aspect_ratio);

		xBorder = (int) (width * borderfactor);
		yBorder = (int) (height * borderfactor);

//		setPosition(getPosition());

//		collision.update();
		
		//updates the bounds
		getBounds().setSize(width + 2 * xBorder, height + 2 * yBorder);
		

		if(!skinyes) {
			texture.optimize(width, height);
			toDraw = texture.getImage();
		} else {
			skin.optimize(width, height);
			toDraw = skin.getSkinImage(getSkinDirection());
		}
//		System.out.println(width+","+height);
		
		
		
		resetSprite();
		updateGraphics();
		
		if(getSprite().getWidth() != width + 2* xBorder)
			Console.output("no");
	}

	/**
	 * DO NOT OVERRIDE THIS METHOD, otherwise it will no longer work correctly!
	 * </p>
	 * You will destroy the entities position-setting and skin-setting properties!!
	 * </p>
	 * Use {@link onEntityTick()} instead
	 * </p>
	 * 
	 * @see {@link Entity} {@link World}
	 */
	@Override
	public void onWorldTick(int tick) {
		updateCollidingState();
		applyMovement();

		if (isRendered() && (hasSkin) && (tick % 2 == 0)) {
			setSkin();

		}
		if ((xVelocity != 0) || (yVelocity != 0)) {
			state = "running";
		} else {
			state = "idle";
		}

		onEntityTick(tick);
		
		if(isRendered()) {
			resetSprite();
			updateGraphics();			
			onRenderedTick(tick);
		}
	}

	public void applyMovement() {
		if (!isColliding()) {

			int xv = xVelocity;
			int xy = yVelocity;
			if (!checkForVelocities(xv, xy)) {
				colliding = true;

				if (!checkForXVelocity(xv))
					xv = 0;
				if (!checkForYVelocity(xy))
					xy = 0;
			}
			pushing = false;
			setPosition(new CPoint(getPosition().x + xv, getPosition().y + xy));
		} else {
			pushing = true;
			push(new CPoint((int) (getPosition().x + getxVelocity() * 0.25d),
					(int) (getPosition().y + getyVelocity() * 0.25d)));
		}

	}
	
	public boolean isMoving() {
		return (getxVelocity() != 0) || (getyVelocity() != 0);
	}

	public void push(CPoint pos) {
		if (!checkGroundColliding(pos)) {
			teleport(pos);
		}
	}

	/**
	 * Override this method to add some features to your entity
	 * </p>
	 * IMPORTANT: do not override {@link onWorldTick()}
	 * </p>
	 * otherwise you will destroy the entities position-setting and skin-setting
	 * properties
	 * 
	 * @param tick - the current tick value (1 - 20)
	 * @see {@link Entity} {@link World}
	 */
	public abstract void onEntityTick(int tick);

	/**
	 * Override this method to add some features to your entity when it is rendered
	 * at screen
	 * </p>
	 * IMPORTANT: do not override {@link onWorldTick()}
	 * </p>
	 * otherwise you will destroy the entities position-setting and skin-setting
	 * properties
	 * 
	 * @param tick - the current tick value (1 - 20)
	 * @see {@link Entity} {@link World}
	 */
	public abstract void onRenderedTick(int tick);

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub
//		System.out.println(name + " random");
	}

	public void updateGraphics() {
		drawSkin();
	}

	protected void setSkin() {
		if (xVelocity > 0) {
			toDraw = skin.getSkinImage(Skin.RIGHT);
			skinDirection = Skin.RIGHT;
		} else if (xVelocity < 0) {
			toDraw = skin.getSkinImage(Skin.LEFT);
			skinDirection = Skin.LEFT;
		} else if (yVelocity > 0) {
			toDraw = skin.getSkinImage(Skin.UP);
			skinDirection = Skin.UP;
		} else if (yVelocity < 0) {
			toDraw = skin.getSkinImage(Skin.DOWN);
			skinDirection = Skin.DOWN;
		}
	}

	public void setPosition(CPoint pos) {

//		colliding = false;

//		checkGroundColliding(pos);

		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos));
//
//		CPoint posStore = position;
//
		position = getWorld().getCoords().check(pos);
//		getCollision().update();
//
//		colliding = checkObjectiveColliding();
//
//		if (!colliding) {
//			colliding = checkGroundColliding(position);
//		}

//		if (!checkObjectiveColliding() && !checkGroundColliding(position)) {
		setBounds(exactPoint.x - (width + 2 * xBorder) / 2, exactPoint.y - (height + 2 * yBorder) / 2,
				width + 2 * xBorder, height + 2 * yBorder);
//		} else {
////			if(getRegistry().getRegistryID().equals("delorean"))
////				System.out.println("wird geblockt "+getRegistry().getName());
//			position = posStore;
		getCollision().update();
//		}
//		colliding = false;
	}

	public void updateBounds() {
		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getPosition());
		setBounds(exactPoint.x - (width + 2 * xBorder) / 2, exactPoint.y - (height + 2 * yBorder) / 2,
				width + 2 * xBorder, height + 2 * yBorder);
	}

	public boolean checkForVelocities(int xVelocity, int yVelocity) {
		boolean placeable = true;
		CPoint store = getPosition();
		position = getWorld().getCoords().check(new CPoint(getPosition().x + xVelocity, getPosition().y + yVelocity));
		getCollision().update();
		placeable = !checkObjectiveColliding();
		if (placeable)
			placeable = !checkGroundColliding(position);
		position = store;
		getCollision().update();
		return placeable;
	}

	public boolean checkForXVelocity(int xVelocity) {
		boolean placeable = true;
		CPoint store = getPosition();
		position = getWorld().getCoords().check(new CPoint(getPosition().x + xVelocity, getPosition().y));
		getCollision().update();
		placeable = !checkObjectiveColliding();
		if (placeable)
			placeable = !checkGroundColliding(position);
		position = store;
		getCollision().update();

		return placeable;
	}

	public boolean checkForYVelocity(int yVelocity) {
		boolean placeable = true;
		CPoint store = getPosition();
		position = getWorld().getCoords().check(new CPoint(getPosition().x, getPosition().y + yVelocity));
		getCollision().update();
		placeable = !checkObjectiveColliding();
		if (placeable)
			placeable = !checkGroundColliding(position);
		position = store;
		getCollision().update();
		return placeable;
	}

	protected boolean yes;

	public final boolean checkObjectiveColliding() {
		yes = false;
//		world.getObjectiveInRadius(getPosition(), (getEntityWidth() + getEntityHeight() * 2)).forEach(obj -> {
//			if (obj != this && obj.getCollision().overlaps(collision) && !yes) {
//
//				yes = obj.getCollision().isBlockAll() || !obj.getCollision().isWalkable();
//			}
//		});
		world.getColliders(getCollision()).forEach(obj -> {
			if (/*obj != this && obj.getCollision().overlaps(collision) && */!yes && compareCollision(obj)) {

				yes = collide(obj);
			}
		});
		return yes;
	}
	
	protected boolean collide(Objective obj) {
		return obj.getCollision().isBlockAll() || !obj.getCollision().isWalkable();
	}
	protected boolean compareCollision(Objective obj) {
		return true;
	}

	public boolean collide(Tile tile) {
		return !(tile.isWalkable() && !tile.isBlockAll());
	}
	
	public final boolean checkGroundColliding(CPoint pos) {
		Tile current = world.getModel().getTile(
				getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos)), world.getPixelLength(1));
		return collide(current);
	}

	public void updateCollidingState() {
		colliding = checkGroundColliding(getPosition());
		if (!colliding)
			colliding = checkObjectiveColliding();
	}

	public void teleport(CPoint pos) {
		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos));
		position = pos;
		setBounds(exactPoint.x - (width + 2 * xBorder) / 2, exactPoint.y - (height + 2 * yBorder) / 2,
				width + 2 * xBorder, height + 2 * yBorder);
		getCollision().update();
	}

	public void spawn(CPoint spawn) {
		position = getWorld().getCoords().check(spawn);
		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getPosition());
		setBounds(exactPoint.x - (width + 2 * xBorder) / 2, exactPoint.y - (height + 2 * yBorder) / 2,
				width + 2 * xBorder, height + 2 * yBorder);
	}

	public CPoint getPosition() {
		return position;
	}

	protected void drawSkin() {
		if (receivedDamage) {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			receivedDamage = false;
		}
		graphics.drawImage(getToDraw(), xBorder, yBorder, world);

		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	public void damage(int damage, UID uid) {
		if (!isInvulnerable() && !getRegistry().getUUID().equals(uid.getUUID())) {
			receivedDamage = true;
			healthPoints = getHealthPoints() - Math.abs(damage);
			onDamage(damage, uid);
			if (getHealthPoints() <= 0) {
				die(uid);
			}
		}
	}
	
	protected void onDamage(int damage, UID from) {}

	public void heal(int value) {
		if (!isInvulnerable()) {
			healthPoints = getHealthPoints() + Math.abs(value);
			if (getHealthPoints() > getMaxHealthPoints()) {
				healthPoints = getMaxHealthPoints();
			}
		}
	}

	public void regenerate() {
		if (!isInvulnerable() && getMaxHealthPoints() > 0) {
			healthPoints = getMaxHealthPoints();
		}
	}

	/**
	 * Makes the {@link Entity} to die and remove from the {@link World} and
	 * {@link Tickable}.
	 * 
	 * @param killer - name of the killer who killed the entity, replace it with
	 *               "none", and no message will be shown
	 */
	public void die(UID killer) {
		die();
	}

	/**
	 * Makes the {@link Entity} to die and remove from the {@link World} and
	 * {@link Tickable}.
	 */
	public void die() {
		setDead(true);
		world.removeObjective(this);
	}

	/**
	 * Override this method to add special things to when the entity is constructed,
	 * this method is already executed in the constructor of the Entity Class, so
	 * there is no need of executing it again!
	 * 
	 * @see {@link Entity}
	 */
	public void constructEntity() {

	}

	public int getEntityWidth() {
		return this.width;
	}

	public int getEntityHeight() {
		return this.height;
	}
	
	@Override
	public int getLightDirection() {
		return getSkinDirection();
	}

	/**
	 * Returns the value of how many pixels should be passed per tick
	 * 
	 * @return speedPixelPerTick
	 */
	public int getPixelSpeedPerTick() {
		return speedPixelPerTick;
	}

	/**
	 * Returns the value of how many imaginary meters in the map should be passed
	 * per tick
	 * 
	 * @return value of how many imaginary "map meters" should be passed by the
	 *         entity per tick
	 */
	public double getSpeedMeterPerTick() {
		return speedMeterPerTick;
	}

	/**
	 * Sets how many "map pixels" should be passed by the entity per tick
	 * 
	 * @param speed - value of how many imaginary "map meters" should be passed by
	 *              the entity per tick
	 */
	public void setSpeedMeterPerTick(double speed) {
		this.speedMeterPerTick = speed;
		speedPixelPerTick = (int) (speed * (Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale))));
	}

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public int getSkinDirection() {
		return skinDirection;
	}

	public boolean isInvulnerable() {
		return invulnerable;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public Image getToDraw() {
		return toDraw;
	}

	public boolean isColliding() {
		return colliding;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public int getMaxHealthPoints() {
		return maxHealthPoints;
	}

	public void setMaxHealthPoints(int maxHealthPoints) {
		this.maxHealthPoints = maxHealthPoints;
	}

	public boolean isPushing() {
		return pushing;
	}
}
