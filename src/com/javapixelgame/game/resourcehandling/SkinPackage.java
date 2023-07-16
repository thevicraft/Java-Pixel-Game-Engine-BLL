package com.javapixelgame.game.resourcehandling;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;

import com.javapixelgame.game.api.graphics.SkinModel;
import com.javapixelgame.game.resourcehandling.world.WorldPackage;

public class SkinPackage {

	public static SkinModel loadSkinModel(String packageName) {
//		File file = new File(packageName);
		
		if(packageName.equals("notfound"))return getNotFound();
		
		String path;

		path = "/skins/" + packageName + "/";

//		File file;
//		try {
//			file = new File(SkinPackage.class.getResource(path).toURI());
//		} catch (URISyntaxException e1) {
//			e1.printStackTrace();
//			return getNotFound();
//		}
//		if (!file.isDirectory())
//			return getNotFound();

//			path = file.getAbsolutePath();
		
		String[] directions = { "right/", "left/", "up/", "down/" };
		Scanner scanner = null;
		
		List<ImageIcon> right = new ArrayList<ImageIcon>();
		List<ImageIcon> left = new ArrayList<ImageIcon>();
		List<ImageIcon> up = new ArrayList<ImageIcon>();
		List<ImageIcon> down = new ArrayList<ImageIcon>();

		List<String> lines = new ArrayList<>();

		// ------------------------------------------------------------------------------------------------------
		try {
//				File txtConfig = new File(path + "/" + directions[0] + "/" + "declaration.txt");
//			File txtConfig = new File(SkinPackage.class.getResource(path + directions[0] + "declaration.txt").toURI());
			InputStream txtConfig = WorldPackage.class.getResourceAsStream(path + directions[0] + "declaration.txt");
			scanner = new Scanner(txtConfig);
			while (scanner.hasNext()) {

				String line = scanner.nextLine();
				if (line.length() > 0) {
					lines.add(line);
				}
			}
			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		lines.forEach(line -> {
			ImageIcon icon = new ImageIcon(SkinPackage.class.getResource(path + directions[0] + line));
			if (icon.getIconWidth() > 0) {
				right.add(icon);
			} else {
				errorCouldNotBeFound(line, path + directions[0]);
			}
		});
		lines.clear();
		// ------------------------------------------------------------------------------------------------------
		try {
//			File txtConfig = new File(SkinPackage.class.getResource(path + directions[1] + "declaration.txt").toURI());
			InputStream txtConfig = WorldPackage.class.getResourceAsStream(path + directions[1] + "declaration.txt");
			scanner = new Scanner(txtConfig);
			while (scanner.hasNext()) {

				String line = scanner.nextLine();
				if (line.length() > 0) {
					lines.add(line);
				}
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lines.forEach(line -> {
			ImageIcon icon = new ImageIcon(SkinPackage.class.getResource(path + directions[1] + line));
			if (icon.getIconWidth() > 0) {
				left.add(icon);
			} else {
				errorCouldNotBeFound(line, path + directions[1]);
			}
		});
		lines.clear();
		// ------------------------------------------------------------------------------------------------------
		try {
//			File txtConfig = new File(SkinPackage.class.getResource(path + directions[2] + "declaration.txt").toURI());
			InputStream txtConfig = WorldPackage.class.getResourceAsStream(path + directions[2] + "declaration.txt");
			scanner = new Scanner(txtConfig);
			while (scanner.hasNext()) {

				String line = scanner.nextLine();
				if (line.length() > 0) {
					lines.add(line);
				}
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lines.forEach(line -> {
			ImageIcon icon = new ImageIcon(SkinPackage.class.getResource(path + directions[2] + line));
			if (icon.getIconWidth() > 0) {
				up.add(icon);
			} else {
				errorCouldNotBeFound(line, path + directions[2]);
			}
		});
		lines.clear();
		// ------------------------------------------------------------------------------------------------------
		try {
//			File txtConfig = new File(SkinPackage.class.getResource(path + directions[3] + "declaration.txt").toURI());
			InputStream txtConfig = WorldPackage.class.getResourceAsStream(path + directions[3] + "declaration.txt");
			scanner = new Scanner(txtConfig);
			while (scanner.hasNext()) {

				String line = scanner.nextLine();
				if (line.length() > 0) {
					lines.add(line);
				}
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lines.forEach(line -> {
			ImageIcon icon = new ImageIcon(SkinPackage.class.getResource(path + directions[3] + line));
			if (icon.getIconWidth() > 0) {
				down.add(icon);
			} else {
				errorCouldNotBeFound(line, path + directions[3]);
			}
		});
		lines.clear();
		// ------------------------------------------------------------------------------------------------------

		List<ImageIcon> notFound = new ArrayList<>();
		notFound.add(Images.unknown);
		SkinModel model = new SkinModel(
				right.size() > 0 ? right : notFound, 
				left.size() > 0 ? left : notFound,
				up.size() > 0 ? up : notFound, 
				down.size() > 0 ? down : notFound
		);

		model.name = packageName;
		return model;

	}

	public static SkinModel getNotFound() {
		List<ImageIcon> notFound = new ArrayList<>();
		notFound.add(Images.unknown);
		SkinModel nf = new SkinModel(notFound, notFound, notFound, notFound);
		nf.name = "notfound";
		return nf;
	}

	private static void errorCouldNotBeFound(String fileName, String path) {
		System.out.println("<===================>");
		System.out.println("Error: " + fileName + " could not be located in " + path);
	}

}
