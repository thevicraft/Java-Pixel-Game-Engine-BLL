package com.javapixelgame.game.resourcehandling.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;

import com.javapixelgame.game.api.graphics.WorldModel;
import com.javapixelgame.game.api.world.Tile;
import com.javapixelgame.game.resourcehandling.Images;
import com.javapixelgame.game.util.ResourceUtil;

public class WorldPackage {

	public static WorldModel loadWorldModel(String packageName)
			throws URISyntaxException, InvalidWorldPackageException {

		File file = null;
		String path = "/models/"+packageName+"/";

		try {
//			file = new File(getResource(path).getFile());
//			file = new File(new URI(getResource(path).toString().replace(" ","%20")).getSchemeSpecificPart());
			file = ResourceUtil.getResourceFile(path);
		} catch (NullPointerException e) {
			throw new InvalidWorldPackageException(packageName);
		}
		
//		if (!file.isDirectory()) {
//			throw new InvalidWorldPackageException(packageName);
//		}

		Scanner scanner = null;

		List<String> lines = new ArrayList<>();
		List<String> declarationLines = new ArrayList<>();
		List<String> walkable = new ArrayList<>();
		List<String> blocking = new ArrayList<>();

		List<List<Tile>> tiles = new ArrayList<>();

//		File worldConfig = new File(getResource(path + "world.csv").toURI());
//		File textureConfig = new File(getResource(path + "textures.txt").toURI());
//		File walkableConfig = new File(getResource(path + "walkable.txt").toURI());
//		File blockingConfig = new File(getResource(path + "blockall.txt").toURI());
		InputStream worldConfig = WorldPackage.class.getResourceAsStream(path + "world.csv");
		InputStream textureConfig = WorldPackage.class.getResourceAsStream(path + "textures.txt");
		InputStream walkableConfig = WorldPackage.class.getResourceAsStream(path + "walkable.txt");
		InputStream blockingConfig = WorldPackage.class.getResourceAsStream(path + "blockall.txt");
		int worldLines = 0;

		scanner = new Scanner(worldConfig);
		while (scanner.hasNext()) {

			String l = scanner.nextLine();
			if (l.length() > 0) {
				lines.add(l);
			}
		}
		worldLines = lines.size();
		scanner.close();
		scanner = new Scanner(textureConfig);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (line.length() > 0) {
				declarationLines.add(line);
			}
		}
		scanner.close();
		scanner = new Scanner(walkableConfig);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (line.length() > 0) {
				walkable.add(line);
			}
		}
		scanner.close();

		scanner = new Scanner(blockingConfig);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (line.length() > 0) {
				blocking.add(line);
			}
		}
		scanner.close();

		// ----------------------------------------------------
		scanner.close();
		for (int i = 0; i < worldLines; i++) {
			tiles.add(new ArrayList<>());
			for (String e : lines.get(i).split(";")) {
				tiles.get(i).add(new Tile(null, false, e.trim()));
			}
		}

		for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).forEach(er -> {
				declarationLines.forEach(texture -> {
					if (texture.equals(er.getName() + ".png")) {
						ImageIcon icon = new ImageIcon(getResource(path + "textures/" + texture));
						if ((icon != null) && (icon.getIconWidth() > 0)) {
							er.setIcon(icon);
						} else {
							er.setIcon(Images.unknown);
						}
						er.setWalkable(walkable.contains(er.getName()));
						er.setBlockAll(blocking.contains(er.getName()));
					}
				});

			});
		}
		WorldModel w = new WorldModel(tiles);
		w.name = packageName;
		return w;

	}
	
	
	private static URL getResource(String filename) {
		return WorldPackage.class.getResource(filename);
	}
}
