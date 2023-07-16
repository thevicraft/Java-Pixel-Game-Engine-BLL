package com.javapixelgame.game.util;

import java.awt.Component;
import java.awt.Point;

public class ComponentUtil {
	
	public static Point placeCentered(Component child, Component parent) {
		int x = parent.getWidth()/2 - child.getWidth()/2;
		int y = parent.getHeight()/2 - child.getHeight()/2;
		return new Point(x,y);
	}
	
	public static Point placeLeft(Component child, Component parent) {
		int x = 0;
		int y = parent.getHeight()/2 - child.getHeight()/2;
		return new Point(x,y);
	}
	
	public static Point placeRight(Component child, Component parent) {
		int x = parent.getWidth() - child.getWidth();
		int y = parent.getHeight()/2 - child.getHeight()/2;
		return new Point(x,y);
	}
	
}
