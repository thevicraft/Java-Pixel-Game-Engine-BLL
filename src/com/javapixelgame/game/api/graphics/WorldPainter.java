package com.javapixelgame.game.api.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.SwingWorker;

import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;
import com.javapixelgame.game.util.ImageUtil;

public class WorldPainter {
	private World world;
	private WorldModel model;
	private int pixelPerImage;
	private BufferedImage tileSet;

	public WorldPainter(World world, WorldModel model, int pixelPerTexture) {
		this.world = world;
		this.model = model;
		this.pixelPerImage = pixelPerTexture;
		initWorld();
		scaleTiles();
		paintTileSet();
	}

	private void initWorld() {
		world.map_width = model.getRow(0).size() * pixelPerImage;
		world.map_height = model.rows() * pixelPerImage;
		world.aspect_ratio = (double) this.world.map_width / (double) this.world.map_height;
		world.widthToMapMeterAspectRatio = (double) pixelPerImage / (double) world.map_width;

	}

	private void scaleTiles() {
		while (model.hasNextRow()) {
			xset = 0;
			model.nextRow().forEach(tile -> {
				tile.scale(pixelPerImage, pixelPerImage);
			});
		}
		model.reset();
	}

	private int yset = 0;
	private int xset = 0;

	private void paintTileSet() {
		tileSet = ImageUtil.getFormatBufferedImage(world.map_width, world.map_height, Transparency.OPAQUE);

		Graphics2D g = getTileSet().createGraphics();
		long start = System.nanoTime();
		yset = 0;
		while (model.hasNextRow()) {
			xset = 0;
			model.nextRow().forEach(tile -> {
				g.drawImage(tile.getIcon().getImage(), xset, yset, world);
				xset += pixelPerImage;
			});

			yset += pixelPerImage;
		}
		model.reset();
		if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
			Console.output((System.nanoTime() - start) * 1e-6 + " milliseconds taken to prepare map!");
//		updateBackground();
	}

	public void paint(Graphics2D g) {
		
		Rectangle render = world.getCamera().getRenderArea();
		
		g.drawImage(getTileSet(), 0, 0, world.getWidth(), world.getHeight(), render.x,
				render.y, render.x + render.width, render.y + render.height, world);
		
		
//		PerformanceUtil.start();
//		g.drawImage(backgroundBuffer, 0, 0, world);
//		PerformanceUtil.stop();
//		updateBackground();
	}
	private BufferedImage backgroundBuffer;
	public void updateBackground() {
			backgroundBuffer = ImageUtil.getFormatBufferedImage(world.getWidth(), world.getHeight(), Transparency.OPAQUE);
			Rectangle render = world.getCamera().getRenderArea();
			
			backgroundBuffer.createGraphics().drawImage(getTileSet(), 0, 0, world.getWidth(), world.getHeight(), render.x,
					render.y, render.x + render.width, render.y + render.height, world);
	}

	public BufferedImage getTileSet() {
		return tileSet;
	}
	

}
