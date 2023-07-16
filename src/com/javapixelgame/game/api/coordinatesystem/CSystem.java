package com.javapixelgame.game.api.coordinatesystem;

import java.awt.Point;

public class CSystem {
	private Point origin;

	private int minX;
	private int maxX;
	private int minY;
	private int maxY;

	public CSystem(Point origin, int minX, int maxX, int minY, int maxY) {
		this.origin = origin;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * Returns the Coordinate Point from the exact Window Point
	 * 
	 * @param absolutePoint - absolute Point on the JFrame / JPanel
	 * @return CPoint - relative Point of the virtual coordinate system
	 * @author thevicraft
	 */
	public CPoint getRelativePoint(Point absolutePoint) {
		int x0 = (int) origin.getX();
		int y0 = (int) origin.getY();
		return new CPoint(absolutePoint.x - x0, y0 - absolutePoint.y);
	}

	/**
	 * Returns the exact the exact Window Point from the CPoint of the virtual
	 * coordinate system
	 * 
	 * @param relativePoint - relative Point in the virtual coordinate system
	 * @return Point - exact Point on the JFrame/JPanel where the coordinate system
	 *         is applied to
	 * @author thevicraft
	 */
	public Point getAbsolutePoint(CPoint relativePoint) {
		int x = relativePoint.x;
		int y = relativePoint.y;
		if (x < minX)
			x = minX;
		if (x > maxX)
			x = maxX;
		if (y < minY)
			y = minY;
		if (y > maxY)
			y = maxY;
		
		return new CPoint(x,y).relativeTo(origin);
	}
	
	/**
	 * Checks if the parameter CPoint is withing the coordinate system and returns the corrected Point if not
	 * @param relativePoint - CPoint that will be tested
	 * @return CPoint - corrected Point
	 */
	public CPoint check(CPoint relativePoint) {
		int x = relativePoint.x;
		int y = relativePoint.y;
		if (x < minX)
			x = minX;
		if (x > maxX)
			x = maxX;
		if (y < minY)
			y = minY;
		if (y > maxY)
			y = maxY;
		return new CPoint(x,y);
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

}