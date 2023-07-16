package com.javapixelgame.game.resourcehandling.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.Player;
import com.javapixelgame.game.api.entity.Entity;
import com.javapixelgame.game.api.entity.NPC;
import com.javapixelgame.game.api.graphics.WorldModel;
import com.javapixelgame.game.api.obstacle.Obstacle;
import com.javapixelgame.game.api.world.World;
import com.javapixelgame.game.api.world.WorldGenerator;
import com.javapixelgame.game.handling.ConfigHandler;
import com.javapixelgame.game.log.Console;

public class WorldInstance {

	public static String SavesDirectory = "saves/";

	public static void save(World world) {
		String name = world.getSaveName();
		createWorldSaveFolder(name);
		try {
			ObjectOutputStream output = new ObjectOutputStream(
					new FileOutputStream(SavesDirectory + name + "/objects.dat"));
			output.writeObject(world.getGameObjectives());
			output.writeObject(world.getModel().name);
			output.writeObject(world.getTime().getTickTime());
			output.close();
		} catch (Exception e) {
			Console.error("Unable to save " + name + "; errors occured...");
			e.printStackTrace();
			return;
		}
		if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
			Console.error("Successfully saved World " + name);
	}
	
	public static World generate(String name, int width, int height) {
		World newInstance = WorldGenerator.generateTestMap(width, height);
		newInstance.setSaveName(name);
		save(newInstance);
		return newInstance;
	}

	@SuppressWarnings("unchecked")
	public static World load(String name, int width, int height) {

		List<Objective> obj = new ArrayList<>();
		ObjectInputStream input = null;
		String model = null;
		int time = 0;
		try {
			input = new ObjectInputStream(new FileInputStream(SavesDirectory + name + "/objects.dat"));
			obj = (List<Objective>) input.readObject();
			model = (String) input.readObject();

			time = (int) input.readObject();
			input.close();

		} catch (Exception e1) {
			Console.error("Unable to load " + name + "; errors occured...");
			e1.printStackTrace();
		}

		
		WorldModel worldmodel = null;
		try {
			worldmodel = WorldPackage.loadWorldModel(model);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidWorldPackageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		World worldInstance = new World(worldmodel,
				Integer.parseInt(ConfigHandler.getConfig(ConfigHandler.map_scale)), width, height);

		worldInstance.getTime().setTickTime(time);

		obj.forEach(o -> {
			if (o instanceof Player) {
				worldInstance.addPlayer((Player) o);
			} else
				worldInstance.addObjective(o);
			o.setWorld(worldInstance);
			if (o instanceof Entity)
				((Entity) o).setPosition(o.getPosition());
			if (o instanceof Obstacle)
				((Obstacle) o).setPosition(o.getPosition());
			if (o instanceof NPC && ((NPC) o).getHandItem() != null)
				((NPC) o).getHandItem().apply((NPC) o);
		});
		worldInstance.getPlayer().setItemHoldHeight(0.6);
		worldInstance.getPlayer().setMiscBarPolicy(NPC.SHOW_MISC_BAR_NEVER);
		
		if (ConfigHandler.getConfig(ConfigHandler.debug).equals("true"))
			Console.error("Successfully loaded World " + name);
		worldInstance.setSaveName(name);
		return worldInstance;
	}
	
	private static void checkSavesFolder() {
		File theDir = null;

			theDir = new File(SavesDirectory);
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
	}
	
	private static void createWorldSaveFolder(String name) {
		checkSavesFolder();
		File theDir = null;
			theDir = new File(SavesDirectory + name);

		if (!theDir.exists()){
		    theDir.mkdirs();
		    return;
		}
	}
	
	public static File getSaveDirectory() {
		checkSavesFolder();
		return new File(SavesDirectory);
	}
	
	public static boolean proofWorldSaveValidity(String name) {
		return new File(SavesDirectory + name +"/objects.dat").exists();
	}

}
