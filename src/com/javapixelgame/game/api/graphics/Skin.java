package com.javapixelgame.game.api.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.javapixelgame.game.resourcehandling.Images;
import com.javapixelgame.game.resourcehandling.SkinID;
import com.javapixelgame.game.resourcehandling.SkinPackage;
import com.javapixelgame.game.util.ImageUtil;

public class Skin {
	private List<Image> left = new ArrayList<Image>();
	private List<Image> right = new ArrayList<Image>();
	private List<Image> up = new ArrayList<Image>();
	private List<Image> down = new ArrayList<Image>();
	public static final int NULL = -1;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	private int leftSize;
	private int rightSize;
	private int upSize;
	private int downSize;
	public double aspect_ratio;
	public String name;
	
	public Skin(SkinModel model) {
		name = model.name;
		left = model.getLeft();
		right = model.getRight();
		up = model.getUp();
		down = model.getDown();
		
		leftSize = model.getLeftSize();
		rightSize = model.getRightSize();
		upSize = model.getUpSize();
		downSize = model.getDownSize();
		
		aspect_ratio = model.getAspect_ratio();
	}
	
	public Skin(SkinID id) {
		this(SkinPackage.loadSkinModel(id.toString().toLowerCase()));
	}
	
	public Skin(String id) {
		this(SkinPackage.loadSkinModel(id));
	}
	
	
	public void optimize(int width, int height) {
		for(int i = 0; i < left.size(); i++) {
			left.set(i, ImageUtil.scale(left.get(i), width, height, BufferedImage.TYPE_INT_ARGB));			
		}
		for(int i = 0; i < right.size(); i++) {
			right.set(i, ImageUtil.scale(right.get(i), width, height, BufferedImage.TYPE_INT_ARGB));			
		}
		for(int i = 0; i < down.size(); i++) {
			down.set(i, ImageUtil.scale(down.get(i), width, height, BufferedImage.TYPE_INT_ARGB));			
		}
		for(int i = 0; i < up.size(); i++) {
			up.set(i, ImageUtil.scale(up.get(i), width, height, BufferedImage.TYPE_INT_ARGB));			
		}
	}
	
	
	private int leftImage = 0;
	private int rightImage = 0;
	private int downImage = 0;
	private int upImage = 0;
	
	public Image getSkinImage(final int direction) {
		switch (direction) {
		case LEFT:
			leftImage++;
			if(leftImage >= leftSize)
				leftImage = 0;
			return left.get(leftImage);
			
		case RIGHT:
			rightImage++;
			if(rightImage >= rightSize)
				rightImage = 0;
			return right.get(rightImage);
			
		case UP:
			upImage++;
			if(upImage >= upSize)
				upImage = 0;
			return up.get(upImage);
			
		case DOWN:
			downImage++;
			if(downImage >= downSize)
				downImage = 0;
			return down.get(downImage);
			
		default:
			return Images.unknown.getImage();
		}
	}
	public int leftImages() {
		return left.size();
	}
	public int rightImages() {
		return right.size();
	}
	public int upImages() {
		return up.size();
	}
	public int downImages() {
		return down.size();
	}
	
	public static int getOppositeDirection(int direction) {
		switch (direction) {
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		}
		return NULL;
	}
	
	public static int turnClockwise(int direction) {
			switch (direction) {
			case UP:
				return RIGHT;
			case DOWN:
				return LEFT;
			case LEFT:
				return UP;
			case RIGHT:
				return DOWN;
			}
			return NULL;
	}
	
	public static int turnAntiClockwise(int direction) {
		switch (direction) {
		case UP:
			return LEFT;
		case DOWN:
			return RIGHT;
		case LEFT:
			return DOWN;
		case RIGHT:
			return UP;
		}
		return NULL;
}
	
}
