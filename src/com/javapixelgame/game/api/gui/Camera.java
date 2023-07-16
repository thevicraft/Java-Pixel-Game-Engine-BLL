package com.javapixelgame.game.api.gui;

import java.awt.Rectangle;

import com.javapixelgame.game.api.world.World;

/**
 * Handles the displaying of the world and the renderable area for a more
 * efficient game rendering
 * 
 * @author thevicraft
 * @see {@link World}
 *
 */
public class Camera {
	private World world;
	private Rectangle renderArea = new Rectangle(0, 0, 0, 0);
//	private Dimension dim = new Dimension(0, 0);
	private int worldX = 0;
	private int worldY = 0;
//	private Point worldLocation;

	public Camera(World world) {
		this.world = world;
	}

	/**
	 * Updates the Camera according to the players position
	 * </p>
	 * This should be executed <b>every Tick</b> to get optimal performance
	 * 
	 * @see {@link World}
	 */
	public void update() {
//		dim = getWorld().getParent().getSize();
		displayLocation();
		adjustrenderArea();
		
		getWorld().getRenderObjects().forEach(e -> e.updateRenderCorner());
	}

	private void displayLocation() {
//		worldX = (int) (dim.width / 2 - getWorld().getPlayer().getBounds().x
//				- getWorld().getPlayer().getBounds().width / 2);
//		worldY = (int) (dim.height / 2 - getWorld().getPlayer().getBounds().y
//				- getWorld().getPlayer().getBounds().height / 2);
		
		worldX = (int) (getWorld().getPlayer().getBounds().x
				+ getWorld().getPlayer().getBounds().width / 2 - world.getWidth()/2);
		worldY = (int) (getWorld().getPlayer().getBounds().y
				+ getWorld().getPlayer().getBounds().height / 2- world.getHeight()/2);
		
//		worldLocation = new Point(
//				worldX < 0 ? (worldX + getWorld().width > dim.width ? worldX : dim.width - getWorld().width) : 0,
//				worldY < 0 ? (worldY + getWorld().height > dim.height ? worldY : dim.height - getWorld().height) : 0);
		if(worldX < 0)
			worldX = 0;
		else if(-worldX + world.map_width < world.getWidth())
			worldX = world.map_width - world.getWidth();
		
		if(worldY < 0)
			worldY = 0;
		else if(-worldY + world.map_height < world.getHeight())
			worldY = world.map_height - world.getHeight();
	}

	private void adjustrenderArea() {
//		renderArea.setBounds(-worldLocation.x, -worldLocation.y, dim.width, dim.height);
		renderArea.setBounds(worldX, worldY, world.getWidth(), world.getHeight());
	}

	public World getWorld() {
		return world;
	}

	/**
	 * Returns a {@link Rectangle} that should be renderable as it is visible on
	 * screen. </p> Offside this renderable area, objects will not be visible as they are
	 * not displayed on screen.
	 * @see {@link World}
	 */
	public Rectangle getRenderArea() {
		return renderArea;
	}
}
