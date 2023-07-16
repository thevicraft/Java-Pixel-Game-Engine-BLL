package com.javapixelgame.game.util;

import com.javapixelgame.game.handling.GameHandler;

public class GameUtil {
	
	public static double getVelocityKMH(int speedpixelpertick) {
		return (GameHandler.getWorld().getMeterLength(speedpixelpertick)*20d) * 3.6d;
	}
	
	public static double getPixelSpeedPerTick(int velocityKMH) {
		return GameHandler.getWorld().getPixelLength((velocityKMH / 3.6d)/20d);
	}
	
	public static double KMHtoMPS(double KMH) {
		return KMH / 3.6d;
	}
	public static double MPStoKMH(double KMH) {
		return KMH * 3.6d;
	}
}
