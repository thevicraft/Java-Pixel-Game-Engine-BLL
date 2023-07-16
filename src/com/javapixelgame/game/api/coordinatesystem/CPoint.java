package com.javapixelgame.game.api.coordinatesystem;
import java.awt.Point;

public class CPoint extends Point {
	private static final long serialVersionUID = 1L;
	public CPoint(int relativeX, int relativeY) {
		this.x = relativeX;
		this.y = relativeY;
	}

	public Point relativeTo(Point origin) {
		int x0 =(int) origin.getX();
		int y0 =(int) origin.getY();
		Point returnValue = new Point(x+x0, y0-y);
		
		return returnValue;
	}
}