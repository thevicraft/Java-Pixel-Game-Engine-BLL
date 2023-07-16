package com.javapixelgame.game.api.entity;

import java.awt.AlphaComposite;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.coordinatesystem.CPoint;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.item.Item;
import com.javapixelgame.game.api.obstacle.Obstacle;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;

public class ItemEntity extends Entity {
	private static final long serialVersionUID = 1L;
	protected double borderFactor = 0;
	protected short duration;
	protected Item item;
	private int animOffset = 0;

	private boolean hasDuration = true;
	private boolean canPickup = false;
	private int pickupDelay;

	private double texture_ratio;

	public static final int defaultPickupDelay = 40;
	public static final short defaultDuration = 6000;

	public ItemEntity(Item item, CPoint spawn, World world, double speedMeterPerTick, short duration, int direction,
			int pickupDelay) {

		super(item.getRelativeSize(), spawn, item.getTexture(), world, speedMeterPerTick, "", 0);
		this.item = item;
		borderFactor = 0;
		this.pickupDelay = pickupDelay;

		hasSkin = false;

//		this.height = world.getPixelLength(sizeInMeter);

//		aspect_ratio = (double) this.item.getIcon().getIconWidth() / (double) this.item.getIcon().getIconHeight();

//		this.width = (int) (this.height * aspect_ratio);
		this.width = this.height;

		texture_ratio = aspect_ratio;
		aspect_ratio = 1;

		xBorder = 0;
		yBorder = 0;
		setBounds(0, 0, this.width, this.height);
		this.toDraw = this.item.getTexture().getImage();
		if (this.duration == Short.MAX_VALUE)
			hasDuration = false;

		this.duration = duration;
		skinDirection = direction;

		animOffset = (int) (world.getPixelLength(0.5) / (double) 20);
		if (animOffset < 1)
			animOffset = 1;

		setInvulnerable(true);

		getCollision().setBlockAll(false);
		getCollision().setWalkable(true);

		super.resetSprite();

		graphics.rotate(Math.toRadians(45), width / 2, height / 2);

		if (texture_ratio > 1) {
			// width is longer than height
			graphics.drawImage(getToDraw(), 0, (int) (height / 2 - (width / texture_ratio) / 2), width,
					(int) (width / texture_ratio), world);
		} else if (texture_ratio < 1) {
			// height is longer than width
			graphics.drawImage(getToDraw(), (int) (width / 2 - (height * texture_ratio) / 2), 0,
					(int) (height * texture_ratio), height, world);
		} else {
			graphics.drawImage(getToDraw(), 0, 0, width, height, world);
		}

		getRegistry().setRegistryID("item");
	}

	@Override
	public void setPosition(CPoint pos) {

		position = getWorld().getCoords().check(pos);
//		Point exactPoint = getWorld().getCoords().getAbsolutePoint(getPosition());
//		this.setBounds(exactPoint.x - (width + 2 * xBorder) / 2, exactPoint.y - (height + 2 * yBorder) / 2,
//				width + 2 * xBorder, height + 2 * yBorder);
		updateBounds();

	}
	
	@Override
	protected boolean collide(Objective obj) {
		if (!(obj instanceof Obstacle))
			return false;
		return obj.getCollision().isBlockAll() || !obj.getCollision().isWalkable();
	}

	@Override
	public void onEntityTick(int tick) {
		if (hasDuration) {
			if (duration <= 0) {
				die();
			} else {
				duration -= 1;
			}
		}

		if (pickupDelay <= 0) {
			canPickup = true;
		} else {
			pickupDelay -= 1;
		}

		double minor = 0.025;

		if (getSpeedMeterPerTick() > 0) {
			setSpeedMeterPerTick(getSpeedMeterPerTick() - minor);
		} else if (getSpeedMeterPerTick() < 0) {
			setSpeedMeterPerTick(0);
		}

		if (getSpeedMeterPerTick() > 0) {
			switch (skinDirection) {
			case Skin.UP:
				setxVelocity(0);
				setyVelocity(getPixelSpeedPerTick());
				break;
			case Skin.DOWN:
				setxVelocity(0);
				setyVelocity(-getPixelSpeedPerTick());
				break;
			case Skin.LEFT:
				setxVelocity(-getPixelSpeedPerTick());
				setyVelocity(0);
				break;
			case Skin.RIGHT:
				setxVelocity(getPixelSpeedPerTick());
				setyVelocity(0);
				break;
			}
		} else {
			setxVelocity(0);
			setyVelocity(0);
		}


//		if (tick == 1)
//			animOffset = animOffset * (-1);
		if(tick % 2 == 0)
			updateAlphaComposite();

//		setPosition(new CPoint(getPosition().x + getxVelocity(),
//				getPosition().y + (getSpeedMeterPerTick() == 0 ? animOffset : 0) + getyVelocity()));


	}

	@Override
	protected void resizeLoad(double sizeInMeter, boolean skinyes, double borderfactor) {
		super.resizeLoad(sizeInMeter, skinyes, 0);

		this.sizeInMeter = sizeInMeter;
		this.height = (int) (Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)) * sizeInMeter);
		if (!hasSkin)
			this.width = (int) (this.height * aspect_ratio);
		else
			this.width = (int) (this.height * aspect_ratio);

		xBorder = 0;
		yBorder = 0;

//		setPosition(getPosition());

//		collision.update();

		// updates the bounds
		getBounds().setSize(width + 2 * xBorder, height + 2 * yBorder);

		if (!skinyes) {
			texture.optimize((int) (height * texture_ratio), height);
			toDraw = texture.getImage();
		} else {
			skin.optimize(width, height);
			toDraw = skin.getSkinImage(getSkinDirection());
		}
//		System.out.println(width+","+height);

		resetSprite();
		updateGraphics();

		if (getSprite().getWidth() != width + 2 * xBorder)
			Console.output("no");
	}

	private float alpha = 0.5f;
	private float jump = 0.05f;

	private float min_alpha = 0.5f;
	private float max_alpha = 1f;
	
	private void updateAlphaComposite() {

		alpha += jump;

		if (alpha <= min_alpha) {
			jump = Math.abs(jump);
			alpha = min_alpha;
		}
		if (alpha >= max_alpha) {
			jump = -Math.abs(jump);
			alpha = max_alpha;
		}

	}

	@Override
	public void updateGraphics() {
//		graphics.fillRect(0, 0, width, height);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		graphics.rotate(Math.toRadians(45), width / 2, height / 2);

		if (texture_ratio > 1) {
			// width is longer than height
			graphics.drawImage(getToDraw(), 0, (int) (height / 2 - (width / texture_ratio) / 2), width,
					(int) (width / texture_ratio), world);
		} else if (texture_ratio < 1) {
			// height is longer than width
			graphics.drawImage(getToDraw(), (int) (width / 2 - (height * texture_ratio) / 2), 0,
					(int) (height * texture_ratio), height, world);
		} else {
			graphics.drawImage(getToDraw(), 0, 0, width, height, world);
		}
	}

//	@Override
//	public void resetSprite() {}

	public Item transfer() {
		die();
		return item;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public boolean canPickup() {
		return canPickup;
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
