package com.javapixelgame.game.api.graphics;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import com.javapixelgame.game.api.Objective;

public class Light {
//	public static final int CIRCLE = Skin.NULL;
//	public static final int LEFT = Skin.LEFT;
//	public static final int RIGHT = Skin.RIGHT;
//	public static final int UP = Skin.UP;
//	public static final int DOWN = Skin.DOWN;

	public static final String CIRCLE = "CIRCLE";
	public static final String DIRECTIONAL = "DIRECTIONAL";

	public static final Shape getLightingArea(Objective o) {


		int range = o.getWorld().getPixelLength(o.getLightRange());

		Point2D center = new Point2D.Float(o.getPosition().x - o.getWorld().getCamera().getRenderArea().x,
				(int) o.getBounds().getCenterY() - o.getWorld().getCamera().getRenderArea().y);
		if (o.getLightProperty().equals(CIRCLE) || o.getLightDirection() == Skin.NULL)
			return new Ellipse2D.Double((int) center.getX() - range, (int) center.getY() - range, range * 2, range * 2);
		
//		Area area = new Area();
		Polygon light = new Polygon();
		Point origin1 = new Point((int) center.getX(),(int) center.getY());
		Point origin2 = new Point((int) center.getX(),(int) center.getY());
		Point far1 = new Point((int) center.getX(),(int) center.getY());
		Point far2 = new Point((int) center.getX(),(int) center.getY());
		
		switch(o.getLightDirection()) {
		case Skin.UP:
			origin1.x += o.getCollision().width/2;
			origin2.x -= o.getCollision().width/2;
			// y values stay the same
			origin1.y -= o.getUpLightDistance();
			origin2.y -= o.getUpLightDistance();
			
			far1.x += o.getCollision().width;
			far2.x -= o.getCollision().width;
			
			far1.y -= range;
			far2.y -= range;
			break;
		case Skin.DOWN:
			origin1.x += o.getCollision().width/2;
			origin2.x -= o.getCollision().width/2;
			// y values stay the same
			origin1.y += o.getDownLightDistance();
			origin2.y += o.getDownLightDistance();
			
			far1.x += o.getCollision().width;
			far2.x -= o.getCollision().width;
			
			far1.y += range;
			far2.y += range;
			break;
		case Skin.LEFT:
			origin1.y += o.getCollision().height/2;
			origin2.y -= o.getCollision().height/3;
			// x values stay the same
			origin1.x -= o.getLeftLightDistance();
			origin2.x -= o.getLeftLightDistance();
			
			far1.y += o.getCollision().height;
			far2.y -= o.getCollision().height;
			
			far1.x -= range;
			far2.x -= range;
			break;
		case Skin.RIGHT:
			origin1.y += o.getCollision().height/2;
			origin2.y -= o.getCollision().height/3;
			// x values stay the same
			origin1.x += o.getRightLightDistance();
			origin2.x += o.getRightLightDistance();
			
			far1.y += o.getCollision().height;
			far2.y -= o.getCollision().height;
			
			far1.x += range;
			far2.x += range;
			break;
		}
		
		light.addPoint(origin1.x, origin1.y);
		light.addPoint(origin2.x, origin2.y);
		light.addPoint(far2.x, far2.y);
		light.addPoint(far1.x, far1.y);
		return light;
	}
}
