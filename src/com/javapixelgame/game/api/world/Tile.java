package com.javapixelgame.game.api.world;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Tile {
	
	private ImageIcon icon;
	private boolean walkable;
	private String name;
	private boolean blockAll = false;
	public Tile(ImageIcon icon, boolean walkable,String name) {
		this.setIcon(icon);
		this.setWalkable(walkable);
		this.setName(name);
	}
	
	public void scale(int width, int height) {
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		buff.createGraphics().drawImage(icon.getImage(), 0, 0,width,height, null);
		icon = new ImageIcon(buff);
	}
	
	
	public boolean isWalkable() {
		return walkable;
	}
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}


	public ImageIcon getIcon() {
		return icon;
	}


	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isBlockAll() {
		return blockAll;
	}


	public void setBlockAll(boolean blockAll) {
		this.blockAll = blockAll;
	}
}
