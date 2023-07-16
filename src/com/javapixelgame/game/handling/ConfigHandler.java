package com.javapixelgame.game.handling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.javapixelgame.game.log.Console;

public class ConfigHandler {

	private static String[][] config = {
			{ "fullscreen", "false"},
			{ "width", "1280" },
			{ "height", "900" },
			{ "debug", "false" },
			{ "fps", "60" },
			{ "gui_scale", "3" },
			{ "map_scale", "50" },
			{ "show_minimap", "false" },
			{ "show_particles", "true" },
			{ "pause_unfocused", "true" },
			{ "animate_gauges", "true" },
	};

	public static final int fullscreen = 0;
	public static final int width = 1;
	public static final int height = 2;
	public static final int debug = 3;
	public static final int fps = 4;
	public static final int gui_scale = 5;
	public static final int map_scale = 6;
	public static final int minimap = 7;
	public static final int particles = 8;
	public static final int pause_unfocused = 9;
	public static final int animate_gauges = 10;
	
	public static String getConfig(int category) {
		return config[category][1];
	}
	
	public static String getName(int category) {
		return config[category][0].replaceAll("_", " ");
	}

	public static void setConfig(int category, String value) {
		if(getConfig(debug).equals("true"))
			Console.output(config[category][0]+" "+config[category][1]+" -> "+value);
		config[category][1] = value;
	}
	
	
	public static void printConsole() {
		for(int i = 0; i < config.length; i++) {
			System.out.println(config[i][0] +"=="+ config[i][1]);
		}
	}

	public static final void loadConfig() {
		if(!new File("config.txt").exists()) {
			saveConfig();
			return;
		}
		try {
			BufferedReader buff = new BufferedReader(new FileReader("config.txt"));
			for (int i = 0; i < config.length; i++) {				
				applySetting(buff.readLine());
			}
			buff.close();
			
//			print();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void applySetting(String s) {
		if(s == null)return;
//		System.out.println(s);
		String[] str = s.split("=");
		if(str.length != 2)return;
		String category = str[0].trim();
		String value = str[1].trim();
		for (int i = 0; i < config.length; i++) {
			if (config[i][0].equals(category)) {
				config[i][1] = value;
				break;
			}
		}
	}

	public static final void saveConfig() {

		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter("config.txt"));

			for (int i = 0; i < config.length; i++) {
				buff.write(config[i][0]+"="+config[i][1]);
				buff.newLine();
			}
			

			buff.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
