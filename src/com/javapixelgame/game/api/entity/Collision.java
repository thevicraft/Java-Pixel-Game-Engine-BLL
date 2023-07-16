package com.javapixelgame.game.api.entity;

import java.awt.Point;
import java.awt.Rectangle;

import com.javapixelgame.game.api.Player;
import com.javapixelgame.game.api.obstacle.Obstacle;

/**
 * Collision class that manages the tile collision or the entity collision in
 * the game. It corresponds with the <strong>objective's bounds</strong>, being
 * <strong>painting coordinates</strong>.
 * 
 * @author thevicraft
 *
 */
public class Collision extends Rectangle {
	private static final long serialVersionUID = 1L;
	private Entity entity;
	private Obstacle obstacle;
	private Player player;
	private int mode;
	private boolean walkable = false;
	private boolean blockAll = false;
	private double xfrom = 0;
	private double xto = 1;
	private double yfrom = 0;
	private double yto = 1;

	public Collision(Entity entity) {
		mode = 0;
		this.entity = entity;
		update();
	}

	public Collision(Obstacle obstacle) {
		mode = 1;
		this.obstacle = obstacle;
		update();
	}

	public Collision(Player player) {
		mode = 2;
		this.player = player;
		update();
	}

	/**
	 * <strong>Updates</strong> the {@link Objective} {@link Collision} box on its
	 * current location and size. <strong>This should happen every game tick on
	 * every game Objective</strong>
	 */
	public void update() {
		switch (mode) {
		case 0:
			entity.updateBounds();
			setBounds(entity.getBounds().x + entity.xBorder + (int) (entity.getEntityWidth() * xfrom),
					entity.getBounds().y + entity.yBorder + (int) (entity.getEntityHeight() * yfrom),
					(int) (entity.getEntityWidth() * xto), (int) (entity.getEntityHeight() * yto));
			break;
		case 1:
			setBounds(obstacle.getBounds().x + obstacle.xBorder + (int) (obstacle.getObstacleWidth() * xfrom),
					obstacle.getBounds().y + obstacle.yBorder + (int) (obstacle.getObstacleHeight() * yfrom),
					(int) (obstacle.getObstacleWidth() * xto), (int) (obstacle.getObstacleHeight() * yto));
			break;
		case 2:
			player.updateBounds();
			Point p = player.getWorld().getCoords().getAbsolutePoint(player.getPosition());
			setBounds(
					p.x - (int) player.getBounds().getWidth() / 2 + player.xBorder
							+ (int) (player.getEntityWidth() * xfrom),
					p.y - (int) player.getBounds().getHeight() / 2 + player.yBorder
							+ (int) (player.getEntityHeight() * yfrom),
					(int) (player.getEntityWidth() * xto), (int) (player.getEntityHeight() * yto));
			break;
		}
	}

	/**
	 * Manually sets the borders of the collision box. It is measured in percentage
	 * of the graphical component of the {@link Objective}. The interval should be
	 * between <strong>0</strong> and <strong>1</strong>.
	 * <strong>IMPORTANT:</strong>
	 * </p>
	 * </p>
	 * The <strong>(0|0)</strong> origin is on the upper left corner!!
	 * </p>
	 * 
	 * @param x0      - the x value of the box corner ( 0 - 1 !)
	 * @param y0      - the y value of the box corner ( 0 - 1 !)
	 * @param xLength - the length of the collision box <strong>relative</strong> to
	 *                the graphics ( 0 - 1 !)
	 * @param yLength - the height of the collision box <strong>relative</strong> to
	 *                the graphics ( 0 - 1 !)
	 */
	public void setBorders(double x0, double y0, double xLength, double yLength) {
		this.xfrom = x0;
		this.yfrom = y0;
		this.xto = xLength;
		this.yto = yLength;
	}

	/**
	 * <strong>Checks</strong> if this {@link Collision} is
	 * <strong>overlapping</strong> with another one.
	 * 
	 * @param c - Collision to check if it is colliding with this object one's
	 * @return
	 */
	public boolean overlaps(Collision c) {
		return intersects(c);
	}

	/**
	 * <strong>BlockAll</strong> is an {@link Collision} attribute that
	 * <strong>blocks every</strong> other Collision. It is {@link Boolean}.
	 * 
	 * @return boolean
	 */
	public boolean isBlockAll() {
		return blockAll;
	}

	/**
	 * <strong>BlockAll</strong> is an {@link Collision} attribute that
	 * <strong>blocks every</strong> other Collision. It is {@link Boolean}.
	 * 
	 * @param boolean
	 */
	public void setBlockAll(boolean blockAll) {
		this.blockAll = blockAll;
	}

	/**
	 * <strong>Walkable</strong> is an {@link Collision} attribute that allows
	 * simple {@link Entity} or more complex {@link NPC} to <strong>be able to
	 * collide</strong> with this Collision. It is {@link Boolean}.
	 * 
	 * @return
	 */
	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * <strong>Walkable</strong> is an {@link Collision} attribute that allows
	 * simple {@link Entity} or more complex {@link NPC} to <strong>be able to
	 * collide</strong> with this Collision. It is {@link Boolean}.
	 * 
	 * @param boolean
	 */
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
}
