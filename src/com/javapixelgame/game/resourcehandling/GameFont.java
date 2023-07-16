package com.javapixelgame.game.resourcehandling;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import com.javapixelgame.game.log.Console;

public class GameFont {

	private static Font KarmaticArcade;
	private static Font ArcadeClassic;
	private static Font RainyHearts;

	public static void load() {
		KarmaticArcade = get("karmaticarcade.ttf");
		ArcadeClassic = get("arcadeclassic.tff");
		RainyHearts = get("rainyhearts.ttf");
		
	}

	private static Font get(String name) {

		InputStream is = GameFont.class.getResourceAsStream("/fonts/" + name);

		try {
			return Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			Console.error("/fonts/" + name + " could not be loaded!");
			e.printStackTrace();
		}
		return null;
	}

	public static Font getKarmaticArcade(int style, float size) {
		return KarmaticArcade.deriveFont(style, size);
	}

	public static Font getArcadeClassic(int style, float size) {
		return ArcadeClassic.deriveFont(style, size);
	}
	
	public static Font getRainyHearts(int style, float size) {
		return RainyHearts.deriveFont(style,size);
	}
}
