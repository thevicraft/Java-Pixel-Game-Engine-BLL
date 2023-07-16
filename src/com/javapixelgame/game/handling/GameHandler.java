package com.javapixelgame.game.handling;

import java.awt.Dimension;

import com.javapixelgame.game.Main;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.resourcehandling.world.WorldInstance;

public final class GameHandler {

	private GameHandler() {
	}

	private static World world;

	public static void disposeWorld() {
		if (Main.game.gp.isRunning()) {
			new Exception("Running Game Interruption").printStackTrace();
			return;
		}
		world = null;
		System.gc();
	}

	private static Dimension size;

	public static void setSize(int width, int height) {
		size = new Dimension(width, height);
	}

	public static Dimension getSize() {
		if (size != null)
			return size;
		return Main.game.gp.getSize();
	}

	public static void loadInstance(String name) {
//		new Thread(() -> {
			world = WorldInstance.load(name, getSize().width, getSize().height);
			applyConfigs();
//		}).start();
	}

	public static void loadTestMap(String name) {
//		new Thread(() -> {
			world = WorldInstance.generate(name, getSize().width, getSize().height);
			applyConfigs();
//		}).start();
	}

	private static void applyConfigs() {
		world.renderHitboxes(ConfigHandler.getConfig(ConfigHandler.debug).equals("true"));
	}

	public static void saveInstance() {
		new Thread(() -> {
			WorldInstance.save(getWorld());
		}).start();
	}

	public static World getWorld() {
		return world;
	}
}
