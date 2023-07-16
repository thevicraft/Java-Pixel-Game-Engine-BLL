package com.javapixelgame.game.api.item;

import java.awt.Image;
import java.awt.Point;

import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.graphics.Skin;
import com.javapixelgame.game.api.graphics.Texture;

public class Item implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private double relativeSize = 0;

	private double aspect_ratio = 0;

	private double holdingX = 0;
	private double holdingY = 0;
	private int damage = 0;
	public boolean shouldAnimateSwing = true;
	private NPC entity;
	
	private String displayName = "undefined";

	private Texture texture = null;
	public static final int plainDamage = 1;
	public static final int plainAttackCooldown = 2;
	
	public Item(Texture texture, double sizeInMeter, double holdingX, double holdingY, int attackCooldown, int damage) {
		this.setDamage(damage);
		this.attackCooldown = attackCooldown;
		relativeSize = sizeInMeter;
		this.holdingX = holdingX;
		this.holdingY = holdingY;

		this.texture = texture;
		aspect_ratio = texture.getAspect();
	}
	

	private int attackCooldown = 0;

	public void onAttack() {

	}
	
	public void onSelection() {
		
	}
	
	public void onUnselection() {
		applied = false;
	}

	private int itemWidth = 0;
	private int itemHeight = 0;
	private Point itemPivotPoint = new Point(0,0);
	private int itemX = 0;
	private int itemY = 0;
	
	private boolean applied = false;
	
	public void apply(NPC entity) {
		applied = true;
		this.setEntity(entity);

		itemHeight = (int) (relativeSize * entity.getWorld().widthToMapMeterAspectRatio * entity.getWorld().map_width);

		itemWidth = (int) (itemHeight * aspect_ratio);

		if ((itemWidth > 0) && (itemHeight > 0)) {
			// hasWeapon = true;

			int xStore = (int) (0.5 * entity.getEntityWidth());
			int yStore = (int) (entity.getItemHoldHeight() * entity.getEntityHeight());

			itemX = xStore - (int) (itemWidth * holdingX);
			itemY = yStore - (int) (itemHeight * holdingY);

			itemPivotPoint = new Point((int) (itemX + holdingX * itemWidth), (int) (itemY + holdingY * itemHeight));
		}
		onSelection();
	}

	public double getAspect_ratio() {
		return aspect_ratio;
	}

	public Image getImageOnDirection() {
		if(entity == null)
			return getTexture().getImage();
		return entity.getSkinDirection() == Skin.LEFT ? getTexture().getMirroredImage(): getTexture().getImage();
	}


	public Texture getTexture() {
		return texture;
	}

	public double getRelativeSize() {
		return relativeSize;
	}

	public int getItemWidth() {
		return itemWidth;
	}

	public int getItemHeight() {
		return itemHeight;
	}

	public Point getItemPivotPoint() {
		return itemPivotPoint;
	}

	public int getItemX() {
		return itemX;
	}

	public int getItemY() {
		return itemY;
	}

	public int getAttackCooldown() {
		return attackCooldown;
	}

	public Item setAttackCooldown(int attackCooldown) {
		this.attackCooldown = attackCooldown;
		return this;
	}

	public int getDamage() {
		return damage;
	}

	public Item setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	public NPC getEntity() {
		return entity;
	}

	public Item setEntity(NPC entity) {
		this.entity = entity;
		return this;
	}

	public boolean isApplied() {
		return applied;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Item setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}
}
