package com.javapixelgame.game.api.entity;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.registry.UID;
import com.javapixelgame.game.api.world.Tile;
import com.javapixelgame.game.api.world.World;

public class ProjectileEntity extends Entity {
	private static final long serialVersionUID = 1L;
	protected double borderFactor = 0;
	protected int duration;
	protected int damage;
	private UID from;
	private Item item;
	private double theta = 0;
	private Dimension size;
	private transient BufferedImage texture;

	public ProjectileEntity(double sizeInMeter, CPoint spawn, Texture texture, World world, double speedMeterPerTick,
			int duration, int direction, int damage, UID from, Item item) {
		super(sizeInMeter, spawn, texture, world, speedMeterPerTick, "", 0);
		// TODO Auto-generated constructor stub
		this.duration = duration;
		this.damage = damage;
		this.setSenderUID(from);
		this.item = item;
		skinDirection = direction;
		switch (direction) {
		case Skin.UP:
			setxVelocity(0);
			setyVelocity(getPixelSpeedPerTick());
			theta = Math.toRadians(0);
			break;
		case Skin.DOWN:
			setxVelocity(0);
			setyVelocity(-getPixelSpeedPerTick());
			theta = Math.toRadians(180);
			break;
		case Skin.LEFT:
			setxVelocity(-getPixelSpeedPerTick());
			setyVelocity(0);
			theta = Math.toRadians(270);
			break;
		case Skin.RIGHT:
			setxVelocity(getPixelSpeedPerTick());
			setyVelocity(0);
			theta = Math.toRadians(90);
			break;
		}
		borderFactor = 0;
		xBorder = 0;
		yBorder = 0;
		if (aspect_ratio > 1) {
//			setSize(new Dimension(world.getPixelLength(sizeInMeter) + xBorder * 2,
//					world.getPixelLength(sizeInMeter) + xBorder * 2));
			setBounds(0, 0, world.getPixelLength(sizeInMeter) + xBorder * 2,
					world.getPixelLength(sizeInMeter) + xBorder * 2);
		} else {
//			setSize(new Dimension(world.getPixelLength(sizeInMeter), world.getPixelLength(sizeInMeter)));
			setBounds(0, 0, world.getPixelLength(sizeInMeter), world.getPixelLength(sizeInMeter));
		}
		size = getBounds().getSize();
		getCollision().setBorders(0.25, 0.25, 0.5, 0.5);
		getCollision().setWalkable(true);
		getCollision().setBlockAll(false);
		this.texture = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D d = this.texture.createGraphics();
//		d.fillRect(0, 0, size.width, size.height);
		d.drawImage(texture.getImage(), (int) -texture.getWidth() / 2 + size.width/2,
				(int) -texture.getHeight()/2 + size.height/2, texture.getWidth(), texture.getHeight(), world);
		d.dispose();
//		drawScaledBackground(texture.getImage(),d);
		
//		System.out.println(getBounds().getSize());
		
		super.resetSprite();
		
		graphics.rotate(theta, getBounds().getWidth() / 2, getBounds().getHeight() / 2);

		graphics.drawImage(this.texture, 0, 0, world);

		graphics.rotate(Math.toRadians(360) - theta, getBounds().getWidth() / 2, getBounds().getHeight() / 2);

		graphics.dispose();
		
		getRegistry().setRegistryID("projectile");
	}

//	@Override
//	public boolean checkObjectiveColliding() {
//		yes = false;
//		world.getObjectiveInRadius(getPosition(), (getEntityWidth() + getEntityHeight() * 2)).forEach(obj -> {
//			if (obj != this && obj.getCollision().overlaps(collision) && !yes) {
//
//				yes = obj.getCollision().isBlockAll();
//			}
//		});
//		return yes;
//	}
	@Override
	protected boolean collide(Objective obj) {
		return obj.getCollision().isBlockAll();
	}
@Override
public boolean collide(Tile tile) {
		return tile.isBlockAll();
	}
	
//	@Override
//	public boolean checkGroundColliding(CPoint pos) {
//		Tile current = world.getModel().getTile(
//				getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos)), world.getPixelLength(1));
//		return current.isBlockAll();
//	}

	@Override
	public void setPosition(CPoint pos) {
		super.setPosition(pos);
		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(position));
		getBounds().setLocation(exactPoint.x - (int) getBounds().getWidth() / 2,
				exactPoint.y - (int) getBounds().getHeight() / 2);
		getBounds().setSize(size);
	}

//	@Override
//	public void setPosition(CPoint pos) {
//		
////			Point exactPoint = getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos));
////
////			if (!world.getModel().getTile(exactPoint, world.getPixelLength(1)).isBlockAll()) {
////
////				position = getWorld().getCoords().check(pos);
////				setLocation(exactPoint.x - getWidth() / 2, exactPoint.y - getHeight() / 2);
////			}
//		
//		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getWorld().getCoords().check(pos));
//
//		Tile current = world.getModel().getTile(exactPoint, world.getPixelLength(1));
//
//		Entity e = world.getEntityAt(pos,this);
//		Obstacle o = world.getObstacleAt(pos);
//		boolean obwalk = true;
//		if (o != null && o.getCollision().overlaps(collision)) {
//			obwalk = !o.getCollision().isBlockAll();
//		}
//
//		boolean enwalk = true;
//		
//		if (e != null && e != this && e.getCollision().overlaps(collision) && !e.getRegistry().isSameAs(getSenderUID())) {
//			enwalk = !e.getCollision().isBlockAll();
//		}
//		
//		if (!current.isBlockAll() && obwalk && enwalk) {
//			colliding = false;
//			position = getWorld().getCoords().check(pos);
////			setLocation(exactPoint.x - getBounds().getWidth() / 2, exactPoint.y - getBounds().getHeight() / 2);
//			getBounds().setLocation(exactPoint.x - (int)getBounds().getWidth() / 2, exactPoint.y - (int)getBounds().getHeight() / 2);
//		} else {
//			colliding = true;
//		}
//
//		
//	}

	@Override
	public void onEntityTick(int tick) {
		if (duration <= 0) {
			die();
		} else {
			duration -= 1;
		}

		// bitte das hier durch das neue getWorld().getColliders() ersetzen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		Entity f = getWorld().getEntityAt(getPosition(), this);
		if (f != null && !isColliding() && (f != this) && !f.getRegistry().equals(getSenderUID())
				&& !(f instanceof ProjectileEntity) && !(f instanceof ItemEntity)) {
			if (f.getCollision().overlaps(getCollision())) {
				f.damage(damage, getSenderUID());
				die();

			}
		}
		
		
	}

	@Override
	public void die() {
		super.die();
		if (item != null) {
			getWorld().addObjective(new ItemEntity(getItem(), getPosition(), world, 0,
					(short) ItemEntity.defaultDuration, Skin.DOWN, ItemEntity.defaultPickupDelay));
		}
	}

	@Override
	public void die(UID killer) {
		die();
	}

	@Override
	public void updateGraphics() {

//		graphics.rotate(theta, getBounds().getWidth() / 2, getBounds().getHeight() / 2);
//
//		graphics.drawImage(texture, 0, 0, world);
//
//		graphics.rotate(Math.toRadians(360) - theta, getBounds().getWidth() / 2, getBounds().getHeight() / 2);
//
//		graphics.dispose();
	}
	
	@Override
	public void resetSprite() {}

	/**
	 * Returns the item if it is supposed to spawn after shooting
	 * 
	 * @return
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Returns the name of the entity that has shot the projectile, to display it
	 * later in the death messages
	 * 
	 * @return
	 */
	public UID getSenderUID() {
		return from;
	}

	/**
	 * Sets the name of the entity that has shot the projectile, to display it later
	 * in the death messages
	 * 
	 * @param from
	 */
	public void setSenderUID(UID from) {
		this.from = from;
	}
	

	@Override
	public void onRenderedTick(int tick) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRandomTick() {
		// TODO Auto-generated method stub
		
	}

}
