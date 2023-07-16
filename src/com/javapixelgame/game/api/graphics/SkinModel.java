package com.javapixelgame.game.api.graphics;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class SkinModel {
	private List<Image> right = new ArrayList<>();
	private List<Image> left = new ArrayList<>();
	private List<Image> up = new ArrayList<>();
	private List<Image> down = new ArrayList<>();
	private int leftSize;
	private int rightSize;
	private int upSize;
	private int downSize;
	private double aspect_ratio;
	public String name;
	
	public SkinModel(List<ImageIcon> right, List<ImageIcon> left, List<ImageIcon> up, List<ImageIcon> down) {
		right.forEach(iterator -> {
			this.right.add(iterator.getImage());
		});
		left.forEach(iterator -> {
			this.left.add(iterator.getImage());
		});
		down.forEach(iterator -> {
			this.down.add(iterator.getImage());
		});
		up.forEach(iterator -> {
			this.up.add(iterator.getImage());
		});
		leftSize = left.size();
		rightSize = right.size();
		upSize = up.size();
		downSize = down.size();
		aspect_ratio = (double) down.get(0).getIconWidth() / (double)  down.get(0).getIconHeight();
	}

	public List<Image> getRight() {
		return right;
	}

	public List<Image> getLeft() {
		return left;
	}

	public List<Image> getUp() {
		return up;
	}

	public List<Image> getDown() {
		return down;
	}

	public int getLeftSize() {
		return leftSize;
	}

	public int getRightSize() {
		return rightSize;
	}

	public int getUpSize() {
		return upSize;
	}

	public int getDownSize() {
		return downSize;
	}

	public double getAspect_ratio() {
		return aspect_ratio;
	}
}
