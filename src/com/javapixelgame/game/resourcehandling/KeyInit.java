package com.javapixelgame.game.resourcehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.javapixelgame.game.keyboard.KeyEventHandler;
@Deprecated
public class KeyInit {
	@Deprecated
	public static final String filename = "key.txt";
@Deprecated
	public static String[][] init() {
		File keys = null;
		try {
			keys = new File(filename);
			if(!keys.exists())
				throw new NullPointerException();
		} catch (NullPointerException e) {
			makeFile();
			return KeyEventHandler.config;
		}

		if (!keys.isDirectory()) {

			Scanner scanner = null;

			List<String[]> keyDeclaration = new ArrayList<>();
			List<String> lines = new ArrayList<>();

			// ------------------------------------------------------------------------------------------------------
			try {
				scanner = new Scanner(keys);
				while (scanner.hasNext()) {

					String line = scanner.nextLine();
					if (line.length() > 0) {
						lines.add(line);
//						System.out.println(line);
					}
				}
			} catch (FileNotFoundException e) {
			}
			
			if(lines.size() != KeyEventHandler.config.length) {
				makeFile();
				return KeyEventHandler.config;
			}
			
			lines.forEach(line -> {
				String[] insert = line.split("=");
				insert[0] = insert[0].trim();
				insert[1] = insert[1].trim();
				keyDeclaration.add(insert);
			});
			lines.clear();
			// ------------------------------------------------------------------------------------------------------

			return keyDeclaration.toArray(new String[0][0]);

		}

		return null;
	}
	
	private static void makeFile() {
		PrintWriter writer = null;
		File keyFile = new File(filename);
		try {
			keyFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer = new PrintWriter(keyFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < KeyEventHandler.config.length; i++) {
			String t = KeyEventHandler.config[i][0]+"="+KeyEventHandler.config[i][1];
			writer.println(t);
		}
		writer.close();
	}
}
